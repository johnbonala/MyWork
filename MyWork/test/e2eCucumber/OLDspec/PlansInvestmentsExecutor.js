var isReadyToRun = false;


describe('Plans And Investments', function() {
    beforeEach(function() {
        isReadyToRun = app().waitUntilReadyToRun(isReadyToRun);
        isReadyToRun = app().applyReadyToRun();
    });
    it('Displays Plan details and fund value', function() {
        app().loginAs(pensionUser());
        expect(app().planName()).toEqual(expectedPlanDetails().PlanName);
        expect(app().fundValue()).toEqual(expectedPlanDetails().FundValue);

    });
    it('Displays title in product details page', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        expect(app().headerTitle()).toEqual('Product details');
    });
    it('Displays Back button in product details page.', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        expect(app().backButtonDisplayed()).toBe(true);
    });
    it('Displays investments in product details page.', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        expect(app().investmentsDisplayed()).toBe(true);
        expect(app().numberOfInvestments()).toEqual(expectedPlanDetails().expectedNumberOfInvestments);
    });
    it('Displays recent payments link in product details page.', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        expect(app().recentPaymentsLinkDisplayed()).toBe(true);
    });
    it('Displays Other Investments of the plan selected in product details page', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        expect(app().additionalInvestmentsDisplayed()).toBe(true);
        expect(app().numberOfAdditionalInvestments()).toEqual(expectedPlanDetails().expectedAdditionalInvestments);
    });
    it('Displays top up button link in product details page.', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        expect(app().topUpButtonDisplayed()).toBe(true);
    });
    it('Displays regular payment button link in product details page.', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        expect(app().regularPaymentButtonDisplayed()).toBe(true);
    });
    it('Displays last 12 months recents payments tab on click of the recent payments link', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().clickOnRecentPaymentsLink();
        expect(app().activeRecentPayments()).toEqual('Last 12 months');
    });
    it('Displays recent payments for current tax year on selecting current tax year tab in recent payments layout.', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().clickOnRecentPaymentsLink();
        app().clickOnCurrentTaxYearTab();
        expect(app().activeRecentPayments()).toEqual('Current tax year');
    });
    it('Displays fund performance graphs on selecting an investment in product details page.', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
        expect(app().selectedGraphTable()).toEqual('1 YEAR');
        expect(app().fundFactSheetLinkDisplayed()).toBe(true);
        expect(app().backButtonDisplayed()).toBe(true);
    });
    it('Displays Fund performance for last 3 months for the selected investment on selecting the 3 Months tab', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
        app().select3MonthsTab();
        expect(app().selectedGraphTable()).toEqual('3 MONTHS');
    });
    it('Displays Fund performance for last 1 year for the selected investment on selecting the 1 Year tab', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
        app().select1YearTab();
        expect(app().selectedGraphTable()).toEqual('1 YEAR');
    });
    it('Displays Fund performance for last 3 years for the selected investment on selecting the 3 Years tab', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
        app().select3YearsTab();
        expect(app().selectedGraphTable()).toEqual('3 YEARS');
    });
    it('Displays Fund performance for last 5 years for the selected investment on selecting the 5 Years tab', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
        app().select5YearsTab();
        expect(app().selectedGraphTable()).toEqual('5 YEARS');
    });

    it('Displays about fund performance link in the fund performace page', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
        expect(app().aboutFundPerformanceLinkDisplayed()).toBe(true);
    });

    it('Displays about fund performance page with details on click of about fund performancelink', function() {
        app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
        app().clickOnAboutFundPerformance();
        expect(app().aboutFundPerformancePageTitle()).toBe('About fund performance');

    });
	it('Displays fund fact sheet link in the fund performace page',function(){
		app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
		 expect(app().fundFactSheetLinkDisplayed()).toBe(true);		
	});
	it('Displays fund fact sheet page on click of fund fact sheet link in fund performace page',function(){
		app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
		app().clickOnFundFactSheetLink();
		
	});
	it('Displays anual fund charge details in fund performace page',function(){
	app().loginAs(pensionUser());
        app().selectPlanToViewInvestments();
        app().selectAnInvestment();
		expect(app().actualFMC()).toEqual(expectedPlanDetails().expectedFMC);
		expect(app().actualAdditionalExpenses()).toEqual(expectedPlanDetails().expectedAditionalExpenses);
		expect(app().actualTotalFundCharge()).toEqual(expectedPlanDetails().expectedTotalFundCharge);		
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