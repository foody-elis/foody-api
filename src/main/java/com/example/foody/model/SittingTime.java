package com.example.foody.model;

import jakarta.persistence.*;
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
@Table(name = "sitting_times")
@SQLRestriction("deleted_at IS NULL")
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

    // When a sitting time is deleted, all bookings associated with it are also deleted and booking owner is notified
    @OneToMany(mappedBy = "sittingTime", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
}