var LoginPage = require('../login/login.page');
var login;

beforeEach(function(){
    login = new LoginPage();
    disableCssAnimations();
})

//KMOB-424
describe('Password Page', function() {

    beforeEach(function() {
        login.enterUserId();
        login.nextButton.click();
    });

    it('should have the correct URL', function() {
        expect(browser.getCurrentUrl()).toEqual('http://localhost:8100/#/login');
    });

    it('should have the correct help text', function() {
        expect(login.passwordHelp.getText()).toEqual(login.passwordDescription);
    });    

    it('should have a link to forgotten login details', function() {
        expect(login.forgotDetails.isDisplayed()).toBeTruthy();
    });

    it('should have a link to the register and terms of use pages', function() {

        //Shouldnt show right away as button overlays it
        expect(login.registerHere.isDisplayed()).toBeFalsy();

        //Removed focus from user id by tapping on the body
        // login.userId.sendKeys(protractor.Key.TAB);
        element(by.xpath("//div[@class='scroll']")).click();

        //Button should dissapear, revealing register here and terms of use link
        expect(login.registerHere.isDisplayed()).toBeTruthy();
        expect(login.termsOfUse.isDisplayed()).toBeTruthy();

    });

    // it('should have 3 different password positions', function() {
    //     // console.log(login.sparsePassword1.getAttribute('value'));
    //     // console.log(login.sparsePassword2.getAttribute('value'));
    //     // console.log(login.sparsePassword3.getAttribute('value'));
    // });

    it('should have focus on the first password field', function() {
       //check that the First sparse password field has focus when first coming to the view
       expect(login.sparsePassword1.getAttribute('ng-model')).toEqual(browser.driver.switchTo().activeElement().getAttribute('ng-model'));
    });

    it('should display the login button when the password fields have focus', function() {
        //expect the first password field to have focus
        expect(login.sparsePassword1.getAttribute('ng-model')).toEqual(browser.driver.switchTo().activeElement().getAttribute('ng-model'));
        //the login button should be visible
        expect(login.loginButton.isDisplayed()).toBeTruthy();
        //move to the second password field
        login.sparsePassword2.click();
        //check that the login button is still visible
        expect(login.loginButton.isDisplayed()).toBeTruthy();
        //move to the third password field
        login.sparsePassword3.click();
        //check that the login button is still visible
        expect(login.loginButton.isDisplayed()).toBeTruthy();
    });

    it('should change input field when I type each character', function() {
        //check that the first password field has focus
        expect(login.sparsePassword1.getAttribute('ng-model')).toEqual(browser.driver.switchTo().activeElement().getAttribute('ng-model'));
        login.sparsePassword1.sendKeys('1');
        //as I type in the first character the focus should move to the second field
        expect(login.sparsePassword2.getAttribute('ng-model')).toEqual(browser.driver.switchTo().activeElement().getAttribute('ng-model'));
        login.sparsePassword2.sendKeys('1');
        //as I type in the second character the focus should move to the third field
        expect(login.sparsePassword3.getAttribute('ng-model')).toEqual(browser.driver.switchTo().activeElement().getAttribute('ng-model'));
        login.sparsePassword3.sendKeys('1');
    });

    it('should only show the login button as active when all characters are entered', function() {
        login.sparsePassword1.sendKeys('1');
        expect(login.loginButtonDisabled.isDisplayed()).toBeTruthy();
        login.sparsePassword2.sendKeys('1');
        expect(login.loginButtonDisabled.isDisplayed()).toBeTruthy();
        login.sparsePassword3.sendKeys('1');
        expect(login.loginButtonActive.isDisplayed()).toBeTruthy();
        login.sparsePassword1.clear();
        expect(login.loginButtonDisabled.isDisplayed()).toBeTruthy();
    });

    // it('should hide the password characters as they are typed', function() {
        
    // });

    it('should take you back to the password screen when enabeling the user Id field', function() {
        login.userId.click();
        expect(login.logo.isDisplayed()).toBeTruthy();
    });

    // it('should show an error when you submit an incorrect password', function() {
        
    // });

    // it('should susspend your account after 3 unsuccessful login attempts', function() {
        
    // });    

    it('should login ok with valid credentials', function() {
        login.enterPassword();
        expect(login.termsAndConditions.isDisplayed()).toBeTruthy();
    });
});

