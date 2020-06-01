package com.papelesinteligentes.bbva.notascontables.jsf.adminnota;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
public class CierrePage extends PrecierreCierrePage {

	private static final long serialVersionUID = -6709113217662690209L;

	private final String TITULO_REPORTE_EXCEL = "Notas en Cierre";

	private static final String INTERFAZ_CONTABLE_FILE_NAME = "ARCHIVO_ENT_INTERCONTA_NTCON.TXT";
	private static final String PUC_FILE_NAME = "ARCHIVO_ENT_PLANCTAS_NTCON.TXT";
	private static final String TERCEROS_FILE_NAME = "ARCHIVO_ENT_CREATERC_NTCON.TXT";
	/**GP 10266 	CREACION ARCHIVO VACIO**/
	private static final String NC_INDICAPC = "ARCHIVO_NC_INDICAPC_NTCON.TXT";

	public CierrePage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
	}

	public String generarArchivoExcel() {
		return ((PrecierreCierrePage) getBean("precierreCierrePage")).generarArchivoExcel(DIR_REPORTES_EXCEL, "NC_CIERRE_", TITULO_REPORTE_EXCEL);
	}

	public String generarArchivoAltamira() {
		return ((PrecierreCierrePage) getBean("precierreCierrePage")).generarArchivoAltamira(DIR_TRANSMISION_ALTAMIRA, INTERFAZ_CONTABLE_FILE_NAME, PUC_FILE_NAME, TERCEROS_FILE_NAME, NC_INDICAPC);
	}

	@Override
	public String mostrar() {
		((PrecierreCierrePage) getBean("precierreCierrePage")).setMostrarArchAlt(false);
		((PrecierreCierrePage) getBean("precierreCierrePage")).setMostrarArchExc(false);
		((PrecierreCierrePage) getBean("precierreCierrePage")).cargarRegistros(false);
		return ADMIN_CIERRE;
	}

	public String getTITULO_REPORTE_EXCEL() {
		return TITULO_REPORTE_EXCEL;
	}

	public String getINTERFAZ_CONTABLE_FILE_NAME() {
		return INTERFAZ_CONTABLE_FILE_NAME;
	}

	public String getPUC_FILE_NAME() {
		return PUC_FILE_NAME;
	}

	public String getTERCEROS_FILE_NAME() {
		return TERCEROS_FILE_NAME;
	}
	public String getNC_INDICAPC() {
		return NC_INDICAPC;
	}

}
