Feature: Unexpected Server responses
	As a Standard Life Customer
	I want to be able to see error messages at unexpected times
	In order to be properly informed when the service is unavailable

  Scenario: Emergency Server error
    Given I have launched the app
  	When the server returns 'emegency error'
    Then I should  see the 'app unavailable' error message

  Scenario: App is unavailable between 12am and 7am 
  	Given I have launched the app
  	When the server returns 'out of hours'
  	Then I should see the 'app unavailable' error message

  Scenario: No data connection
  	Given I have launched the app
  	When the server returns nothing
  	Then I should see the 'app unavailable' error message