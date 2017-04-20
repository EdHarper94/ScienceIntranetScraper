package egwh.scienceintranetscraper.Timetable;

/**
 * Created by eghar on 21/02/2017.
 *
 * Lecture - Lecture class describing a lecture object.
 */

public class Lecture {
    private String moduleCode;
    private String lecturer;
    private String room;
    private int day;
    private int hour;
    private int duration;

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

    public void setLecturer(String lecturer){
        this.lecturer = lecturer;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setDay(int day){
        this.day = day;
    }

    public void setHour(int hour){
        this.hour = hour;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public String toString(){
        return "LECTURE INFO:  " + getModuleCode() + " Lecturer: " + getLecturer() + " Room: " + getRoom() + " Day: " + getDay() + " Hour: " + getHour() + " Duration: " + getDuration();
    }
}
