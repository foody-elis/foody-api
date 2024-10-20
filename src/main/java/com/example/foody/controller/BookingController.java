package com.example.foody.controller;

import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.exceptions.booking.BookingNotAllowedException;
import com.example.foody.exceptions.booking.ForbiddenBookingAccessException;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.model.user.User;
import com.example.foody.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> saveBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO)
            throws EntityNotFoundException, BookingNotAllowedException, EntityCreationException {
        return new ResponseEntity<>(bookingService.save(bookingRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getBookings() {
        return new ResponseEntity<>(bookingService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenBookingAccessException {
        return new ResponseEntity<>(bookingService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUser(@AuthenticationPrincipal User user)
            throws EntityNotFoundException {
        return new ResponseEntity<>(bookingService.findAllByUser(user.getId()), HttpStatus.OK);
    }

    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByRestaurant(@PathVariable("restaurant-id") long restaurantId)
            throws EntityNotFoundException, ForbiddenRestaurantAccessException {
        return new ResponseEntity<>(bookingService.findAllByRestaurant(restaurantId), HttpStatus.OK);
    }

    @PatchMapping(path = "/cancel/{id}")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenBookingAccessException, IllegalStateException, EntityEditException {
        return new ResponseEntity<>(bookingService.cancelById(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeBooking(@PathVariable long id)
            throws EntityNotFoundException, EntityDeletionException {
        bookingService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
