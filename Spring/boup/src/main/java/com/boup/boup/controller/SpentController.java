package com.boup.boup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boup.boup.model.Spent;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;

@RestController
@RequestMapping("/api/spents")
public class SpentController {

	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	// CRUD

	@PostMapping("/insert")
	public ResponseEntity<Spent> insertSpent(@RequestBody Spent spent) {

		ResponseEntity<Spent> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<Spent> opS=spentS.insert(spent);
		if (opS.isPresent()) {
			spent=opS.get();
			rp = new ResponseEntity<>(spent, HttpStatus.OK);
		}

		return rp;
	}

	@PostMapping("/update")
	public ResponseEntity<Spent> updateSpent(@RequestBody Spent spent) {

		ResponseEntity<Spent> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<Spent> opS=spentS.update(spent);
		if (opS.isPresent()) {
			spent=opS.get();
			rp = new ResponseEntity<>(spent, HttpStatus.OK);
		}

		return rp;
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Spent> deletSpent(@RequestBody Spent spent) {

		ResponseEntity<Spent> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		if (spentS.delete(spent.getId())) {
			rp = new ResponseEntity<>(spent, HttpStatus.OK);
		}

		return rp;
	}

	// List,getspent

	@GetMapping("")
	public ResponseEntity<List<Spent>> getSpents() {

		List<Spent> spents = spentS.findAll();

		ResponseEntity<List<Spent>> rp = new ResponseEntity<>(spents, HttpStatus.OK);

		return rp;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Spent> getSpentById(@PathVariable Integer id) {

		ResponseEntity<Spent> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		Optional<Spent> spent = spentS.findById(id);
		if (spent.isPresent()) {
			rp = new ResponseEntity<>(spent.get(), HttpStatus.OK);
		}

		return rp;
	}
	
	//Other
	
	@GetMapping("/group/{groupId}")
	public ResponseEntity<List<Spent>> getGroupSpents(@PathVariable String groupId) {
		ResponseEntity<List<Spent>> rp =new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if(groupS.findById(Integer.parseInt(groupId)).isPresent()) {
			List<Spent> spents = spentS.findByGroup(Integer.parseInt(groupId));

			rp = new ResponseEntity<>(spents, HttpStatus.OK);
		}
		return rp;
	}
	
	@GetMapping("/users/{username}")
	public ResponseEntity<List<Spent>> getUserSpents(@PathVariable String username) {
		
		ResponseEntity<List<Spent>> rp =new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if(userS.findByNick(username).isPresent()) {
			List<Spent> spents = spentS.findByUser(username);
			
			rp= new ResponseEntity<>(spents, HttpStatus.OK);
		}
		return rp;
	}
	
	
	//ADD a spent generating the debts
	@PostMapping("")
	public ResponseEntity<Spent> addSpent(@RequestBody Spent spent) {

		ResponseEntity<Spent> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		Optional<Spent> opS=spentS.addSpent(spent);
		if (opS.isPresent()) {
			spent=opS.get();
			rp = new ResponseEntity<>(spent, HttpStatus.OK);
		}

		return rp;
	}
	
	@DeleteMapping("/{idSpent}")
	public ResponseEntity<Spent> deleteSpent(@PathVariable String idSpent) {

		ResponseEntity<Spent> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if (spentS.deleteSpent(Integer.parseInt(idSpent))) {
			rp = new ResponseEntity<>(HttpStatus.OK);
		}

		return rp;
	}
	
	@PutMapping("")
	public ResponseEntity<Spent> updatSpent(@RequestBody Spent spent) {

		ResponseEntity<Spent> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Optional<Spent> opS=spentS.updateSpent(spent);
		if (opS.isPresent()) {
			rp = new ResponseEntity<>(opS.get(),HttpStatus.OK);
		}

		return rp;
	}
}
