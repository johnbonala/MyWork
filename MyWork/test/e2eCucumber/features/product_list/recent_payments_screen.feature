Feature: Products List page
	As a logged in Standard Life customer
	I want to be able to see a list of my recent payments
	In order to check the payments I have made to my ISA's and when

	Background:
		Given I am on the Regular Payments screen

		Scenario: Last 12 months should be visible when I first view the screen
			Then I should see the last 12 months of data

		Scenario: Navigate back to the Product list
			When I press the back button in the header bar
			Then I should be taken to the Products List screen

		Scenario: Switch to current tax year
			When I select 'Current tax year'
			Then I should see the current tax years data

		Scenario: Switch to current tax year, then back to 12 months
			When I select 'Current tax year'
			Then I should see the current tax years data
			When I select 'Last 12 months'
			Then I should see the last 12 months of data

		Scenario: Back button on Password Screen
			When I press the Android back button
			Then I should be presented with the Product Details screen