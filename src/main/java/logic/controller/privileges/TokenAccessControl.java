package logic.controller.privileges;

import java.util.HashMap;
import java.util.Map;

import logic.util.tuple.Pair;

public class TokenAccessControl {
	private Map<String, Pair<String, Integer>> validTokens = new HashMap<>();

	protected TokenAccessControl() {

	}

	protected final void addOrRefresh(String userEmail) {

	}

	protected final String query(String token) {
		return null;
	}

	private final void del(String token) {

	}
}
