@KMOB-246
Feature: Products List page
	As a logged in Standard Life customer
	I want to be able to see all of my products
	In order to check isa and Products mapped to the User

	Scenario: User already with an ISA
		Given I have successfully logged in with a user with an ISA
		When I am on the Product List screen
		Then I should see my ISA available in the list

	Background:
		Given I have successfully logged in to the app as a Tester

		Scenario: Open More menu
			When I choose to view the More Menu
			Then I should be presented with the More Menu

		Scenario: Verify all available ISA's are visible
			When I am on the Product List screen
			Then I should be presented with all my available Products

		Scenario: Open all available Prodcuts
			When I open each of my available plans
			Then I should be presented with the correct data for that plan

		Scenario: S&S ISA advert advert available
			When I am on the Products List
			Then I should be presented with S&S ISA advert
			When I open the S&S ISA advert
			Then I should see the S&S ISA page

		Scenario: Stock and Shares days
			When I am on the Product List
			Then I Should see the correct remaining days left to apply for an ISA

		Scenario: Pressing the android back button
			When I press the Android back button
			Then I should be presented options to either Cancel or Log out

		Scenario: App auto logout time?
		Scenario: User with no plans?
