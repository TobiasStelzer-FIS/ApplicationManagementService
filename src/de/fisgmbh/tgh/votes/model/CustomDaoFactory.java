package de.fisgmbh.tgh.votes.model;

import javax.persistence.EntityManager;

import de.fisgmbh.tgh.dao.FisDaoFactory;

public class CustomDaoFactory extends FisDaoFactory {

	public CustomDaoFactory(EntityManager em) {
		super(em);
	}
} 
