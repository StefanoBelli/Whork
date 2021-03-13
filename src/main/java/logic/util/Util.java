package logic.util;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.favre.lib.crypto.bcrypt.BCrypt;

import logic.WhorkDesktopLauncher;
import logic.exception.SendMailException;

public final class Util {
	private static final String EMAIL_REGEX = "^(.+)@(.+)$";
	public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	private Util() {
	}

	public static void exceptionLog(Exception e) {
		Logger logger = LoggerFactory.getLogger("WhorkExceptionLogger");

		logger.error("***************************");
		logger.error("* EXCEPTION LOGGING START *");
		logger.error("***************************");
		logger.error("");

		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		logger.error("{} got called by: {}#{}()", trace[1].getMethodName(), trace[2].getClassName(),
				trace[2].getMethodName());

		logger.error("++++++++++++STACK TRACE++++++++++++");
		e.printStackTrace();
		logger.error("++++++++++END STACK TRACE++++++++++");

		logger.error("");
		logger.error("*************************");
		logger.error("* EXCEPTION LOGGING END *");
		logger.error("*************************");
	}

	public static boolean isValidPort(int port) {
		return port >= 0 && port <= 65535;
	}

	public static final class Files {
		private Files() {}

		public static String readAll(File f) 
				throws IOException {
			StringBuilder builder = new StringBuilder();

			try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line).append('\n');
				}
			}

			return builder.toString();
		}

		public static void overWrite(String content, File f) 
				throws IOException {

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
				writer.write(content);
			}
		}

		public static void overWriteJsonAuth(String email, String password) 
				throws IOException {
			File f = new File(WhorkDesktopLauncher.AUTH_FILE_PATH);

			if (!f.exists() && !f.createNewFile()) {
				throw new IOException();
			}

			JSONObject obj = new JSONObject();
			obj.put("email", email);
			obj.put("password", password);

			overWrite(obj.toString(), f);
		}
	}

	public static final class Bcrypt {
		private Bcrypt() {
		}

		public static byte[] hash(String clearText) {
			return BCrypt.withDefaults().hash(12, clearText.toCharArray());
		}

		public static boolean equals(String clearText, byte[] hash2) {
			return BCrypt.verifyer().verify(clearText.getBytes(), hash2).verified;
		}
	}
	
	public static final class Mailer {
		private Mailer() {
		}

		private static final Logger LOGGER = LoggerFactory.getLogger("Mailer");

		private static MailSender mailSender = null;

		private static boolean doChecks() {
			if (mailSender == null) {
				LOGGER.error("unable to send mail: no mail sender specified");
				return false;
			}

			if (mailSender.getFrom() == null) {
				LOGGER.error("unable to send mail: no from address specified");
				return false;
			}

			if (mailSender.getHost() == null) {
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
			if (port != null) {
				properties.put("mail.smtp.port", port);
			}

			Session session;

			String password = mailSender.getPassword();
			if (password != null) {
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

		public static void sendMail(String to, String subject, String textBody) throws SendMailException {
			if (!doChecks()) {
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
}
