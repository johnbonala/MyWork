
var casper = require("casper").create(),
    viewportSizes = [[320,480]],
    urls = ['http://localhost:8100'];

console.log('Step 1');
 
casper.start();
 
casper.each(urls, function(self, url, i) {

    console.log(i);

    saveDir = url.replace(/[^a-zA-Z0-9]/gi, '-').replace(/^https?-+/, '');

    casper.each(viewportSizes, function(self, viewportSize, i) {
     
        // set two vars for the viewport height and width as we loop through each item in the viewport array
        var width = viewportSize[0],
            height = viewportSize[1];
     
        //give some time for the page to load
        casper.wait(3000, function() {
     
            //set the viewport to the desired height and width
            this.viewport(width, height);
     
            casper.thenOpen(url, function() {
                this.echo('Opening at ' + width);
     
                //Set up two vars, one for the fullpage save, one for the actual viewport save
                var FPfilename = saveDir + '/fullpage-' + width + ".png";
                var ACfilename = saveDir + '/' + width + '-' + height + ".png";
     
                this.sendKeys('input[id="loginData_username"]', 'TESTER\n');

                //give some time for the page to load
                casper.wait(2000, function() {

                    //Capture selector captures the whole body
                    this.captureSelector(FPfilename, 'body');
         
                    //capture snaps a defined selection of the page
                    this.capture(ACfilename,{top: 0,left: 0,width: width, height: height});
                    this.echo('snapshot taken');

                });
            });
        });
    });
 
});

casper.run(function() {
    this.echo('Finished captures').exit();
});