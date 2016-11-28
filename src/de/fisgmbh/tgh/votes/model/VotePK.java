package de.fisgmbh.tgh.votes.model;

import java.io.Serializable;
import java.lang.String;

/**
 * ID class for entity: Vote
 *
 */
public class VotePK implements Serializable {

	private String eventId;
	private int voteId;
	private static final long serialVersionUID = 1L;

	public VotePK() {
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public int getVoteId() {
		return voteId;
	}

	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof VotePK)) {
			return false;
		}
		VotePK other = (VotePK) o;
		return (getEventId() == null ? other.getEventId() == null : getEventId().equals(other.getEventId()))
				&& (getVoteId() == other.getVoteId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getEventId() == null ? 0 : getEventId().hashCode());
		result = prime * result + getVoteId();
		return result;
	}

}
