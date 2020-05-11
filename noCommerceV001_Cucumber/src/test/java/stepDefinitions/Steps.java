package stepDefinitions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.By;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import cucumber.api.java.Before;
//import cucumber.api.java.en.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pageObjects.AddcustomerPage;
import pageObjects.LoginPage;
import pageObjects.SearchCustomerPage;

public class Steps extends BaseClass{
	
	@Before
	public void setup() throws IOException
	{
		logger=logger.getLogger("nopCommerce");// Project name// Added logger
		PropertyConfigurator.configure("log4j.properties");
		//path of the file or file name-into the project// Added logger
		
		//Reading properties file- Create object
		configProp=new Properties();
		
		//For load config properties file-
		FileInputStream configProfile=new FileInputStream("config.properties");//open config pro file
		configProp.load(configProfile);//load config properties file
		
		String br=configProp.getProperty("browser");
		
		if(br.equals("chrome"))
		{
		System.setProperty("webdriver.chrome.driver",configProp.getProperty("chromepath"));
		driver=new ChromeDriver();
		}
		else if (br.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver",configProp.getProperty("firefoxpath"));
			driver = new FirefoxDriver();
		}
		else if (br.equals("ie")) {
			System.setProperty("webdriver.ie.driver",configProp.getProperty("iepath"));
			driver = new InternetExplorerDriver();
		}
		
		logger.info("******** Launching browser*********");
	}

	
	@Given("User Launch Chrome browser")
	public void user_Launch_Chrome_browser() {		
		
		lp=new LoginPage(driver);
	   
	}

	@When("User opens URL {string}")
	public void user_opens_URL(String url) {
		logger.info("************** Opening URL ********************");
		driver.get(url);
		driver.manage().window().maximize();
	    
	}

	@When("User enters Email as {string} and Password as {string}")
	public void user_enters_Email_as_and_Password_as(String email, String password) {
		logger.info("************** Providing login details ********************");
		lp.setUserName(email);
		lp.setPassword(password);
	    
	}

	@When("Click on Login")
	public void click_on_Login() throws InterruptedException {
		logger.info("************** Started login ********************");
	    lp.clickLogin();
	    Thread.sleep(3000);
	}

	@Then("Page Title should be {string}")
	public void page_Title_should_be(String title) throws InterruptedException {
		//for invaild data- unsuccessful - message
		
		if (driver.getPageSource().contains("Login was unsuccessful.")) {
			driver.close();
			logger.info("************** Login passed ********************");
			Assert.assertTrue(false);
			
		} else {
			logger.info("************** Login failed ********************");
			
			Assert.assertEquals(title, driver.getTitle());
		}
		Thread.sleep(3000);
		
	    
	}

	@When("User click on Log out link")
	public void user_click_on_Log_out_link() throws InterruptedException {
		logger.info("************** Click on logout link ********************");
		lp.clickLogout();
		Thread.sleep(3000);
	}

	@Then("close browser")
	public void close_browser() {
		logger.info("************** Closing browser ********************");
		driver.quit();
	    
	}
	
	//Customer feature Step definition......
	
		@Then("User can view Dashboard")
		public void user_can_view_Dashboard() {
		addCust=new AddcustomerPage(driver);
		Assert.assertEquals("Dashboard / nopCommerce administration",addCust.getPageTitle());
	}

	@When("User click on customers Menu")
	public void user_click_on_customers_Menu() throws InterruptedException {
		Thread.sleep(3000);
		addCust.clickOnCustomersMenu();
	}

	@When("click on customers Menu Item")
	public void click_on_customers_Menu_Item() throws InterruptedException {
		Thread.sleep(2000);
	   addCust.clickOnCustomersMenuItem();
	}

	@When("click on Add new button")
	public void click_on_Add_new_button() throws InterruptedException {
	   addCust.clickOnAddnew();
	   Thread.sleep(2000);
	}

	@Then("User can view Add new customer page")
	public void user_can_view_Add_new_customer_page() {
	  Assert.assertEquals("Add a new customer / nopCommerce administration", addCust.getPageTitle());
	}

	@When("User enter customer info")
	public void user_enter_customer_info() throws InterruptedException {
		
		logger.info("************** Adding new customer**********************");
		logger.info("************** Providing details**********************");
		String email = randomestring() + "@gmail.com";
		addCust.setEmail(email);
		addCust.setPassword("test123");
		// Registered - default
		// The customer cannot be in both 'Guests' and 'Registered' customer roles
		// Add the customer to 'Guests' or 'Registered' customer role
		addCust.setCustomerRoles("Guest");
		Thread.sleep(3000);

		addCust.setManagerOfVendor("Vendor 2");
		addCust.setGender("Male");
		addCust.setFirstName("Pavan");
		addCust.setLastName("Kumar");
		addCust.setDob("7/05/1985"); // Format: D/MM/YYY
		addCust.setCompanyName("busyQA");
		addCust.setAdminContent("This is for testing.........");
	}

	@When("click on Save button")
	public void click_on_Save_button() throws InterruptedException {
		logger.info("************** Saving Customer data**********************");
		addCust.clickOnSave();
	   Thread.sleep(2000);
	}

	@Then("User can view confirmation message {string}")
	public void user_can_view_confirmation_message(String msg) {
		Assert.assertTrue(driver.findElement(By.tagName("body")).getText()
				.contains("The new customer has been added successfully"));
	}

	
	//steps for searching a customer using Email ID.........
	
	
	@When("Enter customer EMail")
	public void enter_customer_EMail() {
		logger.info("************** Searching customer by email id**********************");
		searchCust=new SearchCustomerPage(driver);
		searchCust.setEmail("victoria_victoria@nopCommerce.com");
	   
	}

	@When("Click on search button")
	public void click_on_search_button() throws InterruptedException {
		searchCust.clickSearch();
		Thread.sleep(3000);
	    
	}

	@Then("User should found Email in the Search table")
	public void user_should_found_Email_in_the_Search_table() {
		boolean status=searchCust.searchCustomerByEmail("victoria_victoria@nopCommerce.com");
	   	Assert.assertEquals(true, status);
	   
	}
	
	//steps for searching a customer by using First Name & Lastname
		@When("Enter customer FirstName")
		public void enter_customer_FirstName() {
			logger.info("************** Searching customer by name **********************");
			searchCust=new SearchCustomerPage(driver);
			searchCust.setFirstName("Victoria");
		}

		@When("Enter customer LastName")
		public void enter_customer_LastName() {
			searchCust.setLastName("Terces");
		}

		@Then("User should found Name in the Search table")
		public void user_should_found_Name_in_the_Search_table() {
			boolean status=searchCust.searchCustomerByName("Victoria Terces");
			Assert.assertEquals(true, status);
		}	
	    
	}


		
