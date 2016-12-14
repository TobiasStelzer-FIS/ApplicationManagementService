package de.fisgmbh.tgh.applman.odata;

import java.util.Map;

public class EdmTypeEntityLinkPositionApplication extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "LinkPositionApplication";

	public EdmTypeEntityLinkPositionApplication() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return null;
	}
	
}
