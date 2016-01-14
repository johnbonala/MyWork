'use strict';

var globals = require('../common/globals').globals;
var users = require('../common/users').users;

console.log(globals);

//Wait for elements to appear for a bit if they dont already
browser.driver.manage().timeouts().implicitlyWait(200);

var LoginPage = function () {
  browser.get(globals.baseUrl);
};

LoginPage.prototype = Object.create({}, {

    //buttons
    loginButton: { get: function () { return element(by.id('login_button')); }},
    nextButton: { get: function () { return element(by.id('next_button')); }},
    loginButtonDisabled: { get: function () { return element(by.css('#login_button.disabled')); }},
    loginButtonActive: { get: function () { return element(by.css('#login_button.active')); }},
    nextButtonDisabled: { get: function () { return element(by.css('#next_button.disabled')); }},
    disagreeButton: { get: function () { return element(by.css('button.disagree')); }},
    agreeButton: { get: function () { return element(by.css('button.agree')); }},

    //inputs
    userId: { get: function () { return element(by.model('loginData.username')); }},
    sparsePassword1: { get: function () { return element(by.model('loginData.sparseAnswers[0]')); }},
    sparsePassword2: { get: function () { return element(by.model('loginData.sparseAnswers[1]')); }},
    sparsePassword3: { get: function () { return element(by.model('loginData.sparseAnswers[2]')); }},

    //links
    registerHere: { get: function () { return element(by.id('login_registerHere')); }},
    termsOfUse: { get: function () { return element(by.id('login_termsOfUse')); }},
    forgotDetails: { get: function () { return element(by.css('.forgot-details')); }},
    termsAndConditions: { get: function () { return element(by.css('.terms-and-conditions.modal')); }},

    //boilerplate
    logo: { get: function () { return element(by.id('login_header')); }},
    intro: { get: function () { return element(by.css('.login-intro')); }},
    passwordHelp: { get: function () { return element(by.xpath("//div[@ng-show='loginData.sparsePositions != null']/p")); }},

    //strings
    passwordDescription: { get: function () { return "Please enter the following\ncharacters from your password" }},

    enterUserId: { value: function () {
        this.userId.sendKeys(users.tester.id);
    }},
    enterPassword: { value: function () {
        this.sparsePassword1.sendKeys(users.tester.password.substr(0, 1));
        this.sparsePassword2.sendKeys(users.tester.password.substr(1, 1));
        this.sparsePassword3.sendKeys(users.tester.password.substr(2, 1));
    }},

    //actions
    loginAs: { value: function (user) {
    	this.login(users[user].id,users[user].password);
    }},
    login: { value: function (username, password) {
    	browser.driver.manage().timeouts().implicitlyWait(1000);
        this.userId.sendKeys(username);
        this.nextButton.click().then(function(){
            this.sparsePassword1.sendKeys(password.substr(0, 1));
            this.sparsePassword2.sendKeys(password.substr(1, 1));
            this.sparsePassword3.sendKeys(password.substr(2, 1));
            this.loginButton.click();
        }.apply(this))

      }}
});

module.exports = LoginPage;
