package infotech.jain.app.school.schoolapp.bean;

/**
 * Created by admin on 19/08/16.
 */
public class LeaveDescription
{
    private String status;
    private String leave_application_number;
    private Leave  leave;
    private String createdAt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeave_application_number() {
        return leave_application_number;
    }

    public void setLeave_application_number(String leave_application_number) {
        this.leave_application_number = leave_application_number;
    }

    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
