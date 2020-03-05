package com.papelesinteligentes.bbva.notascontables.dto;

import java.sql.Date;
import java.sql.Timestamp;

import com.papelesinteligentes.bbva.notascontables.carga.dto.ClaseRiesgo;
import com.papelesinteligentes.bbva.notascontables.carga.dto.PerdidaOperacional;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalLineaOperativa;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalProceso;
import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalProducto;

public class RiesgoOperacional extends CommonVO<RiesgoOperacional> implements java.io.Serializable {

	private static final long serialVersionUID = -6361802696095488112L;
	
	private Number codigo = 0;
	private Number codigoNotaContable = 0;
	private Number codigoTemaNotaContable = 0;
	private Number importeParcial = 0;
	private Number importeTotal = 0;
	private Date fechaEvento = null;
	@SuppressWarnings("unused")
	private final Timestamp fechaEventoTS = null;
	private String codigoTipoPerdida = "";
	private String codigoClaseRiesgo = "";
	private Date fechaDescubrimientoEvento = null;
	private Date fechafinEvento = null;  //se adiciona krb

	
	private String  horaInicioEvento = "";   //se adiciona krb
	private String  minutosInicioEvento =  "";  //se adiciona krb
	private String  horaFinalEvento = "";   //se adiciona krb
	private String  horaTotalInicio = "";  //se adiciona krb
	

	private String  horaTotalFin = "";  //se adiciona krb
	private String  horaTotalDescubre = "";  //se adiciona krb
	

	private String  minutosFinalEvento =  "";  //se adiciona krb
	private String  horaDescubrimiento = "";   //se adiciona krb
	private String  minutosDescubrimiento =  "";  //se adiciona krb
	private String  horario = ""; //se adiciona krb
	private String  horariofinal = ""; //se adiciona krb
	private String  horariodescubre = ""; //se adiciona krb
	

	@SuppressWarnings("unused")
	private final Timestamp fechaDescubrimientoEventoTS = null;
	private final Timestamp FechaFinalEventoTS = null;
	private String codigoProceso = "";
	private String codigoLineaOperativa = "";
	private String codigoProducto = "";

	private PerdidaOperacional tipoPerdida = new PerdidaOperacional();
	private ClaseRiesgo claseRiesgo = new ClaseRiesgo();
	private RiesgoOperacionalProceso proceso = new RiesgoOperacionalProceso();
	private RiesgoOperacionalLineaOperativa lineaOperativa = new RiesgoOperacionalLineaOperativa();
	private RiesgoOperacionalProducto producto = new RiesgoOperacionalProducto();

	public Date getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}
	
	//se adiciona atributo para nueva adicion KRB
	
	public Timestamp getFechaFinalEventoTS() {
		return FechaFinalEventoTS;
	}

	//cambio	
	
	public void setFechaFinalEventoTS(Timestamp FechaFinalEventoTS) {
		this.fechafinEvento = new Date(FechaFinalEventoTS.getTime());
	}

	
	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		
		
			this.horario = horario;
	
	}
	
	public String getHorariofinal() {
		return horariofinal;
	}

	public void setHorariofinal(String horariofinal) {
	
		   this.horariofinal = horariofinal;

	}

	public String getHorariodescubre() {
		return horariodescubre;
	}

	public void setHorariodescubre(String horariodescubre) {
			
	       this.horariodescubre = horariodescubre;
			
	}
	
	public String getHoraTotalInicio() {
		return horaTotalInicio;
	}

	public void setHoraTotalInicio(String horaTotalInicio) {
		this.horaTotalInicio = horaTotalInicio;
	}

	public String getHoraTotalFin() {
		return horaTotalFin;
	}

	public void setHoraTotalFin(String horaTotalFin) {
		this.horaTotalFin = horaTotalFin;
	}

	public String getHoraTotalDescubre() {
		return horaTotalDescubre;
	}

	public void setHoraTotalDescubre(String horaTotalDescubre) {
		this.horaTotalDescubre = horaTotalDescubre;
	}
	
	//fin adicion KRB

	public Timestamp getFechaEventoTS() {
		return new Timestamp(fechaEvento.getTime());
	}

	public void setFechaEventoTS(Timestamp fechaEvento) {
		this.fechaEvento = new Date(fechaEvento.getTime());
	}

	public String getCodigoTipoPerdida() {
		return codigoTipoPerdida;
	}

	public void setCodigoTipoPerdida(String codigoTipoPerdida) {
		this.codigoTipoPerdida = codigoTipoPerdida;
	}

	public String getCodigoClaseRiesgo() {
		return codigoClaseRiesgo;
	}

	public void setCodigoClaseRiesgo(String codigoClaseRiesgo) {
		this.codigoClaseRiesgo = codigoClaseRiesgo;
	}

	public Date getFechaDescubrimientoEvento() {
		return fechaDescubrimientoEvento;
	}

	public void setFechaDescubrimientoEvento(Date fechaDescubrimientoEvento) {
		this.fechaDescubrimientoEvento = fechaDescubrimientoEvento;
	}

	public Timestamp getFechaDescubrimientoEventoTS() {
		return new Timestamp(fechaDescubrimientoEvento.getTime());
	}

	public void setFechaDescubrimientoEventoTS(Timestamp fechaDescubrimientoEvento) {
		this.fechaDescubrimientoEvento = new Date(fechaDescubrimientoEvento.getTime());
	}

	public String getCodigoProceso() {
		return codigoProceso;
	}

	public void setCodigoProceso(String codigoProceso) {
		this.codigoProceso = codigoProceso;
	}

	public String getCodigoLineaOperativa() {
		return codigoLineaOperativa;
	}

	public void setCodigoLineaOperativa(String codigoLineaOperativa) {
		this.codigoLineaOperativa = codigoLineaOperativa;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public PerdidaOperacional getTipoPerdida() {
		return tipoPerdida;
	}

	public void setTipoPerdida(PerdidaOperacional tipoPerdida) {
		this.tipoPerdida = tipoPerdida;
	}

	public ClaseRiesgo getClaseRiesgo() {
		return claseRiesgo;
	}

	public void setClaseRiesgo(ClaseRiesgo claseRiesgo) {
		this.claseRiesgo = claseRiesgo;
	}

	public RiesgoOperacionalProceso getProceso() {
		return proceso;
	}

	public void setProceso(RiesgoOperacionalProceso proceso) {
		this.proceso = proceso;
	}

	public RiesgoOperacionalLineaOperativa getLineaOperativa() {
		return lineaOperativa;
	}

	public void setLineaOperativa(RiesgoOperacionalLineaOperativa lineaOperativa) {
		this.lineaOperativa = lineaOperativa;
	}

	public RiesgoOperacionalProducto getProducto() {
		return producto;
	}

	public void setProducto(RiesgoOperacionalProducto producto) {
		this.producto = producto;
	}

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public Number getCodigoNotaContable() {
		return codigoNotaContable;
	}

	public void setCodigoNotaContable(Number codigoNotaContable) {
		this.codigoNotaContable = codigoNotaContable;
	}

	public Number getCodigoTemaNotaContable() {
		return codigoTemaNotaContable;
	}

	public void setCodigoTemaNotaContable(Number codigoTemaNotaContable) {
		this.codigoTemaNotaContable = codigoTemaNotaContable;
	}

	public Number getImporteParcial() {
		return importeParcial;
	}

	public void setImporteParcial(Number importeParcial) {
		this.importeParcial = importeParcial;
	}

	public Number getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Number importeTotal) {
		this.importeTotal = importeTotal;
	}
	
	//se adicionan krb
	
	public Date getFechafinEvento() {
		return fechafinEvento;
	}

	public void setFechafinEvento(Date fechafinEvento) {
		this.fechafinEvento = fechafinEvento;
	}

	public String getHoraInicioEvento() {
		return horaInicioEvento;
	}

	public void setHoraInicioEvento(String horaInicioEvento) {
		this.horaInicioEvento = horaInicioEvento;
	}

	public String getMinutosInicioEvento() {
		return minutosInicioEvento;
	}

	public void setMinutosInicioEvento(String minutosInicioEvento) {
		this.minutosInicioEvento = minutosInicioEvento;
	}

	public String getHoraFinalEvento() {
		return horaFinalEvento;
	}

	public void setHoraFinalEvento(String horaFinalEvento) {
		this.horaFinalEvento = horaFinalEvento;
	}

	public String getMinutosFinalEvento() {
		return minutosFinalEvento;
	}

	public void setMinutosFinalEvento(String minutosFinalEvento) {
		this.minutosFinalEvento = minutosFinalEvento;
	}

	public String getHoraDescubrimiento() {
		return horaDescubrimiento;
	}

	public void setHoraDescubrimiento(String horaDescubrimiento) {
		this.horaDescubrimiento = horaDescubrimiento;
	}

	public String getMinutosDescubrimiento() {
		return minutosDescubrimiento;
	}

	public void setMinutosDescubrimiento(String minutosDescubrimiento) {
		this.minutosDescubrimiento = minutosDescubrimiento;
	}


	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = new Integer(pk.toString());
	}
}
