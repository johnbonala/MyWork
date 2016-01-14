var password_steps = function () {
	this.Then(/^I should see 3 text inputs for my password$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^they should all have a different placeholder$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^the 'first' input field should have focus$/, function (screen, callback) {
    callback.pending();
  });
	
	this.Then(/^Login button should be visible in an disabled state$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^the 'first' input field should have focus$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I enter the 'first' password character$/, function (screen, callback) {
    callback.pending();
  });
	
	this.Then(/^the 'second' input field should have focus$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I enter the 'second' password character$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^the 'third' input field should have focus$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^the 'first' input field should have focus$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I enter not all of the characters of my password$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should see the Login button in a disabled state$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I enter my 3 password characters$/, function (screen, callback) {
    callback.pending();
  });
	
	this.Then(/^Login button should be visible in an active state$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I enter my password$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should see my input displayed as hidden characters$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I enable the user ID field$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I submit an incorrect password$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should see the 'Incorrect Credentials' error message$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I submit an incorrect password 'x' times$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should see the 'Account disabled' error message$/, function (screen, callback) {
    callback.pending();
  });

};

module.exports = password_steps;