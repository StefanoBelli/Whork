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


			webResFiles.add("//login.jsp");
			webResFiles.add("//success.jsp");
			webResFiles.add("//package.json");
			webResFiles.add("//changepwd.jsp");
			webResFiles.add("//reg_company.jsp");
			webResFiles.add("//reg_jobseeker.jsp");
			webResFiles.add("//index.jsp");
			webResFiles.add("//confirm.jsp");
			webResFiles.add("//chat.jsp");
			webResFiles.add("//offer_successfully_posted.jsp");
			webResFiles.add("//forgotpwd.jsp");
			webResFiles.add("//register.jsp");
			webResFiles.add("//cpoutcome.jsp");
			webResFiles.add("//post_offer.jsp");
			webResFiles.add("//account.jsp");
			webResFiles.add("//package-lock.json");
			webResDirectories.add("fonts");
			webResDirectories.add("fonts/codropsicons");
			webResFiles.add("/fonts/codropsicons/codropsicons.eot");
			webResFiles.add("/fonts/codropsicons/codropsicons.svg");
			webResFiles.add("/fonts/codropsicons/license.txt");
			webResFiles.add("/fonts/codropsicons/codropsicons.ttf");
			webResFiles.add("/fonts/codropsicons/codropsicons.woff");
			webResDirectories.add("fonts/icomoon");
			webResFiles.add("/fonts/icomoon/Read Me.txt");
			webResFiles.add("/fonts/icomoon/icomoon.eot");
			webResFiles.add("/fonts/icomoon/icomoon.svg");
			webResFiles.add("/fonts/icomoon/icomoon.woff");
			webResFiles.add("/fonts/icomoon/icomoon.dev.svg");
			webResFiles.add("/fonts/icomoon/icomoon.ttf");
			webResFiles.add("/fonts/icomoon/license.txt");
			webResDirectories.add("js");
			webResFiles.add("/js/editInfoAccount.js");
			webResFiles.add("/js/common.js");
			webResFiles.add("/js/reg_jobseeker.js");
			webResFiles.add("/js/changepwd.js");
			webResFiles.add("/js/reg_company.js");
			webResFiles.add("/js/modernizr.custom.js");
			webResDirectories.add("assets");
			webResDirectories.add("assets/images");
			webResFiles.add("/assets/images/img2.jpg");
			webResFiles.add("/assets/images/img1.jpg");
			webResFiles.add("/assets/images/img3.jpg");
			webResFiles.add("/assets/images/custom-select.png");
			webResDirectories.add("assets/images/widgets");
			webResFiles.add("/assets/images/widgets/widget-carousel.jpg");
			webResFiles.add("/assets/images/widgets/widget-carousel-2.jpg");
			webResFiles.add("/assets/images/widgets/2.jpg");
			webResFiles.add("/assets/images/widgets/1.jpg");
			webResFiles.add("/assets/images/widgets/3.jpg");
			webResDirectories.add("assets/images/tooltip");
			webResFiles.add("/assets/images/tooltip/tooltip2.svg");
			webResFiles.add("/assets/images/tooltip/tooltip1.svg");
			webResFiles.add("/assets/images/tooltip/shape3.svg");
			webResFiles.add("/assets/images/tooltip/shape1.svg");
			webResFiles.add("/assets/images/tooltip/Euclid.png");
			webResFiles.add("/assets/images/tooltip/tooltip3.svg");
			webResFiles.add("/assets/images/tooltip/shape2.svg");
			webResDirectories.add("assets/images/alert");
			webResFiles.add("/assets/images/alert/alert3.png");
			webResFiles.add("/assets/images/alert/model3.png");
			webResFiles.add("/assets/images/alert/alert7.png");
			webResFiles.add("/assets/images/alert/model.png");
			webResFiles.add("/assets/images/alert/alert4.png");
			webResFiles.add("/assets/images/alert/model2.png");
			webResFiles.add("/assets/images/alert/alert.png");
			webResFiles.add("/assets/images/alert/alert5.png");
			webResFiles.add("/assets/images/alert/alert2.png");
			webResFiles.add("/assets/images/alert/alert6.png");
			webResDirectories.add("assets/images/product");
			webResFiles.add("/assets/images/product/iphone.png");
			webResFiles.add("/assets/images/product/chair3.png");
			webResFiles.add("/assets/images/product/p2.jpg");
			webResFiles.add("/assets/images/product/chair2.png");
			webResFiles.add("/assets/images/product/chair.png");
			webResFiles.add("/assets/images/product/p1.jpg");
			webResFiles.add("/assets/images/product/p4.jpg");
			webResFiles.add("/assets/images/product/ipad.png");
			webResFiles.add("/assets/images/product/p3.jpg");
			webResFiles.add("/assets/images/product/chair4.png");
			webResFiles.add("/assets/images/product/iwatch.png");
			webResDirectories.add("assets/images/users");
			webResFiles.add("/assets/images/users/d3.jpg");
			webResFiles.add("/assets/images/users/d5.jpg");
			webResFiles.add("/assets/images/users/profile.png");
			webResFiles.add("/assets/images/users/d2.jpg");
			webResFiles.add("/assets/images/users/2.png");
			webResFiles.add("/assets/images/users/d4.jpg");
			webResFiles.add("/assets/images/users/widget-table-pic4.jpg");
			webResFiles.add("/assets/images/users/1-old.jpg");
			webResFiles.add("/assets/images/users/7.jpg");
			webResFiles.add("/assets/images/users/widget-table-pic2.jpg");
			webResFiles.add("/assets/images/users/agent.jpg");
			webResFiles.add("/assets/images/users/3.png");
			webResFiles.add("/assets/images/users/widget-table-pic1.jpg");
			webResFiles.add("/assets/images/users/2.jpg");
			webResFiles.add("/assets/images/users/1.jpg");
			webResFiles.add("/assets/images/users/8.jpg");
			webResFiles.add("/assets/images/users/profile-pic.jpg");
			webResFiles.add("/assets/images/users/1.png");
			webResFiles.add("/assets/images/users/5.jpg");
			webResFiles.add("/assets/images/users/agent2.jpg");
			webResFiles.add("/assets/images/users/4.jpg");
			webResFiles.add("/assets/images/users/d1.jpg");
			webResFiles.add("/assets/images/users/profile-pic-2.jpg");
			webResFiles.add("/assets/images/users/6.jpg");
			webResFiles.add("/assets/images/users/widget-table-pic3.jpg");
			webResFiles.add("/assets/images/users/3.jpg");
			webResDirectories.add("assets/images/docs");
			webResFiles.add("/assets/images/docs/gulp.jpg");
			webResDirectories.add("assets/images/rating");
			webResFiles.add("/assets/images/rating/cancel-on.png");
			webResFiles.add("/assets/images/rating/star-half-mono.png");
			webResFiles.add("/assets/images/rating/star-half.png");
			webResFiles.add("/assets/images/rating/like.png");
			webResFiles.add("/assets/images/rating/heart.png");
			webResFiles.add("/assets/images/rating/cancel-off.png");
			webResFiles.add("/assets/images/rating/star-off.png");
			webResFiles.add("/assets/images/rating/star-on.png");
			webResDirectories.add("assets/images/gallery");
			webResFiles.add("/assets/images/gallery/chair4.jpg");
			webResFiles.add("/assets/images/gallery/chair.jpg");
			webResFiles.add("/assets/images/gallery/chair3.jpg");
			webResFiles.add("/assets/images/gallery/chair2.jpg");
			webResDirectories.add("assets/images/background");
			webResFiles.add("/assets/images/background/sidebarbg.png");
			webResFiles.add("/assets/images/background/socialbg.jpg");
			webResFiles.add("/assets/images/background/nyan-cat.gif");
			webResFiles.add("/assets/images/background/img5.jpg");
			webResFiles.add("/assets/images/background/active-bg.png");
			webResFiles.add("/assets/images/background/img5.png");
			webResFiles.add("/assets/images/background/profile-bg.jpg");
			webResFiles.add("/assets/images/background/error-bg.jpg");
			webResFiles.add("/assets/images/background/user-info.jpg");
			webResFiles.add("/assets/images/background/user-bg.jpg");
			webResFiles.add("/assets/images/background/beauty.jpg");
			webResFiles.add("/assets/images/background/active-bg.jpg");
			webResFiles.add("/assets/images/background/megamenubg.jpg");
			webResFiles.add("/assets/images/background/weatherbg.jpg");
			webResFiles.add("/assets/images/background/login-register.jpg");
			webResDirectories.add("assets/images/big");
			webResFiles.add("/assets/images/big/img2.jpg");
			webResFiles.add("/assets/images/big/icon.png");
			webResFiles.add("/assets/images/big/d2.jpg");
			webResFiles.add("/assets/images/big/img5.jpg");
			webResFiles.add("/assets/images/big/img6.jpg");
			webResFiles.add("/assets/images/big/img1.jpg");
			webResFiles.add("/assets/images/big/1.jpg");
			webResFiles.add("/assets/images/big/5.jpg");
			webResFiles.add("/assets/images/big/auth-bg.jpg");
			webResFiles.add("/assets/images/big/img3.jpg");
			webResFiles.add("/assets/images/big/3.jpg");
			webResFiles.add("/assets/images/big/img4.jpg");
			webResFiles.add("/assets/images/big/auth-bg2.jpg");
			webResDirectories.add("assets/images/landingpage");
			webResFiles.add("/assets/images/landingpage/icon-sprite.jpg");
			webResFiles.add("/assets/images/landingpage/img2.jpg");
			webResFiles.add("/assets/images/landingpage/logos.png");
			webResFiles.add("/assets/images/landingpage/right-img.png");
			webResFiles.add("/assets/images/landingpage/section-img.png");
			webResFiles.add("/assets/images/landingpage/banne-img.png");
			webResFiles.add("/assets/images/landingpage/favicon.png");
			webResFiles.add("/assets/images/landingpage/brand-logos.png");
			webResFiles.add("/assets/images/landingpage/img3.jpg");
			webResFiles.add("/assets/images/landingpage/f1.png");
			webResFiles.add("/assets/images/landingpage/section-bg.png");
			webResFiles.add("/assets/images/landingpage/f3.png");
			webResFiles.add("/assets/images/landingpage/banner-bg.png");
			webResFiles.add("/assets/images/landingpage/db.png");
			webResFiles.add("/assets/images/landingpage/logo.png");
			webResFiles.add("/assets/images/landingpage/f2.png");
			webResDirectories.add("assets/images/browser");
			webResFiles.add("/assets/images/browser/firefox-logo.png");
			webResFiles.add("/assets/images/browser/chrome-logo.png");
			webResFiles.add("/assets/images/browser/opera-logo.png");
			webResFiles.add("/assets/images/browser/safari-logo.png");
			webResFiles.add("/assets/images/browser/photoshop.jpg");
			webResFiles.add("/assets/images/browser/internet-logo.png");
			webResFiles.add("/assets/images/browser/netscape-logo.png");
			webResFiles.add("/assets/images/browser/edge-logo.png");
			webResFiles.add("/assets/images/browser/sketch.jpg");
			webResDirectories.add("assets/libs");
			webResDirectories.add("assets/libs/chartist");
			webResDirectories.add("assets/libs/chartist/dist");
			webResFiles.add("/assets/libs/chartist/dist/chartist.min.css");
			webResFiles.add("/assets/libs/chartist/dist/chartist.min.js");
			webResDirectories.add("assets/libs/fullcalendar");
			webResDirectories.add("assets/libs/fullcalendar/dist");
			webResFiles.add("/assets/libs/fullcalendar/dist/fullcalendar.min.css");
			webResFiles.add("/assets/libs/fullcalendar/dist/fullcalendar.print.min.css");
			webResFiles.add("/assets/libs/fullcalendar/dist/fullcalendar.min.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/gcal.min.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale-all.js");
			webResDirectories.add("assets/libs/fullcalendar/dist/locale");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/nl.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/zh-cn.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/et.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/mk.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/en-ie.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ms-my.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/lt.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/zh-hk.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ar-ma.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/th.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ms.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/es.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/cs.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/en-au.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/es-do.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/uk.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/it.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/en-gb.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/gl.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/sk.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/fi.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/hi.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ar.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/hu.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ar-sa.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/pt-br.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/be.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/tr.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/kk.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ar-kw.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/de-at.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/zh-tw.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/en-nz.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ar-tn.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ru.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/lb.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/vi.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/el.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/fr.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/pt.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/en-ca.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/fr-ch.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/af.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ar-ly.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/sr-cyrl.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ka.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/nb.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ar-dz.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ca.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/lv.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/bs.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/fa.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/he.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/de.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/sq.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/sl.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/sv.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/pl.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/nn.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/bg.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/nl-be.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/fr-ca.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/id.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/sr.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ko.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/da.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/es-us.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/hr.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/de-ch.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/is.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/eu.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ja.js");
			webResFiles.add("/assets/libs/fullcalendar/dist/locale/ro.js");
			webResDirectories.add("assets/libs/popper.js");
			webResDirectories.add("assets/libs/popper.js/dist");
			webResFiles.add("/assets/libs/popper.js/dist/popper-utils.min.js");
			webResFiles.add("/assets/libs/popper.js/dist/popper.min.js");
			webResDirectories.add("assets/libs/popper.js/dist/umd");
			webResFiles.add("/assets/libs/popper.js/dist/umd/poppper.js.flow");
			webResFiles.add("/assets/libs/popper.js/dist/umd/popper-utils.min.js");
			webResFiles.add("/assets/libs/popper.js/dist/umd/popper.min.js");
			webResDirectories.add("assets/libs/popper.js/dist/esm");
			webResFiles.add("/assets/libs/popper.js/dist/esm/popper-utils.min.js");
			webResFiles.add("/assets/libs/popper.js/dist/esm/popper.min.js");
			webResDirectories.add("assets/libs/bootstrap");
			webResDirectories.add("assets/libs/bootstrap/dist");
			webResDirectories.add("assets/libs/bootstrap/dist/js");
			webResFiles.add("/assets/libs/bootstrap/dist/js/bootstrap.js");
			webResFiles.add("/assets/libs/bootstrap/dist/js/bootstrap.min.js.map");
			webResFiles.add("/assets/libs/bootstrap/dist/js/bootstrap.bundle.js.map");
			webResFiles.add("/assets/libs/bootstrap/dist/js/bootstrap.min.js");
			webResFiles.add("/assets/libs/bootstrap/dist/js/bootstrap.bundle.min.js.map");
			webResFiles.add("/assets/libs/bootstrap/dist/js/bootstrap.js.map");
			webResFiles.add("/assets/libs/bootstrap/dist/js/bootstrap.bundle.js");
			webResFiles.add("/assets/libs/bootstrap/dist/js/bootstrap.bundle.min.js");
			webResDirectories.add("assets/libs/bootstrap/dist/css");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap-grid.min.css");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap-grid.css");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap.css");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap.min.css.map");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap-reboot.min.css.map");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap.css.map");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap-grid.min.css.map");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap-reboot.min.css");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap-reboot.css");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap-reboot.css.map");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap-grid.css.map");
			webResFiles.add("/assets/libs/bootstrap/dist/css/bootstrap.min.css");
			webResDirectories.add("assets/libs/chart.js");
			webResDirectories.add("assets/libs/chart.js/dist");
			webResFiles.add("/assets/libs/chart.js/dist/Chart.min.js");
			webResFiles.add("/assets/libs/chart.js/dist/Chart.bundle.min.js");
			webResFiles.add("/assets/libs/chart.js/dist/Chart.min.css");
			webResDirectories.add("assets/libs/jquery");
			webResDirectories.add("assets/libs/jquery/dist");
			webResFiles.add("/assets/libs/jquery/dist/jquery.slim.min.js");
			webResFiles.add("/assets/libs/jquery/dist/core.js");
			webResFiles.add("/assets/libs/jquery/dist/jquery.min.js");
			webResDirectories.add("assets/libs/moment");
			webResFiles.add("/assets/libs/moment/ender.js");
			webResFiles.add("/assets/libs/moment/package.js");
			webResFiles.add("/assets/libs/moment/moment.js");
			webResDirectories.add("assets/libs/moment/locale");
			webResFiles.add("/assets/libs/moment/locale/nl.js");
			webResFiles.add("/assets/libs/moment/locale/dv.js");
			webResFiles.add("/assets/libs/moment/locale/zh-cn.js");
			webResFiles.add("/assets/libs/moment/locale/et.js");
			webResFiles.add("/assets/libs/moment/locale/mk.js");
			webResFiles.add("/assets/libs/moment/locale/en-ie.js");
			webResFiles.add("/assets/libs/moment/locale/tg.js");
			webResFiles.add("/assets/libs/moment/locale/ms-my.js");
			webResFiles.add("/assets/libs/moment/locale/hy-am.js");
			webResFiles.add("/assets/libs/moment/locale/bm.js");
			webResFiles.add("/assets/libs/moment/locale/x-pseudo.js");
			webResFiles.add("/assets/libs/moment/locale/ga.js");
			webResFiles.add("/assets/libs/moment/locale/lt.js");
			webResFiles.add("/assets/libs/moment/locale/zh-hk.js");
			webResFiles.add("/assets/libs/moment/locale/se.js");
			webResFiles.add("/assets/libs/moment/locale/ar-ma.js");
			webResFiles.add("/assets/libs/moment/locale/eo.js");
			webResFiles.add("/assets/libs/moment/locale/sd.js");
			webResFiles.add("/assets/libs/moment/locale/th.js");
			webResFiles.add("/assets/libs/moment/locale/ml.js");
			webResFiles.add("/assets/libs/moment/locale/ms.js");
			webResFiles.add("/assets/libs/moment/locale/br.js");
			webResFiles.add("/assets/libs/moment/locale/es.js");
			webResFiles.add("/assets/libs/moment/locale/tzl.js");
			webResFiles.add("/assets/libs/moment/locale/cs.js");
			webResFiles.add("/assets/libs/moment/locale/te.js");
			webResFiles.add("/assets/libs/moment/locale/en-au.js");
			webResFiles.add("/assets/libs/moment/locale/es-do.js");
			webResFiles.add("/assets/libs/moment/locale/uk.js");
			webResFiles.add("/assets/libs/moment/locale/it.js");
			webResFiles.add("/assets/libs/moment/locale/tl-ph.js");
			webResFiles.add("/assets/libs/moment/locale/en-SG.js");
			webResFiles.add("/assets/libs/moment/locale/ur.js");
			webResFiles.add("/assets/libs/moment/locale/en-gb.js");
			webResFiles.add("/assets/libs/moment/locale/lo.js");
			webResFiles.add("/assets/libs/moment/locale/mt.js");
			webResFiles.add("/assets/libs/moment/locale/bn.js");
			webResFiles.add("/assets/libs/moment/locale/gl.js");
			webResFiles.add("/assets/libs/moment/locale/sk.js");
			webResFiles.add("/assets/libs/moment/locale/fi.js");
			webResFiles.add("/assets/libs/moment/locale/en-il.js");
			webResFiles.add("/assets/libs/moment/locale/uz-latn.js");
			webResFiles.add("/assets/libs/moment/locale/hi.js");
			webResFiles.add("/assets/libs/moment/locale/ug-cn.js");
			webResFiles.add("/assets/libs/moment/locale/ar.js");
			webResFiles.add("/assets/libs/moment/locale/hu.js");
			webResFiles.add("/assets/libs/moment/locale/ar-sa.js");
			webResFiles.add("/assets/libs/moment/locale/bo.js");
			webResFiles.add("/assets/libs/moment/locale/uz.js");
			webResFiles.add("/assets/libs/moment/locale/pt-br.js");
			webResFiles.add("/assets/libs/moment/locale/my.js");
			webResFiles.add("/assets/libs/moment/locale/gd.js");
			webResFiles.add("/assets/libs/moment/locale/be.js");
			webResFiles.add("/assets/libs/moment/locale/tr.js");
			webResFiles.add("/assets/libs/moment/locale/kk.js");
			webResFiles.add("/assets/libs/moment/locale/ar-kw.js");
			webResFiles.add("/assets/libs/moment/locale/ky.js");
			webResFiles.add("/assets/libs/moment/locale/tzm.js");
			webResFiles.add("/assets/libs/moment/locale/de-at.js");
			webResFiles.add("/assets/libs/moment/locale/cv.js");
			webResFiles.add("/assets/libs/moment/locale/zh-tw.js");
			webResFiles.add("/assets/libs/moment/locale/ne.js");
			webResFiles.add("/assets/libs/moment/locale/en-nz.js");
			webResFiles.add("/assets/libs/moment/locale/ar-tn.js");
			webResFiles.add("/assets/libs/moment/locale/ru.js");
			webResFiles.add("/assets/libs/moment/locale/lb.js");
			webResFiles.add("/assets/libs/moment/locale/vi.js");
			webResFiles.add("/assets/libs/moment/locale/el.js");
			webResFiles.add("/assets/libs/moment/locale/fr.js");
			webResFiles.add("/assets/libs/moment/locale/gom-latn.js");
			webResFiles.add("/assets/libs/moment/locale/ta.js");
			webResFiles.add("/assets/libs/moment/locale/pt.js");
			webResFiles.add("/assets/libs/moment/locale/en-ca.js");
			webResFiles.add("/assets/libs/moment/locale/fo.js");
			webResFiles.add("/assets/libs/moment/locale/fr-ch.js");
			webResFiles.add("/assets/libs/moment/locale/af.js");
			webResFiles.add("/assets/libs/moment/locale/kn.js");
			webResFiles.add("/assets/libs/moment/locale/it-ch.js");
			webResFiles.add("/assets/libs/moment/locale/ss.js");
			webResFiles.add("/assets/libs/moment/locale/ar-ly.js");
			webResFiles.add("/assets/libs/moment/locale/sr-cyrl.js");
			webResFiles.add("/assets/libs/moment/locale/yo.js");
			webResFiles.add("/assets/libs/moment/locale/ka.js");
			webResFiles.add("/assets/libs/moment/locale/nb.js");
			webResFiles.add("/assets/libs/moment/locale/ar-dz.js");
			webResFiles.add("/assets/libs/moment/locale/tlh.js");
			webResFiles.add("/assets/libs/moment/locale/ca.js");
			webResFiles.add("/assets/libs/moment/locale/lv.js");
			webResFiles.add("/assets/libs/moment/locale/bs.js");
			webResFiles.add("/assets/libs/moment/locale/fa.js");
			webResFiles.add("/assets/libs/moment/locale/he.js");
			webResFiles.add("/assets/libs/moment/locale/jv.js");
			webResFiles.add("/assets/libs/moment/locale/de.js");
			webResFiles.add("/assets/libs/moment/locale/mi.js");
			webResFiles.add("/assets/libs/moment/locale/sq.js");
			webResFiles.add("/assets/libs/moment/locale/sl.js");
			webResFiles.add("/assets/libs/moment/locale/sv.js");
			webResFiles.add("/assets/libs/moment/locale/ku.js");
			webResFiles.add("/assets/libs/moment/locale/mr.js");
			webResFiles.add("/assets/libs/moment/locale/pl.js");
			webResFiles.add("/assets/libs/moment/locale/me.js");
			webResFiles.add("/assets/libs/moment/locale/nn.js");
			webResFiles.add("/assets/libs/moment/locale/sw.js");
			webResFiles.add("/assets/libs/moment/locale/bg.js");
			webResFiles.add("/assets/libs/moment/locale/nl-be.js");
			webResFiles.add("/assets/libs/moment/locale/gu.js");
			webResFiles.add("/assets/libs/moment/locale/az.js");
			webResFiles.add("/assets/libs/moment/locale/fr-ca.js");
			webResFiles.add("/assets/libs/moment/locale/id.js");
			webResFiles.add("/assets/libs/moment/locale/sr.js");
			webResFiles.add("/assets/libs/moment/locale/ko.js");
			webResFiles.add("/assets/libs/moment/locale/da.js");
			webResFiles.add("/assets/libs/moment/locale/fy.js");
			webResFiles.add("/assets/libs/moment/locale/es-us.js");
			webResFiles.add("/assets/libs/moment/locale/hr.js");
			webResFiles.add("/assets/libs/moment/locale/mn.js");
			webResFiles.add("/assets/libs/moment/locale/si.js");
			webResFiles.add("/assets/libs/moment/locale/de-ch.js");
			webResFiles.add("/assets/libs/moment/locale/cy.js");
			webResFiles.add("/assets/libs/moment/locale/is.js");
			webResFiles.add("/assets/libs/moment/locale/eu.js");
			webResFiles.add("/assets/libs/moment/locale/km.js");
			webResFiles.add("/assets/libs/moment/locale/pa-in.js");
			webResFiles.add("/assets/libs/moment/locale/tet.js");
			webResFiles.add("/assets/libs/moment/locale/ja.js");
			webResFiles.add("/assets/libs/moment/locale/ro.js");
			webResFiles.add("/assets/libs/moment/locale/tzm-latn.js");
			webResDirectories.add("assets/libs/moment/min");
			webResFiles.add("/assets/libs/moment/min/locales.min.js");
			webResFiles.add("/assets/libs/moment/min/moment-with-locales.min.js");
			webResFiles.add("/assets/libs/moment/min/moment.min.js");
			webResDirectories.add("assets/libs/morris.js");
			webResFiles.add("/assets/libs/morris.js/morris.min.js");
			webResFiles.add("/assets/libs/morris.js/bower.travis.json");
			webResFiles.add("/assets/libs/morris.js/morris.css");
			webResDirectories.add("assets/libs/datatables.net-bs4");
			webResDirectories.add("assets/libs/datatables.net-bs4/js");
			webResFiles.add("/assets/libs/datatables.net-bs4/js/dataTables.bootstrap4.min.js");
			webResDirectories.add("assets/libs/datatables.net-bs4/css");
			webResFiles.add("/assets/libs/datatables.net-bs4/css/dataTables.bootstrap4.min.css");
			webResDirectories.add("assets/libs/perfect-scrollbar");
			webResDirectories.add("assets/libs/perfect-scrollbar/dist");
			webResFiles.add("/assets/libs/perfect-scrollbar/dist/perfect-scrollbar.jquery.min.js");
			webResFiles.add("/assets/libs/perfect-scrollbar/dist/perfect-scrollbar.common.js");
			webResFiles.add("/assets/libs/perfect-scrollbar/dist/perfect-scrollbar.esm.js");
			webResFiles.add("/assets/libs/perfect-scrollbar/dist/perfect-scrollbar.min.js");
			webResDirectories.add("assets/libs/perfect-scrollbar/dist/js");
			webResFiles.add("/assets/libs/perfect-scrollbar/dist/js/perfect-scrollbar.jquery.min.js");
			webResFiles.add("/assets/libs/perfect-scrollbar/dist/js/perfect-scrollbar.min.js");
			webResDirectories.add("assets/libs/perfect-scrollbar/dist/css");
			webResFiles.add("/assets/libs/perfect-scrollbar/dist/css/perfect-scrollbar.min.css");
			webResDirectories.add("assets/libs/raphael");
			webResFiles.add("/assets/libs/raphael/raphael.no-deps.min.js");
			webResFiles.add("/assets/libs/raphael/license.txt");
			webResFiles.add("/assets/libs/raphael/raphael.min.js");
			webResDirectories.add("assets/libs/raphael/dev");
			webResFiles.add("/assets/libs/raphael/dev/raphael.amd.js");
			webResFiles.add("/assets/libs/raphael/dev/raphael.svg.js");
			webResFiles.add("/assets/libs/raphael/dev/raphael.vml.js");
			webResFiles.add("/assets/libs/raphael/dev/raphaelTest.html");
			webResFiles.add("/assets/libs/raphael/dev/banner.txt");
			webResFiles.add("/assets/libs/raphael/dev/raphael.core.js");
			webResDirectories.add("assets/libs/chartist-plugin-tooltips");
			webResDirectories.add("assets/libs/chartist-plugin-tooltips/dist");
			webResFiles.add("/assets/libs/chartist-plugin-tooltips/dist/chartist-plugin-tooltip.min.js");
			webResFiles.add("/assets/libs/chartist-plugin-tooltips/dist/chartist-plugin-tooltip.css");
			webResDirectories.add("assets/extra-libs");
			webResDirectories.add("assets/extra-libs/datatables.net");
			webResFiles.add("/assets/extra-libs/datatables.net/package.json");
			webResFiles.add("/assets/extra-libs/datatables.net/License.txt");
			webResFiles.add("/assets/extra-libs/datatables.net/Readme.md");
			webResDirectories.add("assets/extra-libs/datatables.net/js");
			webResFiles.add("/assets/extra-libs/datatables.net/js/jquery.dataTables.min.js");
			webResFiles.add("/assets/extra-libs/datatables.net/js/jquery.dataTables.js");
			webResDirectories.add("assets/extra-libs/c3");
			webResFiles.add("/assets/extra-libs/c3/c3.min.js");
			webResFiles.add("/assets/extra-libs/c3/d3.min.js");
			webResFiles.add("/assets/extra-libs/c3/c3.min.css");
			webResDirectories.add("assets/extra-libs/knob");
			webResFiles.add("/assets/extra-libs/knob/jquery.knob.js");
			webResFiles.add("/assets/extra-libs/knob/jquery.knob.min.js");
			webResDirectories.add("assets/extra-libs/taskboard");
			webResDirectories.add("assets/extra-libs/taskboard/js");
			webResFiles.add("/assets/extra-libs/taskboard/js/task-init.js");
			webResFiles.add("/assets/extra-libs/taskboard/js/lobilist.min.js");
			webResFiles.add("/assets/extra-libs/taskboard/js/lobilist.js");
			webResFiles.add("/assets/extra-libs/taskboard/js/demo.js");
			webResFiles.add("/assets/extra-libs/taskboard/js/jquery.ui.touch-punch-improved.js");
			webResFiles.add("/assets/extra-libs/taskboard/js/jquery-ui.min.js");
			webResFiles.add("/assets/extra-libs/taskboard/js/lobibox.min.js");
			webResDirectories.add("assets/extra-libs/taskboard/less");
			webResFiles.add("/assets/extra-libs/taskboard/less/variables.less");
			webResFiles.add("/assets/extra-libs/taskboard/less/lobilist.less");
			webResFiles.add("/assets/extra-libs/taskboard/less/mixins.less");
			webResDirectories.add("assets/extra-libs/taskboard/example1");
			webResFiles.add("/assets/extra-libs/taskboard/example1/insert.php");
			webResFiles.add("/assets/extra-libs/taskboard/example1/update.php");
			webResFiles.add("/assets/extra-libs/taskboard/example1/delete.php");
			webResFiles.add("/assets/extra-libs/taskboard/example1/load.json");
			webResDirectories.add("assets/extra-libs/taskboard/css");
			webResFiles.add("/assets/extra-libs/taskboard/css/jquery-ui.min.css");
			webResFiles.add("/assets/extra-libs/taskboard/css/demo.css");
			webResFiles.add("/assets/extra-libs/taskboard/css/lobilist.min.css");
			webResFiles.add("/assets/extra-libs/taskboard/css/lobilist.css");
			webResDirectories.add("assets/extra-libs/jvector");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-world-mill-en.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-in-mill.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-2.0.2.min.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-us-aea-en.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-europe-mill-en.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-uk-mill-en.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-asia-mill.js");
			webResFiles.add("/assets/extra-libs/jvector/jvectormap.custom.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-us-il-chicago-mill-en.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-au-mill.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-de-mill.js");
			webResFiles.add("/assets/extra-libs/jvector/gdp-data.js");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-2.0.2.css");
			webResFiles.add("/assets/extra-libs/jvector/jquery-jvectormap-ca-lcc.js");
			webResDirectories.add("assets/extra-libs/prism");
			webResFiles.add("/assets/extra-libs/prism/prism.css");
			webResFiles.add("/assets/extra-libs/prism/prism-old.js");
			webResFiles.add("/assets/extra-libs/prism/prism.js");
			webResDirectories.add("assets/extra-libs/datatables.net-bs4");
			webResFiles.add("/assets/extra-libs/datatables.net-bs4/package.json");
			webResFiles.add("/assets/extra-libs/datatables.net-bs4/Readme.md");
			webResDirectories.add("assets/extra-libs/datatables.net-bs4/js");
			webResFiles.add("/assets/extra-libs/datatables.net-bs4/js/dataTables.bootstrap4.min.js");
			webResFiles.add("/assets/extra-libs/datatables.net-bs4/js/dataTables.bootstrap4.js");
			webResDirectories.add("assets/extra-libs/datatables.net-bs4/css");
			webResFiles.add("/assets/extra-libs/datatables.net-bs4/css/dataTables.bootstrap4.min.css");
			webResFiles.add("/assets/extra-libs/datatables.net-bs4/css/dataTables.bootstrap4.css");
			webResDirectories.add("assets/extra-libs/sparkline");
			webResFiles.add("/assets/extra-libs/sparkline/sparkline.js");
			webResDirectories.add("dist");
			webResDirectories.add("dist/js");
			webResFiles.add("/dist/js/sidebarmenu.min.js");
			webResFiles.add("/dist/js/custom.min.js");
			webResFiles.add("/dist/js/app-style-switcher.js");
			webResFiles.add("/dist/js/sidebarmenu.js");
			webResFiles.add("/dist/js/custom.js");
			webResFiles.add("/dist/js/app-style-switcher.min.js");
			webResFiles.add("/dist/js/feather.min.js");
			webResDirectories.add("dist/js/pages");
			webResDirectories.add("dist/js/pages/chartist");
			webResFiles.add("/dist/js/pages/chartist/chartist-init.min.js");
			webResFiles.add("/dist/js/pages/chartist/chartist-init.css");
			webResFiles.add("/dist/js/pages/chartist/chartist-plugin-tooltip.min.js");
			webResFiles.add("/dist/js/pages/chartist/chartist-plugin-tooltip.js");
			webResFiles.add("/dist/js/pages/chartist/chartist-init.js");
			webResDirectories.add("dist/js/pages/dashboards");
			webResFiles.add("/dist/js/pages/dashboards/dashboard1.min.js");
			webResFiles.add("/dist/js/pages/dashboards/dashboard1.js");
			webResDirectories.add("dist/js/pages/chartjs");
			webResFiles.add("/dist/js/pages/chartjs/chartjs.init.min.js");
			webResFiles.add("/dist/js/pages/chartjs/chartjs.init.js");
			webResDirectories.add("dist/js/pages/morris");
			webResFiles.add("/dist/js/pages/morris/morris-data.js");
			webResFiles.add("/dist/js/pages/morris/morris-data.min.js");
			webResDirectories.add("dist/js/pages/calendar");
			webResFiles.add("/dist/js/pages/calendar/cal-init.js");
			webResFiles.add("/dist/js/pages/calendar/cal-init.min.js");
			webResDirectories.add("dist/js/pages/datatable");
			webResFiles.add("/dist/js/pages/datatable/datatable-basic.init.js");
			webResFiles.add("/dist/js/pages/datatable/data.json");
			webResFiles.add("/dist/js/pages/datatable/datatable-basic.init.min.js");
			webResDirectories.add("dist/css");
			webResFiles.add("/dist/css/style.css");
			webResFiles.add("/dist/css/style.min.css");
			webResDirectories.add("dist/css/fonts");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Book.otf");
			webResFiles.add("/dist/css/fonts/Gilmer-Bold.otf");
			webResFiles.add("/dist/css/fonts/Gilmer-Bold.ttf");
			webResFiles.add("/dist/css/fonts/Gilmer-Bold.eot");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Medium.otf");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Book.ttf");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Medium.svg");
			webResFiles.add("/dist/css/fonts/Gilmer-Heavy.eot");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Book.eot");
			webResFiles.add("/dist/css/fonts/Gilmer-Regular.svg");
			webResFiles.add("/dist/css/fonts/Gilmer-Bold.svg");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Regular.ttf");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Regular.woff");
			webResFiles.add("/dist/css/fonts/Gilmer-Heavy.otf");
			webResFiles.add("/dist/css/fonts/Gilmer-Medium.ttf");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Regular.svg");
			webResFiles.add("/dist/css/fonts/Gilmer-Heavy.woff");
			webResFiles.add("/dist/css/fonts/Gilmer-Regular.ttf");
			webResFiles.add("/dist/css/fonts/Gilmer-Medium.woff");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Regular.eot");
			webResFiles.add("/dist/css/fonts/Gilmer-Bold.woff");
			webResFiles.add("/dist/css/fonts/Gilmer-Heavy.ttf");
			webResFiles.add("/dist/css/fonts/Gilmer-Regular.woff");
			webResFiles.add("/dist/css/fonts/Gilmer-Heavy.svg");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Medium.eot");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Medium.woff");
			webResFiles.add("/dist/css/fonts/Gilmer-Medium.svg");
			webResFiles.add("/dist/css/fonts/Gilmer-Regular.otf");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Book.woff");
			webResFiles.add("/dist/css/fonts/Gilmer-Medium.eot");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Regular.otf");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Book.svg");
			webResFiles.add("/dist/css/fonts/Gilmer-Regular.eot");
			webResFiles.add("/dist/css/fonts/Gilmer-Medium.otf");
			webResFiles.add("/dist/css/fonts/TofinoPersonal-Medium.ttf");
			webResDirectories.add("dist/css/icons");
			webResDirectories.add("dist/css/icons/themify-icons");
			webResFiles.add("/dist/css/icons/themify-icons/themify-icons.less");
			webResFiles.add("/dist/css/icons/themify-icons/themify-icons.css");
			webResDirectories.add("dist/css/icons/themify-icons/fonts");
			webResFiles.add("/dist/css/icons/themify-icons/fonts/themify.svg");
			webResFiles.add("/dist/css/icons/themify-icons/fonts/themify.ttf");
			webResFiles.add("/dist/css/icons/themify-icons/fonts/themify.woff");
			webResFiles.add("/dist/css/icons/themify-icons/fonts/themify.eot");
			webResDirectories.add("dist/css/icons/themify-icons/ie7");
			webResFiles.add("/dist/css/icons/themify-icons/ie7/ie7.css");
			webResFiles.add("/dist/css/icons/themify-icons/ie7/ie7.js");
			webResDirectories.add("dist/css/icons/font-awesome");
			webResDirectories.add("dist/css/icons/font-awesome/less");
			webResFiles.add("/dist/css/icons/font-awesome/less/fa-solid.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/fa-brands.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/fontawesome.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/fa-regular.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_fixed-width.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_screen-reader.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_variables.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_rotated-flipped.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_larger.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_list.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_icons.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_stacked.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_core.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_animated.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_bordered-pulled.less");
			webResFiles.add("/dist/css/icons/font-awesome/less/_mixins.less");
			webResDirectories.add("dist/css/icons/font-awesome/scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_list.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_animated.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_mixins.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_rotated-flipped.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_variables.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/fa-solid.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_fixed-width.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/fontawesome.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_bordered-pulled.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_stacked.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/fa-regular.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_larger.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_icons.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/fa-brands.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_core.scss");
			webResFiles.add("/dist/css/icons/font-awesome/scss/_screen-reader.scss");
			webResDirectories.add("dist/css/icons/font-awesome/css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fa-regular.min.css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fa-solid.min.css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fontawesome-all.css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fontawesome.min.css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fa-regular.css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fontawesome-all.min.css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fa-brands.min.css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fa-solid.css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fontawesome.css");
			webResFiles.add("/dist/css/icons/font-awesome/css/fa-brands.css");
			webResDirectories.add("dist/css/icons/font-awesome/webfonts");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-regular-400.eot");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-brands-400.eot");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-regular-400.ttf");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-solid-900.svg");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-brands-400.ttf");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-brands-400.woff");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-regular-400.svg");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-solid-900.woff");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-regular-400.woff2");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-regular-400.woff");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-brands-400.svg");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-brands-400.woff2");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-solid-900.woff2");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-solid-900.eot");
			webResFiles.add("/dist/css/icons/font-awesome/webfonts/fa-solid-900.ttf");
			webResDirectories.add("dist/css/icons/simple-line-icons");
			webResDirectories.add("dist/css/icons/simple-line-icons/fonts");
			webResFiles.add("/dist/css/icons/simple-line-icons/fonts/Simple-Line-Icons.svg");
			webResFiles.add("/dist/css/icons/simple-line-icons/fonts/Simple-Line-Icons.woff");
			webResFiles.add("/dist/css/icons/simple-line-icons/fonts/Simple-Line-Icons.ttf");
			webResFiles.add("/dist/css/icons/simple-line-icons/fonts/Simple-Line-Icons.woff2");
			webResFiles.add("/dist/css/icons/simple-line-icons/fonts/Simple-Line-Icons.eot");
			webResDirectories.add("dist/css/icons/simple-line-icons/less");
			webResFiles.add("/dist/css/icons/simple-line-icons/less/simple-line-icons.less");
			webResDirectories.add("dist/css/icons/simple-line-icons/scss");
			webResFiles.add("/dist/css/icons/simple-line-icons/scss/simple-line-icons.scss");
			webResDirectories.add("dist/css/icons/simple-line-icons/css");
			webResFiles.add("/dist/css/icons/simple-line-icons/css/simple-line-icons.css");
			webResDirectories.add("WEB-INF");
			webResFiles.add("/WEB-INF/web.xml");
			webResDirectories.add("css");
			webResFiles.add("/css/account.css");
			webResFiles.add("/css/default.css");
			webResFiles.add("/css/register.css");
			webResFiles.add("/css/component.css");
			webResFiles.add("/css/whork.css");
			webResFiles.add("/css/reg_jobseeker.css");
			webResFiles.add("/css/login.css");
			webResFiles.add("/css/chat.css");
			webResDirectories.add("html");
			webResFiles.add("/html/app-calendar.html");
			webResFiles.add("/html/ui-carousel.html");
			webResFiles.add("/html/ui-modals.html");
			webResFiles.add("/html/table-basic.html");
			webResFiles.add("/html/ticket-list.html");
			webResFiles.add("/html/ui-typography.html");
			webResFiles.add("/html/chart-morris.html");
			webResFiles.add("/html/ui-tab.html");
			webResFiles.add("/html/icon-simple-lineicon.html");
			webResFiles.add("/html/ui-breadcrumb.html");
			webResFiles.add("/html/ui-cards.html");
			webResFiles.add("/html/form-checkbox-radio.html");
			webResFiles.add("/html/form-input-grid.html");
			webResFiles.add("/html/form-inputs.html");
			webResFiles.add("/html/app-chat.html");
			webResFiles.add("/html/ui-spinner.html");
			webResFiles.add("/html/ui-list-media.html");
			webResFiles.add("/html/ui-popover.html");
			webResFiles.add("/html/ui-tooltip-popover.html");
			webResFiles.add("/html/table-sizing.html");
			webResFiles.add("/html/chart-chart-js.html");
			webResFiles.add("/html/ui-progressbar.html");
			webResFiles.add("/html/ui-scrollspy.html");
			webResFiles.add("/html/table-layout-coloured.html");
			webResFiles.add("/html/ui-notification.html");
			webResFiles.add("/html/ui-buttons.html");
			webResFiles.add("/html/table-dark-basic.html");
			webResFiles.add("/html/ui-bootstrap.html");
			webResFiles.add("/html/authentication-login1.html");
			webResFiles.add("/html/chart-knob.html");
			webResFiles.add("/html/authentication-register1.html");
			webResFiles.add("/html/index.html");
			webResFiles.add("/html/ui-grid.html");
			webResFiles.add("/html/table-datatable-basic.html");
			webResFiles.add("/html/icon-fontawesome.html");
			webResFiles.add("/html/ui-toasts.html");
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
