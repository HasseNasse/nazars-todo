package application.todo;

import qualifiers.Action;
import qualifiers.Service;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Action
@Path("/todo")
public class TodoAction {

    @Inject @Service private TodoService todoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTodos(){
        String res = todoService.getAll();
        return Response.ok(res).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoByID(@PathParam("id") int id){
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTodo(final Todo todo){
        return todoService.addTodoToStore(todo) ? Response.ok().build() : Response.status(403).build();
    }
}
