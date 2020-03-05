/*
	Nombre DTO: Perfil
 */

package com.papelesinteligentes.bbva.notascontables.carga.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Perfil;
import com.papelesinteligentes.bbva.notascontables.dao.CommonDAO;

public class PerfilDAO extends CommonDAO<Perfil> {

	private static String cs_COLUMNAS = "CODIGO_PERFIL, NOMBRE_PERFIL";

	private static String cs_TABLA = "NC_USUARIO_ALTAMIRA";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT DISTINCT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NOMBRE_PERFIL";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO LIKE ?) OR (NOMBRE LIKE ?)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	private static final String SELECT_CV_STRING = "SELECT DISTINCT " + COMODIN_COLUMNAS + " \nFROM " + COMODIN_TABLA + " \nORDER BY :order";

	public PerfilDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Perfil());
	}

	public Collection<Perfil> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Perfil> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") Perfil row) throws Exception {
		return null;
	}

	@Override
	public Perfil getResultSetToVO(ResultSet result) throws Exception {
		Perfil row = new Perfil();

		row.setCodigo(result.getString(1));
		row.setNombre(result.getString(2));

		return row;
	}

	@Override
	protected String getSelectCVString() {
		return SELECT_CV_STRING;
	}

	@Override
	protected void internalUpdate( @SuppressWarnings("unused") Connection con,@SuppressWarnings("unused") Perfil row) throws Exception {
		throw new Exception("Método no implementado");
	}
}