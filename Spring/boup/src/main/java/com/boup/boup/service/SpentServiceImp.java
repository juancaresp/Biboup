package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.model.Debt;
import com.boup.boup.model.Spent;
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

	@Override
	public Optional<Spent> insert(Spent s) {
		System.out.println(s);
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
	public Optional<Spent> addSpent(Spent spent) {
		
		Optional<Spent> spe = Optional.empty();
		spe = insert(spent);
		System.out.println(spe.get());
		spe.ifPresent((sp) -> {
			Double part = sp.getQuantity() / (sp.getUsers().size() + 1);
			System.out.println(part);
			Debt deb;

			for (int i = 0; i < sp.getUsers().size(); i++) {
				deb = Debt.builder()
						.receiver(sp.getPayer())
						.debtor(sp.getUsers().get(i))
						.amount(part)
						.debtGroup(sp.getGroup())
						.build();
				debtS.addDebt(deb);
			}
		});
		// Each one part

		return spe;

	}

}
