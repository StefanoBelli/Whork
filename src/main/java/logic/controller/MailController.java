package logic.controller;

import java.util.Date;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.exception.SendMailException;
import logic.util.Util;

public final class MailController {
	private MailController() {}
	
	private static final Logger LOGGER = LoggerFactory.getLogger("MailController");

	private static MailSender mailSender = null;

	private static boolean doChecks() {
		if(mailSender == null) {
			LOGGER.error("unable to send mail: no mail sender specified");
			return false;
		}

		if(mailSender.getFrom() == null) {
			LOGGER.error("unable to send mail: no from address specified");
			return false;
		}

		if(mailSender.getHost() == null) {
			LOGGER.error("unable to send mail: no host specified");
			return false;
		}

		return true;
	}

	private static Session getSession() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", mailSender.getHost());
		properties.put("mail.smtp.starttls.enable", mailSender.getTls());

		String port = mailSender.getPort();
		if(port != null) {
			properties.put("mail.smtp.port", port);
		}

		Session session;

		String password = mailSender.getPassword();
		if(password != null) {
			properties.put("mail.smtp.auth", "true");
			session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailSender.getFrom(), password);
				}
			});
		} else {
			session = Session.getDefaultInstance(properties);
		}

		return session;
	}

	public static void sendMail(String to, String subject, String textBody) 
			throws SendMailException {
		if(!doChecks()) {
			throw new SendMailException();
		}

		Session session = getSession();
		MimeMessage message = new MimeMessage(session);
		
		try {
			message.setFrom(new InternetAddress(mailSender.getFrom()));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(textBody);
			message.setSentDate(new Date());
			
			Transport.send(message);
		} catch (MessagingException e) {
			Util.exceptionLog(e);
			throw new SendMailException();
		}
	}

	public static void setMailSender(MailSender sender) {
		mailSender = sender;
	}
}
