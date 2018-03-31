package application.todo;

import qualifiers.Repository;
import qualifiers.Service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.NoSuchElementException;

@RequestScoped
@Service
public class TodoService {

    @Inject @Repository private TodoDAO todoDAO;

    public List<Todo> findAll(){
        List<Todo> todos = todoDAO.findAll();
        return todos;
    }

    public Todo findById(String id) throws NoSuchElementException{
        Todo todo = todoDAO.findByID(new ObjectId(id));
        if(todo == null) throw new NoSuchElementException();
        else return todoDAO.findByID(new ObjectId(id));
    }

    public boolean addTodoToStore(Todo todo){
        return todoDAO.insert(todo);
    }

    public boolean deleteTodo(Todo todo) {
        return todoDAO.delete(todo);
    }
}
