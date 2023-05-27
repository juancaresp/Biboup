package com.boup.boup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

	@Autowired
	UserService userS;
	@Autowired
	DebtService debtS;
	@Autowired
	SpentService spentS;
	@Autowired
	GroupService groupS;

	// CRUD

	@PostMapping("/insert")
	public ResponseEntity<Debt> insertDebt(@RequestBody Debt debt) {

		ResponseEntity<Debt> rp = new ResponseEntity<Debt>(HttpStatus.BAD_REQUEST);

		Optional<Debt> opD = debtS.insert(debt);
		if (opD.isPresent()) {
			debt = opD.get();
			rp = new ResponseEntity<Debt>(debt, HttpStatus.OK);
		}

		return rp;
	}

	@PostMapping("/update")
	public ResponseEntity<Debt> updateDebt(@RequestBody Debt debt) {

		ResponseEntity<Debt> rp = new ResponseEntity<Debt>(HttpStatus.BAD_REQUEST);

		Optional<Debt> opD = debtS.update(debt);
		if (opD.isPresent()) {
			debt = opD.get();
			rp = new ResponseEntity<Debt>(debt, HttpStatus.OK);
		}

		return rp;
	}

	@PostMapping("/delete")
	public ResponseEntity<Debt> deleteDebt(@RequestBody Debt debt) {

		ResponseEntity<Debt> rp = new ResponseEntity<Debt>(HttpStatus.BAD_REQUEST);

		if (debtS.delete(debt.getId())) {
			rp = new ResponseEntity<Debt>(debt, HttpStatus.OK);
		}

		return rp;
	}

	// List,getdebt

	@GetMapping("")
	public ResponseEntity<List<Debt>> getDebts() {

		List<Debt> debts = debtS.findAll();

		ResponseEntity<List<Debt>> rp = new ResponseEntity<List<Debt>>(debts, HttpStatus.OK);

		return rp;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Debt> getDebtById(@RequestBody Integer id) {

		ResponseEntity<Debt> rp = new ResponseEntity<Debt>(HttpStatus.BAD_REQUEST);

		Optional<Debt> debt = debtS.findById(id);
		if (debt.isPresent()) {
			rp = new ResponseEntity<Debt>(debt.get(), HttpStatus.OK);
		}

		return rp;
	}

	// Close debt
	@PostMapping("/pay/{username}/group/{groupid}")
	public ResponseEntity<Debt> closeDebt(@PathVariable String username, @PathVariable String groupid) {

		ResponseEntity<Debt> rp = new ResponseEntity<Debt>(HttpStatus.BAD_REQUEST);

		Optional<User> opU = userS.findByNick(username);
		Optional<Group> opG = groupS.findById(Integer.parseInt(groupid));

		if (opU.isPresent() && opG.isPresent()) {

			User u = opU.get();
			Group g = opG.get();
			Optional<Debt> debt = debtS.findByUserAndGroup(u, g);

			if (debt.isPresent()) {
				Optional<Debt> d = debtS.pay(debt.get());

				if (d.isPresent())
					rp = new ResponseEntity<Debt>(d.get(), HttpStatus.OK);
			} else {
				rp = new ResponseEntity<Debt>(HttpStatus.NOT_ACCEPTABLE);
			}
		}

		return rp;
	}

}
