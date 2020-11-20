package com.papelesinteligentes.bbva.notascontables.jsf.nota;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Cliente;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Contrato;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Divisa;
import com.papelesinteligentes.bbva.notascontables.carga.dto.HADTAPL;
import com.papelesinteligentes.bbva.notascontables.carga.dto.PUC;
import com.papelesinteligentes.bbva.notascontables.carga.dto.PartidaPendiente;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalLineaOperativa;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalProceso;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalProducto;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Tercero;
import com.papelesinteligentes.bbva.notascontables.carga.dto.TipoIdentificacion;
import com.papelesinteligentes.bbva.notascontables.dto.Anexo;
import com.papelesinteligentes.bbva.notascontables.dto.FechaHabilitada;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.MontoMaximo;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableRegistroLibre;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTema;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTotal;
import com.papelesinteligentes.bbva.notascontables.dto.RiesgoOperacional;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;
import com.papelesinteligentes.bbva.notascontables.util.StringUtils;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad TipoEvento
 * </p>
 * 
 */
@ViewScoped
public class NotaContableLibrePage extends FlujoNotaContableLibrePage implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int numArchivo = 1;
	private static int numRegistro = -1;

	private String cuentaContable = "";
	private Date fechaNota = null;

	// listas para manejo de combos
	private List<SelectItem> cuentas = new ArrayList<SelectItem>();
	private List<SelectItem> divisas = new ArrayList<SelectItem>();
	private List<SelectItem> sucursalesPartida = new ArrayList<SelectItem>();

	private List<SelectItem> tiposDoc = new ArrayList<SelectItem>();
	private List<SelectItem> contratos1 = new ArrayList<SelectItem>();
	private ArrayList<Date> diasNoHabiles = new ArrayList<Date>();
	private Collection<MontoMaximo> montos = new ArrayList<MontoMaximo>();
	private String minFecha = "";
	private String maxFecha = "";
	private Double montoAlertaCOP = 0d;
	private Double montoAlertaEXT = 0d;
	private List<SelectItem> clasesRiesgo = new ArrayList<SelectItem>();
	private List<SelectItem> perdidaOper = new ArrayList<SelectItem>();
	private List<SelectItem> procesos = new ArrayList<SelectItem>();
	private List<SelectItem> productos = new ArrayList<SelectItem>();
	private List<SelectItem> lineasOperativas = new ArrayList<SelectItem>();

	public NotaContableLibrePage() {
		super();
	}

	public String iniciarPagina() {
		try {
			// se consultan los datos del usuario para usarlos en la creacion
			String codSucursal = getUsuarioLogueado().getSucursal().getCodigo();
			String msg = notasContablesManager.verificarUsuarioSubGerente(codSucursal);
			if (msg.isEmpty()) {
				msg = notasContablesManager.verificarCentroEspecial(codSucursal);
				if (msg.isEmpty()) {
					montos = notasContablesManager.getMontoMaximos();
					for (MontoMaximo monto : montos) {
						if (monto.getEstado().equals("A") && monto.getDivisa().equals("1")) {// local
							montoAlertaCOP = monto.getMontoMaximoAlerta();
						} else if (monto.getEstado().equals("A")) {// extranjera
							montoAlertaEXT = monto.getMontoMaximoAlerta();
						}
					}
					// flujo para determinar la minima fecha de registro de notas contables para la sucursal del usuario
					diasNoHabiles = new ArrayList<Date>(cargaAltamiraManager.getFestivosFecha());

					FechaHabilitada fechaHabilitada = new FechaHabilitada();
					fechaHabilitada.setCodigoSucursal(getUsuarioLogueado().getSucursal().getCodigo());
					fechaHabilitada = notasContablesManager.getFechaHabilitadaPorSucursal(fechaHabilitada);

					// fecha máxima para registro de notas
					maxFecha = StringUtils.getString(DateUtils.getSQLDate(DateUtils.getNextWorkDay(diasNoHabiles)), "dd-MM-yyyy");

					minFecha = StringUtils.getString(DateUtils.getDateTodayBeforeDays(fechaHabilitada.getDias().intValue(), diasNoHabiles), "dd-MM-yyyy");

					if (nota.getCodigo().intValue() > 0) {
						consultarFlujo();
						nota.setPuedeEditar(true);
						nota.setPuedeAnular(true);
						for (NotaContableRegistroLibre tema : temasNotaContable) {
							temaActual = tema;
							fechaNota = temaActual.getFechaContable();
							codigo = tema.getCodigo();
							verNota();
						}
						temaActual = new NotaContableRegistroLibre();
					}
				} else {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, msg);
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, msg);
			}
		} catch (Exception e) {
			lanzarError(e, "Error al obtener la información de conceptos ");
		}
		return null;
	}

	/**
	 * Funcion llamada al iniciar el flujo de creacion de notas contables
	 * 
	 * @return
	 */
	@Override
	public String iniciar() {
		return NOTA_CONTABLE_LIBRE;
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo NotaContable
	 * 
	 * @return
	 */
	public String editar() {
		try {
			cuentaContable = "";
			cuentas = new ArrayList<SelectItem>();
			for (NotaContableRegistroLibre nct : temasNotaContable) {
				if (nct.getCodigo() == codigo.intValue()) {
					temaActual = nct;
					if (codigo > 0) {
						cuentaContable = nct.getCuentaContable();
						buscarCuentaContable();
						if (nct.getPucCuenta().getIndicadorSIRO() != null && nct.getPucCuenta().getIndicadorSIRO().equals("T")) {
							cargarModalRiesgo();
						}
					}
					cargarFiltrosNota();
					return null;
				}
			}
			temaActual = new NotaContableRegistroLibre();
			temaActual.setCodigo(getNumRegistro());
		} catch (Exception e) {
			lanzarError(e, "Error al iniciar el editor de tema");
		}
		return null;
	}

	public String buscarCuentaContable() {
		try {
			if (cuentaContable.length() >= 4) {
				cuentas = new ArrayList<SelectItem>();
				temaActual.setCuentaContable("");
				temaActual.setPucCuenta(new PUC());
				Collection<PUC> pucs = cargaAltamiraManager.searchPUCPorCuenta(cuentaContable);
				if (pucs.isEmpty()) {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontró coincidencia para la cuenta contable " + temaActual.getCuentaContable());
				} else {
					boolean sucValida = false;
					for (PUC p : pucs) {
						sucValida = cargaAltamiraManager.isSucursalValidaPUCOrigen(getUsuarioLogueado().getSucursal(), p);
						if (sucValida) {
							cuentas.add(new SelectItem(p.getNumeroCuenta(), p.getNumeroCuenta() + " " + p.getDescripcion()));
						}
					}
					// se asume que no es un centro autorizado
					if (cuentas.isEmpty()) {
						nuevoMensaje(FacesMessage.SEVERITY_WARN, "Centro no Autorizado para afectar la cuenta " + cuentaContable);
					} else if (cuentas.size() == 1) {
						temaActual.setCuentaContable(pucs.iterator().next().getNumeroCuenta());
						seleccionarCuenta();
					}
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "Por favor indique un número de cuenta de 4 dígitos o mas");
			}
		} catch (Exception e) {
			lanzarError(e, "Error consultando las cuentas contables");
		}
		return null;
	}

	public String seleccionarCuenta() {
		try {
			if (temaActual.getCuentaContable() != null && !temaActual.getCuentaContable().isEmpty()) {
				PUC puc = new PUC();
				puc.setNumeroCuenta(temaActual.getCuentaContable());
				puc = cargaAltamiraManager.getPUC(puc);
				temaActual.setPucCuenta(puc);

				// se determina si la cuenta require contrato
				HADTAPL hadtapl = new HADTAPL();
				hadtapl.setCuentaContable(temaActual.getCuentaContable());
				hadtapl = cargaAltamiraManager.getHADTAPLPorCuenta(hadtapl);
				String cuentaContableS = temaActual.getCuentaContable();
				// si la busqueda para la cuenta especifica requiere contrato
				while (hadtapl.getIndicadorContrato().equals("") && !cuentaContableS.equals("")) {
					hadtapl = new HADTAPL();
					hadtapl.setCuentaContable(cuentaContableS);
					hadtapl = cargaAltamiraManager.getHADTAPLPorCuenta(hadtapl);
					cuentaContableS = cuentaContableS.substring(0,cuentaContableS.length()-1);
				}
				temaActual.setHadtapl(hadtapl);

				// temaActual.setNaturalezaCuentaContable(puc.getNaturaleza());
				cargarFiltrosNota();
			} else {
				temaActual.setCuentaContable("");
				temaActual.setPucCuenta(new PUC());
			}
		} catch (Exception e) {
			lanzarError(e, "Error al consultar la cuenta contable seleccionada");
		}
		return null;
	}

	private void cargarFiltrosNota() throws Exception {
		cargarSucursales();
		cargarDivisas();
		tiposDoc = getSelectItemList(notasContablesManager.getCV(TipoIdentificacion.class), false);
	}

	private void cargarSucursales() throws Exception {
		sucursalesPartida = new ArrayList<SelectItem>();
		String sucUsuario = getUsuarioLogueado().getSucursal().getCodigo();

		boolean sinSucPartida = temaActual.getCodigoSucursalDestino() == null || temaActual.getCodigoSucursalDestino().isEmpty();

		// una vez seleccionada la cuenta, se buscan las sucursales asociadas
		Collection<Sucursal> sucursales = cargaAltamiraManager.getSucursales();
		for (Sucursal s : sucursales) {
			if (cargaAltamiraManager.isSucursalValidaPUCDestino(s, temaActual.getPucCuenta())) {
				FechaHabilitada fecha = new FechaHabilitada();
				fecha.setCodigoSucursal(s.getCodigo());
				fecha = notasContablesManager.getFechaHabilitadaPorSucursal(fecha);

				Date minFechaSuc = DateUtils.getDateTodayBeforeDays(fecha.getDias().intValue(), diasNoHabiles);

				if (minFechaSuc.compareTo(fechaNota) <= 0) {
					// sucursal por defecto para partida
					if (sinSucPartida && s.getCodigo().equals(sucUsuario)) {
						temaActual.setCodigoSucursalDestino(sucUsuario);
					}
					sucursalesPartida.add(new SelectItem(s.getCodigo(), s.getCodigo() + " " + s.getNombre()));
				}
			}
		}
	}

	/**
	 * Carga las divisas a mostrar dependiendo de la nota contable
	 * 
	 * @throws Exception
	 */
	private void cargarDivisas() throws Exception {
		divisas = new ArrayList<SelectItem>();
		TreeSet<Divisa> divSet = new TreeSet<Divisa>(cargaAltamiraManager.getDivisas());
		String tipoDivisa = temaActual.getPucCuenta().getTipoMoneda();
		for (Divisa div : divSet) {
			String codDiv = div.getCodigo();
			boolean indDivisa = false;
			// si el tema indica que no es multidivisa
			if (!tipoDivisa.equals("2")) {
				// si el tema indica que es local y la divisa es de tipo pesos
				if (tipoDivisa.equals("0") && codDiv.equals("COP")) {
					indDivisa = true;
				}
				// si el tema indica que es moneda extranjera y la divisa es diferente a pesos colombianos
				else if (tipoDivisa.equals("1") && !codDiv.equals("COP")) {
					indDivisa = true;
				}
			} else {
				// si es multidivisa se permite cualquier tipo
				indDivisa = true;
			}
			// MODIFICACION COL513513S013857 CARGUE DE DIVISAS POR TIPO DE PARTIDA Y TIPO DE DIVISA
			String tipoPartida = temaActual.getCuentaContable().isEmpty() ? "" : temaActual.getCuentaContable().substring(0, 1);
			/** MODIFICACION DIVISA VALIDA OPCIONES PARA CARGAR EN SELECTITEM **/
			if ((tipoPartida.equals("4") || tipoPartida.equals("5"))) {
				if (tipoDivisa.equals("0") && codDiv.equals("COP")) {
					indDivisa = true;
				}else if (tipoDivisa.equals("1") && codDiv.equals("COD")) {
					indDivisa = true;
				}else if (tipoDivisa.equals("2") &&( codDiv.equals("COD")|| codDiv.equals("COP"))){
					indDivisa = true;
				}else{
					indDivisa = false;
				}
			}
			if (indDivisa) {
				divisas.add(new SelectItem(codDiv, codDiv + " " + div.getNombre()));
			}
		}
	}

	public String validarFecha() {
		/**
		 * INCIDENCIA COL514314I011681 SE VALIDO QUE LA FECHA MAXIMA NO SUPERE LA FECHA DEL DIA EN CASOS 
		 * DE NOTAS CREADAS EN DIAS NO HABILES
		 * */
		java.sql.Date fecha = new java.sql.Date(DateUtils.getDate(maxFecha, "dd-MM-yyyy").getTime());
		Date fechaActual = new Date();
		java.sql.Date fechaAct = DateUtils.getSQLDate(fechaActual);
		// si la fecha es un día no habil, se reinicia
		if (fechaNota.after(DateUtils.getSQLDate(fechaAct))) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha no puede ser superior a HOY");
			fechaNota = fecha;
		} else if
		(fechaNota == null || diasNoHabiles.contains(fechaNota)) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha debe ser un día hábil");
			fechaNota = fecha;
		} else if (fechaNota == null || fechaNota.after(fecha)) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha no puede ser superior a " + StringUtils.getString(fecha, "yyyy-MM-dd"));
			fechaNota = fecha;
		}
		return null;
	}

	public String cargarModalRiesgo() {
		try {
			clasesRiesgo = getSelectItemList(cargaAltamiraManager.getCVClasRiesPorCuenta(temaActual.getCuentaContable()), false);
			if (temaActual.getRiesgoOperacional().getCodigoClaseRiesgo() == null || temaActual.getRiesgoOperacional().getCodigoClaseRiesgo().isEmpty()) {
				perdidaOper = new ArrayList<SelectItem>();
			} else {
				buscarPerdidasCuenta();
			}
			procesos = getSelectItemList(notasContablesManager.getCV(RiesgoOperacionalProceso.class), false);
			productos = getSelectItemList(notasContablesManager.getCV(RiesgoOperacionalProducto.class), false);
			lineasOperativas = getSelectItemList(notasContablesManager.getCV(RiesgoOperacionalLineaOperativa.class), false);
		} catch (Exception e) {
			lanzarError(e, "Error al inicializar el editor de riesgo operacional para la nota actual");
		}
		return null;
	}

	public String buscarPerdidasCuenta() {
		try {
			perdidaOper = getSelectItemList(cargaAltamiraManager.getCVPerdidaOper(temaActual.getCuentaContable(), temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);
		} catch (Exception e) {
			lanzarError(e, "Error al buscar el listado de perdidas operacionales");
		}
		return null;
	}

	public String buscarTercero() {
		buscarTercero(temaActual.getTipoIdentificacion(), temaActual.getNumeroIdentificacion());
		return null;
	}

	private void buscarTercero(String tipo, String numero) {
		try {
			numero = StringUtils.getStringLeftPaddingFixed(numero, 15, '0');
			String numTercero = "";
			String dvTercero = "";
			String nombreTercero = "";
			contratos1 = new ArrayList<SelectItem>();

			// se busca como cliente
			Cliente cliente = new Cliente();
			cliente.setTipoIdentificacion(tipo);
			cliente.setNumeroIdentificacion(numero);
			cliente = cargaAltamiraManager.getClientePorTipoYNumeroIdentificacion(cliente);

			// si existe como cliente:
			if (!cliente.getNumeroIdentificacion().equals("")) {

				// datos básicos del tercero
				numTercero = cliente.getNumeroIdentificacion();
				dvTercero = cliente.getDigitoVerificacion();
				nombreTercero = cliente.getPrimerNombre().trim() + " " + cliente.getPrimerApellido().trim() + " " + cliente.getSegundoApellido().trim();

				// si se requiere contrato, se buscan
				if (temaActual.getHadtapl().getIndicadorContrato().equals("S")) {

					Contrato contrato = new Contrato();
					contrato.setNumeroCliente(cliente.getNumeroCliente());
					Collection<Contrato> contratos = cargaAltamiraManager.getContratosPorCliente(contrato);
					// se crea la lista de items para los contratos encontrados
					for (Contrato c : contratos) {
						contratos1.add(new SelectItem(c.getNumeroContrato(), c.getNumeroContrato()));
					}
				}
			} else {// si no existe como cliente, se busca como tercero
				Tercero tercero = new Tercero();
				tercero.setTipoIdentificacion(tipo);
				tercero.setNumeroIdentificacion(numero);
				tercero = cargaAltamiraManager.getTerceroPorTipoYNumeroIdentificacion(tercero);

				if (!tercero.getTipoIdentificacion().equals("")) {
					// datos básicos del tercero
					numTercero = tercero.getNumeroIdentificacion();
					dvTercero = tercero.getDigitoVerificacion();
					nombreTercero = tercero.getPrimerNombre().trim() + " " + tercero.getPrimerApellido().trim() + " " + tercero.getSegundoApellido().trim();
				} else {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontró ningún cliente ni tercero con la combinación tipo y número de identificación");
				}
			}
			// se asignan los contratos dependiendo del tercero buscado
			temaActual.setNumeroIdentificacion(numTercero);
			temaActual.setDigitoVerificacion(dvTercero);
			temaActual.setNombreCompleto(nombreTercero);
		} catch (Exception e) {
			lanzarError(e, "Ocurrió un error al buscar el tercero con documento " + numero);
		}
	}

	/**
	 * Metodo encargado de cargar los archivos para cada tema
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void listener(UploadEvent event) throws Exception {
		String fileName = "";
		try {
			//System.out.println("INICIANDO PROCESO DE CARGA DE ARCHIVO.....");
			long time = new Date().getTime();
			// se obtiene el archivo a cargar
			UploadItem item = event.getUploadItem();

			String prefijo = getNumArchivo() + "_";

			fileName = (prefijo + time + item.getFileName().substring(item.getFileName().lastIndexOf("."))).toUpperCase();
			//System.out.println("Archivo a cargar: " + fileName);
			// // ruta de destino del archivo
			// File file = item.getFile();
			File localFile = new File(DIR_SOPORTES + fileName);
			// file.renameTo(f);

			final InputStream is = new FileInputStream(item.getFile());
			FileOutputStream fos = null;
			fos = new FileOutputStream(localFile);
			int count = 0;
			int lenght = 1024;
			final byte[] info = new byte[lenght];
			while (is.read(info) != -1) {
				fos.write(info);
				count += lenght;
			}
			is.close();
			fos.close();
			item.getFile().delete();

			System.out.println("************* Archivo cargado en : " + DIR_SOPORTES + fileName);

			// se crea el anexo correspondiente al archivo
			Anexo anexo = new Anexo();
			anexo.setArchivo(fileName);
			anexo.setNombre(item.getFileName());
			anexo.setCodigoUsuario(getCodUsuarioLogueado());
			anexo.setFechaHora(new Timestamp(new Date().getTime()));
			anexo.setBorrar(false);
			anexo.setEstadoInstancia("1");
			anexo.setUsuNombre(getUsuarioLogueado().getUsuAltamira().getNombreEmpleado());
			anexos.add(anexo);

		} catch (Exception e) {
			System.err.println("Error al cargar el archivo...");
			lanzarError(e, "Ocurrió un error al cargar el archivo en: " + DIR_SOPORTES + fileName);
			throw e;
		}
	}

	public String borrarAnexo() {
		for (Anexo anexo : anexos) {
			if (anexo.getBorrar()) {
				// se ubica el anexo y se borra tanto el archivo, como el registro de la lista
				anexos.remove(anexo);
				ServletContext context = (ServletContext) getExternalContext().getContext();
				File f = new File(context.getRealPath(anexo.getArchivo()));
				f.delete();
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "El soporte " + anexo.getNombre() + " se ha borrado correctamente");
				return null;
			}
		}
		return null;
	}

	public String guardarNota() {
		try {
			// validar información requerida
			if (validarNota()) {
				if (nota.getCodigo().intValue() > 0) {
					notasContablesManager.updateNotaContableLibre(nota, anexos, temasNotaContable, getCodUsuarioLogueado());
					aprobar();
				} else {
					nota.setTipoNota("L");// para registro de notas basicas
					nota.setCodigoSucursalOrigen(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());

					// se guarda la información de la nota
					int idInstancia = notasContablesManager
							.crearInstanciaNotaContable(nota, new ArrayList<NotaContableTema>(), temasNotaContable, anexos, new ArrayList<NotaContableTotal>(), new ArrayList<PartidaPendiente>(), getCodUsuarioLogueado());
					if (idInstancia != 0) {
						// se recupera el numero de radicacion de la nota contable
						Instancia instancia = new Instancia();
						instancia.setCodigo(idInstancia);
						instancia = notasContablesManager.getInstancia(instancia);

						NotaContable notaContable = new NotaContable();
						notaContable.setCodigo(instancia.getCodigoNotaContable().intValue());
						notaContable = notasContablesManager.getNotaContable(notaContable);

						// request.setAttribute("idNotaContable", notaContable.getNumeroRadicacion());
						nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido radicada correctamente con el número: " + notaContable.getNumeroRadicacion());
						UsuarioModulo usuarioModulo = new UsuarioModulo();
						try {
							// se envia el correo correspondiente
							int codigoUsuarioAsignado = instancia.getCodigoUsuarioActual().intValue();
							usuarioModulo.setCodigo(codigoUsuarioAsignado);
							usuarioModulo = notasContablesManager.getUsuarioModulo(usuarioModulo);

							enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(), "Módulo Notas Contables - Registro para aprobar",
									"Por favor ingrese al módulo de Notas Contables, se le ha asignado un registro que requiere su verificación");
						} catch (Exception e) {
							lanzarError(e, "Se presentó un error al enviar el correo a la dirección: " + usuarioModulo.getEMailModificado());
						}
						cancelarNota();
					} else {
						nuevoMensaje(FacesMessage.SEVERITY_WARN,
								"No se pudo crear la Nota Contable porque no existe ningún usuario con rol 'SubGerente, Gerente o Responsable del Área Central' para la sucursal origen. Consulte al Administrador del sistema.");
					}
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Ocurrió un error al registrar la nota contable");
		}
		return null;
	}

	private boolean validarNota() {
		validarFecha();

		validador.validarRequerido(nota.getDescripcion(), "Descripción de la nota");
		if (anexos.isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "El registro de contabilidad libre exige soportes, por favor agregue al menos uno");
		}
		for (NotaContableTotal total : totalesNota) {
			if (total.getTotal() != 0) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El total para la divisa " + total.getCodigoDivisa() + " debe sumar cero (0)");
			}
		}
		validarRegistrosTema();
		return !getFacesContext().getMessages().hasNext();
	}

	private boolean validarRegistrosTema() {
		if (temasNotaContable.isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No ha diligenciado la información de ningún registro para la Nota Contable");
			return false;
		}
		validarDuplas();
		validarSucursales();
		return true;
	}

	private void validarSucursales() {
		try {
			for (NotaContableRegistroLibre reg : temasNotaContable) {
				FechaHabilitada fecha = new FechaHabilitada();
				fecha.setCodigoSucursal(reg.getCodigoSucursalDestino());
				fecha = notasContablesManager.getFechaHabilitadaPorSucursal(fecha);
				Date minFechaSuc = DateUtils.getDateTodayBeforeDays(fecha.getDias().intValue(), diasNoHabiles);

				if (minFechaSuc.compareTo(fechaNota) > 0) {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "El centro de destino " + reg.getCodigoSucursalDestino() + " no está habilitado para la fecha contable de la nota");
					break;
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Error validando las sucursales destino por registro");
		}
	}

	private void validarDuplas() {
		HashMap<String, HashMap<Integer, Double>> mapa = new HashMap<String, HashMap<Integer, Double>>();

		for (NotaContableRegistroLibre reg : temasNotaContable) {
			reg.setFechaContable(new java.sql.Date(fechaNota.getTime()));
			int multiplo = reg.getNaturalezaCuentaContable().equalsIgnoreCase("D") ? 1 : -1;
			int cuenta = getCuentaRara(reg.getCuentaContable());

			// si la cuenta necesita verificacion de suma por tupla
			if (cuenta > 0) {
				HashMap<Integer, Double> sumas = mapa.get(reg.getCodigoSucursalDestino());
				if (sumas == null) {
					sumas = new HashMap<Integer, Double>();
				}
				switch (cuenta) {
				case 61:
				case 62:
					sumar(sumas, 6162, reg.getMonto() * multiplo);
					break;
				case 63:
				case 64:
					sumar(sumas, 6364, reg.getMonto() * multiplo);
					break;
				case 81:
				case 83:
					sumar(sumas, 8183, reg.getMonto() * multiplo);
					break;
				case 82:
				case 84:
					sumar(sumas, 8284, reg.getMonto() * multiplo);
					break;
				default:
					break;
				}
				mapa.put(reg.getCodigoSucursalDestino(), sumas);
			}
		}
		// si el mapa tiene datos, se verifica la suma a cero
		if (!mapa.isEmpty()) {
			for (String sucDest : mapa.keySet()) {
				for (int dupla : mapa.get(sucDest).keySet()) {
					if (mapa.get(sucDest).get(dupla).doubleValue() != 0) {
						String duplaString = "" + dupla;
						nuevoMensaje(FacesMessage.SEVERITY_WARN, "Por favor verifique la sumatoria para las cuentas " + duplaString.substring(0, 2) + " - " + duplaString.substring(2, 4) + " de la sucursal de destino " + sucDest);
					}
				}
			}
		}
	}

	private void sumar(HashMap<Integer, Double> sumas, int i, double d) {
		Double valor = sumas.get(i);
		if (valor == null) {
			valor = 0d;
		}
		valor += d;
		sumas.put(i, valor);
	}

	public String cancelarNota() {
		// se reinician los valores
		cuentaContable = "";
		fechaNota = null;
		nota = new NotaContable();
		temaActual = new NotaContableRegistroLibre();
		temasNotaContable = new ArrayList<NotaContableRegistroLibre>();
		totalesNota = new ArrayList<NotaContableTotal>();
		for (Anexo anexo : anexos) {
			// se ubica el anexo y se borra tanto el archivo, como el registro de la lista
			File f = new File(((ServletContext) getExternalContext().getContext()).getRealPath(DIR_SOPORTES + anexo.getArchivo()));
			f.delete();
		}
		anexos = new ArrayList<Anexo>();
		return null;
	}

	public String guardarRiesgo() {
		if (validarRiesgo()) {
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "La información de riesgo operacional se ha guardado temporalmente");
		}
		return null;
	}

	private boolean validarRiesgo() {
		validador.validarPositivo(temaActual.getRiesgoOperacional().getImporteParcial(), "Importe Parcial");
		validador.validarPositivo(temaActual.getRiesgoOperacional().getImporteTotal(), "Importe Total");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaEvento(), "Fecha Inicial del Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaDescubrimientoEvento(), "Fecha del Descubrimiento");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoClaseRiesgo(), "Clase de Riesgo");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoTipoPerdida(), "Tipo Pérdida");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoProceso(), "Proceso");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoLineaOperativa(), "Línea Operativa");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoProducto(), "Producto");
		return !getFacesContext().getMessages().hasNext();
	}

	public String cancelarRiesgo() {
		if (temaActual.getCodigo() <= 0) {
			temaActual.setRiesgoOperacional(new RiesgoOperacional());
		} else {
			try {
				temaActual.setRiesgoOperacional(notasContablesManager.getRiesgoPorNotaContableYTemaNotaContable(temaActual.getCodigoNotaContable(), temaActual.getCodigo()));
			} catch (Exception e) {
				lanzarError(e, "Error recuperando la información de riesgo operacional");
			}
		}
		return null;
	}

	public String guardarTema() {
		if (validarTema()) {
			boolean exist = false;
			for (NotaContableRegistroLibre tema : temasNotaContable) {
				if (tema.getCodigo() == temaActual.getCodigo()) {
					exist = true;
				}
			}
			if (!exist) {
				temasNotaContable.add(temaActual);
			}
			if (temaActual.getCodigo() > 0) {
				temaActual.setEditada(true);
			}
			ajustarTotalesNota();
			temaActual = new NotaContableRegistroLibre();
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "El tema se ha guardado temporalmente");
		}
		return null;
	}
/**
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
	**/
		/** // MODIFICACION COL513513S013857 MODIFICACION DE DECIMALES EN LA NOTA CONTABLE LIBRE  **/
	     private void ajustarTotalesNota() {
			totalesNota = new ArrayList<NotaContableTotal>();
			for (NotaContableRegistroLibre tema : temasNotaContable) {
			if (!tema.getCodigoDivisa().equals("")) {
				boolean existeDivisa = false;
				// se multiplica el valor dependiendo de la naturaleza (debe ->sumar, haber ->restar)
				String multiplo = tema.getNaturalezaCuentaContable().equalsIgnoreCase("D") ? "" : "-";
				existeDivisa = false;
				for (NotaContableTotal totalNota : totalesNota) {
					if (totalNota.getCodigoDivisa().equals(tema.getCodigoDivisa())) {
						existeDivisa = true;
						 
						Double tot = new Double( multiplo + (Math.round(tema.getMonto()*100)));
						totalNota.setTotal(new Double((Math.round(totalNota.getTotal()*100)) + tot)/100);
						break;
					}
				}
				if (!existeDivisa) {
					NotaContableTotal totalNota = new NotaContableTotal();
					totalNota.setCodigoDivisa(tema.getCodigoDivisa());
					totalNota.setTotal(new Double( multiplo + (Math.round(tema.getMonto()*100)))/100);
					totalesNota.add(totalNota);
				}
			}
		}
	 }	

	private int getCuentaRara(String cuenta) {
		cuenta = cuenta.substring(0, 2);
		if (cuenta.startsWith("61") || cuenta.startsWith("62") || cuenta.startsWith("63") || cuenta.startsWith("64") || cuenta.startsWith("81") || cuenta.startsWith("82") || cuenta.startsWith("83") || cuenta.startsWith("84")) {
			return new Integer(cuenta);
		}
		return 0;
	}

	/**
	 * Validaciones asociadas al tema actual
	 * 
	 * @return
	 */
	private boolean validarTema() {
		validador.validarRequerido(temaActual.getCodigoSucursalDestino(), "Sucursal de destino");

		validador.validarRequerido(temaActual.getReferencia(), "Referencia de cruce");

		boolean requiereContrato = temaActual.getHadtapl().getIndicadorContrato().equals("S");
		boolean requiereTercero = temaActual.getPucCuenta().getTipoApunte() != null && temaActual.getPucCuenta().getTipoApunte().equals("T") || temaActual.getPucCuenta().getIndicadorTercero() != null
				&& temaActual.getPucCuenta().getIndicadorTercero().equals("X");

		if (requiereContrato || requiereTercero) {
			validador.validarSeleccion(temaActual.getTipoIdentificacion(), "Tipo de documento del tercero");
			validador.validarRequerido(temaActual.getNumeroIdentificacion(), "Número de documento del tercero");
			if (temaActual.getNombreCompleto().isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "La información del tercero es requerida");
			}
			if (requiereContrato) {
				validador.validarSeleccion(temaActual.getContrato(), "Contrato del tercero");
			}
		}

		for (MontoMaximo monto : montos) {
			if (monto.getDivisa().equals("1") && temaActual.getCodigoDivisa().equals("COP") && temaActual.getMonto() > monto.getMontoMaximo() || !monto.getDivisa().equals("1") && !temaActual.getCodigoDivisa().equals("COP")
					&& temaActual.getMonto() > monto.getMontoMaximo()) {
				NumberFormat f = NumberFormat.getCurrencyInstance();
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El importe supera el monto máximo autorizado (" + f.format(monto.getMontoMaximo()) + ")");
			}
		}

		validador.validarSeleccion(temaActual.getCodigoDivisa(), "Tipo de Divisa");
		validador.validarPositivo(temaActual.getMonto(), "Importe");

		if (temaActual.getPucCuenta().getIndicadorSIRO() != null && temaActual.getPucCuenta().getIndicadorSIRO().equals("T") && temaActual.getRiesgoOperacional().getImporteParcial().doubleValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La cuenta contable requiere información de Riesgo Operacional. Por favor diligenciela");
		}
		return !getFacesContext().getMessages().hasNext();
	}

	public String cancelarTema() {

		for (NotaContableRegistroLibre nct : temasNotaContable) {
			if (nct.getCodigo() == temaActual.getCodigo()) {
				if (nct.getCodigo() <= 0) {
					temasNotaContable.remove(temaActual);
				} else {
					codigo = nct.getCodigo();
					try {
						NotaContableRegistroLibre newReg = new NotaContableRegistroLibre();
						newReg = notasContablesManager.getRegistroLibreNotaContablePorNotaContableYCodigo(temaActual.getCodigoNotaContable(), codigo);
						nct.reset(newReg);
						verNota();
					} catch (Exception e) {
						lanzarError(e, "Error cargando la información previa del registro");
					}
				}
				temaActual = new NotaContableRegistroLibre();
				ajustarTotalesNota();
				break;
			}
		}
		return null;
	}

	private synchronized int getNumArchivo() {
		return numArchivo++;
	}

	private synchronized int getNumRegistro() {
		return numRegistro--;
	}

	@Override
	public NotaContable getNota() {
		return nota;
	}

	@Override
	public void setNota(NotaContable nota) {
		this.nota = nota;
	}

	@Override
	public NotaContableRegistroLibre getTemaActual() {
		return temaActual;
	}

	@Override
	public void setTemaActual(NotaContableRegistroLibre temaActual) {
		this.temaActual = temaActual;
	}

	@Override
	public List<NotaContableRegistroLibre> getTemasNotaContable() {
		return temasNotaContable;
	}

	@Override
	public void setTemasNotaContable(List<NotaContableRegistroLibre> temasNotaContable) {
		this.temasNotaContable = temasNotaContable;
	}

	public List<SelectItem> getDivisas() {
		return divisas;
	}

	public void setDivisas(List<SelectItem> divisas) {
		this.divisas = divisas;
	}

	public List<SelectItem> getSucursalesPartida() {
		return sucursalesPartida;
	}

	public void setSucursalesPartida(List<SelectItem> sucursalesPartida) {
		this.sucursalesPartida = sucursalesPartida;
	}

	public List<SelectItem> getTiposDoc() {
		return tiposDoc;
	}

	public void setTiposDoc(List<SelectItem> tiposDoc) {
		this.tiposDoc = tiposDoc;
	}

	public List<SelectItem> getContratos1() {
		return contratos1;
	}

	public void setContratos1(List<SelectItem> contratos1) {
		this.contratos1 = contratos1;
	}

	public ArrayList<Date> getDiasNoHabiles() {
		return diasNoHabiles;
	}

	public void setDiasNoHabiles(ArrayList<Date> diasNoHabiles) {
		this.diasNoHabiles = diasNoHabiles;
	}

	public String getMinFecha() {
		return minFecha;
	}

	public void setMinFecha(String minFecha) {
		this.minFecha = minFecha;
	}

	public String getMaxFecha() {
		return maxFecha;
	}

	public void setMaxFecha(String maxFecha) {
		this.maxFecha = maxFecha;
	}

	public List<SelectItem> getClasesRiesgo() {
		return clasesRiesgo;
	}

	public void setClasesRiesgo(List<SelectItem> clasesRiesgo) {
		this.clasesRiesgo = clasesRiesgo;
	}

	public List<SelectItem> getPerdidaOper() {
		return perdidaOper;
	}

	public void setPerdidaOper(List<SelectItem> perdidaOper) {
		this.perdidaOper = perdidaOper;
	}

	public List<SelectItem> getProcesos() {
		return procesos;
	}

	public void setProcesos(List<SelectItem> procesos) {
		this.procesos = procesos;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public List<SelectItem> getLineasOperativas() {
		return lineasOperativas;
	}

	public void setLineasOperativas(List<SelectItem> lineasOperativas) {
		this.lineasOperativas = lineasOperativas;
	}

	public List<Anexo> getAnexosTema() {
		return anexos;
	}

	public void setAnexosTema(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	@Override
	public ArrayList<NotaContableTotal> getTotalesNota() {
		return totalesNota;
	}

	@Override
	public void setTotalesNota(ArrayList<NotaContableTotal> totalesNota) {
		this.totalesNota = totalesNota;
	}

	public Date getFechaNota() {
		return fechaNota;
	}

	public void setFechaNota(Date fechaNota) {
		this.fechaNota = fechaNota;
	}

	public List<SelectItem> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<SelectItem> cuentas) {
		this.cuentas = cuentas;
	}

	public Collection<MontoMaximo> getMontos() {
		return montos;
	}

	public void setMontos(Collection<MontoMaximo> montos) {
		this.montos = montos;
	}

	public Double getMontoAlertaCOP() {
		return montoAlertaCOP;
	}

	public void setMontoAlertaCOP(Double montoAlertaCOP) {
		this.montoAlertaCOP = montoAlertaCOP;
	}

	public Double getMontoAlertaEXT() {
		return montoAlertaEXT;
	}

	public void setMontoAlertaEXT(Double montoAlertaEXT) {
		this.montoAlertaEXT = montoAlertaEXT;
	}

}
