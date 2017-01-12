package de.fisgmbh.tgh.applman.odata;

import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.batch.BatchRequestPart;
import org.apache.olingo.odata2.api.batch.BatchResponsePart;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmStructuralType;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderBatchProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.exception.ODataBadRequestException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.PathInfo;
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetComplexPropertyUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityLinkUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetLinksUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetSimplePropertyUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.api.uri.info.PutMergePatchUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;

import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

import de.fisgmbh.tgh.odata.FisODataJPAProcessor;

/**
 * 
 *
 */

public class CustomODataJPAProcessor extends FisODataJPAProcessor {

	public CustomODataJPAProcessor(ODataJPAContext oDataJPAContext) {
		super(oDataJPAContext);
	}

	@Override
	public void addCustomData(Map<String, Object> map) {
		User user;
		HttpServletRequest request = (HttpServletRequest) oDataJPAContext.getODataContext().getParameter(ODataContext.HTTP_SERVLET_REQUEST_OBJECT);
		Principal principal = request.getUserPrincipal();
		String username = null;
		if (principal != null) {
			try {
				UserProvider users = UserManagementAccessor.getUserProvider();
				user = users.getUser(principal.getName());
				username = user.getAttribute("firstname") + " " + user.getAttribute("lastname");
			} catch (Exception e) {
				
			}
		}
		if (username == null) {
			username = request.getRemoteUser();
		}
		map.put("CreatedBy", username);
	}
	
	@Override
	public ODataResponse readEntityLink(final GetEntityLinkUriInfo uriParserResultView, final String contentType)
			throws ODataException {
		ODataResponse oDataResponse = null;
		
		ODataContext ctx = getContext();
		if (ctx.getBatchParentContext() != null) {
			ctx = ctx.getBatchParentContext();
		}
		oDataJPAContext.setODataContext(ctx);	// Important if batch-processing is used
		
		// TODO: Handle Permissions

		Object jpaEntity = jpaProcessor.process(uriParserResultView);
		oDataResponse = responseBuilder.build(uriParserResultView, jpaEntity, contentType);

		return oDataResponse;
	}

	@Override
	public ODataResponse readEntityLinks(final GetEntitySetLinksUriInfo uriParserResultView, final String contentType)
			throws ODataException {
		
		ODataResponse oDataResponse = null;
		
		ODataContext ctx = getContext();
		if (ctx.getBatchParentContext() != null) {
			ctx = ctx.getBatchParentContext();
		}
		oDataJPAContext.setODataContext(ctx);
		
		// TODO: Handle Permissions

		List<Object> jpaEntities = jpaProcessor.process(uriParserResultView);
		oDataResponse = responseBuilder.build(uriParserResultView, jpaEntities, contentType);

		return oDataResponse;
	}

	@Override
	public ODataResponse createEntityLink(final PostUriInfo uriParserResultView, final InputStream content,
			final String requestContentType, final String contentType) throws ODataException {
		
		ODataResponse response = ODataResponse.newBuilder().build();
		
		ODataContext ctx = getContext();
		oDataJPAContext.setODataContext(ctx);
		
		try {
			jpaProcessor.process(uriParserResultView, content, requestContentType, contentType);
		} catch (Exception e) {
			response = ODataResponse.newBuilder().status(HttpStatusCodes.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}

	@Override
	public ODataResponse updateEntityLink(final PutMergePatchUriInfo uriParserResultView, final InputStream content,
			final String requestContentType, final String contentType) throws ODataException {

		ODataResponse response = ODataResponse.newBuilder().build();
		
		ODataContext ctx = getContext();
		oDataJPAContext.setODataContext(ctx);
		
		try {
			jpaProcessor.process(uriParserResultView, content, requestContentType, contentType);
		} catch (Exception e) {
			response = ODataResponse.newBuilder().status(HttpStatusCodes.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}

	@Override
	public ODataResponse deleteEntityLink(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {

		ODataResponse response = ODataResponse.newBuilder().build();
		
		ODataContext ctx = getContext();
		if (ctx.getBatchParentContext() != null) {
			ctx = ctx.getBatchParentContext();
		}
		oDataJPAContext.setODataContext(ctx);
		
		try {
			jpaProcessor.process(uriInfo, contentType);
		} catch (Exception e) {
			response = ODataResponse.newBuilder().status(HttpStatusCodes.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}

	@Override
	public ODataResponse executeBatch(final BatchHandler handler, final String contentType, final InputStream content)
			throws ODataException {
		ODataResponse batchResponse;
		List<BatchResponsePart> batchResponseParts = new ArrayList<BatchResponsePart>();
		PathInfo pathInfo = getContext().getPathInfo();
		EntityProviderBatchProperties batchProperties = EntityProviderBatchProperties.init().pathInfo(pathInfo).build();
		List<BatchRequestPart> batchParts = EntityProvider.parseBatchRequest(contentType, content, batchProperties);
		for (BatchRequestPart batchPart : batchParts) {
			batchResponseParts.add(handler.handleBatchPart(batchPart));
		}
		batchResponse = EntityProvider.writeBatchResponse(batchResponseParts);
		return batchResponse;
	}

	@Override
	public BatchResponsePart executeChangeSet(final BatchHandler handler, final List<ODataRequest> requests)
			throws ODataException {
		List<ODataResponse> responses = new ArrayList<ODataResponse>();
		for (ODataRequest request : requests) {
			ODataResponse response = handler.handleRequest(request);
			if (response.getStatus().getStatusCode() >= HttpStatusCodes.BAD_REQUEST.getStatusCode()) {
				// Rollback
				List<ODataResponse> errorResponses = new ArrayList<ODataResponse>(1);
				errorResponses.add(response);
				return BatchResponsePart.responses(errorResponses).changeSet(false).build();
			}
			responses.add(response);
		}
		return BatchResponsePart.responses(responses).changeSet(true).build();
	}
	
}
