package com.papelesinteligentes.bbva.notascontables.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Festivo;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;

public class Test {
	public static void main(String[] args) {
		List<Date> fechas=new ArrayList<Date>();
		fechas.add(new Date());
		Date d=DateUtils.getSQLDate();
		fechas.add(d);
		if(fechas.contains(DateUtils.getSQLDate())){
			System.out.println("si");
		}else{
			System.out.println("no");
		}
		Date a = new Date();
		a.setMonth(11);
		a.setDate(1);
		
		Date b = new Date();
		b.setMonth(12);
		b.setDate(24);
		System.out.println(DateUtils.getFestivosEntre(a,b, new ArrayList<Festivo>()));
	}
}
