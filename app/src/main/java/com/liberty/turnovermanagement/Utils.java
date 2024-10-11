package com.liberty.turnovermanagement;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static long generateUID() {
        return ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1);
    }
}
