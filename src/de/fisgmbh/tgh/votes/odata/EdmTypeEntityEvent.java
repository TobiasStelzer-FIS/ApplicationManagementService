package de.fisgmbh.tgh.votes.odata;

import java.util.Map;

public class EdmTypeEntityEvent extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "Event";

	public EdmTypeEntityEvent() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return null;
	}

}
