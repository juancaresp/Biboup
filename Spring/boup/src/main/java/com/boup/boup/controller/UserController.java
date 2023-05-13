package com.boup.boup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boup.boup.dto.AddWallet;
import com.boup.boup.dto.UserReg;
import com.boup.boup.dto.UserUpd;
import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	
	//CRUD
		
	@PostMapping("/insert")
	public ResponseEntity<User> insertUser(@RequestBody User user) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		
		Optional<User> opU=userS.insert(user);
		if(opU.isPresent()) {
			user=opU.get();
			rp=new ResponseEntity<User>(user,HttpStatus.OK);
		}
		
		return rp;
	}

	@PostMapping("/delete")
	public ResponseEntity<User> deleteUser(@RequestBody User user) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		
		if(userS.delete(user.getId())) {
			rp=new ResponseEntity<User>(user,HttpStatus.OK);
		}
		
		return rp;
	}
	
	//List,getUser
	
	@GetMapping("")
	public ResponseEntity<List<User>> getUsers() {
		
		List<User> users=userS.findAll();
		
		ResponseEntity<List<User>> rp=new ResponseEntity<List<User>>(users,HttpStatus.OK);
		
		return rp;
	}
	
	//LLamadas pedidas
	
	@GetMapping("/id/{id}")
	public ResponseEntity<User> getUser(@PathVariable Integer id) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findById(id);
		if(user.isPresent()) {
			rp=new ResponseEntity<User>(user.get(),HttpStatus.OK);
		}
		
		return rp;
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findByNick(username);
		if(user.isPresent()) {
			rp=new ResponseEntity<User>(user.get(),HttpStatus.OK);
		}
		
		return rp;
	}
	
	@GetMapping("/email/{mail}")
	public ResponseEntity<User> getUserByMail(@PathVariable String mail) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findByEmail(mail);
		if(user.isPresent()) {
			rp=new ResponseEntity<User>(user.get(),HttpStatus.OK);
		}
		
		return rp;
	}
	
	@PostMapping("/add")
	public ResponseEntity<User> registerUser(@RequestBody UserReg userReg) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		
		
		Optional<User> user=userS.register(userReg);
		if(user.isPresent()) {
			rp=new ResponseEntity<User>(user.get(),HttpStatus.OK);
		}
		
		return rp;
	}
	
	@PostMapping("/{email}/update")
	public ResponseEntity<User> updateUser(@RequestBody UserUpd reg,@PathVariable String email) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		Optional<User> opU=userS.updateU(reg,email);
		
		if(opU.isPresent()) {
			User u =opU.get();
			rp=new ResponseEntity<User>(u,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@PostMapping("/wallet/add")
	public ResponseEntity<User> addWallet(@RequestBody AddWallet add) {
		
		ResponseEntity<User> rp=new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		Optional<User> opU=userS.addWallet(add);
		
		if(opU.isPresent()) {
			User u =opU.get();
			rp=new ResponseEntity<User>(u,HttpStatus.OK);
		}
		
		return rp;
	}
	
}
