package com.example.foody.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    // The CascadeType.REMOVE is not used for bookings because when a sitting time is deleted, the associated bookings are canceled (BookingStatus.CANCELLED)
    @OneToMany(mappedBy = "sittingTime")
    private List<Booking> bookings = new ArrayList<>();
}