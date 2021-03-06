package com.ehcare.ehcare.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ehcare.ehcare.entities.Admin;
import com.ehcare.ehcare.entities.Doctor;
import com.ehcare.ehcare.entities.Patient;
import com.ehcare.ehcare.handlers.AuthorizationException;
import com.ehcare.ehcare.services.AdminService;
import com.ehcare.ehcare.services.DoctorService;
import com.ehcare.ehcare.services.PatientService;
import com.ehcare.ehcare.services.UserDetailsService;
import com.ehcare.ehcare.util.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		if (request.getRequestURI().toString().equals("/authFailure")) {
			chain.doFilter(request, response);
			return;
		}

		Cookie[] cookies = request.getCookies();
		String authorization = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("jwt")) {
					authorization = cookie.getValue();
				}
			}
		}

		String username = null;
		String jwt = null;

		if (authorization != null) {
			jwt = authorization;
			try {
				username = jwtUtil.extractUsername(jwt);

			} catch (Exception e) {
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			if (jwtUtil.validateToken(jwt, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
				String role = "";
				for (GrantedAuthority grantedAuthority : roles)
					role += grantedAuthority.getAuthority();
				username = username.split("#")[0];
				if (role.equals("ADMIN")) {
					Admin admin= adminService.getAdminByAdminEmail(username);
					int adminID = admin.getAdminID();
					String userName = admin.getAdminName();
					request.setAttribute("adminID", adminID);
					request.setAttribute("userID", adminID);
					request.setAttribute("role", "admin");
					request.setAttribute("username", userName);
				

				} else if (role.equals("DOCTOR")) {
					Doctor doctor=doctorService.getDoctorByDoctorEmail(username);
					int doctorID = doctor.getDoctorID();
					String userName=doctor.getDoctorName();
					request.setAttribute("doctorID", doctorID);
					request.setAttribute("userID", doctorID);
					request.setAttribute("role", "doctor");
					request.setAttribute("username", userName);

				} else if (role.equals("PATIENT")) {
					Patient patient=patientService.getPatientByPatientEmail(username);
					int patientID = patient.getPatientID();
					String userName=patient.getPatientName();
					request.setAttribute("patientID", patientID);
					request.setAttribute("userID", patientID);
					request.setAttribute("role", "patient");
					request.setAttribute("username", userName);

				}

			} else {
				throw new AuthorizationException();
			}

		}

		chain.doFilter(request, response);
	}

}