package com.coding.example.bank_account_api.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class BankUtilsTest {

    @Test
    public void randomInt() throws Exception {
        Set<Integer> integers = new HashSet<>();

        for (int i = 1; i <= 27; i++) {
            integers.add(BankUtils.randomInt(8));
        }

        assertTrue(integers.size() == 27);

        for (Integer integer : integers) {
            assertTrue(integer.toString().length() == 8);
        }
    }

}