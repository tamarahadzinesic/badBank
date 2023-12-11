package ch.engenius.bank.data;

import java.math.BigDecimal;

public class Account {
    private BigDecimal money;

    public Account(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
