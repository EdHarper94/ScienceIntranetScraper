package egwh.scienceintranetscraper.Coursework;

import java.util.Date;

/**
 * @file CurrentCoursework.java
 * @author Ed Harper
 * @date 19/03/2013
 *
 * Current Coursework object class for holding and maintaining current coursework data
 */

public class CurrentCoursework extends Coursework{
    private String lecturer;
    private Date feedbackDate;

    /**
     * Initialises current coursework object
     * @param lecturer coursework lecturer
     * @param feedbackDate coursework feedback date
     * @param moduleCode coursework module code
     * @param title coursework title
     * @param deadlineDate coursework deadline
     */
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
