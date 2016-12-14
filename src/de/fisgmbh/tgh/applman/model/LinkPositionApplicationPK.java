package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;

public class LinkPositionApplicationPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String positionId;
	private String applicationId;

	public LinkPositionApplicationPK() {
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
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
		if (!(o instanceof LinkPositionApplicationPK)) {
			return false;
		}
		LinkPositionApplicationPK other = (LinkPositionApplicationPK) o;
		return (getPositionId() == null ? other.getPositionId() == null : getPositionId().equals(other.getPositionId()))
				&& (getApplicationId() == null ? other.getApplicationId() == null : getApplicationId().equals(other.getApplicationId()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getPositionId() == null ? 0 : getPositionId().hashCode());
		result = prime * result + (getApplicationId() == null ? 0 : getApplicationId().hashCode());
		return result;
	}

}
