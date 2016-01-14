Feature: Application Launch
	As a Standard Life Customer
	I want to see a splash screen
	So that I can see the app is opening

	Scenario: App shows Launch screen
		When I have launched the application
		Then I should see the Splash Image displayed
	
	Scenario: App successfully loads
		Given I have launched the application
		And I see the Splach Screen displayed
		When the Splash screen is dissmissed
		Then I should be presented with the Login Screen

	Scenario: App fails to load
		Given I have launched the application
		And I see the Splach Screen displayed
		When the Splash screen is dissmissed
		Then I should be presented with the 'app unavailable' message