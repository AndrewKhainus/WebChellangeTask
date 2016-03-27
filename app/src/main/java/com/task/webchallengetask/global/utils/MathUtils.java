package com.task.webchallengetask.global.utils;

import java.math.BigDecimal;


public final class MathUtils {
    public static float round(float _number, int _decimalPlace) {
        BigDecimal roundNumber = new BigDecimal(Float.toString(_number));
        roundNumber = roundNumber.setScale(_decimalPlace, BigDecimal.ROUND_HALF_UP);
        return roundNumber.floatValue();
    }
}
