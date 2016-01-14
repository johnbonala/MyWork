Background:
	Given I am on the product details screen

	Scenario: Navigate back to the Product list
		When I press the back button in the header bar
		Then I should be taken to the Products List screen

	Scenario: Select Infomation
		When I select 'Infomation'
		Then I should be taken to the Information screen

	Scenario: Selecting an Investment
		When I select an investment
		Then I should be taken to the fund performance screen for that investment

	Scenario: Additional items present
		Given I have successfully logged in with a user with an ISA
		When I am on the Product Details screen
		Then I should see my ISA in the Additional items section

	Scenario: Open ISA details
		Given I have successfully logged in with a user with an ISA
		When I am on the Product Details screen
		And I select my ISA
		Then I should see the fund performance screen for that investment

	Scenario: Select Recent Payments
		When I select 'Recent Payments'
		Then I should be taken to the Recent Payments screen

	Scenario: Regular payments set up for a user
		Given I am logged in as a user with Regular payments
		When I select 'Regular Payments'
		Then I should be taken to the Regular payments screen

	Scenario: Regular payments not set up for a user
		Given I am logged in as a user without Regular payments
		When I select 'Regular Payments'
		Then I should see the call invitation dialog

	Scenario: Open Top up screen set up
		Given I am logged in as a user without Regular payments
		When I select 'Top up'
		Then I should be taken to the Top Up screen

	Scenario: Open Top up screen not set up
		Given I am logged in as a user without Regular payments
		And on the Product Details screen
		When I select 'Top up'
		Then I should see the call invitation dialog

