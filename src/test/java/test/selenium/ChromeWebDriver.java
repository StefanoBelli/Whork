package test.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public final class ChromeWebDriver {
	private ChromeWebDriver() {}

	public static WebDriver getWebDriver() {
		final String os = System.getProperty("os.name").toLowerCase();
		final String driver = os.equals("linux") ? "chromedriver" : "chromedriver.exe";
		
		System.setProperty("webdriver.chrome.driver", 
			new StringBuilder("webdriver/").append(driver).toString());

		return new ChromeDriver();
	}
}
