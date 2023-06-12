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
	
	private static final String GROUPS_VAR="groups";
	private static final String GROUP="group";
	
	@GetMapping("")
	public ModelAndView groups() {
		ModelAndView mav=new ModelAndView(GROUPS_VAR);
		List<Group> groups = groupS.findAll();

		mav.addObject(GROUPS_VAR, groups);
		return mav;
	}
	
	@GetMapping("/{id}")
	public ModelAndView getGroupW(@PathVariable("id") String id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeGroup");
		Group group= groupS.findById(Integer.parseInt(id)).orElse(new Group());
		
		mav.addObject(GROUP,group);
		mav.addObject("users", debtS.findGroupUsers(group));
		
		return mav;
	}
	
	//Crud
	
	@PostMapping("/insert")
	public ModelAndView insertGroupW(Group g) {
		ModelAndView mav=new ModelAndView("seeGroup");

		g=groupS.insert(g).orElse(new Group());
		mav.addObject(GROUP, g);
		
		return mav;
	}
	
	@PostMapping("/delete")
	public ModelAndView deleteGroupW(Model model,Group g) {
		groupS.delete(g.getId());

		return new ModelAndView(GROUPS_VAR).addObject(GROUPS_VAR, groupS.findAll());
	}
	
	@PostMapping("/update")
	public ModelAndView updateGroupW(Group g) {
		ModelAndView mav=new ModelAndView("seeGroup");

		g=groupS.update(g).orElse(new Group());
		mav.addObject(GROUP,g);
		
		return mav;
	}
	
	//Formularios
	
	@GetMapping("/form")
	public ModelAndView getGroupFormEmpty() {
		return new ModelAndView("groupForm").addObject(GROUP,new Group());
	}
	
	@GetMapping("/form/{id}")
	public ModelAndView getGroupForm(@PathVariable("id") String id) {
		//Devuelve groupForm de algun usuario
		ModelAndView mav=new ModelAndView("groupForm");
		mav.addObject(GROUP, groupS.findById(Integer.parseInt(id)).orElse(new Group()));
		return mav;
	}
}
