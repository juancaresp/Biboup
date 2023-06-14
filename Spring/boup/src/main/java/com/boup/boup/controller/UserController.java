package com.boup.boup.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boup.boup.dto.AddWallet;
import com.boup.boup.dto.UserReg;
import com.boup.boup.dto.UserUpd;
import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;
import com.boup.boup.service.DebtService;
import com.boup.boup.service.GroupService;
import com.boup.boup.service.SpentService;
import com.boup.boup.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired UserService userS;
	@Autowired DebtService debtS;
	@Autowired SpentService spentS;
	@Autowired GroupService groupS;
	
	//delete

	@DeleteMapping("")
	public ResponseEntity<User> deleteUser(@RequestBody User user) {
		
		ResponseEntity<User> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if(userS.delete(user.getId())) {
			rp=new ResponseEntity<>(user,HttpStatus.OK);
		}
		
		return rp;
	}
	
	//List,getUser
	
	@GetMapping("")
	public ResponseEntity<List<User>> getUsers() {
		
		List<User> users=userS.findAll();
		
		return new ResponseEntity<>(users,HttpStatus.OK);
	}
	
	//LLamadas pedidas
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable Integer id) {
		
		ResponseEntity<User> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findById(id);
		if(user.isPresent()) {
			rp=new ResponseEntity<>(user.get(),HttpStatus.OK);
		}
		
		return rp;
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
		
		ResponseEntity<User> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findByNick(username);
		if(user.isPresent()) {
			rp=new ResponseEntity<>(user.get(),HttpStatus.OK);
		}
		
		return rp;
	}
	
	@GetMapping("/email/{mail}")
	public ResponseEntity<User> getUserByMail(@PathVariable String mail) {
		
		ResponseEntity<User> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findByEmail(mail);
		if(user.isPresent()) {
			rp=new ResponseEntity<>(user.get(),HttpStatus.OK);
		}
		
		return rp;
	}
	
	@PostMapping("")
	public ResponseEntity<User> registerUser(@RequestBody UserReg userReg) {
		
		ResponseEntity<User> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		
		Optional<User> user=userS.register(userReg);
		if(user.isPresent()) {
			rp=new ResponseEntity<>(user.get(),HttpStatus.OK);
		}
		
		return rp;
	}
	
	@PutMapping("/{email}")
	public ResponseEntity<User> updateUser(@RequestBody UserUpd reg,@PathVariable String email) {
		
		ResponseEntity<User> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Optional<User> opU=userS.updateU(reg,email);
		
		if(opU.isPresent()) {
			User u =opU.get();
			rp=new ResponseEntity<>(u,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@PatchMapping("/wallet/add")
	public ResponseEntity<User> addWallet(@RequestBody AddWallet add) {
		
		ResponseEntity<User> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Optional<User> opU=userS.addWallet(add);
		
		if(opU.isPresent()) {
			User u =opU.get();
			rp=new ResponseEntity<>(u,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@GetMapping("/{username}/groups")
	public ResponseEntity<List<Group>> getUserGroups(@PathVariable String username) {
		
		ResponseEntity<List<Group>> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findByNick(username);
		if(user.isPresent()) {
			List<Group> groups=debtS.findUserGroups(user.get());
			
			rp=new ResponseEntity<>(groups,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@GetMapping("/{username}/debts")
	public ResponseEntity<List<Debt>> getUserDebts(@PathVariable String username) {
		
		ResponseEntity<List<Debt>> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findByNick(username);
		List<Debt> l=debtS.findUserDebts(user.get());
		l.sort((d1,d2)-> d2.getGroup().getGroupName().compareTo(d1.getGroup().getGroupName()));
		if(user.isPresent()) {
			rp=new ResponseEntity<>(l,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@GetMapping("/{username}/debts/win")
	public ResponseEntity<List<Debt>> getUserDebtsPositive(@PathVariable String username) {
		
		ResponseEntity<List<Debt>> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findByNick(username);
		if(user.isPresent()) {
			List<Debt> groups=debtS.findUserDebts(user.get());
			groups.sort((d1,d2)-> Double.compare(d2.getAmount(), d1.getAmount()));
			groups.removeIf(d->d.getAmount()<0);
			rp=new ResponseEntity<>(groups,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@GetMapping("/{username}/debts/lose")
	public ResponseEntity<List<Debt>> getUserDebtsNegative(@PathVariable String username) {
		
		ResponseEntity<List<Debt>> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findByNick(username);
		if(user.isPresent()) {
			List<Debt> groups=debtS.findUserDebts(user.get());
			groups.sort((d1,d2)-> Double.compare(d1.getAmount(), d2.getAmount()));
			groups.removeIf(d->d.getAmount()>=0);
			rp=new ResponseEntity<>(groups,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@PatchMapping("/email/{mail}/token/{token}")
	public ResponseEntity<User> getUpdateToken(@PathVariable String mail,@PathVariable String token) {
		
		ResponseEntity<User> rp=new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Optional<User> user=userS.findByEmail(mail);
		if(user.isPresent()) {
			User u=user.get();
			u.setToken(token);
			userS.update(u);
			rp=new ResponseEntity<>(u,HttpStatus.OK);
		}
		
		return rp;
	}
	
	@GetMapping("/suggest/{username}")
	public ResponseEntity<List<String>> getUsernamesSuggest(@PathVariable String username) {
		
		return new ResponseEntity<>(userS.findBySuggestions(username),HttpStatus.OK);
	}
}
