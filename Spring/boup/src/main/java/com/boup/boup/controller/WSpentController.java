package com.boup.boup.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.boup.boup.model.Spent;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;


@RestController
@RequestMapping("/web")
public class WSpentController {
	
	@Autowired UserService userS;
	@Autowired SpentService spentS;
	@Autowired DebtService debtS;
	@Autowired GroupService groupS;
	
	@GetMapping("/spents")
	public ModelAndView getSpents() {
		ModelAndView mav=new ModelAndView("spents");
		List<Spent> spents=spentS.findAll();

		mav.addObject("spents", spents);
		
		return mav;
	}
	
	@GetMapping("/spent")
	public ModelAndView getSpentW(@RequestParam("id") Integer id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeSpent");
		Spent spent= spentS.findById(id).orElse(new Spent());
		
		mav.addObject("spent",spent);
		mav.addObject("users",spent.getUsers());
		return mav;
	}
	
	//Crud
	
	@PostMapping("/spent/insert")
	public ModelAndView insertSpentW(@ModelAttribute Spent s,@RequestParam("paye")String paye,@RequestParam("dat")String dat,@RequestParam("groupId")Integer groupId) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/spents");
		try {
			LocalDate date=LocalDate.parse(dat);
			s.setDate(date);
			userS.findByNick(paye)
				.ifPresent(payer -> {
					groupS.findById(groupId).ifPresent(g ->{
						s.setPayer(payer);
						s.setGroup(g);
						spentS.insert(s);
					});
				});
		}catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		return mav;
	}
	
	@PostMapping("/spent/delete")
	public ModelAndView deleteSpentW(@ModelAttribute Spent d) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/spents");
		
		spentS.delete(d.getId());

		return mav;
	}
	
	@PostMapping("/spent/update")
	public ModelAndView updateSpentW(@ModelAttribute Spent s,@RequestParam("paye")String paye,@RequestParam("dat")String dat,@RequestParam("groupId")Integer groupId) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/spents");
		try {
			LocalDate date=LocalDate.parse(dat);
			s.setDate(date);
			userS.findByNick(paye)
				.ifPresent(payer -> {
					groupS.findById(groupId).ifPresent(g ->{
						s.setPayer(payer);
						s.setGroup(g);
						spentS.update(s);
					});
				});
			
		}catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		

		return mav;
	}
	
	//Formularios
	
	@GetMapping("/spentFormu")
	public ModelAndView getSpentFormEmpty() {
		//Devuelve spentForm vacio
		ModelAndView mav=new ModelAndView("spentForm");
		mav.addObject("spent", new Spent());
		return mav;
	}
	
	@GetMapping("/spentForm")
	public ModelAndView getSpentForm(@RequestParam("id") Integer id) {
		//Devuelve spentForm de algun usuario
		ModelAndView mav=new ModelAndView("spentForm");
		mav.addObject("spent", spentS.findById(id).orElse(new Spent()));
		return mav;
	}
	
	//ADD Y DELETE USERS
	@PostMapping("/spent/deleteUser")
	public ModelAndView deleteUserGroup(@RequestParam("spentId") Integer spentId,@RequestParam("userId") Integer userid) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/spents");
		spentS.deleteUserSpent(spentId, userid);
		
		return mav;
		
	}
	
	@PostMapping("/spent/addUser")
	public ModelAndView addUserGroup(@RequestParam("spentId") Integer spentId,@RequestParam("userId") Integer userid) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/spents");
		spentS.addUserSpent(spentId, userid);
		
		return mav;
		
	}
}
