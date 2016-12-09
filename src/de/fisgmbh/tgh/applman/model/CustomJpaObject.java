package de.fisgmbh.tgh.applman.model;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import de.fisgmbh.tgh.dao.FisJpaObject;
import de.fisgmbh.tgh.dao.System;

@MappedSuperclass
public class CustomJpaObject extends FisJpaObject  {

	@Embedded
	private System system;

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}
}
