package application.todo;

import application.datastore.DAO;
import application.datastore.DataSourceConnection;
import qualifiers.Repository;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Repository
public class TodoDAO implements DAO<Todo> {

    Jsonb jsonb = JsonbBuilder.create();
    @Inject private DataSourceConnection dbConnection;

    @Override
    public List<Todo> findAll() {
        List<Todo> allTodos = new ArrayList<>();
        Iterator<Todo> it = dbConnection.returnDSConnection()
                .find(Todo.class).iterator();
        while(it.hasNext())
            allTodos.add(it.next());

        return allTodos;
    }

    @Override
    public Todo findByID(String id) {
       Todo todo = dbConnection
                .returnDSConnection()
                .get(Todo.class, id);
        return todo;
    }

    @Override
    public boolean insert(Todo obj) {
        String id = UUID.randomUUID().toString();
        if(obj.getUuid() != null)
            id = obj.getUuid();
        else obj.setUuid(id);

        dbConnection
                .returnDSConnection()
                .save(obj);

        return dbConnection
                .returnDSConnection()
                .get(Todo.class, id) != null;
    }

    @Override
    public boolean update(Todo obj) {
        Todo todo = dbConnection
                        .returnDSConnection()
                        .get(Todo.class, obj.getUuid());

        todo.setTodo(obj.getTodo());
        todo.setDeadline(obj.getDeadline());
        todo.setFinished(obj.isFinished());
        dbConnection.returnDSConnection().delete(Todo.class, todo.getUuid());
        return dbConnection.returnDSConnection().save(todo) != null;
    }

    @Override
    public boolean delete(Todo obj) {
        return deleteByID(obj.getUuid());
    }

    @Override
    public boolean deleteByID(String id) {
        return dbConnection
                .returnDSConnection()
                .delete(Todo.class, id)
                .wasAcknowledged();
    }
}
