Feature: Enter payment details
	As a User
	I want to be able to enter the payment details
	In order to attempt to top up my fund

Background:
	Given I am on the Enter payment screen

	Scenario: Verify empty details error messages
		When I select the Next button
		Then I should see the following error messages:
			| Enter card holder name 			|
			| Enter a valide card number 	|
			| Enter a 'Valid from' date 	|
			| Enter a 'Expired end' date	|
			| Enter a valid CV2 					|
			| enter a valid phone number 	|

	Scenario: From date must be in the past
		When I submit the 'Valid From' date in current month
		Then I should not see the following error message:
			| Enter a 'Valid from' date 	|
		When I submit the 'Valid From' date in a previous month
		Then I should not see the following error message:
			| Enter a 'Valid from' date 	|

	Scenario: Expired date must be in the future
		When I submit the 'Expires end' date in current month
		Then I should not see the following error message:
			| Enter a 'Valid from' date 	|
		When I submit the 'Valid From' date in the next month
		Then I should not see the following error message:
			| Enter a 'Valid from' date 	|

	Scenario: Card number must be 16 digits long
		When I submit a Credit card number 16 digits long
		Then I should not see the following error message:
			| Enter a 'Valid from' date 	|
		When I submit a Credit card number 15 digits long
		Then I should see the following error message:
			| Enter a 'Valid from' date 	|

	Scenario: CV2 must be either 3 or 4 digits	
		When I submit a valid CV2 number
		Then I should not see the following error message:
			| Enter a valid CV2 					|
		When I submit a short CV2 number
		Then I should see the following error message:
			| Enter a valid CV2 					|

	Scenario: Phone number must be at least 11 digits
		When I submit a valid phone number
		Then I should not see the following error message:
			| Enter a valid phone number 	|
		When I submit a short phone number
		Then I should see the following error message:
			| Enter a valid phone number 	|			

	Scenario: Submitting when all details are valid should show the confirm payment screen
		When I submit all the correct details on the Enter Payment Details screen
		Then I should be presented with the Confirm payment screen

	Scenario: Back button returns me to the top up payment screen
		When I press the back button in the header bar
		Then I should be taken to the top up payment screen
	
	Scenario: Pressing the Android back button
		When I press the Android back button
		Then I should be taken to the top up payment screen




