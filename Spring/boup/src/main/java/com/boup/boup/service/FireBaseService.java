package com.boup.boup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.dto.FireBaseNot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FireBaseService {

	@Autowired FirebaseMessaging fireBase;
	
	public boolean sendNotification(FireBaseNot notification) {
		boolean exit=false;
		
		Notification not=Notification.builder()
				.setTitle(notification.getTittle())
				.setBody(notification.getBody())
				.setImage(notification.getImage())
				.build();
		Message mes=Message.builder()
				.setToken(notification.getToken())
				.setNotification(not)
				.build();
		try {
			fireBase.send(mes);
			exit=true;
		}catch (FirebaseMessagingException e) {
			//not sended
		}
		return exit;
	}
}
