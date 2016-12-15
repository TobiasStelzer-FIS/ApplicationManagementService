package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="APPLICATION")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
public class Application extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@TableGenerator(name = "ApplicationGenerator", table = "APPLMAN_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Application", initialValue = 1, allocationSize = 1000)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ApplicationGenerator")
	@Column(name="APPLICATION_ID", nullable=false, length=10)
	private String applicationId;

//	@Column(name="APPLICANT_ID", length=10, nullable=false)
//	private String applicantId;

	@Column(name="STATUS_ID", length=10, nullable=true)
	private String statusId;
	
	@Column(name="ENTERED_BY", length=100, nullable=false)
	private String enteredBy;
	
	@Column(name="ENTERED_ON", nullable=false)
	private Date enteredOn;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL, targetEntity=Applicant.class)
	@JoinColumn(name = "APPLICANT_ID", referencedColumnName = "APPLICANT_ID")
	private Applicant applicant;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "STATUS_ID", referencedColumnName = "STATUS_ID", updatable = false, insertable = false)
	private Status status;
	
	@OneToMany(mappedBy="application", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Comment> comments;
	
	@OneToMany(mappedBy="application", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Document> documents;
		
	@OneToMany(mappedBy="application", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(updatable = false, insertable = false)
	private List<LinkPositionApplication> positions;
	
	@OneToMany(mappedBy="application", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(updatable = false, insertable = false)
	private List<LinkSourceApplication> sources;
	
	public Application() {
		super();
		
		positions = new ArrayList<LinkPositionApplication>();
		sources = new ArrayList<LinkSourceApplication>();
		comments = new ArrayList<Comment>();
		documents = new ArrayList<Document>();
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
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
		applicant.addApplication(this);
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

	public List<LinkPositionApplication> getPositions() {
		return positions;
	}

	public void setPositions(List<LinkPositionApplication> positions) {
		this.positions = positions;
	}

	public List<LinkSourceApplication> getSources() {
		return sources;
	}

	public void setSources(List<LinkSourceApplication> sources) {
		this.sources = sources;
	}
	

}
