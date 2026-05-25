# Dating App - love2day

El proyecto consiste en la implementación de una aplicación de citas estructurada según el modelo de dominio y los diagramas de secuencia presentados en la entrega anterior. 

El objetivo principal de la aplicación es segmentar de manera limpia las distintas responsabilidades del dominio de negocio (gestión de perfiles, emparejamientos, interacciones y mensajería) en componentes autónomos para mantener firmes los principios de **alta cohesión** y **bajo acoplamiento**.


---

## Limitaciones técnicas y Decisiones de Implementación

Al haber diseñado una arquitectura inicialmente orientada a microservicios, la implementación distribuida estándar implicaría que cada servicio operase como un proyecto Java independiente, comunicándose a través de la red. En concreto, en *gradle* habría que crear un subproyecto por cada servicio con una estructura parecida a la siguiente:

```
love2day/
├── build.gradle          ← root
├── settings.gradle       ← lista de módulos
├── account-service/
│   ├── build.gradle
│   └── src/
├─ verification-service/
│   ├── build.gradle
│   └── src/
├── profile-service/
│   ├── build.gradle
│   └── src/
...
```

Sin embargo, dada la limitación de tiempo y a la escasez de recursos sobre como llevarlo a cabo, se ha optado por implementar todos los dominios dentro del mismo proyecto. Los distintos servicios mantienen una estricta separación lógica y se comunican internamente mediante llamadas a métodos. Este enfoque emula la separación de responsabilidades de los microservicios y se alinea con lo que técnicamente se conoce como un **monolito modular**.


---

##  Decisiones de Infraestructura y Comunicación

La integración con el proveedor externo de verificación de identidad, debido a que la mayoría son de pago, se ha simulado utilizando el `DummyVerifier`. Para poder cambiar de proveedor fácilmenete, se ha creado la interfaz `Verifier`, que el *dummy* implementa, y el `Verifier` se inyecta en el servicio. Usando  este patrón de inyección de dependencias, aumenta la flexibilidad y la resistencia a variaciones.

Por otro lado, como se muestra en el diagrama de secuencia de *Recibir mensaje*, para la comunicación asíncrona entre el backend y la interfaz de usuario se ha optado por **WebSockets** (representado en el flujo a través del bloque `ServidorMensajeria`). Esta decisión se debe a que introducir un broker de eventos habría añadido una complejidad innecesaria al proyecto. La arquitectura de WebSockets está plenamente implementada para enviar la señal `notificarNuevoMensaje(chatId)` en tiempo real a la UI. Para escuchar el evento de *verificación completada* el cliente se conecta a `ws/verification`. Para escuchar al evento de *mensaje recibido* el cliente se conecta a `ws/messages`.


---

## Componentes del Sistema (Microservicios Lógicos)

El ecosistema de la aplicación se divide en 6 servicios principales, cada uno encargado de una vertical exclusiva del negocio:


| Servicio | Descripción | Responsabilidad Principal |
| :--- | :--- | :--- |
| **Account Service** | Gestión de Identidad y Seguridad | Manejo de identificadores únicos, credenciales base y comunicación con el servicio externo (simulado) para la verificación de identidad de la cuenta. |
| **Profile Service** | Gestión de Perfiles | Administración de la información pública del usuario: edición de datos personales, fotos, biografía y preferencias de búsqueda. |
| **Discovery Service** | Motor de Recomendación | Filtrado de usuarios potenciales basados en la ubicación, edad y preferencias definidas en el perfil. |
| **Swipe Service** | Registro de Interacciones | Procesamiento de las acciones del usuario (likes o dislikes) de forma eficiente. |
| **Match Service** | Lógica de Emparejamiento | Comprobación en tiempo real de la reciprocidad de interacciones entre dos usuarios para generar un "Match". |
| **Chat Service** | Mensajería | Gestión del canal de comunicación privado, historial de mensajes y estado de la conversación entre perfiles emparejados. |



---

## Requisitos e Instalación

### Prerrequisitos
* Java Development Kit (JDK) 17 o superior.
* Entorno de desarrollo (IDE) como IntelliJ IDEA, Eclipse o VS Code con soporte para Spring.

### Ejecución
Al estar unificados los módulos dentro del mismo contexto de Spring Boot y utilizar Gradle como gestor de dependencias, el proyecto se puede levantar ejecutando el siguiente comando en la raíz del proyecto:

```bash
./gradlew bootRun
