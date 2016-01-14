var enter_payment_details_steps = function () {

	this.Then(/^I should see the following error messages:$/, function (screen, callback) {
    callback.pending();
  });
	// | Enter card holder name 			|
	// | Enter a valide card number 	|
	// | Enter a 'Valid from' date 	|
	// | Enter a 'Expired end' date	|
	// | Enter a valid CV2 					|
	// | enter a valid phone number 	|

	this.When(/^I submit the 'Valid From' date in current month$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I submit the 'Valid From' date in a previous month$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I submit the 'Expires end' date in current month$/, function (screen, callback) {
    callback.pending();
  });
	
	this.When(/^I submit the 'Valid From' date in the next month$/, function (screen, callback) {
    callback.pending();
  });
		
	this.When(/^I submit a Credit card number 16 digits long$/, function (screen, callback) {
    callback.pending();
  });
	
	this.When(/^I submit a valid CV2 number$/, function (screen, callback) {
    callback.pending();
  });
	
	this.When(/^I submit a short CV2 number$/, function (screen, callback) {
    callback.pending();
  });
	
	this.When(/^I submit all the correct details on the Enter Payment Details screen$/, function (screen, callback) {
    callback.pending();
  });
};

module.exports = enter_payment_details_steps;