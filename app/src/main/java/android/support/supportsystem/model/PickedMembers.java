package android.support.supportsystem.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mahmoud shahen on 10/21/2016.
 */

public class PickedMembers implements Serializable{
    private String memberId;
    private Boolean Delivered;
    private String deliveryTime;
    private String Content;
    private List<Comment> Comments;
    public PickedMembers()
    {}

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Boolean getDelivered() {
        return Delivered;
    }

    public void setDelivered(Boolean delivered) {
        Delivered = delivered;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public List<Comment> getComments() {
        return Comments;
    }

    public void setComments(List<Comment> comments) {
        Comments = comments;
    }
}
