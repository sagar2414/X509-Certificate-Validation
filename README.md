Maven Project To Validate Self-Signed Certificate
===============================================

This repository contains a Maven project with TestNG integration. 

#How to execute the test-cases
------------------------------
I have created a GitHub Actions Workflow_Dispatch, using which we can just trigger an action, which intern executes maven command. Due to restrictions from GitHub as mentioned in Notes, Please follow below steps for execution.
1. Clone the repo to the local
2. Check for pre-requisite checklist
3. Execute the test using maven command given below

**Note** :

	I have added Github actions workflow, but as per Github rules, it can only be triggered by Owners and collaborators, 
	so I have triggered and kept few execution results. Please review latest Actions in the same repository, triggered for the execution results.
	
#Maven command
--------------

	mvn clean compile
	mvn test

#Pre-Requisite Checklist
-------------------------
- [ ] JDK-17 or above
- [ ] maven


#Project Overview : File structure
----------------------------------

**BaseDataProvider** : Contains data provider to support TestNG

**CertificateUtility** : Java utility class, serves all the necessary functions for validations.

**Repository** : Separates the string data from the utility and test-cases.

**certificateDestination** : Generated certificates are stored in resources.

**CertificateValidation** :  Test-cases for the validations

**testSuiteFiles** : TestNG files for managing the testcases.


#Contributor
----------------------------------

	Name : B S Sagar
	Role : Senior QA Engineer
