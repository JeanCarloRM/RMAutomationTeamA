package steps;

import cucumber.api.java.en.Then;
import entities.ResourceEntity;
import org.testng.Assert;
import ui.pages.admin.ConferenceRoomsPage;

/**
 * User: Jean Carlo Rodriguez
 * Date: 12/9/15
 * Time: 10:24 AM
 */
public class ConferenceRoom {
    ResourceEntity resourceEntity;
    ConferenceRoomsPage conferenceRoomsPage;
    public ConferenceRoom(ResourceEntity resource)
    {
        resourceEntity = resource;
        conferenceRoomsPage = new ConferenceRoomsPage();
    }


    @Then("^the Resource should be displayed as a button in the Conference Room page header$")
    public void theResourceShouldBeDisplayAsAButtonInTheConferenceRoomPageHeader()
    {
        boolean expectedResult = true;
        Assert.assertEquals(conferenceRoomsPage.isResourceButtonPresent(resourceEntity),expectedResult);
    }

    @Then("^the Resource should be not displayed as a button in the Conference Room page header$")
    public void theResourceShouldBeNotDisplayAsAButtonInTheConferenceRoomPageHeader()
    {
        boolean expectedResult = false;
        Assert.assertEquals(conferenceRoomsPage.isResourceButtonPresent(resourceEntity),expectedResult);
    }
}
