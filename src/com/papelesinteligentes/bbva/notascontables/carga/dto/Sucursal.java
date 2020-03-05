package com.papelesinteligentes.bbva.notascontables.carga.dto;

import java.sql.Timestamp;

import com.papelesinteligentes.bbva.notascontables.anotacion.Columna;
import com.papelesinteligentes.bbva.notascontables.anotacion.Tabla;
import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;

@Tabla(nombreTabla = "NC_SUCURSAL")
public class Sucursal extends CommonVO<Sucursal> implements java.io.Serializable {

	private static final long serialVersionUID = 4106789564618692731L;
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private String codigo = "";
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre = "";
	private String tipoCentro = "";
	private String codigoCentroSuperior = "";
	private String nombreCentroSuperior = "";
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoCentro() {
		return tipoCentro;
	}

	public void setTipoCentro(String tipoCentro) {
		this.tipoCentro = tipoCentro;
	}

	public String getCodigoCentroSuperior() {
		return codigoCentroSuperior;
	}

	public void setCodigoCentroSuperior(String codigoCentroSuperior) {
		this.codigoCentroSuperior = codigoCentroSuperior;
	}

	public String getNombreCentroSuperior() {
		return nombreCentroSuperior;
	}

	public void setNombreCentroSuperior(String nombreCentroSuperior) {
		this.nombreCentroSuperior = nombreCentroSuperior;
	}

	public String getEstadoCarga() {
		return estadoCarga;
	}

	public void setEstadoCarga(String estadoCarga) {
		this.estadoCarga = estadoCarga;
	}

	public Timestamp getFechaUltimaCarga() {
		return fechaUltimaCarga;
	}

	public void setFechaUltimaCarga(Timestamp fechaUltimaCarga) {
		this.fechaUltimaCarga = fechaUltimaCarga;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

}
