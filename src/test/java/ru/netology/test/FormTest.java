package ru.netology.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void sendFormSuccessfulTest() {
        driver.get("http://localhost:9999");
        List<WebElement> inputs = driver.findElements(By.className("input__control"));
        inputs.get(0).sendKeys("Иванов Иван");
        inputs.get(1).sendKeys("+79012345678");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormLatinInNameTest() throws InterruptedException {
        driver.get("http://localhost:9999");
        List<WebElement> inputs = driver.findElements(By.className("input__control"));
        inputs.get(0).sendKeys("Ivanov Ivan");
        inputs.get(1).sendKeys("+79012345678");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        List<WebElement> invalidInput = driver.findElements(By.className("input__sub"));
        String actual = invalidInput.get(0).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendWithIncorrectSymbolsNameTest() throws InterruptedException {
        driver.get("http://localhost:9999");
        List<WebElement> inputs = driver.findElements(By.className("input__control"));
        inputs.get(0).sendKeys("Иванов Иван 123");
        inputs.get(1).sendKeys("+79012345678");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        List<WebElement> invalidInput = driver.findElements(By.className("input__sub"));
        String actual = invalidInput.get(0).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormNotEnoughSymbolsInPhoneNumberTest() throws InterruptedException {
        driver.get("http://localhost:9999");
        List<WebElement> inputs = driver.findElements(By.className("input__control"));
        inputs.get(0).sendKeys("Иванов Иван");
        inputs.get(1).sendKeys("+790123456");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        List<WebElement> invalidInput = driver.findElements(By.className("input__sub"));
        String actual = invalidInput.get(1).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormTooMuchSymbolsInPhoneNumberTest() throws InterruptedException {
        driver.get("http://localhost:9999");
        List<WebElement> inputs = driver.findElements(By.className("input__control"));
        inputs.get(0).sendKeys("Иванов Иван");
        inputs.get(1).sendKeys("+7901234563456");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        List<WebElement> invalidInput = driver.findElements(By.className("input__sub"));
        String actual = invalidInput.get(1).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormMissedPlusInPhoneNumberTest() throws InterruptedException {
        driver.get("http://localhost:9999");
        List<WebElement> inputs = driver.findElements(By.className("input__control"));
        inputs.get(0).sendKeys("Иванов Иван");
        inputs.get(1).sendKeys("79012345678");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        List<WebElement> invalidInput = driver.findElements(By.className("input__sub"));
        String actual = invalidInput.get(1).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormNotClickCheckBoxTest() throws InterruptedException {
        driver.get("http://localhost:9999");
        List<WebElement> inputs = driver.findElements(By.className("input__control"));
        inputs.get(0).sendKeys("Иванов Иван");
        inputs.get(1).sendKeys("+79012345678");
        driver.findElement(By.className("button__text")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";

        WebElement invalidInput = driver.findElement(By.className("checkbox__text"));
        String actual = invalidInput.getText();
        assertEquals(expected, actual);
    }
}
