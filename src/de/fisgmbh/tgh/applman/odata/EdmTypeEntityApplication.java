package de.fisgmbh.tgh.applman.odata;

import java.util.HashMap;
import java.util.Map;

public class EdmTypeEntityApplication extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "Application";
	private static Map<String, String> customPropertyNames;

	static {
		customPropertyNames = new HashMap<>();
		customPropertyNames.put("CommentDetails", "Comments");
		customPropertyNames.put("DocumentDetails", "Documents");
		customPropertyNames.put("PositionDetails", "Positions");
		customPropertyNames.put("SourceDetails", "Sources");
	}
	
	public EdmTypeEntityApplication() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return customPropertyNames;
	}

}
