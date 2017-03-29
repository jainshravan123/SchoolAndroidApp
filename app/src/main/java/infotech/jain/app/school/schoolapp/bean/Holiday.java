package infotech.jain.app.school.schoolapp.bean;

import java.util.ArrayList;

/**
 * Created by admin on 03/09/16.
 */
public class Holiday
{
    private String id;
    private String month;
    private String month_name;
    private String year;
    private String noOfWorkingDays;
    private String noOfHolidays;
    private ArrayList<HolidayDesc> holidaysDescList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getNoOfWorkingDays() {
        return noOfWorkingDays;
    }

    public void setNoOfWorkingDays(String noOfWorkingDays) {
        this.noOfWorkingDays = noOfWorkingDays;
    }

    public String getNoOfHolidays() {
        return noOfHolidays;
    }

    public void setNoOfHolidays(String noOfHolidays) {
        this.noOfHolidays = noOfHolidays;
    }

    public ArrayList<HolidayDesc> getHolidaysDescList() {
        return holidaysDescList;
    }

    public void setHolidaysDescList(ArrayList<HolidayDesc> holidaysDescList) {
        this.holidaysDescList = holidaysDescList;
    }
}
