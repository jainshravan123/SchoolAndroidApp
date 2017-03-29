package infotech.jain.app.school.schoolapp.bean;

/**
 * Created by admin on 20/08/16.
 */
public class Syllabus
{
    private int    id;
    private String exam_name;
    private String attachment_URL;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getAttachment_URL() {
        return attachment_URL;
    }

    public void setAttachment_URL(String attachment_URL) {
        this.attachment_URL = attachment_URL;
    }
}
