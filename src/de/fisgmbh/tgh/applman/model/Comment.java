package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="COMMENT")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
public class Comment extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name = "CommentGenerator", table = "APPLMAN_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Comment", initialValue = 1, allocationSize = 1000)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CommentGenerator")
	@Column(name="COMMENT_ID", nullable=false, length=10)
	private String commentId;
	
	@Column(name="TIMESTAMP", nullable=false)
	private Timestamp timestamp;
	
	@Column(name="NAME", length=100, nullable=false)
	private String name;
	
	@Column(name="SUBJECT", length=30, nullable=false)
	private String subject;
	
	@Column(name="TEXT", length=500, nullable=false)
	private String text;
	
	@ManyToOne
	@JoinColumn(name = "APPLICATION_ID", referencedColumnName = "APPLICATION_ID")
	private Application application;
	
	public Comment() {
		super();
	}
}
