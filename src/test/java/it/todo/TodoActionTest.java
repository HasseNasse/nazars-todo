package it.todo;

import application.todo.Todo;
import it.EndpointTest;
import org.junit.*;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Date;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.hamcrest.core.IsInstanceOf.instanceOf;


public class TodoActionTest extends EndpointTest{
    private final String endpoint = "/rest/todo";
    private static Jsonb jsonb;

    @BeforeClass public static void setupClass() {
        jsonb = JsonbBuilder.create();
    }

    @Before public void setupTest() {

    }


    @Test public void test_getAllTodos_success() {
        Response resp = sendRequest(this.url+endpoint, "GET");
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

    }

    @AfterClass public static void tearDownClass(){

    }
}
