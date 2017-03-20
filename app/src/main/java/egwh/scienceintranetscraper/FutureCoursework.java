package egwh.scienceintranetscraper;

import java.util.Date;

/**
 * Created by eghar on 19/03/2017.
 */

public class FutureCoursework extends Coursework {
    private String lecturer;
    private Date setDate;

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
