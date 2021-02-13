package logic.factory;

import logic.model.UserAuthModel;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import logic.bean.UserAuthBean;

public class ModelFactory {
	private ModelFactory() {}

	public static UserAuthModel buildUserAuthModel(UserAuthBean userAuthBean) {
		UserAuthModel userAuthModel = new UserAuthModel();
		userAuthModel.setEmail(userAuthBean.getEmail());
		userAuthModel.setBcryptedPassword(
			new ByteArrayInputStream(
				userAuthBean.getPassword().getBytes(StandardCharsets.UTF_8)));

		return userAuthModel;
	}
}
