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

import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
	
	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	// CRUD

	@PutMapping("")
	public ResponseEntity<Group> updateGroup(@RequestBody Group group) {

		ResponseEntity<Group> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<Group> opG=groupS.update(group);
		if (opG.isPresent()) {
			group=opG.get();
			rp = new ResponseEntity<>(group, HttpStatus.OK);
		}

		return rp;
	}

	// List,getgroup
	
	@GetMapping("")
	public ResponseEntity<List<Group>> getGroups() {
	
		List<Group> groups = groupS.findAll();
	
		return new ResponseEntity<>(groups, HttpStatus.OK);
	}

	@DeleteMapping("")
	public ResponseEntity<Group> deleteGroup(@RequestBody Group group) {

		ResponseEntity<Group> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		if (groupS.delete(group.getId())) {
			rp = new ResponseEntity<>(group, HttpStatus.OK);
		}

		return rp;
	}

	// List,getgroup

	@GetMapping("/{id}")
	public ResponseEntity<Group> getGroupById(@PathVariable String id) {

		ResponseEntity<Group> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		Optional<Group> group = groupS.findById(Integer.parseInt(id));
		if (group.isPresent()) {
			rp = new ResponseEntity<>(group.get(), HttpStatus.OK);
		}

		return rp;
	}

	//Llamadas pedidas
	
	@GetMapping("/{groupid}/users")
	public ResponseEntity<List<User>> getGroupUsers(@PathVariable String groupid) {

		ResponseEntity<List<User>> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<Group> op=groupS.findById(Integer.parseInt(groupid));
		
		if(op.isPresent()) {
			
			List<User> users=debtS.findGroupUsers(op.get());	
			rp= new ResponseEntity<>(users,HttpStatus.OK);
		}

		return rp;
	}
	
	@PostMapping("/{groupname}/user/{username}")
	public ResponseEntity<Group> createGroup(@PathVariable String groupname,@PathVariable String username){
		ResponseEntity<Group> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Group group=new Group();
		group.setGroupName(groupname);
		
		Optional<Group> opG=groupS.insert(group);
		
		if (opG.isPresent()) {
			group=opG.get();
			
			rp = addUserToGroup(group.getId(), username);
		}
		
		return rp;
	}
	
	@PutMapping("/{idgroup}/addUser/{username}")
	public ResponseEntity<Group> addUserToGroup(@PathVariable("idgroup") Integer idgroup, @PathVariable("username") String username){
		ResponseEntity<Group> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<Group> opG=groupS.findById(idgroup);
		Optional<User> opU=userS.findByNick(username);
		
		if (opG.isPresent() && opU.isPresent()) {
			
			List<User> users=debtS.findGroupUsers(opG.get());
			
			if(!users.contains(opU.get())) {
				Debt d=new Debt();
				d.setGroup(opG.get());
				d.setUser(opU.get());
				d.setAmount(0.0);
				
				debtS.insert(d);
				
				rp = new ResponseEntity<>(opG.get(), HttpStatus.OK);
			}
		}
		
		return rp;
	}
	
	@PutMapping("/{idgroup}/removeUser/{username}")
	public ResponseEntity<Group> removeUserFromGroup(@PathVariable("idgroup") Integer idgroup, @PathVariable("username") String username){
		ResponseEntity<Group> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<Group> opG=groupS.findById(idgroup);
		Optional<User> opU=userS.findByNick(username);
		
		if (opG.isPresent() && opU.isPresent()) {
			
			Debt d=debtS.findByUserAndGroup(opU.get(), opG.get()).orElse(new Debt());
			debtS.delete(d.getId());
			rp = new ResponseEntity<>(opG.get(), HttpStatus.OK);
		}
		
		return rp;
	}
	
	
	@GetMapping("/{groupid}/debts")
	public ResponseEntity<List<Debt>> getGroupDebts(@PathVariable Integer groupid) {

		ResponseEntity<List<Debt>> rp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<Group> op=groupS.findById(groupid);
		
		if(op.isPresent()) {
			
			List<Debt> debts=debtS.findGroupDebts(op.get());	
			rp= new ResponseEntity<>(debts,HttpStatus.OK);
		}

		return rp;
	}
}
