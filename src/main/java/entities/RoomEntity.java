package entities;

/**
 * Created by ArielWagner on 09/12/2015.
 */
public class RoomEntity {

    private String displayName;
    private String code;
    private String capacity;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCapacity() {
        return capacity;
    }

    /**
     * Set all the Field in one method
     * @param displayName
     * @param code
     * @param capacity
     */
    public void setAllFields(String displayName, String code, String capacity)
    {
        this.displayName = displayName;
        this.code = code;
        this.capacity = capacity;
    }

}
