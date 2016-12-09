package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="STATUS")
public class Status extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="STATUS_ID", nullable=false, length=10)
	private String statusId;
	
	@Column(name="NAME", nullable=false, length=20)
	private String name;
	
	@OneToMany(mappedBy="status")
	private List<Application> applications;
	
	public Status() {
		super();
		
		applications = new ArrayList<Application>();
	}
}
