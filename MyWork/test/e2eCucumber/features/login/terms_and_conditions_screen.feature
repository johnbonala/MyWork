Feature: Terms and Conditions
As Standard Life
I want the user to agree to the terms and conditions
So that SL are protected and the user is informed

Scenario: Existing user taken directly to the product list
	Given I have submitted successfull credentials for a user who has used the app already
	Then I should be presented with the Product List

Background:
	Given I have submitted successfull credentials for a first time user

	Scenario: 
		Then I should be asked weather or not I want the app to Save my User ID

	Scenario: View terms and conditions after enabling saving User ID
		When I allow the app to save my User ID
		Then I should be presented with the Terms and Conditions page

	Scenario: View terms and conditions after not enabling saving User ID
		When I disallow the app to save my User ID
		Then I should be presented with the Terms and Conditions page

	Scenario: Not Saving User ID when logging in to the application
		When I disallow the app to save my User ID
		And I log out of the application
		Then I should not see my User Id in the User Id field

	Scenario: Saving User ID when logging in to the application
		When I allow the app to save my User Id
		And I log out of the application
		Then I should see my User Id in the User Id field

	Scenario: Saving User ID but not acceptings the Terms and Conditions
		When I allow the app to save my User Id
		And I Disagree with the Terms and Conditions
		Then I should see my User Id in the User Id field

	Scenario: Accept the terms and conditions on first login
		When I allow the app to save my User Id
		And I agree with the Terms and Conditions
		Then I should be presented with the Product list

	Scenario: User should not see terms and conditions after already accepting them
		When I accept the terms and conditions
		And Log out of the application
		When I login to the application
		Then I should not see the terms and conditions

			