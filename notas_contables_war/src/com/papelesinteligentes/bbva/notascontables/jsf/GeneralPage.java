package com.papelesinteligentes.bbva.notascontables.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.papelesinteligentes.bbva.notascontables.jsf.beans.ContablesApplicationBean;
import com.papelesinteligentes.bbva.notascontables.jsf.beans.ContablesRequestBean;

public abstract class GeneralPage extends BasePage {


	protected abstract void _init() throws Exception;

	protected Integer codigo = 0;

	public GeneralPage() {
		super();
	}

	/**
	 * <p>
	 * Callback method that is called whenever a page is navigated to, either directly via a URL, or indirectly via page navigation. Customize this method to acquire resources that will be needed for event handlers and lifecycle methods, whether or
	 * not this page is performing post back processing.
	 * </p>
	 * 
	 * <p>
	 * Note that, if the current request is a postback, the property values of the components do <strong>not</strong> represent any values submitted with this request. Instead, they represent the property values that were saved for this view when it
	 * was rendered.
	 * </p>
	 */
	@Override
	public void init() {
		super.init();
		try {
			_init();
		} catch (Exception e) {
			log(getClass().getSimpleName() + " Initialization Failure", e);
			if (e instanceof FacesException) {
				throw (FacesException) e;
			}
			lanzarError(e, "Error inicializando");
		}
	}

	/**
	 * <p>
	 * Return a reference to the scoped data bean.
	 * </p>
	 * 
	 * @return reference to the scoped data bean
	 */
	public final ContablesRequestBean getContablesRequestBean() {
		return (ContablesRequestBean) getBean("contablesRequestBean");
	}

	/**
	 * <p>
	 * Return a reference to the scoped data bean.
	 * </p>
	 * 
	 * @return reference to the scoped data bean
	 */
	public final ContablesApplicationBean getContablesApplicationBean() {
		return (ContablesApplicationBean) getBean("contablesApplicationBean");
	}

	@Override
	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public List<SelectItem> getSelectItemSiNo() {
		final List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem("S", "Si"));
		lista.add(new SelectItem("N", "No"));
		return lista;
	}

	public List<Integer> getKeys(final List<String> items) {
		final List<Integer> lista = new ArrayList<Integer>();
		for (final String item : items) {
			lista.add(new Integer(item.toString()));
		}
		return lista;
	}

	protected void println(String string) {
		System.out.println(string);
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

}
