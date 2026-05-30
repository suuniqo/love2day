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

    // Si tryCreateMatch utiliza otros repositorios (ej. MatchRepository), 
    // tendrías que declararlos aquí también con @Mock.

    @InjectMocks
    private SwipeService swipeService;

    @Nested
    @DisplayName("Tests para el método createSwipe()")
    class CreateSwipeTests {

        @Test
        @DisplayName("Camino 1: Lanza excepción si el sourceId y targetId son iguales")
        void shouldThrowException_WhenSourceAndTargetAreSame() {
            // Arrange (Preparar datos)
            Long sourceId = 1L;
            Long targetId = 1L;
            SwipeType type = SwipeType.LIKE;

            // Act & Assert (Ejecutar y comprobar excepción)
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                swipeService.createSwipe(sourceId, targetId, type);
            });

            assertEquals("Swipe cannot be made to self: 1", exception.getMessage());
            
            // Verificamos que el repositorio NUNCA fue llamado
            verifyNoInteractions(swipesRepository);
        }

        @Test
        @DisplayName("Camino 2: Lanza excepción si el swipe ya existe en la base de datos")
        void shouldThrowException_WhenSwipeAlreadyExists() {
            // Arrange
            Long sourceId = 1L;
            Long targetId = 2L;
            SwipeType type = SwipeType.LIKE;

            // Simulamos que el repositorio devuelve true (el swipe ya existe)
            when(swipesRepository.existsBySourceIdAndTargetId(sourceId, targetId)).thenReturn(true);

            // Act & Assert
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                swipeService.createSwipe(sourceId, targetId, type);
            });

            assertEquals("Swipe already made from 1 to 2", exception.getMessage());
            
            // Verificamos que el método de guardado NUNCA se llamó
            verify(swipesRepository, never()).save(any());
        }

        @Test
        @DisplayName("Camino 3: Crea y guarda el swipe correctamente si pasa las validaciones")
        void shouldCreateSwipe_WhenValidationsPass() {
            // Arrange
            Long sourceId = 1L;
            Long targetId = 2L;
            SwipeType type = SwipeType.LIKE;

            // Simulamos que el swipe NO existe previamente
            when(swipesRepository.existsBySourceIdAndTargetId(sourceId, targetId)).thenReturn(false);

            // Act (Ejecutamos el método)
            SwipeService.SwipeResult result = swipeService.createSwipe(sourceId, targetId, type);

            // Assert (Comprobamos los resultados)
            assertNotNull(result);
            assertNotNull(result.swipe()); 
            assertEquals(sourceId, result.swipe().getSourceId());
            assertEquals(targetId, result.swipe().getTargetId());
            
            // Verificamos que el método save() del repositorio fue llamado exactamente 1 vez
            verify(swipesRepository, times(1)).save(any(Swipe.class));
        }
    }
}
