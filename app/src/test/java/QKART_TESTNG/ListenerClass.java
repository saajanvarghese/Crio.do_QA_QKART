package QKART_TESTNG;

import org.testng.ITestListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener{

public void onTestFailure(ITestResult result){

    System.out.println("Taking Screenshot for : "+result.getName());
}

public void onTestSuccess(ITestResult result){

    System.out.println("Test Case is Successful ##################### : "+result.getName());
}

public void onStart(ITestContext context){

    System.out.println("Test Cases Execution Started ##################### : ");
}


}