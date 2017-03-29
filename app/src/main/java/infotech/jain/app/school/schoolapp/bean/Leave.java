package infotech.jain.app.school.schoolapp.bean;

/**
 * Created by admin on 18/08/16.
 */
public class Leave
{
    private User      user;
    private String    details;
    private LeaveType leaveType;
    private String    leaveFrom;
    private String    leaveTill;
    private String    attached_doc_path;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveFrom() {
        return leaveFrom;
    }

    public void setLeaveFrom(String leaveFrom) {
        this.leaveFrom = leaveFrom;
    }

    public String getLeaveTill() {
        return leaveTill;
    }

    public void setLeaveTill(String leaveTill) {
        this.leaveTill = leaveTill;
    }

    public String getAttached_doc_path() {
        return attached_doc_path;
    }

    public void setAttached_doc_path(String attached_doc_path) {
        this.attached_doc_path = attached_doc_path;
    }
}
