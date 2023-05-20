package com.boup.boup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.boup.boup.model.Group;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;


@Controller
@RequestMapping("/web/groups")
public class WGroupController {
	
	@Autowired GroupService groupS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired UserService userS;
	
	@GetMapping("")
	public String hola(Model modelo) {
		List<Group> groups = groupS.findAll();

		modelo.addAttribute("groups", groups);
		return "groups";
	}
	
	@GetMapping("/{id}")
	public ModelAndView getGroupW(@PathVariable("id") String id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeGroup");
		Group group= groupS.findById(Integer.parseInt(id)).orElse(new Group());
		
		mav.addObject("group",group);
		//mav.addObject("users",group.getUsers());
		return mav;
	}
	
	//Crud
	
	@PostMapping("/insert")
	public String insertGroupW(Model model) {
		
		Group u =(Group) model.getAttribute("group");
		Optional<Group> us=groupS.insert(u);
		
		return "seeGroup";
	}
	
	@PostMapping("/delete")
	public ModelAndView deleteGroupW(@ModelAttribute Group u) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/groups");
		
		groupS.delete(u.getId());

		return mav;
	}
	
	@PostMapping("/update")
	public ModelAndView updateGroupW(@ModelAttribute Group u) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/group");
		
		mav.addObject("id", groupS.update(u).orElse(u).getId());
		
		return mav;
	}
	
	//Formularios
	
	@GetMapping("/formu")
	public String getGroupFormEmpty(Model model) {
		model.addAttribute("group",new Group());
		return "groupForm";
	}
	
	@GetMapping("/form/{id}")
	public ModelAndView getGroupForm(@PathVariable("id") String id) {
		//Devuelve groupForm de algun usuario
		ModelAndView mav=new ModelAndView("groupForm");
		mav.addObject("group", groupS.findById(Integer.parseInt(id)).orElse(new Group()));
		return mav;
	}
}
