package com.boup.boup.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.boup.boup.model.Spent;
import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/web/spents")
public class WSpentController {
	
	@Autowired UserService userS;
	@Autowired SpentService spentS;
	@Autowired DebtService debtS;
	@Autowired GroupService groupS;
	
	@GetMapping("")
	public ModelAndView getSpents() {
		ModelAndView mav=new ModelAndView("spents");

		mav.addObject("spents", spentS.findAll());
		
		return mav;
	}
	
	@GetMapping("/{id}")
	public ModelAndView getSpentW(@PathVariable("id") Integer id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeSpent");
		Spent spent= spentS.findById(id).orElse(new Spent());
		
		mav.addObject("spent",spent);
		mav.addObject("users",spent.getUsers());
		return mav;
	}
	
	//Crud
	
	@PostMapping("/insert")
	public ModelAndView insertSpentW(@ModelAttribute Spent s) {
		
		ModelAndView mav=new ModelAndView("spents");
		try {
			userS.findByNick(s.getPayer().getUsername())
				.ifPresent(payer -> {
					groupS.findById(s.getGroup().getId()).ifPresent(g ->{
						s.setPayer(payer);
						s.setGroup(g);
						spentS.addSpent(s);
					});
				});
			mav=new ModelAndView("seeSpent");
			
			mav.addObject("spent",s);
			mav.addObject("users",s.getUsers());
		}catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		return mav;
	}
	
	@PostMapping("/delete")
	public ModelAndView deleteSpentW(@ModelAttribute Spent s) {
		
		spentS.delete(s.getId());

		return new ModelAndView("spents").addObject("spents",spentS.findAll());
	}
	
	@PostMapping("/update")
	public ModelAndView updateSpentW(@ModelAttribute Spent s) {
		
		ModelAndView mav=new ModelAndView("seeSpent");
		
		try {
			userS.findByNick(s.getPayer().getUsername())
				.ifPresent(payer -> {
					groupS.findById(s.getGroup().getId()).ifPresent(g ->{
						s.setPayer(payer);
						s.setGroup(g);
						spentS.update(s);
					});
				});
		}catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		mav.addObject("spent",s);
		mav.addObject("users",s.getUsers());
		
		return mav;
	}
	
	//Formularios
	
	@GetMapping("/form")
	public ModelAndView getSpentFormEmpty() {
		//Devuelve spentForm vacio
		return new ModelAndView("spentForm").addObject("spent", new Spent());
	}
	
	@GetMapping("/form/{id}")
	public ModelAndView getSpentForm(@PathVariable("id") Integer id) {
		//Devuelve spentForm de algun usuario
		return new ModelAndView("spentForm").addObject("spent", spentS.findById(id).orElse(new Spent()));
	}
	
	//ADD Y DELETE USERS
	@PostMapping("/deleteUser")
	public ModelAndView deleteUserGroup(@RequestParam("spentId") Integer spentId,@RequestParam("username") String username, HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("seeSpent");

		Optional<Spent> opS=spentS.findById(spentId);
		if(opS.isPresent()) {
			Spent s=opS.get();
			spentS.deleteSpent(s.getId());
			s.getUsers().removeIf(u-> u.getUsername().equals(username));
			spentS.addSpent(s);
			mav.addObject("spent",s);
			mav.addObject("users",s.getUsers());
		}else {
			mav=new ModelAndView("spents").addObject("spents", spentS.findAll());
		}
		
		
		return mav;
	}
	
	@PostMapping("/addUser")
	public ModelAndView addUserGroup(@RequestParam("spentId") Integer spentId,@RequestParam("username") String username, HttpServletRequest request) {
		
		ModelAndView mav=new ModelAndView("seeSpent");

		Optional<Spent> opS=spentS.findById(spentId);
		Optional<User> opU=userS.findByNick(username);
		
		if(opS.isPresent()&&opU.isPresent()) {
			Spent s=opS.get();
			spentS.deleteSpent(s.getId());
			s.getUsers().add(opU.get());
			spentS.addSpent(s);
			mav.addObject("spent",s);
			mav.addObject("users",s.getUsers());
		}else {
			mav=new ModelAndView("seeSpent").addObject("spents", spentS.findAll());
		}
		
		return mav;
		
	}
}
