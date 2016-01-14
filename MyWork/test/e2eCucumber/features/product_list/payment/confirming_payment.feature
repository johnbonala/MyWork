Feature: Confirming payment
	As standard life
	We want to confirm the payment before accepting it
	So that the transaction has been completed before confirming to the user

	Scenario: Failed payment on Top Up
		Given the user has provided the details to Top Up their fund
		When the payment is rejected
		Then the user should see the Payment failed screen

	Scenario: Successful payment on Top Up
		Given the user has provided the details to Top Up their fund
		When the payment is accepted
		Then the user should see the Payment Success screen

	Scenario: Failed payment on Regular Payment
		Given the user has provided the details to adjust their regular payment
		When the payment is rejected
		Then the user should see the Payment failed screen

	Scenario: Successful payment on Regular Payment
		Given the user has provided the details to adjust their regular payment
		When the payment is accepted
		Then the user should see the Payment failed screen