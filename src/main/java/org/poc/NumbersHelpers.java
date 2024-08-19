package org.poc;

public class NumbersHelpers {
    public static Integer getRandomNumber(Integer min, Integer max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
