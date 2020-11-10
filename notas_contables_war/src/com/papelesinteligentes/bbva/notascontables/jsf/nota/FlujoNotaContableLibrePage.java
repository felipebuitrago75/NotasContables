package com.papelesinteligentes.bbva.notascontables.jsf.nota;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.HADTAPL;
import com.papelesinteligentes.bbva.notascontables.carga.dto.PUC;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.carga.dto.TipoIdentificacion;
import com.papelesinteligentes.bbva.notascontables.dto.Anexo;
import com.papelesinteligentes.bbva.notascontables.dto.CausalDevolucion;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
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
 * Pagina para manejar la administraci�n de parametros relacionados con la entidad TipoEvento
 * </p>
 * 
 */
@KeepAlive
public class FlujoNotaContableLibrePage extends GeneralPage implements IPages {

	protected final EMailSender enviarEMail;

	// nota contable a manejar
	protected NotaContable nota = new NotaContable();
	// variable para guardar el tema visualizado en un instante dado
	protected NotaContableRegistroLibre temaActual = new NotaContableRegistroLibre();
	// temas asociados a la nota contable
	protected List<NotaContableRegistroLibre> temasNotaContable = new ArrayList<NotaContableRegistroLibre>();
	protected ArrayList<NotaContableTotal> totalesNota = new ArrayList<NotaContableTotal>();
	protected List<Anexo> anexos = new ArrayList<Anexo>();
	protected Instancia instancia = new Instancia();

	private Integer causalDevolucion = 0;
	private String otraCausalDev = "";

	private List<SelectItem> causalesDev = new ArrayList<SelectItem>();

	public FlujoNotaContableLibrePage() {
		super();
		enviarEMail = new EMailSender();
	}

	@Override
	public void _init() {
	}

	public void consultarFlujo() {
		try {
			Integer codUsuAsignado = nota.getCodUsuAsignado().intValue();
			nota = notasContablesManager.getNotaContable(nota);
			instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

			if (codUsuAsignado > 0 && !instancia.getEstado().equals("4") && !instancia.getEstado().equals("5") && !instancia.getEstado().equals("9")) {
				nota.setPuedeAprobar(getCodUsuarioLogueado() == codUsuAsignado.intValue());
			}
			if (codUsuAsignado > 0 && !instancia.getEstado().equals("1") && !instancia.getEstado().equals("9")) {
				nota.setPuedeRechazar(getCodUsuarioLogueado() == codUsuAsignado.intValue());
				causalesDev = getSelectItemList(notasContablesManager.getCV(CausalDevolucion.class), false);
			}

			anexos = new ArrayList<Anexo>(notasContablesManager.getAnexosPorInstancia(nota.getCodigo().intValue()));
			temasNotaContable = new ArrayList<NotaContableRegistroLibre>(notasContablesManager.getRegistrosNotaContableLibre(nota.getCodigo()));
			ajustarTotalesNota();
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "No se ha podido completar la ejecuci�n solicitada");
		}
	}

	private void ajustarTotalesNota() {
		totalesNota = new ArrayList<NotaContableTotal>();
		for (NotaContableRegistroLibre tema : temasNotaContable) {
			if (!tema.getCodigoDivisa().equals("")) {
				boolean existeDivisa = false;
				// se multiplica el valor dependiendo de la naturaleza (debe ->sumar, haber ->restar)
				int multiplo = tema.getNaturalezaCuentaContable().equalsIgnoreCase("D") ? 1 : -1;
				for (NotaContableTotal totalNota : totalesNota) {
					if (totalNota.getCodigoDivisa().equals(tema.getCodigoDivisa())) {
						existeDivisa = true;
						totalNota.setTotal(totalNota.getTotal() + tema.getMonto() * multiplo);
						break;
					}
				}
				if (!existeDivisa) {
					NotaContableTotal totalNota = new NotaContableTotal();
					totalNota.setCodigoDivisa(tema.getCodigoDivisa());
					totalNota.setTotal(tema.getMonto() * multiplo);
					totalesNota.add(totalNota);
				}
			}
		}
	}

	/**
	 * Funcion llamada al iniciar el flujo de creacion de notas contables
	 * 
	 * @return
	 */
	public String iniciar() {
		return NOTA_CONTABLE_LIBRE;
	}

	public String verNota() {
		try {
			// temaActual = notasContablesManager.getTemaNotaContablePorCodigo(temaActual);
			for (NotaContableRegistroLibre nct : temasNotaContable) {
				if (nct.getCodigo() == codigo.intValue()) {
					temaActual = nct;
					if (codigo.intValue() > 0) {
						// se consultan los nombres del tipo deidentificacion
						TipoIdentificacion tipoIdentificacion = new TipoIdentificacion();
						if (!temaActual.getNombreCompleto().isEmpty()) {
							tipoIdentificacion.setCodigo(temaActual.getTipoIdentificacion());
							temaActual.setNombreTipoDoc(cargaAltamiraManager.getTipoIdentificacion(tipoIdentificacion).getNombre());
						}
						Sucursal sucursal = new Sucursal();
						sucursal.setCodigo(temaActual.getCodigoSucursalDestino());
						temaActual.setSucursalDestino(cargaAltamiraManager.getSucursal(sucursal));

						temaActual.setRiesgoOperacional(notasContablesManager.getRiesgoPorNotaContableYTemaNotaContable(nota.getCodigo().intValue(), temaActual.getCodigo()));
						// se verifica la partida y contra partida
						PUC partidaContable = new PUC();
						partidaContable.setNumeroCuenta(temaActual.getCuentaContable());
						partidaContable = cargaAltamiraManager.getPUC(partidaContable);
						temaActual.setPucCuenta(partidaContable);

						// se determina si la cuenta require contrato
						HADTAPL hadtapl = new HADTAPL();
						hadtapl.setCuentaContable(temaActual.getCuentaContable());
						hadtapl = cargaAltamiraManager.getHADTAPLPorCuenta(hadtapl);

						// si la busqueda para la cuenta especifica requiere contrato
						if (!hadtapl.getIndicadorContrato().equals("S")) {
							hadtapl = new HADTAPL();
							hadtapl.setCuentaContable(temaActual.getCuentaContable().substring(0, 4));
							hadtapl = cargaAltamiraManager.getHADTAPLPorCuenta(hadtapl);
						}
						temaActual.setHadtapl(hadtapl);
					}
					break;
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Error al iniciar el editor de tema");
		}
		return null;
	}

	public String aprobar() {
		try {
			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

			String errorMessage = notasContablesManager.verificarUsuarioSiguienteActividad(instancia, getCodUsuarioLogueado(), true, 0);

			if (errorMessage.equals("")) {
				for (NotaContableRegistroLibre tema : temasNotaContable) {
					temaActual = tema;
					// se carga la informacion del tema
					verNota();
				}
				int codigoUsuarioAsignado = notasContablesManager.siguienteActividad(instancia, new ArrayList<NotaContableTema>(), new ArrayList<NotaContableTotal>(),
						getCodUsuarioLogueado(), true, 0, null);

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
					enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(),
							"M�dulo Notas Contables - Registro para aprobar",
							"Por favor ingrese al m�dulo de Notas Contables, se le ha asignado un registro que requiere su verificaci�n ");
				} catch (Exception e) {
					nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se present� un error al enviar el correo a la direcci�n: " + usuarioModulo.getEMailModificado());
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_INFO, errorMessage);
			}
		} catch (Exception e) {
			lanzarError(e, "Se present� un error al aprobar la nota contable");
		}
		return null;
	}

	public String rechazar() {
		try {
			validador.validarSeleccion(causalDevolucion, "Causal de devolucion");
			if (causalDevolucion == 18) {// causal: otra
				validador.validarRequerido(otraCausalDev, "Descripci�n de la causal");
			}
			// si no hay errores de validacion
			if (!getFacesContext().getMessages().hasNext()) {
				Instancia instancia = new Instancia();
				instancia.setCodigoNotaContable(nota.getCodigo());
				instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

				String errorMessage = notasContablesManager.verificarUsuarioSiguienteActividad(instancia, getCodUsuarioLogueado(), false, causalDevolucion);

				if (errorMessage.equals("")) {
					for (NotaContableRegistroLibre tema : temasNotaContable) {
						temaActual = tema;
						// se carga la informacion del tema
						verNota();
					}
					int codigoUsuarioAsignado = notasContablesManager.siguienteActividad(instancia, new ArrayList<NotaContableTema>(), new ArrayList<NotaContableTotal>(),
							getCodUsuarioLogueado(), false, causalDevolucion, otraCausalDev);

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
					causalDevolucion = 0;
					otraCausalDev = "";
					try {
						enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(),
								"M�dulo Notas Contables - Registro rechazado",
								"Por favor ingrese al m�dulo de Notas Contables, se le ha asignado un registro que requiere su verificaci�n");
					} catch (Exception e) {
						nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se present� un error al enviar el correo a la direcci�n: " + usuarioModulo.getEMailModificado());
					}
				} else {
					nuevoMensaje(FacesMessage.SEVERITY_INFO, errorMessage);
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Se present� un error al rechazar la nota contable");
		}
		return null;
	}

	public String anular() {
		try {
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
		} catch (Exception e) {
			lanzarError(e, "Se present� un error al anular la nota contable");
		}
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

	public NotaContable getNota() {
		return nota;
	}

	public void setNota(NotaContable nota) {
		this.nota = nota;
	}

	public NotaContableRegistroLibre getTemaActual() {
		return temaActual;
	}

	public void setTemaActual(NotaContableRegistroLibre temaActual) {
		this.temaActual = temaActual;
	}

	public List<NotaContableRegistroLibre> getTemasNotaContable() {
		return temasNotaContable;
	}

	public void setTemasNotaContable(List<NotaContableRegistroLibre> temasNotaContable) {
		this.temasNotaContable = temasNotaContable;
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

	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	public ArrayList<NotaContableTotal> getTotalesNota() {
		return totalesNota;
	}

	public void setTotalesNota(ArrayList<NotaContableTotal> totalesNota) {
		this.totalesNota = totalesNota;
	}

}
