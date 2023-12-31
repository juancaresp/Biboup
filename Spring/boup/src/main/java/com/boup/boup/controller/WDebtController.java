package com.boup.boup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;


@RestController
@RequestMapping("/web/debts")
public class WDebtController {
	
	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	private static final String DEBT="debt";
	private static final String DEBTS_VAR="debts";
	
	@GetMapping("")
	public ModelAndView getDebts() {
		ModelAndView mav=new ModelAndView(DEBTS_VAR);
		List<Debt> debts=debtS.findAll();

		mav.addObject(DEBTS_VAR, debts);
		
		return mav;
	}
	
	@GetMapping("/{id}")
	public ModelAndView getDebtW(@PathVariable("id") String id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeDebt");
		Debt debt= debtS.findById(Integer.parseInt(id)).orElse(new Debt());
		
		mav.addObject(DEBT,debt);
		
		return mav;
	}
	
	//Crud
	
	@PostMapping("/insert")
	public ModelAndView insertDebtW(@ModelAttribute Debt d,Model model) {
		Optional<User> opU=userS.findByNick(d.getUser().getUsername());
		Optional<Group> opG=groupS.findById(d.getGroup().getId());
		
		if(opU.isPresent()&&opG.isPresent()) {
			d.setUser(opU.get());
			d.setGroup(opG.get());
			d=debtS.insert(d).orElse(new Debt());
			model.addAttribute(DEBT, d);
		}
		
		return new ModelAndView(DEBTS_VAR).addObject(DEBTS_VAR, debtS.findAll());
	}
	
	@PostMapping("/delete")
	public ModelAndView deleteDebtW(@ModelAttribute Debt d) {		
		
		debtS.delete(d.getId());

		return new ModelAndView(DEBTS_VAR).addObject(DEBTS_VAR, debtS.findAll());
	}
	
	@PostMapping("/update")
	public ModelAndView updateDebtW(@ModelAttribute Debt d, Model model) {
		
		Optional<User> opU=userS.findByNick(d.getUser().getUsername());
		Optional<Group> opG=groupS.findById(d.getGroup().getId());
		
		if(opU.isPresent()&&opG.isPresent()) {
			d.setUser(opU.get());
			d.setGroup(opG.get());
			d=debtS.update(d).orElse(new Debt());
			model.addAttribute(DEBT, d);
		}
		return new ModelAndView(DEBTS_VAR).addObject(DEBTS_VAR, debtS.findAll());
	}
	
	//Formularios
	
	@GetMapping("/formu")
	public ModelAndView getDebtFormEmpty(Model model) {
		//Devuelve debtForm vacio
		ModelAndView mav=new ModelAndView("debtForm");
		mav.addObject(DEBT, new Debt());
		return mav;
	}
	
	@GetMapping("/form/{id}")
	public ModelAndView getDebtForm(Model model,@PathVariable("id") String id ) {
		//Devuelve debtForm
		ModelAndView mav=new ModelAndView("debtForm");
		mav.addObject(DEBT, debtS.findById(Integer.parseInt(id)).orElse(new Debt()));
		return mav;
	}
}
