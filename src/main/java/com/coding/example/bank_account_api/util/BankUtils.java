package com.coding.example.bank_account_api.util;

import java.util.Random;

public class BankUtils {

    public static int randomInt(int digits) {
        int minimum = (int) Math.pow(10, digits - 1); // minimum value with 2 digits is 10 (10^1)
        int maximum = (int) Math.pow(10, digits) - 1; // maximum value with 2 digits is 99 (10^2 - 1)
        Random random = new Random();
        return minimum + random.nextInt((maximum - minimum) + 1);
    }
}
