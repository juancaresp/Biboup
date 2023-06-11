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
		//Cogemos la cantidad de la deuda 
		Double cant =d.getAmount();
		Optional<Debt> opD=Optional.empty();
		//Si la dantidad de la deuda es menor que 0 la ponemos en positivo
		if(cant<0) {
			cant=Math.abs(d.getAmount());
			
			User u = d.getUser();
			Group g = d.getGroup();
			//Si tiene suficiente dinero en su cartera
			if (u.getWallet() - cant >= 0) {
				//ponemos su deuda a 0 en ese grupo
				d.setAmount(0D);
				//Y le restamos de su cartera la cantidad que debia 
				d.getUser().setWallet(u.getWallet() - cant);
				//Actualizamos la deuda
				opD=update(d);
				//Cogemos las demas deudas de ese grupo
				List<Debt> debts = findGroupDebts(g);
				//Borramos de la lista la del usuario que va a pagar
				debts.removeIf(debt->debt.getUser().equals(d.getUser()));
				//ordenamos la lista de mayor a menor
				debts.sort((d1, d2) -> Double.compare(d2.getAmount(), d1.getAmount()));
				//Recorremos la lista hasta que se acabe o hasta que la cantidad sea 0
				for (int i = 0; i < debts.size() && cant > 0D; i++) {
					//Cogemos la deuda por la que vamos
					Debt de = debts.get(i);
					//si la cantidad que se le debe en esta deuda es mayor que la que va a pagar el usuario que esta pagando
					if (de.getAmount() >= cant) {
						//Se le quita de la deuda la cantidad que le han pagado
						de.setAmount(de.getAmount() - cant);
						//se le añade en la cartera al usuario la cantidad que se le ha pagado
						de.getUser().setWallet(de.getUser().getWallet() + cant);
						//Se le envia una notificacion avisandolo de que se ha recibido un pago
						sendNotification(de.getUser(),de.getGroup(),cant);
						// y se pone la cantidad que queda por repartir a 0 para que salga del bucle
						cant=0D;
					} else {
						//si la cantidad que se le debe a este usuario en total es menor que lo que 
						//va a pagar el que esta saldando la deuda se calcula lo que se le va a pagar
						double diff = de.getAmount();
						//En la deuda se le pone que no se le debe nada en ese grupo
						de.setAmount(0D);
						//Y añadimos a su cartera la cantidad que ha saldado el otro usuario
						de.getUser().setWallet(de.getUser().getWallet() + diff);
						//Le mandamos una notificacion para avisarle de la transaccion
						sendNotification(de.getUser(),de.getGroup(),diff);
						//Reducimos la cantidad de lo que nos queda por repartir entre el resto de usuarios
						cant = cant - diff;
					}
					//en ambos casos actualizamos la deuda
					update(de);
				}
			}
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
