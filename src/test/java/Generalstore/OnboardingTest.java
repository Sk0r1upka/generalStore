package Generalstore;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

public class OnboardingTest {

    private static AndroidDriver driver;
    private static WebDriverWait wait;
    public static void main(String[] args) {
        try {
            setupDriver();
            verifyAppLaunch();
            findAndVerifyElements();
            //performUserActions();
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
    private static void setupDriver()throws Exception{
        //налаштування capabilities телефону
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:deviceName", "emulator-5554");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:appPackage", "com.androidsample.generalstore");
        capabilities.setCapability("appium:appActivity", ".SplashActivity");
        capabilities.setCapability("appium:automationName", "UIAutomator2");
        //встановлення драйверу
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    private static void verifyAppLaunch() {
        String currentPackage = driver.getCurrentPackage();
        if (currentPackage.equals("com.androidsample.generalstore")) {
            System.out.println("Application has loaded successfully and the package is avaliable");
        } else {
            System.err.println("✗ Application failed to launch. Current package: " + currentPackage);
            throw new RuntimeException("App launch verification failed");
        }
        try {
            WebElement bucket = driver.findElement(By.id("com.androidsample.generalstore:id/splashscreen"));
            if (bucket.isDisplayed()) {
                System.out.println("✓ Splash screen element found and is displayed");
            }
            else {
                System.out.println("! Splash screen element found but is not displayed");
            }
        }
        catch (Exception e) {
            System.out.println("! Splash screen element not found (may have disappeared)");
        }
    }
    private static void findAndVerifyElements() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("com.androidsample.generalstore:id/toolbar_title")));

            WebElement selectCountryText = driver.findElement(By.xpath("//android.widget.TextView[@text='Select the country where you want to shop']"));
            WebElement yourNameText = driver.findElement(By.xpath("//android.widget.TextView[@text='Your Name']"));
            WebElement genderText = driver.findElement(By.xpath("//android.widget.TextView[@text='Gender']"));
            WebElement countrySpinner = driver.findElement(By.id("android:id/text1"));
            WebElement picture = driver.findElement(By.xpath("//android.widget.ImageView"));
            WebElement spinnerCountry = driver.findElement(By.id("com.androidsample.generalstore:id/spinnerCountry"));
            WebElement maleRadio = driver.findElement(By.id("com.androidsample.generalstore:id/radioMale"));
            WebElement femaleRadio = driver.findElement(By.id("com.androidsample.generalstore:id/radioFemale"));
            WebElement nameInput = driver.findElement(By.id("com.androidsample.generalstore:id/nameField"));
            WebElement shopButton = driver.findElement(By.id("com.androidsample.generalstore:id/btnLetsShop"));

            verifyTitle(title);
            verifyText(selectCountryText, "Select the country where you want to shop");
            verifyText(yourNameText, "Your Name");
            verifyText(genderText, "Gender");
            verifyCountrySpinner(countrySpinner);
            verifyImageElement(picture);
            verifySpinnerElement(spinnerCountry);
            verifyRadioButton(maleRadio, "Male", true); // Should be checked by default
            verifyRadioButton(femaleRadio, "Female", false);
            verifyInputField(nameInput);
            verifyButton(shopButton, "Let's  Shop");
        } catch (Exception e) {
            System.err.println("✗ Error finding elements: " + e.getMessage());
            throw new RuntimeException("Element verification failed", e);
        }
    }
    private static void verifyTitle(WebElement element) {
        if (element != null && element.isDisplayed()) {
            System.out.println("✓ Title is displayed: " + element.getText());
        } else {
            System.err.println("✗ Title is not displayed.");
        }
    }
}