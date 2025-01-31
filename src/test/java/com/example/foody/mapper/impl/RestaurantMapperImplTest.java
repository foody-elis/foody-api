package com.example.foody.mapper.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.builder.AddressBuilder;
import com.example.foody.builder.RestaurantBuilder;
import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.helper.DishHelper;
import com.example.foody.mapper.CategoryMapper;
import com.example.foody.mapper.ReviewMapper;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link RestaurantMapperImpl} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class RestaurantMapperImplTest {

    @InjectMocks
    private RestaurantMapperImpl restaurantMapper;

    @Mock
    private RestaurantBuilder restaurantBuilder;

    @Mock
    private AddressBuilder addressBuilder;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private SittingTimeMapper sittingTimeMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private DishHelper dishHelper;

    @Test
    void restaurantToRestaurantResponseDTOWhenRestaurantIsValidReturnsDTO() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();

        when(categoryMapper.categoriesToCategoryResponseDTOs(anyList())).thenReturn(Collections.emptyList());

        // Act
        RestaurantResponseDTO result = restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(restaurant.getId(), result.getId());
        assertEquals(restaurant.getName(), result.getName());
    }

    @Test
    void restaurantToRestaurantResponseDTOWhenRestaurantIsNullReturnsNull() {
        // Assert
        assertNull(restaurantMapper.restaurantToRestaurantResponseDTO(null));
    }

    @Test
    void restaurantToRestaurantResponseDTOWhenRestaurantRestaurateurIsNullReturnsRestaurantResponseDTO() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurant.setRestaurateur(null);

        // Act
        RestaurantResponseDTO result = restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);

        // Assert
        assertNotNull(result);
    }

    @Test
    void restaurantToRestaurantResponseDTOWhenRestaurantAddressIsNullReturnsRestaurantResponseDTO() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurant.setAddress(null);

        // Act
        RestaurantResponseDTO result = restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);

        // Assert
        assertNotNull(result);
    }

    @Test
    void restaurantToDetailedRestaurantResponseDTOWhenValidReturnsDTO() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        List<SittingTime> sittingTimes = Collections.emptyList();
        List<Dish> dishes = Collections.emptyList();
        List<Review> reviews = Collections.emptyList();

        when(dishHelper.buildDishResponseDTOs(dishes)).thenReturn(Collections.emptyList());
        when(reviewMapper.reviewsToReviewResponseDTOs(reviews)).thenReturn(Collections.emptyList());
        when(sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes)).thenReturn(Collections.emptyList());

        // Act
        DetailedRestaurantResponseDTO result = restaurantMapper.restaurantToDetailedRestaurantResponseDTO(restaurant, 4.5, sittingTimes, dishes, reviews);

        // Assert
        assertNotNull(result);
        assertEquals(4.5, result.getAverageRating());
    }

    @Test
    void restaurantToDetailedRestaurantResponseDTOWhenRestaurantIsNullReturnsNull() {
        // Assert
        assertNull(restaurantMapper.restaurantToDetailedRestaurantResponseDTO(null, 0, Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
    }

    @Test
    void restaurantRequestDTOToRestaurantWhenRequestIsNullReturnsNull() {
        // Assert
        assertNull(restaurantMapper.restaurantRequestDTOToRestaurant(null));
    }

    @Test
    void restaurantRequestDTOToRestaurantWhenValidReturnsRestaurant() {
        // Arrange
        RestaurantRequestDTO requestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        Address address = TestDataUtil.createTestAddress();

        when(addressBuilder.city(address.getCity())).thenReturn(addressBuilder);
        when(addressBuilder.province(address.getProvince())).thenReturn(addressBuilder);
        when(addressBuilder.street(address.getStreet())).thenReturn(addressBuilder);
        when(addressBuilder.civicNumber(address.getCivicNumber())).thenReturn(addressBuilder);
        when(addressBuilder.postalCode(address.getPostalCode())).thenReturn(addressBuilder);
        when(addressBuilder.build()).thenReturn(address);

        when(restaurantBuilder.name(requestDTO.getName())).thenReturn(restaurantBuilder);
        when(restaurantBuilder.description(requestDTO.getDescription())).thenReturn(restaurantBuilder);
        when(restaurantBuilder.phoneNumber(requestDTO.getPhoneNumber())).thenReturn(restaurantBuilder);
        when(restaurantBuilder.seats(requestDTO.getSeats())).thenReturn(restaurantBuilder);
                        when(restaurantBuilder.address(address)).thenReturn(restaurantBuilder);
        when(restaurantBuilder.build()).thenReturn(TestDataUtil.createTestRestaurant());

        // Act
        Restaurant result = restaurantMapper.restaurantRequestDTOToRestaurant(requestDTO);

        // Assert
        assertNotNull(result);
    }

    @Test
    void updateRestaurantFromRestaurantRequestDTOWhenValidUpdatesRestaurant() {
        // Arrange
        RestaurantRequestDTO requestDTO = mock(RestaurantRequestDTO.class);
        Restaurant restaurant = mock(Restaurant.class);
        Address address = mock(Address.class);

        when(restaurant.getAddress()).thenReturn(address);
        when(requestDTO.getName()).thenReturn("Updated Name");
        when(requestDTO.getDescription()).thenReturn("Updated Description");
        when(requestDTO.getPhoneNumber()).thenReturn("123456789");
        when(requestDTO.getSeats()).thenReturn(100);
        when(requestDTO.getCity()).thenReturn("Updated City");
        when(requestDTO.getProvince()).thenReturn("Updated Province");
        when(requestDTO.getStreet()).thenReturn("Updated Street");
        when(requestDTO.getCivicNumber()).thenReturn("123");
        when(requestDTO.getPostalCode()).thenReturn("54321");

        // Act
        restaurantMapper.updateRestaurantFromRestaurantRequestDTO(restaurant, requestDTO);

        // Assert
        verify(restaurant).setName("Updated Name");
        verify(restaurant).setDescription("Updated Description");
        verify(restaurant).setPhoneNumber("123456789");
        verify(restaurant).setSeats(100);
        verify(address).setCity("Updated City");
        verify(address).setProvince("Updated Province");
        verify(address).setStreet("Updated Street");
        verify(address).setCivicNumber("123");
        verify(address).setPostalCode("54321");
    }

    @Test
    void updateRestaurantFromRestaurantRequestDTOWhenRequestIsNullDoesNotUpdateRestaurant() {
        // Arrange
        RestaurantRequestDTO requestDTO = null;
        Restaurant restaurant = mock(Restaurant.class);

        // Act
        restaurantMapper.updateRestaurantFromRestaurantRequestDTO(restaurant, requestDTO);

        // Assert
        verify(restaurant, never()).setName(any());
    }

    @Test
    void updateRestaurantFromRestaurantRequestDTOWhenRestaurantIsNullDoesNotUpdateRestaurant() {
        // Arrange
        RestaurantRequestDTO requestDTO = mock(RestaurantRequestDTO.class);
        Restaurant restaurant = null;

        // Act
        restaurantMapper.updateRestaurantFromRestaurantRequestDTO(restaurant, requestDTO);

        // Assert
        verifyNoInteractions(requestDTO);
    }

    @Test
    void updateRestaurantFromRestaurantRequestDTOWhenRestaurantAddressIsNullDoesNotUpdateAddress() {
        // Arrange
        RestaurantRequestDTO requestDTO = mock(RestaurantRequestDTO.class);
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurant.getAddress()).thenReturn(null);

        // Act
        restaurantMapper.updateRestaurantFromRestaurantRequestDTO(restaurant, requestDTO);

        // Assert
        verify(requestDTO, never()).getCity();
        verify(requestDTO, never()).getProvince();
        verify(requestDTO, never()).getStreet();
        verify(requestDTO, never()).getCivicNumber();
        verify(requestDTO, never()).getPostalCode();
    }
}