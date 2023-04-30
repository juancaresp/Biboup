package com.boup.boup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.boup.boup.model.Spent;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;

@Controller
public class SpentController {

	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	// CRUD

	@PostMapping("/spent/insert")
	public ResponseEntity<Spent> insertSpent(@RequestBody Spent spent) {

		ResponseEntity<Spent> rp = new ResponseEntity<Spent>(HttpStatus.BAD_REQUEST);
		
		Optional<Spent> opS=spentS.insert(spent);
		if (opS.isPresent()) {
			spent=opS.get();
			rp = new ResponseEntity<Spent>(spent, HttpStatus.OK);
		}

		return rp;
	}

	@PostMapping("/spent/update")
	public ResponseEntity<Spent> updateSpent(@RequestBody Spent spent) {

		ResponseEntity<Spent> rp = new ResponseEntity<Spent>(HttpStatus.BAD_REQUEST);
		
		Optional<Spent> opS=spentS.update(spent);
		if (opS.isPresent()) {
			spent=opS.get();
			rp = new ResponseEntity<Spent>(spent, HttpStatus.OK);
		}

		return rp;
	}

	@PostMapping("/spent/delete")
	public ResponseEntity<Spent> deleteSpent(@RequestBody Spent spent) {

		ResponseEntity<Spent> rp = new ResponseEntity<Spent>(HttpStatus.BAD_REQUEST);

		if (spentS.delete(spent.getId())) {
			rp = new ResponseEntity<Spent>(spent, HttpStatus.OK);
		}

		return rp;
	}

	// List,getspent

	@GetMapping("/spents")
	public ResponseEntity<List<Spent>> getSpents() {

		List<Spent> spents = spentS.findAll();

		ResponseEntity<List<Spent>> rp = new ResponseEntity<List<Spent>>(spents, HttpStatus.OK);

		return rp;
	}

	@GetMapping("/spent")
	public ResponseEntity<Spent> getSpentById(@RequestBody Integer id) {

		ResponseEntity<Spent> rp = new ResponseEntity<Spent>(HttpStatus.BAD_REQUEST);

		Optional<Spent> spent = spentS.findById(id);
		if (spent.isPresent()) {
			rp = new ResponseEntity<Spent>(spent.get(), HttpStatus.OK);
		}

		return rp;
	}
	
	//Other
	
	@GetMapping("/groupSpents")
	public ResponseEntity<List<Spent>> getGroupSpents(@RequestBody Integer groupId) {

		List<Spent> spents = spentS.findByGroup(groupId);

		ResponseEntity<List<Spent>> rp = new ResponseEntity<List<Spent>>(spents, HttpStatus.OK);

		return rp;
	}
	
	//ADD a spent generating the debts
	@PostMapping("/spent/add")
	public ResponseEntity<Spent> addSpent(@RequestBody Spent spent) {

		ResponseEntity<Spent> rp = new ResponseEntity<Spent>(HttpStatus.BAD_REQUEST);
		
		Optional<Spent> opS=spentS.addSpent(spent);
		if (opS.isPresent()) {
			spent=opS.get();
			rp = new ResponseEntity<Spent>(spent, HttpStatus.OK);
		}

		return rp;
	}
}
