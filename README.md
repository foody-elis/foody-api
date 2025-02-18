# Foody API

# Table of Contents

- [Description](#description)
  - [Useful Links](#useful-links)
  - [What is Foody](#what-is-foody)
  - [What is Foody API](#what-is-foody-api)
- [Usage](#usage)
  - [Clone the repository](#clone-the-repository)
  - [Set up the environment](#set-up-the-environment)
  - [Build and run the containers](#build-and-run-the-containers)
  - [Access the backend](#access-the-backend)
- [API Endpoints](#api-endpoints)
  - [User](#user)
  - [Authentication](#authentication)
  - [Restaurant](#restaurant)
  - [Category](#category)
  - [WeekDayInfo](#weekdayinfo)
  - [SittingTime](#sittingtime)
  - [Booking](#booking)
  - [Order](#order)
  - [Dish](#dish)
  - [Review](#review)
- [Error Handling](#error-handling)

## Description

### Useful Links

- Documentation: https://docs.google.com/document/d/1p1RFOiUF8x7opr-N9B8PLcPE7cJZiqI1_v2iclKKEvk/edit?usp=sharing
- Presentation: https://pitch.com/v/presentazione-foody-hk9puv

### What is Foody

Foody is an **innovative** and **centralized** software solution designed to optimize interactions between **users** and
**restaurants**. It offers an all-in-one platform for **table reservations**, **in-restaurant orders**, **payments** and
**reviews** through a user-friendly interface.

The system includes a mobile app for customers to find restaurants, book tables, place orders, make payments and leave
reviews, as well as a dedicated app for restaurant owners to **manage menus**, **reservations**, and **customer feedback
**. Additionally, it supports restaurant staff by **streamlining order management** and communication between the
kitchen and dining area, improving efficiency and service accuracy.

Foody addresses the growing need for digitalization in the restaurant industry, providing a **scalable** and *
*innovative** solution to enhance both customer experience and operational performance.

### What is Foody API

Foody API is the **backend engine** that powers the entire Foody ecosystem, providing **secure** and **efficient**
management of all core functionalities. Designed to support seamless interactions between **users**, **restaurants**,
and **staff**, Foody API enables:

- **User authentication and management**, ensuring secure access for customers and restaurant owners through **JWT-based
  authentication**
- **Restaurant data handling**, including menus, availability, and real-time updates
- **Table booking**, allowing customers to book seamlessly and receive confirmations
- **Order processing**, ensuring smooth communication between customers, waiters, and cooks
- **Review and feedback management**, helping restaurants improve their services based on user ratings
- **Real-time messaging**, enabling direct communication between customers and restaurant staff
- **Endpoint security**, ensuring data protection and controlled access using **JWT (JSON Web Tokens)**
- ...and much more!

With its **scalable**, **high-performance**, and **well-structured** architecture, Foody API ensures that every
interaction within the Foody platform is **fast, reliable, and secure**.  
By leveraging **JWT tokens**, the API enforces authentication and authorization, ensuring that only authenticated users
can access protected resources while preventing unauthorized access to sensitive endpoints.

## Usage

To run the backend using Docker, follow these steps:

### Clone the repository

```sh
$ git clone https://github.com/foody-elis/foody-api.git
$ cd foody-api
```

### Set up the environment

Create a file named `db_root_password.txt` in the `docker/secrets` directory and add your desired root password for the
MariaDB database.

### Build and run the containers

```sh
$ docker-compose up --build
```

### Access the backend

The backend API will be available at: **http://localhost:8080/api/v1**

## API Endpoints

### User

| Method | Endpoint                      | Response                | Exceptions                                                                                |
|--------|-------------------------------|-------------------------|-------------------------------------------------------------------------------------------|
| GET    | `/api/v1/users`               | `List<UserResponseDTO>` |                                                                                           |
| GET    | `/api/v1/users/{id}`          | `UserResponseDTO`       | `EntityNotFoundException`                                                                 |
| GET    | `/api/v1/users/email/{email}` | `UserResponseDTO`       | `EntityNotFoundException`                                                                 |
| GET    | `/api/v1/users/role/{role}`   | `List<UserResponseDTO>` |                                                                                           |
| PUT    | `/api/v1/users`               | `UserResponseDTO`       | `GoogleDriveFileUploadException`, `GoogleDriveFileDeleteException`, `EntityEditException` |
| DELETE | `/api/v1/users/{id}`          | `void`                  | `EntityNotFoundException`, `GoogleDriveFileDeleteException`, `EntityDeletionException`    |

### Authentication

| Method | Endpoint                                          | Response                  | Exceptions                                                                                                                                                                  |
|--------|---------------------------------------------------|---------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | `/api/v1/auth/admins`                             | `UserResponseDTO`         | `GoogleDriveFileUploadException`, `EntityDuplicateException`, `EntityCreationException`                                                                                     |
| POST   | `/api/v1/auth/moderators`                         | `UserResponseDTO`         | `GoogleDriveFileUploadException`, `EntityDuplicateException`, `EntityCreationException`                                                                                     |
| POST   | `/api/v1/auth/restaurateurs`                      | `TokenResponseDTO`        | `GoogleDriveFileUploadException`, `EntityDuplicateException`, `EntityCreationException`, `EntityNotFoundException`, `UserNotActiveException`, `InvalidCredentialsException` |
| POST   | `/api/v1/auth/restaurant/{restaurant-id}/cooks`   | `EmployeeUserResponseDTO` | `GoogleDriveFileUploadException`, `EntityDuplicateException`, `EntityCreationException`, `EntityNotFoundException`, `ForbiddenRestaurantAccessException`                    |
| POST   | `/api/v1/auth/restaurant/{restaurant-id}/waiters` | `EmployeeUserResponseDTO` | `GoogleDriveFileUploadException`, `EntityDuplicateException`, `EntityCreationException`, `EntityNotFoundException`, `ForbiddenRestaurantAccessException`                    |
| POST   | `/api/v1/auth/customers`                          | `TokenResponseDTO`        | `GoogleDriveFileUploadException`, `EntityDuplicateException`, `EntityCreationException`, `EntityNotFoundException`, `UserNotActiveException`, `InvalidCredentialsException` |
| POST   | `/api/v1/auth/login`                              | `TokenResponseDTO`        | `EntityNotFoundException`, `UserNotActiveException`, `InvalidCredentialsException`                                                                                          |
| GET    | `/api/v1/auth/user`                               | `UserResponseDTO`         |                                                                                                                                                                             |
| PATCH  | `/api/v1/auth/change-password`                    | `void`                    | `InvalidPasswordException`, `EntityEditException`                                                                                                                           |

### Restaurant

| Method | Endpoint                                     | Response                              | Exceptions                                                                                                                                                 |
|--------|----------------------------------------------|---------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | `/api/v1/restaurants`                        | `RestaurantResponseDTO`               | `RestaurateurAlreadyHasRestaurantException`, `GoogleDriveFileUploadException`, `EntityCreationException`                                                   |
| GET    | `/api/v1/restaurants`                        | `List<DetailedRestaurantResponseDTO>` |                                                                                                                                                            |
| GET    | `/api/v1/restaurants/{id}`                   | `DetailedRestaurantResponseDTO`       | `EntityNotFoundException`                                                                                                                                  |
| GET    | `/api/v1/restaurants/restaurateur`           | `DetailedRestaurantResponseDTO`       | `EntityNotFoundException`                                                                                                                                  |
| GET    | `/api/v1/restaurants/category/{category-id}` | `List<DetailedRestaurantResponseDTO>` | `EntityNotFoundException`                                                                                                                                  |
| PATCH  | `/api/v1/restaurants/approve/{id}`           | `DetailedRestaurantResponseDTO`       | `EntityNotFoundException`, `EntityEditException`                                                                                                           |
| PUT    | `/api/v1/restaurants/{id}`                   | `DetailedRestaurantResponseDTO`       | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`, `GoogleDriveFileUploadException`, `GoogleDriveFileDeleteException`, `EntityEditException` |
| DELETE | `/api/v1/restaurants/{id}`                   | `void`                                | `EntityNotFoundException`, `GoogleDriveFileDeleteException`, `EntityDeletionException`                                                                     |

### Category

| Method | Endpoint                  | Response                    | Exceptions                                            |
|--------|---------------------------|-----------------------------|-------------------------------------------------------|
| POST   | `/api/v1/categories`      | `CategoryResponseDTO`       | `EntityDuplicateException`, `EntityCreationException` |
| GET    | `/api/v1/categories`      | `List<CategoryResponseDTO>` |                                                       |
| GET    | `/api/v1/categories/{id}` | `CategoryResponseDTO`       | `EntityNotFoundException`                             |
| DELETE | `/api/v1/categories/{id}` | `void`                      | `EntityNotFoundException`, `EntityDeletionException`  |

### WeekDayInfo

| Method | Endpoint                                            | Response                       | Exceptions                                                                                 |
|--------|-----------------------------------------------------|--------------------------------|--------------------------------------------------------------------------------------------|
| POST   | `/api/v1/week-day-infos`                            | `WeekDayInfoResponseDTO`       | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`, `EntityCreationException` |
| GET    | `/api/v1/week-day-infos`                            | `List<WeekDayInfoResponseDTO>` |                                                                                            |
| GET    | `/api/v1/week-day-infos/restaurant/{restaurant-id}` | `List<WeekDayInfoResponseDTO>` | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`                            |
| PUT    | `/api/v1/week-day-infos/{id}`                       | `WeekDayInfoResponseDTO`       | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`                            |

### SittingTime

| Method | Endpoint                                                                               | Response                       | Exceptions                                           |
|--------|----------------------------------------------------------------------------------------|--------------------------------|------------------------------------------------------|
| GET    | `/api/v1/sitting-times`                                                                | `List<SittingTimeResponseDTO>` |                                                      |
| GET    | `/api/v1/sitting-times/restaurant/{restaurant-id}`                                     | `List<SittingTimeResponseDTO>` | `EntityNotFoundException`                            |
| GET    | `/api/v1/sitting-times/restaurant/{restaurant-id}/week-day/{week-day}`                 | `List<SittingTimeResponseDTO>` | `EntityNotFoundException`, `InvalidWeekDayException` |
| GET    | `/api/v1/sitting-times/restaurant/{restaurant-id}/week-day/{week-day}/start-after-now` | `List<SittingTimeResponseDTO>` | `EntityNotFoundException`, `InvalidWeekDayException` |

### Booking

| Method | Endpoint                                      | Response                   | Exceptions                                                                                                   |
|--------|-----------------------------------------------|----------------------------|--------------------------------------------------------------------------------------------------------------|
| POST   | `/api/v1/bookings`                            | `BookingResponseDTO`       | `EntityNotFoundException`, `BookingNotAllowedException`, `EntityCreationException`                           |
| GET    | `/api/v1/bookings`                            | `List<BookingResponseDTO>` |                                                                                                              |
| GET    | `/api/v1/bookings/{id}`                       | `BookingResponseDTO`       | `EntityNotFoundException`, `ForbiddenBookingAccessException`                                                 |
| GET    | `/api/v1/bookings/customer`                   | `List<BookingResponseDTO>` | `EntityNotFoundException`                                                                                    |
| GET    | `/api/v1/bookings/customer/current`           | `List<BookingResponseDTO>` |                                                                                                              |
| GET    | `/api/v1/bookings/restaurant/{restaurant-id}` | `List<BookingResponseDTO>` | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`                                              |
| PATCH  | `/api/v1/bookings/cancel/{id}`                | `BookingResponseDTO`       | `EntityNotFoundException`, `ForbiddenBookingAccessException`, `IllegalStateException`, `EntityEditException` |
| DELETE | `/api/v1/bookings/{id}`                       | `void`                     | `EntityNotFoundException`, `EntityDeletionException`                                                         |

### Order

| Method | Endpoint                                                | Response                 | Exceptions                                                                                                                                            |
|--------|---------------------------------------------------------|--------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | `/api/v1/orders`                                        | `OrderResponseDTO`       | `EntityNotFoundException`, `ForbiddenOrderAccessException`, `OrderNotAllowedException`, `EntityCreationException`                                     |
| GET    | `/api/v1/orders`                                        | `List<OrderResponseDTO>` |                                                                                                                                                       |
| GET    | `/api/v1/orders/{id}`                                   | `OrderResponseDTO`       | `EntityNotFoundException`, `ForbiddenOrderAccessException`                                                                                            |
| GET    | `/api/v1/orders/buyer`                                  | `List<OrderResponseDTO>` | `EntityNotFoundException`                                                                                                                             |
| GET    | `/api/v1/orders/restaurant/{restaurant-id}`             | `List<OrderResponseDTO>` | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`                                                                                       |
| GET    | `/api/v1/orders/restaurant/{restaurant-id}/in-progress` | `List<OrderResponseDTO>` | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`                                                                                       |
| PATCH  | `/api/v1/orders/pay/{id}`                               | `OrderResponseDTO`       | `EntityNotFoundException`, `ForbiddenOrderAccessException`, `InvalidOrderStateException`, `EntityEditException`                                       |
| PATCH  | `/api/v1/orders/prepare/{id}`                           | `OrderResponseDTO`       | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`, `ForbiddenOrderAccessException`, `InvalidOrderStateException`, `EntityEditException` |
| PATCH  | `/api/v1/orders/complete/{id}`                          | `OrderResponseDTO`       | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`, `ForbiddenOrderAccessException`, `InvalidOrderStateException`, `EntityEditException` |
| DELETE | `/api/v1/orders/{id}`                                   | `void`                   | `EntityNotFoundException`, `EntityDeletionException`                                                                                                  |

### Dish

| Method | Endpoint                                    | Response                | Exceptions                                                                                                                                                 |
|--------|---------------------------------------------|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | `/api/v1/dishes`                            | `DishResponseDTO`       | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`, `GoogleDriveFileUploadException`, `EntityCreationException`                               |
| GET    | `/api/v1/dishes`                            | `List<DishResponseDTO>` |                                                                                                                                                            |
| GET    | `/api/v1/dishes/{id}`                       | `DishResponseDTO`       | `EntityNotFoundException`                                                                                                                                  |
| GET    | `/api/v1/dishes/restaurant/{restaurant-id}` | `List<DishResponseDTO>` |                                                                                                                                                            |
| PUT    | `/api/v1/dishes/{id}`                       | `DishResponseDTO`       | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`, `GoogleDriveFileUploadException`, `GoogleDriveFileDeleteException`, `EntityEditException` |
| DELETE | `/api/v1/dishes/{id}`                       | `void`                  | `EntityNotFoundException`, `ForbiddenRestaurantAccessException`, `GoogleDriveFileDeleteException`, `EntityDeletionException`                               |

### Review

| Method | Endpoint                                     | Response                  | Exceptions                                                                             |
|--------|----------------------------------------------|---------------------------|----------------------------------------------------------------------------------------|
| POST   | `/api/v1/reviews`                            | `ReviewResponseDTO`       | `EntityNotFoundException`, `ReviewNotAllowedException`, `EntityCreationException`      |
| GET    | `/api/v1/reviews`                            | `List<ReviewResponseDTO>` |                                                                                        |
| GET    | `/api/v1/reviews/{id}`                       | `ReviewResponseDTO`       | `EntityNotFoundException`, `ForbiddenReviewAccessException`                            |
| GET    | `/api/v1/reviews/customer`                   | `List<ReviewResponseDTO>` | `EntityNotFoundException`                                                              |
| GET    | `/api/v1/reviews/restaurant/{restaurant-id}` | `List<ReviewResponseDTO>` | `EntityNotFoundException`                                                              |
| GET    | `/api/v1/reviews/dish/{dish-id}`             | `List<ReviewResponseDTO>` | `EntityNotFoundException`                                                              |
| DELETE | `/api/v1/reviews/{id}`                       | `void`                    | `EntityNotFoundException`, `ForbiddenReviewAccessException`, `EntityDeletionException` |

## Error Handling

Whenever an exception is thrown, the API returns a standardized error response in the form of an `ErrorDTO`.  
This ensures consistent error reporting across all endpoints.