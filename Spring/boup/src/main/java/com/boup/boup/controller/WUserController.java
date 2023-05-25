package com.boup.boup.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;


@RestController
@RequestMapping("/web/users")
public class WUserController {
	
	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	/*
	@GetMapping("")
	public ModelAndView getInicio() {
		ModelAndView mav=new ModelAndView("start");
		
		return mav;
	}*/
	
	@GetMapping("")
	public ModelAndView getUsersW() {
		ModelAndView mav=new ModelAndView("users");

		mav.addObject("users", userS.findAll());
		
		return mav;
	}
	
	@GetMapping("/{id}")
	public ModelAndView getUserW(@PathVariable("id") Integer id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeUser");
		User user= userS.findById(id).orElse(new User());
		
		//Meter grupos a los que pertenece 
		mav.addObject("user",user);
		mav.addObject("groups", debtS.findUserGroups(user));
		
		return mav;
	}
	
	//Crud
	
	@PostMapping("/insert")
	public ModelAndView insertUserW(User u) {
		
		ModelAndView mav;
		Optional<User> us=userS.insert(u);
		
		//Si se inserta correctamente me redirecciona a la pagina de ese usuario si no a la de usuarios
		if(us.isPresent()) {
			mav=new ModelAndView("seeUser");
			mav.addObject("user", us.get());
			mav.addObject("groups", debtS.findUserGroups(us.get()));
		}else {
			mav=new ModelAndView("users");
		}
		
		return mav;
	}
	
	@PostMapping("/delete")
	public ModelAndView deleteUserW(User u) {
		
		userS.delete(u.getId());
		
		ModelAndView mav=new ModelAndView("users");
		mav.addObject("users", userS.findAll());
		
		return mav;
	}
	
	@PostMapping("/update")
	public ModelAndView updateUserW(Model model,User u) {
		ModelAndView mav;
		
		Optional<User> us=userS.update(u);
		if(us.isPresent()) {
			mav= new ModelAndView("seeUser");
			mav.addObject("user", us.get());
			mav.addObject("groups", debtS.findUserGroups(us.get()));
		}else {
			mav=new ModelAndView("users");
		}

		return mav;
	}
	
	//Formularios
	
	@GetMapping("/form")
	public ModelAndView getUserFormEmpty() {
		//Devuelve userForm vacio
		ModelAndView mav=new ModelAndView("userForm");
		mav.addObject("user", new User());
		return mav;
	}
	
	@GetMapping("/form/{id}")
	public ModelAndView getUserForm(@PathVariable("id") Integer id) {
		//Devuelve userForm de algun usuario
		ModelAndView mav=new ModelAndView("userForm");
		mav.addObject("user", userS.findById(id).orElse(new User()));
		return mav;
	}
	
	//other
	/*
	@PostMapping("deleteGroup")
	public ModelAndView deleteUserGroup(@RequestParam("groupId") Integer groupid,@RequestParam("userId") Integer userid) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/users");
		groupS.deleteUserGroup(groupid, userid);
		
		return mav;
		
	}
	
	@PostMapping("/user/addGroup")
	public ModelAndView addUserGroup(@RequestParam("groupid") Integer groupid,@RequestParam("userId") Integer userid) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/users");
		groupS.addUserGroup(groupid, userid);
		
		return mav;
		
	}*/
}
