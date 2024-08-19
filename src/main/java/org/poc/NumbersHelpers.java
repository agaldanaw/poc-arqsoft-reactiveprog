package org.poc;

public class NumbersHelpers {
    // Get a random number between min and max
    public static Integer getRandomNumber(Integer min, Integer max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
