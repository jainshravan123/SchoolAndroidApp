package infotech.jain.app.school.schoolapp.bean;

/**
 * Created by admin on 02/09/16.
 */
public class Exam
{
    public int    examTermId;
    public String examTermName;
    public String subjectName;
    public String totalMarks;
    public String outOfTotal;
    public String grade;
    public String status;

    public int getExamTermId() {
        return examTermId;
    }

    public void setExamTermId(int examTermId) {
        this.examTermId = examTermId;
    }

    public String getExamTermName() {
        return examTermName;
    }

    public void setExamTermName(String examTermName) {
        this.examTermName = examTermName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getOutOfTotal() {
        return outOfTotal;
    }

    public void setOutOfTotal(String outOfTotal) {
        this.outOfTotal = outOfTotal;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
