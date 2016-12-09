package de.fisgmbh.tgh.applman.odata;

import java.util.Map;

public class EdmTypeEntityDocument extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "Document";

	public EdmTypeEntityDocument() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return null;
	}

}
