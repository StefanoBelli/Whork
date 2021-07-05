package logic.net.protocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import logic.exception.AlreadyRegisteredCommandException;
import logic.exception.InvalidMethodSignatureException;
import logic.net.protocol.annotation.RequestHandler;
import logic.util.Util;
import logic.util.tuple.Pair;

public final class StatelessProtocol {
	private final Map<String, Method> handlers = new HashMap<>();
	private final Object impl;

	public StatelessProtocol(Object impl) {
		this.impl = impl;
		
		Class<?> implClass = impl.getClass();
		Method[] implClassMethods = implClass.getMethods();

		for(final Method reqMethod : implClassMethods) {

			RequestHandler[] annots = reqMethod.getAnnotationsByType(RequestHandler.class);
			if(annots.length > 0) {
				
				String cmdName = annots[0].value();

				for(final Map.Entry<String, Method> entry : handlers.entrySet()) {
					if(entry.getKey().equals(cmdName)) {
						throw new AlreadyRegisteredCommandException(
							cmdName, entry.getValue().getName(), reqMethod.getName());
					}
				}

				boolean goodMethodSignature =
					reqMethod.getReturnType() == Response.class &&
					reqMethod.getParameterCount() == 1 &&
					reqMethod.getParameterTypes()[0] == Request.class;

				if(goodMethodSignature) {
					handlers.put(cmdName, reqMethod);
				} else {
					throw new InvalidMethodSignatureException(reqMethod.getName());
				}
			}
		}
	}

	private Pair<String, Boolean> errorResponseCloseNow(String str) {
		Response errorResp = new Response();
		errorResp.setStatus(Response.Status.KO);
		errorResp.setBody(str);
		errorResp.addHeaderEntry("Content-Length", Integer.toString(str.length()));

		return new Pair<>(errorResp.toString(), true);
	}

	public Pair<String,Boolean> onMessage(String what) {
		Pair<Method, Request> p = parseAndBuildPair(what);

		if(p == null) {
			return errorResponseCloseNow("SyntaxOrInvalidCommandError");
		}

		Response resp;

		try {
			resp = (Response) p.getFirst().invoke(impl, p.getSecond());
		} catch(IllegalStateException | 
				InvocationTargetException | 
				IllegalAccessException e) {
			Util.exceptionLog(e);
			return errorResponseCloseNow("InvokeError");
		}

		if(resp == null) {
			return errorResponseCloseNow("MissingImplementationError");
		}
		
		return new Pair<>(resp.toString(), false);
	}

	private Pair<Method, Request> 
		buildResultingPair(String cmd, Map<String, String> hdrs, String body) {

		Method handler = handlers.get(cmd);
		if(handler == null) {
			return null;
		}

		Request request = new Request();
		request.setBody(body);
		request.setHeaders(hdrs);

		return new Pair<>(handler, request);
	}

	private Pair<Method, Request> parseAndBuildPair(String msg) {
		int cmdPlusHdrEndIdx = msg.indexOf('\0', 0);
		if(cmdPlusHdrEndIdx == -1) {
			return null;
		}

		String[] cmdPlusHdr = msg.substring(0, cmdPlusHdrEndIdx).split("\t");
		if(cmdPlusHdr.length != 2) {
			return null;
		}

		String cmd = cmdPlusHdr[0];
		if(cmd.isEmpty() || cmd.isBlank()) {
			return null;
		}

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

	public static final class Response {
		public enum Status {
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

		public void setStatus(Status status) {
			this.status = status;
		}

		public void addHeaderEntry(String key, String value) {
			this.headers.put(key, value);
		}

		public void setBody(String body) {
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
				.append("\0")
				.append(body);

			return builder.toString();
		}
	}

	public static final class Request {
		private Map<String, String> headers;
		private String body;

		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getBody() {
			return body;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}
	}
}
