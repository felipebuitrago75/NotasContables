package com.papelesinteligentes.bbva.notascontables.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Test {

	public static void main(String[] args) throws FileNotFoundException {
		final JFileChooser fc = new JFileChooser("C:\\Users\\ASUS - ROG\\Desktop\\papeles\\archivos de Carga del 23042012\\NOTAS_CONTABLES");
		int returnVal = fc.showOpenDialog(null);
		String format = "INSERT INTO NC_PAIS (PREFIJO, NOMBRE) VALUES ('%1$s', '%2$s');";
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				String str = sc.nextLine();
				System.out.println(String.format(format, str.substring(0, 3), str.substring(3).trim()));
			}
		}
		// final JFileChooser fc = new JFileChooser("C:/desarrollo/IBM/SDP/workspace/NotasContablesWeb/model/");
		// int returnVal = fc.showOpenDialog(null);
		// if (returnVal == JFileChooser.APPROVE_OPTION) {
		// File file = fc.getSelectedFile();
		// System.out.println("DELETE FROM NC_SUB_MENU_ROL;");
		// System.out.println("COMMIT;");
		// System.out.println("DELETE FROM NC_SUB_MENU;");
		// System.out.println("COMMIT;");
		// System.out.println("DELETE FROM NC_MENU;");
		// System.out.println("COMMIT;");
		// try {
		// Scanner sc = new Scanner(file);
		// while (sc.hasNext()) {
		// String nextLine = sc.nextLine();
		// String[] data = nextLine.split(",");
		// System.out.print("INSERT INTO NC_MENU(CODIGO, ORDEN_VISUAL, NOMBRE) VALUES (");
		// System.out.print(data[0] + ", ");
		// System.out.print(data[1] + ", ");
		// System.out.print("'" + data[2] + "');");
		// System.out.println();
		// System.out.println("COMMIT;");
		// }
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// returnVal = fc.showOpenDialog(null);
		// if (returnVal == JFileChooser.APPROVE_OPTION) {
		// File file = fc.getSelectedFile();
		// try {
		// Scanner sc = new Scanner(file);
		// while (sc.hasNext()) {
		// String nextLine = sc.nextLine();
		// String[] data = nextLine.split(",");
		// System.out.print("INSERT INTO NC_SUB_MENU(CODIGO, ORDEN_VISUAL, NOMBRE, ACCION, CODIGO_MENU) VALUES (");
		// System.out.print(data[0] + ", ");
		// System.out.print(data[1] + ", ");
		// System.out.print("'" + data[2] + "', ");
		// System.out.print("'" + data[3] + "', ");
		// System.out.print(data[4] + ");");
		// System.out.println();
		// System.out.println("COMMIT;");
		// }
		// System.out.println("INSERT INTO NC_SUB_MENU_ROL(CODIGO_SUB_MENU, CODIGO_ROL) SELECT SU.CODIGO, RO.CODIGO FROM NC_SUB_MENU SU, NC_ROL RO;");
		// // System.out.println("INSERT INTO NC_SUB_MENU_ROL(CODIGO_SUB_MENU, CODIGO_ROL) SELECT CODIGO, 7 FROM NC_SUB_MENU;");
		// System.out.println("COMMIT;");
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }

		// String st = "CREATE SEQUENCE %1$s MINVALUE 0 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 100 NOCACHE NOORDER NOCYCLE ;";
		// String stDelete = "DROP SEQUENCE ";
		// String[] tables = new String[] { "SEQ_ACTIVIDAD_ECONOMICA", "SEQ_ACTIVIDAD_REALIZADA", "SEQ_ANEXO", "SEQ_AUDITORIA", "SEQ_AUDITORIA_CARGA", "SEQ_AUDITORIA_DETALLE", "SEQ_BK_ACTIVIDAD_REALIZADA", "SEQ_BK_ANEXO", "SEQ_BK_INSTANCIA",
		// "SEQ_BK_NOTA_CONTABLE", "SEQ_BK_NOTA_CONTABLE_TEMA", "SEQ_BK_NOTA_CONTABLE_TOTAL", "SEQ_BK_NOTA_CONT_REG_LIBRE", "SEQ_BK_NOTA_CONT_TEMA_IMPUESTO", "SEQ_BK_NOTA_CONT_TEMA_RIESGO", "SEQ_BK_NOT_CON_CRU_PAR_PEN", "SEQ_BK_OBSERVACION",
		// "SEQ_BK_RECHAZO_SALIDA", "SEQ_CAUSAL_DEVOLUCION", "SEQ_CENTRO_ESPECIAL", "SEQ_CENTRO_OPE_DES_PYG", "SEQ_CIERRE_MENSUAL", "SEQ_CLASE_RIESGO", "SEQ_CLIENTE", "SEQ_CONCEPTO", "SEQ_CONTRATO", "SEQ_CUENTA_TRANSITORIA", "SEQ_DEPARTAMENTO",
		// "SEQ_DIVISA", "SEQ_ENTE_AUTORIZADOR", "SEQ_ERROR_VALIDACION", "SEQ_EXCEPCION_DIVISA", "SEQ_FECHA_HABILITADA", "SEQ_FESTIVO", "SEQ_HADTAPL", "SEQ_IMPUESTO", "SEQ_INSTANCIA", "SEQ_MENU", "SEQ_MONTO_AUTORIZADO", "SEQ_MONTO_AUT_GENERAL",
		// "SEQ_MONTO_MAXIMO", "SEQ_MUNICIPIO", "SEQ_NOTA_CONTABLE", "SEQ_NOTA_CONTABLE_TEMA", "SEQ_NOTA_CONT_TEMA_IMPUESTO", "SEQ_NOTA_CONTABLE_TEMA_RIESGO", "SEQ_NOTA_CONTABLE_TOTAL", "SEQ_NOTA_CONT_REGISTRO_LIBRE",
		// "SEQ_NOT_CON_CRUCE_PART_PEND", "SEQ_OBSERVACION", "SEQ_PADRINO", "SEQ_PAIS", "SEQ_PARAMETRO", "SEQ_PARTIDA_PENDIENTE", "SEQ_PERDIDA_OPERACION", "SEQ_PERD_OPER_CLAS_RIES", "SEQ_PRODUCTO", "SEQ_PUC", "SEQ_RECHAZO_SALIDA",
		// "SEQ_REGISTRO_CARGA", "SEQ_RIES_OPER_LINE_OPER", "SEQ_RIES_OPER_PROC", "SEQ_RIES_OPER_PROD", "SEQ_ROL", "SEQ_SUB_MENU", "SEQ_SUB_MENU_ROL", "SEQ_SUCURSAL", "SEQ_TEMA", "SEQ_TEMA_AUTORIZACION", "SEQ_TEMA_IMPUESTO",
		// "SEQ_TEMA_PRODUCTO", "SEQ_TERCERO", "SEQ_TIPO_EVENTO", "SEQ_TIPO_IDENTIFICACION", "SEQ_TIPO_INDICATIVO", "SEQ_TIPO_TELEFONO", "SEQ_UNIDAD_ANALISIS", "SEQ_USUARIO", "SEQ_USUARIO_ALTAMIRA" };
		// for (String str : tables) {
		// System.out.println(stDelete + str + ";");
		// }
		// System.out.println("COMMIT;");
		// System.out.println();
		// System.out.println();
		// for (String str : tables) {
		// System.out.println(String.format(st, str));
		// }
		// System.out.println("COMMIT;");
	}
}
