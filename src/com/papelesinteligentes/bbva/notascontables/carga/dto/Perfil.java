package com.papelesinteligentes.bbva.notascontables.carga.dto;

import com.papelesinteligentes.bbva.notascontables.anotacion.Columna;
import com.papelesinteligentes.bbva.notascontables.anotacion.Tabla;
import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;

@Tabla(nombreTabla = "NC_USUARIO_ALTAMIRA")
public class Perfil extends CommonVO<Perfil> implements java.io.Serializable {

	private static final long serialVersionUID = -3654909311847274997L;

	@Columna(nombreDB = "CODIGO_PERFIL", nombreApp = "codigo", esClave = true)
	private String codigo = "";
	@Columna(nombreDB = "NOMBRE_PERFIL", nombreApp = "nombre", esValor = true)
	private String nombre = "";

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

	@Override
	public Object getPK() {
		return codigo;
	}

}
