@Meetings
Feature: Meetings
  Background:
    Given I'm logged in the tablet page
      And I select the room "Floor1Room28"

  Scenario Outline: Create a meeting
    Given I navigate to Available section
    When I create a Meeting with the following information: "<Organizer>", "<Subject>", "<From>", "<To>", "<Attendees>", "<Body>"
    Then an information message should be displayed "Meeting successfully created"
    And the Meeting should be displayed in the Schedule bar
    And the Meeting information should be displayed in the Next section of Main page
    And the Meeting should be listed in the Meetings of Room using the API
    Examples:
      | Organizer   | Subject | From  | To    | Attendees           | Body      |
      | Ariel.Rojas | meeting | 17:00 | 17:30 | test1@forest1.local | Important |
      | miguel      |meeting 2| 17:05 | 17:25 |                     |           |
      | JeanCarlo   | .-.-./  | 17:10 | 17:40 | test1@forest1.local |           |

  Scenario: Try to create a meeting at the same time than other meeting
    Given I navigate to Available section
      And I have a Meeting with the following information: "organizer", "meeting API", "08:20", "11:00", "test1@forest1.local", "Important for API"
    When I create other Meeting with the following information: "Carla Flores", "meeting UI", "08:20", "11:00", "test1@forest1.local", "Important for UI"
    Then an error message should be displayed
      And the second Meeting should not be displayed in the Schedule bar
      And the second Meeting information should not be displayed in the Next section
      And the second Meeting should not be listed in the Meetings of Room using the API

  @removeMeeting
  Scenario: Remove a meeting
    Given I navigate to Available section
      And I have a Meeting with the following information: "organizer", "meeting API", "15:00", "16:00", "test1@forest1.local", "Important"
    When I remove the Meeting
    Then an information message should be displayed "Meeting successfully removed"
      And the meeting should not be displayed in the Schedule bar
      And the meeting information should not be displayed in the Next section of Main page
      And the Meeting should not be listed in the meetings of Room using the API

  Scenario: Update a meeting
    Given I navigate to Available section
      And I have a Meeting with the following information: "JeanCarlo.Rodriguez", "meeting API", "16:00", "17:00", "test1@forest1.local", "this meeting is update"
    When I update the meeting information: "Miguel.Terceros", "meeting UI update", "16:10", "17:30", "test1@forest1.local", "meeting updated"
    Then an information message should be displayed "Meeting successfully updated"
      And the Meeting should be displayed in the Schedule bar
      And the Meeting information should be displayed in the Next section of Main page
      And the Meeting should be listed in the Meetings of Room using the API

  Scenario Outline: Try to create a meeting with missing information
    Given I navigate to Available section
    When I create unsuccessfully a meeting with the following information: "<Organizer>", "<Subject>", "<From>", "<To>", "<Attendees>", "<Body>"
    Then an error "<Error>" message should be displayed
    And the meeting should not be displayed in the Schedule bar
    And the meeting information should not be displayed in the Next section of Main page
    And the Meeting should not be listed in the meetings of Room using the API

    Examples:
      |Organizer |Subject  |From |To   |Attendees           |Body       |Error                   |
      |          |meeting  |14:00|14:30|test1@forest1.local |Be on Time |Organizer is required   |
      |JeanCarlo |         |15:00|15:30|                    |Bring Paper|Subject is required     |
      |Ariel     |planning |16:00|15:30|                    |Bring Paper|Start time must be smaller than end time|
