package com.example.foody;

import com.example.foody.builder.*;
import com.example.foody.builder.impl.*;
import com.example.foody.dto.request.*;
import com.example.foody.dto.response.*;
import com.example.foody.model.*;
import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.user.*;
import com.example.foody.state.booking.impl.ActiveState;
import com.example.foody.state.order.impl.CreatedState;
import com.example.foody.utils.enums.BookingStatus;
import com.example.foody.utils.enums.OrderStatus;
import com.example.foody.utils.enums.Role;
import com.example.foody.utils.enums.SittingTimeStep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestDataUtil {

    public static AdminUser createTestAdminUser() {
        AdminUserBuilder adminUserBuilder = new AdminUserBuilderImpl();
        return adminUserBuilder
                .id(1L)
                .email("admin@test.com")
                .password("password123")
                .name("Admin")
                .surname("Test")
                .birthDate(LocalDate.of(1970, 1, 1))
                .phoneNumber("0123456789")
                .avatarUrl("http://example.com/admin-avatar.jpg")
                .role(Role.ADMIN)
                .active(true)
                .firebaseCustomToken("admin-firebase-custom-token")
                .build();
    }

    public static ModeratorUser createTestModeratorUser() {
        ModeratorUserBuilder moderatorUserBuilder = new ModeratorUserBuilderImpl();
        return moderatorUserBuilder
                .id(1L)
                .email("moderator@test.com")
                .password("password123")
                .name("Moderator")
                .surname("Test")
                .birthDate(LocalDate.of(1970, 1, 1))
                .phoneNumber("0123456789")
                .avatarUrl("http://example.com/moderator-avatar.jpg")
                .role(Role.MODERATOR)
                .active(true)
                .firebaseCustomToken("moderator-firebase-custom-token")
                .build();
    }

    public static RestaurateurUser createTestRestaurateurUser() {
        RestaurateurUserBuilder restaurateurUserBuilder = new RestaurateurUserBuilderImpl();
        return restaurateurUserBuilder
                .id(1L)
                .email("restaurateur@test.com")
                .password("password123")
                .name("Restaurateur")
                .surname("Test")
                .birthDate(LocalDate.of(1980, 1, 1))
                .phoneNumber("0123456789")
                .avatarUrl("http://example.com/restaurateur-avatar.jpg")
                .role(Role.RESTAURATEUR)
                .active(true)
                .firebaseCustomToken("restaurateur-firebase-custom-token")
                .build();
    }

    public static CookUser createTestCookUser() {
        CookUserBuilder cookUserBuilder = new CookUserBuilderImpl();
        return cookUserBuilder
                .id(1L)
                .email("cook@test.com")
                .password("password123")
                .name("Cook")
                .surname("Test")
                .birthDate(LocalDate.of(1980, 1, 1))
                .phoneNumber("0123456789")
                .avatarUrl("http://example.com/cook-avatar.jpg")
                .role(Role.COOK)
                .active(true)
                .firebaseCustomToken("cook-firebase-custom-token")
                .build();
    }

    public static WaiterUser createTestWaiterUser() {
        WaiterUserBuilder waiterUserBuilder = new WaiterUserBuilderImpl();
        return waiterUserBuilder
                .id(1L)
                .email("waiter@test.com")
                .password("password123")
                .name("Waiter")
                .surname("Test")
                .birthDate(LocalDate.of(1980, 1, 1))
                .phoneNumber("0123456789")
                .avatarUrl("http://example.com/waiter-avatar.jpg")
                .role(Role.WAITER)
                .active(true)
                .firebaseCustomToken("waiter-firebase-custom-token")
                .build();
    }

    public static CustomerUser createTestCustomerUser() {
        CustomerUserBuilder customerUserBuilder = new CustomerUserBuilderImpl();
        return customerUserBuilder
                .id(1L)
                .email("customer@test.com")
                .password("password123")
                .name("Customer")
                .surname("Test")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phoneNumber("0123456789")
                .avatarUrl("http://example.com/customer-avatar.jpg")
                .role(Role.CUSTOMER)
                .active(true)
                .firebaseCustomToken("customer-firebase-custom-token")
                .build();
    }

    public static Restaurant createTestRestaurant() {
        RestaurantBuilder restaurantBuilder = new RestaurantBuilderImpl();
        return restaurantBuilder
                .id(1L)
                .name("Test Restaurant")
                .description("A test description for the restaurant.")
                .photoUrl("http://example.com/photo.jpg")
                .phoneNumber("1234567890")
                .seats(50)
                .approved(false)
                .address(createTestAddress())
                .restaurateur(createTestRestaurateurUser())
                .employees(List.of(createTestCookUser(), createTestWaiterUser()))
                .build();
    }

    public static Address createTestAddress() {
        AddressBuilder addressBuilder = new AddressBuilderImpl();
        return addressBuilder
                .id(1L)
                .city("Test City")
                .province("TC")
                .street("Test Street")
                .civicNumber("123")
                .postalCode("12345")
                .build();
    }

    public static Category createTestCategory() {
        CategoryBuilder categoryBuilder = new CategoryBuilderImpl();
        return categoryBuilder
                .id(1L)
                .name("Test Category")
                .build();
    }

    public static WeekDayInfo createTestWeekDayInfo() {
        WeekDayInfoBuilder weekDayInfoBuilder = new WeekDayInfoBuilderImpl();
        return weekDayInfoBuilder
                .id(1L)
                .weekDay(1)
                .startLaunch(LocalTime.of(12, 0))
                .endLaunch(LocalTime.of(14, 0))
                .startDinner(LocalTime.of(19, 0))
                .endDinner(LocalTime.of(22, 0))
                .sittingTimeStep(SittingTimeStep.SIXTY)
                .restaurant(createTestRestaurant())
                .build();
    }

    public static SittingTime createTestSittingTime() {
        SittingTimeBuilder sittingTimeBuilder = new SittingTimeBuilderImpl();
        return sittingTimeBuilder
                .id(1L)
                .start(LocalTime.of(12, 0))
                .end(LocalTime.of(14, 0))
                .weekDayInfo(createTestWeekDayInfo())
                .build();
    }

    public static Booking createTestBooking() {
        BookingBuilder bookingBuilder = new BookingBuilderImpl();
        return bookingBuilder
                .id(1L)
                .date(LocalDate.of(2040, 1, 2))
                .seats(2)
                .sittingTime(createTestSittingTime())
                .customer(createTestCustomerUser())
                .restaurant(createTestRestaurant())
                .state(new ActiveState())
                .build();
    }

    public static Dish createTestDish() {
        DishBuilder dishBuilder = new DishBuilderImpl();
        return dishBuilder
                .id(1L)
                .name("Test Dish")
                .description("A test description for the dish.")
                .photoUrl("http://example.com/dish-photo.jpg")
                .price(BigDecimal.valueOf(10.0))
                .restaurant(createTestRestaurant())
                .build();
    }

    public static OrderDish createTestOrderDish() {
        OrderDishBuilder orderDishBuilder = new OrderDishBuilderImpl();
        return orderDishBuilder
                .order(createTestOrderWithoutDishes())
                .dish(createTestDish())
                .quantity(2)
                .build();
    }

    public static Order createTestOrder() {
        OrderBuilder orderBuilder = new OrderBuilderImpl();
        CustomerUser customer = createTestCustomerUser();
        return orderBuilder
                .id(1L)
                .tableCode("A1")
                .orderDishes(List.of(createTestOrderDish()))
                .buyer(new BuyerUser(customer.getId(), new ArrayList<>()))
                .restaurant(createTestRestaurant())
                .state(new CreatedState())
                .build();
    }

    public static Order createTestOrderWithoutDishes() {
        OrderBuilder orderBuilder = new OrderBuilderImpl();
        CustomerUser customer = createTestCustomerUser();
        return orderBuilder
                .id(1L)
                .tableCode("A1")
                .buyer(new BuyerUser(customer.getId(), new ArrayList<>()))
                .restaurant(createTestRestaurant())
                .state(new CreatedState())
                .build();
    }

    public static Review createTestReview() {
        ReviewBuilder reviewBuilder = new ReviewBuilderImpl();
        return reviewBuilder
                .id(1L)
                .title("Test Review")
                .description("A test description for the review.")
                .rating(5)
                .customer(createTestCustomerUser())
                .restaurant(createTestRestaurant())
                .dish(createTestDish())
                .build();
    }

    public static UserLoginRequestDTO createTestUserLoginRequestDTO() {
        UserLoginRequestDTO requestDTO = new UserLoginRequestDTO();
        requestDTO.setEmail("user@test.com");
        requestDTO.setPassword("password");
        return requestDTO;
    }

    public static UserRequestDTO createTestUserRequestDTO() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setEmail("user@test.com");
        requestDTO.setPassword("password123");
        requestDTO.setName("User");
        requestDTO.setSurname("Test");
        requestDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        requestDTO.setPhoneNumber("0123456789");
        requestDTO.setAvatarBase64("aW1hZ2U6Ly9leGFtcGxlLmNvbS9hdmF0YXItYmFzZTY0LmpwZw==");
        return requestDTO;
    }

    public static UserResponseDTO createTestAdminUserResponseDTO() {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmail("admin@test.com");
        responseDTO.setName("Admin");
        responseDTO.setSurname("Test");
        responseDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        responseDTO.setPhoneNumber("0123456789");
        responseDTO.setAvatarUrl("http://example.com/avatar-base64.jpg");
        responseDTO.setRole(Role.ADMIN);
        responseDTO.setActive(true);
        return responseDTO;
    }

    public static UserResponseDTO createTestModeratorUserResponseDTO() {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmail("moderator@test.com");
        responseDTO.setName("Moderator");
        responseDTO.setSurname("Test");
        responseDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        responseDTO.setPhoneNumber("0123456789");
        responseDTO.setAvatarUrl("http://example.com/avatar-base64.jpg");
        responseDTO.setRole(Role.MODERATOR);
        responseDTO.setActive(true);
        return responseDTO;
    }

    public static EmployeeUserResponseDTO createTestCookUserResponseDTO() {
        EmployeeUserResponseDTO responseDTO = new EmployeeUserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmail("cook@test.com");
        responseDTO.setName("Cook");
        responseDTO.setSurname("Test");
        responseDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        responseDTO.setPhoneNumber("0123456789");
        responseDTO.setAvatarUrl("http://example.com/avatar-base64.jpg");
        responseDTO.setRole(Role.COOK);
        responseDTO.setActive(true);
        responseDTO.setEmployerRestaurantId(1L);
        return responseDTO;
    }

    public static EmployeeUserResponseDTO createTestWaiterUserResponseDTO() {
        EmployeeUserResponseDTO responseDTO = new EmployeeUserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmail("waiter@test.com");
        responseDTO.setName("Waiter");
        responseDTO.setSurname("Test");
        responseDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        responseDTO.setPhoneNumber("0123456789");
        responseDTO.setAvatarUrl("http://example.com/avatar-base64.jpg");
        responseDTO.setRole(Role.WAITER);
        responseDTO.setActive(true);
        responseDTO.setEmployerRestaurantId(1L);
        return responseDTO;
    }

    public static CustomerUserResponseDTO createTestCustomerUserResponseDTO() {
        CustomerUserResponseDTO responseDTO = new CustomerUserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmail("customer@test.com");
        responseDTO.setName("Customer");
        responseDTO.setSurname("Test");
        responseDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        responseDTO.setPhoneNumber("0123456789");
        responseDTO.setAvatarUrl("http://example.com/customer-avatar.jpg");
        responseDTO.setRole(Role.CUSTOMER);
        responseDTO.setActive(true);
        return responseDTO;
    }

    public static UserUpdateRequestDTO createTestUserUpdateRequestDTO() {
        UserUpdateRequestDTO requestDTO = new UserUpdateRequestDTO();
        requestDTO.setName("User");
        requestDTO.setSurname("Test");
        requestDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        requestDTO.setPhoneNumber("0123456789");
        requestDTO.setAvatarBase64("aW1hZ2U6Ly9leGFtcGxlLmNvbS9hdmF0YXItYmFzZTY0LmpwZw==");
        return requestDTO;
    }

    public static UserChangePasswordRequestDTO createTestUserChangePasswordRequestDTO() {
        UserChangePasswordRequestDTO requestDTO = new UserChangePasswordRequestDTO();
        requestDTO.setCurrentPassword("password");
        requestDTO.setNewPassword("new-password");
        return requestDTO;
    }

    public static RestaurantRequestDTO createTestRestaurantRequestDTO() {
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO();
        requestDTO.setName("Test Restaurant");
        requestDTO.setDescription("A test description for the restaurant.");
        requestDTO.setPhoneNumber("1234567890");
        requestDTO.setSeats(50);
        requestDTO.setCategories(List.of(1L));
        requestDTO.setCity("Test City");
        requestDTO.setProvince("TC");
        requestDTO.setStreet("Test Street");
        requestDTO.setCivicNumber("123");
        requestDTO.setPostalCode("12345");
        return requestDTO;
    }

    public static RestaurantResponseDTO createTestRestaurantResponseDTO() {
        RestaurantResponseDTO responseDTO = new RestaurantResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Restaurant");
        responseDTO.setDescription("A test description for the restaurant.");
        responseDTO.setPhotoUrl("http://example.com/photo.jpg");
        responseDTO.setPhoneNumber("1234567890");
        responseDTO.setSeats(50);
        responseDTO.setApproved(false);
        responseDTO.setCategories(List.of());
        responseDTO.setCity("Test City");
        responseDTO.setProvince("TC");
        responseDTO.setStreet("Test Street");
        responseDTO.setCivicNumber("123");
        responseDTO.setPostalCode("12345");
        responseDTO.setRestaurateurId(1L);
        return responseDTO;
    }

    public static DetailedRestaurantResponseDTO createTestDetailedRestaurantResponseDTO() {
        DetailedRestaurantResponseDTO responseDTO = new DetailedRestaurantResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Restaurant");
        responseDTO.setDescription("A test description for the restaurant.");
        responseDTO.setPhotoUrl("http://example.com/photo.jpg");
        responseDTO.setPhoneNumber("1234567890");
        responseDTO.setSeats(50);
        responseDTO.setApproved(false);
        responseDTO.setCategories(List.of());
        responseDTO.setCity("Test City");
        responseDTO.setProvince("TC");
        responseDTO.setStreet("Test Street");
        responseDTO.setCivicNumber("123");
        responseDTO.setPostalCode("12345");
        responseDTO.setRestaurateurId(1L);
        return responseDTO;
    }

    public static CategoryRequestDTO createTestCategoryRequestDTO() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Test Category");
        return requestDTO;
    }

    public static CategoryResponseDTO createTestCategoryResponseDTO() {
        CategoryResponseDTO responseDTO = new CategoryResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Category");
        return responseDTO;
    }

    public static WeekDayInfoRequestDTO createTestWeekDayInfoRequestDTO() {
        WeekDayInfoRequestDTO requestDTO = new WeekDayInfoRequestDTO();
        requestDTO.setWeekDay(1);
        requestDTO.setStartLaunch(LocalTime.of(12, 0));
        requestDTO.setEndLaunch(LocalTime.of(14, 0));
        requestDTO.setStartDinner(LocalTime.of(19, 0));
        requestDTO.setEndDinner(LocalTime.of(22, 0));
        requestDTO.setSittingTimeStep(SittingTimeStep.SIXTY.name());
        requestDTO.setRestaurantId(1L);
        return requestDTO;
    }

    public static WeekDayInfoResponseDTO createTestWeekDayInfoResponseDTO() {
        WeekDayInfoResponseDTO responseDTO = new WeekDayInfoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setWeekDay(1);
        responseDTO.setStartLaunch(LocalTime.of(12, 0));
        responseDTO.setEndLaunch(LocalTime.of(14, 0));
        responseDTO.setStartDinner(LocalTime.of(19, 0));
        responseDTO.setEndDinner(LocalTime.of(22, 0));
        responseDTO.setSittingTimeStep(SittingTimeStep.SIXTY.name());
        responseDTO.setRestaurantId(1L);
        return responseDTO;
    }

    public static WeekDayInfoUpdateRequestDTO createTestWeekDayInfoUpdateRequestDTO() {
        WeekDayInfoUpdateRequestDTO requestDTO = new WeekDayInfoUpdateRequestDTO();
        requestDTO.setStartLaunch(LocalTime.of(12, 0));
        requestDTO.setEndLaunch(LocalTime.of(14, 0));
        requestDTO.setStartDinner(LocalTime.of(19, 0));
        requestDTO.setEndDinner(LocalTime.of(22, 0));
        requestDTO.setSittingTimeStep(SittingTimeStep.SIXTY.name());
        return requestDTO;
    }

    public static SittingTimeResponseDTO createTestSittingTimeResponseDTO() {
        SittingTimeResponseDTO responseDTO = new SittingTimeResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStart(LocalTime.of(12, 0));
        responseDTO.setEnd(LocalTime.of(14, 0));
        responseDTO.setWeekDayInfoId(1L);
        return responseDTO;
    }

    public static DishRequestDTO createTestDishRequestDTO() {
        DishRequestDTO requestDTO = new DishRequestDTO();
        requestDTO.setName("Test Dish");
        requestDTO.setDescription("A test description for the dish.");
        requestDTO.setPhotoBase64("aW1hZ2U6Ly9leGFtcGxlLmNvbS9kaXNoLXBob3RvLmpwZw==");
        requestDTO.setPrice(BigDecimal.valueOf(10.0));
        requestDTO.setRestaurantId(1L);
        return requestDTO;
    }

    public static DishResponseDTO createTestDishResponseDTO() {
        DishResponseDTO responseDTO = new DishResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Dish");
        responseDTO.setDescription("A test description for the dish.");
        responseDTO.setPhotoUrl("http://example.com/dish-photo.jpg");
        responseDTO.setPrice(BigDecimal.valueOf(10.0));
        responseDTO.setRestaurantId(1L);
        return responseDTO;
    }

    public static DishUpdateRequestDTO createTestDishUpdateRequestDTO() {
        DishUpdateRequestDTO requestDTO = new DishUpdateRequestDTO();
        requestDTO.setName("Test Dish");
        requestDTO.setDescription("A test description for the dish.");
        requestDTO.setPrice(BigDecimal.valueOf(10.0));
        return requestDTO;
    }

    public static BookingRequestDTO createTestBookingRequestDTO() {
        BookingRequestDTO requestDTO = new BookingRequestDTO();
        requestDTO.setDate(LocalDate.of(2021, 1, 1));
        requestDTO.setSeats(2);
        requestDTO.setSittingTimeId(1L);
        requestDTO.setRestaurantId(1L);
        return requestDTO;
    }

    public static BookingResponseDTO createTestBookingResponseDTO() {
        BookingResponseDTO responseDTO = new BookingResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setDate(LocalDate.of(2021, 1, 1));
        responseDTO.setSeats(2);
        responseDTO.setSittingTime(TestDataUtil.createTestSittingTimeResponseDTO());
        responseDTO.setCustomer(TestDataUtil.createTestCustomerUserResponseDTO());
        responseDTO.setRestaurant(TestDataUtil.createTestRestaurantResponseDTO());
        responseDTO.setStatus(BookingStatus.ACTIVE.name());
        return responseDTO;
    }

    public static OrderDishRequestDTO createTestOrderDishRequestDTO() {
        OrderDishRequestDTO requestDTO = new OrderDishRequestDTO();
        requestDTO.setDishId(1L);
        requestDTO.setQuantity(2);
        return requestDTO;
    }

    public static OrderDishResponseDTO createTestOrderDishResponseDTO() {
        OrderDishResponseDTO responseDTO = new OrderDishResponseDTO();
        responseDTO.setDishId(1L);
        responseDTO.setDishName("Test Dish");
        responseDTO.setPrice(BigDecimal.valueOf(10.0));
        responseDTO.setQuantity(2);
        return responseDTO;
    }

    public static OrderRequestDTO createTestOrderRequestDTO() {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setTableCode("A1");
        requestDTO.setOrderDishes(List.of(createTestOrderDishRequestDTO()));
        requestDTO.setRestaurantId(1L);
        return requestDTO;
    }

    public static OrderResponseDTO createTestOrderResponseDTO() {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCreatedAt(LocalDateTime.of(2021, 1, 1, 12, 0, 0));
        responseDTO.setTableCode("A1");
        responseDTO.setOrderDishes(List.of(createTestOrderDishResponseDTO()));
        responseDTO.setBuyer(TestDataUtil.createTestCustomerUserResponseDTO());
        responseDTO.setRestaurant(TestDataUtil.createTestRestaurantResponseDTO());
        responseDTO.setStatus(OrderStatus.CREATED.name());
        return responseDTO;
    }

    public static ReviewRequestDTO createTestReviewRequestDTO() {
        ReviewRequestDTO requestDTO = new ReviewRequestDTO();
        requestDTO.setTitle("Test Review");
        requestDTO.setDescription("A test description for the review.");
        requestDTO.setRating(5);
        requestDTO.setRestaurantId(1L);
        requestDTO.setDishId(1L);
        return requestDTO;
    }

    public static ReviewResponseDTO createTestReviewResponseDTO() {
        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCreatedAt(LocalDateTime.of(2021, 1, 1, 12, 0, 0));
        responseDTO.setTitle("Test Review");
        responseDTO.setDescription("A test description for the review.");
        responseDTO.setRating(5);
        responseDTO.setRestaurantId(1L);
        responseDTO.setDishId(1L);
        responseDTO.setCustomerId(1L);
        responseDTO.setCustomerName("Customer");
        responseDTO.setCustomerSurname("Test");
        responseDTO.setCustomerAvatarUrl("http://example.com/customer-avatar.jpg");
        return responseDTO;
    }

    public static TokenResponseDTO createTestTokenResponseDTO() {
        TokenResponseDTO responseDTO = new TokenResponseDTO();
        responseDTO.setAccessToken("access-token");
        return responseDTO;
    }
}