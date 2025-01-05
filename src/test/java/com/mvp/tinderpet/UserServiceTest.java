package com.mvp.tinderpet;

import com.mvp.tinderpet.domain.user.User;
import com.mvp.tinderpet.location.GeocodingService;
import com.mvp.tinderpet.repository.UserRepository;
import com.mvp.tinderpet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private OAuth2User oAuth2User;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Testando o método getAll
    @Test
    void testGetAllUsers() {
        int page = 0;
        int size = 10;
        Pageable pageable = Pageable.ofSize(size);
        User user = new User();
        user.setEmail("user@example.com");
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<User> result = userService.getAll(page, size);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(pageable);
    }

    // Testando o método registerNewUser
    @Test
    void testRegisterNewUser() {
        String email = "user@example.com";
        String name = "User Test";
        String address = "Av. Paulista, 1111 - Bela Vista, São Paulo - SP, 01311-920";  // Endereço atualizado

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setAddress(address);

        // Simulando o comportamento do repositório de usuários
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Simulando o comportamento do serviço de geolocalização com um endereço real
        when(geocodingService.geocode(eq(address))).thenReturn(new double[]{});  // Aqui estamos usando o endereço real

        // Chamando o método de registro
        userService.registerNewUser(email, name);

        // Verificando se o save do usuário foi chamado uma vez
        verify(userRepository, times(1)).save(any(User.class));

        // Verificando se o geocoding foi chamado com o endereço correto
        verify(geocodingService, times(1)).geocode(eq(address));
    }



    // Testando o método deleteUser
    @Test
    void testDeleteUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        boolean result = userService.deleteUser(userId);

        assertTrue(result);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_Failure() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(userId);

        assertFalse(result);
        verify(userRepository, times(0)).delete(any());
    }

    // Testando o método getUserIdByEmailFromGoogle
    @Test
    void testGetUserIdByEmailFromGoogle_Success() {
        // Email e ID do usuário simulados
        String email = "user@example.com";
        Long expectedUserId = 1L;

        // Criando o mock do OAuth2User
        OAuth2User oauthUser = mock(OAuth2User.class);
        when(oauthUser.getAttribute("email")).thenReturn(email);

        // Criando o mock do Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(oauthUser);

        // Criando o mock do SecurityContext e associando o Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);  // Configurando o SecurityContextHolder com o mock

        // Criando o usuário esperado no repositório
        User user = new User();
        user.setId(expectedUserId);
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Chamando o método do UserService
        Long result = userService.getUserIdByEmailFromGoogle();

        // Verificando o resultado
        assertEquals(expectedUserId, result);

        // Limpando o SecurityContextHolder após o teste
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetUserIdByEmailFromGoogle_ThrowsException() {
        // Criando o mock do OAuth2User
        OAuth2User oauthUser = mock(OAuth2User.class);

        // Simulando que não será encontrado o atributo "email" do OAuth2User
        when(oauthUser.getAttribute("email")).thenReturn(null); // Email está nulo

        // Criando o mock do Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(oauthUser);

        // Criando o mock do SecurityContext e associando o Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Configurando o SecurityContextHolder com o mock do SecurityContext
        SecurityContextHolder.setContext(securityContext);

        // Chamando o método do UserService
        // Esperando que o método lance uma NoSuchElementException por causa do email nulo
        assertThrows(NoSuchElementException.class, () -> userService.getUserIdByEmailFromGoogle());

        // Limpando o SecurityContextHolder após o teste
        SecurityContextHolder.clearContext();
    }

    // Testando o método existsByEmail
    @Test
    void testExistsByEmail() {
        String email = "user@example.com";

        when(userRepository.findByEmail(email)).thenReturn(new User());

        boolean result = userService.existsByEmail(email);

        assertTrue(result);
    }

    @Test
    void testExistsByEmail_NotFound() {
        String email = "user@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        boolean result = userService.existsByEmail(email);

        assertFalse(result);
    }


}
