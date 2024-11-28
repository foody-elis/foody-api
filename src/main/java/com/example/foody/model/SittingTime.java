package com.example.foody.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sitting_times")
public class SittingTime extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start", nullable = false)
    private LocalTime start;

    @Column(name = "end", nullable = false)
    private LocalTime end;

    @ManyToOne
    @JoinColumn(name = "week_day_info_id", nullable = false)
    private WeekDayInfo weekDayInfo;

    public SittingTime() {
    }

    public SittingTime(long id, LocalTime start, LocalTime end, WeekDayInfo weekDayInfo) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.weekDayInfo = weekDayInfo;
    }
}
