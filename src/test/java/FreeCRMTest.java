import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import junit.framework.Assert;

public class FreeCRMTest {


	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest logger;
	
	
	
	
	@BeforeTest
	public void SetExtent() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/extentReport.html",true);
		extent.addSystemInfo("Host Name","Windows10");
		extent.addSystemInfo("Environmnet","QA");
		extent.addSystemInfo("UserName","Parth Kandpal");
	}
	
	
	
	@AfterTest
	
	public void endReport() {
		extent.flush();
		extent.close();
	}
	
	public static String getScreenshot(WebDriver driver, String ScreenshotName) throws IOException {
		
		String Datename = new SimpleDateFormat("yyMMddhhmmss").format(new Date());
		String destination= System.getProperty("user.home") + "/FailedTestScreenshots"+ ScreenshotName + Datename;
		
		File SS= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		File FinalDestination = new File(destination);
		FileUtils.copyFile(SS,FinalDestination);
		
		
		return destination;
	}
	
	
	
	
	@BeforeMethod	
	public void setUp() {
	
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\PARTH KANDPAL\\Downloads\\chromedriver\\chromedriver.exe");	
		driver = new ChromeDriver(); 

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.get("https://classic.crmpro.com/");
		
	}
	
	@Test
	public void FreeCRMTitleTest() {
		
		logger = extent.startTest("freeCrmTitleTest");
		String Title= driver.getTitle();
		Assert.assertEquals(Title, "CRMPRO - CRM software for customer relationship management, sales, and support.1");
		
	}
	@Test
	public void freemCRMLogoTest(){
		logger = extent.startTest("freemCRMLogoTest");
		boolean b = driver.findElement(By.xpath("//a[@href='https://classic.freecrm.com/index.html']/img[@class='img-responsive']")).isDisplayed();
		Assert.assertTrue(b);
	}
	
	
	@AfterMethod
	
	public void tearDown(ITestResult result) throws IOException {
		if(result.getStatus()==ITestResult.FAILURE){
			
			// If test case fails 
			logger.log(LogStatus.FAIL, "Test failed"+ result.getName());		// to add test case name
			logger.log(LogStatus.FAIL, "Test failed"+ result.getThrowable());	//to add error thrown by failed test case
			
			
			String Screenshotpath=FreeCRMTest.getScreenshot(driver, result.getName()); // setting ss path to testcase name
			
			
			logger.log(LogStatus.FAIL, logger.addScreenCapture(Screenshotpath));		// adding Screenshot to extentreport
			
			
		}
		else if(result.getStatus()==ITestResult.SKIP) {
			logger.log(LogStatus.SKIP, "Test Skipped"+ result.getName());		// to add test case name
			logger.log(LogStatus.SKIP, "Test Skipped"+ result.getThrowable());	//to add error thrown by Skipped test case
			
			
			String Screenshotpath=FreeCRMTest.getScreenshot(driver, result.getName()); // setting ss path to testcase name
			
			
			logger.log(LogStatus.SKIP, logger.addScreenCapture(Screenshotpath));		// adding Screenshot to extentreport
			
			
		}else if(result.getStatus()==ITestResult.SUCCESS) {
			logger.log(LogStatus.PASS, "Test Passed"+ result.getName());		// to add test case name
		
		}
			
		extent.endTest(logger);   //ending test and prepare to create HTML Report
		
		driver.quit();
			
	}
}
