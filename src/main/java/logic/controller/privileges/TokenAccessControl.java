package logic.controller.privileges;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import logic.util.Util;
import logic.util.tuple.Pair;

public class TokenAccessControl {
	private final Map<String, Pair<String, Long>> validTokens = new HashMap<>();
	private static final int VALID_TOKEN_INTVL = 300; //seconds

	protected TokenAccessControl() {}

	protected final void addOrRefresh(String userEmail) {
		validTokens.put(Util.generateToken(), new Pair<>(userEmail, new Date().getTime()));
	}

	protected final String query(String tok) {
		Date nowTime = new Date();
		Pair<String, Long> token = validTokens.get(tok);

		if(nowTime.getTime() - token.getSecond() > VALID_TOKEN_INTVL) {
			validTokens.remove(tok);
			return null;
		}

		return token.getFirst();
	}
}
