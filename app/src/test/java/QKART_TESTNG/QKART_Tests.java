package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;
import io.github.bonigarcia.wdm.WebDriverManager;
import static org.testng.Assert.*;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

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
    /*
     * Testcase01: Verify a new user can successfully register
     */
    @Test(priority = 1,groups = { "Sanity_test" })
    public void TestCase01() throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");
      //  takeScreenshot(driver, "StartTestCase", "TestCase1");

       // Visit the Registration page and register a new user
       Register registration = new Register(driver);
       registration.navigateToRegisterPage();
       status = registration.registerUser("testUser", "testUser@123", true);
       Assert.assertTrue(status, "Failed to register new user");

       // Save the last generated username
       lastGeneratedUserName = registration.lastGeneratedUsername;

       // Visit the login page and login with the previuosly registered user
       Login login = new Login(driver);
       login.navigateToLoginPage();
       status = login.PerformLogin(lastGeneratedUserName, "testUser@123");
       Assert.assertTrue(status, "Failed to login with registered user");
       logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
       Assert.assertTrue(status, "Failed to login with registered user");

       // Visit the home page and log out the logged in user
       Home home = new Home(driver);
       status = home.PerformLogout();

        logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status
        ? "PASS" : "FAIL");
       //  takeScreenshot(driver, "EndTestCase", "TestCase1");
   }


@Test(priority = 2 ,groups = { "Sanity_test" })
   public void TestCase02() throws InterruptedException {
    Boolean status;
    logStatus("Start Testcase", "Test Case 2: Verify User Registration with an existing username ", "DONE");

    // Visit the Registration page and register a new user
    Register registration = new Register(driver);
    registration.navigateToRegisterPage();
   // Thread.sleep(3000);
    status = registration.registerUser("testUser", "abc@123", true);
    logStatus("Test Step", "User Registration : ", status ? "PASS" : "FAIL");
    Assert.assertTrue(status, "Unable to Verify user registeration");
    // if (!status) {
    //     logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "PASS" : "FAIL");
    //     return false;
    // }

    // Save the last generated username
    lastGeneratedUserName = registration.lastGeneratedUsername;

    // Visit the Registration page and try to register using the previously
    // registered user's credentials
    registration.navigateToRegisterPage();
    status = registration.registerUser(lastGeneratedUserName, "abc@123", false);
    Assert.assertFalse(status, "Unable to verify user registeration");
    Thread.sleep(2000);
    // If status is true, then registration succeeded, else registration has
    // failed. In this case registration failure means Success
    logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "FAIL" : "PASS");
   
    // return !status;
  
}

@Test(priority = 3 ,groups = { "Sanity_test" })
public void TestCase03() throws InterruptedException {
    logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product
        status = homePage.searchForProduct("YONEX");
        Assert.assertTrue(status, "Unable to search for given product");
        // if (!status) {
        //     logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");
        //     return false;
        // }

        Thread.sleep(3000);

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available
        if (searchResults.size() == 0) {
            logStatus("TestCase 3", "Test Case Failure. There were no results for the given search string", "FAIL");
           
            //return status;
        }

        for(WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            if (!elementText.toUpperCase().contains("YONEX")) {
                logStatus("TestCase 3", "Test Case Failure. Test Results contains un-expected values: " + elementText,
                        "FAIL");
               // return false;
            }
        }

        logStatus("Step Success", "Successfully validated the search results ", "PASS");

        // Search for product
        status = homePage.searchForProduct("Gesundheit");
        System.out.println(status);
        Assert.assertTrue(status, "Invalid keyword returned results");

        // if (status == false) {
        //     logStatus("TestCase 3", "Test Case Failure. Invalid keyword returned results", "FAIL");
        //    // return false;
        // }
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
          //  return false;
        }
       // return true;
    }

@Test(priority = 4,groups = {"Regression_Test" })
public void TestCase04() throws InterruptedException {
    logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
    boolean status = false;

    // Visit home page
    Home homePage = new Home(driver);
    homePage.navigateToHome();

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

       // webElement.getSize();
        System.out.println(searchResults.size());


        status = result.verifySizeChartExists();
        System.out.println(status);
            logStatus("Step Success", "Verifivation: Verify if Size chart link exists", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Size Chart Link Does not Exists");

            // Verify if size dropdown exists
            status = result.verifyExistenceofSizeDropdown(driver);
            System.out.println(status);
            logStatus("Step Success", "Verification:  presence of Size drop down", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Error occured while validating Size Drop Down");

            //Check if Size Chart Link Opens
            status = result.openSizechart();
            System.out.println(status);
            logStatus("Step Success", "Verification:  presence of Size drop down", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Error occured while opening Size Chart Link");

            //Validate SizeChart Contents
            status = result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver);
            System.out.println(status);
            logStatus("Step Success", "Verification: Validated contents inside Size Chart link", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Error occured while opening Size Chart Link");

            //Verify if SizeChart Link Closes
            status = result.closeSizeChart(driver);
            System.out.println(status);
            logStatus("Step Success", "Verification: Closing of Size Chart link", status ? "PASS" : "FAIL");
            Assert.assertTrue(status, "Failed: Error occured while closing Size Chart Link");
        }

        logStatus("Step Success", "Successfully Validated TestCase04", "PASS");
    }


@Test(priority = 5,groups = {"Sanity_test" })
public void TestCase05() throws InterruptedException {
    Boolean status;
    logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");

    // Go to the Register page
    Register registration = new Register(driver);
    registration.navigateToRegisterPage();

    // Register a new user
    status = registration.registerUser("testUser", "abc@123", true);
    if (!status) {
        logStatus("TestCase 5", "Test Case Failure. Happy Flow Test Failed", "FAIL");
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
    }

    // Go to the home page
    Home homePage = new Home(driver);
    homePage.navigateToHome();

    // Find required products by searching and add them to the user's cart
    status = homePage.searchForProduct("YONEX");
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

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

    // Check if placing order redirected to the Thansk page
    status = driver.getCurrentUrl().endsWith("/thanks");

    // Go to the home page
    homePage.navigateToHome();

    // Log out the user
    homePage.PerformLogout();

    logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
    //return status;
}

@Test(priority = 6,groups = {"Regression_Test" })
public void TestCase06() throws InterruptedException {
    Boolean status;
    logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
    Home homePage = new Home(driver);
    Register registration = new Register(driver);
    Login login = new Login(driver);

    registration.navigateToRegisterPage();
    status = registration.registerUser("testUser", "abc@123", true);
    if (!status) {
        logStatus("Step Failure", "User Perform Register Failed", status ? "PASS" : "FAIL");
        logStatus("End TestCase", "Test Case 6:  Verify that cart can be edited: ", status ? "PASS" : "FAIL");
        //return false;
    }
    lastGeneratedUserName = registration.lastGeneratedUsername;

    login.navigateToLoginPage();
    status = login.PerformLogin(lastGeneratedUserName, "abc@123");
    if (!status) {
        logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        logStatus("End TestCase", "Test Case 6:  Verify that cart can be edited: ", status ? "PASS" : "FAIL");
        //return false;
    }

    homePage.navigateToHome();
    status = homePage.searchForProduct("Xtend");
    homePage.addProductToCart("Xtend Smart Watch");

    status = homePage.searchForProduct("Yarine");
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

    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
    } catch (TimeoutException e) {
        System.out.println("Error while placing order in: " + e.getMessage());
        //return false;
    }

    status = driver.getCurrentUrl().endsWith("/thanks");

    homePage.navigateToHome();
    homePage.PerformLogout();

    logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ? "PASS" : "FAIL");
    //return status;
}


@Test(priority = 7,groups = {"Regression_Test" })
public void TestCase07() throws InterruptedException {
    Boolean status;
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
        //return false;
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
        //return false;
    }

    Home homePage = new Home(driver);
    homePage.navigateToHome();
    status = homePage.searchForProduct("Stylecon");
    homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");

    homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set ", 10);

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

    //return status;
}

@Test(priority = 8,groups = {"Regression_Test" })
public void TestCase08() throws InterruptedException {
    Boolean status = false;

    logStatus("Start TestCase",
            "Test Case 8: Verify that product added to cart is available when a new tab is opened",
            "DONE");
    //takeScreenshot(driver, "StartTestCase", "TestCase09");

    Register registration = new Register(driver);
    registration.navigateToRegisterPage();
    status = registration.registerUser("testUser", "abc@123", true);
    if (!status) {
        logStatus("TestCase 8",
                "Test Case Failure. Verify that product added to cart is available when a new tab is opened",
                "FAIL");
        //takeScreenshot(driver, "Failure", "TestCase09");
    }
    lastGeneratedUserName = registration.lastGeneratedUsername;

    Login login = new Login(driver);
    login.navigateToLoginPage();
    status = login.PerformLogin(lastGeneratedUserName, "abc@123");
    if (!status) {
        logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        //takeScreenshot(driver, "Failure", "TestCase9");
        logStatus("End TestCase",
                "Test Case 8:   Verify that product added to cart is available when a new tab is opened",
                status ? "PASS" : "FAIL");
    }

    Home homePage = new Home(driver);
    homePage.navigateToHome();

    status = homePage.searchForProduct("YONEX");
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
    "Test Case 8: Verify that product added to cart is available when a new tab is opened",
    status ? "PASS" : "FAIL");
    //takeScreenshot(driver, "EndTestCase", "TestCase08");

    //return status;
}

@Test(priority = 9,groups = {"Regression_Test" })
public void TestCase09() throws InterruptedException {
    Boolean status = false;

    logStatus("Start TestCase",
            "Test Case 09: Verify that the Privacy Policy, About Us are displayed correctly ",
            "DONE");
    //takeScreenshot(driver, "StartTestCase", "TestCase09");

    Register registration = new Register(driver);
    registration.navigateToRegisterPage();
    status = registration.registerUser("testUser", "abc@123", true);
    if (!status) {
        logStatus("TestCase 09",
                "Test Case Failure.  Verify that the Privacy Policy, About Us are displayed correctly ",
                "FAIL");
        //takeScreenshot(driver, "Failure", "TestCase09");
    }
    lastGeneratedUserName = registration.lastGeneratedUsername;

    Login login = new Login(driver);
    login.navigateToLoginPage();
    status = login.PerformLogin(lastGeneratedUserName, "abc@123");
    if (!status) {
        logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
       // takeScreenshot(driver, "Failure", "TestCase09");
        logStatus("End TestCase",
                "Test Case 9:    Verify that the Privacy Policy, About Us are displayed correctly ",
                status ? "PASS" : "FAIL");
    }

    Home homePage = new Home(driver);
    homePage.navigateToHome();

    String basePageURL = driver.getCurrentUrl();

    driver.findElement(By.linkText("Privacy policy")).click();
    status = driver.getCurrentUrl().equals(basePageURL);

    if (!status) {
        logStatus("Step Failure", "Verifying parent page url didn't change on privacy policy link click failed", status ? "PASS" : "FAIL");
       // takeScreenshot(driver, "Failure", "TestCase09");
        logStatus("End TestCase",
                "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly ",
                status ? "PASS" : "FAIL");
    }

    Set<String> handles = driver.getWindowHandles();
    driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
    WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
    status = PrivacyPolicyHeading.getText().equals("Privacy Policy");
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
   // takeScreenshot(driver, "EndTestCase", "TestCase9");

    //return status;
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

    WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
    name.sendKeys("crio user");
    WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
    email.sendKeys("criouser@gmail.com");
    WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
    message.sendKeys("Testing the contact us page");

    WebElement contactUs = driver.findElement(
            By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));

    contactUs.click();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    wait.until(ExpectedConditions.invisibilityOf(contactUs));

    logStatus("End TestCase",
            "Test Case 10: Verify that contact us option is working correctly ",
            "PASS");

   // takeScreenshot(driver, "EndTestCase", "TestCase10");

    //return true;
}

@Test(priority = 11,groups = { "Sanity_test" })
public void TestCase11() throws InterruptedException {
    Boolean status = false;
    logStatus("Start TestCase",
            "Test Case 11: Ensure that the links on the QKART advertisement are clickable",
            "DONE");
   // takeScreenshot(driver, "StartTestCase", "TestCase11");

    Register registration = new Register(driver);
    registration.navigateToRegisterPage();
    status = registration.registerUser("testUser", "abc@123", true);
    if (!status) {
        logStatus("TestCase 11",
                "Test Case Failure. Ensure that the links on the QKART advertisement are clickable",
                "FAIL");
        //takeScreenshot(driver, "Failure", "TestCase11");
    }
    lastGeneratedUserName = registration.lastGeneratedUsername;

    Login login = new Login(driver);
    login.navigateToLoginPage();
    status = login.PerformLogin(lastGeneratedUserName, "abc@123");
    if (!status) {
        logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        //takeScreenshot(driver, "Failure", "TestCase 11");
        logStatus("End TestCase",
                "Test Case 11:  Ensure that the links on the QKART advertisement are clickable",
                status ? "PASS" : "FAIL");
    }

    Home homePage = new Home(driver);
    homePage.navigateToHome();

    status = homePage.searchForProduct("YONEX Smash Badminton Racquet");
    homePage.addProductToCart("YONEX Smash Badminton Racquet");
    homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
    homePage.clickCheckout();

    Checkout checkoutPage = new Checkout(driver);
    checkoutPage.addNewAddress("Addr line 1  addr Line 2  addr line 3");
    checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
    checkoutPage.placeOrder();
    Thread.sleep(3000);

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
    //return status;
}
        
    


    @AfterSuite
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }


//     public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
//         try {
//             File theDir = new File("/screenshots");
//             if (!theDir.exists()) {
//                 theDir.mkdirs();
//             }
//             String timestamp = String.valueOf(java.time.LocalDateTime.now());
//             String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);
//             TakesScreenshot scrShot = ((TakesScreenshot) driver);
//             File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
//             File DestFile = new File("screenshots/" + fileName);
//             FileUtils.copyFile(SrcFile, DestFile);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
 }

