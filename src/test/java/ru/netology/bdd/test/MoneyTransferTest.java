package ru.netology.bdd.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;
import ru.netology.bdd.page.ReplenishmentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    @BeforeEach
    void loginToAccount() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }
    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        var dashboardPage = new DashboardPage();
        var firstCardStartBalance = dashboardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardStartBalance = dashboardPage.getCardBalance(DataHelper.getSecondCard().getId());
        dashboardPage.replenishSecondCardClick();
        var replenishmentPage = new ReplenishmentPage();
        var amount = 7000;
        replenishmentPage.transferCardToCard(String.valueOf(amount), DataHelper.getFirstCard());
        var firstCardBalance = dashboardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardBalance = dashboardPage.getCardBalance(DataHelper.getSecondCard().getId());
        assertEquals(firstCardStartBalance - amount, firstCardBalance);
        assertEquals(secondCardStartBalance + amount, secondCardBalance);
    }
    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        var dashboardPage = new DashboardPage();
        var firstCardStartBalance = dashboardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardStartBalance = dashboardPage.getCardBalance(DataHelper.getSecondCard().getId());
        dashboardPage.replenishFirstCardClick();
        var replenishmentPage = new ReplenishmentPage();
        var amount = 7000;
        replenishmentPage.transferCardToCard(String.valueOf(amount), DataHelper.getSecondCard());
        var firstCardBalance = dashboardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardBalance = dashboardPage.getCardBalance(DataHelper.getSecondCard().getId());
        assertEquals(firstCardStartBalance + amount, firstCardBalance);
        assertEquals(secondCardStartBalance - amount, secondCardBalance);
    }
    @Test
    void shouldNotTransferAmountGreaterBalanceFromSecondToFirstCard() {
        var dashboardPage = new DashboardPage();
        var firstCardStartBalance = dashboardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardStartBalance = dashboardPage.getCardBalance(DataHelper.getSecondCard().getId());
        dashboardPage.replenishFirstCardClick();
        var replenishmentPage = new ReplenishmentPage();
        var amount = 11000;
        replenishmentPage.transferCardToCard(String.valueOf(amount), DataHelper.getSecondCard());
        replenishmentPage.waitingError();
        assertEquals(firstCardStartBalance, dashboardPage.getCardBalance(DataHelper.getFirstCard().getId()));
        assertEquals(secondCardStartBalance, dashboardPage.getCardBalance(DataHelper.getSecondCard().getId()));
    }
}