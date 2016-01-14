var isReadyToRun = false;
var YES = true;
var NO = false;
var start = new Date().getTime();


describe('Authentication', function() {
    beforeEach(function() {
        isReadyToRun = app().waitUntilReadyToRun(isReadyToRun);
        isReadyToRun = app().applyReadyToRun();

    });

    it('Prompts for a sparse password when user id is provided', function() {
        app().enterUserIdAs(pensionUser().id);
        app().clickOnNextButton();
        expect(app().sparePassword1Displayed()).toBe(true);
        expect(app().sparePassword2Displayed()).toBe(true);
        expect(app().sparePassword3Displayed()).toBe(true);
    });
    it('Prompts error message on entering invalid user id or password', function() {
        console.log('No Data available now');
    });
    it('Should Allow the user to Login using userId and password, and view the plans and fund value', function() {
        app().loginAs(pensionUser());
        expect(app().planName()).toBe('Group Flexible Retirement Plan');
    });

    it('Logout from the application', function() {
        app().loginAs(pensionUser());
        app().logout();


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