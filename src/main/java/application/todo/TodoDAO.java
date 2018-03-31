package application.todo;

import application.datastore.DAO;
import application.datastore.DataSourceConnection;
import com.mongodb.operation.UpdateOperation;
import org.bson.types.ObjectId;
import qualifiers.Repository;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public Todo findByID(ObjectId id) {
       Todo todo =  dbConnection
                .returnDSConnection()
                .get(Todo.class, id);
        return todo;
    }

    @Override
    public boolean insert(Todo obj) {
        ObjectId id = new ObjectId();
        obj.setId(id);
        dbConnection
                .returnDSConnection()
                .save(obj);

        return dbConnection
                .returnDSConnection()
                .get(Todo.class, id) != null;
    }

    @Override
    public boolean update(Todo obj) {
        //// TODO: 2018-02-23 FINISH UPDATE STATEMENT
        Todo todo = dbConnection
                        .returnDSConnection()
                        .get(Todo.class, obj.getId());

        todo.setTodo(obj.getTodo());
        todo.setDeadline(obj.getDeadline());
        todo.setFinished(obj.isFinished());
        return false;
    }

    @Override
    public boolean delete(Todo obj) {
        return deleteByID(obj.getId());
    }

    @Override
    public boolean deleteByID(ObjectId id) {
        return dbConnection
                .returnDSConnection()
                .delete(Todo.class, id)
                .wasAcknowledged();
    }
}
