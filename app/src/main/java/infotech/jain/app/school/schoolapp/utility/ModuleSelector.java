package infotech.jain.app.school.schoolapp.utility;

/**
 * Created by admin on 01/07/16.
 */
public class ModuleSelector
{


    public String getModuleNameByModuleId(int id)
    {
        String module_name = "";

        switch(id)
        {
            case 1:
                module_name = "AttendanceModule";
                break;
            case 2:
                module_name = "CircularsModule";
                break;
            case 3:
                module_name = "HomeworkModule";
                break;
            case 4:
                module_name = "LeaveModule";
                break;
            case 5:
                module_name = "TimetableModule";
                break;
            case 6:
                module_name = "CalenderModule";
                break;
            case 7:
                module_name = "ComplainModule";
                break;
            case 8:
                module_name = "SyllabusModule";
                break;
            case 9:
                module_name = "FeeModule";
                break;
            case 10:
                module_name = "TransportModule";
                break;
            case 11:
                module_name = "ResultModule";
                break;
            case 12:
                module_name = "LibraryModule";
                break;
            case 13:
                module_name = "RemarksModule";
                break;
            case 14:
                module_name = "GalleryModule";
                break;
            case 15:
                module_name = "HolidaysModule";
                break;
            case 16:
                module_name = "AchievementsModule";
                break;
            case 17:
                module_name = "AppointmentModule";
                break;
            default:
                module_name = "";
                break;

        }
        return module_name;
    }

}
