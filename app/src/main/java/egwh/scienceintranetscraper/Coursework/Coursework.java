package egwh.scienceintranetscraper.Coursework;

import java.util.Date;

/**
 * @file Coursework.java
 * @author Ed Harper
 * @date 17/03/2017
 *
 * Coursework object class for storing and maintaining coursework data
 */

public abstract class Coursework {

    private String moduleCode;
    private String title;
    private Date deadlineDate;

    /**
     * Initialises coursework object
     * @param moduleCode the module code
     * @param title the module title
     * @param deadlineDate the deadline date
     */
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
