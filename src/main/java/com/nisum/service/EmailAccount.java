package com.nisum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailAccount {
	
	
	@Value("${gmail.email}")
	private String adminemail;
	
	@Value("${gmail.password}")
	private String adminpassword;
	
	@Value("${gmail.admin.email}")
	private String adminemailId;
	
	public String getAdminemailId() {
		return adminemailId;
	}

	public void setAdminemailId(String adminemailId) {
		this.adminemailId = adminemailId;
	}

	@Value("${gmail.subject}")
	private String subject;

	public String getAdminemail() {
		return adminemail;
	}

	public void setAdminemail(String adminemail) {
		this.adminemail = adminemail;
	}

	public String getAdminpassword() {
		return adminpassword;
	}

	public void setAdminpassword(String adminpassword) {
		this.adminpassword = adminpassword;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
		
}
