var isReadyToRun = false;
var YES = true;
var NO = false;
var start = new Date().getTime();


describe('Terms of Use', function() {
    beforeEach(function() {
        isReadyToRun = app().waitUntilReadyToRun(isReadyToRun);
        isReadyToRun = app().applyReadyToRun();
    });
    it('Displays terms of use on clicking terms of use link', function() {
        app().clickOnTermsofUse();
        expect(app().termsOfUseTitle()).toEqual('Terms of use');
    });

    it('Closes terms of Use on click of close button', function() {
        app().clickOnTermsofUse();
        expect(app().closeButtoninTermsofUseDisplayed()).toBe(true);
        app().closeTermsOfUse();

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