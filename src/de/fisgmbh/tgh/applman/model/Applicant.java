package de.fisgmbh.tgh.applman.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="APPLICANT")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", discriminatorType = DiscriminatorType.STRING, length = 36)
public class Applicant extends CustomJpaObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name = "ApplicantGenerator", table = "APPLMAN_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Applicant", initialValue = 1, allocationSize = 1000)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ApplicantGenerator")
	@Column(name="APPLICANT_ID", nullable=false, length=10)
	private String applicantId;
	
	@Column(name="GENDER", length=2, nullable=false)
	private String gender;
	
	@Column(name="SALUTATION", length=50, nullable=true)
	private String salutation;
	
	@Column(name="FIRSTNAME", length=30, nullable=false)
	private String firstname;
	
	@Column(name="LASTNAME", length=30, nullable=false)
	private String lastname;
	
	@Column(name="BIRTHDATE", nullable=false)
	private Date birthdate;
	
	@Column(name="PICTURE", length=40, nullable=true)
	private String picture;
	
	@Column(name="ZIPCODE", length=10, nullable=false)
	private String zipcode;
	
	@Column(name="CITY", length=40, nullable=false)
	private String city;
	
	@Column(name="STREET", length=60, nullable=false)
	private String street;
	
	@Column(name="PHONE", length=40, nullable=true)
	private String phone;
	
	@Column(name="MOBILE", length=40, nullable=true)
	private String mobile;
	
	@Column(name="EMAIL", length=80, nullable=true)
	private String email;
	
	@OneToMany(mappedBy="applicant", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Application> applications;
	
	public Applicant() {
		super();
		
		applications = new ArrayList<Application>();
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
	
	public void addApplication(Application application) {
		this.applications.add(application);
	}
}
