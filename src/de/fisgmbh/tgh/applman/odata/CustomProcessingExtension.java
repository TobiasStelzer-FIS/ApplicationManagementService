package de.fisgmbh.tgh.applman.odata;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;

import com.sap.cloud.account.TenantContext;

import de.fisgmbh.tgh.applman.model.CustomDaoFactory;
import de.fisgmbh.tgh.dao.FisDaoFactory;
import de.fisgmbh.tgh.odata.FisJPAEdmExtension;
import de.fisgmbh.tgh.odata.FisODataRequestContext;

public class CustomProcessingExtension extends FisJPAEdmExtension {

	public CustomProcessingExtension(ODataJPAContext oDataJPAContext, TenantContext tenantctx) {
		super(oDataJPAContext, tenantctx);
	}

	@Override
	protected void registerEdmTypes() {
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntityApplicant());
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntityApplication());
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntityComment());
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntityDocument());
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntityPosition());
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntitySource());
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntityStatus());
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntityTest());
	}

	@Override
	protected FisDaoFactory createDaoFactory() {
		return new CustomDaoFactory(this.getODataJPAContext().getEntityManager());
	}

	@Override
	protected FisODataRequestContext createODataRequestContext() {
		return new CustomODataRequestContext(this.getODataJPAContext());
	}

}
