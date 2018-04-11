package application.datastore;

import application.todo.Todo;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;



@Singleton
public class DataSourceConnection {

    Config config = ConfigProvider.getConfig();
    final Morphia morphia =  new Morphia();
    private Datastore ds;
    private MongoClient client;

    private DataSourceConnection(){
        try {
            this.client = new MongoClient(config.getValue("datastore.host", String.class)
                    , config.getValue("datastore.port", Integer.class).intValue());
            morphia.map(Todo.class);

            ds = morphia.createDatastore(this.client,config.getValue("datastore.db.name", String.class));

        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public Datastore returnDSConnection() {
        return this.ds;
    }

    @PreDestroy
    private void preDestroy(){
        this.client.close();
    }
}
