package application.interceptors;

import qualifiers.Action;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Interceptor
@Action
public class GeneralActionInterceptor {
    final Logger logger = LoggerFactory.getLogger(GeneralActionInterceptor.class);


    @AroundInvoke
    public Object handleTransaction(InvocationContext ctx) throws Exception{
        logger.debug("*Init* Action: " + ctx.getMethod() + "...");
        Object value = ctx.proceed();
        logger.debug("-- *Finished* Action: " + ctx.getMethod());
        return value;
    }
}
