package ru.netology;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        LocalDate date = LocalDate.now().plusDays(shift);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    public static String generateCity(Faker faker) {
        String[] validCities = {
                "Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург", "Казань",
                "Нижний Новгород", "Челябинск", "Самара", "Омск", "Ростов-на-Дону",
                "Уфа", "Красноярск", "Воронеж", "Пермь", "Волгоград"
        };
        Random random = new Random();
        return validCities[random.nextInt(validCities.length)];
    }

    public static String generateName(Faker faker) {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generatePhone(Faker faker) {
        return faker.phoneNumber().phoneNumber().replaceAll("[^0-9]", "").substring(0, 11);
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new UserInfo(
                    DataGenerator.generateCity(faker),
                    DataGenerator.generateName(faker),
                    DataGenerator.generatePhone(faker)
            );
        }

        public static String generateCity(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return DataGenerator.generateCity(faker);
        }

        public static String generateName(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return DataGenerator.generateName(faker);
        }

        public static String generatePhone(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return DataGenerator.generatePhone(faker);
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}