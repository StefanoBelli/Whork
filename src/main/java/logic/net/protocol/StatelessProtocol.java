package logic.net.protocol;

import java.util.HashMap;
import java.util.Map;

import logic.net.Protocol;
import logic.util.tuple.Pair;

abstract class StatelessProtocol implements Protocol {
	private Map<String, OnRequestHandler> handlers = new HashMap<>();

	void addCommand(String name, OnRequestHandler handler) {
		handlers.put(name, handler);
	}

	@Override
	public Pair<String,Boolean> onMessage(String what) {
		return null; //TODO here
	}

	private Pair<OnRequestHandler, Request> 
		buildResultingPair(String cmd, Map<String, String> hdrs, String body) {

		OnRequestHandler handler = handlers.get(cmd);
		if(handler == null) {
			return null;
		}

		Request request = new Request();
		request.setBody(body);
		request.setHeaders(hdrs);

		return new Pair<>(handler, request);
	}

	private Pair<OnRequestHandler, Request> parseAndBuildPair(String msg) {
		//find the first '\0' character, that is meaning that cmd+hdr is done
		int cmdPlusHdrEndIdx = msg.indexOf('\0', 0);
		if(cmdPlusHdrEndIdx == -1) {
			return null;
		}

		//extract cmd+hdr (substring) and work ONLY on it
		//(so we don't require any escaping of the body if working
		//using split), on the substring, split based of '\t'
		String[] cmdPlusHdr = msg.substring(0, cmdPlusHdrEndIdx).split("\t");
		if(cmdPlusHdr.length != 2) {
			return null;
		}

		//get cmd
		String cmd = cmdPlusHdr[0];
		if(cmd.isEmpty() || cmd.isBlank()) {
			return null;
		}

		//by checking for cmdPlusHdr.length != 2, we are also ensuring
		//that headers are always here
		Map<String, String> hdrs = new HashMap<>();
		String hdr = cmdPlusHdr[1];
		String[] hdrKvs = hdr.split("\n");
		for (final String hdrKv : hdrKvs) {
			int sp = hdrKv.indexOf(":", 0);
			if (sp == -1) {
				return null;
			}

			String key = hdrKv.substring(0, sp);
			String value = hdrKv.substring(sp + 1, hdrKv.length());

			if(key.isEmpty() || key.isBlank()) {
				return null;
			}

			hdrs.put(key, value);
		}

		return buildResultingPair(cmd, hdrs, msg.substring(cmdPlusHdrEndIdx + 1, msg.length()));
	}

	interface OnRequestHandler {
		Response onRequest(Request request);
	}

	static class Response {
		enum Status {
			OK("OK"),
			KO("KO");

			private final String name;

			private Status(String s) {
        		name = s;
    		}

			@Override
			public String toString() {
				return this.name;
			}
		}

		private Status status;
		private Map<String, String> headers = new HashMap<>();
		private String body;

		void setStatus(Status status) {
			this.status = status;
		}

		void addHeader(String key, String value) {
			this.headers.put(key, value);
		}

		void setBody(String body) {
			this.body = body;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();

			builder.append(status.toString()).append("\t");

			for(final Map.Entry<String, String> entry : headers.entrySet()) {
				builder
					.append(entry.getKey())
					.append(":")
					.append(entry.getValue())
					.append("\n");
			}

			builder
				.append("\r")
				.append(body);

			return builder.toString();
		}
	}

	static class Request {
		private Map<String, String> headers;
		private String body;

		void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}

		void setBody(String body) {
			this.body = body;
		}

		String getBody() {
			return body;
		}

		Map<String, String> getHeaders() {
			return headers;
		}
	}
}
