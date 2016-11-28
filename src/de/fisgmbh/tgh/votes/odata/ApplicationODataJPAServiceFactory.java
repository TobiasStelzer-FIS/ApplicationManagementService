package de.fisgmbh.tgh.votes.odata;

import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import com.sap.cloud.account.TenantContext;

import de.fisgmbh.tgh.odata.FisJPAEdmExtension;
import de.fisgmbh.tgh.odata.FisODataApplication;

public class ApplicationODataJPAServiceFactory extends FisODataApplication {

	private static final String PUNIT_NAME = "VotesSrv";

	@Override
	protected FisJPAEdmExtension createJPAExtension(ODataJPAContext oDataJPAContext, TenantContext tenantctx) {
		return new CustomProcessingExtension(oDataJPAContext, tenantctx);
	}

	@Override
	protected String getPersistenceUnitName() {
		return PUNIT_NAME;
	}

	@Override
	protected ODataSingleProcessor createODataSingleProcessor(ODataJPAContext oDataJPAContext) {
		return new CustomODataJPAProcessor(oDataJPAContext);
	}

}