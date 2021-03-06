package com.ehcare.ehcare.services;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ehcare.ehcare.entities.Patient;
import com.ehcare.ehcare.handlers.UserAlreadyExistsException;
import com.ehcare.ehcare.handlers.UserNotFoundException;
import com.ehcare.ehcare.repository.PatientRepository;

@Service
public class PatientImpl implements PatientService {

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public Patient savePatient(Patient patient) {
		Patient checkPatient = patientRepository.findPatientByPatientEmail(patient.getPatientEmail());
		if (checkPatient != null)
			throw new UserAlreadyExistsException();
		patient.setPassword(bCryptPasswordEncoder.encode(patient.getPassword()));
		return patientRepository.save(patient);
	}

	@Override
	@Transactional
	public Patient updatePatient(int patientID, Patient patient) {
		Optional<Patient> patientToGet = patientRepository.findById(patientID);
		if (!patientToGet.isPresent())
			throw new UserNotFoundException();
		Patient patientToUpdate = patientToGet.get();
		patientToUpdate.setPatientEmail(patient.getPatientEmail());
		if(patient.getPassword()!=null)
			patientToUpdate.setPassword(bCryptPasswordEncoder.encode(patient.getPassword()));
		else
			patientToUpdate.setPassword(patientToUpdate.getPassword());
		patientToUpdate.setPatientName(patient.getPatientName());
		patientToUpdate.setPatientContact(patient.getPatientContact());
		patientToUpdate.setPatientAddress(patient.getPatientAddress());
		patientToUpdate.setPatientAge(patient.getPatientAge());
		patientToUpdate.setPatientGender(patient.getPatientGender());
		return patientRepository.save(patientToUpdate);
	}

	@Override
	@Transactional
	public Patient getPatient(int patientID) {
		Optional<Patient> patientToGet = patientRepository.findById(patientID);
		if (!patientToGet.isPresent())
			throw new UserNotFoundException();
		return patientToGet.get();
	}

	@Override
	@Transactional
	public Patient getPatientByPatientEmail(String patientEmail) {
		return patientRepository.findPatientByPatientEmail(patientEmail);
	}

	@Override
	@Transactional
	public void deletePatient(int patientID) {
		Optional<Patient> patientToGet = patientRepository.findById(patientID);
		if (!patientToGet.isPresent())
			throw new UserNotFoundException();
		Patient patientToDelete = patientToGet.get();
		patientRepository.delete(patientToDelete);

	}

	@Override
	@Transactional
	public List<Patient> getAllPatients() {
		List<Patient> allPatients = new LinkedList<Patient>();
		Iterable<Patient> patients = patientRepository.findAll();
		Iterator<Patient> iterator = patients.iterator();
		while (iterator.hasNext())
			allPatients.add(iterator.next());
		return allPatients;
	}

}
