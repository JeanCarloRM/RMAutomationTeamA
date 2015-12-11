package ui.pages.tablet;

import entities.MeetingEntity;
import org.apache.log4j.chainsaw.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ui.BasePageObject;

/**
 * Created by MiguelTerceros on 12/10/2015.
 */
public class SchedulePage extends BasePageObject {
    private TopMenuPage topMenuPage;
    private MeetingEntity meetingEntity;
    @FindBy(xpath = "//div[contains(@class,'item-title')]/span")
    WebElement scheduleLabel;
    @FindBy(xpath = "//div[contains(@class,'form-group')]/input[contains(@id,'txtOrganizer')]")
    WebElement organizerInput;
    @FindBy(xpath = "//div[contains(@class,'form-group')]/input[contains(@id,'txtSubject')]")
    WebElement subjectInput;
    @FindBy(xpath = "//label[contains(text(),'From')]/../input[contains(@type,'time')]")
    WebElement fromInput;
    @FindBy(xpath = "//label[contains(text(),'To')]/../input[contains(@type,'time')]")
    WebElement toInput;
    @FindBy(xpath = "//div[contains(@class,'angucomplete-holder')]/input")
    WebElement attendeesInput;
    @FindBy(xpath = "//div[contains(@class,'form-group')]/textarea[contains(@id,'txtBody')]")
    WebElement bodyTextArea;
    @FindBy(xpath = "//div[contains(@class,'form-bar')]/button[contains(@class,'item-btn')]")
    WebElement createButton;

    WebElement meetingLabel;
    WebElement meetingForDelete;
    @FindBy(xpath = "//div[contains(text(),'Meeting successfully created')]")
    WebElement successfullyMessage;
    @FindBy(xpath = "//div[contains(@class,'form-bar')]/button/span[contains(text(),'Remove')]")
    WebElement removeButton;

    /**
     * This method is the constructor
     */
    public SchedulePage() {
        waitUntilPageObjectIsLoaded();
        topMenuPage = new TopMenuPage();
    }

    @Override
    public void waitUntilPageObjectIsLoaded() {
        wait.until(ExpectedConditions.visibilityOf(scheduleLabel));
    }

    /***************** TOP MENU ****************/
    public MainTabletPage goMainPage(){
        return topMenuPage.clickInHome();
    }

    public SchedulePage goSchedulePage(){
        return this;
    }

    public SearchPage goSearchPage(){
        return topMenuPage.clickInSearch();
    }

    /****************** BODY *****************/
    public SchedulePage setOrganizerInput(String organizer){
        organizerInput.clear();
        organizerInput.sendKeys(organizer);
        return this;
    }

    public SchedulePage setSubjectInput(String subject){
        subjectInput.clear();
        subjectInput.sendKeys(subject);
        return this;
    }

    public SchedulePage setFromInput(String from){
        fromInput.clear();
        fromInput.sendKeys(from);
        return this;
    }

    public SchedulePage setToInput(String to){
        toInput.clear();
        toInput.sendKeys(to);
        return this;
    }

    public SchedulePage setAttendeesInput(String attendees){
        attendeesInput.clear();
        attendeesInput.sendKeys(attendees);
        return this;
    }

    public SchedulePage setBodyTextArea(String body){
        bodyTextArea.clear();
        bodyTextArea.sendKeys(body);
        return this;
    }

    public CredentialsPage clickCreateButton(){
        createButton.click();
        return new CredentialsPage();
    }

    public CredentialsPage createNewMeeting(MeetingEntity meetingEntity){
        this.meetingEntity = meetingEntity;
        setOrganizerInput(meetingEntity.getOrganizer());
        setSubjectInput(meetingEntity.getSubject());
        setFromInput(meetingEntity.getFrom());
        setToInput(meetingEntity.getTo());
        setAttendeesInput(meetingEntity.getAttendees());
        setBodyTextArea(meetingEntity.getBody());
        return clickCreateButton();
    }

    public String buildMeetingDisplay(String nameMeeting){
        return "//div[contains(@class,'vis-item-overflow')]/div[contains(@class,'vis-item-content')]/span[contains(text(),'"+nameMeeting+"')]";
    }

    public boolean isMeetingDisplayed(String nameMeeting){
        meetingLabel = driver.findElement(By.xpath(buildMeetingDisplay(nameMeeting)));
        return meetingLabel.isDisplayed();
    }

    public boolean isSuccessfullyMessageDisplayed(){
        return successfullyMessage.isDisplayed();
    }

    public SchedulePage selectMeetingForDelete(String nameMeeting){
        meetingForDelete = driver.findElement(By.xpath(buildMeetingDisplay(nameMeeting)));
        meetingForDelete.click();
        return this;
    }

    public CredentialsPage clickRemoveButton(){
        removeButton.click();
        return new CredentialsPage();
    }

    public CredentialsPage deleteMeeting(String nameMeeting){
        selectMeetingForDelete(nameMeeting);
        return clickRemoveButton();
    }
}