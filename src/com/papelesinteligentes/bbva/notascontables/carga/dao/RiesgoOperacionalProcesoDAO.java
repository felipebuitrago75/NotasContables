/*
	Nombre DTO: RiesgoOperacionalProceso
 */

package com.papelesinteligentes.bbva.notascontables.carga.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import com.papelesinteligentes.bbva.notascontables.carga.dto.RiesgoOperacionalProceso;
import com.papelesinteligentes.bbva.notascontables.dao.CommonDAO;

public class RiesgoOperacionalProcesoDAO extends CommonDAO<RiesgoOperacionalProceso> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_RIES_OPER_PROC";

	private static String cs_CODIGO = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY TO_NUMBER(CODIGO)";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO LIKE ?) OR (UPPER(NOMBRE) LIKE ?) ORDER BY TO_NUMBER(CODIGO)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CODIGO = ?)";

	public RiesgoOperacionalProcesoDAO() {
		super(cs_TABLA, cs_CODIGO, cs_COLUMNAS, new RiesgoOperacionalProceso());
	}

	@Override
	public void internalUpdate( Connection con,RiesgoOperacionalProceso row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCodigo() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<RiesgoOperacionalProceso> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<RiesgoOperacionalProceso> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<RiesgoOperacionalProceso> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, RiesgoOperacionalProceso row) throws Exception {
		return new Object[] { row.getCodigo(), row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public RiesgoOperacionalProceso getResultSetToVO(ResultSet result) throws Exception {
		RiesgoOperacionalProceso row = new RiesgoOperacionalProceso();

		row.setCodigo(result.getString(1));
		row.setNombre(result.getString(2));
		row.setEstadoCarga(result.getString(3));
		row.setFechaUltimaCarga(result.getTimestamp(4));

		return row;
	}
}