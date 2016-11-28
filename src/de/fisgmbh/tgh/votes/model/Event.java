package de.fisgmbh.tgh.votes.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name = "EVENT")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
public class Event extends CustomJpaObject implements Serializable {

	@Id
	@Column(name = "EVENT_ID", nullable = false, length = 10)
	private String eventId;

	@Column(name = "NAME", nullable = false, length = 100)
	private String text;

	@Column(name = "DESCRIPTION", nullable = true, length = 254)
	private String description;

	@OneToMany(mappedBy = "event")
	@JoinColumn(updatable = false, insertable = false)
	private List<Vote> votes;

	private static final long serialVersionUID = 1L;

	public Event() {
		super();
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

}
