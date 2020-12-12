package test.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.controller.LoginController;

public class TestLoginController {

	@Test
	public void testNullLogin() {
		assertEquals(null, LoginController.userBasicAuth("", ""));
	}
	
	@Test
	public void testNullCookie() {
		assertEquals(null, LoginController.validateCookie(""));
	}

}
