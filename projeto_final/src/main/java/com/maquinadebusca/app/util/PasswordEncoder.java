package com.maquinadebusca.app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
	public static void main(String[] args) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println("user: "+passwordEncoder.encode("user"));
		System.out.println("admin: "+passwordEncoder.encode("admin"));
	}
	
}
