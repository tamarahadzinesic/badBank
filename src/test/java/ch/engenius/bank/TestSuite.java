package ch.engenius.bank;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({AccountServiceTest.class, BankServiceTest.class})
public class TestSuite {
}
