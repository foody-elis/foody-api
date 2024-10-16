package com.example.foody.exceptions.week_day_info;

public class DuplicateWeekDayInfoException extends RuntimeException {
    public DuplicateWeekDayInfoException(int weekDay, long restaurantId) {
        super("A week day info with week day " + weekDay + " and restaurant id " + restaurantId + " already exists");
    }
}
