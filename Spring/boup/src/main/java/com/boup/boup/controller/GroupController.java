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

import com.boup.boup.model.Group;
import com.boup.boup.model.Spent;
import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;

@Controller
public class GroupController {
	
	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	// CRUD

	@PostMapping("/group/insert")
	public ResponseEntity<Group> insertGroup(@RequestBody Group group) {

		ResponseEntity<Group> rp = new ResponseEntity<Group>(HttpStatus.BAD_REQUEST);
		
		Optional<Group> opG=groupS.insert(group);
		if (opG.isPresent()) {
			group=opG.get();
			rp = new ResponseEntity<Group>(group, HttpStatus.OK);
		}

		return rp;
	}

	@PostMapping("/group/update")
	public ResponseEntity<Group> updateGroup(@RequestBody Group group) {

		ResponseEntity<Group> rp = new ResponseEntity<Group>(HttpStatus.BAD_REQUEST);
		
		Optional<Group> opG=groupS.update(group);
		if (opG.isPresent()) {
			group=opG.get();
			rp = new ResponseEntity<Group>(group, HttpStatus.OK);
		}

		return rp;
	}

	@PostMapping("/group/delete")
	public ResponseEntity<Group> deleteGroup(@RequestBody Group group) {

		ResponseEntity<Group> rp = new ResponseEntity<Group>(HttpStatus.BAD_REQUEST);

		if (groupS.delete(group.getId())) {
			rp = new ResponseEntity<Group>(group, HttpStatus.OK);
		}

		return rp;
	}

	// List,getgroup

	@GetMapping("/groups")
	public ResponseEntity<List<Group>> getGroups() {

		List<Group> groups = groupS.findAll();

		ResponseEntity<List<Group>> rp = new ResponseEntity<List<Group>>(groups, HttpStatus.OK);

		return rp;
	}

	@GetMapping("/group")
	public ResponseEntity<Group> getGroupById(@RequestBody Integer id) {

		ResponseEntity<Group> rp = new ResponseEntity<Group>(HttpStatus.BAD_REQUEST);

		Optional<Group> group = groupS.findById(id);
		if (group.isPresent()) {
			rp = new ResponseEntity<Group>(group.get(), HttpStatus.OK);
		}

		return rp;
	}

	//Other
	
	@GetMapping("/groupUsers")
	public ResponseEntity<List<User>> getGroupUsers(@RequestBody Integer groupId) {

		Group group = groupS.findById(groupId).orElse(new Group());

		ResponseEntity<List<User>> rp = new ResponseEntity<List<User>>((List<User>) group.getUsers(), HttpStatus.OK);

		return rp;
	}
	
	@PostMapping("/addSpent")
	public ResponseEntity<Spent> addSpent(@RequestBody Spent spent) {

		Spent spe=spentS.addSpent(spent).orElse(new Spent());

		ResponseEntity<Spent> rp = new ResponseEntity<Spent>(spe, HttpStatus.OK);

		return rp;
	}
}
