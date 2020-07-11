/*
	Nombre DTO: RiesgoOperacional
 */

package com.papelesinteligentes.bbva.notascontables.dao;

import java.sql.Connection;

import java.sql.ResultSet;
import java.util.Collection;

import com.papelesinteligentes.bbva.notascontables.dto.RiesgoOperacional;
import com.papelesinteligentes.bbva.notascontables.dto.Tema;

public class NotaContableTemaRiesgoDAO extends CommonSeqDAO<RiesgoOperacional> {

	//se adicionan campo krb
	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CODIGO_TEMA, IMPORTE_PARCIAL, IMPORTE_TOTAL, FECHA_EVENTO, CODIGO_TIPO_PERDIDA, CODIGO_CLASE_RIESGO, FECHA_DESCUBRIMIENTO, CODIGO_PROCESO, CODIGO_LINEA_OPER, CODIGO_PRODUCTO, " +
			"FECHA_FIN_EVENTO, HORA_INICIO_EVENTO, HORA_FIN_EVENTO, HORA_DESCUBRIMIENTO";

	private static String cs_TABLA = "NC_NOTA_CONTABLE_TEMA_RIESGO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_TEMA_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_AND_TEMA_NOTA_CONTABLE_SENTENCE = "SELECT NC.CODIGO \"codigo\", NC.CODIGO_NOTA_CONTABLE \"codigoNotaContable\", " +
			"NC.CODIGO_TEMA \"codigoTemaNotaContable\", NC.IMPORTE_PARCIAL \"importeParcial\", NC.IMPORTE_TOTAL \"importeTotal\"," +
			"NC.FECHA_EVENTO \"fechaEventoTS\", NC.CODIGO_TIPO_PERDIDA \"codigoTipoPerdida\", NC.CODIGO_CLASE_RIESGO \"codigoClaseRiesgo\", " +
			"NC.FECHA_DESCUBRIMIENTO \"fechaDescubrimientoEventoTS\", NC.CODIGO_PROCESO \"codigoProceso\",  NC.CODIGO_LINEA_OPER \"codigoLineaOperativa\", " +
			"NC.CODIGO_PRODUCTO \"codigoProducto\",NC.FECHA_FIN_EVENTO \"FechaFinalEventoTS\", SUBSTR(NC.HORA_INICIO_EVENTO,0,2) \"horaInicioEvento\", SUBSTR(NC.HORA_INICIO_EVENTO,4,2) \"minutosInicioEvento\"," +
            "SUBSTR(NC.HORA_INICIO_EVENTO,7,3) \"horario\", SUBSTR(NC.hora_fin_evento,0,2) \"horaFinalEvento\", SUBSTR(NC.hora_fin_evento,4,2) \"minutosFinalEvento\", SUBSTR(NC.hora_fin_evento,7,3) \"horariofinal\"," +
		    "SUBSTR(NC.hora_descubrimiento,0,2) \"horaDescubrimiento\", SUBSTR(NC.hora_descubrimiento,4,2) \"minutosDescubrimiento\", SUBSTR(NC.hora_descubrimiento,7,3) \"horariodescubre\"," +
   		    "PER.DESCRIPCION \"tipoPerdida__descripcion\", CLA.NOMBRE \"claseRiesgo__nombre\", " +
			"RIE.NOMBRE \"proceso__nombre\", RIEL.NOMBRE \"lineaOperativa__nombre\", RIEP.NOMBRE \"producto__nombre\", " +
   		    "NC.HORA_INICIO_EVENTO \"horaTotalInicio\", NC.hora_fin_evento \"horaTotalFin\", NC.hora_descubrimiento \"horaTotalDescubre\" " +
			"FROM NC_NOTA_CONTABLE_TEMA_RIESGO NC LEFT JOIN NC_PERDIDA_OPERACION PER ON (PER.CODIGO = NC.CODIGO_TIPO_PERDIDA) LEFT JOIN NC_CLASE_RIESGO CLA ON (CLA.CODIGO = NC.CODIGO_CLASE_RIESGO) LEFT JOIN NC_RIES_OPER_PROC RIE ON (RIE.CODIGO = NC.CODIGO_PROCESO) LEFT JOIN NC_RIES_OPER_LINE_OPER RIEL ON (RIEL.CODIGO = NC.CODIGO_LINEA_OPER) LEFT JOIN NC_RIES_OPER_PROD RIEP ON (RIEP.CODIGO = NC.CODIGO_PRODUCTO) WHERE (NC.CODIGO_NOTA_CONTABLE = ?) AND (NC.CODIGO_TEMA = ?) ORDER BY NC.CODIGO";

		//NC.HORA_INICIO_EVENTO,NC.hora_fin_evento, NC.hora_descubrimiento
	private static String SQL_DELETE_BY_CODIGO_TEMA_SENTENCE = " DELETE FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?)";

	public NotaContableTemaRiesgoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new RiesgoOperacional());
	}

	public void deleteByTemaNotaContable(Connection con, int codigoTemaNotaContable, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_CODIGO_TEMA_SENTENCE, codigoTemaNotaContable);
		/**
		// BLOQUEO BASE DE DATOS
		String xmlDataOriginal = getXMLDataByTemaNotaContable(con, codigoTemaNotaContable);
		if (!xmlDataOriginal.isEmpty()) {
			int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar Riesgo Operacional del tema ", Tema.class.getSimpleName(), "" + codigoTemaNotaContable);
			addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
		}
		**/
	}

	public Collection<RiesgoOperacional> findByTemaNotaContable(int codigoTemaNotaContable) throws Exception {
		return findByGeneral(SQL_SELECT_BY_TEMA_NOTA_CONTABLE_SENTENCE, codigoTemaNotaContable);
	}

	public RiesgoOperacional findByNotaContableAndTemaNotaContable(int codigoNotaContable, int codigoTemaNotaContable) throws Exception {
	
		return obtenerObjeto(SQL_SELECT_BY_NOTA_CONTABLE_AND_TEMA_NOTA_CONTABLE_SENTENCE, codigoNotaContable, codigoTemaNotaContable);
	}

	public String getXMLDataByTemaNotaContable(Connection con, int codigoTemaNotaContable) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_TEMA_NOTA_CONTABLE_SENTENCE, codigoTemaNotaContable);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, RiesgoOperacional row) throws Exception {
		row.setCodigo(getMaxCode(con));
		
		//System.out.println("valor de hora inicio evento OJJJJJJJO"+row.getHoraInicioEvento()+":"+row.getMinutosInicioEvento()+":"+row.getHorario());
		
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCodigoTemaNotaContable(), row.getImporteParcial(), row.getImporteTotal(), row.getFechaEvento(), row.getCodigoTipoPerdida(), row.getCodigoClaseRiesgo(),
				row.getFechaDescubrimientoEvento(), row.getCodigoProceso(), row.getCodigoLineaOperativa(), row.getCodigoProducto(),row.getFechafinEvento(),row.getHoraInicioEvento()+":"+row.getMinutosInicioEvento()+":"+row.getHorario(),
				row.getHoraFinalEvento()+":"+row.getMinutosFinalEvento()+":"+row.getHorariofinal(),row.getHoraDescubrimiento()+":"+row.getMinutosDescubrimiento()+":"+row.getHorariodescubre()};
				//"FECHA_FIN_EVENTO, HORA_INICIO_EVENTO, HORA_FIN_EVENTO, HORA_DESCUBRIMIENTO";
	}

	@Override
	public RiesgoOperacional getResultSetToVO(ResultSet result) throws Exception {
		RiesgoOperacional row = new RiesgoOperacional();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCodigoTemaNotaContable(result.getInt(3));
		row.setImporteParcial(result.getDouble(4));
		row.setImporteTotal(result.getDouble(5));
		row.setFechaEvento(result.getDate(6));
		row.setCodigoTipoPerdida(result.getString(7));
		row.setCodigoClaseRiesgo(result.getString(8));
		row.setFechaDescubrimientoEvento(result.getDate(9));
		row.setCodigoProceso(result.getString(10));
		row.setCodigoLineaOperativa(result.getString(11));
		row.setCodigoProducto(result.getString(12));
		//krb adicion de nuevos campos 
		row.setFechafinEvento(result.getDate(13));
		row.setHoraInicioEvento(result.getString(14));
		row.setHoraFinalEvento(result.getString(15));
		row.setHorariodescubre(result.getString(16));

		return row;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, RiesgoOperacional row) throws Exception {
		throw new Exception("Método no implementado");
	}

	public Collection<RiesgoOperacional> findByNotaContable(int codNota) throws Exception {
		return findByGeneral(SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codNota);
	}
}