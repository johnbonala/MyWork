var login_steps = function() {
    this.When(/^I focus on the User ID field$/, function(callback) {
        callback.pending();
    });

    this.Then(/^I should see the Next button in a disabled state$/, function(callback) {
        callback.pending();
    });

    this.When(/^I enter a User ID less than 6 characters$/, function(callback) {
        callback.pending();
    });

    this.Then(/^I should see the Next button in a disabled state$/, function(callback) {
        callback.pending();
    });

    this.When(/^I enter a valid User Id$/, function(callback) {
        element(by.model('loginData.username')).sendKeys('TESTER').then(function() {
            callback();
        });
    });

    this.Then(/^I should see the Next button become active to attempt to Login$/, function(callback) {
        var btn = element(by.css('#next_button.active'));
        this.expect(btn).to.exist;
        callback.pending();
    });

    this.When(/^I submit a valid User Id$/, function(callback) {
        callback.pending();
    });

    this.When(/^I enter the correct credentials$/, function(callback) {
        callback.pending();
    });

    this.Then(/^I should see my User Id in the User Id field$/, function(screen, callback) {
        callback.pending();
    });

    this.Then(/^I should not see my User Id in the User Id field$/, function(screen, callback) {
        callback.pending();
    });

};

module.exports = login_steps;

