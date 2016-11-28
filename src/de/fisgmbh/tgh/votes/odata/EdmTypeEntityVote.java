package de.fisgmbh.tgh.votes.odata;

import java.util.Map;

public class EdmTypeEntityVote extends EdmTypeEntityJPAStandard {

	private static final String ET_NAME = "Vote";

	public EdmTypeEntityVote() {
		super(ET_NAME);
	}

	@Override
	protected Map<String, String> getCustomPropertyNames() {
		return null;
	}

}
