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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.ajax4jsf.model.KeepAlive;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Cliente;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Contrato;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Divisa;
import com.papelesinteligentes.bbva.notascontables.carga.dto.PartidaPendiente;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalLineaOperativa;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalProceso;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalProducto;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Sucursal;
import com.papelesinteligentes.bbva.notascontables.carga.dto.Tercero;
import com.papelesinteligentes.bbva.notascontables.carga.dto.TipoIdentificacion;
import com.papelesinteligentes.bbva.notascontables.dto.Anexo;
import com.papelesinteligentes.bbva.notascontables.dto.CentroEspecial;
import com.papelesinteligentes.bbva.notascontables.dto.Concepto;
import com.papelesinteligentes.bbva.notascontables.dto.FechaHabilitada;
import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.MontoMaximo;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableRegistroLibre;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTema;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTemaImpuesto;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTotal;
import com.papelesinteligentes.bbva.notascontables.dto.RiesgoOperacional;
import com.papelesinteligentes.bbva.notascontables.dto.Tema;
import com.papelesinteligentes.bbva.notascontables.dto.TemaProducto;
import com.papelesinteligentes.bbva.notascontables.dto.TipoEvento;
import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;
import com.papelesinteligentes.bbva.notascontables.util.StringUtils;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad TipoEvento
 * </p>
 * 
 */
@KeepAlive
public class NotaContablePage extends FlujoNotaContablePage implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int numArchivo = 1;
	
	// listas para manejo de combos
	private List<SelectItem> conceptos = new ArrayList<SelectItem>();
	private List<SelectItem> tiposEvento = new ArrayList<SelectItem>();
	private List<SelectItem> divisas = new ArrayList<SelectItem>();
	private List<SelectItem> sucursalesPartida = new ArrayList<SelectItem>();
	private List<SelectItem> sucursalesContraPartida = new ArrayList<SelectItem>();
	private List<SelectItem> tiposDoc = new ArrayList<SelectItem>();
	private List<SelectItem> contratos1 = new ArrayList<SelectItem>();
	private List<SelectItem> contratos2 = new ArrayList<SelectItem>();
	private List<SelectItem> clasesRiesgo = new ArrayList<SelectItem>();
	private List<SelectItem> perdidaOper = new ArrayList<SelectItem>();
	private List<SelectItem> procesos = new ArrayList<SelectItem>();
	private List<SelectItem> productos = new ArrayList<SelectItem>();
	private List<SelectItem> lineasOperativas = new ArrayList<SelectItem>();
	private List<SelectItem> horas = new ArrayList<SelectItem>(); //krb
	private List<SelectItem> minutos = new ArrayList<SelectItem>(); //krb
	private List<SelectItem> horario = new ArrayList<SelectItem>(); //krb

	private Collection<TemaProducto> productosTema = new ArrayList<TemaProducto>();
	private ArrayList<Date> diasNoHabiles = new ArrayList<Date>();
	private Collection<MontoMaximo> montos = new ArrayList<MontoMaximo>();
	private String minFecha = "";
	private String maxFecha = "";
	private Double montoAlertaCOP = 0d;
	private Double montoAlertaEXT = 0d;
	private Double montoAnt = 0d;
	private String concepto = "";

	public NotaContablePage() {
		super();
	}

	public String iniciarPagina() {
		try {
			// se consultan los datos del usuario para usarlos en la creacion
			Sucursal sucursal = getUsuarioLogueado().getSucursal();
			String msg = notasContablesManager.verificarUsuarioSubGerente(sucursal.getCodigo());
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
				//krb 
				//System.out.println("El valor del codigo de la sucursal ESSSSS en iniciarPagina()"+getUsuarioLogueado().getSucursal().getCodigo());				
				fechaHabilitada.setCodigoSucursal(getUsuarioLogueado().getSucursal().getCodigo());
				fechaHabilitada = notasContablesManager.getFechaHabilitadaPorSucursal(fechaHabilitada);

				// fecha máxima para registro de notas
				maxFecha = StringUtils.getString(DateUtils.getSQLDate(DateUtils.getNextWorkDay(diasNoHabiles)), "dd-MM-yyyy");

				minFecha = StringUtils.getString(DateUtils.getDateTodayBeforeDays(fechaHabilitada.getDias().intValue(), diasNoHabiles), "dd-MM-yyyy");

				// carga de tipos de evento
				tiposEvento = getSelectItemList(notasContablesManager.getCV(TipoEvento.class), false);
				// se consultan los conceptos adecuados segun el usuario actual
				concepto = "";
				
				conceptos = new ArrayList<SelectItem>();
				if (nota.getCodigo().intValue() > 0) {
					consultarFlujo();
					seleccionarConcepto();
					nota.setPuedeEditar(true);
					nota.setPuedeAnular(true);
					conceptos.add(new SelectItem(nota.getConcepto().getCodigo().toString(), nota.getConcepto().getNombre()));
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
		return NOTA_CONTABLE;
	}
	

	public String buscarConcepto() {
		conceptos = new ArrayList<SelectItem>();
		try {
			Sucursal sucursal = getUsuarioLogueado().getSucursal();
			TreeSet<Concepto> set = new TreeSet<Concepto>(notasContablesManager.searchConcepto(concepto, "A"));  
						
			CentroEspecial centroEspecial = getUsuarioLogueado().getCentroEspecial();
			for (Concepto row : set) {
				boolean indView = false;
				if (row.getCentrosAutSucursales().equals("S") && sucursal.getTipoCentro().equals("O")) {
					indView = true;
				} else if (row.getCentrosAutAreasCentrales().equals("S") && sucursal.getTipoCentro().equals("C")) {
					indView = true;
				} else if (row.getCentrosAutCentroEspecial().equals("S") && centroEspecial.getCodigo().intValue() != 0) {
					indView = true;
				}
				if (indView) {
					conceptos.add(new SelectItem(row.getCodigo().toString(), row.getNombre()));
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Error consultando los conceptos");
		}
		return null;
	}

	public String seleccionarConcepto() {
		try {
			// se busca el concepto seleccionado y se asigna
			Concepto concepto = new Concepto();
			concepto.setCodigo(nota.getCodigoConcepto());
			
			concepto = notasContablesManager.getConcepto(concepto);

			nota.setConcepto(concepto);
			temaActual = new NotaContableTema();
			
			
			if (nota.getCodigo().intValue() <= 0) {
				
				totalesNota = new ArrayList<NotaContableTotal>();
				
				//krb 
				//System.out.println("El valor del codigo de la sucursal ESSSSS en seleccionarConcepto()"+getUsuarioLogueado().getSucursal().getCodigo());
				nota.setCodigoSucursalOrigen(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
				// se buscan todos los temas activos asociados al concepto
				Tema t = new Tema();
				t.setCodigoConcepto(nota.getCodigoConcepto());
				t.setEstado("A");
				Collection<Tema> temas = notasContablesManager.getTemasPorConceptoYEstado(t);

				// se crean los temas para la nota actual
				temasNotaContable = new ArrayList<NotaContableTema>();
				int cod = -1;
				
				for (Tema tema : temas) {
					NotaContableTema temaNota = new NotaContableTema();
					crearNotaContableTema(temaNota, tema, cod--, maxFecha);
					
					temasNotaContable.add(temaNota);
				}
				nota.setPuedeEditar(true);
			} else {
				int cod = -1;
				for (NotaContableTema tema : temasNotaContable) {
					temaActual = tema;
					codigo = tema.getCodigo().intValue();
					if (codigo > 0) {
						verNota();
					} else {
						crearNotaContableTema(tema, tema.getTema(), cod--, maxFecha);
					}
				}
				temaActual = new NotaContableTema();
			}
		} catch (Exception e) {
		
			lanzarError(e, "Error al consultar los temas del concepto indicado");
		}
		return null;
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo NotaContable
	 * 
	 * @return
	 */
	public String editar() {
		try {
			for (NotaContableTema nct : temasNotaContable) {
				if (nct.getCodigo().intValue() == codigo.intValue()) {
					temaActual = nct;
					if (temaActual.getCodigo().intValue() > 0 && !temaActual.isEditada()) {
						verNota();
						if (temaActual.getTema().getRiesgoOperacional().equals("S")) {
							cargarModalRiesgo();
						}
						if (temaActual.getTema().getTercero1().equals("S")) {
							buscarTercero1();
						}
						if (temaActual.getTema().getTercero2().equals("S")) {
							buscarTercero2();
						}
					}
					tiposDoc = getSelectItemList(notasContablesManager.getCV(TipoIdentificacion.class), false);

					// se cargan las sucursales de partida y contrapartida
					cargarSucursales();
					// carga las divisas adecuadas para el tema actual
					cargarDivisas();

					// productos asociados al tema actual
					productosTema = notasContablesManager.getProductosPorTema(temaActual.getTema().getCodigo().intValue());
					break;
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Error al iniciar el editor de tema");
		}
		return null;
	}

	private void cargarSucursales() throws Exception {
		sucursalesPartida = new ArrayList<SelectItem>();
		sucursalesContraPartida = new ArrayList<SelectItem>();
		String sucUsuario = getUsuarioLogueado().getSucursal().getCodigo();

		boolean sinSucPartida = temaActual.getCodigoSucursalDestinoPartida() == null || temaActual.getCodigoSucursalDestinoPartida().isEmpty();
		boolean sinSucContraPartida = temaActual.getCodigoSucursalDestinoContraPartida() == null || temaActual.getCodigoSucursalDestinoContraPartida().isEmpty();

		// si no ha indicado datos de sucursal origen y destino

		// si el concepto lo indica, se consultan las sucursales
		if (nota.getConcepto().getOrigenDestino().equals("N")) {
			Collection<Sucursal> sucursales = cargaAltamiraManager.getSucursales();
			for (Sucursal s : sucursales) {
				if (cargaAltamiraManager.isSucursalValidaPUCDestino(s, temaActual.getTema().getPucPartida())) {
					// sucursal por defecto para partida
					if (sinSucPartida && s.getCodigo().equals(sucUsuario)) {
						temaActual.setCodigoSucursalDestinoPartida(sucUsuario);
					}
					sucursalesPartida.add(new SelectItem(s.getCodigo(), s.getCodigo() + " " + s.getNombre()));
				}
				if (cargaAltamiraManager.isSucursalValidaPUCDestino(s, temaActual.getTema().getPucContraPartida())) {
					// sucursal por defecto para contrapartida
					if (sinSucContraPartida && s.getCodigo().equals(sucUsuario)) {
						temaActual.setCodigoSucursalDestinoContraPartida(sucUsuario);
					}
					sucursalesContraPartida.add(new SelectItem(s.getCodigo(), s.getCodigo() + " " + s.getNombre()));
				}
			}
		} else if (sinSucPartida && sinSucContraPartida) {
			// cuando el origen es igual al destino, se asigna la misma sucursal del usuario
			temaActual.setCodigoSucursalDestinoPartida(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
			temaActual.setCodigoSucursalDestinoContraPartida(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
		}
	}
	
	
	

	/**
	 * Carga las divisas a mostrar dependiendo del tipo de tema
	 * 
	 * @throws Exception
	 */
	private void cargarDivisas() throws Exception {
		divisas = new ArrayList<SelectItem>();
		Collection<Divisa> divMap = cargaAltamiraManager.getDivisas();
		String tipoDivisa = temaActual.getTema().getTipoDivisa();
		for (Divisa div : divMap) {
			String codDiv = div.getCodigo();
			boolean indDivisa = false;
			// si el tema indica que no es multidivisa
			if (!tipoDivisa.equals("M")) {
				// si el tema indica que es local y la divisa es de tipo pesos
				if (tipoDivisa.equals("L") && codDiv.equals("COP")) {
					indDivisa = true;
				}
				// si el tema indica que es moneda extranjera y la divisa es diferente a pesos colombianos
				else if (tipoDivisa.equals("E") && !codDiv.equals("COP")) {
					indDivisa = true;
				}
			} else {
				// si es multidivisa se permite cualquier tipo
				indDivisa = true;
			}
			// si la partida contable no es nula
			String tipoPartida = temaActual.getTema().getPartidaContable().isEmpty() ? "" : temaActual.getTema().getPartidaContable().substring(0, 1);
			String tipoContraPartida = temaActual.getTema().getContraPartidaContable().isEmpty() ? "" : temaActual.getTema().getContraPartidaContable().substring(0, 1);
			if ((tipoPartida.equals("4") || tipoPartida.equals("5") || tipoContraPartida.equals("4") || tipoContraPartida.equals("5")) && !tipoDivisa.equals("L")) {
				if (codDiv.equals("COD")) {
					indDivisa = true;
				} else {
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
		if (temaActual.getFechaContable().after(DateUtils.getSQLDate(fechaAct))) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha no puede ser superior a HOY");
			temaActual.setFechaContable(fecha);
		} else if (diasNoHabiles.contains(temaActual.getFechaContable())) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha debe ser un día hábil");
			temaActual.setFechaContable(fecha);
		} else if (temaActual.getFechaContable().after(DateUtils.getDate(fecha))) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha no puede ser superior a " + StringUtils.getString(fecha, "yyyy-MM-dd"));
			temaActual.setFechaContable(fecha);
		}
		return null;
	}

	public String cambiarSucursalPartida() {
		// se cambia el valor de la sucursal de CONTRA-PARTIDA a mismo de la PARTIDA de ser necesario
		if (cambiarSucursal(temaActual.getCodigoSucursalDestinoPartida(), sucursalesContraPartida)) {
			temaActual.setCodigoSucursalDestinoContraPartida(temaActual.getCodigoSucursalDestinoPartida());
		}
		return null;
	}

	public String cambiarSucursalContraPartida() {
		// se cambia el valor de la sucursal de PARTIDA a mismo de la CONTRA PARTIDA de ser necesario
		// if (cambiarSucursal(temaActual.getCodigoSucursalDestinoContraPartida(), sucursalesPartida) ) {
		// temaActual.setCodigoSucursalDestinoPartida(temaActual.getCodigoSucursalDestinoContraPartida());
		// }
		cambiarSucursal(temaActual.getCodigoSucursalDestinoContraPartida(), sucursalesPartida);
		return null;
	}

	public String cambiarContratoPartida() {
		if (temaActual.getNumeroIdentificacion1().equals(temaActual.getNumeroIdentificacion2())) {
			temaActual.setContrato2(temaActual.getContrato1());
		}
		return null;
	}

	public String cambiarContratoContraPartida() {
		if (temaActual.getNumeroIdentificacion1().equals(temaActual.getNumeroIdentificacion2())) {
			temaActual.setContrato1(temaActual.getContrato2());
		}
		return null;
	}

	private boolean cambiarSucursal(String sucursal, List<SelectItem> sucursales) {
		// se determina si el cambio de sucursal aplica a partida y contrapartida
		String partida = temaActual.getTema().getPartidaContable().substring(0, 2);
		String contraPartida = temaActual.getTema().getContraPartidaContable().substring(0, 2);
		if (partida.equals("61") && contraPartida.equals("62") || partida.equals("62") && contraPartida.equals("61")) {
			return true;
		}
		if (partida.equals("63") && contraPartida.equals("64") || partida.equals("64") && contraPartida.equals("63")) {
			return true;
		}
		if (partida.equals("81") && contraPartida.equals("83") || partida.equals("83") && contraPartida.equals("81")) {
			return true;
		}
		if (partida.equals("82") && contraPartida.equals("84") || partida.equals("84") && contraPartida.equals("82")) {
			return true;
		}
		for (SelectItem item : sucursales) {
			if (item.getValue().toString().equals(sucursal)) {
				return true;
			}
		}
		return false;
	}

	public String ajustarCargaImpositiva() {
		for (NotaContableTemaImpuesto imp : temaActual.getImpuestoTema()) {
			// se realiza el calculo
			imp.setBase(temaActual.getMonto().doubleValue());
			imp.setCalculado(imp.getBase() * imp.getImpuestoTema().getImpuesto().getValor() / 100);
			if (imp.getBoolExonera()) {
				imp.setExonera("S");
				imp.setCalculado(0);
				imp.setBase(0);
			} else {
				imp.setExonera("N");
			}
		}
		// if (montoAnt != temaActual.getMonto().doubleValue() && temaActual.getMonto().doubleValue() > (temaActual.getCodigoDivisa().equals("COP") ? montoAlertaCOP : montoAlertaEXT)) {
		// nuevoMensaje(FacesMessage.SEVERITY_WARN, "Por favor verifique si el importe ingresado es el adecuado.");
		// }
		return null;
	}

	public String cargarModalRiesgo() {
		try {
			//CargarHoras();
			
            clasesRiesgo = getSelectItemList(cargaAltamiraManager.getCVClasRiesPorCuenta(temaActual.getTema().getPartidaContable()), false);
			
		
			
			if (temaActual.getRiesgoOperacional().getCodigoClaseRiesgo() == null || temaActual.getRiesgoOperacional().getCodigoClaseRiesgo().isEmpty()) {
				perdidaOper = new ArrayList<SelectItem>();
			
			} else {
				buscarPerdidasCuenta();
			}
			
			  horas= new ArrayList<SelectItem>();
			
			
			 for (int i = 1 ; i<=12; i++){
				
			   String horast = Integer.toString(i);
			 
			 if (i<10){
				 
				 horast = "0"+horast;
				 
			 }
			
			
			 horas.add(new SelectItem(horast, horast));	
			} 
			
			 minutos= new ArrayList<SelectItem>();
			
			
			 for (int j = 0 ; j<=59; j++){
				
			   String mint = Integer.toString(j);
			 
			 if (j<10){
				 
				 mint = "0"+mint;
				 
			 }
		
			 minutos.add(new SelectItem(mint, mint));	
			} 
			
			//horario 
			horario = new ArrayList<SelectItem>();
			horario.add(new SelectItem("a.m","a.m"));
			horario.add(new SelectItem("p.m","p.m"));
	
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
			perdidaOper = getSelectItemList(cargaAltamiraManager.getCVPerdidaOper(temaActual.getTema().getPartidaContable(), temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);
		} catch (Exception e) {
			lanzarError(e, "Error al buscar el listado de perdidas operacionales");
		}
		return null;
	}

	public String buscarTercero1() {
		buscarTercero(temaActual.getTipoIdentificacion1(), temaActual.getNumeroIdentificacion1(), true);
		return null;
	}

	public String buscarTercero2() {
		buscarTercero(temaActual.getTipoIdentificacion2(), temaActual.getNumeroIdentificacion2(), false);
		return null;
	}

	private void buscarTercero(String tipo, String numero, boolean tercero1) {
		try {
			numero = StringUtils.getStringLeftPaddingFixed(numero, 15, '0');
			String numTercero = "";
			String dvTercero = "";
			String nombreTercero = "";
			List<SelectItem> items = new ArrayList<SelectItem>();

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

				// se buscan los contratos y se asignan segun el tipo de tercero buscado
				if (tercero1 && temaActual.getTema().getContrato1().equals("S") || !tercero1 && temaActual.getTema().getContrato2().equals("S")) {
					Contrato contrato = new Contrato();
					contrato.setNumeroCliente(cliente.getNumeroCliente());
					Collection<Contrato> contratos = cargaAltamiraManager.getContratosPorCliente(contrato);

					// se crea la lista de items para los contratos encontrados
					for (Contrato c : contratos) {
						String codProducto = c.getNumeroContrato().substring(8, 10);
						// se verifica si el contrato se debe mostrar
						if (productosTema.isEmpty() || notasContablesManager.isProductoIncluido(productosTema, codProducto)) {
							items.add(new SelectItem(c.getNumeroContrato(), c.getNumeroContrato()));
						}
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
			if (tercero1) {
				temaActual.setNumeroIdentificacion1(numTercero);
				temaActual.setDigitoVerificacion1(dvTercero);
				temaActual.setNombreCompleto1(nombreTercero);
				if (tercero1 && temaActual.getTema().getContrato1().equals("S")) {
					contratos1 = items;
				}
				// si no se ha indicado tercero 2 y es requerido, se asigna el msmo para comodidad del usuario
				if (temaActual.getTema().getTercero2().equals("S") && temaActual.getNombreCompleto2().isEmpty()) {
					temaActual.setTipoIdentificacion2(temaActual.getTipoIdentificacion1());
					temaActual.setNumeroIdentificacion2(numTercero);
					temaActual.setDigitoVerificacion2(dvTercero);
					temaActual.setNombreCompleto2(nombreTercero);
					if (temaActual.getTema().getContrato2().equals("S")) {
						contratos2 = items;
					}
				}
			} else {
				temaActual.setNumeroIdentificacion2(numTercero);
				temaActual.setDigitoVerificacion2(dvTercero);
				temaActual.setNombreCompleto2(nombreTercero);
				if (!tercero1 && temaActual.getTema().getContrato2().equals("S")) {
					contratos2 = items;
				}
			}
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
			// ruta de destino del archivo
			// File file = item.getFile();
			File localFile = new File(DIR_SOPORTES + fileName);
			// localFile.renameTo(f);

			final InputStream is = new FileInputStream(item.getFile());
			FileOutputStream fos = null;
			fos = new FileOutputStream(localFile);
			//TODO contador no se usa en el codigo
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

			//System.out.println("************* Archivo cargado en : " + DIR_SOPORTES + fileName);

			// se crea el anexo correspondiente al archivo
			Anexo anexo = new Anexo();
			anexo.setArchivo(fileName);
			anexo.setNombre(item.getFileName());
			anexo.setCodigoUsuario(getCodUsuarioLogueado());
			anexo.setFechaHora(new Timestamp(new Date().getTime()));
			anexo.setBorrar(false);
			anexo.setEstadoInstancia("1");
			anexo.setUsuNombre(getUsuarioLogueado().getUsuAltamira().getNombreEmpleado());
			temaActual.getAnexoTema().add(anexo);
		} catch (Exception e) {
			System.err.println("Error al cargar el archivo...");
			lanzarError(e, "Ocurrió un error al cargar el archivo en: " + DIR_SOPORTES + fileName);
			throw e;
		}
	}

	public String borrarAnexo() {
		for (Anexo anexo : temaActual.getAnexoTema()) {
			if (anexo.getBorrar()) {
				// se ubica el anexo y se borra tanto el archivo, como el registro de la lista
				temaActual.getAnexoTema().remove(anexo);
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
				// validar totales para la nota completa
				if (validarTotales()) {
					nota.setTipoNota("R");// para registro de notas basicas
					if (nota.getCodigo().intValue() > 0) {
						notasContablesManager.updateNotaContableRegistro(nota, temasNotaContable, totalesNota, getCodUsuarioLogueado());
						aprobar();
					} else {
						// se guarda la información de la nota
						
						
						int idInstancia = notasContablesManager.crearInstanciaNotaContable(nota, temasNotaContable, new ArrayList<NotaContableRegistroLibre>(), new ArrayList<Anexo>(), totalesNota, new ArrayList<PartidaPendiente>(),
								getCodUsuarioLogueado());
						if (idInstancia != 0) {
							// se recupera el numero de radicacion de la nota contable
							Instancia instancia = new Instancia();
							instancia.setCodigo(idInstancia);
							instancia = notasContablesManager.getInstancia(instancia);

							NotaContable notaContable = new NotaContable();
							notaContable.setCodigo(instancia.getCodigoNotaContable().intValue());
							notaContable = notasContablesManager.getNotaContable(notaContable);

							cancelarNota();

							// request.setAttribute("idNotaContable", notaContable.getNumeroRadicacion());
							nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido radicada correctamente con el número: " + notaContable.getNumeroRadicacion());
							UsuarioModulo usuarioModulo = new UsuarioModulo();
							try {
								notasContablesManager.sendMail(instancia, getUsuarioLogueado());
							} catch (Exception e) {
								nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se presentó un error al enviar el correo a la dirección: " + usuarioModulo.getEMailModificado());
							}
						} else {
							nuevoMensaje(FacesMessage.SEVERITY_WARN,
									"No se pudo crear la Nota Contable porque no existe ningún usuario con rol 'SubGerente, Gerente o Responsable del Área Central' para la sucursal origen. Consulte al Administrador del sistema.");
						}
					}
				} else {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No ha diligenciado la información de ningún tema para la Nota Contable");
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Ocurrió un error al registrar la nota contable");
		}
		return null;
	}

	public String cancelarNota() {
		// se reinician los valores
		nota = new NotaContable();
		temaActual = new NotaContableTema();
		temasNotaContable = new ArrayList<NotaContableTema>();
		totalesNota = new ArrayList<NotaContableTotal>();
		return null;
	}

	private boolean validarNota() {
		validador.validarRequerido(nota.getCodigoTipoEvento(), "Tipo de evento");
		validador.validarRequerido(nota.getCodigoConcepto(), "Concepto");
		validarTemasObligatorios();
		return !getFacesContext().getMessages().hasNext();
	}

	private boolean validarTemasObligatorios() {
		for (NotaContableTema tema : temasNotaContable) {
			if (tema.isObligatorio() && tema.getMonto().doubleValue() <= 0) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "No ha diligenciado la información de los temas obligatorios para la Nota Contable");
				return false;
			}
		}
		return true;
	}

	private boolean validarTotales() {
		for (NotaContableTotal notaContableTotal : totalesNota) {
			if (notaContableTotal.getTotal() != 0) {
				return true;
			}
		}
		return false;
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
		validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaEvento(), "Fecha Inicio del Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaDescubrimientoEvento(), "Fecha del Descubrimiento");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoClaseRiesgo(), "Clase de Riesgo");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getFechafinEvento(), "Fecha Finalización del Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraInicioEvento(), "Hora Inicio Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosInicioEvento(), "Minutos Inicio Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraFinalEvento(), "Hora Finalizacion Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosFinalEvento(), "Minutos Fin Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraDescubrimiento(), "Hora Descubrimiento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosDescubrimiento(), "Minutos Descubrimiento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHorario(), "horario fecha inicial");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHorariodescubre(), "horario descubrimiento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHorariofinal(), "horario fecha final");
		//validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoTipoPerdida(), "Tipo Pérdida");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoProceso(), "Proceso");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoLineaOperativa(), "Línea Operativa");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoProducto(), "Producto");
		
		try{
		   if (temaActual.getRiesgoOperacional().getImporteParcial().doubleValue()>temaActual.getRiesgoOperacional().getImporteTotal().doubleValue()){
			
			   validador.DiferencaValor("Importe Parcial","Importe Total");
		   }
		}
		catch(NumberFormatException e){
			e.getMessage();
		}
			
		
		if (temaActual.getRiesgoOperacional().getFechaEvento().after(temaActual.getRiesgoOperacional().getFechafinEvento())){
			
			validador.Diferenciafecha("Fecha Inicio del Evento","Fecha Finalización del Evento");
	 
		}
		
         if (temaActual.getRiesgoOperacional().getFechafinEvento().after(temaActual.getRiesgoOperacional().getFechaDescubrimientoEvento())){
			
			validador.Diferenciafecha("Fecha Finalización del Evento","Fecha del Descubrimiento");
         }
         
         
		//validarFechaMenor(Object va11, Object val2, String fechini, String fechfin)
		//fecha inical vs fecha final 
		//validador.validarFechaMenor(temaActual.getRiesgoOperacional().getFechaEvento(),temaActual.getRiesgoOperacional().getFechafinEvento(), "Fecha Inicio del Evento","Fecha Finalización del Evento");
		//fecha final vs fecha Descubrimiento 
		//validador.validarFechaMenor(temaActual.getRiesgoOperacional().getFechafinEvento(),temaActual.getRiesgoOperacional().getFechaDescubrimientoEvento(), "Fecha Finalización del Evento", "Fecha del Descubrimiento");
		
		
		return !getFacesContext().getMessages().hasNext();
	}

	public String cancelarRiesgo() {
		if (temaActual.getCodigo().intValue() <= 0) {
			temaActual.setRiesgoOperacional(new RiesgoOperacional());
		} else {
			try {
				
				//krb 
				temaActual.setRiesgoOperacional(notasContablesManager.getRiesgoPorNotaContableYTemaNotaContable(temaActual.getCodigoNotaContable().intValue(), temaActual.getCodigo().intValue()));
			} catch (Exception e) {
				
				lanzarError(e, "Error recuperando la información de riesgo operacional");
			}
		}
		return null;
	}

	public String guardarTema() {
		if (validarTema()) {
			temaActual.setEditada(true);
			ajustarCargaImpositiva();
			ajustarTotalesNota();
			temaActual = new NotaContableTema();
			montoAnt = 0d;
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "El tema se ha guardado temporalmente");
		}
		return null;
	}

	/**
	 * Validaciones asociadas al tema actual
	 * 
	 * @return
	 */
	private boolean validarTema() {
		validarFecha();
		validador.validarRequerido(temaActual.getCodigoSucursalDestinoPartida(), "Sucursal destino de la pártida");
		validador.validarRequerido(temaActual.getCodigoSucursalDestinoContraPartida(), "Sucursal destino de la contra pártida");

		if (temaActual.getTema().getReferencia1().equals("S")) {
			validador.validarRequerido(temaActual.getReferencia1(), "Referencia de la pártida");
		}
		if (temaActual.getTema().getReferencia2().equals("S")) {
			validador.validarRequerido(temaActual.getReferencia2(), "Referencia de la contra pártida");
		}
		if (temaActual.getTema().getTercero1().equals("S")) {
			validador.validarSeleccion(temaActual.getTipoIdentificacion1(), "Tipo de documento del tercero asiciado a la pártida");
			validador.validarRequerido(temaActual.getNumeroIdentificacion1(), "Número de documento del tercero asiciado a la pártida");

			if (temaActual.getNombreCompleto1().isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "La información del tercero asociado a la partida es requerida");
			}
			if (temaActual.getTema().getContrato1().equals("S")) {
				validador.validarSeleccion(temaActual.getContrato1(), "Contrato del tercero asiciado a la pártida");
			}
		}
		if (temaActual.getTema().getTercero2().equals("S")) {
			validador.validarSeleccion(temaActual.getTipoIdentificacion2(), "Tipo de documento del tercero asiciado a la contra pártida");
			validador.validarRequerido(temaActual.getNumeroIdentificacion2(), "Número de documento del tercero asiciado a la contra pártida");
			if (temaActual.getNombreCompleto2().isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "La información del tercero asociado a la contra pártida es requerida");
			}
			if (temaActual.getTema().getContrato2().equals("S")) {
				validador.validarSeleccion(temaActual.getContrato2(), "Contrato del tercero asiciado a la contra pártida");
			}
		}
		validador.validarRequerido(temaActual.getDescripcion(), "Descripción");
		validador.validarSeleccion(temaActual.getCodigoDivisa(), "Tipo de Divisa");
		validador.validarPositivo(temaActual.getMonto(), "Importe");

		for (MontoMaximo monto : montos) {
			if (monto.getDivisa().equals("1") && temaActual.getCodigoDivisa().equals("COP") && temaActual.getMonto().doubleValue() > monto.getMontoMaximo() || !monto.getDivisa().equals("1") && !temaActual.getCodigoDivisa().equals("COP")
					&& temaActual.getMonto().doubleValue() > monto.getMontoMaximo()) {
				NumberFormat f = NumberFormat.getCurrencyInstance();
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El importe supera el monto máximo autorizado (" + f.format(monto.getMontoMaximo()) + ")");
			}
		}

		if (temaActual.getTema().getRiesgoOperacional().equals("S")) {
			if (temaActual.getRiesgoOperacional().getImporteParcial().doubleValue() <= 0) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El tema requiere información de Riesgo Operacional");
			}
		}
		if (nota.getConcepto().getSoportes().equals("S") && temaActual.getAnexoTema().isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "El concepto exige soportes para todos los temas, por favor agregue al menos uno");
		}
		return !getFacesContext().getMessages().hasNext();
	}

	/**
	 * Obtiene los totales de la nota agrupados por tipo de divisa
	 */
	private void ajustarTotalesNota() {
		totalesNota = new ArrayList<NotaContableTotal>();
		for (NotaContableTema tema : temasNotaContable) {
			if (!tema.getCodigoDivisa().equals("")) {
				boolean existeDivisa = false;

				for (NotaContableTotal totalNota : totalesNota) {
					if (totalNota.getCodigoDivisa().equals(tema.getCodigoDivisa())) {
						existeDivisa = true;
						totalNota.setTotal(totalNota.getTotal() + tema.getMonto().doubleValue());
						break;
					}
				}
				if (!existeDivisa) {
					NotaContableTotal totalNota = new NotaContableTotal();
					totalNota.setCodigoDivisa(tema.getCodigoDivisa());
					totalNota.setTotal(tema.getMonto().doubleValue());
					totalesNota.add(totalNota);
				}
			}
		}
	}

	public String cancelarTema() {

		ServletContext context = (ServletContext) getExternalContext().getContext();

		for (NotaContableTema nct : temasNotaContable) {
			if (nct.getCodigo().intValue() == temaActual.getCodigo().intValue()) {
				codigo = nct.getCodigo().intValue();
				NotaContableTema newNota = new NotaContableTema();
				if (codigo <= 0) {
					// temasNotaContable.remove(temaActual);
					newNota.setFechaContable(new java.sql.Date(DateUtils.getDate(maxFecha, "dd-MM-yyyy").getTime()));

					// se borran los anexos
					for (Anexo anexo : temaActual.getAnexoTema()) {
						// se ubica el anexo y se borra tanto el archivo, como el registro de la lista
						File f = new File(context.getRealPath(DIR_SOPORTES + anexo.getArchivo()));
						f.delete();
					}

					// se vuelve a crear el tema limpio
					newNota.setAnexoTema(new ArrayList<Anexo>());
					newNota.setRiesgoOperacional(new RiesgoOperacional());

					for (NotaContableTemaImpuesto impuestoTNC : nct.getImpuestoTema()) {
						impuestoTNC.setExonera("N");
						impuestoTNC.setBoolExonera(false);
						impuestoTNC.setBase(0);
						impuestoTNC.setCalculado(0);
						newNota.getImpuestoTema().add(impuestoTNC);
					}
					nct.reset(newNota);
				} else {
					try {
						newNota = notasContablesManager.getTemaNotaContablePorCodigo(temaActual);
						System.out.println("EN CANCELACION HORARIO INICIO"+newNota.getRiesgoOperacional().getHorario());
						System.out.println("EN CANCELACION HORARIO DESCUBRE"+newNota.getRiesgoOperacional().getHorariodescubre());
						nct.reset(newNota);
						verNota();
					} catch (Exception e) {
						lanzarError(e, "Error cargando la información previa del tema");
					}
				}
				temaActual = new NotaContableTema();
				ajustarTotalesNota();
				break;
			}
		}
		return null;
	}

	private synchronized int getNumArchivo() {
		return numArchivo++;
	}

	public List<SelectItem> getHoras() {
		return horas;
	}

	public void setHoras(List<SelectItem> horas) {
		this.horas = horas;
	}

	public List<SelectItem> getConceptos() {
		return conceptos;
	}

	public void setConceptos(List<SelectItem> conceptos) {
		this.conceptos = conceptos;
	}

	public List<SelectItem> getTiposEvento() {
		return tiposEvento;
	}

	public void setTiposEvento(List<SelectItem> tiposEvento) {
		this.tiposEvento = tiposEvento;
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

	public List<SelectItem> getSucursalesContraPartida() {
		return sucursalesContraPartida;
	}

	public void setSucursalesContraPartida(List<SelectItem> sucursalesContraPartida) {
		this.sucursalesContraPartida = sucursalesContraPartida;
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

	public List<SelectItem> getContratos2() {
		return contratos2;
	}

	public void setContratos2(List<SelectItem> contratos2) {
		this.contratos2 = contratos2;
	}

	public Collection<TemaProducto> getProductosTema() {
		return productosTema;
	}

	public void setProductosTema(Collection<TemaProducto> productosTema) {
		this.productosTema = productosTema;
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

	public Collection<MontoMaximo> getMontos() {
		return montos;
	}

	public void setMontos(Collection<MontoMaximo> montos) {
		this.montos = montos;
	}

	public Double getMontoAnt() {
		return montoAnt;
	}

	public void setMontoAnt(Double montoAnt) {
		this.montoAnt = montoAnt;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public List<SelectItem> getMinutos() {
		return minutos;
	}

	public void setMinutos(List<SelectItem> minutos) {
		this.minutos = minutos;
	}

	public List<SelectItem> getHorario() {
		return horario;
	}

	public void setHorario(List<SelectItem> horario) {
		this.horario = horario;
	}

}
