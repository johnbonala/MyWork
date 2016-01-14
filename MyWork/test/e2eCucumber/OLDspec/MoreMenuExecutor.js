var isReadyToRun = false;
var YES = true;
var NO = false;
var start = new Date().getTime();

describe('More Menu', function() {
    beforeEach(function() {
        isReadyToRun = app().waitUntilReadyToRun(isReadyToRun);
        isReadyToRun = app().applyReadyToRun();
    });
    it('Closes the settings menu on click of close button', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        expect(app().closeButtoninSettingsMenuDisplayed()).toBe(true);
        app().closeSettingsMenu();
    });
    it('Displays Frequently asked questions link in the settings menu', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        expect(app().frequentlyAskedQuestionsLinkDisplayed()).toBe(true);
    });
    it('Displays Frequently asked questions page', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        app().clickFrequentlyAskedQuestions();
        expect(app().PageTitle()).toBe('Frequently asked questions');

    });
    it('Displays contact link', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        expect(app().contactLinkDisplayed()).toBe(true);

    });
    it('Displays contact page', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        app().clickContactLink();
        expect(app().PageTitle()).toEqual('Contact');
    });
    it('Displays about Link', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        expect(app().aboutLinkDisplayed()).toBe(true);

    });
    it('Displays about Page', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        app().clickAboutLink();
        expect(app().PageTitle()).toEqual('About');
    });
    it('Displays Moneyplus Blog link', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        expect(app().moneyplusBlogDisplayed()).toBe(true);
    });
    it('Displays moneyplus blog  page', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        expect(app().moneyplusBlogDisplayed()).toBe(true);
        //app().clickMoneyPlusBlog();

    });
    it('Displays save user id field and swicth save users id on', function() {
        app().loginAs(pensionUser());
        app().clickSettingsMenu();
        //app().turnOnSaveUserId();		
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