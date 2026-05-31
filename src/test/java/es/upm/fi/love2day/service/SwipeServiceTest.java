package es.upm.fi.love2day.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import es.upm.fi.love2day.exceptions.BadRequestException;
import es.upm.fi.love2day.model.SwipeType;
import es.upm.fi.love2day.model.Swipe;
import es.upm.fi.love2day.repository.SwipesRepository;

@ExtendWith(MockitoExtension.class)
class SwipeServiceTest {

    @Mock
    private SwipesRepository swipesRepository;

    @InjectMocks
    private SwipeService swipeService;

    @Nested
    @DisplayName("Tests para el método createSwipe()")
    class CreateSwipeTests {

        // C1: sourceId == targetId -> BadRequestException
        @Test
        @DisplayName("Camino 1: Lanza excepción si el sourceId y targetId son iguales")
        void shouldThrowException_WhenSourceAndTargetAreSame() {
            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                swipeService.createSwipe(1L, 1L, SwipeType.LIKE));

            assertEquals("Swipe cannot be made to self: 1", exception.getMessage());
            verifyNoInteractions(swipesRepository);
        }

        // C2: el swipe ya existe -> BadRequestException
        @Test
        @DisplayName("Camino 2: Lanza excepción si el swipe ya existe en la base de datos")
        void shouldThrowException_WhenSwipeAlreadyExists() {
            when(swipesRepository.existsBySourceIdAndTargetId(1L, 2L)).thenReturn(true);

            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                swipeService.createSwipe(1L, 2L, SwipeType.LIKE));

            assertEquals("Swipe already made from 1 to 2", exception.getMessage());
            verify(swipesRepository, never()).save(any());
        }

        // C3: validaciones superadas -> swipe creado y guardado
        @Test
        @DisplayName("Camino 3: Crea y guarda el swipe correctamente si pasa las validaciones")
        void shouldCreateSwipe_WhenValidationsPass() {
            when(swipesRepository.existsBySourceIdAndTargetId(1L, 2L)).thenReturn(false);

            SwipeService.SwipeResult result = swipeService.createSwipe(1L, 2L, SwipeType.LIKE);

            assertNotNull(result);
            assertNotNull(result.swipe());
            assertEquals(1L, result.swipe().getSourceId());
            assertEquals(2L, result.swipe().getTargetId());
            verify(swipesRepository, times(1)).save(any(Swipe.class));
        }
    }
}
