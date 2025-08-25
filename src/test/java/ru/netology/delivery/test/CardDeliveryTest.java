package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        // Первое заполнение формы
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Запланировать']").click();

        // Проверка успешного планирования
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(visible, Duration.ofSeconds(15));

        // Повторное заполнение с другой датой
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $x("//*[text()='Запланировать']").click();

        // Проверка появления уведомления о перепланировании
        $("[data-test-id=replan-notification] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible, Duration.ofSeconds(15));

        // Нажатие кнопки перепланирования
        $x("//*[text()='Перепланировать']").click();

        // Проверка успешного перепланирования
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should show error for invalid city")
    void shouldShowErrorForInvalidCity() {
        var invalidCity = "Лондон";
        var date = DataGenerator.generateDate(3);
        var name = DataGenerator.Registration.generateName("ru");
        var phone = DataGenerator.Registration.generatePhone("ru");

        $("[data-test-id=city] input").setValue(invalidCity);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Запланировать']").click();

        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    @DisplayName("Should show error for invalid name")
    void shouldShowErrorForInvalidName() {
        var city = DataGenerator.Registration.generateCity("ru");
        var date = DataGenerator.generateDate(3);
        var invalidName = "John Smith";
        var phone = DataGenerator.Registration.generatePhone("ru");

        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue(invalidName);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Запланировать']").click();

        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
}