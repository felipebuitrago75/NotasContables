/*
	Nombre DTO: RiesgoOperacionalLineaOperativa
 */

package com.papelesinteligentes.bbva.notascontables.carga.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalLineaOperativa;
import com.papelesinteligentes.bbva.notascontables.dao.CommonDAO;

public class RiesgoOperacionalLineaOperativaDAO extends CommonDAO<RiesgoOperacionalLineaOperativa> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_RIES_OPER_LINE_OPER";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY TO_NUMBER(CODIGO)";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO LIKE ?) OR (UPPER(NOMBRE) LIKE ?) ORDER BY TO_NUMBER(CODIGO)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CODIGO = ?)";

	public RiesgoOperacionalLineaOperativaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new RiesgoOperacionalLineaOperativa());
	}

	@Override
	public void internalUpdate( Connection con,RiesgoOperacionalLineaOperativa row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCodigo() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<RiesgoOperacionalLineaOperativa> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<RiesgoOperacionalLineaOperativa> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<RiesgoOperacionalLineaOperativa> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, RiesgoOperacionalLineaOperativa row) throws Exception {
		return new Object[] { row.getCodigo(), row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public RiesgoOperacionalLineaOperativa getResultSetToVO(ResultSet result) throws Exception {
		RiesgoOperacionalLineaOperativa row = new RiesgoOperacionalLineaOperativa();

		row.setCodigo(result.getString(1));
		row.setNombre(result.getString(2));
		row.setEstadoCarga(result.getString(3));
		row.setFechaUltimaCarga(result.getTimestamp(4));

		return row;
	}
}