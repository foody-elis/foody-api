package com.example.foody.model;

import com.example.foody.utils.enums.SittingTimeStep;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "week_day_info",
        uniqueConstraints = {
                @UniqueConstraint(name = "week_day_restaurant_id_unique", columnNames = {"week_day", "restaurant_id"})
        }
)
public class WeekDayInfo extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "week_day", nullable = false)
    @Min(1)
    @Max(7)
    private int weekDay;

    @Column(name = "start_launch")
    private LocalTime startLaunch;

    @Column(name = "end_launch")
    private LocalTime endLaunch;

    @Column(name = "start_dinner")
    private LocalTime startDinner;

    @Column(name = "end_dinner")
    private LocalTime endDinner;

    @Column(name = "sitting_time_step", nullable = false)
    @Enumerated(EnumType.STRING)
    private SittingTimeStep sittingTimeStep;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "weekDayInfo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SittingTime> sittingTimes = new ArrayList<>();

    public WeekDayInfo() {
    }

    public WeekDayInfo(long id, int weekDay, LocalTime startLaunch, LocalTime endLaunch, LocalTime startDinner, LocalTime endDinner, SittingTimeStep sittingTimeStep, Restaurant restaurant, List<SittingTime> sittingTimes) {
        this.id = id;
        this.weekDay = weekDay;
        this.startLaunch = startLaunch;
        this.endLaunch = endLaunch;
        this.startDinner = startDinner;
        this.endDinner = endDinner;
        this.sittingTimeStep = sittingTimeStep;
        this.restaurant = restaurant;
        this.sittingTimes = sittingTimes;
    }
}