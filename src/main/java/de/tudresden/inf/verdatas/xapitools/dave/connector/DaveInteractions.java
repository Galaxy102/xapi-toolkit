package de.tudresden.inf.verdatas.xapitools.dave.connector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

class DaveInteractions {
    static WebDriver startNewSession(URL daveEndpoint) {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(750, TimeUnit.MILLISECONDS);
        driver.get(daveEndpoint.toString());
        return driver;
    }

    static void createWorkbook(WebDriver driver) throws InterruptedException {
        driver.findElement(By.cssSelector(".majorbutton")).click();

        TimeUnit.MILLISECONDS.sleep(150);
        List<WebElement> workbookForm = driver.findElements(By.cssSelector(".mdc-text-field__input"));
        TimeUnit.MILLISECONDS.sleep(75);
        workbookForm.get(0).sendKeys("Workbook");
        TimeUnit.MILLISECONDS.sleep(75);
        workbookForm.get(1).sendKeys("Description");
        TimeUnit.MILLISECONDS.sleep(75);
        driver.findElement(By.cssSelector(".wizardfooter :nth-child(2)")).click();
    }

    static void connectLRS(WebDriver driver, String name, String url, String key, String secret) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(75);
        driver.findElement(By.cssSelector(".testdatasetblock .minorbutton")).click();
        TimeUnit.MILLISECONDS.sleep(75);
        driver.findElement(By.cssSelector(".wizardfooter :nth-child(2)")).click();

        TimeUnit.MILLISECONDS.sleep(75);
        driver.findElement(By.cssSelector(".picker.active ul :nth-child(3)")).click();

        TimeUnit.MILLISECONDS.sleep(150);
        List<WebElement> lrsForm = driver.findElements(By.cssSelector(".mdc-text-field__input"));
        TimeUnit.MILLISECONDS.sleep(75);
        lrsForm.get(0).sendKeys(name);
        TimeUnit.MILLISECONDS.sleep(75);
        lrsForm.get(1).sendKeys(url);
        TimeUnit.MILLISECONDS.sleep(75);
        lrsForm.get(2).sendKeys(key);
        TimeUnit.MILLISECONDS.sleep(75);
        lrsForm.get(3).sendKeys(secret);
        TimeUnit.MILLISECONDS.sleep(75);
        driver.findElement(By.cssSelector(".wizardfooter :nth-child(2)")).click();
        // Wait until loading of statements from LRS has stopped
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MINUTES);
        driver.findElement(By.cssSelector(".testdatasetblock .mdc-linear-progress:not(.mdc-linear-progress--closed)"));
        driver.findElement(By.cssSelector(".testdatasetblock .mdc-linear-progress--closed"));
        driver.manage().timeouts().implicitlyWait(750, TimeUnit.MILLISECONDS);
    }

    static void createAnalysis(WebDriver driver) throws InterruptedException {
        driver.findElement(By.cssSelector(".majorbutton.newblock")).click();

        TimeUnit.MILLISECONDS.sleep(150);
        driver.findElement(By.cssSelector(".mdc-text-field__input")).sendKeys("Analysis");
        TimeUnit.MILLISECONDS.sleep(75);
        driver.findElement(By.cssSelector(".wizardfooter :nth-child(2)")).click();
    }

    // TODO decider als Enum, Rückgabe einer Fehlernachricht
    static void addDescriptionToAnalysis(WebDriver driver, String Path, Boolean decider) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(75);

        if (!decider) {
            WebElement query_upload = driver.findElement(By.cssSelector("#query-input-file"));
            query_upload.sendKeys(Path);
            // empty String if query is valid (no error occurred)
            String errorMessage = driver.findElements(By.cssSelector(".error")).get(0).getText();
            if (!errorMessage.isEmpty()) {
                throw new IllegalStateException(errorMessage);
            }
        } else {
            WebElement description_upload = driver.findElement(By.cssSelector("#vega-input-file"));
            description_upload.sendKeys(Path);
            // empty String if description is valid (no error occurred)
            String errorMessage = driver.findElements(By.cssSelector(".error")).get(1).getText();
            if (!errorMessage.isEmpty()) {
                throw new IllegalStateException(errorMessage);
            }
        }
    }

    static String getVisOfAnalysis(WebDriver driver) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(150);
        List<WebElement> buttons = driver.findElements(By.cssSelector(".minorbutton"));
        buttons.get(4).click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.findElement(By.cssSelector(".mdc-snackbar.dave-snackbar.mdc-snackbar--active"));
        driver.findElement(By.cssSelector(".mdc-snackbar.dave-snackbar:not(.mdc-snackbar--active)"));
        driver.manage().timeouts().implicitlyWait(750, TimeUnit.MILLISECONDS);

        return driver.findElement(By.cssSelector(".dave-vega-container svg")).getAttribute("outerHTML");
    }

    static String getResultsForAnalysis(WebDriver driver) {
        List<WebElement> sub_buttons = driver.findElements(By.cssSelector(".minimalbutton"));
        sub_buttons.get(4).click();

        return driver.findElement(By.cssSelector(".analysis-grid .analysis-inner .analysis-inner .cell-6 .result pre")).getText();
    }
}
