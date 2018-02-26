package application.interceptors;

import org.slf4j.LoggerFactory;
import qualifiers.Action;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Level;
import org.slf4j.Logger;

@Interceptor
@Action
public class GeneralActionInterceptor {
    final static Logger logger = LoggerFactory.getLogger(GeneralActionInterceptor.class);


    @AroundInvoke
    public Object handleTransaction(InvocationContext ctx) throws Exception{

        logger.debug("*Init* Action: " + ctx.getMethod() + "...");
        Object value = ctx.proceed();
        logger.debug("-- *Finished* Action: " + ctx.getMethod());
        return value;
    }
}
