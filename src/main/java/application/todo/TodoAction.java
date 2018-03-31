package application.todo;

import qualifiers.Action;
import qualifiers.Service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.bson.types.ObjectId;

@Action
@Path("/todo")
public class TodoAction {

    @Inject @Service private TodoService todoService;
    Jsonb jsonb = JsonbBuilder.create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTodos(){
        List<Todo> todos = todoService.findAll();
        String todosJson = jsonb.toJson(todos, List.class);
        return Response.ok(todosJson).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findTodoById(@PathParam("id") String id){
        try{
            todoService.findById(id);
        }catch(NoSuchElementException ex){
            return Response.noContent().build();
        }
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTodo(final Todo todo){
        return todoService.addTodoToStore(todo) ? Response.ok().build() : Response.status(403).build();
    }
}
