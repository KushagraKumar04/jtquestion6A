package com.example;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;

public class FlipkartLoginTest {

    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/sande/Downloads/chromedriver-win64 (1)/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.facebook.com");


    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password) {
        WebElement userField = driver.findElement(By.cssSelector("input[data-testid$='email']"));
        WebElement passField = driver.findElement(By.cssSelector("input[data-testid$='pass']"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[data-testid='royal_login_button']"));

        userField.clear();
        userField.sendKeys(username);
        passField.clear();
        passField.sendKeys(password);
        loginButton.click();

        // Check for login success or failure
        try {
            WebElement myAccount = driver.findElement(By.cssSelector("input[name='q']"));
            Assert.assertTrue(myAccount.isDisplayed(), "Login should succeed, but it did not.");
        } catch (Exception e) {
            WebElement errorMessage = driver.findElement(By.cssSelector("span._2YULOR"));
            Assert.assertTrue(errorMessage.isDisplayed(), "Login failed, but no error message was displayed.");
        }
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() throws IOException {
        FileInputStream fis = new FileInputStream("LoginData.xlsx");
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getPhysicalNumberOfRows();
        Object[][] data = new Object[rowCount][2];

        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            data[i][0] = row.getCell(0).getStringCellValue();
            data[i][1] = row.getCell(1).getStringCellValue();
        }

        workbook.close();
        fis.close();
        return data;
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
