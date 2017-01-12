package de.fisgmbh.tgh.applman.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import de.fisgmbh.tgh.dao.FisJpaObject;
import de.fisgmbh.tgh.dao.System;

@MappedSuperclass
public class CustomJpaObject extends FisJpaObject  {

	@Embedded
	private System system;

	@Column(name="CREATED_BY", length=100)
	private String createdBy;
	
	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
