package com.papelesinteligentes.bbva.notascontables.dto;

import com.papelesinteligentes.bbva.notascontables.anotacion.Columna;
import com.papelesinteligentes.bbva.notascontables.anotacion.ColumnaId;
import com.papelesinteligentes.bbva.notascontables.anotacion.Tabla;

@Tabla(nombreTabla = "NC_TIPO_EVENTO")
public class TipoEvento extends CommonVO<TipoEvento> implements java.io.Serializable {

	private static final long serialVersionUID = -8351719529164157592L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private Number codigo = 0;
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre = "";
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
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
