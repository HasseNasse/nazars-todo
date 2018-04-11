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
        logger.debug("*Init* \tService:\t" + ctx.getMethod().getName());
        Object value = ctx.proceed();
        logger.debug("-- *Finished* \tService:\t" + ctx.getMethod().getName());
        return value;
    }
}