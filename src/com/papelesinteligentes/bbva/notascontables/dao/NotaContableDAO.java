/*
	Nombre DTO: NotaContable
 */

package com.papelesinteligentes.bbva.notascontables.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;
import com.papelesinteligentes.bbva.notascontables.util.StringUtils;

public class NotaContableDAO extends CommonSeqDAO<NotaContable> {

	private static String cs_COLUMNAS = "CODIGO, FECHA_HORA_REGISTRO_MODULO, FECHA_HORA_REGISTRO_ALTAMIRA, CODIGO_SUCURSAL_ORIGEN, CODIGO_CONCEPTO, CODIGO_TIPO_EVENTO, NUMERO_RADICACION, TIPO_NOTA, DESCRIPCION, ASIENTO_CONTABLE, ESTADO";

	private static String cs_TABLA = "NC_NOTA_CONTABLE";
	private static String cs_PK = "CODIGO";

	private static String cs_SELECT_MAX_CODE_BY_SUCURSAL_ORIGEN_SENTENCE = "SELECT COUNT(*) FROM " + cs_TABLA + " WHERE (NUMERO_RADICACION LIKE ':param%')";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO_SUCURSAL_ORIGEN";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO_SUCURSAL_ORIGEN";

	private static String SQL_SELECT_BY_NUMERO_RADICACION_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NUMERO_RADICACION = ?)";

	private static String SQL_SELECT_BY_ESTADO_INSTANCIA_SENTENCE = "SELECT A.CODIGO, A.FECHA_HORA_REGISTRO_MODULO, A.FECHA_HORA_REGISTRO_ALTAMIRA, A.CODIGO_SUCURSAL_ORIGEN, A.CODIGO_CONCEPTO, A.CODIGO_TIPO_EVENTO, A.NUMERO_RADICACION, A.TIPO_NOTA, A.DESCRIPCION, A.ASIENTO_CONTABLE, A.ESTADO FROM NC_NOTA_CONTABLE A, NC_INSTANCIA B WHERE (A.CODIGO = B.CODIGO_NOTA_CONTABLE) AND (B.ESTADO = ?) ORDER BY A.CODIGO";

	private static String SQL_SELECT_BY_PRECIERRE = "SELECT DISTINCT NC.CODIGO, NC.FECHA_HORA_REGISTRO_MODULO, NC.FECHA_HORA_REGISTRO_ALTAMIRA, NC.CODIGO_SUCURSAL_ORIGEN, NC.CODIGO_CONCEPTO, NC.CODIGO_TIPO_EVENTO, NC.NUMERO_RADICACION, NC.TIPO_NOTA, NC.DESCRIPCION, NC.ASIENTO_CONTABLE, NC.ESTADO FROM NC_NOTA_CONTABLE NC LEFT JOIN NC_INSTANCIA INS ON (INS.CODIGO_NOTA_CONTABLE = NC.CODIGO) WHERE INS.ESTADO = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET FECHA_HORA_REGISTRO_ALTAMIRA = ?, CODIGO_SUCURSAL_ORIGEN = ?, CODIGO_CONCEPTO = ?, CODIGO_TIPO_EVENTO = ?, DESCRIPCION = ?, ESTADO = ? WHERE (CODIGO = ?)";

	private static String SQL_SELECT_BY_PK_SENTENCE = "SELECT NC.CODIGO \"codigo\", NC.FECHA_HORA_REGISTRO_MODULO \"fechaRegistroModulo\", NC.FECHA_HORA_REGISTRO_ALTAMIRA \"fechaRegistroAltamira\", NC.CODIGO_SUCURSAL_ORIGEN \"codigoSucursalOrigen\", NC.CODIGO_CONCEPTO \"codigoConcepto\", NC.CODIGO_TIPO_EVENTO \"codigoTipoEvento\", NC.NUMERO_RADICACION \"numeroRadicacion\", NC.TIPO_NOTA \"tipoNota\", NC.DESCRIPCION \"descripcion\", NC.ASIENTO_CONTABLE \"asientoContable\", NC.ESTADO \"estado\", CO.NOMBRE \"concepto__nombre\", EV.NOMBRE \"tipoEvento__nombre\", SU.NOMBRE \"sucursalOrigen__nombre\" FROM NC_NOTA_CONTABLE NC LEFT JOIN NC_CONCEPTO CO ON (CO.CODIGO = NC.CODIGO_CONCEPTO) LEFT JOIN NC_TIPO_EVENTO EV ON (NC.CODIGO_TIPO_EVENTO = EV.CODIGO) LEFT JOIN NC_SUCURSAL SU ON (SU.CODIGO = NC.CODIGO_SUCURSAL_ORIGEN) WHERE NC.CODIGO = ?";

	private static String sql_SELECT_TO_BACKUP = "SELECT NC.CODIGO, NC.FECHA_HORA_REGISTRO_MODULO, NC.FECHA_HORA_REGISTRO_ALTAMIRA, NC.CODIGO_SUCURSAL_ORIGEN, NC.CODIGO_CONCEPTO, NC.CODIGO_TIPO_EVENTO, NC.NUMERO_RADICACION, NC.TIPO_NOTA, NC.DESCRIPCION, NC.ASIENTO_CONTABLE, NC.ESTADO"
			+ " FROM " + cs_TABLA + " NC LEFT JOIN NC_INSTANCIA INS ON (INS.CODIGO_NOTA_CONTABLE = NC.CODIGO) WHERE INS.ESTADO < 6 AND FECHA_HORA_REGISTRO_MODULO < ?";

	private static String sql_SELECT_ANULADOS = "SELECT NC.CODIGO, NC.FECHA_HORA_REGISTRO_MODULO, NC.FECHA_HORA_REGISTRO_ALTAMIRA, NC.CODIGO_SUCURSAL_ORIGEN, NC.CODIGO_CONCEPTO, NC.CODIGO_TIPO_EVENTO, NC.NUMERO_RADICACION, NC.TIPO_NOTA, NC.DESCRIPCION, NC.ASIENTO_CONTABLE, NC.ESTADO"
			+ " FROM " + cs_TABLA + " NC LEFT JOIN NC_INSTANCIA INS ON (INS.CODIGO_NOTA_CONTABLE = NC.CODIGO) WHERE INS.ESTADO = 9 AND FECHA_HORA_REGISTRO_MODULO < ?";

	private static String sql_SELECT_A_ANULAR = "SELECT NC.CODIGO, NC.FECHA_HORA_REGISTRO_MODULO, NC.FECHA_HORA_REGISTRO_ALTAMIRA, NC.CODIGO_SUCURSAL_ORIGEN, NC.CODIGO_CONCEPTO, NC.CODIGO_TIPO_EVENTO, NC.NUMERO_RADICACION, NC.TIPO_NOTA, NC.DESCRIPCION, NC.ASIENTO_CONTABLE, NC.ESTADO"
			+ " FROM " + cs_TABLA + " NC LEFT JOIN NC_INSTANCIA INS ON (INS.CODIGO_NOTA_CONTABLE = NC.CODIGO) WHERE INS.ESTADO < 6 AND FECHA_HORA_REGISTRO_MODULO < ?";

	public NotaContableDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContable());
	}

	@Override
	public void internalUpdate(Connection con, NotaContable row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getFechaRegistroAltamiraTs(), row.getCodigoSucursalOrigen(), row.getCodigoConcepto(), row.getCodigoTipoEvento(), row.getDescripcion(), row.getEstado(), row.getCodigo() });
	}

	public NotaContable findByNumeroRadicacion(NotaContable row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_NUMERO_RADICACION_SENTENCE, row.getNumeroRadicacion());
	}

	public Collection<NotaContable> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<NotaContable> findByEstado(NotaContable row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<NotaContable> findByPrecierreCierre(boolean esPrecierre) throws Exception {
		return findByGeneral(SQL_SELECT_BY_PRECIERRE, new Object[] { esPrecierre ? "4" : "5" });
	}

	public Collection<NotaContable> findByEstadoInstancia(String estadoInstancia) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_INSTANCIA_SENTENCE, estadoInstancia);
	}

	private int getMaxCodePorSucursalOrigenFecha(Connection con, String numRad) throws Exception {
		return getMaxCode(con, cs_SELECT_MAX_CODE_BY_SUCURSAL_ORIGEN_SENTENCE.replaceAll(":param", numRad));
	}

	@Override
	protected Object[] getDataToAdd(Connection con, NotaContable row) throws Exception {
		row.setCodigo(getMaxCode(con));
		String sucursal = row.getCodigoSucursalOrigen();
		String fecha = StringUtils.getString(DateUtils.getTimestamp(), "ddMMyyyy");
		int consecutivo = getMaxCodePorSucursalOrigenFecha(con, sucursal + fecha);
		String numRadicacion = sucursal + fecha + StringUtils.getStringLeftPadding("" + consecutivo, 3, '0');
		return new Object[] { row.getCodigo(), DateUtils.getTimestamp(), null, row.getCodigoSucursalOrigen(), row.getCodigoConcepto(), row.getCodigoTipoEvento(), numRadicacion, row.getTipoNota(), row.getDescripcion(), row.getAsientoContable(),
				row.getEstado() };
	}

	@Override
	public NotaContable getResultSetToVO(ResultSet result) throws Exception {
		NotaContable row = new NotaContable();

		row.setCodigo(result.getInt(1));
		row.setFechaRegistroModuloTs(result.getTimestamp(2));
		row.setFechaRegistroAltamiraTs(result.getTimestamp(3));
		row.setCodigoSucursalOrigen(result.getString(4));
		row.setCodigoConcepto(result.getInt(5));
		row.setCodigoTipoEvento(result.getInt(6));
		row.setNumeroRadicacion(result.getString(7));
		row.setTipoNota(result.getString(8));
		row.setDescripcion(result.getString(9));
		row.setAsientoContable(result.getString(10));
		row.setEstado(result.getString(11));

		return row;
	}

	@Override
	public NotaContable findByPrimaryKey(NotaContable row) throws Exception {
		return obtenerObjeto(SQL_SELECT_BY_PK_SENTENCE, row.getCodigo());
	}

	public Collection<NotaContable> findToBackup(int cantMeses) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -cantMeses);
		Timestamp ts = new Timestamp(calendar.getTimeInMillis());
		return findByGeneral(sql_SELECT_TO_BACKUP, ts);
	}

	public Collection<NotaContable> findByAnulados(int cantDias) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -cantDias);
		Timestamp ts = new Timestamp(calendar.getTimeInMillis());
		return findByGeneral(sql_SELECT_ANULADOS, ts);
	}

	public Collection<NotaContable> findToAnular(int cantDias) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -cantDias);
		Timestamp ts = new Timestamp(calendar.getTimeInMillis());
		return findByGeneral(sql_SELECT_A_ANULAR, ts);
	}
}