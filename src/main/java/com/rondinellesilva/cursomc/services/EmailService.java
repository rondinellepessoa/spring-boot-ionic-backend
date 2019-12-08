package com.rondinellesilva.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.rondinellesilva.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
