package edu.course.lab02;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class Tests {
    @Test
    void testSuccessfulWithdraw() {
        BankAccount account = new BankAccount(100);
        account.withdraw(30);
        assertEquals(70, account.getBalance());
    }

    @Test
    void testWithdrawExceedsBalance() {
     BankAccount account = new BankAccount(50);
        assertThrows(IllegalArgumentException.class, () -> {
         account.withdraw(100);
     });
}
}
