package com.task.webchallengetask.global.utils;

import java.math.BigDecimal;

/**
 * Created by root on 27.03.16.
 */
public final class MathUtils {
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
