package com.boup.boup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
		
		return mav;
	}
	
	//Crud
	
	@PostMapping("/insert")
	public String insertGroupW(Model model,Group g) {
		
		groupS.insert(g);
		model.addAttribute("group", g);
		
		return "seeGroup";
	}
	
	@PostMapping("/delete")
	public String deleteGroupW(Model model,Group g) {
		
		groupS.delete(g.getId());

		return "groups";
	}
	
	@PostMapping("/update")
	public String updateGroupW(Model model,Group g) {

		groupS.update(g);
		model.addAttribute("group",g);
		
		return "seeGroup";
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
