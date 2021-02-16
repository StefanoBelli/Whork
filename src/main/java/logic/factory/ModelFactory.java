package logic.factory;

import logic.model.UserAuthModel;
import logic.util.Util;

import java.io.ByteArrayInputStream;

import logic.bean.UserAuthBean;

public final class ModelFactory {
	private ModelFactory() {}

	public static UserAuthModel buildUserAuthModel(UserAuthBean userAuthBean) {
		UserAuthModel userAuthModel = new UserAuthModel();
		userAuthModel.setEmail(userAuthBean.getEmail());
		userAuthModel.setBcryptedPassword(
			new ByteArrayInputStream(
				Util.Bcrypt.hash(userAuthBean.getPassword())));

		return userAuthModel;
	}
}
