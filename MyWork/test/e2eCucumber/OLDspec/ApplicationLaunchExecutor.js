var isReadyToRun = false;
var YES = true;
var NO = false;
var start = new Date().getTime();
describe('Application Launch', function() {
    beforeEach(function() {        
        isReadyToRun = app().waitUntilReadyToRun(isReadyToRun);
        isReadyToRun = app().applyReadyToRun();
    });

    it('Displays Standard life savings Image on launch of application', function() {
        expect(app().standardLifeSavingImage().isDisplayed()).toBe(true);
    });
    it('Prompts for a user id on launch of application', function() {
        expect(app().userIdTextDisplayed()).toBe('Enter User ID');
    });
    it('Displays forgotten login details link on launch of application', function() {
        expect(app().forgottenLoginDetails().isDisplayed()).toBe(true);
    });
    it('Displays Terms of Use link on launch of application', function() {
        expect(app().termsOfUse().isDisplayed()).toBe(true);
    });
    it('Displays Register here link on launch of application', function() {
        expect(app().registerHere().isDisplayed()).toBe(true);
    });
});



function app() {
    return protractor.userSession;
}

function expectedPlanDetails() {
    return protractor.testData.originalvalues()[0].planDetails[0];
}

function pensionUser() {
    return protractor.testData.User()[0];
}