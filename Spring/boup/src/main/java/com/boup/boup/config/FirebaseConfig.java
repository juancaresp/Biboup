package com.boup.boup.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FirebaseConfig {

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		GoogleCredentials cred= GoogleCredentials.fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());
		FirebaseOptions opt= FirebaseOptions.builder().setCredentials(cred).build();
		FirebaseApp app=FirebaseApp.initializeApp(opt,"my-app");
		
		return FirebaseMessaging.getInstance(app);
	}
	
}
