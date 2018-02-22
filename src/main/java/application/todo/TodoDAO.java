package application.todo;

import application.datastore.DataSourceConnection;
import com.mongodb.DB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ApplicationScoped
public class TodoDAO {

    Jsonb jsonb = JsonbBuilder.create();
    @Inject private DataSourceConnection dbConnection;

    public String getAll() {
        List<Todo> allTodos = new ArrayList<>();
        Iterator<Todo> it = dbConnection.returnDSConnection()
                .find(Todo.class).iterator();
        while(it.hasNext())
            allTodos.add(it.next());

        return jsonb.toJson(allTodos, List.class);
    }

    public boolean post(Todo todo){
        dbConnection.returnDSConnection().save(todo);
        return true;
    }
}
