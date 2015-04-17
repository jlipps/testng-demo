package com.saucelabs;

/**
 * @author Neil Manvar
 */

import org.testng.annotations.Test;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;

import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.saucelabs.saucerest.SauceREST;



public class TestNGWikipediaDemoBis {
    class Ctx {
        public WebDriver driver;
        public String jobId;
        public boolean passed;
    }
    /*
    String username = "random";
    String accessKey = "iforgot";
    */

    private Map<String, String> env = System.getenv();
    private String username = env.get("SAUCE_USERNAME");
    private String accessKey = env.get("SAUCE_ACCESS_KEY");
    private Random randomGenerator = new Random();
    private SauceREST sauceREST = new SauceREST(username, accessKey);

    private ThreadLocal threadLocal = new ThreadLocal();

    @BeforeMethod
    public void setUp(Method method) throws Exception {
        if(threadLocal.get() == null) threadLocal.set(new Ctx());
        Ctx ctx = (Ctx) threadLocal.get();
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("name", "TestNGWikipediaDemoBis - " + method.getName());
        ctx.driver = new RemoteWebDriver(
                new URL("http://" + username + ":" + accessKey + "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        ctx.driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        ctx.jobId = ((RemoteWebDriver) ctx.driver).getSessionId().toString();
        ctx.passed = true;
    }

    @AfterMethod
    public void tearDown() throws Exception {
        Ctx ctx = (Ctx) threadLocal.get();
        WebDriver driver = ctx.driver;
        if (ctx.passed) {
            sauceREST.jobPassed(ctx.jobId);
        } else {
            sauceREST.jobFailed(ctx.jobId);
        }
        driver.quit();
    }


    @Test
    public void verifyLaunch() throws Exception {
        Ctx ctx = (Ctx) threadLocal.get();
        WebDriver driver = ctx.driver;
        try {
            driver.get("http://wikipedia.org");

            // Make the browser get the page and check its title
            Assert.assertEquals("Wikipedia", driver.getTitle());

            // Check if the launch page elements are there
            Assert.assertTrue(driver.findElement(By.cssSelector(".central-featured")).isDisplayed());
            Assert.assertTrue(driver.findElement(By.cssSelector("#searchInput")).isDisplayed());
            Assert.assertTrue(driver.findElement(By.cssSelector("#searchLanguage")).isDisplayed());
            Assert.assertTrue(driver.findElement(By.cssSelector(".search-form .formBtn")).isDisplayed());
        } catch (Exception e) {
            ctx.passed = false;
            throw e;
        }
    }

    @Test
    public void verifyLaunchBis() throws Exception {
        verifyLaunch();
    }

    @Test
    public void verifyLaunchTer() throws Exception {
        verifyLaunch();
    }

    @Test
    public void verifySearchForUFC() throws Exception {
        Ctx ctx = (Ctx) threadLocal.get();
        WebDriver driver = ctx.driver;
        try {
            driver.get("http://wikipedia.org");

            // Fill out search box with UFC and click search
            driver.findElement(By.cssSelector("#searchInput")).sendKeys("UFC");
            driver.findElement(By.cssSelector(".search-form .formBtn")).click();

            // verify UFC page is there
            Assert.assertEquals("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship", driver.getCurrentUrl());
            Assert.assertEquals("Ultimate Fighting Championship - Wikipedia, the free encyclopedia", driver.getTitle());
        } catch (Exception e) {
            ctx.passed = false;
            throw e;
        }
    }

    @Test
    public void verifySearchForUFCBis() throws Exception {
        verifySearchForUFC();
    }

    @Test
    public void verifySearchForUFCTer() throws Exception {
        verifySearchForUFC();
    }

    @Test
    public void goToHistorySection() throws Exception {
        Ctx ctx = (Ctx) threadLocal.get();
        WebDriver driver = ctx.driver;
        try {
            driver.get("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship");

            // click History
            driver.findElement(By.cssSelector("li [href='#History']")).click();
            Assert.assertEquals("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship#History", driver.getCurrentUrl());

            // make sure we have scrolled to history
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            Long value = (Long) executor.executeScript("return window.scrollY;");
            Assert.assertNotEquals(value, 0, 0);
        } catch (Exception e) {
            ctx.passed = false;
            throw e;
        }
    }

    @Test
    public void goToHistorySectionBis() throws Exception {
        goToHistorySection();
    }

    @Test
    public void goToHistorySectionTer() throws Exception {
        goToHistorySection();
    }

    @Test
    public void verifyEditPageUI() throws Exception {
        Ctx ctx = (Ctx) threadLocal.get();
        WebDriver driver = ctx.driver;
        try {
            driver.get("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship");

            // click edit page
            driver.findElement(By.cssSelector("#ca-edit a")).click();

            // verify edit page UI
            Assert.assertEquals("http://en.wikipedia.org/w/index.php?title=Ultimate_Fighting_Championship&action=edit", driver.getCurrentUrl());
            Assert.assertTrue(driver.findElement(By.cssSelector(".wikiEditor-ui")).isDisplayed());
        } catch (Exception e) {
            ctx.passed = false;
            throw e;
        }
    }

    @Test
    private void longTest() throws Exception {
        Ctx ctx = (Ctx) threadLocal.get();
        WebDriver driver = ctx.driver;
        try {
            // looping between 1 and 3 times
            int numOfLoops = randomGenerator.nextInt(3) + 1;
            for (int idx = 1; idx <= 10; ++idx) {

                // a few tests on the Wikipedia root page
                driver.get("http://wikipedia.org");
                Assert.assertEquals("Wikipedia", driver.getTitle());

                // coffee break
                Thread.sleep(1000 * (1 + randomGenerator.nextInt(2)));

                // more test on the Wikipedia Ultimate Fighting Championship page
                driver.get("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship");
                driver.findElement(By.cssSelector("#ca-edit a")).click();
                Assert.assertEquals(
                        "http://en.wikipedia.org/w/index.php?title=Ultimate_Fighting_Championship&action=edit",
                        driver.getCurrentUrl());
            }
        } catch (Exception e) {
            ctx.passed = false;
            throw e;
        }
    }

    @Test
    public void longTestBis() throws Exception {
        longTest();
    }

    @Test
    public void longTestTer() throws Exception {
        longTest();
    }

 }
