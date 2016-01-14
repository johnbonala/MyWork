@KMOB-243	@KMOB-244
	Feature: Login Screen
	As a Standard Life customer
	I want to be able to login to the application
	In order to check ISA Products mapped to the User

Background:
	Given I am on the Home Screen

	Scenario: Back button should dismiss the application
		When I press the Android back button
		Then I should see the application dismissed

	Scenario Outline: Verify links on the Home Screen		
		When I open the <link> link
		Then I should be taken to the correct view for that link

		Examples:
			| link 							|
			| Register Here			|
			| Terms of use 			|
			| Forgotten Details	|	
	
	Scenario: Next button is visible when the User ID field has focus
		When I focus on the User ID field
		Then I should see the Next button in a disabled state

	Scenario: User Id too short
		When I enter a User ID less than 6 characters
		Then I should see the Next button in a disabled state

	@login
	Scenario: Next button visible when entering User ID
		When I enter a valid User Id
		Then I should see the Next button become active to attempt to Login

	Scenario: Moving to the Password screen
		When I submit a valid User Id
		Then I should be presented with the Password screen

	Scenario: Successful login
		When I enter the correct credentials
		And accept the Terms and Conditions
		Then I should be presented with the Product page

	Scenario: TEST Successful login
		When I open the "Register here" link

