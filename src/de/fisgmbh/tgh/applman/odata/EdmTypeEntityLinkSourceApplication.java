package de.fisgmbh.tgh.applman.odata;

import java.util.Map;

public class EdmTypeEntityLinkSourceApplication extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "LinkSourceApplication";

	public EdmTypeEntityLinkSourceApplication() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return null;
	}
	
}
