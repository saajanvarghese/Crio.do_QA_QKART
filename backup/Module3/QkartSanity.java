/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package QKART_SANITY_LOGIN.Module1;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QkartSanity {

    public static String lastGeneratedUserName;


    public static RemoteWebDriver createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);

        return driver;
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION - Implement method using below steps
        /*
         * 1. Check if the folder "/screenshots" exists, create if it doesn't
         * 2. Generate a unique string using the timestamp
         * 3. Capture screenshot
         * 4. Save the screenshot inside the "/screenshots" folder using the following
         * naming convention: screenshot_<Timestamp>_<ScreenshotType>_<Description>.png
         * eg: screenshot_2022-03-05T06:59:46.015489_StartTestcase_Testcase01.png
         */
        try {
            File theDir = new File("/screenshots");
            if(!theDir.exists()){
                theDir.mkdirs();
            }
            String timeStamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timeStamp, screenshotType, description);
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File destFile = new File("screenshots/"+ fileName);
            FileUtils.copyFile(srcFile, destFile);
            } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }

         
    }

    /*
     * Testcase01: Verify the functionality of Login button on the Home page
     */
    public static Boolean TestCase01(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;

        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase01");

        logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "FAIL");
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");

            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase01");

            // Return False as the test case Fails
            return false;
        } else {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "PASS");
        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");
            return false;
        }

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();
        logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");

        //Take Screenshot
        takeScreenshot(driver, "EndTestCase", "TestCase01");

        return status;
    }

    /*
     * Verify that an existing user is not allowed to re-register on QKart
     */
    public static Boolean TestCase02(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;

         //Take Screenshot
         takeScreenshot(driver, "StartTestCase", "TestCase02");

        logStatus("Start Testcase", "Test Case 2: Verify User Registration with an existing username ", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        logStatus("Test Step", "User Registration : ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "PASS" : "FAIL");

             //Take Screenshot
        takeScreenshot(driver, "Failure", "TestCase02");

            return false;

        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "FAIL" : "PASS");

        //Take Screenshot
        takeScreenshot(driver, "EndTestCase", "TestCase02");

        return !status;
    }

    /*
     * Verify the functinality of the search text box
     */
    public static Boolean TestCase03(RemoteWebDriver driver) throws InterruptedException {

        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase03");

        logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // SLEEP_STMT_01 : Wait for Page to Load
        Thread.sleep(5000);

        // Search for the "yonex" product
        status = homePage.searchForProduct("yonex");
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");

            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase03");

            return false;
        }

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available
        if (searchResults.size() == 0) {
            logStatus("TestCase 3", "Test Case Failure. There were no results for the given search string", "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase03");
            return false;
        }

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            if (!elementText.toUpperCase().contains("YONEX")) {
                logStatus("TestCase 3", "Test Case Failure. Test Results contains un-expected values: " + elementText,
                        "FAIL");

            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase03");
                return false;
            }
        }

        logStatus("Step Success", "Successfully validated the search results ", "PASS");
        // SLEEP_STMT_02
        Thread.sleep(2000);

        // Search for product
        status = homePage.searchForProduct("Gesundheit");
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase03");
            return false;
        }

        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        if (searchResults.size() == 0) {
            if (homePage.isNoResultFound()) {
                logStatus("Step Success", "Successfully validated that no products found message is displayed", "PASS");
            }
            logStatus("TestCase 3", "Test Case PASS. Verified that no search results were found for the given text",
                    "PASS");
        } else {
            logStatus("TestCase 3", "Test Case Fail. Expected: no results , actual: Results were available", "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase03");
            return false;
        }

        //Take Screenshot
        takeScreenshot(driver, "EndTestCase", "TestCase03");

        return true;
    }

    /*
     * Verify the presence of size chart and check if the size chart content is as
     * expected
     */
    public static Boolean TestCase04(RemoteWebDriver driver) throws InterruptedException {

        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase04");

        logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // SLEEP_STMT_03 : Wait for page to load
        Thread.sleep(5000);

        // Search for product and get card content element of search results
        status = homePage.searchForProduct("Running Shoes");
        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result
            if (result.verifySizeChartExists()) {
                logStatus("Step Success", "Successfully validated presence of Size Chart Link", "PASS");

                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                logStatus("Step Success", "Validated presence of drop down", status ? "PASS" : "FAIL");

                // Open the size chart
                if (result.openSizechart()) {
                    // Verify if the size chart contents matches the expected values
                    if (result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver)) {
                        logStatus("Step Success", "Successfully validated contents of Size Chart Link", "PASS");
                    } else {
                        logStatus("Step Failure", "Failure while validating contents of Size Chart Link", "FAIL");
                        //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase04");
                    }

                    // Close the size chart modal
                    status = result.closeSizeChart(driver);

                } else {
                    logStatus("TestCase 4", "Test Case Fail. Failure to open Size Chart", "FAIL");
                    //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase04");
                    return false;
                }

            } else {
                logStatus("TestCase 4", "Test Case Fail. Size Chart Link does not exist", "FAIL");
                //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase04");
                return false;
            }
        }
        logStatus("TestCase 4", "Test Case PASS. Validated Size Chart Details", "PASS");

        //Take Screenshot
        takeScreenshot(driver, "EndTestCase", "TestCase04");

        return status;
    }

    /*
     * Verify the complete flow of checking out and placing order for products is
     * working correctly
     */
    public static Boolean TestCase05(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;

        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase05");

        logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 5", "Test Case Failure. Happy Flow Test Failed", "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase05");
        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase05");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct("Tan");
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        // Place the order
        checkoutPage.placeOrder();
        // SLEEP_STMT_04: Wait for place order to succeed and navigate to Thanks page

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        //Thread.sleep(3000);

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        // Go to the home page
        homePage.navigateToHome();
        Thread.sleep(3000);

        // Log out the user
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");

        //Take Screenshot
        takeScreenshot(driver, "EndTestCase", "TestCase05");

        return status;
    }

    /*
     * Verify the quantity of items in cart can be updated
     */
    public static Boolean TestCase06(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;

        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase06");

        logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
        Home homePage = new Home(driver);
        //Register registration = new Register(driver);
        //Login login = new Login(driver);

        // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

        // TODO: Register a new user

        logStatus("Start TestCase06", "Test Case 1: Verify User Registration", "DONE");
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "FAIL");
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");

            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase06");

            // Return False as the test case Fails
            return false;
        } else {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "PASS");
        }       

        // TODO: Login using the newly registed user

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase06");
            return false;
        }


        // TODO: Add "Xtend Smart Watch" to cart
        status = homePage.searchForProduct("Xtend Smart Watch");
        homePage.addProductToCart("Xtend Smart Watch");

        // TODO: Add "Yarine Floor Lamp" to cart

        status = homePage.searchForProduct("Yarine Floor Lamp");
        homePage.addProductToCart("Yarine Floor Lamp");

        // update watch quantity to 2
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);

        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);

        // update watch quantity again to 1

        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = driver.getCurrentUrl().endsWith("/thanks");

        homePage.navigateToHome();
        Thread.sleep(3000);
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ? "PASS" : "FAIL");

        //Take Screenshot
        takeScreenshot(driver, "EndTestCase", "TestCase06");

        return status;

        
    }


    public static Boolean TestCase07(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;

        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase07");

        logStatus("Start TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough",
                "DONE");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("Step Failure", "User Perform Registration Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase07");
            return false;
        }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");

        //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase07");

            return false;
        }

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Stylecon");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set");
        Thread.sleep(3000);

        homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set", 10);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();

        logStatus("End TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                status ? "PASS" : "FAIL");

        //Take Screenshot
         takeScreenshot(driver, "EndTestCase", "TestCase09");

        return status;
    }

    public static Boolean TestCase08(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;

         // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase08");

        logStatus("Start TestCase", "Test Case 8: Verify Happy Flow of buying products", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 8", "Test Case Failure. Happy Flow Test Failed", "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase08");
        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase08");
            logStatus("End TestCase", "Test Case 8: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        String currentURL = driver.getCurrentUrl();
        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        
        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);

        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        logStatus("End TestCase",
        "Test Case 8: Multi-Tab Validation performed:  ",status ? "PASS" : "FAIL");

          //Take Screenshot
         takeScreenshot(driver, "EndTestCase", "TestCase08");


        return status;
    }

    public static Boolean TestCase9(RemoteWebDriver driver) throws InterruptedException {
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -

        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase09");

        Boolean status = false;

        logStatus("Start TestCase", "Test Case 09: Verify that the Privacy Policy, About Us are displayed Correctly",
         "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 8", "Test Case Failure. Verify that the Privacy Policy, About Us are displayed Correctly",
            "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase08");
        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase08");
            logStatus("End TestCase", "Test Case 8: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();
        driver.findElement(By.linkText("Privacy policy")).click();
        //assert (driver.getCurrentUrl().equals(basePageURL));

        if(!driver.getCurrentUrl().equals(basePageURL)){
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase09");
            return false;
        }

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        WebElement privacy_policyHeading = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/h2"));
        if(!privacy_policyHeading.getText().equals("Privacy Policy")){
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase09");
            return false;
        }

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();

        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TermsOfServicHeading = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/h2"));

        if(!TermsOfServicHeading.getText().equals("Terms of Service")){
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase09");
            return false;
        }

        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);


        logStatus("End TestCase",
        "Test Case 09: Verify that the Privacy Policy, About Us are displayed Correctly",status ? "PASS" : "FAIL");
        //Take Screenshot
        takeScreenshot(driver, "EndTestCase", "TestCase09");
        return status;
    }

    public static Boolean TestCase10(RemoteWebDriver driver) throws InterruptedException {
        Boolean status = false;

        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase10");

        logStatus("Start TestCase", "Test Case 10: Validating Contact Us pop-up","DONE");
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
         // Go to the home page
         Home homePage = new Home(driver);
         homePage.navigateToHome();

        WebElement contactnowpage = driver.findElement(By.xpath("//*[@id='root']/div/div/div[5]/div[2]/p[3]"));
        contactnowpage.click();

        Thread.sleep(2000);

        WebElement contactnowpagedisplay = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div"));
         if(contactnowpagedisplay.isDisplayed()){
            status = true;
         }
         else{
             //Take Screenshot
         takeScreenshot(driver, "Failure", "TestCase10");
            status =  false;
         }

         WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
         //name.click();
         name.sendKeys("crio user");
         Thread.sleep(2000);

         WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
         //email.click();
         email.sendKeys("criouser@gmail.com");
         Thread.sleep(2000);

         WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
         //message.click();
          message.sendKeys("Testing the contact us page");
        //name.sendKeys("Testing the contact us page");
         Thread.sleep(2000);


         WebElement contactnowbtn = driver.findElement(By.xpath("//html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));
         contactnowbtn.click();
         Thread.sleep(2000);

         logStatus("End TestCase","Test Case 10: Validating Contact Us pop-up",status ? "PASS" : "FAIL");


        //Take Screenshot
        takeScreenshot(driver, "EndTestCase", "TestCase10");
        return status;
    }

    public static Boolean TestCase11(RemoteWebDriver driver) throws InterruptedException {
        Boolean status = false;

        //Take Screenshot
        takeScreenshot(driver, "StartTestCase", "TestCase11");

        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
        logStatus("Start TestCase", "Test Case 11: Ensure that QKART advertisement is clickable",  "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 11", "Test Case Failure. Verify that the Privacy Policy, About Us are displayed Correctly",
            "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase08");
        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            //Take Screenshot
            takeScreenshot(driver, "Failure", "TestCase08");
            logStatus("End TestCase", "Test Case 8: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        status = Advertisements.size() == 3;
        logStatus("Step", "Verify that 3 advertisements are available", status ? "PASS" : "FAIL");

        WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();

        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        logStatus("Step", "Verify that Advertisement 1 is clickable", status ? "PASS" : "FAIL");

        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();

        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        logStatus("Step", "Verify that Advertisement 2 is clickable", status ? "PASS" : "FAIL");

        logStatus("End TestCase","Test Case 11: Ensure that QKART advertisement is clickable",status ? "PASS" : "FAIL");


        //Take Screenshot
        takeScreenshot(driver, "EndTestCase", "TestCase11");
        return status;
    }

    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        int totalTests = 0;
        int passedTests = 0;
        Boolean status;
        RemoteWebDriver driver = createDriver();
        // Maximize and Implicit Wait for things to initailize
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(05, TimeUnit.SECONDS);

        try {
            // Execute Test Case 1
        //     totalTests += 1;
        //     status = TestCase01(driver);
        //     if (status) {
        //         passedTests += 1;
        //     }

        //     System.out.println("");

        //     // // Execute Test Case 2
        //     // Execute Test Case 2
        //     totalTests += 1;
        //     status = TestCase02(driver);
        //     if (status) {
        //         passedTests += 1;
        //     }

        //      System.out.println("");

        // //     // // Execute Test Case 3
        //     totalTests += 1;
        //     status = TestCase03(driver);
        //     if (status) {
        //     passedTests += 1;
        //     }

        // //       System.out.println("");

        // //     // // Execute Test Case 4
        //     totalTests += 1;
        //     status = TestCase04(driver);
        //     if (status) {
        //     passedTests += 1;
        //     }

        //     System.out.println("");

        // //     // Execute Test Case 5
        //     totalTests += 1;
        //     status = TestCase05(driver);
        //     if (status) {
        //     passedTests += 1;
        //     }

        //     System.out.println("");

        // //     // Execute Test Case 6
        //     totalTests += 1;
        //     status = TestCase06(driver);
        //     if (status) {
        //     passedTests += 1;
        //     }

        //     System.out.println("");

        // //     //Execute Test Case 7
        //     totalTests += 1;
        //     status = TestCase07(driver);
        //     if (status) {
        //     passedTests += 1;
        //     }

        //      System.out.println("");

        // //     //Execute Test Case 8
            // totalTests += 1;
            // status = TestCase08(driver);
            // if (status) {
            // passedTests += 1;
            // }

            // System.out.println("");

        // //    // Execute Test Case 9
        //     totalTests += 1;
        //     status = TestCase9(driver);
        //     if (status) {
        //     passedTests += 1;
        //     }

        //     System.out.println("");

        // //     //Execute Test Case 10
        //     totalTests += 1;
        //     status = TestCase10(driver);
        //     if (status) {
        //     passedTests += 1;
        //     }

        //     System.out.println("");

        //    //Execute Test Case 11
        //     totalTests += 1;
        //     status = TestCase11(driver);
        //     if (status) {
        //     passedTests += 1;
        //     }

        //     System.out.println("");
        } catch (Exception e) {
            throw e;
        } finally {
            // quit Chrome Driver
            driver.quit();

            System.out.println(String.format("%s out of %s test cases Passed ", Integer.toString(passedTests),
                    Integer.toString(totalTests)));
        }

    }
}