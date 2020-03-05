/*
	Nombre DTO: Auditoria
 */

package com.papelesinteligentes.bbva.notascontables.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import oracle.sql.TIMESTAMP;

import com.papelesinteligentes.bbva.notascontables.dto.Auditoria;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;

public class AuditoriaDAO extends CommonSeqDAO<Auditoria> {

	public AuditoriaDAO() {
		super(IAuditoriaSentence.cs_TABLA, IAuditoriaSentence.cs_COLUMNAS, IAuditoriaSentence.cs_PK, new Auditoria());
	}

	public Collection<Auditoria> findAll() throws Exception {
		return obtenerLista(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Auditoria> findBy(String palabraClave) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		return obtenerLista(SQL_SELECT_BY_SENTENCE, new Object[] { param, param });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Auditoria row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), DateUtils.getTimestamp(), row.getCodigoUsuario(), row.getOperacion(), row.getTipoRegistro(), row.getCodigoRegistro() };
	}

	@Override
	public Auditoria getResultSetToVO(ResultSet result) throws Exception {
		Auditoria row = new Auditoria();

		row.setCodigo(result.getInt(1));
		row.setFechaHora((TIMESTAMP) result.getObject(2));
		row.setCodigoUsuario(result.getInt(3));
		row.setOperacion(result.getString(4));
		row.setTipoRegistro(result.getString(5));
		row.setCodigoRegistro(result.getString(6));

		return row;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") Auditoria row) throws Exception {
		throw new Exception("M�todo no implementado");
	}
}