package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;



public class QKART_Tests {

    static WebDriver driver;
    public static String lastGeneratedUserName;

     @BeforeSuite(alwaysRun = true)
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
       ChromeOptions options = new ChromeOptions();
       driver = new ChromeDriver(options);

		WebDriverManager.chromedriver().setup();

        driver.manage().window().maximize();
       
    }

     @Test(description = "Verify user registration -login -logout", dataProvider = "data-provider", dataProviderClass = DP.class,  priority = 1, groups={"Sanity_test"}, enabled = true)
    public void TestCase01(String Username, String Password) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 01: Verify User Registration", "DONE");

       // Visit the Registration page and register a new user
       Register registration = new Register(driver);
       registration.navigateToRegisterPage();
       Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/register"), "Fail: Page has not been redirected to Register Page");
       takeScreenshot(driver, "Start TestCase: TestCase 01 - Success", "TestCase01 Successfully navigated to RegisterPage");             
       
       status = registration.registerUser(Username, Password, true);
       if(status){
        logStatus("TestCase 01", "User Registeration Successfully", "Success");
        Assert.assertTrue(status, "Failed to register new user");
        takeScreenshot(driver, "TestCase 01 - Success", "TestCase01 Successfully registered a new User"); 
    }
    else{
        logStatus("TestCase 01", "User Registeration Failed", "Failure");
        takeScreenshot(driver, "TestCase 01 - Failure", "TestCase01 Failed to Register a new User"); 
    }

       // Save the last generated username
       lastGeneratedUserName = registration.lastGeneratedUsername;

       // Visit the login page and login with the previuosly registered user
       Login login = new Login(driver);
       login.navigateToLoginPage();
       Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/login"), "Fail: Page has not been redirected to Login Page");
       takeScreenshot(driver, "TestCase 01 - Success", "TestCase01 Successfully navigated to Login Page"); 

       status = login.PerformLogin(lastGeneratedUserName, Password);
       if(status){
        logStatus("TestCase 01", "User Login Successfully", "Success");
        Assert.assertTrue(status, "Failed to login with registered user");
        takeScreenshot(driver, "TestCase 01 - Success", "TestCase01 User Login Success"); 
    }
    else{
        logStatus("TestCase 01", "User Login Failed", "Failure");
        takeScreenshot(driver, "TestCase 01 - Failure", "TestCase01 User Login Failed");
    }

       // Visit the home page and log out the logged in user
       Home home = new Home(driver);
       status = home.PerformLogout();

       if(status){
        logStatus("TestCase 01", "User Logged Out Successfully", "Success");
        takeScreenshot(driver, "TestCase 01 - Success ", "TestCase01 - User logged Out Successfully");
    }
    else{
        logStatus("TestCase 01", "User Logout Failed", "Failure");
        //takeScreenshot(driver, "TestCase 01 - Failure", "TestCase01 Failed to Logout the User");
    }
       Assert.assertTrue(status, "Failed to login with registered user");

        logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status
        ? "PASS" : "FAIL");
         takeScreenshot(driver, "EndTestCase", "TestCase01");
   }

@Test(priority = 2 ,groups = { "Sanity_test" })
   public void TestCase02(String Username, String Password) throws InterruptedException {
    Boolean status;
    logStatus("Start Testcase", "Test Case 02: Verify User Registration with an existing username ", "DONE");

    // Visit the Registration page and register a new user
    Register registration = new Register(driver);
    registration.navigateToRegisterPage();
    Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/register"), "Fail: Page has not been redirected to Register Page");
    takeScreenshot(driver, "TestCase 02 - Success", "TestCase01 - Successfully redirected to Register Page"); 

    status = registration.registerUser("testuser", "test@123", true);

    if(status){
        logStatus("TestCase 02", "User Logged Out Successfully", "Success");
        takeScreenshot(driver, "TestCase 02 - Success", "TestCase02 - Successfully redirected to Register Page"); 
    }
    else{
        logStatus("TestCase 02", "User Logout Failed", "Failure");
        takeScreenshot(driver, "TestCase 02 - Failure", "TestCase02 Failed to Register a new User");
    }
    Assert.assertTrue(status, "Unable to Verify user registeration");


    // Save the last generated username
    lastGeneratedUserName = registration.lastGeneratedUsername;

    // Visit the Registration page and try to register using the previously
    // registered user's credentials
    registration.navigateToRegisterPage();
    Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/register"), "Fail: Page has not been redirected to Register Page");


    status = registration.registerUser(lastGeneratedUserName, "test@123", false);
      // If status is true, then registration succeeded, else registration has
    // failed. In this case registration failure means Success
    if(!status){
        logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "FAIL" : "PASS");
        Assert.assertFalse(status, "Unable to verify user registeration");
        takeScreenshot(driver, "TestCase 02 - Success", "TestCase02 - Success - Not able to register the same user again"); 
    }

    logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "FAIL" : "PASS");
}

@Test(priority = 3 ,groups = { "Sanity_test" })
public void TestCase03() throws InterruptedException {
    logStatus("TestCase 03", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();
        Assert.assertTrue(driver.getCurrentUrl().contains("https://crio-qkart-frontend-qa.vercel.app"), "Fail: Page has not been redirected to HomePage");
        takeScreenshot(driver, "TestCase 03 - Failure", "TestCase03- Successfully Redirected to HomePage");
        
        // Search for the "yonex" product
        status = homePage.searchForProduct("YONEX");
        if(status){
            logStatus("TestCase 03", "Product Searched Successfully", "Success");
            takeScreenshot(driver, "TestCase 03 - Success", "TestCase03 Product Search Success");
        }
        else{
            logStatus("TestCase 03", "Product Search Unsuccessful", "Failure");
            takeScreenshot(driver, "TestCase 03 - Failed", "TestCase03 Product Search Unsuccessful");
        }
        Assert.assertTrue(status, "Unable to search for given product");
     

        Thread.sleep(2000);

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available
        if (searchResults.size() == 0) {
            logStatus("TestCase 03", "Test Case Failure. There were no results for the given search string", "FAIL");
            Assert.assertFalse(status, "No search results Found");
            takeScreenshot(driver, "TestCase 03 - Failed", "TestCase03 - No search results found");
        }

        for(WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            if (!elementText.toUpperCase().contains("YONEX")) {
                logStatus("TestCase 03", "Test Case Failure. Test Results contains un-expected values: " + elementText,
                        "FAIL");
                        Assert.assertFalse(status, "The title of the product does not match with the product");
                        takeScreenshot(driver, "TestCase 03 - Failed", "TestCase03 - Mismatch of Product Title");
            }
        }

        logStatus("Step Success", "Successfully validated the search results ", "PASS");
        takeScreenshot(driver, "TestCase 03 - Success", "TestCase03 - Successfully validated Search results");

        // Search for product
        status = homePage.searchForProduct("Gesundheit");

        if(!status){
            logStatus("End TestCase", "Test Case Failure. Unable to search the product", "FAIL");
            Assert.assertFalse(status, "Test Case Failure. Invalid keyword returned results");
            takeScreenshot(driver, "TestCase 03 - Failed", "TestCase03 - Mismatch of Product Title");
        }

        searchResults = homePage.getSearchResults();
        
        if (searchResults.size() == 0) {

            status = homePage.isNoResultFound();

            if(status){
                logStatus("TestCase03", "Successfully validated that no products found message is displayed", "PASS");
                takeScreenshot(driver, "TestCase 03 - Success", "TestCase03 - No products found message displayed");
            }
            else{
                logStatus("TestCase03", "Test Case Fail. Expected: no results , actual: Results were available", "FAIL");
                takeScreenshot(driver, "TestCase 03 - Failed", "TestCase03 - Products are displaying.");
            }
            Assert.assertTrue(status, "Some Products were found");       
        }
        logStatus("End TestCase", "Test Case 3: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
        takeScreenshot(driver, "End TestCase 03", "End TestCase 03"); 
    }


@Test(priority = 4,groups = {"Regression_Test"})
public void TestCase04() throws InterruptedException {
    logStatus("TestCase 4", "Start test case 04 : Verify the presence of size Chart", "DONE");
    boolean status = false;

    // Visit home page
    Home homePage = new Home(driver);
    homePage.navigateToHome();

    Assert.assertTrue(driver.getCurrentUrl().contains("https://crio-qkart-frontend-qa.vercel.app"), "Fail: Page has not been redirected to HomePage");
    takeScreenshot(driver, "Start TestCase: TestCase 04 - Success", "TestCase04 Successfully navigated to RegisterPage");

    // Search for product and get card content element of search results
    status = homePage.searchForProduct("Running Shoes");
    if(status){
                    logStatus("TestCase 04", "Product Searched Successfully", "Success");
                    takeScreenshot(driver, "TestCase 04 - Success", "TestCase04 Product Search Success");
                }
                else{
                    logStatus("TestCase 04", "Product Search Unsuccessful", "Failure");
                    takeScreenshot(driver, "TestCase 04 - Failed", "TestCase04 Product Search Unsuccessful");

                }
                Assert.assertTrue(status, "Unable to search for given product");

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

       // webElement.getSize();
        System.out.println(searchResults.size());


        status = result.verifySizeChartExists();
            logStatus("Step Success", "Verifivation: Verify if Size chart link exists", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Size Chart Link Does not Exists");
            takeScreenshot(driver, "TestCase 04 - Success", "TestCase04 SizeChart Link Exists");

            // Verify if size dropdown exists
            status = result.verifyExistenceofSizeDropdown(driver);
            logStatus("Step Success", "Verification:  presence of Size drop down", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Error occured while validating Size Drop Down");
            takeScreenshot(driver, "TestCase 04 - Success", "TestCase04 Verified SizeDropDown Element");

            //Check if Size Chart Link Opens
            status = result.openSizechart();
            logStatus("Step Success", "Verification:  presence of Size drop down", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Error occured while opening Size Chart Link");
            takeScreenshot(driver, "TestCase 04 - Success", "TestCase04 Size Chart link Opened");

            //Validate SizeChart Contents
            status = result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver);
            logStatus("Step Success", "Verification: Validated contents inside Size Chart link", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Error occured while opening Size Chart Link");
            takeScreenshot(driver, "TestCase 04 - Success", "TestCase04 - Successfully validated contents of SizeChart link");

            //Verify if SizeChart Link Closes
            status = result.closeSizeChart(driver);
            logStatus("Step Success", "Verification: Closing of Size Chart link", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Error occured while closing Size Chart Link");
            takeScreenshot(driver, "TestCase 04 - Success", "TestCase04 - Sizechart Link Successfully closed");

        }

        logStatus("Step Success", "Successfully Validated TestCase04", "PASS");
        takeScreenshot(driver, "TestCase 04 - Success", "TestCase04 - Successfully validated TestCase04");
    }

@Test(priority = 5,groups = {"Sanity_test" })
public void TestCase05() throws InterruptedException {
    Boolean status;
    logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");

     // Visit the Registration page and register a new user
     Register registration = new Register(driver);
     registration.navigateToRegisterPage();
     Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/register"), "Fail: Page has not been redirected to Register Page");
     takeScreenshot(driver, "Start TestCase: TestCase 05 - Success", "TestCase05 Successfully navigated to RegisterPage");           
     
     status = registration.registerUser("testUser", "testUser@123", true);
     if(status){
      logStatus("TestCase 05", "User Registeration Successfully", "Success");
      takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Successfully registered a new User"); 
  }
  else{
      logStatus("TestCase 05", "User Registeration Failed", "Failure");
      takeScreenshot(driver, "TestCase 05 - Failure", "TestCase05 Failed to Register a new User");
  }
     Assert.assertTrue(status, "Failed to register new user");

     // Save the last generated username
     lastGeneratedUserName = registration.lastGeneratedUsername;

     // Visit the login page and login with the previuosly registered user
     Login login = new Login(driver);
     login.navigateToLoginPage();
     Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/login"), "Fail: Page has not been redirected to Login Page");
     takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Successfully navigated to Login Page");

     status = login.PerformLogin(lastGeneratedUserName, "testUser@123");
     if(status){
      logStatus("TestCase 05", "User Login Successfully", "Success");
      takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 User Login Success");
  }
  else{
      logStatus("TestCase 05", "User Login Failed", "Failure");
      takeScreenshot(driver, "TestCase 05 - Failure", "TestCase05 User Login Failed");
  }
     Assert.assertTrue(status, "Failed to login with registered user");
 
    // Go to the home page
    Home homePage = new Home(driver);
    homePage.navigateToHome();

    // Find required products by searching and add them to the user's cart
    status = homePage.searchForProduct("YONEX");
    if(status){
        logStatus("TestCase 05", "Product Searched Successfully", "Success");
        takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Product Search Success");
    }
    else{
        logStatus("TestCase 05", "Product Search Unsuccessful", "Failure");
        takeScreenshot(driver, "TestCase 05 - Failed", "TestCase05 Product Search Unsuccessful");
    }
    Assert.assertTrue(status, "Unable to search for given product");

    status = homePage.addProductToCart("YONEX Smash Badminton Racquet");
    if(status){
        logStatus("TestCase 05", "Product Added to Cart Successfully", "Success");
        takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Product added to Cart");
    }
    else{
        logStatus("TestCase 05", "Error Occured while Adding Product to Cart", "Failure");
        takeScreenshot(driver, "TestCase 05 - Failed", "TestCase05 Unable to add Product to Cart");
    }
    Assert.assertTrue(status, "Unable to Add Product To Cart");

    status = homePage.searchForProduct("Tan");
    if(status){
        logStatus("TestCase 05", "Product Searched Successfully", "Success");
        takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Product Search Success");
    }
    else{
        logStatus("TestCase 05", "Product Search Unsuccessful", "Failure");
        takeScreenshot(driver, "TestCase 05 - Failed", "TestCase05 Product Search Unsuccessful");
    }
    Assert.assertTrue(status, "Unable to search for given product");

    status =homePage.addProductToCart("Tan Leatherette Weekender Duffle");
    if(status){
        logStatus("TestCase 05", "Product Added to Cart Successfully", "Success");
        takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Product added to Cart");
    }
    else{
        logStatus("TestCase 05", "Error Occured while Adding Product to Cart", "Failure");
        takeScreenshot(driver, "TestCase 05 - Failed", "TestCase05 Unable to add Product to Cart");
    }
    Assert.assertTrue(status, "Unable to Add Product To Cart");

    // Click on the checkout button
    status = homePage.clickCheckout();
    if(status){
        logStatus("TestCase 05", "Sucess: Clicked On CheckOut Button", "Success");
        takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Clicked on CheckOut button");
    }
    else{
        logStatus("TestCase 05", "Fail: Error Occured while clicking on CheckOut Button", "Failure");
        takeScreenshot(driver, "TestCase 05 - Failure", "TestCase05 Unable to click on CheckOut button");
    }
    Assert.assertTrue(status, "Unable to click on CheckOut Button");
    // Add a new address on the Checkout page and select it
    Checkout checkoutPage = new Checkout(driver);
    status = checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
    if(status){
        logStatus("TestCase 05", "Success: Added New Address on CheckOut Page", "Success");
        takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Added New Address");
    }
    else{
        logStatus("TestCase 05", "Fail : Error Occured while Adding New Address on CheckOut Page", "Failure");
        takeScreenshot(driver, "TestCase 05 - Failure", "TestCase05 Unable to add New Address");
    }
    Assert.assertTrue(status, "Unable to Add New Address on CheckOut Page");

    status =checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
   if(status){
        logStatus("TestCase 05", "Success: Able to Select the Added Address on CheckOut Page", "Success");
        takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Added Address gets selected");
    }
    else{
        logStatus("TestCase 05", "Fail : Error Occured while Selecting the Added Address on CheckOut Page", "Failure");
        takeScreenshot(driver, "TestCase 05 - Failure", "TestCase05 Unable to select New Added Address");
    }
    Assert.assertTrue(status, "Unable to Select the Added Address on CheckOut Page");
    
    // Place the order
    checkoutPage.placeOrder();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

    // Check if placing order redirected to the Thansk page
    status = driver.getCurrentUrl().endsWith("/thanks");

    Assert.assertTrue(driver.getCurrentUrl().endsWith("/thanks"), "Unable to redirect to Order Placed Page");
    takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Successfully navigated to Order Confirmation Page");

    // Go to the home page
    homePage.navigateToHome();

    Assert.assertTrue(driver.getCurrentUrl().contains("https://crio-qkart-frontend-qa.vercel.app"), "Fail: Page has not been redirected to HomePage");
    takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Successfully navigated to HomePage");
    // Log out the user
    status = homePage.PerformLogout();

    if(status){
        logStatus("TestCase 05", "Success: Able to Perform Log Out Operation", "Success");
        takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 User Logged Out Successfully");
    }
    else{
        logStatus("TestCase 05", "Fail : Error Occured while Performing Logout Operation", "Failure");
        takeScreenshot(driver, "TestCase 05 - Success", "TestCase05 Unable to Logout");
    }
    Assert.assertTrue(status, "Unable to Perform LogOut Operation");

    logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
    takeScreenshot(driver, "End TestCase 05", "End TestCase 05"); 
}

@Test(priority = 6,groups = {"Regression_Test" })
public void TestCase06() throws InterruptedException {
    Boolean status;
    Home homePage = new Home(driver);
    logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
    // Visit the Registration page and register a new user
    Register registration = new Register(driver);
    registration.navigateToRegisterPage();
    Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/register"), "Fail: Page has not been redirected to Register Page");
    takeScreenshot(driver, "Start TestCase: TestCase 06 - Success", "TestCase06 Successfully navigated to RegisterPage");
    
    status = registration.registerUser("testUser", "testUser@123", true);
    if(status){
     logStatus("TestCase 06", "User Registeration Successfully", "Success");
     takeScreenshot(driver, "TestCase 06 - Failure", "TestCase06 Failed to Register a new User");
 }
 else{
     logStatus("TestCase 06", "User Registeration Failed", "Failure");
     takeScreenshot(driver, "TestCase 06 - Failure", "TestCase06 Failed to Register a new User");
 }
    Assert.assertTrue(status, "Failed to register new user");

    // Save the last generated username
    lastGeneratedUserName = registration.lastGeneratedUsername;

    // Visit the login page and login with the previuosly registered user
    Login login = new Login(driver);
    login.navigateToLoginPage();
    Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/login"), "Fail: Page has not been redirected to Login Page");

    status = login.PerformLogin(lastGeneratedUserName, "testUser@123");
    if(status){
     logStatus("TestCase 06", "User Login Successfully", "Success");
     takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 User Login Success");
 }
 else{
     logStatus("TestCase 06", "User Login Failed", "Failure");
     takeScreenshot(driver, "TestCase 06 - Failure", "TestCase06 User Login Failed");
 }
    Assert.assertTrue(status, "Failed to login with registered user");

    homePage.navigateToHome();

    Assert.assertTrue(driver.getCurrentUrl().contains("https://crio-qkart-frontend-qa.vercel.app"), "Fail: Page has not been redirected to HomePage");
    
    status = homePage.searchForProduct("Xtend");
    if(status){
        logStatus("TestCase 06", "Product Searched Successfully", "Success");
        takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Product Search Success");
    }
    else{
        logStatus("TestCase 06", "Product Search Unsuccessful", "Failure");
        takeScreenshot(driver, "TestCase 06 - Failed", "TestCase06 Product Search Unsuccessful");
    }
    Assert.assertTrue(status, "Unable to search for given product");

    homePage.addProductToCart("Xtend Smart Watch");
    if(status){
        logStatus("TestCase 06", "Product Added to Cart Successfully", "Success");
        takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Product added to Cart");
    }
    else{
        logStatus("TestCase 06", "Error Occured while Adding Product to Cart", "Failure");
        takeScreenshot(driver, "TestCase 06 - Failed", "TestCase06 Unable to add Product to Cart");
    }
    Assert.assertTrue(status, "Unable to Add Product To Cart");

    status = homePage.searchForProduct("Yarine");
    if(status){
        logStatus("TestCase 06", "Product Searched Successfully", "Success");
        takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Product added to Cart");
    }
    else{
        logStatus("TestCase 06", "Product Search Unsuccessful", "Failure");
        takeScreenshot(driver, "TestCase 06 - Failed", "TestCase06 Unable to add Product to Cart");
    }
    Assert.assertTrue(status, "Unable to search for given product");

    status = homePage.addProductToCart("Yarine Floor Lamp");
    if(status){
        logStatus("TestCase 06", "Product Added to Cart Successfully", "Success");
        takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Product added to Cart");
    }
    else{
        logStatus("TestCase 06", "Error Occured while Adding Product to Cart", "Failure");
        takeScreenshot(driver, "TestCase 06 - Failed", "TestCase06 Unable to add Product to Cart");
    }
    Assert.assertTrue(status, "Unable to Add Product To Cart");

    // update watch quantity to 2
    logStatus("Step Success", "Verification:  Product Quantity Changed: ", status ? "PASS" : "FAIL");
    Assert.assertTrue(status, "Failed: Error occured while Product Quantity Change inside Cart");
    takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Product Quantity changed inside cart");

    // update table lamp quantity to 0
    status = homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);
    logStatus("Step Success", "Verification:  Product Quantity Changed: ", status ? "PASS" : "FAIL");
    Assert.assertTrue(status, "Failed: Error occured while Product Quantity Change inside Cart");
    takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Product Quantity changed inside cart");

    // update watch quantity again to 1
    status = homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);
    logStatus("Step Success", "Verification:  Product Quantity Changed: ", status ? "PASS" : "FAIL");
    Assert.assertTrue(status, "Failed: Error occured while Product Quantity Change inside Cart");
    takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Product Quantity changed inside cart");

    status = homePage.clickCheckout();
    if(status){
        logStatus("TestCase 06", "Sucess: Clicked On CheckOut Button", "Success");
        takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Clicked on CheckOut button");
    }
    else{
        logStatus("TestCase 06", "Fail: Error Occured while clicking on CheckOut Button", "Failure");
        takeScreenshot(driver, "TestCase 06 - Failure", "TestCase06 Unable to click on CheckOut button");
    }
    Assert.assertTrue(status, "Unable to click on CheckOut Button");

    // Add a new address on the Checkout page and select it
    Checkout checkoutPage = new Checkout(driver);
    status = checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
    if(status){
        logStatus("TestCase 06", "Success: Added New Address on CheckOut Page", "Success");
        takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Added New Address");
    }
    else{
        logStatus("TestCase 06", "Fail : Error Occured while Adding New Address on CheckOut Page", "Failure");
        takeScreenshot(driver, "TestCase 06 - Failure", "TestCase06 Unable to add New Address");
    }
    Assert.assertTrue(status, "Unable to Add New Address on CheckOut Page");
    status = checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
    if(status){
        logStatus("TestCase 06", "Success: Able to Select the Added Address on CheckOut Page", "Success");
        takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Added Address gets selected");
    }
    else{
        logStatus("TestCase 06", "Fail : Error Occured while Selecting the Added Address on CheckOut Page", "Failure");
        takeScreenshot(driver, "TestCase 06 - Failure", "TestCase06 Unable to select New Added Address");
    }
    Assert.assertTrue(status, "Unable to Select the Added Address on CheckOut Page");

    checkoutPage.placeOrder();

    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
    } catch (TimeoutException e) {
        System.out.println("Error while placing order in: " + e.getMessage());
        //return false;
    }

    Assert.assertTrue(driver.getCurrentUrl().endsWith("/thanks"), "Unable to redirect to Order Placed Page");
    takeScreenshot(driver, "TestCase 06 - Success", "TestCase06 Successfully navigated to Order Confirmation Page");

    homePage.navigateToHome();
    Assert.assertTrue(driver.getCurrentUrl().contains("https://crio-qkart-frontend-qa.vercel.app"), "Fail: Page has not been redirected to HomePage");
    
    
    status = homePage.PerformLogout();
    if(status){
        logStatus("TestCase 06", "Success: Able to Perform Log Out Operation", "Success");
        takeScreenshot(driver, "TestCase 06 - Success ", "TestCase06 - User logged Out Successfully");
    }
    else{
        logStatus("TestCase 06", "Fail : Error Occured while Performing Logout Operation", "Failure");
        takeScreenshot(driver, "TestCase 06 - Failure", "TestCase06 Failed to Logout the User");
    }
    Assert.assertTrue(status, "Unable to Perform LogOut Operation");

    logStatus("TestCase 06", "Test Case 6: Verify that cart can be edited: ", status ? "PASS" : "FAIL");
    takeScreenshot(driver, "End TestCase06", "End TestCase06");
}

@Test(priority = 7,groups = {"Regression_Test" })
public void TestCase07() throws InterruptedException {
    Boolean status;
    logStatus("Start TestCase",
            "Test Case 07: Verify that insufficient balance error is thrown when the wallet balance is not enough",
            "DONE");
     // Visit the Registration page and register a new user
     Register registration = new Register(driver);
     registration.navigateToRegisterPage();
     Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/register"), "Fail: Page has not been redirected to Register Page");
     takeScreenshot(driver, "Start TestCase: TestCase 07 - Success", "TestCase07 Successfully navigated to RegisterPage"); 
     
     status = registration.registerUser("testUser", "testUser@123", true);
     if(status){
      logStatus("TestCase 07", "User Registeration Successfully", "Success");
      takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 Successfully registered a new User"); 
  }
  else{
      logStatus("TestCase 07", "User Registeration Failed", "Failure");
      takeScreenshot(driver, "TestCase 07 - Failure", "TestCase07 Failed to Register a new User");
  }
     Assert.assertTrue(status, "Failed to register new user");

     // Save the last generated username
     lastGeneratedUserName = registration.lastGeneratedUsername;

     // Visit the login page and login with the previuosly registered user
     Login login = new Login(driver);
     login.navigateToLoginPage();
     Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/login"), "Fail: Page has not been redirected to Login Page");
     takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 Successfully navigated to Login Page");

     status = login.PerformLogin(lastGeneratedUserName, "testUser@123");
     if(status){
      logStatus("TestCase 07", "User Login Successfully", "Success");
      takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 User Login Success");
  }
  else{
      logStatus("TestCase 07", "User Login Failed", "Failure");
      takeScreenshot(driver, "TestCase 07 - Failure", "TestCase07 User Login Failed");
  }
     Assert.assertTrue(status, "Failed to login with registered user");

    Home homePage = new Home(driver);
    homePage.navigateToHome();
    Assert.assertTrue(driver.getCurrentUrl().contains("https://crio-qkart-frontend-qa.vercel.app"), "Fail: Page has not been redirected to HomePage");
    takeScreenshot(driver, "TestCase 07 - Failure", "TestCase07- Successfully Redirected to HomePage");

    status = homePage.searchForProduct("Stylecon");
    if(status){
        logStatus("TestCase 07", "Product Searched Successfully", "Success");
        takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 Product Search Success");
    }
    else{
        logStatus("TestCase 07", "Product Search Unsuccessful", "Failure");
        takeScreenshot(driver, "TestCase 07 - Failed", "TestCase07 Product Search Unsuccessful");
    }
    Assert.assertTrue(status, "Unable to search for given product");

    status = homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");
    if(status){
        logStatus("TestCase 07", "Product Added to Cart Successfully", "Success");
        takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 Product added to Cart");
    }
    else{
        logStatus("TestCase 07", "Error Occured while Adding Product to Cart", "Failure");
        takeScreenshot(driver, "TestCase 07 - Failed", "TestCase07 Unable to add Product to Cart");
    }
    Assert.assertTrue(status, "Unable to Add Product To Cart");

   status = homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set ", 10);
   if(status){
    logStatus("TestCase 07", "Product Quantity changed Successfully", "Success");
    takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 Product Quantity changed inside cart");
}
else{
    logStatus("TestCase 07", "Error Occured while changing quantity in Cart", "Failure");
    takeScreenshot(driver, "TestCase 07 - Failure", "TestCase07 Product Quantity did not change inside cart");
}
Assert.assertTrue(status, "Unable to Change Quantity inside Cart");

    status = homePage.clickCheckout();
    if(status){
        logStatus("TestCase 07", "Sucess: Clicked On CheckOut Button", "Success");
        takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 Clicked on CheckOut button");
    }
    else{
        logStatus("TestCase 07", "Fail: Error Occured while clicking on CheckOut Button", "Failure");
        takeScreenshot(driver, "TestCase 07 - Failure", "TestCase07 Unable to click on CheckOut button");
    }
    Assert.assertTrue(status, "Unable to click on CheckOut Button");

    Checkout checkoutPage = new Checkout(driver);
    status = checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
    if(status){
        logStatus("TestCase 07", "Success: Added New Address on CheckOut Page", "Success");
        takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 Added New Address");
    }
    else{
        logStatus("TestCase 07", "Fail : Error Occured while Adding New Address on CheckOut Page", "Failure");
        takeScreenshot(driver, "TestCase 07 - Failure", "TestCase07 Unable to add New Address");
    }
    Assert.assertTrue(status, "Unable to Add New Address on CheckOut Page");

    status = checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
    if(status){
        logStatus("TestCase 07", "Success: Able to Select the Added Address on CheckOut Page", "Success");
        takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 Added Address gets selected");
    }
    else{
        logStatus("TestCase 07", "Fail : Error Occured while Selecting the Added Address on CheckOut Page", "Failure");
        takeScreenshot(driver, "TestCase 07 - Failure", "TestCase07 Unable to select New Added Address");
    }
    Assert.assertTrue(status, "Unable to Select the Added Address on CheckOut Page");

    checkoutPage.placeOrder();

    status = checkoutPage.verifyInsufficientBalanceMessage();

    if(status){
        logStatus("TestCase 07", "Success: Able to Verify Insufficient Balance Message", "Success");
        takeScreenshot(driver, "TestCase 07 - Success", "TestCase07 Verified Insufficient Balance Message");
    }
    else{
        logStatus("TestCase 07", "Fail : Error Occured while Verifying Insufficient Balance Message", "Failure");
        takeScreenshot(driver, "TestCase 07 - Failure", "TestCase07 Unable to verifying Insufficient Balance message");
    }
    Assert.assertTrue(status, "Unable to SVerify Insufficient Balance Message");


    logStatus("End TestCase",
            "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
            status ? "PASS" : "FAIL");
            takeScreenshot(driver, "TestCase 07 EndTestCase 07", "EndTestCase 07");
}

@Test(priority = 8,groups = {"Regression_Test" })
public void TestCase08() throws InterruptedException {
    Boolean status = false;

    logStatus("Start TestCase",
            "Test Case 8: Verify that product added to cart is available when a new tab is opened",
            "DONE");
// Visit the Registration page and register a new user
Register registration = new Register(driver);
registration.navigateToRegisterPage();
Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/register"), "Fail: Page has not been redirected to Register Page");
takeScreenshot(driver, "Start TestCase: TestCase 08 - Success", "TestCase08 Successfully navigated to RegisterPage");
       
status = registration.registerUser("testUser", "testUser@123", true);
if(status){
 logStatus("TestCase 08", "User Registeration Successfully", "Success");
 takeScreenshot(driver, "TestCase 08 - Success", "TestCase08 Successfully registered a new User");
}
else{
 logStatus("TestCase 08", "User Registeration Failed", "Failure");
 takeScreenshot(driver, "TestCase 08 - Failure", "TestCase08 Failed to Register a new User");
}
Assert.assertTrue(status, "Failed to register new user");

// Save the last generated username
lastGeneratedUserName = registration.lastGeneratedUsername;

// Visit the login page and login with the previuosly registered user
Login login = new Login(driver);
login.navigateToLoginPage();
Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/login"), "Fail: Page has not been redirected to Login Page");
takeScreenshot(driver, "TestCase 08 - Success", "TestCase08 Successfully navigated to Login Page");

status = login.PerformLogin(lastGeneratedUserName, "testUser@123");
if(status){
 logStatus("TestCase 08", "User Login Successfully", "Success");
 takeScreenshot(driver, "TestCase 08 - Success", "TestCase08 User Login Success");
}
else{
 logStatus("TestCase 08", "User Login Failed", "Failure");
 takeScreenshot(driver, "TestCase 08 - Failure", "TestCase08 User Login Failed");
}
Assert.assertTrue(status, "Failed to login with registered user");

    Home homePage = new Home(driver);
    homePage.navigateToHome();

    Assert.assertTrue(driver.getCurrentUrl().contains("https://crio-qkart-frontend-qa.vercel.app"), "Fail: Page has not been redirected to HomePage");
    takeScreenshot(driver, "TestCase 08 - Failure", "TestCase08- Successfully Redirected to HomePage");

    status = homePage.searchForProduct("YONEX");
    if(status){
        logStatus("TestCase 08", "Product Searched Successfully", "Success");
        takeScreenshot(driver, "TestCase 08 - Success", "TestCase08 Product Search Success");
    }
    else{
        logStatus("TestCase 08", "Product Search Unsuccessful", "Failure");
        takeScreenshot(driver, "TestCase 08 - Failed", "TestCase08 Product Search Unsuccessful");
    }
    Assert.assertTrue(status, "Unable to search for given product");

    homePage.addProductToCart("YONEX Smash Badminton Racquet");
    if(status){
        logStatus("TestCase 08", "Product Added to Cart Successfully", "Success");
        takeScreenshot(driver, "TestCase 08 - Success", "TestCase08 Product added to Cart");
    }
    else{
        logStatus("TestCase 08", "Error Occured while Adding Product to Cart", "Failure");
        takeScreenshot(driver, "TestCase 08 - Failed", "TestCase08 Unable to add Product to Cart");
    }
    Assert.assertTrue(status, "Unable to Add Product To Cart");

    String currentURL = driver.getCurrentUrl();

    driver.findElement(By.linkText("Privacy policy")).click();
    Set<String> handles = driver.getWindowHandles();
    driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

    driver.get(currentURL);
    Thread.sleep(2000);

    List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
    status = homePage.verifyCartContents(expectedResult);
    if(status){
        logStatus("TestCase 08", "Verification of Cart Contents Successful", "Success");
        takeScreenshot(driver, "TestCase 08 - Success", "TestCase08 Verified Cart contents");
    }
    else{
        logStatus("TestCase 08", "Error occured while verifying cart contents", "Failure");
        takeScreenshot(driver, "TestCase 08 - Failed", "TestCase08 Unable to verify cart contents");
    }
    Assert.assertTrue(status, "Unable to verify cart contents");

    driver.close();

    driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

    logStatus("End TestCase",
    "Test Case 8: Verify that product added to cart is available when a new tab is opened",
    status ? "PASS" : "FAIL");
    takeScreenshot(driver, "EndTestCase", "TestCase08");
}

@Test(priority = 9,groups = {"Regression_Test" })
public void TestCase09() throws InterruptedException {
    Boolean status = false;

    logStatus("Start TestCase",
            "Test Case 09: Verify that the Privacy Policy, About Us are displayed correctly ",
            "DONE");

// Visit the Registration page and register a new user
Register registration = new Register(driver);
registration.navigateToRegisterPage();
Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/register"), "Fail: Page has not been redirected to Register Page");
takeScreenshot(driver, "Start TestCase: TestCase 09 - Success", "TestCase09 Successfully navigated to RegisterPage");
       
status = registration.registerUser("testUser", "testUser@123", true);
if(status){
 logStatus("TestCase 09", "User Registeration Successfully", "Success");
 takeScreenshot(driver, "TestCase 09 - Success", "TestCase09 Successfully registered a new User"); 
}
else{
 logStatus("TestCase 09", "User Registeration Failed", "Failure");
 takeScreenshot(driver, "TestCase 09 - Failure", "TestCase09 Failed to Register a new User");
}
Assert.assertTrue(status, "Failed to register new user");

// Save the last generated username
lastGeneratedUserName = registration.lastGeneratedUsername;

// Visit the login page and login with the previuosly registered user
Login login = new Login(driver);
login.navigateToLoginPage();
Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/login"), "Fail: Page has not been redirected to Login Page");
takeScreenshot(driver, "TestCase 09 - Success", "TestCase09 Successfully navigated to Login Page");

status = login.PerformLogin(lastGeneratedUserName, "testUser@123");
if(status){
 logStatus("TestCase 09", "User Login Successfully", "Success");
 takeScreenshot(driver, "TestCase 09 - Success", "TestCase09 User Login Success");
}
else{
 logStatus("TestCase 09", "User Login Failed", "Failure");
 takeScreenshot(driver, "TestCase 09 - Failure", "TestCase09 User Login Failed");
}
Assert.assertTrue(status, "Failed to login with registered user");

    Home homePage = new Home(driver);
    homePage.navigateToHome();

    Assert.assertTrue(driver.getCurrentUrl().contains("https://crio-qkart-frontend-qa.vercel.app"), "Fail: Page has not been redirected to HomePage");
    takeScreenshot(driver, "TestCase 09 - Failure", "TestCase09- Successfully Redirected to HomePage");

    String basePageURL = driver.getCurrentUrl();

    driver.findElement(By.linkText("Privacy policy")).click();
    status = driver.getCurrentUrl().equals(basePageURL);

    Assert.assertTrue(status, "Verifying new tab opened has Terms Of Service page heading failed");


    if (!status) {
        logStatus("Step Failure", "Verifying parent page url didn't change on privacy policy link click failed", status ? "PASS" : "FAIL");
       // takeScreenshot(driver, "Failure", "TestCase09");
        logStatus("End TestCase",
                "Test Case 09: Verify that the Privacy Policy, About Us are displayed correctly ",
                status ? "PASS" : "FAIL");
    }
    
    Set<String> handles = driver.getWindowHandles();
    driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
    WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
    status = PrivacyPolicyHeading.getText().equals("Privacy Policy");

    Assert.assertTrue(status, "Verifying new tab opened has Privacy Policy page heading failed");

    if (!status) {
        logStatus("Step Failure", "Verifying new tab opened has Privacy Policy page heading failed", status ? "PASS" : "FAIL");
       // takeScreenshot(driver, "Failure", "TestCase9");
        logStatus("End TestCase",
                "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly ",
                status ? "PASS" : "FAIL");
    }

    driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
    driver.findElement(By.linkText("Terms of Service")).click();

    handles = driver.getWindowHandles();
    driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
    WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
    status = TOSHeading.getText().equals("Terms of Service");
    if (!status) {
        logStatus("Step Failure", "Verifying new tab opened has Terms Of Service page heading failed", status ? "PASS" : "FAIL");
      //  takeScreenshot(driver, "Failure", "TestCase9");
        logStatus("End TestCase",
                "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly ",
                status ? "PASS" : "FAIL");
    }

    driver.close();
    driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
    driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

    logStatus("End TestCase",
    "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly ",
    "PASS");
    takeScreenshot(driver, "EndTestCase", "TestCase09");
}

@Test(priority = 10,groups = { "Sanity_test" })
public void TestCase10() throws InterruptedException {
    logStatus("Start TestCase",
            "Test Case 10: Verify that contact us option is working correctly ",
            "DONE");
    //takeScreenshot(driver, "StartTestCase", "TestCase10");

    Home homePage = new Home(driver);
    homePage.navigateToHome();

    driver.findElement(By.xpath("//*[text()='Contact us']")).click();

    WebDriverWait openContactUS = new WebDriverWait(driver, Duration.ofSeconds(30));
    openContactUS.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@class='text-center contact-us']")));

    takeScreenshot(driver, "TestCase 10 - Success", "TestCase10- Contact Us Element Displayed");

    WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
    name.sendKeys("crio user");
    WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
    email.sendKeys("criouser@gmail.com");
    WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
    message.sendKeys("Testing the contact us page");

    WebElement contactUs = driver.findElement(
            By.xpath("//div[@class='col-md-12']//button"));

            takeScreenshot(driver, "TestCase 10 - Success", "TestCase10- Details Filled at Contact Us");

    contactUs.click();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    wait.until(ExpectedConditions.invisibilityOf(contactUs));

    logStatus("End TestCase",
            "Test Case 10: Verify that contact us option is working correctly ",
            "PASS");

    takeScreenshot(driver, "EndTestCase", "TestCase10");
}

@Test(priority = 11,groups = { "Sanity_test" })
public void TestCase11() throws InterruptedException {
    Boolean status = false;
    logStatus("Start TestCase",
            "Test Case 11: Ensure that the links on the QKART advertisement are clickable",
            "DONE");

    // Visit the Registration page and register a new user
    Register registration = new Register(driver);
    registration.navigateToRegisterPage();
    Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/register"), "Fail: Page has not been redirected to Register Page");
    takeScreenshot(driver, "Start TestCase: TestCase 11 - Success", "TestCase01 Successfully navigated to RegisterPage");
    
    status = registration.registerUser("testUser", "testUser@123", true);
    if(status){
     logStatus("TestCase 11", "User Registeration Successfully", "Success");
     takeScreenshot(driver, "TestCase 11 - Success", "TestCase11 Successfully registered a new User"); 
 }
 else{
     logStatus("TestCase 11", "User Registeration Failed", "Failure");
     takeScreenshot(driver, "TestCase 11 - Failure", "TestCase11 Failed to Register a new User");
 }
    Assert.assertTrue(status, "Failed to register new user");

    // Save the last generated username
    lastGeneratedUserName = registration.lastGeneratedUsername;

    // Visit the login page and login with the previuosly registered user
    Login login = new Login(driver);
    login.navigateToLoginPage();
    Assert.assertTrue(driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/login"), "Fail: Page has not been redirected to Login Page");
    takeScreenshot(driver, "TestCase 11 - Success", "TestCase11 Successfully navigated to Login Page");

    status = login.PerformLogin(lastGeneratedUserName, "testUser@123");
    if(status){
     logStatus("TestCase 11", "User Login Successfully", "Success");
     takeScreenshot(driver, "TestCase 11 - Success", "TestCase11 User Login Success");
 }
 else{
     logStatus("TestCase 11", "User Login Failed", "Failure");
     takeScreenshot(driver, "TestCase 11 - Failure", "TestCase11 User Login Failed");
 }
    Assert.assertTrue(status, "Failed to login with registered user");

    Home homePage = new Home(driver);
    homePage.navigateToHome();

    Assert.assertTrue(driver.getCurrentUrl().contains("https://crio-qkart-frontend-qa.vercel.app"), "Fail: Page has not been redirected to HomePage");
    takeScreenshot(driver, "TestCase 11 - Failure", "TestCase11- Successfully Redirected to HomePage");

    status = homePage.searchForProduct("YONEX Smash Badminton Racquet");
    if(status){
        logStatus("TestCase 11", "Product Searched Successfully", "Success");
        takeScreenshot(driver, "TestCase 11 - Success", "TestCase11 Product Search Success");
    }
    else{
        logStatus("TestCase 11", "Product Search Unsuccessful", "Failure");
        takeScreenshot(driver, "TestCase 11 - Failed", "TestCase11 Product Search Unsuccessful");
    }
    Assert.assertTrue(status, "Unable to search for given product");

    status = homePage.addProductToCart("YONEX Smash Badminton Racquet");
    if(status){
        logStatus("TestCase 11", "Product Added to Cart Successfully", "Success");
        takeScreenshot(driver, "TestCase 11 - Success", "TestCase11 Product added to Cart");
    }
    else{
        logStatus("TestCase 11", "Error Occured while Adding Product to Cart", "Failure");
        takeScreenshot(driver, "TestCase 11 - Failed", "TestCase11 Unable to add Product to Cart");
    }
    Assert.assertTrue(status, "Unable to Add Product To Cart");

    status =homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        if(status){
        logStatus("TestCase 11", "Product Quantity changed Successfully", "Success");
        takeScreenshot(driver, "TestCase 11 - Success", "TestCase11 Product Quantity changed Successfully");
    }
    else{
        logStatus("TestCase 11", "Error Occured while changing quantity in Cart", "Failure");
        takeScreenshot(driver, "TestCase 11 - Failure", "TestCase11 Unable to change Product quantity in Cart");
    }
    Assert.assertTrue(status, "Unable to Change Quantity inside Cart");
    status = homePage.clickCheckout();
    if(status){
        logStatus("TestCase 11", "Sucess: Clicked On CheckOut Button", "Success");
        takeScreenshot(driver, "TestCase 11 - Success", "TestCase11 Clicked on CheckOut button");
    }
    else{
        logStatus("TestCase 11", "Fail: Error Occured while clicking on CheckOut Button", "Failure");
        takeScreenshot(driver, "TestCase 11 - Failure", "TestCase11 Unable to click on CheckOut button");
    }
    Assert.assertTrue(status, "Unable to click on CheckOut Button");

    Checkout checkoutPage = new Checkout(driver);
    checkoutPage.addNewAddress("Addr line 1  addr Line 2  addr line 3");
    if(status){
        logStatus("TestCase 11", "Success: Added New Address on CheckOut Page", "Success");
        takeScreenshot(driver, "TestCase 11 - Success", "TestCase11 Added New Address");
    }
    else{
        logStatus("TestCase 11", "Fail : Error Occured while Adding New Address on CheckOut Page", "Failure");
        takeScreenshot(driver, "TestCase 11 - Failure", "TestCase11 Unable to add New Address");
    }
    Assert.assertTrue(status, "Unable to Add New Address on CheckOut Page");

    checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
    if(status){
        logStatus("TestCase 11", "Success: Able to Select the Added Address on CheckOut Page", "Success");
        takeScreenshot(driver, "TestCase 11 - Success", "TestCase11 Added Address gets selected");
    }
    else{
        logStatus("TestCase 11", "Fail : Error Occured while Selecting the Added Address on CheckOut Page", "Failure");
        takeScreenshot(driver, "TestCase 11 - Failure", "TestCase11 Unable to select New Added Address");
    }
    checkoutPage.placeOrder();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

    // Check if placing order redirected to the Thansk page
    status = driver.getCurrentUrl().endsWith("/thanks");

    Assert.assertTrue(driver.getCurrentUrl().endsWith("/thanks"), "Unable to redirect to Order Placed Page");
    takeScreenshot(driver, "TestCase 11 - Success", "TestCase11- Successfully Redirected to Confirmation Page");

    String currentURL = driver.getCurrentUrl();

    List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

    status = Advertisements.size() == 3;
    logStatus("Step ", "Verify that 3 Advertisements are available", status ? "PASS" : "FAIL");

    WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
    driver.switchTo().frame(Advertisement1);
    driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
    driver.switchTo().parentFrame();

    status = !driver.getCurrentUrl().equals(currentURL);
    logStatus("Step ", "Verify that Advertisement 1 is clickable ", status ? "PASS" : "FAIL");

    driver.get(currentURL);
    Thread.sleep(3000);

    WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
    driver.switchTo().frame(Advertisement2);
    driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
    driver.switchTo().parentFrame();

    status = !driver.getCurrentUrl().equals(currentURL);
    logStatus("Step ", "Verify that Advertisement 2 is clickable ", status ? "PASS" : "FAIL");

    logStatus("End TestCase",
            "Test Case 11:  Ensure that the links on the QKART advertisement are clickable",
            status ? "PASS" : "FAIL");
            takeScreenshot(driver, "EndTestCase", "EndTestCase 11");
}

  @AfterSuite(alwaysRun = true)
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }

    public static String takeScreenshot(WebDriver driver, String screenshotType, String description) {
        String timestamp = String.valueOf(java.time.LocalDateTime.now());
        timestamp = timestamp.replace(":", "_");
        String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);
    
        File srcFile =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try{
        String fullPath = "D:\\Automation Crio projects\\Qkart\\johnsaj97-ME_QKART_QA_V2-master\\app\\screenshots";
        File desFile = new File(fullPath + File.separator + fileName + ".png");
        FileUtils.copyFile(srcFile,desFile);
        String errFilePath = desFile.getAbsolutePath();
        return errFilePath;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

}
}

