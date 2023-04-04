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

import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;

@Controller
public class UserController {

	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	
	//CRUD
		
	@PostMapping("/user/insert")
	public ResponseEntity<User> insertUser(@RequestBody User user) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		
		Optional<User> opU=userS.insert(user);
		if(opU.isPresent()) {
			user=opU.get();
			rp=new ResponseEntity<User>(user,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@PostMapping("/user/update")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		Optional<User> opU=userS.update(user);
		if(opU.isPresent()) {
			user=opU.get();
			rp=new ResponseEntity<User>(user,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@PostMapping("/user/delete")
	public ResponseEntity<User> deleteUser(@RequestBody User user) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		
		if(userS.delete(user.getId())) {
			rp=new ResponseEntity<User>(user,HttpStatus.OK);
		}
		
		return rp;
	}
	
	//List,getUser
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		
		List<User> users=userS.findAll();
		
		ResponseEntity<List<User>> rp=new ResponseEntity<List<User>>(users,HttpStatus.OK);
		
		return rp;
	}
	
	@GetMapping("/user")
	public ResponseEntity<User> getUser(@RequestBody Integer id) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findById(id);
		if(user.isPresent()) {
			rp=new ResponseEntity<User>(user.get(),HttpStatus.OK);
		}
		
		return rp;
	}
	
}
