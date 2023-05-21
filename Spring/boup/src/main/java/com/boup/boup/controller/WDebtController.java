package com.boup.boup.controller;

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

import com.boup.boup.model.Debt;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;


@RestController
@RequestMapping("/web")
public class WDebtController {
	
	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	@GetMapping("/debts")
	public ModelAndView getDebts() {
		ModelAndView mav=new ModelAndView("debts");
		List<Debt> debts=debtS.findAll();

		mav.addObject("debts", debts);
		
		return mav;
	}
	
	@GetMapping("/debt")
	public ModelAndView getDebtW(@RequestParam("id") Integer id) {
		
		//Devuelve la pagina de un usuario
		ModelAndView mav=new ModelAndView("seeDebt");
		Debt debt= debtS.findById(id).orElse(new Debt());
		
		mav.addObject("debt",debt);
		
		return mav;
	}
	
	//Crud
	
	@PostMapping("/debt/insert")
	public ModelAndView insertDebtW(@ModelAttribute Debt d,@RequestParam("rec")String receiver,@RequestParam("debto") String debtor,@RequestParam("group") String group) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/debts");
		/*userS.findByNick(receiver)
			.ifPresent(rec -> userS.findByNick(debtor)
					.ifPresent(debto-> groupS.findByGroupName(group)
										.ifPresent(grou->{
											d.setReceiver(rec);
											d.setDebtor(debto);
											d.setDebtGroup(grou);
										debtS.insert(d);})));
		
		*/
		return mav;
	}
	
	@PostMapping("/debt/delete")
	public ModelAndView deleteDebtW(@ModelAttribute Debt d) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/debts");
		
		debtS.delete(d.getId());

		return mav;
	}
	
	@PostMapping("/debt/update")
	public ModelAndView updateDebtW(@ModelAttribute Debt d,@RequestParam("rec")String receiver,@RequestParam("debto") String debtor,@RequestParam("group") String group) {
		
		ModelAndView mav=new ModelAndView("redirect:/web/debts");
		/*
		userS.findByNick(receiver)
		.ifPresent(rec -> userS.findByNick(debtor)
				.ifPresent(debto-> groupS.findByGroupName(group)
									.ifPresent(grou->{
										d.setReceiver(rec);
										d.setDebtor(debto);
										d.setDebtGroup(grou);
									debtS.update(d);})));
		*/
		return mav;
	}
	
	//Formularios
	
	@GetMapping("/debtFormu")
	public ModelAndView getDebtFormEmpty() {
		//Devuelve debtForm vacio
		ModelAndView mav=new ModelAndView("debtForm");
		mav.addObject("debt", new Debt());
		return mav;
	}
	
	@GetMapping("/debtForm")
	public ModelAndView getDebtForm(@RequestParam("id") Integer id) {
		//Devuelve debtForm de algun usuario
		ModelAndView mav=new ModelAndView("debtForm");
		mav.addObject("debt", debtS.findById(id).orElse(new Debt()));
		return mav;
	}
}
