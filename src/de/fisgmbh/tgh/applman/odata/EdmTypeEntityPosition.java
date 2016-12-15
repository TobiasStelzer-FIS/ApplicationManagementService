package de.fisgmbh.tgh.applman.odata;

import java.util.HashMap;
import java.util.Map;

public class EdmTypeEntityPosition extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "Position";
	private static Map<String, String> customPropertyNames;

	static {
		customPropertyNames = new HashMap<>();
		customPropertyNames.put("ApplicationDetails", "Applications");
	}
	
	public EdmTypeEntityPosition() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return customPropertyNames;
	}

}
