package com.boup.boup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.boup.boup.model.Group;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;



@RestController
@RequestMapping("/web")
public class WGroupController {
	
	@Autowired GroupService groupS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired UserService userS;
	
	@GetMapping("/groupss")
	public ModelAndView getGroups() {
		ModelAndView mav=new ModelAndView("groups");
		List<Group> groups=groupS.findAll();

		mav.addObject("groups", groups);
		
		return mav;
	}
	
	@GetMapping("/group")
	public ModelAndView getGroupW(@RequestParam("id") String id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeGroup");
		Group group= groupS.findById(Integer.parseInt(id)).orElse(new Group());
		
		mav.addObject("group",group);
		//mav.addObject("users",group.getUsers());
		return mav;
	}
	
	//Crud
	
	@PostMapping("/group/insert")
	public ModelAndView insertGroupW(@ModelAttribute Group u) {
		
		ModelAndView mav;
		Optional<Group> us=groupS.insert(u);
		
		//Si se inserta correctamente me redirecciona a la pagina de ese usuario si no a la de usuarios
		if(us.isPresent()) {
			mav=new ModelAndView("redirect:/web/group");
			mav.addObject("id", us.get().getId());
		}else {
			mav=new ModelAndView("redirect:/web/groups");
		}
		
		return mav;
	}
	
	@PostMapping("/group/delete")
	public ModelAndView deleteGroupW(@ModelAttribute Group u) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/groups");
		
		groupS.delete(u.getId());

		return mav;
	}
	
	@PostMapping("/group/update")
	public ModelAndView updateGroupW(@ModelAttribute Group u) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/group");
		
		mav.addObject("id", groupS.update(u).orElse(u).getId());
		
		return mav;
	}
	
	//Formularios
	
	@GetMapping("/groupFormu")
	public ModelAndView getGroupFormEmpty() {
		//Devuelve groupForm vacio
		ModelAndView mav=new ModelAndView("groupForm");
		mav.addObject("group", new Group());
		return mav;
	}
	
	@GetMapping("/groupForm")
	public ModelAndView getGroupForm(@RequestParam("id") Integer id) {
		//Devuelve groupForm de algun usuario
		ModelAndView mav=new ModelAndView("groupForm");
		mav.addObject("group", groupS.findById(id).orElse(new Group()));
		return mav;
	}
}
