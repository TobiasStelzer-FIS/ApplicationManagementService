package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="TEST")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
public class Test extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Customer ids are generated within a number range starting with 1 */
//	@TableGenerator(name = "TestGenerator", table = "APPLMAN_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Test", initialValue = 100000000, allocationSize = 100)
	@Id
//	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TestGenerator")
	@Column(name="TEST_ID", nullable=false, length=10)
	private String testId;
	
	@Column(name="NAME", length=20)
	private String name;
	
	public Test() {
		super();
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
