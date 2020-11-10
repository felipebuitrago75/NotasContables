package com.papelesinteligentes.bbva.notascontables.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Festivo;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;

/**
 * 
 * @author Paul Jimenez
 *
 */
public class Test {
	/**
	 * 
	 * <b> Modificar metodos para quitar llamadas deprecadas de Date. </b>
	 * <p>
	 * [Author: Usuario, Date: 10/11/2020]
	 * </p>
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		List<Date> fechas = new ArrayList<Date>();
		fechas.add(new Date());
		Date d = DateUtils.getSQLDate();
		fechas.add(d);
		if (fechas.contains(DateUtils.getSQLDate())) {
			System.out.println("si");
		} else {
			System.out.println("no");
		}
		Date a = new Date();
		a.setMonth(11);
		a.setDate(1);

		Date b = new Date();
		b.setMonth(12);
		b.setDate(24);
		System.out.println(DateUtils.getFestivosEntre(a, b, new ArrayList<Festivo>()));
	}
}
