package com.quicklearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("a", "xin chàoaaaaaa");
		model.addAttribute("b", "xin chào2");
        return "home/index";
    }
	
	@GetMapping("/login")
    public String login(Model model) {
        return "home/login";
    }
	
	@GetMapping("/register")
    public String register(Model model) {
        return "home/register";
    }
	
	@GetMapping("/course")
    public String courseDetail(Model model) {
        return "home/course";
    }
	
	@GetMapping("/course-list")
    public String courseList(Model model) {
        return "home/course_list";
    }
	
	@GetMapping("/student-profile")
    public String studentProfile(Model model) {
        return "home/student_profile";
    }
	
	@GetMapping("/teacher-profile")
    public String teacherProfile(Model model) {
        return "home/teacher_profile";
    }
}
