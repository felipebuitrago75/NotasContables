package com.papelesinteligentes.bbva.notascontables.jsf.nota;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.PartidaPendiente;
import com.papelesinteligentes.bbva.notascontables.dto.Anexo;
import com.papelesinteligentes.bbva.notascontables.dto.CausalDevolucion;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableCrucePartidaPendiente;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableRegistroLibre;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTema;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTotal;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;
import com.papelesinteligentes.bbva.notascontables.jsf.GeneralPage;
import com.papelesinteligentes.bbva.notascontables.jsf.IPages;
import com.papelesinteligentes.bbva.notascontables.jsf.adminnota.PendientePage;
import com.papelesinteligentes.bbva.notascontables.util.EMailSender;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad TipoEvento
 * </p>
 * 
 */
@KeepAlive
public class NotaRefCrucePage extends GeneralPage implements IPages, Serializable {

	private static final long serialVersionUID = 1L;

	protected final EMailSender enviarEMail;

	// nota contable a manejar
	protected NotaContable nota = new NotaContable();
	private String param = "";
	public Integer scrollPage = 1;

	private List<PartidaPendiente> cuentas = new ArrayList<PartidaPendiente>();
	private List<PartidaPendiente> partidasPendientes = new ArrayList<PartidaPendiente>();
	private List<PartidaPendiente> partidasSeleccionadas = new ArrayList<PartidaPendiente>();
	protected ArrayList<NotaContableTotal> totalesNota = new ArrayList<NotaContableTotal>();

	private Integer causalDevolucion = 0;
	private String otraCausalDev = "";

	private boolean  chequeoReasignacion=false;
	
	private int usuariologueado=0;
	
	private int usuarioinstancia=0;
	
	private List<SelectItem> causalesDev = new ArrayList<SelectItem>();

	class CompPartPendPorCuenta implements Comparator<PartidaPendiente> {

		@Override
		public int compare(PartidaPendiente p1, PartidaPendiente p2) {
			return p1.getCuenta().compareTo(p2.getCuenta());
		}

	}

	public NotaRefCrucePage() {
		super();
		enviarEMail = new EMailSender();
	}

	@Override
	public void _init() {
	}

	public String iniciarPagina() {
		try {
			String codSucursal = getUsuarioLogueado().getUsuario().getCodigoAreaModificado();
			// se consultan los datos del usuario para usarlos en la creacion
			String msg = notasContablesManager.verificarUsuarioSubGerente(codSucursal);
			if (msg.isEmpty()) {
				cuentas = new ArrayList<PartidaPendiente>(cargaAltamiraManager.getPartidasPendientesCuentasPorSucursal(codSucursal));
				if (cuentas.isEmpty()) {
					nuevoMensaje(FacesMessage.SEVERITY_INFO, "Actualmente no hay cuentas para cruzar.");
				} else {
					Collections.sort(cuentas, new CompPartPendPorCuenta());
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, msg);
			}
		} catch (Exception e) {
			lanzarError(e, "Error al obtener la información de partidas pendientes");
		}
		return null;
	}

	/**
	 * Funcion llamada al iniciar el flujo de creacion de notas contables
	 * 
	 * @return
	 */
	public String iniciar() {
		return NOTA_REF_CRUCE;
	}

	public String buscarFiltro() {
		try {
			boolean filtro = false;
			for (PartidaPendiente c : cuentas) {
				if (c.getSeleccionada()) {
					filtro = true;
					break;
				}
			}
			partidasPendientes = new ArrayList<PartidaPendiente>();
			Collection<PartidaPendiente> partidas = cargaAltamiraManager.searchPartidaPendientePorSucursal(getUsuarioLogueado().getUsuario().getCodigoAreaModificado(), param);
			if (!partidas.isEmpty()) {
				for (PartidaPendiente p : partidas) {
					if (!partidasSeleccionadas.contains(p)) {
						if (!filtro) {
							partidasPendientes.add(p);
						} else {
							// se verifica el filtro de las cuentas. Esto se hace asi de chambon para soportar lo del desarrollador anterior :(
							for (PartidaPendiente c : cuentas) {
								if (c.getSeleccionada() && c.getCuenta().equals(p.getCuenta())) {
									partidasPendientes.add(p);
									break;
								}
							}
						}
					}
				}
			}
			Collections.sort(partidasPendientes);
		} catch (Exception e) {
			lanzarError(e, "Ocurrió un error al realizar la busqueda por filtro");
		}
		return null;
	}

	public String seleccionar() {
		for (PartidaPendiente p : cuentas) {
			p.setSeleccionada(!p.getSeleccionada());
		}
		return null;
	}

	public String adicionar() {
		try {
			for (PartidaPendiente p : partidasPendientes) {
				// si es la seleccion del usuario
				if (p.getSeleccionada()) {
					// se saca de la lista
					partidasPendientes.remove(p);
					// se marca como usada, se limpia la seleccion y se agrega a la seleccion
					p.setUsada("S");
					p.setSeleccionada(false);
					partidasSeleccionadas.add(p);
					break;
				}
			}
			Collections.sort(partidasSeleccionadas);
			ajustarTotalesNota();
		} catch (Exception e) {
			lanzarError(e, "Error al adicionar la partida seleccionada");
		}
		return null;
	}

	public String remover() {
		try {
			for (PartidaPendiente p : partidasSeleccionadas) {
				// si es la que se desea borrar...
				if (p.getSeleccionada()) {
					// se borra de la lista de seleccionadas
					partidasSeleccionadas.remove(p);
					// se marca como NO usada, se limpia la seleccion y se agrega a la lista de opciones de seleccion
					p.setUsada("N");
					p.setSeleccionada(false);
					partidasPendientes.add(p);
					break;
				}
			}
			ajustarTotalesNota();
		} catch (Exception e) {
			lanzarError(e, "Error al remover la partida seleccionada");
		}
		return null;
	}

	private void ajustarTotalesNota() {
		totalesNota = new ArrayList<NotaContableTotal>();
		for (PartidaPendiente p : partidasSeleccionadas) {
			String negativo = p.getNaturaleza().equalsIgnoreCase("D") ? "" : "-";
			boolean existeDivisa = false;
			for (NotaContableTotal totalNota : totalesNota) {
				if (totalNota.getCodigoDivisa().equals(p.getDivisa())) {
					existeDivisa = true; 
					Double tot = new Double( negativo + (Math.round(p.getImporte()*100)));
					totalNota.setTotal(new Double((Math.round(totalNota.getTotal()*100)) + tot)/100);
					break;
				}
			}
			if (!existeDivisa) {
				NotaContableTotal totalNota = new NotaContableTotal();
				totalNota.setCodigoDivisa(p.getDivisa());
				totalNota.setTotal(new Double( negativo + (Math.round(p.getImporte()*100)))/100);
				totalesNota.add(totalNota);
			}
		}
	}

	public boolean validarRegistros() {
		for (NotaContableTotal total : totalesNota) {
			if (total.getTotal() != 0) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El total para la divisa " + total.getCodigoDivisa() + " debe sumar cero (0)");
			}
		}
		if (partidasSeleccionadas.isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No ha seleccionado ninguna partida contable para registrar la Nota Contable");
		}
		return !getFacesContext().getMessages().hasNext();
	}

	public String guardar() {
		try {
			if (validarRegistros()) {
				if (nota.getCodigo().intValue() <= 0) {
					nota.setCodigoSucursalOrigen(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
					nota.setCodigoTipoEvento(0);
					nota.setTipoNota("C");
					int idInstancia = notasContablesManager.crearInstanciaNotaContable(nota, new ArrayList<NotaContableTema>(), new ArrayList<NotaContableRegistroLibre>(), new ArrayList<Anexo>(), new ArrayList<NotaContableTotal>(),
							partidasSeleccionadas, getCodUsuarioLogueado());
					if (idInstancia != 0) {
						Instancia instancia = new Instancia();
						instancia.setCodigo(idInstancia);
						try {
							instancia = notasContablesManager.getInstancia(instancia);

							NotaContable notaContable = new NotaContable();
							notaContable.setCodigo(instancia.getCodigoNotaContable().intValue());
							notaContable = notasContablesManager.getNotaContable(notaContable);

							int codigoUsuarioAsignado = instancia.getCodigoUsuarioActual().intValue();
							UsuarioModulo usuarioModulo = new UsuarioModulo();
							usuarioModulo.setCodigo(codigoUsuarioAsignado);
							usuarioModulo = notasContablesManager.getUsuarioModulo(usuarioModulo);
							cancelar();
							nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido registrada correctamente con el número de radicación " + notaContable.getNumeroRadicacion());
							try {
								enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(), "Módulo Notas Contables - Registro para aprobar",
										"Por favor ingrese al módulo de Notas Contables, se le ha asignado un registro que requiere su verificación");
							} catch (Exception e) {
								nuevoMensaje(FacesMessage.SEVERITY_INFO, "Ocurrio un error al enviar el correo a la dirección " + usuarioModulo.getEMailModificado());
							}
						} catch (Exception le_e) {

						}
					} else {
						nuevoMensaje(FacesMessage.SEVERITY_ERROR,
								"No se pudo crear la Nota Contable porque no existe ningún usuario con rol 'SubGerente, Gerente o Responsable del Área Central' para la sucursal origen. Consulte al Administrador del sistema.");
					}
				} else {
					nuevoMensaje(FacesMessage.SEVERITY_ERROR, "La información de la nota ya ha sido registrada anteriormente.");
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Error al guardar la información de la nota");
		}
		return null;
	}

	public String cancelar() {
		partidasSeleccionadas = new ArrayList<PartidaPendiente>();
		totalesNota = new ArrayList<NotaContableTotal>();
		nota = new NotaContable();
		buscarFiltro();
		return null;
	}

	public boolean isMostrarAsiento() {
		try {
			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);
			return instancia.getEstado() != null && instancia.getEstado().trim().equals("6");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void consultarFlujo() {
		try {
			Integer codUsuAsignado = nota.getCodUsuAsignado().intValue();
			nota = notasContablesManager.getNotaContable(nota);
			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

			if (codUsuAsignado > 0 && !instancia.getEstado().equals("4") && !instancia.getEstado().equals("5") && !instancia.getEstado().equals("9")) {
				nota.setPuedeAprobar(getCodUsuarioLogueado() == codUsuAsignado.intValue());
			}
			if (codUsuAsignado > 0 && !instancia.getEstado().equals("1") && !instancia.getEstado().equals("9")) {
				nota.setPuedeRechazar(getCodUsuarioLogueado() == codUsuAsignado.intValue());
				causalesDev = getSelectItemList(notasContablesManager.getCV(CausalDevolucion.class), false);
			}
			if (codUsuAsignado > 0 && instancia.getEstado().equals("1")) {
				nota.setPuedeAnular(true);
			}
			partidasSeleccionadas = new ArrayList<PartidaPendiente>();
			Collection<NotaContableCrucePartidaPendiente> temasNota = new ArrayList<NotaContableCrucePartidaPendiente>(notasContablesManager.getCrucesPartidasPendientesPorNotaContable(nota.getCodigo().intValue()));
			for (NotaContableCrucePartidaPendiente n : temasNota) {
				PartidaPendiente p = new PartidaPendiente();
				p.setCuenta(n.getCuentaContable());
				p.setSucursalDestino(n.getCodigoSucursalDestino());
				p.setNaturaleza(n.getNaturaleza());
				p.setDivisa(n.getDivisa());
				p.setReferenciaCruce(n.getReferenciaCruce());
				p.setImporte(n.getImporte());
				p.setConcepto(n.getConcepto());
				p.setFechaContable(n.getFechaContable());
				p.setNumeroAsiento(n.getNumeroAsiento());
				partidasSeleccionadas.add(p);
			}
			ajustarTotalesNota();
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "No se ha podido completar la ejecución solicitada");
		}
	}

	public String aprobar() {
		try {
			
			
			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

			String errorMessage = notasContablesManager.verificarUsuarioSiguienteActividad(instancia, getCodUsuarioLogueado(), true, 0);

			//Codigo nuevo 
			//Para Resolver incidencia que ocurre cuando una
			//Nota Contable Es Aprobada en una misma sesión; en diferentes Navegadores Web
			//lo que ocasionaba que se saltara de Estado; y subsequentemente que notas no se encuentren en Altamira
			this.chequeoReasignacion=true;
			this.usuariologueado =getCodUsuarioLogueado();
			this.usuarioinstancia=instancia.getCodigoUsuarioActual().intValue();
			if(getCodUsuarioLogueado()==instancia.getCodigoUsuarioActual().intValue())
			 {
				chequeoReasignacion=true;
				
			 }else
			 {
				 chequeoReasignacion=false;
			 } //fin codigo
			
			if (errorMessage.equals("")) {
				
				int codigoUsuarioAsignado = 0;
				
				int evaluoActividad=notasContablesManager.siguienteActividad(instancia, new ArrayList<NotaContableTema>(), new ArrayList<NotaContableTotal>(), getCodUsuarioLogueado(), true, 0, null, chequeoReasignacion);
				if(evaluoActividad!=-1)
				{
					 codigoUsuarioAsignado = evaluoActividad;
				}else{
					nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se presentó un error al aprobar la nota: Verfique que el Aplicativo Notas Contables esté  abierto en  un único navegador Web y en una única pestaña.  ");
					
					return null;
				}
				
				
				UsuarioModulo usuarioModulo = new UsuarioModulo();
				usuarioModulo.setCodigo(codigoUsuarioAsignado);
				usuarioModulo = notasContablesManager.getUsuarioModulo(usuarioModulo);
				nota.setPuedeAprobar(false);
				nota.setPuedeRechazar(false);
				nota.setPuedeEditar(false);
				nota.setPuedeAnular(false);
				// se cargan los pendientes
				((PendientePage) getBean("pendientePage")).cargarPendientes();
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido aprobada correctamente");
				try {
					enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(), "Módulo Notas Contables - Registro para aprobar",
							"Por favor ingrese al módulo de Notas Contables, se le ha asignado un registro que requiere su verificación");
				} catch (Exception e) {
					nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se presentó un error al enviar el correo a la dirección: " + usuarioModulo.getEMailModificado());
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_INFO, errorMessage);
			}
		} catch (Exception e) {
			lanzarError(e, "Se presentó un error al aprobar la nota contable");
		}
		return null;
	}

	public String rechazar() {
		try {
			Instancia instanciado = new Instancia();
			instanciado.setCodigoNotaContable(nota.getCodigo());
			instanciado = notasContablesManager.getInstanciaPorNotaContable(instanciado);

			//Codigo nuevo 
			//Para Resolver incidencia que ocurre cuando una
			//Nota Contable Es Aprobada en una misma sesión; en diferentes Navegadores Web
			//lo que ocasionaba que se saltara de Estado; y subsequentemente que notas no se encuentren en Altamira
			this.chequeoReasignacion=true;
			this.usuariologueado =getCodUsuarioLogueado();
			this.usuarioinstancia=instanciado.getCodigoUsuarioActual().intValue();
			if(getCodUsuarioLogueado()==instanciado.getCodigoUsuarioActual().intValue())
			 {
				chequeoReasignacion=true;
				
			 }else
			 {
				 chequeoReasignacion=false;
			 } //fin codigo
			if(chequeoReasignacion)
			{
				validador.validarSeleccion(causalDevolucion, "Causal de devolucion");
				if (causalDevolucion == 18) {// causal: otra
					validador.validarRequerido(otraCausalDev, "Descripción de la causal");
				}
				// si no hay errores de validacion
				if (!getFacesContext().getMessages().hasNext()) {
					Instancia instancia = new Instancia();
					instancia.setCodigoNotaContable(nota.getCodigo());
					instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);
	
					String errorMessage = notasContablesManager.verificarUsuarioSiguienteActividad(instancia, getCodUsuarioLogueado(), false, causalDevolucion);
	
					if (errorMessage.equals("")) {
						int codigoUsuarioAsignado = notasContablesManager.siguienteActividad(instancia, new ArrayList<NotaContableTema>(), new ArrayList<NotaContableTotal>(), getCodUsuarioLogueado(), false, causalDevolucion, otraCausalDev);
	
						UsuarioModulo usuarioModulo = new UsuarioModulo();
						usuarioModulo.setCodigo(codigoUsuarioAsignado);
						usuarioModulo = notasContablesManager.getUsuarioModulo(usuarioModulo);
						// se cargan los pendientes
						nota.setPuedeAprobar(false);
						nota.setPuedeRechazar(false);
						nota.setPuedeEditar(false);
						nota.setPuedeAnular(false);
						((PendientePage) getBean("pendientePage")).cargarPendientes();
						nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido rechazada correctamente");
						try {
							enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(), "Módulo Notas Contables - Registro rechazado",
									"Por favor ingrese al módulo de Notas Contables, se le ha asignado un registro que requiere su verificación");
						} catch (Exception e) {
							nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se presentó un error al enviar el correo a la dirección: " + usuarioModulo.getEMailModificado());
						}
					} else {
						nuevoMensaje(FacesMessage.SEVERITY_INFO, errorMessage);
					}
				}
			}else
			{
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se presentó un error al rechazar la nota: Verfique que el Aplicativo Notas Contables /n esté  abierto en  un único navegador Web y en una única pestaña. ");
				return null;
			}
		} catch (Exception e) {
			lanzarError(e, "Se presentó un error al rechazar la nota contable");
		}
		return null;
	}

	public String anular() {
		try {
			
			//Codigo nuevo 
			Instancia instanciado = new Instancia();
			instanciado.setCodigoNotaContable(nota.getCodigo());
			instanciado = notasContablesManager.getInstanciaPorNotaContable(instanciado);

			//Para Resolver incidencia que ocurre cuando una
			//Nota Contable Es Aprobada en una misma sesión; en diferentes Navegadores Web
			//lo que ocasionaba que se saltara de Estado; y subsequentemente que notas no se encuentren en Altamira
			this.chequeoReasignacion=true;
			this.usuariologueado =getCodUsuarioLogueado();
			this.usuarioinstancia=instanciado.getCodigoUsuarioActual().intValue();
			if(getCodUsuarioLogueado()==instanciado.getCodigoUsuarioActual().intValue())
			 {
				chequeoReasignacion=true;
				
			 }else
			 {
				 chequeoReasignacion=false;
			 } //fin codigo
			if(chequeoReasignacion)
			{
				Instancia instancia = new Instancia();
				instancia.setCodigoNotaContable(nota.getCodigo());
				instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);
	
				notasContablesManager.anularNotaContable(instancia, getCodUsuarioLogueado());
				nota.setPuedeAprobar(false);
				nota.setPuedeRechazar(false);
				nota.setPuedeEditar(false);
				nota.setPuedeAnular(false);
	
				((PendientePage) getBean("pendientePage")).cargarPendientes();
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido anulada correctamente");
			
			}else
			{
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se presentó un error al anular la nota: Verfique que el Aplicativo Notas Contables /n esté  abierto en  un único navegador Web y en una única pestaña. ");
				return null;
			}
		} catch (Exception e) {
			lanzarError(e, "Se presentó un error al anular la nota contable");
		}
		return null;
	}

	public NotaContable getNota() {
		return nota;
	}

	public void setNota(NotaContable nota) {
		this.nota = nota;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Collection<PartidaPendiente> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<PartidaPendiente> cuentas) {
		this.cuentas = cuentas;
	}

	public List<PartidaPendiente> getPartidasPendientes() {
		return partidasPendientes;
	}

	public void setPartidasPendientes(List<PartidaPendiente> partidasPendientes) {
		this.partidasPendientes = partidasPendientes;
	}

	public List<PartidaPendiente> getPartidasSeleccionadas() {
		return partidasSeleccionadas;
	}

	public void setPartidasSeleccionadas(List<PartidaPendiente> partidasSeleccionadas) {
		this.partidasSeleccionadas = partidasSeleccionadas;
	}

	public Integer getScrollPage() {
		if (scrollPage == null) {
			scrollPage = 1;
		}
		return scrollPage;
	}

	public void setScrollPage(Integer srollPage) {
		this.scrollPage = srollPage;
	}

	public int getTotalFilas() {
		return partidasPendientes == null ? 0 : partidasPendientes.size();
	}

	public ArrayList<NotaContableTotal> getTotalesNota() {
		return totalesNota;
	}

	public void setTotalesNota(ArrayList<NotaContableTotal> totalesNota) {
		this.totalesNota = totalesNota;
	}

	public Integer getCausalDevolucion() {
		return causalDevolucion;
	}

	public void setCausalDevolucion(Integer causalDevolucion) {
		this.causalDevolucion = causalDevolucion;
	}

	public String getOtraCausalDev() {
		return otraCausalDev;
	}

	public void setOtraCausalDev(String otraCausalDev) {
		this.otraCausalDev = otraCausalDev;
	}

	public List<SelectItem> getCausalesDev() {
		return causalesDev;
	}

	public void setCausalesDev(List<SelectItem> causalesDev) {
		this.causalesDev = causalesDev;
	}

}
