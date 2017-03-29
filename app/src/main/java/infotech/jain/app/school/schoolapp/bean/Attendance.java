package infotech.jain.app.school.schoolapp.bean;

import java.util.ArrayList;

/**
 * Created by admin on 21/08/16.
 */
public class Attendance
{

    private int    id;
    private String month;
    private String month_name;
    private String year;
    private String absent;
    private String working_days;
    private ArrayList<String> absented_days_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getWorking_days() {
        return working_days;
    }

    public void setWorking_days(String working_days) {
        this.working_days = working_days;
    }

    public ArrayList<String> getAbsented_days_list() {
        return absented_days_list;
    }

    public void setAbsented_days_list(ArrayList<String> absented_days_list) {
        this.absented_days_list = absented_days_list;
    }
}
