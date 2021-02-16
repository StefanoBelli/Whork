package test.econtroller;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import logic.bean.UserBean;
import logic.controller.LoginController;
import logic.exception.InternalException;
import logic.exception.SyntaxException;
import logic.factory.BeanFactory;

public class TestLoginController {
	@Test
	public void testBasicLoginEmployeeFail() 
			throws InternalException, SyntaxException {
		UserBean userBean = 
			LoginController.basicLogin(BeanFactory.buildUserAuthBean("me@az.com", "aa"));

		assertEquals(null, userBean);
	}

	@Test
	public void testBasicLoginJobSeekerFail() 
			throws InternalException, SyntaxException {
		UserBean userBean = 
			LoginController.basicLogin(BeanFactory.buildUserAuthBean("a@b.com", "aa"));

		assertEquals(null, userBean);
	}

	@Test
	public void testBasicLoginEmployeeOk() 
			throws InternalException, SyntaxException {
		UserBean userBean = 
			LoginController.basicLogin(BeanFactory.buildUserAuthBean("me@az.com", "pwd123"));

		assertNotEquals(null, userBean);
	}

	@Test
	public void testBasicLoginJobSeekerOk() 
			throws InternalException, SyntaxException {
		UserBean userBean = 
			LoginController.basicLogin(BeanFactory.buildUserAuthBean("a@b.com", "pwd123"));

		assertNotEquals(null, userBean);
	}

	@Test
	public void testBasicLoginNonExistantShouldFail() 
			throws InternalException, SyntaxException {
		UserBean userBean = 
			LoginController.basicLogin(BeanFactory.buildUserAuthBean("lol@notex.ists", "abcd"));

		assertEquals(null, userBean);
	}

	@Test
	public void testBasicLoginWithRegPendingJobSeekerShouldFail() 
			throws InternalException, SyntaxException {
		UserBean userBean = 
			LoginController.basicLogin(BeanFactory.buildUserAuthBean("ll@bcd.com", "pwd123"));

		assertEquals(null, userBean);
	}

	@Test
	public void testBasicLoginWithRegPendingEmployeeShouldFail() 
			throws InternalException, SyntaxException {
		UserBean userBean = 
			LoginController.basicLogin(BeanFactory.buildUserAuthBean("brbr@xx.com", "pwd123"));

		assertEquals(null, userBean);
	}

	@Test
	public void testBasicLoginEmployeeWrongPwdShouldFail() 
			throws InternalException, SyntaxException {
		UserBean userBean = LoginController.basicLogin(BeanFactory.buildUserAuthBean("me@az.com", "pwd12x"));

		assertEquals(null, userBean);
	}

	@Test
	public void testBasicLoginJobSeekerWrongPwdShouldFail() 
			throws InternalException, SyntaxException {
		UserBean userBean = LoginController.basicLogin(BeanFactory.buildUserAuthBean("a@b.com", "pwd12x"));

		assertEquals(null, userBean);
	}
}
