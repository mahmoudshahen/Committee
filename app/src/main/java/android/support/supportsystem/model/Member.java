package android.support.supportsystem.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mahmoud shahen on 10/21/2016.
 */

public class Member implements Serializable{

    private List<String> TasksId;
    private String Committee;
    public  User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommittee() {
        return Committee;
    }

    public void setCommittee(String Committee) {
        this.Committee = Committee;
    }

    public List<String> getTasksId() {
        return TasksId;
    }

    public void setTasksId(List<String> TasksId) {
        this.TasksId = TasksId;
    }
}
