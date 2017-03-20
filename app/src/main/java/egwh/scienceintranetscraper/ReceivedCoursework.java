package egwh.scienceintranetscraper;

import java.util.Date;

/**
 * Created by eghar on 19/03/2017.
 */

public class ReceivedCoursework extends Coursework {
    public enum Received{
        ON_TIME,LATE,NO
    }

    private Received received;
    private Date feedbackDate;

    public ReceivedCoursework(Received received, Date feedbackDate, String moduleCode, String title, Date deadlineDate){
        super(moduleCode, title, deadlineDate);
        this.received = received;
        this.feedbackDate = feedbackDate;
    }

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
