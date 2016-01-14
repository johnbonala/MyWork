var UserPage = require('./planlistscreen.page');
var user;

beforeEach(function() {

    user = new UserPage();
});
it('Should display more in the nav bar ', function() {
    console.log('spec executing');
    expect(user.moreButton.isDisplayed()).toBeTruthy();
});
it('Should display plan pame for each plan plan in the plan list', function() {
    expect(user.planName.isDisplayed()).toBeTruthy();
});
it('Should display plan number for each plan in the plan list', function() {
    expect(user.planNumber.isDisplayed()).toBeTruthy();
});
it('Should display plan value for each plan in the plan list ', function() {
    expect(user.planValue.isDisplayed()).toBeTruthy();
});
it('Should display plan details on click of a plan in the plan list screen', function() {
    user.openPlanDetails.then(function() {
        expect(browser.getCurrentUrl()).toContain('plandetail');
    });
});
it('Should display stocks and shares ISA advert', function() {
    expect(user.stockandSharesISA.isDisplayed()).toBeTruthy();
});
it('Should display number of days left in this tax year in the ISA advert', function() {
    expect(user.numberOfDaysinTaxYear.isDisplayed()).toBeTruthy();
});
it('Should open the external web page on click of ISA advert', function() {
    user.openISAAdvert;
});
it('Should open the more menu from the bottom on click on more button', function() {
    user.openMoreMenu.then(function() {
        expect(user.moreMenuScreen.isDisplayed()).toBeTruthy();
    });

});