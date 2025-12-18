import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderCardTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    void shouldSubmitFormSuccessfully() {
        // Открываем страницу
        driver.get("http://localhost:9999");

        //Заполняем поле "Фамилия и имя"
        WebElement nameField = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameField.sendKeys("Иванов Иван");

        //Заполняем поле "Телефон"
        WebElement phoneField = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneField.sendKeys("+79161234567");

        //Кликаем чекбокс согласия
        WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id='agreement']"));
        checkbox.click();

        //Нажимаем кнопку "Продолжить"
        WebElement button = driver.findElement(By.xpath("//button[.//span[text()='Продолжить']]"));
        button.click();

        //Ждем появления сообщения
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@data-test-id='order-success']")
        ));

        //Проверяем текст
        String actualText = successMessage.getText().trim();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertTrue(actualText.contains(expectedText),
                "Ожидалось сообщение содержащее: '" + expectedText +
                        "', но получено: '" + actualText + "'");
    }
}
