package com.eqipped.userservice.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.eqipped.userservice.config.CryptoUtils;
import com.eqipped.userservice.entities.User;
import com.eqipped.userservice.helper.ChangePasswordDetails;
import com.eqipped.userservice.helper.EmailDetails;
import com.eqipped.userservice.helper.RendomString;
import com.eqipped.userservice.repositories.UserRepository;
import com.eqipped.userservice.service.EmailService;
import com.eqipped.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/password")
@CrossOrigin("*")
public class ForgetPasswordController {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	// Sending a simple Email
	@PutMapping("/forgetPassword")
	public Map<String, Object> forgetPassword(@RequestBody EmailDetails details) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
			String status;
			String otp = RendomString.getAlphaNumericString(8);
			String hashpassword = CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(otp));
		//Random random = new Random();
		//int otp = 100000 + random.nextInt(900000);

			User user = userService.getUserByUsernameForEmail(details.getRecipient());
			if (user != null) {
				if (user.getUsername().equalsIgnoreCase(details.getRecipient())) {
					user.setPassword(hashpassword);

					String signature = "\n\nThanks & Regards,\nEqipped Help Desk Team\n";

					userRepository.save(user);
					EmailDetails email = new EmailDetails();
					email.setMsgBody("Dear " + user.getFirstName()
							+ ",\n\n\t\t\tDon't Share Password from Anyone.\n \n\t\t\tPassword : " + otp + "\n\t\t\t"
							+ signature);
					email.setSubject("New Changed Password");
					email.setRecipient(user.getEmail());
					status = emailService.sendSimpleMail(email);
					map.put("STATUS", "SUCCESS");
					map.put("Email", status);

				}
			} else {
				status = "Your Email Id is not Present in our data database";
				map.put("STATUS", "FAILED");
				map.put("Email", status);
			}
		} catch (Exception e) {
			// TODO: handle exception
			//status = "Server API Through Exception";
			e.printStackTrace();
			map.put("STATUS", "FAILED");
			map.put("Email", "Server API Through Exception");
		}
		return map;

	}

	@PutMapping("/changePassword")
	public void changePassword(@RequestBody ChangePasswordDetails newUser) throws Exception {

		String status;
		User oldUser = userService.getUserByUsername(newUser.getEmail());

		String oldHashPassword = CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(newUser.getOldPassword()));
		String newHashPassword = CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(newUser.getNewPassword()));

		if (oldUser.getEmail().equalsIgnoreCase(newUser.getEmail()) && oldUser.getPassword().equals(oldHashPassword)) {
			oldUser.setPassword(newHashPassword);
			userRepository.save(oldUser);
			status = "Password has changed Successfly";
		} else
			status = "Error has been Occured for Changeing Password";

	}

}
