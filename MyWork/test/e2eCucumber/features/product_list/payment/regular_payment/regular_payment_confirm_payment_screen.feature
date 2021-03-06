Feature: Confirm payment
	As a user
	I want to confirm my new payment amount
	So that I dont inadvertently make a mistake

	Background:
		Given I am on Confirm Payment screen of my Regular Payment

	Scenario: Verify links on the Regular payment Investment details for a Pension
		Then I should have the following working links within the page:
			| Terms and Conditions	|
			| Keyfacts document 		|
			| Key features document	|

	Scenario: Verify links on the Regular payment Investment details for a ISA
		Then I should have the following working links within the page:
			| Terms and Conditions	|
			| Keyfacts document 		|
			| Key features document	|

	Scenario: Verify back button
		When I press the back button in the header bar
		Then I should be taken to the Regular Payments screen

	Scenario: Verify UI elements
		Then I should see the following Confirm Payment elements on the page:
			| title 								|
			|	Payment amount 				|
			| calculated tax releif |
			| legal summary					|
			| back button						|

	Scenario: Tap the Confirm button
		When I tap the 'Confirm' button
		Then I should be taken to the Verified Payment screen