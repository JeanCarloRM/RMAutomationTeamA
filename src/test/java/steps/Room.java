package steps;

import api.APIOutOfOrdersMethods;
import api.APIRoomsMethods;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import db.DBRoomsMethods;
import entities.OutOfOrderEntity;
import entities.ResourceEntity;
import entities.RoomEntity;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONObject;
import ui.common.CommonMethods;
import ui.pages.admin.ConferenceRoomsPage;
import ui.pages.admin.MainAdminPage;
import ui.pages.admin.RoomAssociationResourcePage;
import ui.pages.admin.RoomInfoPage;
import ui.pages.admin.OutOfOrderPlanningPage;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ArielWagner on 09/12/2015.
 */
public class Room {

    MainAdminPage mainAdminPage;
    ConferenceRoomsPage conferenceRoomsPage;
    RoomInfoPage roomInfoPage;
    RoomAssociationResourcePage roomAssociationResourcePage;
    OutOfOrderPlanningPage outOfOrderPlanningPage;
    RoomEntity roomEntity;
    ResourceEntity resourceEntity;
    DBRoomsMethods dbRoomsMethods;
    private static String quantity;
    APIRoomsMethods apiRoomsMethods;
    APIOutOfOrdersMethods apiOutOfOrdersMethods;
    RoomEntity originalRoomEntity = new RoomEntity();
    OutOfOrderEntity outOfOrderEntity = new OutOfOrderEntity();

    public Room(MainAdminPage mainAdminPage, RoomEntity roomEntity, ResourceEntity resourceEntity) {
        this.mainAdminPage = mainAdminPage;
        this.roomEntity = roomEntity;
        this.resourceEntity = resourceEntity;
    }

    @When("^I open \"([^\"]*)\" Room for edit$")
    public void iOpenRoomForEdit(String displayName) {
        conferenceRoomsPage = new ConferenceRoomsPage();
        roomInfoPage = conferenceRoomsPage.selectRoom(displayName);
        roomEntity.setDisplayName(displayName);
        originalRoomEntity.setDisplayName(displayName);
        originalRoomEntity.setCode(roomInfoPage.getCode());
        originalRoomEntity.setCapacity(roomInfoPage.getCapacity());
    }

    @And("^I edit the displayName \"([^\"]*)\" ,code \"([^\"]*)\" and capacity \"([^\"]*)\"$")
    public void editDataRoom(String displayName, String code, String capacity) {
        roomEntity.setAllFields(displayName, code, capacity);
        roomInfoPage.editRoom(roomEntity);
        conferenceRoomsPage = roomInfoPage.clickSaveRoom();
    }

    @Then("^a information message should be displayed$")
    public void informationMessageDisplayed() {
        Assert.assertEquals(conferenceRoomsPage.getRoomModifiedMessagePopUp(), "Room successfully Modified", "information message displayed");
    }

    @And("^the Room data should be the edited$")
    public void roomDataShouldBeTheEdited() {
        roomInfoPage = conferenceRoomsPage.selectRoom(roomEntity.getDisplayName());
        String actualDisplayName = roomInfoPage.getDisplayName();
        String actualCode = roomInfoPage.getCode();
        String actualCapacity = roomInfoPage.getCapacity();
        Assert.assertEquals(actualDisplayName, roomEntity.getDisplayName(), "comparing displayName by UI");
        Assert.assertEquals(actualCode, roomEntity.getCode(), "comparing code by UI");
        Assert.assertEquals(actualCapacity, roomEntity.getCapacity(), "comparing capacity by UI");
        conferenceRoomsPage = roomInfoPage.clickSaveRoom();
    }

    @And("^the Room edited should be obtained using the API$")
    public void theRoomEditedShouldBeObtainedUsingTheAPI() {
        apiRoomsMethods = new APIRoomsMethods();
        JSONObject jsonObject = apiRoomsMethods.getJson(roomEntity.getDisplayName());
        String actualDisplayName = jsonObject.getString("customDisplayName");
        String actualCode = jsonObject.getString("code");
        String actualCapacity = String.valueOf(jsonObject.getInt("capacity"));
        Assert.assertEquals(actualDisplayName, roomEntity.getDisplayName(), "comparing displayName by API");
        Assert.assertEquals(actualCode, roomEntity.getCode(), "comparing code by API");
        Assert.assertEquals(actualCapacity, roomEntity.getCapacity(), "comparing capacity by API");
    }


    @When("^I search a Room by \"([^\"]*)\"$")
    public void iSearchARoomBy(String criteria) {
        conferenceRoomsPage.setFilterByRoom(criteria);
    }

    @Then("^the Room or Rooms according to \"([^\"]*)\" should be listed$")
    public void theRoomOrRoomsShouldBeListed(String criteria) {
        dbRoomsMethods = new DBRoomsMethods();
        ArrayList<String> roomDisplayNames = dbRoomsMethods.filterRoomsByCriteria(criteria);
        ArrayList<String> roomsList = conferenceRoomsPage.getRoomsContainer();
        Collections.sort(roomDisplayNames);
        Collections.sort(roomsList);
        int position = 0;
        for(String roomDisplayName : roomDisplayNames) {
            Assert.assertEquals(roomsList.get(position), roomDisplayName, "comparing displayName by UI");
            position++;
        }
    }

    @When("^I go to Out of Order Planning Tab$")
    public void goToOutOfOrderPlanningTab() {
        outOfOrderPlanningPage = roomInfoPage.goToOutOfOrderPlanning();
    }

    @And("^I configure the Room with the option out of order \"([^\"]*)\" at time \"([^\"]*)\" to \"([^\"]*)\" - \"([^\"]*)\"$")
    public void configureTheRoomWithTheOptionOutOfOrderAtTimeTo(String outOfOrder, String hourStart, String hourEnd, String meridian) {
        outOfOrderEntity.setAllFields(outOfOrder, hourStart, hourEnd, meridian);
        conferenceRoomsPage = outOfOrderPlanningPage.configureOutOfOrder(outOfOrderEntity);
    }

    @And("^should display an icon on the Out Of Order column$")
    public void displayIconOnTheOutOfOrderColumn() {
        Assert.assertTrue(conferenceRoomsPage.isPresentOutOfOrderIcon(), "out of order icon is displayed");
    }

    @And("^the Out Of Order state should be obtained using the API$")
    public void theOutOfOrderStateShouldBeObtainedUsingTheAPI() {
        apiOutOfOrdersMethods = new APIOutOfOrdersMethods();
        JSONObject jsonObject = apiOutOfOrdersMethods.getJson(outOfOrderEntity.getTitle());
        String actualTitle = jsonObject.getString("title");
        Assert.assertEquals(actualTitle, outOfOrderEntity.getTitle(), "comparing out of order title by API");
    }

    @Then("^a error message should be displayed$")
    public void aErrorMessageShouldBeDisplayed() {
        Assert.assertTrue(outOfOrderPlanningPage.errorOutOfOrderIsDisplayed(), "error message displayed");
        conferenceRoomsPage = outOfOrderPlanningPage.clickCancelButton();
    }

    @And("^the Out Of Order state should not be obtained using the API$")
    public void theOutOfOrderStateShouldNotBeObtainedUsingTheAPI() {
        apiOutOfOrdersMethods = new APIOutOfOrdersMethods();
        JSONObject jsonObject = apiOutOfOrdersMethods.getJson(outOfOrderEntity.getTitle());
        String code = jsonObject.getString("code");
        Assert.assertEquals(code, "NotFoundError", "out of order is not obtained");
    }

    @And("^I select the Resource in the Conference Room page header$")
    public void iSelectTheResourceInTheConferenceRoomPageHeader()
    {
        conferenceRoomsPage = new ConferenceRoomsPage();
        conferenceRoomsPage.clickOnResourceButton(resourceEntity);
    }

    @And("^I go to Resource Association tab$")
    public void iGoToResourceAssociationTab()
    {
        roomAssociationResourcePage = roomInfoPage.goToResourceAssociations();
    }

    @And("^I associate the Resource with quantity \"([^\"]*)\"$")
    public void iAssociateTheResourceWithQuantity(String quantity)
    {
        this.quantity = quantity;
        roomAssociationResourcePage.associateResource(resourceEntity,quantity);
        roomAssociationResourcePage.clickSaveRoom();
    }
    // Todo
    @Then("^the Resource should be displayed with the assigned quantity in the list$")
    public void theResourceShouldBeDisplayedWithTheAssignedQuantityInTheList(){
        String IconAndName[] = conferenceRoomsPage.getIconAndQuantityFromResourceAssociated(roomEntity, resourceEntity);
        String actualIcon = IconAndName[0];
        String actualQuantity = IconAndName[1];
        String expectedIcon = "fa "+resourceEntity.getIconName();
        String expectedQuantity = "x "+quantity;

        Assert.assertEquals(actualIcon,expectedIcon);
        Assert.assertEquals(actualQuantity,expectedQuantity);
    }

    @Then("^the Resource should be not associated to the Room in the list$")
    public void theResourceShouldBeNotAssociatedToTheRoomInTheList(){
        boolean actualResult = conferenceRoomsPage.isResourceAssignedToTheRoom(roomEntity,resourceEntity);
        Assert.assertEquals(actualResult,false);
    }

    @Given("^I associate the Resource to the Room \"([^\"]*)\" with quantity \"([^\"]*)\"$")
    public void iAssociateTheResourceToTheRoomWithQuantity(String roomName,String quantity){
        roomEntity.setDisplayName(roomName);
        iSelectTheResourceInTheConferenceRoomPageHeader();
        iSearchARoomBy(roomName);
        iOpenRoomForEdit(roomName);
        iGoToResourceAssociationTab();
        iAssociateTheResourceWithQuantity(quantity);
    }

    @When("^I remove the association with the Resource$")
    public void iRemoveTheAssociationWithTheResource(){
        roomAssociationResourcePage.removeTheAssociatedResource(resourceEntity);
        roomAssociationResourcePage.clickSaveRoom();
    }

    @After("@Room")
    public void goBeforeDataRoom(){
        apiRoomsMethods = new APIRoomsMethods();
        apiRoomsMethods.putRoom(roomEntity, originalRoomEntity);
        CommonMethods.refresh();
        mainAdminPage = new MainAdminPage();
        mainAdminPage.getLeftMenuPage().goToResources();
    }

    @Before("@OutOfOrder, @OutOfOrderError")
    public void goToOtherPage(){
        mainAdminPage = new MainAdminPage();
        mainAdminPage.getLeftMenuPage().goToResources();
    }

    @After("@OutOfOrder")
    public void deleteOutOfOrder() {
        apiOutOfOrdersMethods = new APIOutOfOrdersMethods();
        apiOutOfOrdersMethods.deleteOutOfOrder(originalRoomEntity, outOfOrderEntity);
    }
}
