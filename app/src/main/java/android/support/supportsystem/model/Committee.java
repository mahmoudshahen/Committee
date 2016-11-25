package android.support.supportsystem.model;

import java.util.List;

/**
 * Created by mahmoud shahen on 10/21/2016.
 */

public class Committee {

    private String Name;
    private List<Member> myMembers;
    private List<Task> myTasks;
    private List<Announcement> myAnnouncements;

    public Committee()
    {}
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Member> getMyMembers() {
        return myMembers;
    }

    public void setMyMembers(List<Member> myMembers) {
        this.myMembers = myMembers;
    }

    public List<Task> getMyTasks() {
        return myTasks;
    }

    public void setMyTasks(List<Task> myTasks) {
        this.myTasks = myTasks;
    }

    public List<Announcement> getMyAnnouncements() {
        return myAnnouncements;
    }

    public void setMyAnnouncements(List<Announcement> myAnnouncements) {
        this.myAnnouncements = myAnnouncements;
    }
}
