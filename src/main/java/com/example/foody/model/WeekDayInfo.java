package com.example.foody.model;

import com.example.foody.utils.enums.SittingTimeStep;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "week_day_infos",
        uniqueConstraints = {
                @UniqueConstraint(name = "week_day_restaurant_id_unique", columnNames = {"week_day", "restaurant_id"})
        }
)
@SQLRestriction("deleted_at IS NULL")
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

    @Override
    public void delete() {
        super.delete();
        sittingTimes.forEach(SittingTime::delete);
    }
}