var UserPage = require('./termsofuse.page');
var user;
beforeEach(function() {
    user = new UserPage();
})

//KMOB-431
describe('Terms of Use Page', function() {

    it('Should display Terms of use page with title Terms of use', function() {
        expect(user.title).toBe('Terms of use');
    });
    it('Should have a Close button for online servicing in Terms of use page', function() {
        expect(user.closeButton.isDisplayed()).toBeTruthy();
    });
    it('Should have a link Terms of use for online servicing in Terms of use page', function() {
        expect(user.termsOfUseforOnlineServicing.isDisplayed()).toBeTruthy();
    });
    it('Should have a link Privacy policy in Terms of use page', function() {
        expect(user.privacyPolicy.isDisplayed()).toBeTruthy();
    });
    it('Should have a link Cookie policy in Terms of use page', function() {
        expect(user.cookiePolicy.isDisplayed()).toBeTruthy();
    });
    it('Should display Terms of use for online servicing page on click of Terms of use for online servicing Link', function() {
        user.clickTermsOfUseforOnlineService;
    });
    it('Should display Privacy policy page on click of Privacy policy Link', function() {
        user.clickTermsOfUseforOnlineService;
    });
    it('Should display Cookie policy page on click of Cookie policy Link', function() {
        user.clickTermsOfUseforOnlineService;
    });
    it('Should close terms of Use page on click of Close button', function() {
        user.closeTermsofUsePage;
    });

});