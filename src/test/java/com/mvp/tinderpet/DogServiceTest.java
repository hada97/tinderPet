package com.mvp.tinderpet;


import com.mvp.tinderpet.domain.dog.*;
import com.mvp.tinderpet.domain.user.User;
import com.mvp.tinderpet.repository.DogRepository;
import com.mvp.tinderpet.repository.UserRepository;
import com.mvp.tinderpet.service.DogService;
import com.mvp.tinderpet.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DogServiceTest {

    @InjectMocks
    private DogService dogService;

    @Mock
    private DogRepository dogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Testando o método getDogs
    @Test
    void testGetDogs() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Long userId = 1L;

        // Mockando o userService para retornar o userId
        when(userService.getUserIdByEmailFromGoogle()).thenReturn(userId);

        // Mockando o repositório de usuários
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mockando o repositório de cães para retornar uma lista paginada de cães
        Dog dog = new Dog();
        Page<Dog> dogs = new PageImpl<>(Collections.singletonList(dog));
        when(dogRepository.findByUserIdNotAndIdNotIn(eq(userId), any(), eq(pageable)))
                .thenReturn(dogs);

        // Ação
        Page<Dog> result = dogService.getDogs(page, size);

        // Verificação
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(dogRepository, times(1)).findByUserIdNotAndIdNotIn(eq(userId), any(), eq(pageable));
    }

    // Testando o método getDogById
    @Test
    void testGetDogById_Success() {
        Long dogId = 1L;
        Dog dog = new Dog();
        dog.setId(dogId);
        when(dogRepository.findById(dogId)).thenReturn(Optional.of(dog));

        Dog result = dogService.getDogById(dogId);

        assertNotNull(result);
        assertEquals(dogId, result.getId());
    }

    @Test
    void testGetDogById_ThrowsEntityNotFoundException() {
        Long dogId = 1L;
        when(dogRepository.findById(dogId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dogService.getDogById(dogId));
    }

    // Testando o método createDog
    @Test
    void testCreateDog_Success() {
        // Definindo os dados do cão a ser criado
        DogDto dogDto = new DogDto();
        dogDto.setName("Buddy");
        dogDto.setBreed("Labrador");
        dogDto.setAge(2);
        dogDto.setGender(Sex.valueOf("MALE"));
        dogDto.setSize(Size.valueOf("MEDIUM"));
        dogDto.setProfilePictureUrl("http://image.com");
        dogDto.setDescription("Friendly dog");
        dogDto.setNeutered(true);

        // Simulando o ID do usuário
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // Simulando a resposta do userService e userRepository
        when(userService.getUserIdByEmailFromGoogle()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        // Simulando o repositório de cães para salvar o novo cão
        when(dogRepository.save(any(Dog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Chamando o método
        DogDetail result = dogService.createDog(dogDto);

        // Verificando os resultados
        assertNotNull(result);
        assertEquals("Buddy", result.name());
        assertEquals("Labrador", result.breed());
        assertEquals(2, result.age());
        assertTrue(result.isNeutered());

        // Verificando se o cão foi realmente salvo no banco de dados
        verify(dogRepository, times(1)).save(any(Dog.class));
    }



    // Testando o método updateDog
    @Test
    void testUpdateDog_Success() {
        Long dogId = 1L;
        DogUpdateDto dogUpdateDto = new DogUpdateDto();
        dogUpdateDto.setName("Buddy Updated");

        Dog existingDog = new Dog();
        existingDog.setId(dogId);
        when(dogRepository.findById(dogId)).thenReturn(Optional.of(existingDog));
        when(dogRepository.save(any(Dog.class))).thenReturn(existingDog);

        Dog result = dogService.updateDog(dogId, dogUpdateDto);

        assertNotNull(result);
        assertEquals("Buddy Updated", result.getName());
    }

    @Test
    void testUpdateDog_ThrowsEntityNotFoundException() {
        Long dogId = 1L;
        DogUpdateDto dogUpdateDto = new DogUpdateDto();
        dogUpdateDto.setName("Buddy Updated");

        when(dogRepository.findById(dogId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dogService.updateDog(dogId, dogUpdateDto));
    }

    // Testando o método deleteDog
    @Test
    void testDeleteDog_Success() {
        Long dogId = 1L;
        Dog dog = new Dog();
        dog.setId(dogId);

        when(dogRepository.findById(dogId)).thenReturn(Optional.of(dog));

        dogService.deleteDog(dogId);

        verify(dogRepository, times(1)).delete(dog);
    }

    @Test
    void testDeleteDog_ThrowsEntityNotFoundException() {
        Long dogId = 1L;

        when(dogRepository.findById(dogId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dogService.deleteDog(dogId));
    }

    @Test
    void testDeleteDogWithOAuth2_Success() {
        Long dogId = 1L;
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("user@example.com");

        Dog dog = new Dog();
        dog.setId(dogId);
        User user = new User();
        user.setEmail("user@example.com");
        dog.setUser(user);

        when(dogRepository.findById(dogId)).thenReturn(Optional.of(dog));

        ResponseEntity<?> response = dogService.deleteDog(dogId, authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteDogWithOAuth2_Forbidden() {
        Long dogId = 1L;
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("user@example.com");

        Dog dog = new Dog();
        dog.setId(dogId);
        User user = new User();
        user.setEmail("otheruser@example.com");
        dog.setUser(user);

        when(dogRepository.findById(dogId)).thenReturn(Optional.of(dog));

        ResponseEntity<?> response = dogService.deleteDog(dogId, authentication);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Você não tem permissão para excluir este cão.", response.getBody());
    }
}
