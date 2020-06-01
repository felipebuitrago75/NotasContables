package com.papelesinteligentes.bbva.notascontables.anotacion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Tabla {

	String nombreTabla();

}
