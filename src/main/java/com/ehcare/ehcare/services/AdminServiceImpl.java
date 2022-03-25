package com.ehcare.ehcare.services;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ehcare.ehcare.entities.Admin;
import com.ehcare.ehcare.repository.AdminRepository;



//TODO 
// 1. add custom exception
@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Override
	@Transactional
	public Admin saveAdmin(Admin admin) {
		Admin checkAdmin=adminRepository.findAdminByAdminEmail(admin.getAdminEmail());
		if(checkAdmin!=null)
			throw new RuntimeException();
		admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
		return adminRepository.save(admin);
	}

	@Override
	@Transactional
	public Admin updateAdmin(int adminID, Admin admin) {
		Optional<Admin> adminToGet=adminRepository.findById(adminID);
		if(!adminToGet.isPresent())
			throw new RuntimeException();
		Admin adminToUpdate=adminToGet.get();
		adminToUpdate.setAdminEmail(admin.getAdminEmail());
		adminToUpdate.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
		adminToUpdate.setAdminName(admin.getAdminName());
		adminToUpdate.setAdminContact(admin.getAdminContact());
		return adminRepository.save(adminToUpdate);
	}

	@Override
	@Transactional
	public Admin getAdmin(int adminID) {
		Optional<Admin> adminToGet=adminRepository.findById(adminID);
		if(!adminToGet.isPresent())
			throw new RuntimeException();
		return adminToGet.get();
	}

	@Override
	@Transactional
	public Admin getAdminByAdminEmail(String adminAdminEmail) {
		return adminRepository.findAdminByAdminEmail(adminAdminEmail);
	}

	@Override
	@Transactional
	public void deleteAdmin(int adminID) {
		Optional<Admin> adminToGet = adminRepository.findById(adminID);
		if(!adminToGet.isPresent())
			throw new RuntimeException();
		Admin adminToDelete = adminToGet.get();
	    adminRepository.delete(adminToDelete);
		
	}

	@Override
	@Transactional
	public List<Admin> getAllAdmins() {
		List<Admin> allAdmins=new LinkedList<Admin>();
		Iterable<Admin> admins=adminRepository.findAll();
		Iterator<Admin> iterator=admins.iterator();
		while(iterator.hasNext())
			allAdmins.add(iterator.next());
		return allAdmins;
	}
}