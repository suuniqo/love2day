# Dating App - love2day

El proyecto consiste en la implementación de una aplicación de citas estructurada según el modelo de dominio y los diagramas de secuencia presentados en la entrega anterior. 

El objetivo principal de la aplicación es segmentar de manera limpia las distintas responsabilidades del dominio de negocio (gestión de perfiles, emparejamientos, interacciones y mensajería) en componentes autónomos para mantener firmes los principios de **alta cohesión** y **bajo acoplamiento**.

---

## 🛠️ Limitaciones técnicas y Decisiones de Implementación

Al haber diseñado una arquitectura inicialmente orientada a microservicios, la implementación distribuida estándar implicaría que cada servicio operase como un proyecto Java independiente, comunicándose a través de la red.

Sin embargo, dada la limitación de tiempo y el alcance actual de la evaluación, se ha optado por implementar todos los dominios dentro del mismo proyecto. Los distintos servicios mantienen una estricta separación lógica y se comunican internamente mediante llamadas a métodos. Este enfoque emula la separación de responsabilidades de los microservicios y se alinea con lo que técnicamente se conoce como una **monolito modular**.



---

##  📡 Decisiones de Infraestructura y Comunicación

La integración con proveedores externos de infraestructura (como el `VerificationService` para la validación de identidad) ha sido completamente simulada (*mocked*). En caso de una implementación futura, bastará con modificar la clase que implementa dicha interfaz sin alterar el dominio. 

Por otro lado, como se muestra en el diagrama de secuencia de *Recibir mensaje*, para la comunicación asíncrona entre el backend y la interfaz de usuario se ha optado por **WebSockets** (representado en el flujo a través del bloque `ServidorMensajeria`). Esta decisión se debe a que introducir un broker de eventos habría añadido una complejidad innecesaria al proyecto. La arquitectura de WebSockets está plenamente implementada para enviar la señal `notificarNuevoMensaje(chatId)` en tiempo real a la UI.



---

## 📦 Componentes del Sistema (Microservicios Lógicos)

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

## 📋 Requisitos e Instalación

### Prerrequisitos
* Java Development Kit (JDK) 17 o superior.
* Entorno de desarrollo (IDE) como IntelliJ IDEA, Eclipse o VS Code con soporte para Spring.

### Ejecución
Al estar unificados los módulos dentro del mismo contexto de Spring Boot y utilizar Gradle como gestor de dependencias, el proyecto se puede levantar ejecutando el siguiente comando en la raíz del proyecto:

```bash
./gradlew bootRun
