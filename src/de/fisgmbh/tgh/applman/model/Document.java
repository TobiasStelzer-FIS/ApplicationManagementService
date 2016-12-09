package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="DOCUMENT")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
public class Document extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOCUMENT_ID", nullable=false, length=10)
	private String documentId;
	
	@Column(name="DOCUMENTPATH", length=30)
	private String dokumentpath;
	
	@Column(name="DISPLAYTEXT", length=100)
	private String displaytext;
	
	@ManyToOne
	@JoinColumn(name = "APPLICATION_ID", referencedColumnName = "APPLICATION_ID")
	private Application application;
	
	public Document() {
		super();
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDokumentpath() {
		return dokumentpath;
	}

	public void setDokumentpath(String dokumentpath) {
		this.dokumentpath = dokumentpath;
	}

	public String getDisplaytext() {
		return displaytext;
	}

	public void setDisplaytext(String displaytext) {
		this.displaytext = displaytext;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	
	
}
