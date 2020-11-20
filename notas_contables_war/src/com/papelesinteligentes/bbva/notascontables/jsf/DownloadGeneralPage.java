package com.papelesinteligentes.bbva.notascontables.jsf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletResponse;

@ViewScoped
public class DownloadGeneralPage extends BasePage {

	static final int SOPORTE = 1;
	static final int RECEPCION_ALTAMIRA = 2;
	static final int TRANSMISION_ALTAMIRA = 3;
	static final int REPORTE_EXCEL = 4;
	static final int CARGA = 5;

	private String file;
	private Integer type;

	public DownloadGeneralPage() {
		super();
	}

	public String download() {
		HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
		writeOutContent(response, new File(getPath() + file), file);
		getFacesContext().responseComplete();
		return null;
	}

	void writeOutContent(final HttpServletResponse res, final File content, final String theFilename) {
		if (content == null) {
			return;
		}
		try {
			//System.out.println("Descargando archivo: " + theFilename + "\tExiste: " + content.exists());
			res.setHeader("Pragma", "no-cache");
			res.setDateHeader("Expires", 0);
			res.setContentType("application/force-download");
			res.setHeader("Content-disposition", "attachment; filename=" + theFilename);
			fastChannelCopy(Channels.newChannel(new FileInputStream(content)), Channels.newChannel(res.getOutputStream()));
		} catch (final IOException e) {
			lanzarError(e, "Error descargando el archivo desde " + getPath() + theFilename);
		}
	}

	void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16384);// 16*1024
		while (src.read(buffer) != -1) {
			buffer.flip();
			dest.write(buffer);
			buffer.compact();
		}
		buffer.flip();
		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}

	private String getPath() {

		switch (type) {
		case SOPORTE:
			return DIR_SOPORTES;
		case REPORTE_EXCEL:
			return DIR_REPORTES_EXCEL;
		case RECEPCION_ALTAMIRA:
			return DIR_RECEPCION_ALTAMIRA;
		case TRANSMISION_ALTAMIRA:
			return DIR_TRANSMISION_ALTAMIRA;
		case CARGA:
			return DIR_CARGA;
		}
		return "";
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public int getSOPORTE() {
		return SOPORTE;
	}

	public int getRECEPCION_ALTAMIRA() {
		return RECEPCION_ALTAMIRA;
	}

	public int getTRANSMISION_ALTAMIRA() {
		return TRANSMISION_ALTAMIRA;
	}

	public int getREPORTE_EXCEL() {
		return REPORTE_EXCEL;
	}

	public int getCARGA() {
		return CARGA;
	}

}
