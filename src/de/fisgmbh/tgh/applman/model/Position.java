package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="POSITION")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
public class Position extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="POSITION_ID", nullable=false, length=10)
	private String positionId;
	
	@Column(name="NAME", nullable=false, length=80)
	private String name;

	@ManyToMany(mappedBy="positions")
	private List<Application> applications;
	
	public Position() {
		super();
		
		applications = new ArrayList<Application>();
	}
	
}