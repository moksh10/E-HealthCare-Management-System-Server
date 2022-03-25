package com.ehcare.ehcare.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehcare.ehcare.entities.Doctor;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Integer>{

	Doctor findDoctorByDoctorEmail(String doctorEmail);
}
