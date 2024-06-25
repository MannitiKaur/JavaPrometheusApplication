package com.app.prommonitor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

	@GetMapping("/monitor")
	public String monitor() {
		return "Application is up";
	}
		
}
