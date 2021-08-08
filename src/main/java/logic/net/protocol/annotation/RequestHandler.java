package logic.net.protocol.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;

/**
 * @author Stefano Belli
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface RequestHandler {
	public String value();
}
