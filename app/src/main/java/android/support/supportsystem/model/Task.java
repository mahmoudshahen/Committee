package android.support.supportsystem.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mahmoud shahen on 10/21/2016.
 */

public class Task implements Serializable {

    private String Id;
    private String Title;
    private String Content;
    private String DeadLine;
    private String timeStamp;
    private List<PickedMembers> assignedMembers;


    public Task()
    {}
    public List<PickedMembers> getAssignedMembers() {
        return assignedMembers;
    }

    public void setAssignedMembers(List<PickedMembers> assignedMembers) {
        this.assignedMembers = assignedMembers;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDeadLine() {
        return DeadLine;
    }

    public void setDeadLine(String deadLine) {
        DeadLine = deadLine;
    }
}
