package infotech.jain.app.school.schoolapp.bean;

/**
 * Created by admin on 31/07/16.
 */
public class UserProfile
{

    private int user_id;
    private String name;
    private String fathers_name;
    private String mothers_name;
    private String fathers_mobile;
    private String mothers_mobile;
    private String email;
    private String home_address;
    private String school_address;
    private String admission_id;
    private String photo;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFathers_name() {
        return fathers_name;
    }

    public void setFathers_name(String fathers_name) {
        this.fathers_name = fathers_name;
    }

    public String getMothers_name() {
        return mothers_name;
    }

    public void setMothers_name(String mothers_name) {
        this.mothers_name = mothers_name;
    }

    public String getFathers_mobile() {
        return fathers_mobile;
    }

    public void setFathers_mobile(String fathers_mobile) {
        this.fathers_mobile = fathers_mobile;
    }

    public String getMothers_mobile() {
        return mothers_mobile;
    }

    public void setMothers_mobile(String mothers_mobile) {
        this.mothers_mobile = mothers_mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getSchool_address() {
        return school_address;
    }

    public void setSchool_address(String school_address) {
        this.school_address = school_address;
    }

    public String getAdmission_id() {
        return admission_id;
    }

    public void setAdmission_id(String admission_id) {
        this.admission_id = admission_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
