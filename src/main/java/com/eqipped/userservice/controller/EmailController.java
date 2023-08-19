package com.eqipped.userservice.controller;
//Java Program to Create Rest Controller that
//Defines various API for Sending Mail



//Importing required classes

import com.eqipped.userservice.helper.EmailDetails;
import com.eqipped.userservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;





//Annotation
@RestController
@CrossOrigin("*")
//Class
public class EmailController {

	@Autowired private EmailService emailService;

	// Sending a simple Email
	@PostMapping("/sendMail")
	public String
	sendMail(@RequestBody EmailDetails details)
	{
		String status
			= emailService.sendSimpleMail(details);

		return status;
	}

	// Sending email with attachment
	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(
		@RequestBody EmailDetails details)
	{
		String status
			= emailService.sendMailWithAttachment(details);

		return status;
	}
}

