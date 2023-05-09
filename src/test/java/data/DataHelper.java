package data;

import com.github.javafaker.Faker;
import lombok.Value;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;



public class DataHelper {

    public DataHelper() {
    }

    @Value
    public static class CardInfo {
        public String cardNumber;
        public String moth;
        public String year;
        public String cardHolder;
        public String cvc;
    }


    public static CardInfo getValidDataCard(String cardNumb) {
        Faker faker = new Faker();
        String cardNumber = cardNumb;
        String month = getValidMonth();
        String year = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        String cardHolder = faker.name().name().toUpperCase(Locale.forLanguageTag("eng"));
        String cvc = faker.numerify("###");
        return new CardInfo(cardNumber, month, year, cardHolder, cvc);

    }

    public static CardInfo getInvalidDataCard() {
        Faker faker = Faker.instance();
        String cardNumber = faker.numerify("#");
        String moth = faker.numerify("#");
        String year = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        String cardHolder = faker.name().firstName().toUpperCase(Locale.forLanguageTag("ru"));
        String cvc = faker.numerify("##");
        return new CardInfo(cardNumber, moth, year, cardHolder, cvc);
    }

    public static String getInvalidSymbols() {
        return "*&^<&%>{%#%";
    }

    public static String getValidCardNumberApproved() {
        return "4444 4444 4444 4441";
    }

    public static String getValidCardNumberDeclined() {
        return "4444 4444 4444 4442";
    }

    public static String getValidMonth() {
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        Random index = new Random();
        int indexInt = index.nextInt(months.length);
        return months[indexInt];
    }
}
