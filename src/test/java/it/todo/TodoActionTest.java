package it.todo;

import application.JaxrsApplication;
import application.datastore.DAO;
import application.datastore.DataSourceConnection;
import application.todo.Todo;
import application.todo.TodoAction;
import application.todo.TodoDAO;
import application.todo.TodoService;
import com.mongodb.DB;
import it.EndpointTest;
import it.JwtVerifier;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.common.i18n.Exception;
import org.apache.cxf.common.util.Base64Exception;
import org.bson.types.ObjectId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.event.Level;
import qualifiers.Action;
import qualifiers.Repository;
import qualifiers.Service;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.logging.Logger;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(Arquillian.class)
public class TodoActionTest extends EndpointTest{

    String uuid1, uuid2, uuid3;
    String token;
    private final String endpoint = "/todos";
    private static Jsonb jsonb;

    @Deployment
    public static WebArchive createDeployment() {
        //Create a JWT Token and push it to a token file
        try (PrintWriter out = new PrintWriter("src/test/resources/token.txt")) {
            Set<String> groups = new HashSet<>( );
            groups.add( "admin" );
            groups.add( "user" );

            JwtVerifier jwtVerifier = new JwtVerifier();
            String token = jwtVerifier.createJwt( "MYUSER", groups );
            System.out.println("TOKEN " + token);

            out.println(token);
        }catch ( java.lang.Exception e ) {
            e.printStackTrace();
        }

        File[] pomFiles = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();
        File configFile = new File("src/main/resources/META-INF/microprofile-config.properties");
        File keyStore = new File ("src/test/resources/keystore.jceks");
        File token = new File ("src/test/resources/token.txt");

        WebArchive warch = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(DAO.class,
                        Todo.class,
                        TodoAction.class,
                        TodoService.class,
                        TodoDAO.class,
                        DataSourceConnection.class,
                        JaxrsApplication.class,
                        Action.class,
                        Repository.class,
                        Service.class,
                        EndpointTest.class,
                        JwtVerifier.class)
                .addAsWebInfResource( EmptyAsset.INSTANCE, "beans.xml" )
                .addAsResource( configFile, "META-INF/microprofile-config.properties" )
                .addAsResource( keyStore, "keystore.jceks" )
                .addAsResource( token, "token.txt" )
                .addAsLibraries( pomFiles );
        return warch;
    }

    private @Inject DataSourceConnection DBcon;
    private @Inject @Repository TodoDAO todoDAO;

    @BeforeClass public static void setupClass() { }

    @Before public void setupTest() {
        this.jsonb = JsonbBuilder.create();
        //Fill dummy-data
        System.out.println("Filling data for test");
        this.uuid1 = UUID.randomUUID().toString();
        this.uuid2 = UUID.randomUUID().toString();
        this.uuid3 = UUID.randomUUID().toString();


        Todo dummytodo1 = new Todo(new ObjectId(), this.uuid1, "Gotta do 1 stuff", false, new Date(), new Date());
        Todo dummytodo2 = new Todo(new ObjectId(), this.uuid2, "Gotta do 2 stuff", false, new Date(), new Date());
        Todo dummytodo3 = new Todo(new ObjectId(), this.uuid3, "Gotta do 3 stuff", false, new Date(), new Date());

        DBcon.returnDSConnection().save(dummytodo1);
        DBcon.returnDSConnection().save(dummytodo2);
        DBcon.returnDSConnection().save(dummytodo3);

        //Read in the created token for integration testing
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            this.token = IOUtils.toString( classLoader.getResourceAsStream( "/token.txt" ), Charset.defaultCharset() ).trim();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }


    @Test public void test_getAllTodos_success(){
        Response resp = sendRequest(this.url + endpoint, "GET", this.token);
        assertThat( resp.getStatus(), is( equalTo(200 )));
        List<Todo> values = jsonb.fromJson(resp.readEntity(String.class)
                , new ArrayList<Todo>(){}.getClass().getGenericSuperclass());

        assertThat(resp, is(notNullValue()));
    }

    @Test public void test_getTodoByID_success() {
        Response resp = sendRequest(this.url+endpoint + "/" + this.uuid1, "GET", null);
        assertThat(resp, is(notNullValue()));
        assertThat(resp.getStatus(), is(equalTo(200)));
    }

    @Test public void test_getTodoByID_incorrectIDGiven() {
        String nonUsedId = UUID.randomUUID().toString();

        Response resp = sendRequest(this.url+endpoint + "/" + nonUsedId, "GET", null);
        assertThat(resp, is(notNullValue()));
        assertThat(resp.getStatus(), not(equalTo(200)));
    }

    @Test public void test_updateTodo_success() {
        String updatableTodoId = UUID.randomUUID().toString();
        Todo updatableTodo = new Todo(
                new ObjectId(),
                updatableTodoId,
                "my update todo",
                false,
                new Date(),
                new Date()
        );

        DBcon.returnDSConnection().save(updatableTodo);
        Response resp = sendRequest(this.url+endpoint + "/" + updatableTodoId, "GET", null);
        assertThat(resp.getStatus(), is(equalTo(200)));

        updatableTodo.setTodo("My newly changed Todo");
        resp = sendPutRequest(this.url+endpoint,jsonb.toJson(updatableTodo), null);
        assertThat(resp.getStatus(), is(equalTo(200)));

        DBcon.returnDSConnection().delete(Todo.class, updatableTodoId);
    }


    @Test public void test_createTodo_success() {
        String newTodoId = UUID.randomUUID().toString();
        Todo newTodo = new Todo();
        newTodo.setUuid(newTodoId);
        newTodo.setTodo("customtodo");
        newTodo.setCreated(new Date());
        newTodo.setDeadline(null);


        Response resp = sendPostRequest(this.url+endpoint, jsonb.toJson(newTodo), null);
        assertThat(resp, is(notNullValue()));
        assertThat(resp.getStatus(), is(equalTo(200)));

        DBcon.returnDSConnection().delete(Todo.class, newTodoId);
    }




    @After public void tearDownTest() {
        System.out.println("Cleaning up after test");
        if(DBcon.returnDSConnection().delete(Todo.class, uuid1).wasAcknowledged())
            System.out.println(uuid1 + "Object deleted");
        if(DBcon.returnDSConnection().delete(Todo.class, uuid2).wasAcknowledged())
            System.out.println(uuid2 + "Object deleted");
        if(DBcon.returnDSConnection().delete(Todo.class, uuid3).wasAcknowledged())
            System.out.println(uuid3 + "Object deleted");
    }

    @AfterClass public static void tearDownClass(){

    }
}
