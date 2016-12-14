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
@Table(name="LINK_POSITION_APPLICATION")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
@IdClass(LinkPositionApplicationPK.class)
public class LinkPositionApplication extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="POSITION_ID", length=10)
	private String positionId;
	
	@Id
	@Column(name="APPLICATION_ID", length=10)
	private String applicationId;
	
	@ManyToOne
	@JoinColumn(name = "POSITION_ID", referencedColumnName = "POSITION_ID", updatable = false, insertable = false)
	private Position position;
	
	@ManyToOne
	@JoinColumn(name="APPLICATION_ID", referencedColumnName="APPLICATION_ID", updatable = false, insertable = false)
	private Application application;
	
	public LinkPositionApplication() {
		super();
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

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	
}
