package ui.pages.admin;

import entities.OutOfOrderEntity;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created by ArielWagner on 09/12/2015.
 */
public class OutOfOrderPlanningPage extends RoomMenuPage {

    @FindBy(xpath = "//label[@for = 'active-button']")
    WebElement calendarButton;

    @FindBy(xpath = "//label[contains(@for, 'dropdown')]")
    WebElement outOfOrderDropDown;

    @FindBy(xpath = "//input[@ng-model = 'hours']")
    WebElement hourStartInput;

    @FindBy(xpath = "//div[2][@class = 'col-md-6']//input[@ng-model = 'hours']")
    WebElement hourEndInput;

    @FindBy(xpath = "//a[@ng-click = 'incrementHours()']")
    WebElement incrementHourStartButton;

    @FindBy(xpath = "//div[2][@class = 'col-md-6']//a[@ng-click = 'incrementHours()']")
    WebElement incrementHourEndButton;

    @FindBy(xpath = "//a[@ng-click = 'decrementHours()']")
    WebElement decrementHourStartButton;

    @FindBy(xpath = "//div[2][@class = 'col-md-6']//a[@ng-click = 'decrementHours()']")
    WebElement decrementHourEndButton;

    @FindBy(xpath = "//button[@ng-click = 'toggleMeridian()']")
    WebElement stateMeridianStartButton;

    @FindBy(xpath = "//div[2][@class = 'col-md-6']//button[@ng-click = 'toggleMeridian()']")
    WebElement stateMeridianEndButton;

    By errorDisplayedLabel = By.xpath("//small[contains(text(), 'field must be greater than')]");

    By outOfOrderOption;

    /**
     * This method is the constructor
     */
    public OutOfOrderPlanningPage() {
        waitUntilPageObjectIsLoaded();
    }

    @Override
    public void waitUntilPageObjectIsLoaded() {
        wait.until(ExpectedConditions.visibilityOf(calendarButton));
    }

    /**
     * This method allows configure out of order a room
     * @param outOfOrderEntity
     * @return the ConferenceRooms page
     */
    public ConferenceRoomsPage configureOutOfOrder(OutOfOrderEntity outOfOrderEntity) {
        outOfOrderDropDown.click();
        outOfOrderOption = By.xpath("//ul//a[contains(text(), '"+ outOfOrderEntity.getTitle() +"')]");
        driver.findElement(outOfOrderOption).click();
        int hourActualStart = Integer.parseInt(getHourActualStart());
        int hourExpectStart = Integer.parseInt(outOfOrderEntity.getStartHour());
        String meridianStateStart = getStateMeridianStart();
        int hourActualEnd = Integer.parseInt(getHourActualEnd());
        int hourExpectEnd = Integer.parseInt(outOfOrderEntity.getEndHour());
        String meridianStateEnd = getStateMeridianEnd();
        placeHour(outOfOrderEntity, hourActualStart, hourExpectStart, meridianStateStart, stateMeridianStartButton, incrementHourStartButton, decrementHourStartButton);
        placeHour(outOfOrderEntity, hourActualEnd, hourExpectEnd, meridianStateEnd,stateMeridianEndButton, incrementHourEndButton, decrementHourEndButton);
        OutOfOrderPlanningPage.super.clickSaveRoom();
        return new ConferenceRoomsPage();
    }

    /**
     * Places an hour specified
     * @param outOfOrderEntity
     * @param hourActual
     * @param hourExpect (Start hour)
     * @param meridianState (PM or AM)
     * @param meridianStateButton
     * @param incrementHourButton
     * @param decrementHourButton
     */
    private void placeHour(OutOfOrderEntity outOfOrderEntity, int hourActual, int hourExpect ,String meridianState, WebElement meridianStateButton, WebElement incrementHourButton, WebElement decrementHourButton) {
        while(hourActual != hourExpect) {
            if(hourExpect > hourActual ) {
                if(!(meridianState.equals(outOfOrderEntity.getMeridian()))) {
                    meridianState = outOfOrderEntity.getMeridian();
                    meridianStateButton.click();
                    incrementHourButton.click();
                    hourActual++;
                }
                else {
                    incrementHourButton.click();
                    hourActual++;
                }
            }
            else {
                if(!(meridianState.equals(outOfOrderEntity.getMeridian()))) {
                    meridianState = outOfOrderEntity.getMeridian();
                    meridianStateButton.click();
                    decrementHourButton.click();
                    hourActual--;
                }
                else {
                    decrementHourButton.click();
                    hourActual--;
                }
            }
        }
        if(!(meridianState.equals(outOfOrderEntity.getMeridian()))) {
            meridianStateButton.click();
        }
    }

    /**
     * This method allows get the actual hour of start
     * @return a String
     */
    public String getHourActualStart() {
        return hourStartInput.getAttribute("value");
    }

    /**
     * This method allows get the actual hour od end
     * @return a String
     */
    public String getHourActualEnd() {
        return hourEndInput.getAttribute("value");
    }

    /**
     * This method allows get the meridian state of start
     * @return a String
     */
    public String getStateMeridianStart() {
        return stateMeridianStartButton.getText();
    }

    /**
     * This method allows get the meridian state of end
     * @return a String
     */
    public String getStateMeridianEnd() {
        return stateMeridianEndButton.getText();
    }

    /**
     * This method allows verify if the out of order error
     * @return false or true
     */
    public boolean errorOutOfOrderIsDisplayed() {
        return isDisplayed(errorDisplayedLabel);
    }
}
