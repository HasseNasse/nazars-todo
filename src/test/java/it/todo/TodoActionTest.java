package it.todo;

import application.datastore.DataSourceConnection;
import application.todo.Todo;
import application.todo.TodoAction;
import application.todo.TodoDAO;
import application.todo.TodoService;
import it.EndpointTest;
import org.bson.types.ObjectId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.*;
import org.junit.runner.RunWith;
import qualifiers.Repository;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    ObjectId id1, id2, id3;
    private final String endpoint = "/rest/todo";
    private static Jsonb jsonb;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "TodoActionTest.war")
                .addClasses(Todo.class, TodoAction.class, TodoService.class,
                        TodoDAO.class, DataSourceConnection.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private @Inject DataSourceConnection DBcon;
    private @Inject @Repository TodoDAO todoDAO;

    @BeforeClass public static void setupClass() {
        jsonb = JsonbBuilder.create();
    }

    @Before public void setupTest() {
        //Fill dummy-data
        System.out.println("Filling data for test");
        this.id1 = new ObjectId();
        this.id2 = new ObjectId();
        this.id3 = new ObjectId();


        Todo dummytodo1 = new Todo(this.id1, "Gotta do 1 stuff", false, new Date(), new Date());
        Todo dummytodo2 = new Todo(this.id2, "Gotta do 2 stuff", false, new Date(), new Date());
        Todo dummytodo3 = new Todo(this.id3, "Gotta do 3 stuff", false, new Date(), new Date());

        DBcon.returnDSConnection().save(dummytodo1);
        DBcon.returnDSConnection().save(dummytodo2);
        DBcon.returnDSConnection().save(dummytodo3);
    }


    @Test public void test_getAllTodos_success() {
        Response resp = sendRequest(this.url+endpoint, "GET");
        List<Todo> values = jsonb.fromJson(resp.readEntity(String.class)
                , new ArrayList<Todo>(){}.getClass().getGenericSuperclass());

        assertThat(resp, is(notNullValue()));
        assertThat(resp.getStatus(), is(equalTo(200)));
    }

    @Test public void test_getTodoByID_success() {
        int id = 1;

        Response resp = sendRequest(this.url+endpoint + "/" + 1, "GET");
        assertThat(resp, is(notNullValue()));
        assertThat(resp.getStatus(), is(equalTo(200)));
    }

    @Test public void test_getTodoByID_incorrectIDGiven() {
        String falseID = "bla";

        Response resp = sendRequest(this.url+endpoint + "/" + falseID, "GET");
        assertThat(resp, is(notNullValue()));
        assertThat(resp.getStatus(), not(equalTo(200)));
    }


    @Test public void test_createTodo_success() {
        Todo newTodo = new Todo();
        newTodo.setTodo("Eat food");
        newTodo.setCreated(new Date());
        newTodo.setDeadline(null);


        Response resp = sendPostRequest(this.url+endpoint, jsonb.toJson(newTodo));
        assertThat(resp, is(notNullValue()));
        assertThat(resp.getStatus(), is(equalTo(200)));
    }




    @After public void tearDownTest() {
        System.out.println("Cleaning up after test");
        if(DBcon.returnDSConnection().delete(Todo.class, id1).wasAcknowledged())
            System.out.println(id1 + "Object deleted");
        if(DBcon.returnDSConnection().delete(Todo.class, id2).wasAcknowledged())
            System.out.println(id2 + "Object deleted");
        if(DBcon.returnDSConnection().delete(Todo.class, id3).wasAcknowledged())
            System.out.println(id3 + "Object deleted");
    }

    @AfterClass public static void tearDownClass(){

    }
}
