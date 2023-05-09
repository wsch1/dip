package test;

import com.codeborne.selenide.Configuration;
import data.DataHelper;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.BuyingTour;
import pages.Dashboard;
import sql.Sql;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pages.BuyingTour.*;

public class TestBuyTour {

    private final DataHelper.CardInfo invalidData = DataHelper.getInvalidDataCard();
    private final DataHelper.CardInfo validDataWithCardApproved = DataHelper.getValidDataCard(DataHelper.getValidCardNumberApproved());
    private final DataHelper.CardInfo validDataWithCardDeclined = DataHelper.getValidDataCard(DataHelper.getValidCardNumberDeclined());
    private final BuyingTour buyingTour = new BuyingTour();

    @BeforeEach
    public void setUp() {
        Configuration.headless = true;
        open("http://localhost:8080/");
    }


    @Name("Тест с пустыми полями" +
            "Проверка отображение уведомлений о пустых полях и их корректность")
    @Test
    public void shouldBeNotificationsDisplayedForAllFields() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                "",
                "",
                "",
                "",
                ""
        );

        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
        buyingTour.checkEmptyFieldMothError();
        buyingTour.checkEmptyFieldYearError();
        buyingTour.checkEmptyFieldHolderError();
        buyingTour.checkEmptyFieldCvcError();
    }

    /////////// Тесты покупки по карте ///////////
    @Name("Проверка успешной покупки по карте" +
            "Должен появится в базе данных ПОКУПОК со статусом ОДОБРЕНО. Так же UI проверка на успешность покупки")
    @Test
    public void shouldAppearInTheDatabaseWithStatusApproved() {

        Sql.deleteAllStringsForPaymentEntity();
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.successBuy();
        assertEquals("APPROVED", Sql.checkStatus());


    }

    @Name("Проверка отказа покупки" +
            "Должен появится в базе данных ПОКУПОК со статусом ОТКЛОНЕНО. Так же UI проверка на отказ от банка")
    @Test
    public void shouldAppearInTheDatabaseWithStatusDeclined() {
        //Удаление всех записей в бд из таблицы покупка по карте
        Sql.deleteAllStringsForPaymentEntity();
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardDeclined.cardNumber,
                validDataWithCardDeclined.moth,
                validDataWithCardDeclined.year,
                validDataWithCardDeclined.cardHolder,
                validDataWithCardDeclined.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.rejected();
        assertEquals("DECLINED", Sql.checkStatus());


    }


    @Name("Пустое поле номер карты" +
            "Должно появится уведомление 'Поле обязательно для заполнения'")
    @Test
    void shouldNotificationAppearUnderTheCardNumberFieldBuyByCard() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                "",
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();

    }

    @Name("Ввод символов в поле номер карты" +
            "Должно появится уведомление 'Поле обязательно для заполнения' так как поле принимает только цифры")
    @Test
    void shouldAppearAnIncorrectFormatNotificationUnderTheCardNumbFieldBuyByCard0() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Ввод в поле НОМЕР КАРТЫ значение с одной цифрой" +
            "Должно появится уведомление под полем НОМЕР КАРТЫ 'Неверный формат'")
    @Test
    void shouldAppearAnIncorrectFormatNotificationUnderTheCardNumbFieldBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                "5",
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Пустое поле МЕСЯЦ" +
            "Должно появится уведомление под полем МЕСЯЦ 'Поле обязательно для заполнения'")
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppearBuyByCard() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                "",
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Ввод символов в поле МЕСЯЦ" +
            "Должно появится уведомление под полем МЕСЯЦ 'Поле обязательно для заполнения'" +
            "так как поле принимает только цифры ")
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppearBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля месяц " +
            "Должно появится уведомление под полем МЕСЯЦ 'Неверно указан срок действия карты'")
    @Test
    void shouldAppearNotificationAboutAnIncorrectFormatBuyByCard0() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                "13",
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldMothErrorWithInvalidValue();
    }

    @Name("Проверка поля месяц" +
            "Должно появится уведомление под полем МЕСЯЦ 'Неверно указан срок действия карты'")
    @Test
    void shouldAppearNotificationAboutAnIncorrectFormatBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                "00",
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldMothErrorWithInvalidValue();
    }

    @Name("Проверка поля год: Пустое поле " +
            "Должно появится уведомление под полем ГОД 'Поле обязательно для заполнения'")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppearBuyByCard() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                "",
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля год: Ввод символов " +
            "Должно появится уведомление под полем ГОД 'Поле обязательно для заполнения' " +
            "так как поле должно принимать только цифры")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppearBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля год: Ввод года меньше текущего на 1" +
            "Должно появится уведомление под полем ГОД 'Истёк срок действия карты' ")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppearBuyByCard2() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                invalidData.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldYearErrorWithInvalidValue();
    }

    @Name("Проверка поля год: Ввод 00 " +
            "Должно появится уведомление под полем ГОД 'Истёк срок действия карты'")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppearBuyByCard3() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                "00",
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldYearErrorWithInvalidValue();
    }


    @Name("Проверка поля Владелец: Пустое поле " +
            "Должно появится уведомление под полем ВЛАДЕЛЕЦ 'Поле обязательно для заполнения' ")
    @Test
    void shouldNotificationAboutAnEmptyHolderFieldWillAppearBuyByCard() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                "",
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorNotification();
    }


    @Name("Проверка поля Владелец: Ввод символов" +
            "Должно появится уведомление под полем ВЛАДЕЛЕЦ 'Поле обязательно для заполнения' ")
    @Test
    void shouldNotificationAboutAnEmptyHolderFieldWillAppearBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля CVC: Пустое поле" +
            "Должно появится уведомление под полем CVC 'Поле обязательно для заполнения' ") //
    @Test
    void shouldNotificationAboutAnEmptyCVCFieldWillAppearBuyByCard0() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                ""
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля CVC: значение с 2 цифрами" +
            "Должно появится уведомление под полем CVC 'Неверный формат' ")
    @Test
    void shouldNotificationAboutAnEmptyCVCFieldWillAppearBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                invalidData.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

}

