exports.folders=function(){
return [{
        "screenshotsfolder": "test/e2e/test-results/screenshots/",
		"baseFolder":"test-results/htmlresults",
		"loadExtension":"--load-extension=C:/Users/co55417/AppData/Local/Google/Chrome/User Data/Default/Extensions/geelfhphabnejjhdalkjhgipohgpdnoc/0.9.15_0"
    }];
}

exports.User = function(){
return [
    {
	//User Having a pension account.
        "id": "Tester",
        "password": "1111111111111111111111111111111111111111111111"
	// User Having an Isa 
	
    }  
];
}
exports.originalvalues =function(){
return[
    {
        "planDetails": [
            {
                "PlanName": "Group Flexible Retirement Plan",
                "PlanNumber": "D2224792000",
                "FundValue": "6,207.20",
				"expectedNumberOfInvestments":3,
				"expectedAdditionalInvestments":1,
				"expectedFMC":"1",
				"expectedAditionalExpenses":"0.013",
				"expectedTotalFundCharge":"1.013"
            }
        ]
    }
];
}
exports.server =function(){
return [{"rippleUrl":"http://localhost:8100/#/login/?enableripple=true&platform=default",
"url":"http://localhost:8100/#/login/"}];
}