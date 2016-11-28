package de.fisgmbh.tgh.votes.odata;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;

import com.sap.cloud.account.TenantContext;

import de.fisgmbh.tgh.dao.FisDaoFactory;
import de.fisgmbh.tgh.odata.FisJPAEdmExtension;
import de.fisgmbh.tgh.odata.FisODataRequestContext;
import de.fisgmbh.tgh.votes.model.CustomDaoFactory;

public class CustomProcessingExtension extends FisJPAEdmExtension {

	public CustomProcessingExtension(ODataJPAContext oDataJPAContext, TenantContext tenantctx) {
		super(oDataJPAContext, tenantctx);
	}

	@Override
	protected void registerEdmTypes() {
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntityEvent());
		this.getEdmTypeFactory().registerEdmType(new EdmTypeEntityVote());
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
