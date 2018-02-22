package application.todo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class TodoService {

    @Inject private TodoDAO todoDAO;

    public String getAll(){
        return todoDAO.getAll();
    }

    public boolean addTodoToStore(Todo todo){
        return todoDAO.post(todo);
    }
}
