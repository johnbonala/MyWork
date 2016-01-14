@KMOB-301		
Feature: More screen
	As a logged in Standard Life customer
	I want to be able to access the features of the More Menu
	In order to gain access to all the Help pages and Settings available

	Background: Login to the application, and open the More Menu
		Given I have successfully logged in to the app as a Tester
		And I open the More menu

		Scenario: Launch More Menu
			When I select the close button
			Then I should be taken to the Products List page

		Scenario Outline: Verify options
			When I look at the entire More Menu
			Then I should see the following options:
				|	header 	| Help						|
				|	link 		|	FAQ							|
				|	link 		| Terms & Policy 	|
				|	link 		|	About this app 	|
				|	link 		|	Contact us			|
				| header 	| Account 				|
				|	toggle	|	Save user ID		|
				|	link 		|	Log out					|

		Scenario Outline: Verify menu links
			When I open the <link> link
			Then I should be taken to the correct view for that link

			Examples:
				| link 						|
				|	FAQ							|
				| Terms & Policy 	|
				|	About this app 	|
				|	Contact us			|

		Scenario: Verify Save user ID
			When I enable the Save user ID option
			And I log out of the app
			Then the app should remember my User ID

		Scenario: Verify not saving my user ID
			When I disable the Save user ID option
			And I log out of the app
			Then the app should not remember my User ID

		Scenario: Select log out option
			When I attempt to log out of the app
			Then I should be presented options to either Cancel or Log out

		Scenario: Cancel log out
			When I attempt to log out of the app
			And I choose to Cancel
			Then I should be on the Settings page 

		Scenario: Log out of the application
			When I Log out of the application
			Then I should be returned to the application Home screen
			And I should not be able to access any of the apps features