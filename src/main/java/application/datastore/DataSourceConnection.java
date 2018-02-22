package application.datastore;

import application.todo.Todo;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.inject.Singleton;

@Singleton
public class DataSourceConnection {
    final Morphia morphia =  new Morphia();
    private Datastore ds;
    private MongoClient client;

    private DataSourceConnection(){
        try {
            this.client = new MongoClient("localhost", 27017);
            morphia.map(Todo.class);

            ds = morphia.createDatastore(this.client,"todo");

        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public Datastore returnDSConnection() {
        return this.ds;
    }

    @PreDestroy
    private void preDestroy(){

    }
}
