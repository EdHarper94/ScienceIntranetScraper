package egwh.scienceintranetscraper;

import java.util.Date;

/**
 * Created by eghar on 19/03/2017.
 */

public class CurrentCoursework extends Coursework{
    private String lecturer;
    private Date feedbackDate;

    public CurrentCoursework(String lecturer, Date feedbackDate, String moduleCode, String title, Date deadlineDate){
        super(moduleCode, title, deadlineDate);
        this.lecturer = lecturer;
        this.feedbackDate = feedbackDate;
    }

    public String getLecturer(){
        return lecturer;
    }

    public Date getFeedbackDate(){
        return feedbackDate;
    }

    @Override
    public String toString(){
        return "CURRENT-CW INFO:  ModuleCode: " + getModuleCode() + " Lecturer: " + getLecturer() + " Title: " + getTitle() + " Deadline: " + getDeadlineDate() + " Lecturer: " + getLecturer() + " Feedback: " + getFeedbackDate() + ".";
    }
}
