package de.fisgmbh.tgh.votes.odata;

import java.util.HashMap;
import java.util.Map;

public class EdmTypeEntityEvent extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "Event";
	private static Map<String, String> customPropertyNames;

	static {
		customPropertyNames = new HashMap<>();
		customPropertyNames.put("VoteDetails", "Votes");
	}

	public EdmTypeEntityEvent() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return customPropertyNames;
	}

	@Override
	public String getCustomPropertyName(String originalPropertyName) {
		return super.getCustomPropertyName(originalPropertyName);
	}
}
