package com.boup.boup.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.dto.FireBaseNot;
import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

@Service
public class DebtServiceImp implements DebtService {

	@Autowired
	GroupRepository groupR;
	@Autowired
	SpentRepository spentR;
	@Autowired
	UserRepository userR;
	@Autowired
	DebtRepository debtR;
	@Autowired
	UserService userS;
	@Autowired
	FireBaseService fireBase;

	@Override
	public Optional<Debt> insert(Debt d) {

		return Optional.of(debtR.save(d));
	}

	@Override
	public Optional<Debt> update(Debt d) {

		Optional<Debt> op = Optional.empty();
		if (debtR.existsById(d.getId())) {
			op = Optional.of(debtR.save(d));
		}
		return op;
	}

	@Override
	public boolean delete(Integer id) {

		boolean exit = false;
		if (debtR.existsById(id)) {
			debtR.deleteById(id);
			exit = true;
		}
		return exit;
	}

	@Override
	public List<Debt> findAll() {

		return debtR.findAll();
	}

	@Override
	public List<Debt> findByUser(User u) {

		return debtR.findByUser(u);
	}

	@Override
	public Optional<Debt> findByUserAndGroup(User u, Group g) {

		return debtR.findByUserAndGroup(u, g);
	}

	@Override
	public Optional<Debt> findById(Integer id) {

		return debtR.findById(id);
	}

	@Override
	public List<User> findGroupUsers(Group g) {

		List<Debt> debts = debtR.findByGroup(g);
		List<User> users = new ArrayList<>();
		debts.forEach(d -> users.add(d.getUser()));

		return users;
	}

	@Override
	public List<Group> findUserGroups(User user) {

		List<Debt> debts = debtR.findByUser(user);
		List<Group> groups = new ArrayList<>();
		debts.forEach(d -> groups.add(d.getGroup()));

		return groups;
	}

	@Override
	public List<Debt> findGroupDebts(Group group) {
		return debtR.findByGroup(group);
	}

	@Override
	public List<Debt> findUserDebts(User user) {
		return debtR.findByUser(user);
	}

	@Override
	public Optional<Debt> pay(Debt d) {
		Double cant = Math.abs(d.getAmount());
		User u = d.getUser();
		Group g = d.getGroup();
		Optional<Debt> opD;

		if (d.getAmount() < 0 && u.getWallet() - cant >= 0) {

			d.setAmount(0D);
			d.getUser().setWallet(u.getWallet() - cant);			

			opD=update(d);
						
			List<Debt> debts = findGroupDebts(g);
			debts.sort((d1, d2) -> Double.compare(d2.getAmount(), d1.getAmount()));

			for (int i = 0; i < debts.size() && cant > 0D; i++) {

				Debt de = debts.get(i);
				
				if (de.getAmount() >= cant) {

					de.setAmount(de.getAmount() - cant);
					de.getUser().setWallet(de.getUser().getWallet() + cant);
					sendNotification(de.getUser(),de.getGroup(),cant);
					cant=0D;
				} else {
					double diff = de.getAmount();
					de.setAmount(0D);
					de.getUser().setWallet(de.getUser().getWallet() + diff);
					sendNotification(de.getUser(),de.getGroup(),diff);
					cant = cant - diff;
				}

				update(de);
			}
		} else {
			opD = Optional.empty();
		}

		return opD;
	}

	private void sendNotification(User user, Group group, Double cant) {
        DecimalFormat df = new DecimalFormat("#.##");
		FireBaseNot not=new FireBaseNot();
		not.setToken(user.getToken());
		not.setTittle("Has recibido un pago");
		not.setBody("Has recibido "+ df.format(cant)+ "€ del grupo "+group.getGroupName()+".");
		fireBase.sendNotification(not);
	}

}
