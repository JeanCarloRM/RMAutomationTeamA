package entities;

/**
 * Created by ArielWagner on 16/12/2015.
 */
public class OutOfOrderEntity {

    private String title;
    private String startHour;
    private String endHour;
    private String meridian;

    public String getTitle() {
        return title;
    }

    public String getStartHour() {
        return startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getMeridian() {
        return meridian;
    }

    /**
     * Set all the Field in one method
     * @param title
     * @param startHour
     * @param endHour
     * @param meridian (PM or AM)
     */
    public void setAllFields(String title, String startHour, String endHour, String meridian) {
        this.title = title;
        this.startHour = startHour;
        this.endHour = endHour;
        this.meridian = meridian;
    }
}
