Feature: Top up payment
	As a user
	I want to top up my plan
	So that I can increase its size

	Background:
		Given I am on the Top up payment screen

		Scenario: Verify Infomation button
			When I select 'Infomation'
			Then I should be taken to the Information screen

		Scenario: Navigate back to the Product Product details
			When I press the Cancel button in the header bar
			Then I should be taken to the Plan Details screen
		
		Scenario: Input has focus when first landing on the page
			Then the New Payment amount field should have focus

		Scenario: View investment details
			When I select 'View Investment details'
			Then I should be taken to the Investment details screen

		Scenario: Payment input field is empty
			Then I should see nothing within the Payment input field

		Scenario: Next with an empty amount
			When the New Payment amount field has focus
			And I press the Next button
			Then I should see the 'Please enter a payment amount' error message

		Scenario: automatically calculated Tax Relief
			When I enter 100 in the Payment amount field
			Then I should see 120.00 as the calculated Tax Releif

		Scenario: Entering an amount > £40,000
			When I enter 40001 in the Payment amount field
			Then I should see the 'The maximum payment is £40,000' error message

		Scenario:	Entering a Valid amount
			When I enter a value between my current payment amount and 140000
			And I press the Next button
			Then I should be taken to the Confirm Payment screen

		Scenario Outline: Verify shortcut amount buttons
			When I tap the <amount> button
			Then I should see <amount> in the Payment amount field

			Examples:
				| amount 	|
				| 100 		|
				| 250 		|
				| 500 		|

