package application;

import org.eclipse.microprofile.auth.LoginConfig;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//@LoginConfig(
//        authMethod = "MP-JWT",
//        realmName = "MP-JWT"
//)
//@DeclareRoles({"user", "admin"})
@ApplicationPath("/todos")
public class JaxrsApplication extends Application {
}
