package logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.controller.ChatController;
import logic.dao.ComuniDao;
import logic.exception.DataAccessException;
import logic.exception.DatabaseException;
import logic.util.Util;
import logic.dao.EmploymentStatusDao;
import logic.dao.JobCategoryDao;
import logic.dao.JobPositionDao;
import logic.dao.QualificationDao;
import logic.dao.TypeOfContractDao;

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
import org.apache.catalina.connector.Connector;
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
	private static final String SVCCHATPORTOPT = "svcChatPort";
	private static final String SVCTOKINTVLOPT = "svcTokIntvl";
	private static final String USRDATAOPT = "usrData";
	
	// tomcat contexts
	private static final String DFLROOT_CONTEXT = "/default";
	private static final String USR_DATA_CONTEXT = "/usrdata";

	// Self-extraction properties
	private static List<String> webResDirectories = null;
	private static List<String> webResFiles = null;
	private static List<String> dflResDirectories = null;
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
	private static boolean mailTls = true;
	private static String mailSmtpPort = "587";
	private static String mailFrom = null;
	private static String mailPwd = null;
	private static String mailHost = null;
	private static int chatPort = 45612;
	private static int tokIntvl = 300;
	private static String usrData = new File("whork_usrdata").getAbsolutePath();

	// Static config for whork
	private static final String DBNAME = "whorkdb";

	private static final String NOT_USING_PWDAUTH = "NOT using password authentication";
	private static final String HIDE_PWD = "[HIDDEN]";
	private static final String PORT_RANGE_ERROR_FMT = "{} number must be within range [0-65535]";
	private static final String IGN_PROP_SELFEXT_ENABLED_FMT = "ignoring {} property because self extraction is enabled...";
	private static final String IGN_PROP_SELFEXT_DISABLED_FMT = "ignoring {} value because self extraction is disabled...";

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
		dflResDirectories = new ArrayList<>();
		dflResFiles = new ArrayList<>();
		dflResFiles.add("/placeholder");
		dflResFiles.add("/placeholder1");

		if(res == AssignResources.ALL) {
			webResDirectories = new ArrayList<>();
			webResFiles = new ArrayList<>();
			ExtractorFileLister.list(webResDirectories, webResFiles);
		}
	}

	private static void selfExtract(AssignResources res) 
			throws IOException {
		setResources(res);

		LOGGER.info("starting archive self-extraction...");

		ArchiveSelfExtractor.extract(dflRoot, dflResDirectories, dflResFiles);
		if(!launchDesktop) {
			ArchiveSelfExtractor.extract(webRoot, webResDirectories, webResFiles);
		}
	}

	private static boolean isJarPackaged() {
		return App.class.getResource("App.class").toString().charAt(0) == 'j';
	}

	private static boolean startTomcat() {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(port);
		tomcat.addWebapp(USR_DATA_CONTEXT, usrData);
		tomcat.addWebapp(DFLROOT_CONTEXT, dflRoot);
		tomcat.addWebapp(base, webRoot);
		
		Connector connector = tomcat.getConnector();
		connector.setProperty("compression", "on");
		connector.setProperty("compressionMinSize", "1024");
		connector.setProperty("noCompressionUserAgents", "gozilla, traviata");
		connector.setProperty("compressableMimeType", "text/html,text/xml, text/css, application/json, application/javascript");

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

		opt.addOption(SVCTOKINTVLOPT, true, 
				new StringBuilder()
						.append("Set paired service token validity interval (default: ")
						.append(tokIntvl).append(")").toString());
		
		opt.addOption(SVCCHATPORTOPT, true, 
				new StringBuilder()
						.append("Set paired chat service port (default: ")
						.append(chatPort).append(")").toString());

		opt.addOption(USRDATAOPT, true, 
				new StringBuilder()
						.append("Set user data directory (default: ")
						.append(usrData).append(")").toString());

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

	private static boolean assignChatServicePort(String arg, String value) {
		chatPort = Integer.parseInt(value);
		if (!Util.isValidPort(chatPort)) {
			LOGGER.error(PORT_RANGE_ERROR_FMT, arg);
			return false;
		}

		return true;
	}

	private static boolean assignServerPort(String arg, String value) {
		port = Integer.parseInt(value);
		if (!Util.isValidPort(port)) {
			LOGGER.error(PORT_RANGE_ERROR_FMT, arg);
			return false;
		}

		return true;
	}

	private static boolean assignSmtpPort(String arg, String value) {
		mailSmtpPort = value;
		if (!Util.isValidPort(Integer.parseInt(mailSmtpPort))) {
			LOGGER.error(PORT_RANGE_ERROR_FMT, arg);
			return false;
		}

		return true;
	}

	private static void assignWebRootOnRes(String arg, String value) {
		if (selfExtract) {
			LOGGER.warn(IGN_PROP_SELFEXT_ENABLED_FMT, arg);
		} else {
			webRoot = new File(value).getAbsolutePath();
		}
	}

	private static void assignDflRootOnRes(String arg, String value) {
		if (selfExtract) {
			LOGGER.warn(IGN_PROP_SELFEXT_ENABLED_FMT, arg);
		} else {
			dflRoot = new File(value).getAbsolutePath();
		}
	}

	private static void assignWebRootOnRoot(String arg, String value) {
		if (!selfExtract) {
			LOGGER.warn(IGN_PROP_SELFEXT_DISABLED_FMT, arg);
		} else {
			webRoot = new File(value).getAbsolutePath();
		}
	}

	private static void assignDflRootOnRoot(String arg, String value) {
		if (!selfExtract) {
			LOGGER.warn(IGN_PROP_SELFEXT_DISABLED_FMT, arg);
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
			mailTls = false;
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
		} else if(argName.equals(SVCCHATPORTOPT)) {
			assignChatServicePort(argName, opt.getValue());
		} else if(argName.equals(SVCTOKINTVLOPT)) {
			tokIntvl = Integer.parseInt(opt.getValue());
		} else if(argName.equals(USRDATAOPT)) {
			usrData = new File(opt.getValue()).getAbsolutePath();
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
							+ " {}\n--> mailpwd: {}\n--> mailtls: {}\n--> smtpport: {}\n--> usrdata: {}\n",
					dflRoot, dbConnect, dbUser, getPasswordBanner(dbPwd), mailFrom, mailHost, 
					getPasswordBanner(mailPwd), mailTls, mailSmtpPort, usrData);
		} else {
			LOGGER.info(
					"{}:\n--> port: {}\n--> base: {}\n--> webroot: {}\n--> dflroot: {}\n"
							+ "--> self-extract? {}\n--> db: {}\n |--> dbuser: {}\n"
							+ " |--> dbpwd: {}\n--> mailfrom: {}\n--> mailhost: {}\n"
							+ "--> mailpwd: {}\n--> mailtls: {}\n--> smtpport: {}\n"
							+ "--> chatport: {}\n--> tokenintvl: {}\n--> usrdata: {}\n",
					"Settings for Whork webapp", port, base.isEmpty() ? "/" : base, webRoot, dflRoot,
					selfExtract, dbConnect, dbUser, getPasswordBanner(dbPwd), mailFrom, mailHost, 
					getPasswordBanner(mailPwd), mailTls, mailSmtpPort, chatPort, tokIntvl, usrData);
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
			JobCategoryDao.populatePool();
			JobPositionDao.populatePool();
			QualificationDao.populatePool();
			TypeOfContractDao.populatePool();
		} catch (DataAccessException e) {
			exceptionMessageBeforeStart(e, e.getCause().getMessage());
			return false;
		}

		return true;
	}

	private static void setInstanceConfigs() {		
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, mailFrom);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, mailHost);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILPWD, mailPwd);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, mailTls);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, mailSmtpPort);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_DFL_ROOT, dflRoot);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_SVC_INTVL_TOK, tokIntvl);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_SVC_CHAT_PORT, chatPort);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_USR_DATA, usrData);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_CTX_USR_DATA, USR_DATA_CONTEXT);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_CTX_DFL_ROOT, DFLROOT_CONTEXT);
	}

	private static boolean attemptToEstablishDbConnection() {
		LOGGER.info("Checking if we can correctly talk to DB...");
		LOGGER.info("Driver: {}", Database.DRIVER);

		try {
			Connection conn = Database.getInstance(dbConnect, dbUser, dbPwd).getConnection();
			conn.setCatalog(DBNAME);
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
	
	private static boolean startChatServiceThatRunAlong() {
		LOGGER.info("starting chat service...");
		if (!ChatController.getInstance().startService()) {
			LOGGER.error("service chat failed to start, aborting now...");
			return false;
		}

		return true;
	}

	private static void stopChatServiceThatRunAlong() {
		LOGGER.info("stopping chat service...");
		if (!ChatController.getInstance().stopService()) {
			LOGGER.warn("service chat failed to stop, ignoring and proceeding anyway...");
		}
	}

	private static boolean initialize() {
		if (!attemptToEstablishDbConnection()) {
			return false;
		}

		if (!prePopulatePools()) {
			return false;
		}

		setInstanceConfigs();

		createUserDataDirectoryIfNonExistant();

		if(launchDesktop) {
			return true;
		}

		return startChatServiceThatRunAlong();
	}

	private static void createUserDataDirectoryIfNonExistant() {
		File usrDataDir = new File(usrData);
		if (!usrDataDir.exists()){
			LOGGER.info("creating user data directory...");
			usrDataDir.mkdir();
		}
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
				exceptionMessageBeforeStart(e, "unable to correctly self-extract");
				return false;
			}
		}

		return true;
	}

	private static void setRuntimeHooks() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if(!launchDesktop) {
					stopChatServiceThatRunAlong();
				}

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
	 */
	public static void main(String[] args) {
		if (parse(args)) {
			setRuntimeHooks();
			if (initialize()) {
				LOGGER.info("Whork has been initialized!");
				finalizeLaunch(args);
			} else {
				LOGGER.error("unable to initialize Whork, exiting...");
			}
		} else {
			LOGGER.error("unable to parse command line for Whork, exiting...");
		}
	}
}
