package com.boup.boup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.boup.boup.model.Group;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;


@RestController
@RequestMapping("/web/groups")
public class WGroupController {
	
	@Autowired GroupService groupS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired UserService userS;
	
	@GetMapping("")
	public ModelAndView hola() {
		ModelAndView mav=new ModelAndView("groups");
		List<Group> groups = groupS.findAll();

		mav.addObject("groups", groups);
		return mav;
	}
	
	@GetMapping("/{id}")
	public ModelAndView getGroupW(@PathVariable("id") String id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeGroup");
		Group group= groupS.findById(Integer.parseInt(id)).orElse(new Group());
		
		mav.addObject("group",group);
		mav.addObject("users", debtS.findGroupUsers(group));
		
		return mav;
	}
	
	//Crud
	
	@PostMapping("/insert")
	public ModelAndView insertGroupW(Group g) {
		ModelAndView mav=new ModelAndView("seeGroup");

		g=groupS.insert(g).orElse(new Group());
		mav.addObject("group", g);
		
		return mav;
	}
	
	@PostMapping("/delete")
	public ModelAndView deleteGroupW(Model model,Group g) {
		ModelAndView mav=new ModelAndView("groups");
		groupS.delete(g.getId());

		return mav;
	}
	
	@PostMapping("/update")
	public ModelAndView updateGroupW(Group g) {
		ModelAndView mav=new ModelAndView("seeGroup");

		g=groupS.update(g).orElse(new Group());
		mav.addObject("group",g);
		
		return mav;
	}
	
	//Formularios
	
	@GetMapping("/formu")
	public ModelAndView getGroupFormEmpty() {
		ModelAndView mav=new ModelAndView("seeGroup");

		mav.addObject("group",new Group());
		return mav;
	}
	
	@GetMapping("/form/{id}")
	public ModelAndView getGroupForm(@PathVariable("id") String id) {
		//Devuelve groupForm de algun usuario
		ModelAndView mav=new ModelAndView("groupForm");
		mav.addObject("group", groupS.findById(Integer.parseInt(id)).orElse(new Group()));
		return mav;
	}
}
