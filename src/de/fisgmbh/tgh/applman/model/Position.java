package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="POSITION")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
public class Position extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name = "PositionGenerator", table = "APPLMAN_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Position", initialValue = 1, allocationSize = 1000)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PositionGenerator")
	@Column(name="POSITION_ID", nullable=false, length=10)
	private String positionId;
	
	@Column(name="NAME", nullable=false, length=150)
	private String name;

	@OneToMany(mappedBy="position")
	@JoinColumn(updatable = false, insertable = false)
	private List<LinkPositionApplication> applications;
	
	public Position() {
		super();
		
		applications = new ArrayList<LinkPositionApplication>();
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<LinkPositionApplication> getApplications() {
		return applications;
	}

	public void setApplications(List<LinkPositionApplication> applications) {
		this.applications = applications;
	}
	
}
