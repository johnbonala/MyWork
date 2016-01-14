Feature: Regular payment Investment details
	AS a user
	I want to see details of the investment
	So that I can see where my money is going

	Background:
		Given I am on the Regular payment Investment details screen

	Scenario: Verify links on the Regular payment Investment details
		When I open the Direct Debit Guarantee link
		Then I should be taken to the correct view for that link

	Scenario: Verify back button on Investment details
		When I press the back button in the header bar
		Then I should be taken to the Regular Payments screen

	Scenario: Tap Change investment instructions button
		When I tap the 'Change investment instructions' button
		Then I should see a call to action

	Scenario: Act on Call to action
		When I tap the 'Change investment instructions' button
		And I see a call to action
		When I Press Call
		Then I should be presented with the Android dialler

	Scenario: Cancel Call to action
		When I tap the 'Change investment instructions' button
		And I see a call to action
		When I Press Cancel within the Call to action
		Then I should be presented with the Regular payment Investment details screen

	Scenario: Verify card details of user
		Then I should see my Debit Card details

	Scenario: Verify Fund break down
		Then I should a break down of how my funds will be spread
