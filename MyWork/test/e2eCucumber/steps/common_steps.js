'use strict';

var common_steps = function() {

    this.Given(/^I am on the Home Screen$/, function(callback) {
        browser.get('http://localhost:8100/#/login').then(function() {
            callback();
        });
    });

    this.When(/^I press the Android back button$/, function(callback) {
        callback.pending();
    });

    this.Then(/^I should see the application dismissed$/, function(callback) {
        callback.pending();
    });

    this.When(/^I open the "(.*)" link$/, function(link, callback) {
        callback.pending();
    });

    this.Then(/^I should be taken to the correct view for that link$/, function(callback) {
        callback.pending();
    });

    this.Then(/^I should be presented with the "(.*)" screen$/, function(screen, callback) {
        callback.pending();
    });

    this.Given(/^I have successfully logged in with a user with an ISA$/, function(callback) {
        callback.pending();
    });

    this.Given(/^I have successfully logged in to the app as a Tester$/, function(callback) {
        callback.pending();
    });

    this.When(/^the field has focus$/, function(callback) {
        callback.pending();
    });

    this.When(/^I log out of the application$/, function(callback) {
        callback.pending();
    });

    this.When(/^I login to the application$/, function(callback) {
        callback.pending();
    });

    this.When(/^I open the Important Information$/, function(callback) {
        callback.pending();
    });

    this.When(/^I select the 'Infomation' button$/, function(callback) {
        callback.pending();
    });

    this.When(/^I select "(.*)"$/, function(link, callback) {
        callback.pending();
    });

    this.When(/^I tap the "(.*)" button$/, function(text, callback) {
        callback.pending();
    });

    this.When(/^I press the back button in the header bar$/, function(callback) {
        callback.pending();
    });

    this.When(/^I press the Next button$/, function(callback) {
        callback.pending();
    });
    this.When(/^I see the Call to action for "(.*)"$/, function(callback) {
        callback.pending();
    });

    this.When(/^I Press Cancel within the Call to action$/, function(callback) {
        callback.pending();
    });

    this.Then(/^I should be presented with the Android dialler$/, function(callback) {
        callback.pending();
    });

    this.Then(/^I should see "(.*)" as the calculated Tax Releif$/, function(amount, callback) {
        callback.pending();
    });

    this.Then(/^I should see the "(.*)" error message$/, function(error, callback) {
        callback.pending();
    });

    this.Then(/^I should not see the following error message:$/, function(callback) {
        callback.pending();
    });
    // | Enter a 'Valid from' date   |

    this.Then(/^I should see the following error message:$/, function(callback) {
        callback.pending();
    });
    // | Enter a 'Valid from' date   |

    this.Then(/^I should have the following working links within the page:$/, function(callback) {
        callback.pending();
    });

    this.When(/^I tap the "(.*)" button$/, function(text, callback) {
        callback.pending();
    });

    this.When(/^I tap the body of the app$/, function(screen, callback) {
        callback.pending();
    });

    this.Then(/^the keyboard should be dismissed$/, function(screen, callback) {
        callback.pending();
    });

    this.When(/^I open the (.*) link$/, function(link, callback) {
        // Write code here that turns the phrase above into concrete actions
        callback.pending();
    });

    this.Then(/^I should be taken to the (.*)$/, function(correctView, callback) {
        // Write code here that turns the phrase above into concrete actions
        callback.pending();
    });

    this.Then(/^I should be presented with the Password screen$/, function(callback) {
        // Write code here that turns the phrase above into concrete actions
        callback.pending();
    });

    this.When(/^accept the Terms and Conditions$/, function(callback) {
        // Write code here that turns the phrase above into concrete actions
        callback.pending();
    });

    this.Then(/^I should be presented with the Product page$/, function(callback) {
        // Write code here that turns the phrase above into concrete actions
        callback.pending();
    });

};

module.exports = common_steps;

