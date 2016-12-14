package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;

public class LinkSourceApplicationPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String sourceId;
	private String applicationId;

	public LinkSourceApplicationPK() {
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String SourceId) {
		this.sourceId = SourceId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LinkSourceApplicationPK)) {
			return false;
		}
		LinkSourceApplicationPK other = (LinkSourceApplicationPK) o;
		return (getSourceId() == null ? other.getSourceId() == null : getSourceId().equals(other.getSourceId()))
				&& (getApplicationId() == null ? other.getApplicationId() == null : getApplicationId().equals(other.getApplicationId()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getSourceId() == null ? 0 : getSourceId().hashCode());
		result = prime * result + (getApplicationId() == null ? 0 : getApplicationId().hashCode());
		return result;
	}

}
