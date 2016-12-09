package de.fisgmbh.tgh.applman.odata;

import java.util.Map;

public class EdmTypeEntityTest extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "Test";
	
	public EdmTypeEntityTest() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return null;
	}

}
