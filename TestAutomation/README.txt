Overview
--------

build.xml
	Used to run the tests from a local dev env.


build.hudson.ci.xml
	Compiles the code and runs junit tests in hudson. Part of a continuous integration process.


build.hudson.xml
	Used to schedule test suit runs from hudson.


build.backup-restore.xml
	Used to restore DBs from hudson


build.hudson.upgrade-dbs.xml
	Used to upgrade DBs from hudson


SampleScripts.xlsx
	A self testing script. Contains all different scenarios to 'test' the automation framework.
	Its primary purpose is as a system test. If you’re making a code change give it a run after you’ve finished to check you haven’t broken anything.
	Please keep this up to date with any valid test scenarios you can think of.
	All keywords should be included, and using all permutations of parameters (i.e. optional/blank etc).
	In particular if a tester gives you an error scenario please put it into this spreadsheet and ensure it is running error-free after your code fix.
	For now this is the closest we have to a suite of junits.


Test Automation keyword manual.xlsx
	A manual for the available keywords


How to run locally
------------------
1. Edit config.properties:
   * Set 'testlab' = the folder holding your test scripts
   * Set 'mailListTo' = your email address
   * Set any other properties e.g. 'browser', 'testRunFolder' & 'url'
2. Run build.xml target 'iterateTestLab' as an Ant task.




How to debug locally
--------------------
1. Open up ExecuteTestsFromWorkbook_Debug.java and change String testsuite = "Your test script.xlsx"
2. Set up breakpoints in code.
3. ExecuteTestsFromWorkbook_Debug.java -> Debug As -> TestNG Test


build.backup-restore.xml
------------------------

backupdatabases-rc
	Used to create new backups of the automation test databases in RC cluster test
	
restoredatabases-rc
	Used to restore backups of the databases created in the target (backupdatabases-rc)
	
backupdatabases-test5
	Used to create new backups of the automation test databases in test5
	
restoredatabases-test5
	Used to restore backups of the databases created in the target (backupdatabases-test5)


Upgrading to Java7 locally
---------------------------
There is one known issue with using java6 which sometimes comes while downloading a report.
java6 supports only till 1024KB of response at a time and if it is more than that it fails 
to download a file.So we need to follow below steps to upgrade java6 to java7.

1.Go to eclipse-preferences-Installed JRE's remove java6 JRE's and select java7 JRE. 
2.Go to eclipse-preference-Compiler-Compiler compliance level to 1.7
3.Then Apply and do clean build 
	