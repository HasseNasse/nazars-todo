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
public class GeneralRepositoryInterceptor {
    final static Logger logger = LoggerFactory.getLogger(GeneralRepositoryInterceptor.class);


    @AroundInvoke
    public Object handleTransaction(InvocationContext ctx) throws Exception{
        logger.debug("*Init* \tDAO func.:\t\t" + ctx.getMethod().getName());
        Object value = ctx.proceed();
        logger.debug("-- *Finished* \tDAO func.:\t\t" + ctx.getMethod().getName());
        return value;
    }
}
