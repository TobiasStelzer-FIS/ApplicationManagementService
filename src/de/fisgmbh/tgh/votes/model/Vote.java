package de.fisgmbh.tgh.votes.model;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name = "VOTE")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
@IdClass(VotePK.class)
public class Vote extends CustomJpaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EVENT_ID", nullable = false, length = 10)
	private String eventId;

	@Id
	@Column(name = "VOTE_ID", nullable = false)
	private int voteId;

	@Column(name = "VOTE", nullable = false, length = 10)
	private int vote;

	@Column(name = "TEXT", nullable = true, length = 254)
	private String text;

	@Column(name = "IP", nullable = false, length = 15)
	private String ip;

	@ManyToOne
	@JoinColumn(name = "EVENT_ID", referencedColumnName = "EVENT_ID", updatable = false, insertable = false)
	private Event event;
	
	public Vote() {
		super();
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

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
