package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="APPLICATION")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
public class Application extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="APPLICATION_ID", nullable=false, length=10)
	private String applicationId;
	
	@Column(name="ENTERED_BY", length=100, nullable=false)
	private String enteredBy;
	
	@Column(name="ENTERED_ON", nullable=false)
	private Date enteredOn;
	
	@ManyToOne
	@JoinColumn(name = "APPLICANT_ID", referencedColumnName = "APPLICANT_ID")
	private Applicant applicant;
	
	@ManyToOne
	@JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID")
	private Status status;
	
	@OneToMany(mappedBy="application")
	private List<Comment> comments;
	
	@OneToMany(mappedBy="application")
	private List<Document> documents;
	
	@ManyToMany
	@JoinTable(name ="LINK_APPLICATION_POSITION",
		    joinColumns=@JoinColumn(name="APPLICATION_ID", referencedColumnName="APPLICATION_ID", updatable=false, insertable=false),
		    inverseJoinColumns=@JoinColumn(name="POSITION_ID", referencedColumnName="POSITION_ID", updatable=false, insertable=false))
	private List<Position> positions;
	
	@ManyToMany
	@JoinTable(name ="LINK_APPLICATION_SOURCE",
		    joinColumns=@JoinColumn(name="APPLICATION_ID"),
		    inverseJoinColumns=@JoinColumn(name="SOURCE_ID"))
	private List<Source> sources;
	
	public Application() {
		super();
		
		positions = new ArrayList<Position>();
		comments = new ArrayList<Comment>();
		documents = new ArrayList<Document>();
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

	public Date getEnteredOn() {
		return enteredOn;
	}

	public void setEnteredOn(Date enteredOn) {
		this.enteredOn = enteredOn;
	}

	public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public List<Source> getSources() {
		return sources;
	}

	public void setSources(List<Source> sources) {
		this.sources = sources;
	}
	
	
}