package com.dmk.poatp1back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import java.util.Properties;

@SpringBootApplication
public class PoaTp1BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoaTp1BackApplication.class, args);
	}

}
