package de.fisgmbh.tgh.applman.odata;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;

import de.fisgmbh.tgh.odata.FisODataRequestContext;

public class CustomODataRequestContext extends FisODataRequestContext {

	/**
	 * 
	 */
	public static final String ROLE_MANAGER = "Manager";

	/**
	 * 
	 * @param oDataJPAContext
	 */
	public CustomODataRequestContext(ODataJPAContext oDataJPAContext) {
		super(oDataJPAContext);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isManager() {
		return this.getRequest().isUserInRole(ROLE_MANAGER);
	}

}
