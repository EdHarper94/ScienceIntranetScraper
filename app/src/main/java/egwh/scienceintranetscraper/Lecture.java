package egwh.scienceintranetscraper;

/**
 * Created by eghar on 21/02/2017.
 *
 * Lecture - Lecture class describing a lecture object.
 */

public class Lecture {
    private String moduleCode;
    private String lecturer;
    private String room;

    public Lecture(String moduleCode, String lecturer, String room){
        this.moduleCode = moduleCode;
        this.lecturer = lecturer;
        this.room = room;
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

    public void setModuleCode(String moduleCode){
        this. moduleCode = moduleCode;
    }

    public void setLecturer(String lecturer){
        this.lecturer = lecturer;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
