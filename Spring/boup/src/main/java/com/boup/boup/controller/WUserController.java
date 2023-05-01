package com.boup.boup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.boup.boup.model.Group;
import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;


@Controller
@RequestMapping("/web")
public class WUserController {
	
	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	@GetMapping("/")
	public ModelAndView getInicio() {
		ModelAndView mav=new ModelAndView("inicio");
		
		
		return mav;
	}
	
	@GetMapping("/users")
	public ModelAndView getUsers() {
		ModelAndView mav=new ModelAndView("users");
		List<User> users=userS.findAll();

		mav.addObject("users", users);
		
		return mav;
	}
	
	@GetMapping("/user")
	public ModelAndView getUserW(@RequestParam("id") Integer id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeUser");
		User user= userS.findById(id).orElse(new User());
		List<Group> groups= groupS.findByUser(user);
		mav.addObject("user",user);
		mav.addObject("groups",groups);
		return mav;
	}
	
	//Crud
	
	@PostMapping("/user/insert")
	public ModelAndView insertUserW(@ModelAttribute User u) {
		
		ModelAndView mav;
		Optional<User> us=userS.insert(u);
		
		//Si se inserta correctamente me redirecciona a la pagina de ese usuario si no a la de usuarios
		if(us.isPresent()) {
			mav=new ModelAndView("redirect:/web/user");
			mav.addObject("id", us.get().getId());
		}else {
			mav=new ModelAndView("redirect:/web/users");
		}
		
		return mav;
	}
	
	@PostMapping("/user/delete")
	public ModelAndView deleteUserW(@ModelAttribute User u) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/users");
		
		userS.delete(u.getId());

		return mav;
	}
	
	@PostMapping("/user/update")
	public ModelAndView updateUserW(@ModelAttribute User u) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/user");
		
		mav.addObject("id", userS.update(u).orElse(u).getId());
		
		return mav;
	}
	
	//Formularios
	
	@GetMapping("/userFormu")
	public ModelAndView getUserFormEmpty() {
		//Devuelve userForm vacio
		ModelAndView mav=new ModelAndView("userForm");
		mav.addObject("user", new User());
		return mav;
	}
	
	@GetMapping("/userForm")
	public ModelAndView getUserForm(@RequestParam("id") Integer id) {
		//Devuelve userForm de algun usuario
		ModelAndView mav=new ModelAndView("userForm");
		mav.addObject("user", userS.findById(id).orElse(new User()));
		return mav;
	}
	
	//other
	
	@PostMapping("/user/deleteGroup")
	public ModelAndView deleteUserGroup(@RequestParam("groupId") Integer groupid,@RequestParam("userId") Integer userid) {
		
		ModelAndView mav=new ModelAndView("seeUser");
		User u=userS.deleteUserGroup(groupid, userid).orElse(new User());
		List<Group> groups= groupS.findByUser(u);
		mav.addObject("user",u);
		mav.addObject("groups",groups);
		return mav;
		
	}
	
	@PostMapping("/user/addGroup")
	public ModelAndView addUserGroup(@RequestParam("groupName") String groupname,@RequestParam("userId") Integer userid) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/users");
		groupS.addUserGroup(groupname, userid);
		
		return mav;
		
	}
}
