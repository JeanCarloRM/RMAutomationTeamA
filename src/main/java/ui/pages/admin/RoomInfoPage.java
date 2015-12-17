package ui.pages.admin;

import entities.RoomEntity;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created by ArielWagner on 09/12/2015.
 */
public class RoomInfoPage extends RoomMenuPage {

    @FindBy(xpath = "//button[contains(@ng-click, 'enableDisableRoom')]")
    WebElement enableDisableRoomButton;

    @FindBy(xpath = "//input[contains(@ng-model, 'DisplayName')]")
    WebElement displayNameInput;

    @FindBy(xpath = "//input[contains(@ng-model, 'code')]")
    WebElement codeInput;

    @FindBy(xpath = "//input[contains(@ng-model, 'capacity')]")
    WebElement capacityInput;

    /**
     * This method is the constructor
     */
    public RoomInfoPage() {
        waitUntilPageObjectIsLoaded();
    }

    @Override
    public void waitUntilPageObjectIsLoaded() {
        wait.until(ExpectedConditions.visibilityOf(enableDisableRoomButton));
    }

    /**
     * This method allows edit the display name
     * @param name
     */
    public void editDisplayName(String name)
    {
        displayNameInput.clear();
        displayNameInput.sendKeys(name);
    }

    /**
     * This method allows edit the code
     * @param code
     */
    public void editCode(String code){
        codeInput.clear();
        codeInput.sendKeys(code);
    }

    /**
     * This method allows edit the capacity
     * @param capacity
     */
    public void editCapacity(String capacity){
        capacityInput.clear();
        capacityInput.sendKeys(capacity);
    }

    /**
     * This method allows edit the room data
     * @param roomEntity
     */
    public void editRoom(RoomEntity roomEntity) {
        editDisplayName(roomEntity.getDisplayName());
        editCode(roomEntity.getCode());
        editCapacity(roomEntity.getCapacity());
    }

    /**
     * This method allows get the displayName
     * @return a String
     */
    public String getDisplayName() {
        return displayNameInput.getAttribute("value");
    }

    /**
     * This method allows get the code
     * @return a String
     */
    public String getCode() {
        return codeInput.getAttribute("value");
    }

    /**
     * This method allows get the capacity
     * @return a String
     */
    public  String getCapacity() {
        return capacityInput.getAttribute("value");
    }
}
