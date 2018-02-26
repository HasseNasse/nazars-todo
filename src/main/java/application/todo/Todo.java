package application.todo;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity(value="todos", noClassnameStored = true)
public class Todo {
    @Id private ObjectId id;
    private String todo;
    private boolean finished;
    private Date created;
    private Date deadline;

    public Todo(ObjectId id, String todo, boolean finished, Date created, Date deadline) {
        this.id = id;
        this.todo = todo;
        this.finished = finished;
        this.created = created;
        this.deadline = deadline;
    }

    public Todo() {

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
