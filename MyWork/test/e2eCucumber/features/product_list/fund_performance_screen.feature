Feature: Fund performance
	As a user
	I want to see my fund performance
	So that I can evaluate its performance

Background:
	Given I am on the Fund performance page for a Investment

	Scenario: Navigate back to the Product Product details
		When I press the back button in the header bar
		Then I should be taken to the Products details screen

	Scenario Outline: Graph displaying Fund performance
		Then I should see a graph displaying:
			| Fund name 						|
			| Dates on x axis 			|
			| % movement on y axis 	|

	Scenario: Tapping on a point on the graph
		When I tap on a point on the graph
		Then I should see details of the point I have clicked

	Scenario Outline: change Graph via date buttons
		When I select the <date> underneath the graph
		Then I should see the graph change to represent <date>

		Examples:
			| 3 months 	|
			| 1 year 		|
			| 3 years		|
			| 5 years 	|

	Scenario: Performance summary text
		Then I should see the Performance summary underneath the graph

	Scenario: Opening About fund performance
		When I open the About performance link
		Then I should be taken to the About fund performance screen

	Scenario: Opening Fund factsheet
		When I open the About performance link
		Then I should be taken to the fund factsheet screen

	Scenario: see total fund changes (%)
		Then I should see the total fund changes section
		#FMC
		#Additional expenses
		#Total annual charge

	Scenario: open important info
		When I open the important information
		Then I should be presented with the important info page