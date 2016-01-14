Feature: Increase Regular Payments
	As a user
	I want to increase my current regular payments
	So that I can invest more

	Background:
		Given I am on the Increase Regular Payments screen

		Scenario: Verify Infomation button
			When I select 'Infomation'
			Then I should be taken to the Information screen

		Scenario: Navigate back to the Product Product details
			When I press the Cancel button in the header bar
			Then I should be taken to the Plan Details screen
		
		Scenario: Input has focus when first landing on the page
			Then the New Payment amount field should have focus

		Scenario: View Direct Debit and investment details
			When I select 'View Direct Debit & Investment details'
			Then I should be taken to the Direct Debit & Investment details screen

		Scenario: Payment input field has my current amount as the Placeholder number
			Then I should see my current amount within the Payment input field

		Scenario: Next with an empty amount
			When the New Payment amount field has focus
			And I press the Next button
			Then I should see the 'Please enter a payment amount' error message

		Scenario: Payment amount < 32
			When I enter 31 in the Payment amount field
			And I press the Next button
			Then I should see the 'The minimum payment is £32'

		Scenario: Payment amount > 32 but < than your current payment
			When I enter a number between 32 and my current payment amount
			And I press the Next button
			Then I should see the Call to action for Reduce your Payment

		Scenario: Act on Call to action
			And I submit a number between 32 and my current payment amount
			And see the Call to action for Reduce your Payment
			When I Press Call
			Then I should be presented with the Android dialler

		Scenario: Cancel Call to action
			And I submit a number between 32 and my current payment amount
			And see the Call to action for Reduce your Payment
			When I Press Cancel within the Call to action
			Then I should be presented with the Increase Regular Payments screen

		Scenario: automatically calculated Tax Relief
			When I enter 100 in the Payment amount field
			Then I should see 120.00 as the calculated Tax Releif

		Scenario: Entering an amount > £144,000
			When I enter 144001 in the Payment amount field
			Then I should see the 'The maximum payment is £144,000' error message

		Scenario:	Entering a Valid amount
			When I enter a value between my current payment amount and 140000
			And I press the Next button
			Then I should be taken to the Confirm Payment screen