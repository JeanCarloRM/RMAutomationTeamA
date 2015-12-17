@Rooms
Feature: Rooms
  Background:
    Given I'm logged in the admin page
      And I go to Conference Rooms page

  @Room
  Scenario Outline: Edit a Room
    When I open "Floor1Room11" Room for edit
    And I edit the displayName "<displayName>" ,code "<code>" and capacity "<capacity>"
    Then a information message should be displayed
    And the Room data should be the edited
    And the Room edited should be obtained using the API

    Examples:
      | displayName | code | capacity |
      | Room11      | R11  | 100      |
      | RoomNew     | NR   | 300      |


  @Room
  Scenario Outline: Filter the Rooms for name, displayName and code
    Given I open "Floor1Room10" Room for edit
    And I edit the displayName "Room10" ,code "om11" and capacity "400"
    When I search a Room by "<Criteria>"
    Then the Room or Rooms according to "<Criteria>" should be listed
    Examples:
      |   Criteria   |
      | Room10       |
      | Floor1Room10 |
      | om11         |


  @OutOfOrder
  Scenario Outline: Place a room to out of order
    Given I open "Floor1Room15" Room for edit
    When I go to Out of Order Planning Tab
      And I configure the Room with the option out of order "<Out Of Order>" at time "<From>" to "<To>" - "<meridian>"
    Then a information message should be displayed
      And should display an icon on the Out Of Order column
      And the Out Of Order state should be obtained using the API

    Examples:
      | Out Of Order           |  From    | To    | meridian  |
      | Closed for maintenance |  10      | 11    |   PM      |
      | Closed for reparations |  9       | 10    |   PM      |


  @OutOfOrderError
  Scenario Outline: Place a room to out of order with invalid date
    Given I open "Floor1Room100" Room for edit
    When I go to Out of Order Planning Tab
    And I configure the Room with the option out of order "<Out Of Order>" at time "<From>" to "<To>" - "<meridian>"
    Then a error message should be displayed
    And the Out Of Order state should not be obtained using the API

    Examples:
      | Out Of Order                  |  From    | To    | meridian  |
      | Temporarily Out of Order      |  11      | 10    |   PM      |
      | Reserved for a special event  |  5       | 2     |   PM      |


