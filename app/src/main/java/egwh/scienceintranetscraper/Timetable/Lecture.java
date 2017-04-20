package egwh.scienceintranetscraper.Timetable;

/**
 * @file Lecture.java
 * @author Ed harper
 * @date 21/02/2017#
 *
 * Lecture object storing and maintaining lecture data
 */

public class Lecture {
    private String moduleCode;
    private String lecturer;
    private String room;
    private int day;
    private int hour;
    private int duration;

    /**
     * Initialises lecture object
     * @param moduleCode the module code
     * @param lecturer the lecturer for this lecture
     * @param room the room for the lecture
     * @param day the day of the lecture
     * @param hour the hour of the lecture
     * @param duration the duration of the lecture
     */
    public Lecture(String moduleCode, String lecturer, String room, int day, int hour, int duration){
        this.moduleCode = moduleCode;
        this.lecturer = lecturer;
        this.room = room;
        this.day = day;
        this.hour = hour;
        this.duration = duration;
    }

    public String getModuleCode(){
        return moduleCode;
    }

    public String getLecturer(){
        return lecturer;
    }

    public String getRoom(){
        return room;
    }

    public int getDay(){
        return day;
    }

    public int getHour(){
        return hour;
    }

    public int getDuration(){
        return duration;
    }

    public void setModuleCode(String moduleCode){
        this. moduleCode = moduleCode;
    }

    public String toString(){
        return "LECTURE INFO:  " + getModuleCode() + " Lecturer: " + getLecturer() + " Room: " + getRoom() + " Day: " + getDay() + " Hour: " + getHour() + " Duration: " + getDuration();
    }
}
