package de.fisgmbh.tgh.applman.odata;

import java.util.Map;

public class EdmTypeEntityComment extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "Comment";

	public EdmTypeEntityComment() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return null;
	}

}
