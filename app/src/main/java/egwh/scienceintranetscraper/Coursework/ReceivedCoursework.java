package egwh.scienceintranetscraper.Coursework;

import java.util.Date;

/**
 * @file ReceivedCoursework.java
 * @author Ed Harper
 * @date 20/04/2017
 * Receieved Coursework object class for storing and maintaining recevied coursework data
 */
public class ReceivedCoursework extends Coursework {

    // Enumeration Received
    public enum Received{
        ON_TIME,LATE,NO
    }

    private Received received;
    private Date feedbackDate;

    /**
     * Initialises received coursework object
     * @param received see enum Received
     * @param feedbackDate coursework feedback date
     * @param moduleCode coursework module code
     * @param title coursework title
     * @param deadlineDate coursework deadline date
     */
    public ReceivedCoursework(Received received, Date feedbackDate, String moduleCode, String title, Date deadlineDate){
        super(moduleCode, title, deadlineDate);
        this.received = received;
        this.feedbackDate = feedbackDate;
    }

    /**
     * Gets received based on enum
     * @return
     */
    public String getReceived(){
        String rec;
        if (received == Received.ON_TIME){
            rec = "On Time";
        }else if(received == Received.LATE){
            rec = "Late";
        }else if(received == Received.NO){
            rec = "NO";
        }else{
            rec = "N/A";
        }
        return rec;
    }

    public Date getFeedbackDate(){
        return feedbackDate;
    }

    @Override
    public String toString(){
        return "RECEIVED-CW INFO:  ModuleCode: " + getModuleCode() + " Title: " + getTitle() + " Deadline: " + getDeadlineDate() + " Received: " + getReceived() + " Feedback: " + getFeedbackDate() + ".";
    }
}
