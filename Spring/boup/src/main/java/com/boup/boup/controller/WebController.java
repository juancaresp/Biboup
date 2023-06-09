package com.boup.boup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/web")
public class WebController {
	
	@GetMapping("")
	public ModelAndView getW() {
		
		return new ModelAndView("index");
	}
	
}
