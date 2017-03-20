package egwh.scienceintranetscraper;

import java.util.Date;

/**
 * Created by eghar on 17/03/2017.
 */

public abstract class Coursework {
    private String moduleCode;
    private String title;
    private Date deadlineDate;

    public Coursework(String moduleCode, String title, Date deadlineDate){
        this.moduleCode = moduleCode;
        this.title = title;
        this.deadlineDate = deadlineDate;
    }

    public String getModuleCode(){
        return moduleCode;
    }

    public String getTitle(){
        return title;
    }

    public Date getDeadlineDate(){
        return deadlineDate;
    }

    public String toString(){
        return "COURSEWORK INFO:  ModuleCode: " + getModuleCode() + " Title: " + getTitle() + " Deadline: " + getDeadlineDate() + ".";
    }
}
