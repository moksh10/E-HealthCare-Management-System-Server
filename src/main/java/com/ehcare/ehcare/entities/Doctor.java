package com.ehcare.ehcare.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="doctor")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","appointments","medicalRecords"})
public class Doctor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="doctor_id")
	private int doctorID;
	
	@Column(name="doctor_email")
	@Email(message = "Not a valid email")
	@NotEmpty(message = "Doctor email required")
	@Size(max=200,message = "Email length exceeded: 200")
	@NotNull
	private String doctorEmail;
	
	@Column(name="password")
	@Size(min=8,message = "Password must be minimum of 8 characters")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@Column(name="doctor_name")
	@NotEmpty(message = "Doctor name cannot be empty")
	@Size(max=200,message = "Doctor name length exceeded: 200")
	private String doctorName;
	
	@Column(name="doctor_contact")
	@NotEmpty(message = "Doctor contact cannot be empty")
	@Size(max=12,message = "Doctor name length exceeded: 12")
	private String doctorContact;
	
	@Column(name="doctor_address")
	@NotEmpty(message = "Doctor name cannot be empty")
	@Size(max=400,message = "Doctor address length exceeded: 400")
	private String doctorAddress;
	
	@Column(name="specialist_in")
	@NotEmpty(message = "Please mention doctor's speciality")
	@Size(max=200,message = "Doctor specialiy length exceeded: 200")
	private String specialistIn;
	
	@Column(name="shift_start_time")
	@NotEmpty(message = "Please mention shift time")
	private String shiftStartTime;
	

	@Column(name="shift_end_time")
	@NotEmpty(message = "Please mention shift time")
	private String shiftEndTime;
	
	@OneToMany(mappedBy = "doctor",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Appointment> appointments;

	@OneToMany(mappedBy = "doctor",fetch = FetchType.LAZY,cascade ={CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	private List<MedicalRecord> medicalRecords;


	public Doctor() {
		// TODO Auto-generated constructor stub
	}

	public Doctor(String doctorEmail,String doctorName,String doctorContact,String doctorAddress,String specialistIn,String shiftStartTime,String shiftEndTime) {
		this.doctorEmail = doctorEmail;
		this.doctorName = doctorName;
		this.doctorContact = doctorContact;
		this.doctorAddress = doctorAddress;
		this.specialistIn = specialistIn;
		this.shiftStartTime = shiftStartTime;
		this.shiftEndTime = shiftEndTime;
	}


	public int getDoctorID() {
		return doctorID;
	}


	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}


	public String getDoctorEmail() {
		return doctorEmail;
	}


	public void setDoctorEmail(String doctorEmail) {
		this.doctorEmail = doctorEmail;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getDoctorName() {
		return doctorName;
	}


	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}


	public String getDoctorContact() {
		return doctorContact;
	}


	public void setDoctorContact(String doctorContact) {
		this.doctorContact = doctorContact;
	}


	public String getDoctorAddress() {
		return doctorAddress;
	}


	public void setDoctorAddress(String doctorAddress) {
		this.doctorAddress = doctorAddress;
	}


	public String getSpecialistIn() {
		return specialistIn;
	}


	public void setSpecialistIn(String specialistIn) {
		this.specialistIn = specialistIn;
	}


	public String getShiftStartTime() {
		return shiftStartTime;
	}


	public void setShiftStartTime(String shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}


	public String getShiftEndTime() {
		return shiftEndTime;
	}


	public void setShiftEndTime(String shiftEndTime) {
		this.shiftEndTime = shiftEndTime;
	}


	public List<Appointment> getAppointments() {
		return appointments;
	}


	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}


	public List<MedicalRecord> getMedicalRecords() {
		return medicalRecords;
	}


	public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
		this.medicalRecords = medicalRecords;
	}
	
	
	

}
