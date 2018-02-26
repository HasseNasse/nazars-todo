package application.todo;

import qualifiers.Repository;
import qualifiers.Service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;

@RequestScoped
@Service
public class TodoService {

    @Inject @Repository private TodoDAO todoDAO;
    Jsonb jsonb = JsonbBuilder.create();

    public String getAll(){
        List<Todo> todos = todoDAO.findAll();
        return jsonb.toJson(todos, List.class);
    }

    public boolean addTodoToStore(Todo todo){
        return todoDAO.insert(todo);
    }

    public boolean deleteTodo(Todo todo) {
        return todoDAO.delete(todo);
    }
}
