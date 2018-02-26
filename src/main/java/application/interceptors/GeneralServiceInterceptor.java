package application.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qualifiers.Action;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Action
public class GeneralServiceInterceptor {
    final static Logger logger = LoggerFactory.getLogger(GeneralActionInterceptor.class);


    @AroundInvoke
    public Object handleTransaction(InvocationContext ctx) throws Exception{
        logger.debug("*Init* Service: " + ctx.getMethod() + "...");
        Object value = ctx.proceed();
        logger.debug("-- *Finished* Service: " + ctx.getMethod());
        return value;
    }
}