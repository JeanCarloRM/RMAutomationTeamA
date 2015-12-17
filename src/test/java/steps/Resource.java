package steps;

import api.APIResourcesMethods;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import db.DBResourcesMethods;
import entities.ResourceEntity;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import entities.RoomEntity;
import org.testng.Assert;
import ui.common.CommonMethods;
import ui.pages.admin.MainAdminPage;
import ui.pages.admin.ResourceAssociationPage;
import ui.pages.admin.ResourcesPage;

import java.util.ArrayList;

/**
 * User: Jean Carlo Rodriguez
 * Date: 12/7/15
 * Time: 8:41 PM
 */
public class Resource {
    MainAdminPage mainAdminPage;
    ResourceEntity resourceEntity;
    ResourcesPage resourcesPage;
    RoomEntity roomEntity;
    static String criteria;
    public Resource(MainAdminPage mainAdminPage, ResourceEntity resourceEntity, RoomEntity roomEntity){
        this.mainAdminPage = mainAdminPage;
        resourcesPage = new ResourcesPage();
        this.resourceEntity = resourceEntity;
        this.roomEntity = roomEntity;
    }

    @When("^I create a Resource with values: \"([^\\\"]*)\",\"([^\\\"]*)\",\"([^\\\"]*)\" and \"([^\\\"]*)\"$")
    public void iCreateAResourceWithValues(String name, String displayName, String description, String icon){
        resourceEntity.setAllFields(name, displayName, description, icon);
        resourcesPage.goToAddNewResource()
                .createAResource(resourceEntity);
        //walk around step to avoid the resource issue(at create a resources the list of resource is empty)
        mainAdminPage.getLeftMenuPage().goToLocations();
        mainAdminPage.getLeftMenuPage().goToResources();
    }

    @Given("^I create a Resource for API with values: \"([^\\\"]*)\",\"([^\\\"]*)\",\"([^\\\"]*)\" and \"([^\\\"]*)\"$")
    public void iCreateAResourceForAPIWithValues(String name, String displayName, String description, String icon){
        resourceEntity.setName(name);
        resourceEntity.setDisplayName(displayName);
        resourceEntity.setDescription(description);
        resourceEntity.setIconName(icon);

        APIResourcesMethods apiResourcesMethods = new APIResourcesMethods();
        apiResourcesMethods.createResource(resourceEntity);
        CommonMethods.refresh();
        //walk around step to avoid the resource issue(at create a resources the list of resource is empty)
        mainAdminPage.getLeftMenuPage().goToLocations();
        mainAdminPage.getLeftMenuPage().goToResources();

    }

    @Then("^the Resource is displayed in the list of Resources$")
    public void theResourceIsDisplayedInTheListOfResources(){
        String actualName = resourcesPage.getName(resourceEntity.getName());
        String actualDisplayName = resourcesPage.getDisplayName(resourceEntity.getName());
        String actualIconName = resourcesPage.getIconName(resourceEntity.getName());

        Assert.assertEquals(actualName, resourceEntity.getName(),"Comparing if the name of the Resource is the correct one");
        Assert.assertEquals(actualDisplayName, resourceEntity.getDisplayName(),"Comparing if the Display Name of the Resource is the correct one");
        Assert.assertEquals(actualIconName,"fa "+ resourceEntity.getIconName(),"Comparing if the Icon of the Resource is the correct one");
    }

    @Then("^the Resource is not longer displayed in the Resource list$")
    public void theResourceIsNotLongerDisplayedInTheResourceList(){
        boolean actualResult = resourcesPage.isResourceInTheResourceList(resourceEntity);
        Assert.assertTrue(actualResult,"the resource is in the list of resource?");
    }

    @When("^I remove the Resource$")
    public void iRemoveTheResource(){
        resourcesPage.removeResource(resourceEntity).ClickOnRemoveButton();
        //walk around step to avoid the resource issue(at create a resources the list of resource is empty)
        mainAdminPage.getLeftMenuPage().goToLocations();
        mainAdminPage.getLeftMenuPage().goToResources();
    }

    @When("^I filter the Resources with the criteria \"([^\\\"]*)\"$")
    public void iFilterTheResourcesWithTheCriteria(String byCriteria){
        resourcesPage.setCriteria(byCriteria);
        criteria = byCriteria;

    }

    @Then("^the result of filter should be the same Resources for the UI and the DB$")
    public void theResultOfFilterShouldBeTheSameResourcesFotTheUIAndTheDB(){
        DBResourcesMethods dbResourcesMethods = new DBResourcesMethods();
        ArrayList<String> actualResult = resourcesPage.getActualTheListOfResources();
        ArrayList<String> expectedResult = dbResourcesMethods.filterResourcesByCriteria("name", criteria);

        Assert.assertEqualsNoOrder(actualResult.toArray(),expectedResult.toArray(), "Comparing if the resource obtained from the UI are the same that the DB");

        mainAdminPage.getLeftMenuPage().goToLocations();
    }

    @Then("^the Resource should be displayed with the Room Associated in Resources Association tab$")
    public void theResourceShouldBeDisplayedWithTheRoomAssociatedInResourcesAssociationTab(){
        ResourceAssociationPage resourceAssociationPage = resourcesPage.openResource(resourceEntity)
                .goToAssociationTab();
        boolean actualResult =resourceAssociationPage.isResourceAssociatedWithTheRoom(roomEntity);

        Assert.assertEquals(actualResult,true, "The resource is associated to the room?");
        resourceAssociationPage.clickOnCloseButton();
        //Walk around
        mainAdminPage.getLeftMenuPage().goToLocations();
    }

    @Then("^the Resource should not be displayed with the Room Associated in Resources Association tab$")
    public void theResourceShouldNotBeDisplayedWithTheRoomAssociatedInResourcesAssociationTab(){
        ResourceAssociationPage resourceAssociationPage = resourcesPage.openResource(resourceEntity)
                .goToAssociationTab();
        boolean actualResult =resourceAssociationPage.isResourceAssociatedWithTheRoom(roomEntity);

        Assert.assertEquals(actualResult,false, "the resource shows no association in the Resource Association Tab");
        resourceAssociationPage.clickOnCloseButton();
        //Walk around
        mainAdminPage.getLeftMenuPage().goToLocations();

    }

    @And("^the Resource should be obtained using the API$")
    public void TheResourceShouldBeObtainedUsingTheAPI(){
        APIResourcesMethods apiResourcesMethods = new APIResourcesMethods();
        boolean actualResult = apiResourcesMethods.isResourcePresent(resourceEntity);

        Assert.assertEquals(actualResult, true, "the resource exists in the API?");
    }

    @And("^the resource should not be obtained using the API$")
    public void theResourceShouldNotBeObtainedUsingTheAPI(){
        APIResourcesMethods apiResourcesMethods = new APIResourcesMethods();
        boolean actualResult = apiResourcesMethods.isResourcePresent(resourceEntity);

        Assert.assertEquals(actualResult, false,"the resource do not exists in the API?");
    }

    @After("@createResource")
    public void removeResourceFromAPI(){
        APIResourcesMethods apiResourcesMethods = new APIResourcesMethods();
        apiResourcesMethods.removeResource(resourceEntity);
    }

    @And("^the Resource assigned to the Room should be obtained using the API$")
    public void theResourceAssignedToTheRoomShouldBeObtainedUsingTheAPI(){
        APIResourcesMethods apiResourcesMethods = new APIResourcesMethods();
        boolean actualResult = apiResourcesMethods.isResourceAssociatedToTheRoom(roomEntity,resourceEntity);
        Assert.assertEquals(actualResult,true, "the resources is associated to the room?");

    }

    @And("^no Resource associated to the Room should be obtained using the API$")
    public void noResourceAssociatedToTheRoomShouldBeObtainedUsingTheAPI(){
        APIResourcesMethods apiResourcesMethods = new APIResourcesMethods();
        boolean actualResult = apiResourcesMethods.isResourceAssociatedToTheRoom(roomEntity,resourceEntity);
        Assert.assertEquals(actualResult,false, "the resources is not associated to the room?");
    }
}
