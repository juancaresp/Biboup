package com.boup.boup.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.dto.FireBaseNot;
import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.Spent;
import com.boup.boup.model.User;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

@Service
public class SpentServiceImp implements SpentService {

	@Autowired
	GroupRepository groupR;
	@Autowired
	SpentRepository spentR;
	@Autowired
	UserRepository userR;
	@Autowired
	DebtRepository debtR;
	@Autowired
	DebtService debtS;
	@Autowired
	GroupService groupS;
	@Autowired
	FireBaseService fireBase;

	@Override
	public Optional<Spent> insert(Spent s) {

		Optional<Spent> op = Optional.of(spentR.save(s));

		return op;
	}

	@Override
	public Optional<Spent> update(Spent s) {
		Optional<Spent> op = Optional.empty();

		if (!spentR.existsById(s.getId())) {
			op = Optional.of(spentR.save(s));
		}
		return op;
	}

	@Override
	public boolean delete(Integer id) {
		boolean exit = false;
		if (spentR.existsById(id)) {
			spentR.deleteById(id);
			exit = true;
		}
		return exit;
	}

	@Override
	public List<Spent> findAll() {
		// TODO Auto-generated method stub
		return (List<Spent>) spentR.findAll();
	}

	@Override
	public Optional<Spent> findById(Integer id) {
		// TODO Auto-generated method stub
		return spentR.findById(id);
	}

	@Override
	public List<Spent> findByGroup(Integer id) {
		// TODO Auto-generated method stub
		return spentR.findByGroupId(id);
	}

	

	@Override
	public void deleteUserSpent(Integer spentId, Integer userid) {
		// TODO Auto-generated method stub
		spentR.findById(spentId).ifPresent(s -> {
			User u = userR.findById(userid).orElse(new User());
			s.getUsers().remove(u);
			spentR.save(s);
		});
		;

	}

	@Override
	public void addUserSpent(Integer spentId, Integer userid) {
		// TODO Auto-generated method stub
		spentR.findById(spentId).ifPresent(s -> {
			User u = userR.findById(userid).orElse(new User());
			s.getUsers().add(u);
			spentR.save(s);
		});
		;

	}

	@Override
	public List<Spent> findByUser(String username) {
		return spentR.findByPayerUsernameOrUsersUsername(username, username);
	}

	@Override
	public boolean deleteSpent(int id) {
		Optional<Spent> opS = spentR.findById(id);

		opS.ifPresent((sp) -> {
			Double part = sp.getQuantity() / (sp.getUsers().size() + 1);
			groupS.findById(sp.getGroup().getId()).ifPresent(g -> {
				Debt d = debtS.findByUserAndGroup(sp.getPayer(), g).orElse(new Debt());
				d.setAmount(d.getAmount() - sp.getQuantity() + part);
				debtR.save(d);

				sp.getUsers().forEach(u -> {
					Debt de = debtS.findByUserAndGroup(u, g).orElse(new Debt());
					de.setAmount(de.getAmount() + part);
					debtR.save(de);
				});
				spentR.deleteById(id);
			});
			;
		});

		return !spentR.existsById(id);
	}

	@Override
	public Optional<Spent> addSpent(Spent spent) {

		Optional<Spent> spe = insert(spent);

		spe.ifPresent((sp) -> {
			Double part = sp.getQuantity() / (sp.getUsers().size() + 1);
			groupS.findById(sp.getGroup().getId()).ifPresent(g -> {
				Debt d = debtS.findByUserAndGroup(sp.getPayer(), g).orElse(new Debt());
				d.setAmount(d.getAmount() + sp.getQuantity() - part);
				debtR.save(d);
				sp.getUsers().removeIf(u-> u.equals(sp.getPayer()));
				sp.getUsers().forEach(u -> {
					Debt de = debtS.findByUserAndGroup(u, g).orElse(new Debt());
					de.setAmount(de.getAmount() - part);
					debtR.save(de);
					sendNotification(u, g,sp, part);
				});

			});
			;
		});

		return spe;
	}
	
	@Override
	public Optional<Spent> updateSpent(Spent spent) {
		Optional<Spent> opS=spentR.findById(spent.getId());
		
		if(opS.isPresent()) {
			if(deleteSpent(opS.get().getId())) {
				opS=addSpent(spent);
			}
		}
		
		return opS;
	}

	private void sendNotification(User user, Group group, Spent sp, Double cant) {
        DecimalFormat df = new DecimalFormat("#.##");
		FireBaseNot not=new FireBaseNot();
		not.setToken(user.getToken());
		not.setTittle("Tienes un nuevo gasto");
		not.setBody("El gasto"+sp.getSpentName()+" de "+ df.format(cant)+ "€ en el grupo "+group.getGroupName()+" por"+sp.getSpentDesc()+".");
		fireBase.sendNotification(not);
	}
}
