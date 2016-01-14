var more_screen_steps = function () {
		
	this.Then(/^I open the More menu$/, function (screen, callback) {
    callback.pending();
  });
	
	this.When(/^I look at the entire More Menu$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should see the following options:$/, function (screen, callback) {
    callback.pending();
  });
	// |	header 	| Help						|
	// |	link 		|	FAQ							|
	// |	link 		| Terms & Policy 	|
	// |	link 		|	About this app 	|
	// |	link 		|	Contact us			|
	// | header 	| Account 				|
	// |	toggle	|	Save user ID		|
	// |	link 		|	Log out					|
	
	this.When(/^I enable the Save user ID option$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I log out of the application$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^the app should remember my User ID$/, function (screen, callback) {
    callback.pending();
  });
	
	this.When(/^I disable the Save user ID option$/, function (screen, callback) {
    callback.pending();
  });
	
	this.Then(/^the app should not remember my User ID$/, function (screen, callback) {
    callback.pending();
  });
	
	this.When(/^I attempt to log out of the app$/, function (screen, callback) {
    callback.pending();
  });
	
	this.Then(/^I should be presented options to either Cancel or Log out$/, function (screen, callback) {
    callback.pending();
  });
	
	this.Then(/^I choose to Cancel Log out$/, function (screen, callback) {
    callback.pending();
  });
	
	this.Then(/^I should not be able to access any of the apps features$/, function (screen, callback) {
    callback.pending();
  });
};

module.exports = more_screen_steps;