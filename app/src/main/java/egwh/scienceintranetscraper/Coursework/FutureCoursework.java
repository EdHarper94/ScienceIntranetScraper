package egwh.scienceintranetscraper.Coursework;

import java.util.Date;

/**
 * @file FutureCoursework.java
 * @author Ed Harper
 * @date 19/03/2017
 *
 * Future Coursework object class for storing and maintaining future coursework data
 */

public class FutureCoursework extends Coursework {
    private String lecturer;
    private Date setDate;

    /**
     * Initialises future coursework object
     * @param lecturer
     * @param setDate
     * @param moduleCode
     * @param title
     * @param deadlineDate
     */
    public FutureCoursework(String lecturer, Date setDate, String moduleCode, String title, Date deadlineDate){
        super(moduleCode, title, deadlineDate);
        this.lecturer = lecturer;
        this.setDate = setDate;
    }

    public String getLecturer(){
        return lecturer;
    }

    public Date getSetDate(){
        return setDate;
    }

    public String toString(){
        return "FUTURE-CW INFO:  ModuleCode: " + getModuleCode() + " Title: " + getTitle() + " Deadline: " + getDeadlineDate() + " Lecturer " + getLecturer() + " SetDate: " + getSetDate() + ".";
    }
}
