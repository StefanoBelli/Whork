package logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.dao.ComuniDao;
import logic.exception.DataAccessException;
import logic.exception.DatabaseException;
import logic.util.Util;
import logic.util.MailSender;
import logic.dao.EmploymentStatusDao;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

final class App {
	// Logger tagged "WhorkStartup" for startup phase
	private static final Logger LOGGER = LoggerFactory.getLogger("WhorkStartup");

	// Cmdline opts
	private static final String BASEOPT = "base";
	private static final String WEBRESOPT = "webRes";
	private static final String WEBROOTOPT = "webRoot";
	private static final String HELPOPT = "help";
	private static final String PORTOPT = "port";
	private static final String DBCONNECTOPT = "dbConnect";
	private static final String DBUSEROPT = "dbUser";
	private static final String DBPWDOPT = "dbPwd";
	private static final String DESKTOPOPT = "desktop";
	private static final String MAILFROMOPT = "mailFrom";
	private static final String MAILPASSWORDOPT = "mailPassword";
	private static final String MAILSMTPPORTOPT = "mailSmtpPort";
	private static final String MAILNOTLSOPT = "mailNoTls";
	private static final String MAILHOSTOPT = "mailHost";
	private static final String DFLRESOPT = "dflRes";
	private static final String DFLROOTOPT = "dflRoot";

	// Self-extraction properties
	private static List<String> webResDirectory = null;
	private static List<String> webResFiles = null;
	private static List<String> dflResDirectory = null;
	private static List<String> dflResFiles = null;

	// Dynamic config for whork
	private static String webRoot = null;
	private static String dflRoot = null;
	private static String base = "";
	private static int port = 8080;
	private static boolean selfExtract = isJarPackaged();
	private static String dbConnect = "localhost:3306";
	private static String dbUser = null;
	private static String dbPwd = null;
	private static boolean launchDesktop = false;
	private static String mailTls = "true";
	private static String mailSmtpPort = "587";
	private static String mailFrom = null;
	private static String mailPwd = null;
	private static String mailHost = null;

	// Static config for whork
	private static final String DBNAME = "whorkdb";

	private static final String NOT_USING_PWDAUTH = "NOT using password authentication";
	private static final String HIDE_PWD = "[HIDDEN]";

	private static final class ArchiveSelfExtractor {
		private ArchiveSelfExtractor() {}

		private static void mkdirs(String basedir, List<String> dirs) {
			for (final String dir : dirs) {
				StringBuilder builder = new StringBuilder();
				builder.append(basedir);
				builder.append("/");
				builder.append(dir);

				new File(builder.toString()).mkdir();
			}
		}

		private static String touch(String basedir, String resfile) 
				throws IOException {
			StringBuilder builder = new StringBuilder();

			builder.append(basedir);
			builder.append(resfile);

			String path = builder.toString();
			File resFile = new File(path);

			if (!resFile.createNewFile())
				LOGGER.info("{} already exists", path);

			return path;
		}

		private static void extract(String dir, List<String> subdirs, List<String> files) 
				throws IOException {
			File f = new File(dir);
			f.mkdir();

			mkdirs(dir, subdirs);

			for(final String file : files) {
				String finalPath = touch(dir, file);

				try(BufferedInputStream istOrigin = new BufferedInputStream(
						App.class.getResourceAsStream(file))) {
					
					try(BufferedOutputStream ostDest = new BufferedOutputStream(
							new FileOutputStream(finalPath))) {

						ostDest.write(istOrigin.readAllBytes());
					}
				}
			}
		}
	}

	private enum AssignResources {
		ONLY_DEFAULTS,
		ALL
	}

	private static void setResources(AssignResources res) {
		dflResDirectory = new ArrayList<>();
		dflResFiles = new ArrayList<>();
		dflResFiles.add("/placeholder");
		dflResFiles.add("/placeholder1");

		if(res == AssignResources.ALL) {
			webResDirectory = new ArrayList<>();
			webResFiles = new ArrayList<>();

			webResDirectory.add("WEB-INF");

			webResFiles.add("/WEB-INF/web.xml");
			webResFiles.add("/index.jsp");
		}

	}

	private static void selfExtract(AssignResources res) 
			throws IOException {
		setResources(res);

		LOGGER.info("starting archive self-extraction...");

		ArchiveSelfExtractor.extract(dflRoot, dflResDirectory, dflResFiles);
		if(!launchDesktop) {
			ArchiveSelfExtractor.extract(webRoot, webResDirectory, webResFiles);
		}
	}

	private static boolean isJarPackaged() {
		return App.class.getResource("App.class").toString().charAt(0) == 'j';
	}

	private static boolean startTomcat() {
		Tomcat tomcat = new Tomcat();

		tomcat.setPort(port);
		tomcat.addWebapp(base, webRoot);

		try {
			tomcat.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
			return false;
		}

		LOGGER.info("startup successful: up and running!");

		tomcat.getServer().await();

		return true;
	}

	private static Options createOptions() {
		Options opt = new Options();

		// webapp
		opt.addOption(PORTOPT, true, new StringBuilder()
				.append("Provide different port from the standard one (default: ").append(port).append(")").toString());

		// webapp
		opt.addOption(BASEOPT, true, new StringBuilder().append("Provide base path for website (default: ")
				.append(base.isEmpty() ? "/" : base).append(")").toString());

		// webapp
		opt.addOption(WEBROOTOPT, true,
				new StringBuilder()
					.append("Provide different root directory for web resource extraction and usage (default: ")
					.append(webRoot == null ? "webRes must be provided" : webRoot).append(")").toString());

		opt.addOption(DBCONNECTOPT, true,
				new StringBuilder()
					.append("Where we can find your DB? \"hostname:[0-65535]\". We don't check syntax. (default: ")
					.append(dbConnect).append(")").toString());

		opt.addOption(DBPWDOPT, true, 
				new StringBuilder()
					.append("Password for \"dbUser\". (default: ")
					.append(dbPwd == null ? NOT_USING_PWDAUTH : dbPwd).append(")").toString());

		// webapp
		opt.addOption(WEBRESOPT, true,
				"Provide web resources on your own (required if no self-extraction, default: none)");

		opt.addOption(DBUSEROPT, true,
				"User which has sufficient privileges to manage whork's DB. (required, default: none)");

		opt.addOption(DESKTOPOPT, false, "Launch desktop application");

		opt.addOption(MAILFROMOPT, true, "From email address (required)");

		opt.addOption(MAILPASSWORDOPT, true, "Mail provider password authentication");

		opt.addOption(MAILHOSTOPT, true, "Mail provider host (required, hostname or IP address)");

		opt.addOption(MAILSMTPPORTOPT, true, 
				new StringBuilder()
					.append("Mail provider SMTP port (default: ")
					.append(mailSmtpPort).append(")").toString());

		opt.addOption(MAILNOTLSOPT, false, "Disable TLS for mail provider");

		opt.addOption(DFLRESOPT, true,
				"Provide default resources on your own (required if no self-extraction, default: none)");

		opt.addOption(DFLROOTOPT, true,
				new StringBuilder()
						.append("Provide different root directory for defaults resources extraction and usage (default: ")
						.append(dflRoot == null ? "dflRes must be provided" : dflRoot).append(")").toString());

		opt.addOption(HELPOPT, false, "Print this help and immediately exit");

		return opt;
	}

	private static boolean assignBase(String arg, String value) {
		base = value;
		if (base.equals("/")) {
			base = "";
		} else if (base.charAt(0) != '/' || base.endsWith("/")) {
			LOGGER.error("{} must start with / *AND* not end with /", arg);
			return false;
		}

		return true;
	}

	private static boolean assignServerPort(String arg, String value) {
		port = Integer.parseInt(value);
		if (!Util.isValidPort(port)) {
			LOGGER.error("{} number must be within range [0-65535]", arg);
			return false;
		}

		return true;
	}

	private static boolean assignSmtpPort(String arg, String value) {
		mailSmtpPort = value;
		if (!Util.isValidPort(Integer.parseInt(mailSmtpPort))) {
			LOGGER.error("{} number must be within range [0-65535]", arg);
			return false;
		}

		return true;
	}

	private static void assignWebRootOnRes(String arg, String value) {
		if (selfExtract) {
			LOGGER.warn("ignoring {} value because self extraction is enabled...", arg);
		} else {
			webRoot = new File(value).getAbsolutePath();
		}
	}

	private static void assignDflRootOnRes(String arg, String value) {
		if (selfExtract) {
			LOGGER.warn("ignoring {} value because self extraction is enabled...", arg);
		} else {
			dflRoot = new File(value).getAbsolutePath();
		}
	}

	private static void assignWebRootOnRoot(String arg, String value) {
		if (!selfExtract) {
			LOGGER.warn("ignoring {} property because self extraction is disabled...", arg);
		} else {
			webRoot = new File(value).getAbsolutePath();
		}
	}

	private static void assignDflRootOnRoot(String arg, String value) {
		if (!selfExtract) {
			LOGGER.warn("ignoring {} property because self extraction is disabled...", arg);
		} else {
			dflRoot = new File(value).getAbsolutePath();
		}
	}

	private static boolean optCheck(Option opt) {
		String argName = opt.getOpt();

		if (argName == null) {
			return true;
		}

		if (argName.equals(BASEOPT) && 
				!assignBase(argName, opt.getValue())) {
			return false;
		} else if (argName.equals(PORTOPT) && 
				!assignServerPort(argName, opt.getValue())) {
			return false;
		} else if (argName.equals(WEBRESOPT)) {
			assignWebRootOnRes(argName, opt.getValue());
		} else if (argName.equals(WEBROOTOPT)) {
			assignWebRootOnRoot(argName, opt.getValue());
		} else if (argName.equals(DBCONNECTOPT)) {
			dbConnect = opt.getValue();
		} else if (argName.equals(DBUSEROPT)) {
			dbUser = opt.getValue();
		} else if (argName.equals(DBPWDOPT)) {
			dbPwd = opt.getValue();
		} else if (argName.equals(DESKTOPOPT)) {
			launchDesktop = true;
		} else if (argName.equals(MAILHOSTOPT)) {
			mailHost = opt.getValue();
		} else if (argName.equals(MAILNOTLSOPT)) {
			mailTls = "false";
		} else if (argName.equals(MAILSMTPPORTOPT) && 
				!assignSmtpPort(argName, opt.getValue())) {
			return false;
		} else if (argName.equals(MAILFROMOPT)) {
			mailFrom = opt.getValue();
		} else if (argName.equals(MAILPASSWORDOPT)) {
			mailPwd = opt.getValue();
		} else if (argName.equals(DFLRESOPT)) {
			assignDflRootOnRes(argName, opt.getValue());
		} else if (argName.equals(DFLROOTOPT)) {
			assignDflRootOnRoot(argName, opt.getValue());
		}

		return true;
	}

	private static String getPasswordBanner(String pwd) {
		return pwd == null ? NOT_USING_PWDAUTH : HIDE_PWD;
	}

	private static void logParsedConfig() {
		if (launchDesktop) {
			LOGGER.info(
					"Settings for Whork desktop:\n--> dflroot: {}\n--> db: {}\n |--> dbuser:"
							+ " {}\n |--> dbpwd: {}\n--> mailfrom: {}\n--> mailhost:"
							+ " {}\n--> mailpwd: {}\n--> mailtls: {}\n--> smtpport: {}",
					dflRoot, dbConnect, dbUser, getPasswordBanner(dbPwd), mailFrom, mailHost, 
					getPasswordBanner(mailPwd), mailTls, mailSmtpPort);
		} else {
			LOGGER.info(
					"{}:\n--> port: {}\n--> base: {}\n--> webroot: {}\n--> dflroot: {}\n"
							+ "--> self-extract? {}\n--> db: {}\n |--> dbuser: {}\n"
							+ " |--> dbpwd: {}\n--> mailfrom: {}\n--> mailhost: {}\n"
							+ "--> mailpwd: {}\n--> mailtls: {}\n--> smtpport: {}",
					"Settings for Whork webapp", port, base.isEmpty() ? "/" : base, webRoot, dflRoot,
					selfExtract, dbConnect, dbUser, getPasswordBanner(dbPwd), mailFrom, mailHost, 
					getPasswordBanner(mailPwd), mailTls, mailSmtpPort);
		}
	}

	private static boolean configRequirementsChecks() {
		if (dbUser == null) {
			LOGGER.error("you must pass -{} in order to access DB", DBUSEROPT);
			return false;
		}

		if (mailHost == null || mailFrom == null) {
			LOGGER.error("you must pass -{} and -{} in order to be able to send emails", 
				MAILFROMOPT, MAILHOSTOPT);
			return false;
		}

		if (!selfExtract) {
			if (dflRoot == null) {
				LOGGER.error("you must pass -{} in order to specify where to locate default resources",
					DFLRESOPT);
				return false;
			}

			if (!launchDesktop && webRoot == null) {
				LOGGER.error("you must pass -{} in order to specify where to locate web resources", 
					WEBRESOPT);
				return false;
			}
		}

		return true;
	}

	private static boolean propertySetup(String[] args) throws ParseException {
		Options options = createOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("whork-webapp", options);
			return false;
		}

		for (Option option : cmd.getOptions()) {
			if (!optCheck(option))
				return false;
		}

		if (!configRequirementsChecks()) {
			return false;
		}

		logParsedConfig();

		return true;
	}

	private static void utilDeleteDirectoryRecursion(Path path) throws IOException {
		if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
			try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
				for (Path entry : entries) {
					utilDeleteDirectoryRecursion(entry);
				}
			}
		}
		Files.delete(path);
	}

	private static void deleteDirWithMessage(Logger logger, String name, String path) {
		logger.info("deleting {}...", name);
		try {
			utilDeleteDirectoryRecursion(Paths.get(path));
		} catch (IOException e) {
			e.getMessage();
			logger.error("unable to delete {} @ {}", name, path);
		}
	}

	private static void cleanup() {
		Logger cleanupLogger = LoggerFactory.getLogger("WhorkCleanup");

		if (selfExtract) {
			if(!launchDesktop) {
				deleteDirWithMessage(cleanupLogger, "webroot", webRoot);
			}

			deleteDirWithMessage(cleanupLogger, "dflroot", dflRoot);
		}

		cleanupLogger.info("closing DB connection...");

		try {
			Database.getInstance().closeConnection();
		} catch (DatabaseException e) {
			cleanupLogger.warn(e.getMessage());
			cleanupLogger.warn("please report this, we don't expect this at this point. Something is broken :S");
		}

		cleanupLogger.info("bye bye!");
	}

	private static void exceptionMessageBeforeStart(Exception e, String msg) {
		LOGGER.error("{}: {}", e.getMessage(), msg);
		LOGGER.error("Whork will not start!");
	}

	private static boolean prePopulatePools() {
		LOGGER.info("Prepopulating pools with default values (presets)...");

		try {
			ComuniDao.populatePool();
			EmploymentStatusDao.populatePool();
		} catch (DataAccessException e) {
			exceptionMessageBeforeStart(e, e.getCause().getMessage());
			return false;
		}

		return true;
	}

	private static void setMailSender() {
		LOGGER.info("Setting mail sender...");

		MailSender sender = new MailSender();
		sender.setFrom(mailFrom);
		sender.setHost(mailHost);
		sender.setPassword(mailPwd);
		sender.setTls(mailTls);
		sender.setPort(mailSmtpPort);

		Util.Mailer.setMailSender(sender);
	}

	private static boolean attemptToEstablishDbConnection() {
		LOGGER.info("Checking if we can correctly talk to DB...");
		LOGGER.info("Driver: {}", Database.DRIVER);

		DatabaseName.setDbName(DBNAME);

		try {
			Connection conn = Database.getInstance(dbConnect, dbUser, dbPwd).getConnection();
			conn.setCatalog(DatabaseName.getDbName());
			LOGGER.info("Yes, we're able to talk to DB!");
		} catch (ClassNotFoundException e) {
			exceptionMessageBeforeStart(e, "unable to load driver class, this SHOULD be reported!");
			return false;
		} catch (SQLException e) {
			exceptionMessageBeforeStart(e, "unable to correctly establish a DB connection!");
			return false;
		}

		return true;
	}

	private static boolean initialize() {
		if (!attemptToEstablishDbConnection()) {
			return false;
		}

		if (!prePopulatePools()) {
			return false;
		}

		setMailSender();

		return true;
	}

	private static AssignResources assignResourcesAndLog() {
		AssignResources res = AssignResources.ALL;

		if (launchDesktop) {
			res = AssignResources.ONLY_DEFAULTS;
			LOGGER.info("launching desktop application: any of {},{},{},{} options are ignored", 
					BASEOPT, PORTOPT, WEBRESOPT, WEBROOTOPT);
		}

		return res;
	}

	private static void assignDefaultRoots() {
		if (selfExtract) {
			webRoot = new File("whork_webroot").getAbsolutePath();
			dflRoot = new File("whork_defaultsroot").getAbsolutePath();
		} else {
			webRoot = null;
			dflRoot = null;
		}
	}

	private static boolean parse(String[] args) {
		assignDefaultRoots();

		try {
			if (!propertySetup(args)) {
				return false;
			}
		} catch(ParseException e) {
			exceptionMessageBeforeStart(e, "unable to parse command line");
			return false;
		}

		AssignResources res = assignResourcesAndLog();

		if(selfExtract) {
			try {
				selfExtract(res);
			} catch (IOException e) {
				exceptionMessageBeforeStart(e, "unable to correclty self-extract");
				return false;
			}
		}

		return true;
	}

	private static void setRuntimeHooks() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				App.cleanup();
			}
		});
	}

	private static void finalizeLaunch(String[] args) {
		if (!launchDesktop) {
			LOGGER.info("Welcome to Whork webapp! Starting up...");

			if (!startTomcat()) {
				LOGGER.error("unable to start tomcat, details above");
			}
		} else {
			LOGGER.info("Welcome to Whork desktop! Starting up...");
			WhorkDesktopLauncher.launchApp(args);
		}
	}

	/**
	 * @param args
	 * 
	 * Entry point
	 */
	public static void main(String[] args) {
		if (parse(args)) {
			setRuntimeHooks();
			if (initialize()) {
				finalizeLaunch(args);
			}
		}
	}

	/**
	 * 
	 * @return default root
	 */
	public static String getDflRoot() {
		return dflRoot;
	}
}
