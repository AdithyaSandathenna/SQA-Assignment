package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    protected Properties properties;

    protected String baseUrl;
    protected String validUsername;
    protected String validPassword;
    protected String invalidUsername;
    protected String invalidPassword;
    protected String searchTerm;
    protected static int demoDelay = 1500;

    public BaseTest() {
        loadConfig();
    }

    private void loadConfig() {
        properties = new Properties();
        try {
            File configFile = new File("src/main/resources/config.properties");
            if (configFile.exists()) {
                try (InputStream input = new FileInputStream(configFile)) {
                    properties.load(input);
                }
            } else {
                InputStream cpInput = getClass().getClassLoader().getResourceAsStream("config.properties");
                if (cpInput != null) {
                    properties.load(cpInput);
                }
            }
        } catch (Exception e) {
            System.out.println("[WARN] Unable to load config.properties, using defaults/system properties: " + e.getMessage());
        }

        baseUrl = System.getProperty("baseUrl", properties.getProperty("baseUrl", "https://www.linkedin.com/login"));
        validUsername = System.getProperty("valid.username", properties.getProperty("valid.username", "adithyasandathenna@gmail.com"));
        validPassword = System.getProperty("valid.password", properties.getProperty("valid.password", "2003Linkedin*bh"));
        invalidUsername = System.getProperty("invalid.username", properties.getProperty("invalid.username", "adi@gmail.com"));
        invalidPassword = System.getProperty("invalid.password", properties.getProperty("invalid.password", "56565678"));
        searchTerm = System.getProperty("search.term", properties.getProperty("search.term", "Software Testing"));
        
        try {
            demoDelay = Integer.parseInt(properties.getProperty("demo.delay.millis", "1500"));
        } catch (Exception ignored) {}
    }

    public static void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", properties.getProperty("headless", "false")));
        if (headless) {
            options.addArguments("--headless=new");
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Open Base URL
        driver.get(baseUrl);
        pause(800);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot("failure_" + result.getName());
        }

        // Brief demonstration pause so the final test state is clearly visible before closing browser
        pause(demoDelay);

        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {}
        }
    }

    public String takeScreenshot(String name) {
        try {
            File screenshotsDir = new File("screenshots");
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = name + "_" + timestamp + ".png";
            File destination = new File(screenshotsDir, fileName);

            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, destination);

            System.out.println("[INFO] Screenshot captured: " + destination.getAbsolutePath());
            return destination.getAbsolutePath();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }
}
