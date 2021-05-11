package logic.net;

import logic.util.tuple.Pair;

public interface Protocol {
	Pair<String, Boolean> onMessage(String what);
}
