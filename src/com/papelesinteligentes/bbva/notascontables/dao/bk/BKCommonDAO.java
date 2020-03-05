package com.papelesinteligentes.bbva.notascontables.dao.bk;

import com.papelesinteligentes.bbva.notascontables.dao.CommonDAO;
import com.papelesinteligentes.bbva.notascontables.dto.CommonVO;

public abstract class BKCommonDAO<T extends CommonVO<T>> extends CommonDAO<T> {

	protected BKCommonDAO(String tableName, String columnNames, String pkName, T instance) {
		super(tableName, columnNames, pkName, instance);
	}

	@Override
	protected String getJndiDatasourceName() {
		return "jdbc/notasContables";
	}

}
