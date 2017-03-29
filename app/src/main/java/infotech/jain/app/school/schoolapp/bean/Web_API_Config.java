package infotech.jain.app.school.schoolapp.bean;

/**
 * Created by admin on 17/06/16.
 */
public class Web_API_Config
{

    public static String root_domain_url                  = "http://manage-school.in/school/api/";
    public static String root_image_url                   = "http://manage-school.in/school/";




    public static String  school_name_API                 = root_domain_url + "getSchoolLogoName.php";
    public static String userLoginValidateAPI             = root_domain_url + "userLoginValidate.php";
    public static String userSignUpAPI                    = root_domain_url + "registerUserSave.php";
    public static String userProfile                      = root_domain_url + "getProfile.php?Addmission_No=";
    public static String modulesAPI                       = root_domain_url + "enabledModules.php";

    public static String subjectsAPI                      = root_domain_url + "getHomework.php?";
    public static String circularAPI                      = root_domain_url + "getCirculars.php";
    public static String circularDescAPI                  = root_domain_url + "getCircularDetails.php?Circular_Id=";
    public static String galleryCategoryAPI               = root_domain_url + "getGalleryCategory.php";
    public static String galleryCategoryDescAPI           = root_domain_url + "getGalleryCategoryImages.php?Gallery_Category_Id=";
    public static String send_token_fcm_notification      = root_domain_url + "saveTokenMacIdUserId.php";
    public static String update_user_id_fcm_notification  = root_domain_url + "updateTokenMacIdUserId.php";
    public static String apply_leave_API                  = root_domain_url + "applyLeaveSave.php";
    public static String leave_type_API                   = root_domain_url + "getLeaveOptions.php";
    public static String previous_leaves_API              = root_domain_url + "applyLeaveList.php?SId=";
    public static String previous_leave_desc_API          = root_domain_url + "applyLeaveListDetail.php?Leave_Application_Id=";
    public static String subjects_API                     = root_domain_url + "getSyllabus.php?SId=";
    public static String syllabus_API                     = root_domain_url + "getSyllabusForSubject.php";
    public static String attendance_API                   = root_domain_url + "getAttendence.php?SId=";
    public static String attendance_desc_API              = root_domain_url + "getAttendenceDetails.php?";
    public static String homework_subjects_API            = root_domain_url + "getHomework.php?SId=";
    public static String homework_by_subject_API          = root_domain_url + "getHomeworkForSubject.php?";
    public static String homework_desc_API                = root_domain_url + "getHomeworkForSubjectDetails.php?Homework_Id=";

    //Timetable API
    public static String timetable_API                    = root_domain_url + "getTimeTable.php?SectionId=";

    //Exams API
    public static String exam_list_API                    = root_domain_url + "getClassExams.php?ClassId=";

    //Exam Result API
    public static String exam_result_API                  = root_domain_url + "getExamResult.php?";

    //Holiday API
    public static String holiday_API                      = root_domain_url + "getHolidayList.php?";

}
