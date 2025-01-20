package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.restaurant.RestaurateurAlreadyHasRestaurantException;
import com.example.foody.helper.RestaurantHelper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Address;
import com.example.foody.model.Category;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.*;
import com.example.foody.repository.CategoryRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.service.AddressService;
import com.example.foody.service.CategoryService;
import com.example.foody.service.EmailService;
import com.example.foody.service.GoogleDriveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private RestaurantHelper restaurantHelper;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AddressService addressService;

    @Mock
    private GoogleDriveService googleDriveService;

    @Mock
    private EmailService emailService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private void mockSecurityContext(User user) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void saveWhenRestaurantIsValidReturnsRestaurantResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Address address = TestDataUtil.createTestAddress();
        mockSecurityContext(restaurateur);

        when(restaurantMapper.restaurantRequestDTOToRestaurant(restaurantRequestDTO)).thenReturn(restaurant);
        when(addressService.save(any())).thenReturn(address);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant))
                .thenReturn(TestDataUtil.createTestRestaurantResponseDTO());

        // Act
        RestaurantResponseDTO restaurantResponseDTO = restaurantService.save(restaurantRequestDTO);

        // Assert
        assertNotNull(restaurantResponseDTO);
        verify(emailService, times(1)).sendTemplatedEmail(anyString(), any(), any());
    }

    @Test
    void saveWhenCategoryProvidedReturnsRestaurantResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Address address = TestDataUtil.createTestAddress();
        Category category = TestDataUtil.createTestCategory();
        restaurantRequestDTO.setCategories(List.of(category.getId()));
        mockSecurityContext(restaurateur);

        when(restaurantMapper.restaurantRequestDTOToRestaurant(restaurantRequestDTO)).thenReturn(restaurant);
        when(addressService.save(any())).thenReturn(address);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant))
                .thenReturn(TestDataUtil.createTestRestaurantResponseDTO());
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryService.addRestaurant(category.getId(), restaurant)).thenReturn(category);

        // Act
        RestaurantResponseDTO restaurantResponseDTO = restaurantService.save(restaurantRequestDTO);

        // Assert
        assertNotNull(restaurantResponseDTO);
        verify(categoryService, times(1)).addRestaurant(category.getId(), restaurant);
    }

    @Test
    void saveWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Address address = TestDataUtil.createTestAddress();
        mockSecurityContext(restaurateur);

        when(restaurantMapper.restaurantRequestDTOToRestaurant(restaurantRequestDTO)).thenReturn(restaurant);
        when(addressService.save(any())).thenReturn(address);
        doThrow(new RuntimeException()).when(restaurantRepository).save(restaurant);

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> restaurantService.save(restaurantRequestDTO));
    }

    @Test
    void saveWhenUserAlreadyHasRestaurantThrowsRestaurateurAlreadyHasRestaurantException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        mockSecurityContext(restaurateur);

        when(restaurantRepository.existsByRestaurateur_Id(restaurateur.getId())).thenReturn(true);

        // Act & Assert
        assertThrows(RestaurateurAlreadyHasRestaurantException.class, () -> restaurantService.save(restaurantRequestDTO));
    }

    @Test
    void saveWhenPhotoBase64IsNullSetsPhotoUrlToNull() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        restaurantRequestDTO.setPhotoBase64(null);
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Address address = TestDataUtil.createTestAddress();
        mockSecurityContext(restaurateur);

        when(restaurantMapper.restaurantRequestDTOToRestaurant(restaurantRequestDTO)).thenReturn(restaurant);
        when(addressService.save(any())).thenReturn(address);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant))
                .thenReturn(TestDataUtil.createTestRestaurantResponseDTO());

        // Act
        RestaurantResponseDTO restaurantResponseDTO = restaurantService.save(restaurantRequestDTO);

        // Assert
        assertNotNull(restaurantResponseDTO);
        assertNull(restaurant.getPhotoUrl());
    }

    @Test
    void saveWhenCategoriesEmptyDoesNotAddCategories() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        restaurantRequestDTO.setCategories(List.of());
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Address address = TestDataUtil.createTestAddress();
        mockSecurityContext(restaurateur);

        when(restaurantMapper.restaurantRequestDTOToRestaurant(restaurantRequestDTO)).thenReturn(restaurant);
        when(addressService.save(any())).thenReturn(address);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant))
                .thenReturn(TestDataUtil.createTestRestaurantResponseDTO());

        // Act
        RestaurantResponseDTO restaurantResponseDTO = restaurantService.save(restaurantRequestDTO);

        // Assert
        assertNotNull(restaurantResponseDTO);
        assertTrue(restaurant.getCategories().isEmpty());
    }

    @Test
    void findAllWhenUserIsAdminReturnsAllRestaurants() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        List<Restaurant> restaurants = List.of(TestDataUtil.createTestRestaurant());
        mockSecurityContext(admin);

        when(restaurantRepository.findAll()).thenReturn(restaurants);
        when(restaurantHelper.buildDetailedRestaurantResponseDTOs(restaurants))
                .thenReturn(List.of(TestDataUtil.createTestDetailedRestaurantResponseDTO()));

        // Act
        List<DetailedRestaurantResponseDTO> detailedRestaurantResponseDTOs = restaurantService.findAll();

        // Assert
        assertNotNull(detailedRestaurantResponseDTOs);
        assertEquals(1, detailedRestaurantResponseDTOs.size());
    }

    @Test
    void findAllWhenUserIsModeratorReturnsAllRestaurants() {
        // Arrange
        ModeratorUser moderator = TestDataUtil.createTestModeratorUser();
        List<Restaurant> restaurants = List.of(TestDataUtil.createTestRestaurant());
        mockSecurityContext(moderator);

        when(restaurantRepository.findAll()).thenReturn(restaurants);
        when(restaurantHelper.buildDetailedRestaurantResponseDTOs(restaurants))
                .thenReturn(List.of(TestDataUtil.createTestDetailedRestaurantResponseDTO()));

        // Act
        List<DetailedRestaurantResponseDTO> detailedRestaurantResponseDTOs = restaurantService.findAll();

        // Assert
        assertNotNull(detailedRestaurantResponseDTOs);
        assertEquals(1, detailedRestaurantResponseDTOs.size());
    }

    @Test
    void findAllWhenUserIsNotAdminNorModeratorReturnsApprovedRestaurants() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        List<Restaurant> approvedRestaurants = List.of(TestDataUtil.createTestRestaurant());
        mockSecurityContext(customer);

        when(restaurantRepository.findAllByApproved(true)).thenReturn(approvedRestaurants);
        when(restaurantHelper.buildDetailedRestaurantResponseDTOs(approvedRestaurants))
                .thenReturn(List.of(TestDataUtil.createTestDetailedRestaurantResponseDTO()));

        // Act
        List<DetailedRestaurantResponseDTO> detailedRestaurantResponseDTOs = restaurantService.findAll();

        // Assert
        assertNotNull(detailedRestaurantResponseDTOs);
        assertEquals(1, detailedRestaurantResponseDTOs.size());
    }

    @Test
    void findByIdWhenRestaurantExistsAndUserIsAdminReturnsDetailedRestaurantResponseDTO() {
        // Arrange
        AdminUser adminUser = TestDataUtil.createTestAdminUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(adminUser);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant))
                .thenReturn(TestDataUtil.createTestDetailedRestaurantResponseDTO());

        // Act
        DetailedRestaurantResponseDTO responseDTO = restaurantService.findById(restaurant.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(restaurantHelper, times(1)).buildDetailedRestaurantResponseDTO(restaurant);
    }

    @Test
    void findByIdWhenRestaurantExistsAndUserIsModeratorReturnsDetailedRestaurantResponseDTO() {
        // Arrange
        ModeratorUser moderator = TestDataUtil.createTestModeratorUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(moderator);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant))
                .thenReturn(TestDataUtil.createTestDetailedRestaurantResponseDTO());

        // Act
        DetailedRestaurantResponseDTO responseDTO = restaurantService.findById(restaurant.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(restaurantHelper, times(1)).buildDetailedRestaurantResponseDTO(restaurant);
    }

    @Test
    void findByIdWhenRestaurantExistsAndUserIsRestaurateurReturnsDetailedRestaurantResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurant.setRestaurateur(restaurateur);
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findByIdAndApproved(restaurant.getId(), true)).thenReturn(Optional.of(restaurant));
        when(restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant))
                .thenReturn(TestDataUtil.createTestDetailedRestaurantResponseDTO());

        // Act
        DetailedRestaurantResponseDTO responseDTO = restaurantService.findById(restaurant.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(restaurantRepository, times(1)).findByIdAndApproved(restaurant.getId(), true);
        verify(restaurantHelper, times(1)).buildDetailedRestaurantResponseDTO(restaurant);
    }

    @Test
    void findByIdWhenRestaurantDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        CustomerUser user = TestDataUtil.createTestCustomerUser();
        mockSecurityContext(user);

        when(restaurantRepository.findByIdAndApproved(0L, true))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> restaurantService.findById(0L));
        verify(restaurantRepository, times(1)).findByIdAndApproved(0L, true);
    }

    @Test
    void findByIdWhenUserIsNotAdminOrRestaurateurThrowsEntityNotFoundException() {
        // Arrange
        CustomerUser user = TestDataUtil.createTestCustomerUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(user);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> restaurantService.findById(restaurant.getId()));
    }

    @Test
    void findByRestaurateurWhenRestaurateurExistsReturnsDetailedRestaurantResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurant.setRestaurateur(restaurateur);

        when(restaurantRepository.findByRestaurateur_Id(restaurateur.getId())).thenReturn(Optional.of(restaurant));
        when(restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant))
                .thenReturn(TestDataUtil.createTestDetailedRestaurantResponseDTO());

        // Act
        DetailedRestaurantResponseDTO responseDTO = restaurantService.findByRestaurateur(restaurateur.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(restaurantRepository, times(1)).findByRestaurateur_Id(restaurateur.getId());
        verify(restaurantHelper, times(1)).buildDetailedRestaurantResponseDTO(restaurant);
    }

    @Test
    void findByRestaurateurWhenRestaurantDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();

        when(restaurantRepository.findByRestaurateur_Id(restaurateur.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> restaurantService.findByRestaurateur(restaurateur.getId()));
        verify(restaurantRepository, times(1)).findByRestaurateur_Id(restaurateur.getId());
    }

    @Test
    void findAllByCategoryWhenUserIsAdminReturnsDetailedRestaurantResponseDTOs() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        Category category = TestDataUtil.createTestCategory();
        List<Restaurant> restaurants = List.of(TestDataUtil.createTestRestaurant());
        mockSecurityContext(admin);

        when(restaurantRepository.findAllByCategory_Id(category.getId())).thenReturn(restaurants);
        when(restaurantHelper.buildDetailedRestaurantResponseDTOs(restaurants))
                .thenReturn(List.of(TestDataUtil.createTestDetailedRestaurantResponseDTO()));

        // Act
        List<DetailedRestaurantResponseDTO> detailedRestaurantResponseDTOs = restaurantService.findAllByCategory(category.getId());

        // Assert
        assertNotNull(detailedRestaurantResponseDTOs);
        assertEquals(1, detailedRestaurantResponseDTOs.size());
    }

    @Test
    void findAllByCategoryWhenUserIsModeratorReturnsDetailedRestaurantResponseDTOs() {
        // Arrange
        ModeratorUser moderator = TestDataUtil.createTestModeratorUser();
        Category category = TestDataUtil.createTestCategory();
        List<Restaurant> restaurants = List.of(TestDataUtil.createTestRestaurant());
        mockSecurityContext(moderator);

        when(restaurantRepository.findAllByCategory_Id(category.getId())).thenReturn(restaurants);
        when(restaurantHelper.buildDetailedRestaurantResponseDTOs(restaurants))
                .thenReturn(List.of(TestDataUtil.createTestDetailedRestaurantResponseDTO()));

        // Act
        List<DetailedRestaurantResponseDTO> detailedRestaurantResponseDTOs = restaurantService.findAllByCategory(category.getId());

        // Assert
        assertNotNull(detailedRestaurantResponseDTOs);
        assertEquals(1, detailedRestaurantResponseDTOs.size());
    }

    @Test
    void findAllByCategoryWhenUserIsNotAdminNorModeratorReturnsDetailedRestaurantResponseDTOs() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Category category = TestDataUtil.createTestCategory();
        List<Restaurant> approvedRestaurants = List.of(TestDataUtil.createTestRestaurant());
        mockSecurityContext(customer);

        when(restaurantRepository.findAllByCategory_IdAndApproved(category.getId(), true)).thenReturn(approvedRestaurants);
        when(restaurantHelper.buildDetailedRestaurantResponseDTOs(approvedRestaurants))
                .thenReturn(List.of(TestDataUtil.createTestDetailedRestaurantResponseDTO()));

        // Act
        List<DetailedRestaurantResponseDTO> detailedRestaurantResponseDTOs = restaurantService.findAllByCategory(category.getId());

        // Assert
        assertNotNull(detailedRestaurantResponseDTOs);
        assertEquals(1, detailedRestaurantResponseDTOs.size());
    }

    @Test
    void approveByIdWhenRestaurantExistsApprovesRestaurantAndSendsEmail() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurant.setApproved(false);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant))
                .thenReturn(TestDataUtil.createTestDetailedRestaurantResponseDTO());

        // Act
        DetailedRestaurantResponseDTO responseDTO = restaurantService.approveById(restaurant.getId());

        // Assert
        assertNotNull(responseDTO);
        assertTrue(restaurant.isApproved());
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(restaurantRepository, times(1)).save(restaurant);
        verify(emailService, times(1)).sendTemplatedEmail(anyString(), any(), any());
        verify(restaurantHelper, times(1)).buildDetailedRestaurantResponseDTO(restaurant);
    }

    @Test
    void approveByIdWhenRestaurantDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(restaurantRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> restaurantService.approveById(0L));
        verify(restaurantRepository, times(1)).findById(0L);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
        verify(emailService, never()).sendTemplatedEmail(anyString(), any(), any());
    }

    @Test
    void approveByIdWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurant.setApproved(false);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        doThrow(new RuntimeException()).when(restaurantRepository).save(any(Restaurant.class));

        // Act & Assert
        assertThrows(EntityEditException.class, () -> restaurantService.approveById(restaurant.getId()));
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(restaurantRepository, times(1)).save(restaurant);
        verify(emailService, never()).sendTemplatedEmail(anyString(), any(), any());
    }

    @Test
    void updateWhenRestaurantExistsReturnsDetailedRestaurantResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        Address updatedAddress = TestDataUtil.createTestAddress();
        Category category = TestDataUtil.createTestCategory();
        restaurant.setCategories(List.of(category));
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(eq(restaurant.getId()))).thenReturn(Optional.of(restaurant));
        when(addressService.update(eq(restaurant.getAddress().getId()), any(Address.class))).thenReturn(updatedAddress);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(restaurantHelper.buildDetailedRestaurantResponseDTO(any(Restaurant.class)))
                .thenReturn(TestDataUtil.createTestDetailedRestaurantResponseDTO());

        // Act
        DetailedRestaurantResponseDTO responseDTO = restaurantService.update(restaurant.getId(), restaurantRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(restaurantRepository, times(1)).findById(eq(restaurant.getId()));
        verify(addressService, times(1)).update(eq(restaurant.getAddress().getId()), any(Address.class));
        verify(categoryRepository, times(restaurantRequestDTO.getCategories().size())).findById(anyLong());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
        verify(restaurantHelper, times(1)).buildDetailedRestaurantResponseDTO(any(Restaurant.class));
    }

    @Test
    void updateWhenRestaurantDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(eq(0L))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> restaurantService.update(0L, restaurantRequestDTO));
        verify(restaurantRepository, times(1)).findById(eq(0L));
        verify(addressService, never()).update(anyLong(), any(Address.class));
        verify(categoryRepository, never()).findById(anyLong());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void updateWhenRestaurateurIsNotOwnerThrowsForbiddenRestaurateurAccessException() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurateurUser otherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        otherRestaurateur.setId(2L);
        restaurant.setRestaurateur(restaurateur);
        mockSecurityContext(otherRestaurateur);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(ForbiddenRestaurantAccessException.class, () -> restaurantService.update(restaurant.getId(), restaurantRequestDTO));
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(addressService, never()).update(anyLong(), any(Address.class));
        verify(categoryRepository, never()).findById(anyLong());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void updateWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        RestaurantRequestDTO restaurantRequestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        Address updatedAddress = TestDataUtil.createTestAddress();
        Category category = TestDataUtil.createTestCategory();
        restaurant.setCategories(List.of(category));
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(eq(restaurant.getId()))).thenReturn(Optional.of(restaurant));
        when(addressService.update(eq(restaurant.getAddress().getId()), any(Address.class))).thenReturn(updatedAddress);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(restaurantRepository.save(any(Restaurant.class))).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(EntityEditException.class, () -> restaurantService.update(restaurant.getId(), restaurantRequestDTO));
        verify(restaurantRepository, times(1)).findById(eq(restaurant.getId()));
        verify(addressService, times(1)).update(eq(restaurant.getAddress().getId()), any(Address.class));
        verify(categoryRepository, times(restaurantRequestDTO.getCategories().size())).findById(anyLong());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void removeWhenRestaurantExistsAndUserIsOwnerSavesUpdatedRestaurant() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        RestaurateurUser restaurateurUser = TestDataUtil.createTestRestaurateurUser();
        restaurant.setRestaurateur(restaurateurUser);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        // Act
        boolean result = restaurantService.remove(restaurant.getId());

        // Assert
        assertTrue(result);
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    void removeWhenRestaurantDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(restaurantRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> restaurantService.remove(0L));
        verify(restaurantRepository, times(1)).findById(0L);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void removeWhenSaveFailsThrowsEntityDeletionException() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        RestaurateurUser restaurateurUser = TestDataUtil.createTestRestaurateurUser();
        restaurant.setRestaurateur(restaurateurUser);
        Category category = TestDataUtil.createTestCategory();
        restaurant.setCategories(List.of(category));

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        doThrow(new RuntimeException()).when(restaurantRepository).save(any(Restaurant.class));

        // Act & Assert
        assertThrows(EntityDeletionException.class, () -> restaurantService.remove(restaurant.getId()));
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(restaurantRepository, times(1)).save(restaurant);
    }
}