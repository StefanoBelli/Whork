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
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.favre.lib.crypto.bcrypt.BCrypt;

import logic.exception.SendMailException;

public final class Util {
	private Util() {}

	private static final String EMAIL_REGEX = "^(.+)@(.+)$";
	private static final String FC_REGEX = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
	public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	public static final Pattern FC_PATTERN = Pattern.compile(FC_REGEX);
	public static final String INADDR_ANY = "0.0.0.0";
	public static final String WORK_SHIFT_REGEX = "^[0-2][0-9]:[0-5][0-9] - [0-2][0-9]:[0-5][0-9]$";
	public static final Pattern WORK_SHIFT_PATTERN = Pattern.compile(WORK_SHIFT_REGEX);
	
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

	public static String generateToken() {
		StringBuilder builder = new StringBuilder();
		builder.append(Long.toString(new Date().getTime()));
		builder.append("-");
		builder.append(UUID.randomUUID().toString());

		return builder.toString();
	}

	public static boolean isValidPort(int port) {
		return port >= 0 && port <= 65535;
	}

	public static String simpleHttpGet(String urlStr) 
			throws IOException {
		StringBuilder responseBuilder = new StringBuilder();

		URL url = new URL(urlStr);
		try(
			BufferedReader in = new BufferedReader(
									new InputStreamReader(
										url.openConnection().getInputStream()))
		) {
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				responseBuilder.append(inputLine);
			}
		}
		
		return responseBuilder.toString();
	}

	private static String getBirthMonthFromEncoding(char c) {
		switch(c) {
			case('A'): return "01";
			case('B'): return "02";
			case('C'): return "03";
			case('D'): return "04";
			case('E'): return "05";
			case('H'): return "06";
			case('L'): return "07";
			case('M'): return "08";
			case('P'): return "09";
			case('R'): return "10";
			case('S'): return "11";
			case('T'): return "12";
			default: throw new IllegalArgumentException("Invalid month encoding for fiscal code");
		}
	}

	public static Date deriveBirthdayFromFiscalCode(String fiscalCode) 
			throws IllegalArgumentException {
		String day = fiscalCode.substring(9, 11); // DAY
		int dayIntegerLess40 = Integer.parseInt(day) - 40;
		
		if (dayIntegerLess40 >= 0) {
			day = String.valueOf(dayIntegerLess40);
		}
		
		String month = getBirthMonthFromEncoding(fiscalCode.charAt(8)); // MONTH

		String year = fiscalCode.substring(6, 8); //YEAR

		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
		dateFormat.setLenient(false);		

		try {
			return dateFormat.parse(new StringBuilder()
				.append(day).append("/").append(month).append("/").append(year).toString());
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unable to parse date for fiscal code");
		}
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
			File f = new File(InstanceConfig.getString(InstanceConfig.KEY_AUTH_FILE_PATH));

			if (!f.exists() && !f.createNewFile()) {
				throw new IOException();
			}

			JSONObject obj = new JSONObject();
			obj.put("email", email);
			obj.put("password", password);

			overWrite(obj.toString(), f);
		}

		public static String saveUserFile(String userCf, File file) 
				throws IOException {
			if(file == null) {
				return null;
			}
			
			String fileName = new StringBuilder().append(userCf).append("_-_")
					.append(Long.toString(new Date().getTime())).append("_-_").append(file.getName()).toString();

			String completeFileName = new StringBuilder()
					.append(Util.InstanceConfig.getString(Util.InstanceConfig.KEY_USR_DATA)).append("/")
					.append(fileName).toString();

			File newSubmittedFile = new File(completeFileName);
			if (newSubmittedFile.createNewFile()) {
				try (InputStream istream = new BufferedInputStream(new FileInputStream(file))) {
					try (OutputStream ostream = new BufferedOutputStream(new FileOutputStream(newSubmittedFile))) {
						ostream.write(istream.readAllBytes());
					}
				}
			} else {
				throw new IOException();
			}

			return fileName;
		}
	}

	public static final class Bcrypt {
		private Bcrypt() {}

		public static byte[] hash(String clearText) {
			return BCrypt.withDefaults().hash(12, clearText.toCharArray());
		}

		public static boolean equals(String clearText, byte[] hash2) {
			return BCrypt.verifyer().verify(clearText.getBytes(), hash2).verified;
		}
	}
	
	public static final class Mailer {
		private Mailer() {}

		private static final Logger LOGGER = LoggerFactory.getLogger("Mailer");

		private static final String MAIL_FROM = InstanceConfig.getString(InstanceConfig.KEY_MAILFROM);
		private static final String MAIL_HOST = InstanceConfig.getString(InstanceConfig.KEY_MAILHOST);
		private static final String MAIL_PWD = InstanceConfig.getString(InstanceConfig.KEY_MAILPWD);
		private static final String MAIL_SMTP_PORT = InstanceConfig.getString(InstanceConfig.KEY_MAILSMTP_PORT);
		private static final boolean MAIL_TLS = InstanceConfig.getBoolean(InstanceConfig.KEY_MAILTLS);
		
		private static boolean doChecks() {
			if (MAIL_FROM == null) {
				LOGGER.error("unable to send mail: no from address specified");
				return false;
			}

			if (MAIL_HOST == null) {
				LOGGER.error("unable to send mail: no host specified");
				return false;
			}

			return true;
		}

		private static Session getSession() {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", MAIL_HOST);
			properties.put("mail.smtp.starttls.enable", MAIL_TLS);

			String port = MAIL_SMTP_PORT;
			if (port != null) {
				properties.put("mail.smtp.port", port);
			}

			Session session;

			String password = MAIL_PWD;
			if (password != null) {
				properties.put("mail.smtp.auth", "true");
				session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(MAIL_FROM, password);
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
				message.setFrom(new InternetAddress(MAIL_FROM));
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
	}

	public static final class InstanceConfig {
		private InstanceConfig() {}

		public static final String KEY_MAILFROM = "any.mail.from";
		public static final String KEY_MAILHOST = "any.mail.host";
		public static final String KEY_MAILPWD = "any.mail.pwd";
		public static final String KEY_MAILTLS = "any.mail.tls";
		public static final String KEY_MAILSMTP_PORT = "any.mail.smtp_port";
		public static final String KEY_DFL_ROOT = "any.dfl_root";
		public static final String KEY_AUTH_FILE_PATH = "desktop.auth_file_path";
		public static final String KEY_SVC_CHAT_PORT = "any.service.chat_port";
		public static final String KEY_SVC_INTVL_TOK = "any.service.intvl_tok";
		public static final String KEY_SVC_EDITOR_PORT = "any.service.editor_port";
		public static final String KEY_USR_DATA = "any.usr_data";
		public static final String KEY_CTX_USR_DATA = "web.tomcat.usr_data_context";
		public static final String KEY_CTX_DFL_ROOT = "web.tomcat.dfl_root_context";

		private static Map<String, Object> config = new HashMap<>();
		
		public static void setConf(String key, Object value) {
			config.put(key, value);
		}

		public static String getString(String key) {
			return (String) get(key);
		}

		public static boolean getBoolean(String key) {
			return (boolean) get(key);
		}

		public static int getInt(String key) {
			return (int) get(key);
		}

		public static Object get(String key) {
			return config.get(key);
		}
	}
}
