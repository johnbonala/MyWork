@KMOB-243	@KMOB-244	
	Feature: Password Screen
	As a Standard Life customer
	I want to be able to check that my password is correct
	In order to log in to the app securely

Background:
	Given I am on the Password Screen as a Tester

	Scenario: 3 sparse password position placeholder
		Then I should see 3 text inputs for my password
		And they should all have a different placeholder

	Scenario: First input field should have focus
		Then the 'first' input field should have focus

	Scenario: Login button should be visible when within a password field
		When the <field> has focus
		Then Login button should be visible in an disabled state  

	Scenario: Focus changes as I type each character of the Password
		And the 'first' input field should have focus
		When I enter the 'first' password character
		Then the 'second' input field should have focus
		When I enter the 'second' password character
		Then the 'third' input field should have focus

	Scenario: Dismiss keyboard
		When the 'first' input field should have focus
		And I tap the body of the app
		Then the keyboard should be dismissed

	Scenario: Inactive Login button visible when entering password
		When I enter not all of the characters of my password
		Then I should see the Login button in a disabled state

	Scenario: Enable the Login button when all password characters are entered
		When I enter my 3 password characters
		Then Login button should be visible in an active state

	Scenario: Password characters should be hidden when typed
		When I enter my password
		Then I should see my input displayed as hidden characters 

	Scenario Outline: Verify Forgotten login details link on password screen
		When I open the <link> link
		Then I should be taken to the correct view for that link

		Examples:
			| link 							|
			| Register Here			|
			| Terms of use 			|
			| Forgotten Details	|	
	
	Scenario: Re enter User ID from Password Screen
		When I enable the user ID field
		Then I should be presented with the Login Screen

	Scenario: Back button on Password Screen
		When I press the Android back button
		Then I should be presented with the Login Screen

	Scenario: Invalid Password error message
		When I submit an incorrect password
		Then I should see the 'Incorrect Credentials' error message

	Scenario: Block login after failing to login 'x' times
		When I submit an incorrect password 'x' times
		Then I should see the 'Account disabled' error message

	Scenario: Timeout?