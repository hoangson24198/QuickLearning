package com.quicklearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
	
	
	@GetMapping("/admin")
    public String admin(Model model) {
		
        return "admin/index";
    }
	
	@GetMapping("/admin/login")
    public String login(Model model) {
		
        return "admin/login";
    }
	
	@GetMapping("/admin/student-manager")
    public String studentManager(Model model) {
		
        return "admin/student_manager";
    }
}
