package de.fisgmbh.tgh.applman.odata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmFunctionImport;
import org.apache.olingo.odata2.api.edm.EdmLiteral;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.NavigationSegment;
import org.apache.olingo.odata2.api.uri.info.GetEntityLinkUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetLinksUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;

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
	public ODataResponse readEntityLink(final GetEntityLinkUriInfo uriInfo, final String contentType)
			throws ODataException {
/*		final Object data = retrieveData(uriInfo.getStartEntitySet(), uriInfo.getKeyPredicates(),
				uriInfo.getFunctionImport(), mapFunctionParameters(uriInfo.getFunctionImportParameters()),
				uriInfo.getNavigationSegments());

		// if (!appliesFilter(data, uriInfo.getFilter()))
		if (data == null) {
			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
		}
*/
		final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();

		Map<String, Object> values = new HashMap<String, Object>();
		for (final EdmProperty property : entitySet.getEntityType().getKeyProperties()) {
//			values.put(property.getName(), valueAccess.getPropertyValue(data, property));
		}

		ODataContext context = getContext();
		final EntityProviderWriteProperties entryProperties = EntityProviderWriteProperties
				.serviceRoot(context.getPathInfo().getServiceRoot()).build();

		final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeLink");

		final ODataResponse response = EntityProvider.writeLink(contentType, entitySet, values, entryProperties);

		context.stopRuntimeMeasurement(timingHandle);

		return ODataResponse.fromResponse(response).build();
	}

	@Override
	public ODataResponse readEntityLinks(final GetEntitySetLinksUriInfo uriInfo, final String contentType)
			throws ODataException {
		ArrayList<Object> data = new ArrayList<Object>();
		/*
		 * try { data.addAll((List<?>) retrieveData(uriInfo.getStartEntitySet(),
		 * uriInfo.getKeyPredicates(), uriInfo.getFunctionImport(),
		 * mapFunctionParameters(uriInfo.getFunctionImportParameters()),
		 * uriInfo.getNavigationSegments())); } catch (final
		 * ODataNotFoundException e) { data.clear(); }
		 * 
		 * final Integer count =
		 * applySystemQueryOptions(uriInfo.getTargetEntitySet(), data,
		 * uriInfo.getFilter(), uriInfo.getInlineCount(), null, //
		 * uriInfo.getOrderBy(), uriInfo.getSkipToken(), uriInfo.getSkip(),
		 * uriInfo.getTop());
		 * 
		 * final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
		 * 
		 * List<Map<String, Object>> values = new ArrayList<Map<String,
		 * Object>>(); for (final Object entryData : data) { Map<String, Object>
		 * entryValues = new HashMap<String, Object>(); for (final EdmProperty
		 * property : entitySet.getEntityType().getKeyProperties()) {
		 * entryValues.put(property.getName(),
		 * valueAccess.getPropertyValue(entryData, property)); }
		 * values.add(entryValues); }
		 * 
		 * ODataContext context = getContext(); final
		 * EntityProviderWriteProperties entryProperties =
		 * EntityProviderWriteProperties
		 * .serviceRoot(context.getPathInfo().getServiceRoot()).inlineCountType(
		 * uriInfo.getInlineCount()) .inlineCount(count).build();
		 * 
		 * final int timingHandle =
		 * context.startRuntimeMeasurement("EntityProvider", "writeLinks");
		 * 
		 * final ODataResponse response = EntityProvider.writeLinks(contentType,
		 * entitySet, values, entryProperties);
		 * 
		 * context.stopRuntimeMeasurement(timingHandle); return
		 * ODataResponse.fromResponse(response).build();
		 */
		return null;
	}

	private static Map<String, Object> mapFunctionParameters(final Map<String, EdmLiteral> functionImportParameters)
			throws EdmSimpleTypeException {
		if (functionImportParameters == null) {
			return Collections.emptyMap();
		} else {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			for (final String parameterName : functionImportParameters.keySet()) {
				final EdmLiteral literal = functionImportParameters.get(parameterName);
				final EdmSimpleType type = literal.getType();
				parameterMap.put(parameterName,
						type.valueOfString(literal.getLiteral(), EdmLiteralKind.DEFAULT, null, type.getDefaultType()));
			}
			return parameterMap;
		}
	}
/*
	private Object retrieveData(final EdmEntitySet startEntitySet, final List<KeyPredicate> keyPredicates,
			final EdmFunctionImport functionImport, final Map<String, Object> functionImportParameters,
			final List<NavigationSegment> navigationSegments) throws ODataException {
		Object data;
		final Map<String, Object> keys = mapKey(keyPredicates);

		ODataContext context = getContext();
		final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "retrieveData");

		try {
			data = functionImport == null
					? keys.isEmpty() ? dataSource.readData(startEntitySet) : dataSource.readData(startEntitySet, keys)
					: dataSource.readData(functionImport, functionImportParameters, keys);

			EdmEntitySet currentEntitySet = functionImport == null ? startEntitySet : functionImport.getEntitySet();
			for (NavigationSegment navigationSegment : navigationSegments) {
				data = dataSource.readRelatedData(currentEntitySet, data, navigationSegment.getEntitySet(),
						mapKey(navigationSegment.getKeyPredicates()));
				currentEntitySet = navigationSegment.getEntitySet();
			}
		} finally {
			context.stopRuntimeMeasurement(timingHandle);
		}
		return data;
	}

	private static Map<String, Object> mapKey(final List<KeyPredicate> keys) throws EdmException {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		for (final KeyPredicate key : keys) {
			final EdmProperty property = key.getProperty();
			final EdmSimpleType type = (EdmSimpleType) property.getType();
			keyMap.put(property.getName(), type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT,
					property.getFacets(), type.getDefaultType()));
		}
		return keyMap;
	}
*/
}
