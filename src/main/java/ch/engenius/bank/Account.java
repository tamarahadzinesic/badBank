package ch.engenius.bank;

import java.math.BigDecimal;

public class Account {
    private BigDecimal money;

    public void withdraw(BigDecimal amount) {
        if ((money.subtract(amount)).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("not enough credits on account");
        }
        setMoney(money.subtract(amount));

    }

    public void deposit(BigDecimal amount) {
        setMoney(money.add(amount));
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
