package egwh.scienceintranetscraper.StaffOfficeHours;

/**
 * @file StaffHoursScraper.java
 * @author Ed Harper
 * @date 20/04/2017
 *
 * Staff hours object class, holds staff and office hours data
 */

public class StaffHours {

    private String title;
    private String firstName;
    private String lastName;
    private String hours;
    private String lastUpdated;

    public StaffHours(String title, String firstName, String lastName, String hours, String lastUpdated){
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hours = hours;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Gets staff title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets staff firstname
     * @return firstname
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets staff last name
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets staff full name
     * @return getTitle + getFirstName + getLastName
     */
    public String getFullName(){
        return getTitle() + " " + getFirstName() + " " + getLastName();
    }

    /**
     * Gets staff hours
     * @return hours
     */
    public String getHours() {
        return hours;
    }

    /**
     * Gets hours last updated
     * @return lastUpdated
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Puts object variables to string
     * @return
     */
    public String toString(){
        return "Staff Hours: " + getFullName() + ". " + getHours() + ". " +getLastUpdated() + ".";
    }
}
