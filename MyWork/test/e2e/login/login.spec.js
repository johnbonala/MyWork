var UserPage = require('./login.page');
var user;

beforeEach(function(){
    user = new UserPage();
    disableCssAnimations();
})

//KMOB-424
describe('Login page', function() {

    it('should have the correct URL', function() {
		expect(browser.getCurrentUrl()).toEqual('http://localhost:8100/#/login');
	});

    it('should have a link to forgotten login details', function() {
    	expect(user.forgotDetails.isDisplayed()).toBeTruthy();
    });

    it('should display the SL logo', function() {
    	expect(user.logo.isDisplayed()).toBeTruthy();
    });

    it('should display the into text', function() {
    	expect(user.intro.isDisplayed()).toBeTruthy();
    });

    it('should have a link to the register and terms of use pages', function() {

    	//Shouldnt show right away as button overlays it
    	expect(user.registerHere.isDisplayed()).toBeFalsy();

    	//Removed focus from user id
    	user.userId.sendKeys(protractor.Key.TAB);

    	//Button should dissapear, revealing register here and terms of use link
    	expect(user.registerHere.isDisplayed()).toBeTruthy();
    	expect(user.termsOfUse.isDisplayed()).toBeTruthy();

     });

    it('should login ok', function() {
    	user.loginAs('tester');
        expect(user.termsAndConditions.isDisplayed()).toBeTruthy();
     });
});

