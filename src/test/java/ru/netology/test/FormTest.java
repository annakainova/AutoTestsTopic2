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
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormEmptyNameTest() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Поле обязательно для заполнения";

        WebElement invalidInput = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String actual = invalidInput.getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormEmptyPhoneTest() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Поле обязательно для заполнения";

        WebElement invalidInput = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actual = invalidInput.getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormLatinInNameTest() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivanov Ivan");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        WebElement invalidInput = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String actual = invalidInput.getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendWithIncorrectSymbolsNameTest() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов 123");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        WebElement invalidInput = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String actual = invalidInput.getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormNotEnoughSymbolsInPhoneNumberTest() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+790123456");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        WebElement invalidInput = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actual = invalidInput.getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormTooMuchSymbolsInPhoneNumberTest() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7901234563456");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        WebElement invalidInput = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actual = invalidInput.getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormMissedPlusInPhoneNumberTest() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79012345678");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
        driver.findElement(By.className("button__text")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        WebElement invalidInput = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actual = invalidInput.getText();
        assertEquals(expected, actual);
    }

    @Test
    public void sendFormNotClickCheckBoxTest() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Петров-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79012345678");
        driver.findElement(By.className("button__text")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";

        WebElement invalidInput = driver.findElement(By.className("checkbox__text"));
        String actual = invalidInput.getText();
        assertEquals(expected, actual);
    }
}
