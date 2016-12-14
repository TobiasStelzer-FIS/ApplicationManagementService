package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="LINK_SOURCE_APPLICATION")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
@IdClass(LinkSourceApplicationPK.class)
public class LinkSourceApplication extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SOURCE_ID", length=10)
	private String sourceId;
	
	@Id
	@Column(name="APPLICATION_ID", length=10)
	private String applicationId;
	
	@ManyToOne
	@JoinColumn(name = "SOURCE_ID", referencedColumnName = "SOURCE_ID", updatable = false, insertable = false)
	private Source source;
	
	@ManyToOne
	@JoinColumn(name="APPLICATION_ID", referencedColumnName="APPLICATION_ID", updatable = false, insertable = false)
	private Application application;
	
	public LinkSourceApplication() {
		super();
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source Source) {
		this.source = Source;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	
}