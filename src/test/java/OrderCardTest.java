import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;

import data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.generateDate;


public class OrderCardTest {
    private final DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
    private final int daysToAdd = 4;
    private final String firstMeetingDate = DataGenerator.generateDate(daysToAdd);


    @BeforeAll//Выполняется перед всеми тестами.
    static void setUpBeforeAll() {
        Configuration.holdBrowserOpen = true;
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
//Аннотация @BeforeEach Выполняется перед каждым тестом.
    void setUpBeforeEach() {

        open("http://localhost:9999");
    }

    @AfterAll //Выполняется после всех тестов.
    static void setUpAfterAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Positive test")
    public void testPositive() {
        $("[data-test-id=\"city\"] input").setValue(validUser.getCity());
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").setValue(validUser.getName());
        $("[data-test-id=\"phone\"] input").setValue(validUser.getPhone());

        $("[data-test-id=\"agreement\"]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=\"success-notification\"]")
                .shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Negative testPhone")
    public void testNegativePhone() {
        $("[data-test-id=\"city\"] input").setValue(validUser.getCity());
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").setValue(validUser.getName());

        $("[data-test-id=\"agreement\"]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=\"phone\"] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Negative testCity")
    public void testNegativeCity() {
        $("[data-test-id=\"city\"] input").setValue("Минск");
        $("[data-test-id=\"date\"] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").setValue(validUser.getName());

        $("[data-test-id=\"agreement\"]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=\"city\"] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }
}
