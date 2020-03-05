package com.papelesinteligentes.bbva.notascontables.dao;

import java.sql.Connection;

import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;

public abstract class CommonSeqDAO<T extends CommonVO<T>> extends CommonDAO<T> {

	protected CommonSeqDAO(String tableName, String columnNames, String pkName, T instance) {
		super(tableName, columnNames, pkName, instance);
	}

	@Override
	protected int getMaxCode(Connection con) throws Exception {
		return getMaxCode(con, sql_SELECT_SEQUENCE_SENTENCE);
	}
}
