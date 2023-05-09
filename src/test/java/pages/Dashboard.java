package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

public class Dashboard {
    public Dashboard() {
    }

    private static final SelenideElement heading = $("[class='heading heading_size_l heading_theme_alfa-on-white']")
            .shouldHave(Condition.text("Путешествие дня"));

    private static final SelenideElement buyCard = $("[class='heading heading_size_m heading_theme_alfa-on-white']");

    private static final SelenideElement formCard = $$("[class='button__content']")
            .findBy(Condition.text("Купить"));

    private static final SelenideElement formCredit = $$("[class='button__content']")
            .findBy(Condition.text("Купить в кредит"));


    public static BuyingTour clickButtonBuyByCard() {
        heading.shouldBe(Condition.visible);
        formCard.click();
        buyCard.shouldBe(Condition.visible).shouldHave(Condition.text("Оплата по карте"));
        return new BuyingTour();
    }


    public static BuyingTour clickButtonBuyCreditCard() {
        heading.shouldBe(Condition.visible);
        formCredit.click();
        buyCard.shouldBe(Condition.visible).shouldHave(Condition.text("Кредит по данным карты"));
        return new BuyingTour();
    }


}
