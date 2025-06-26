package Generalstore;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

public class OnboardingTest {
    private static final String APK_PATH = "C:\\Users\\savch\\IdeaProjects\\untitled\\src\\test\\java\\Generalstore\\General-Store.apk";
    private static final String APP_PACKAGE = "com.androidsample.generalstore";
    private static AndroidDriver driver;
    private static WebDriverWait wait;
    public static void main(String[] args) {
        try {
            setupDriver();
            verifyAppLaunch();
            findAndVerifyElements();
            //performUserActions();
            verifyProductListVisible();
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    private static void setupDriver()throws Exception{
        //налаштування capabilities телефону
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:deviceName", "emulator-5554");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:app", APK_PATH);
        capabilities.setCapability("appium:appPackage", APP_PACKAGE);
        capabilities.setCapability("appium:appActivity", ".SplashActivity");
        capabilities.setCapability("appium:automationName", "UIAutomator2");
        //встановлення драйверу
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    private static void cleanup() {
        if (driver == null) return;
        try {
            driver.terminateApp(APP_PACKAGE);
            if (driver.queryAppState(APP_PACKAGE) != ApplicationState.NOT_RUNNING) {
                System.out.println("......…");
                Thread.sleep(1000);
            }
            boolean removed = driver.removeApp(APP_PACKAGE);
            System.out.println(removed
                    ? "✓ " + APP_PACKAGE + " remove."
                    : "✗ Not " + APP_PACKAGE);
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
        } finally {
            driver.quit();
            System.out.println("✓ Succ");
        }
    }
//    private static void verifyAppLaunch() {
//        String currentPackage = driver.getCurrentPackage();
//        if (currentPackage.equals("com.androidsample.generalstore")) {
//            System.out.println("Application has loaded successfully and the package is avaliable");
//        } else {
//            System.err.println("✗ Application failed to launch. Current package: " + currentPackage);
//            throw new RuntimeException("App launch verification failed");
//        }
//        try {
//            WebElement bucket = driver.findElement(By.id("com.androidsample.generalstore:id/splashscreen"));
//            if (bucket.isDisplayed()) {
//                System.out.println("✓ Splash screen element found and is displayed");
//            }
//            else {
//                System.out.println("! Splash screen element found but is not displayed");
//            }
//        }
//        catch (Exception e) {
//            System.out.println("! Splash screen element not found (may have disappeared)");
//        }
//    }
private static void verifyAppLaunch() {
    String currentPackage = driver.getCurrentPackage();
    if (currentPackage.equals("com.androidsample.generalstore")) {
        System.out.println("✓ Application has loaded successfully and the package is available");
    } else {
        System.err.println("✗ Application failed to launch. Current package: " + currentPackage);
        throw new RuntimeException("App launch verification failed");
    }

    try {
        WebElement splashScreen = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.androidsample.generalstore:id/splashscreen")));
        verifyElementState(splashScreen, "Splash screen");
    } catch (Exception e) {
        System.err.println("✗ Splash screen element not found (may have disappeared): " + e.getMessage());
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
            shopButton.click();
        } catch (Exception e) {
            System.err.println("✗ Error during form filling or transition: " + e.getMessage());
            throw new RuntimeException("Form interaction failed", e);
        }
    }
    private static void verifyInputField(WebElement input) {
        String testName = "Test";
        verifyElementState(input, "Name input field");
        try {
            input.clear();
            input.sendKeys(testName);
            String enteredText = input.getText();
            if (enteredText.equals(testName)) {
                System.out.println("✓ Input field accepted text correctly: " + enteredText);
            } else {
                System.err.println("✗ Input field text mismatch. Expected: " + testName + ", Found: " + enteredText);
            }
        } catch (Exception e) {
            System.err.println("✗ Failed to interact with name input field: " + e.getMessage());
        }
    }
    private static void verifyTitle(WebElement element) {
        if (element != null && element.isDisplayed()) {
            System.out.println("✓ Title is displayed: " + element.getText());
        } else {
            System.err.println("✗ Title is not displayed.");
        }
    }
    private static void verifyText(WebElement element, String expectedText) {
        if (element != null && element.isDisplayed()) {
            String actualText = element.getText();
            if (actualText.equals(expectedText)) {
                System.out.println("✓ Text matches: " + actualText);
            } else {
                System.err.println("✗ Text does not match. Expected: " + expectedText + ", Found: " + actualText);
            }
        } else {
            System.err.println("✗ Element with text '" + expectedText + "' is not displayed.");
        }
    }
    private static void verifyCountrySpinner(WebElement element) {
        if (element != null && element.isDisplayed()) {
            System.out.println("✓ Country spinner is displayed with value: " + element.getText());
        } else {
            System.err.println("✗ Country spinner not displayed.");
        }
    }
    private static void verifyImageElement(WebElement element) {
        if (element != null && element.isDisplayed()) {
            System.out.println("✓ Image element is displayed.");
        } else {
            System.err.println("✗ Image element is not displayed.");
        }
    }
    private static void verifySpinnerElement(WebElement element) {
        if (element != null && element.isDisplayed()) {
            System.out.println("✓ Spinner element is displayed.");
        } else {
            System.err.println("✗ Spinner element is not displayed.");
        }
    }
    private static void verifyRadioButton(WebElement element, String label, boolean shouldBeChecked) {
        if (element != null && element.isDisplayed()) {
            String checkedAttr = element.getAttribute("checked");
            boolean isChecked = "true".equalsIgnoreCase(checkedAttr);

            if (isChecked == shouldBeChecked) {
                System.out.println("✓ Radio button '" + label + "' is correctly " + (isChecked ? "checked" : "not checked"));
            } else {
                System.err.println("✗ Radio button '" + label + "' checked state mismatch. Expected: " + shouldBeChecked + ", Actual: " + isChecked);
            }
        } else {
            System.err.println("✗ Radio button '" + label + "' is not displayed.");
        }
    }
    private static void verifyButton(WebElement element, String expectedText) {
        if (element != null && element.isDisplayed()) {
            String actualText = element.getText();
            if (actualText.equals(expectedText)) {
                System.out.println("✓ Button text matches: " + actualText);
            } else {
                System.err.println("✗ Button text mismatch. Expected: '" + expectedText + "', but found: '" + actualText + "'");
            }
        } else {
            System.err.println("✗ Button is not displayed.");
        }
    }
    private static void verifyProductListVisible() {
        By productListBy = By.id("com.androidsample.generalstore:id/rvProductList");
        WebElement productList = wait.until(
                ExpectedConditions.visibilityOfElementLocated(productListBy));
        viewProductlist(productList, true, true);
    }
    private static void viewProductlist(WebElement element, boolean expectedResultDisplayed, boolean expectedResultEnabled){
        if (element != null && element.isDisplayed()) {
            boolean actualResultDisplayed = element.isDisplayed();
            boolean actualResultEnabled = element.isEnabled();
            System.out.println("product list is displayed");
            if (!actualResultDisplayed){
                System.out.println("product list not found.");
            }
            if (!actualResultEnabled) {
                System.err.println("! Warning: " + element + " is not enabled for interaction.");
            }
        }
        else {
            System.err.println("something went wrong");
        }
    }
    private static void verifyElementState(WebElement element, String elementName) {
        if (element != null) {
            boolean isDisplayed = element.isDisplayed();
            boolean isEnabled = element.isEnabled();
            System.out.printf("✓ %s found: Displayed = %s, Enabled = %s%n", elementName, isDisplayed, isEnabled);

            if (!isDisplayed) {
                System.err.println("! Warning: " + elementName + " is not displayed on screen.");
            }

            if (!isEnabled) {
                System.err.println("! Warning: " + elementName + " is not enabled for interaction.");
            }
        } else {
            System.err.println("✗ " + elementName + " is null.");
        }
    }
}