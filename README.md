# Proyecto Spring Boot - Gestión de Usuarios

Este proyecto es una aplicación robusta para la gestión de usuarios y sus teléfonos, desarrollada con Spring Boot y Java. Esta aplicación ha sido creada como parte del proceso de postulación al puesto de líder técnico en NTT Data, demostrando habilidades en desarrollo backend, seguridad, y buenas prácticas de programación.

## Características

- Gestión completa de usuarios y sus teléfonos con operaciones CRUD.
- Autenticación segura y autorización con Spring Security.
- Documentación interactiva de la API con OpenAPI/Swagger.
- Configuración avanzada de seguridad utilizando `SecurityFilterChain`.
- Implementación de DTOs para una transferencia de datos eficiente y segura.
- Mapeo inteligente entre entidades y DTOs.
- Pruebas unitarias exhaustivas con Spock Framework.
- Manejo de excepciones personalizado para una mejor experiencia de usuario.

## Requisitos Previos

- JDK 17
- Maven 3.6.3 o superior
- Git (para clonar el repositorio)

## Dependencias Principales

El proyecto utiliza las siguientes dependencias clave:

- Spring Boot 3.3.2 (Web, Security, Data JPA, Validation, Actuator)
- SpringDoc OpenAPI UI 2.2.0
- Lombok
- HSQLDB (para base de datos en memoria)
- Groovy y Spock Framework (para pruebas)
- JSON Web Token (JWT) 0.11.5

Para una lista completa de dependencias, consulta el archivo `pom.xml`.

## Diagramas
    https://drive.google.com/file/d/1j7sqmpIKm_YAV-yQi1Bkzvg5Fsaz3EMY/view?usp=sharing
```bash


## Configuración y Ejecución del Proyecto

## Clonar el repositorio:
   ```bash
   git clone https://github.com/AngeloVenegasVilche/tlbci.git
   cd tlbci
```
## Estructura del proyecto
```bash
src
├── main
│   ├── java
│   │   └── com
│   │       └── ntt
│   │           └── evaluation
│   │               ├── config        # Configuración de seguridad y otras configuraciones
│   │               ├── constant      # Constantes del sistema
│   │               ├── controller    # Controladores REST
│   │               ├── dto           # DTOs para la transferencia de datos
│   │               ├── entity        # Entidades JPA
│   │               ├── errors        # Manejo de excepciones personalizado
│   │               ├── mapper        # Mappers para transformación Entity-DTO
│   │               ├── repository    # Repositorios para persistencia de datos
│   │               ├── service       # Servicios de negocio
│   │               ├── util          # Clases utilitarias
│   │               └── Application.java # Clase principal de Spring Boot
│   └── resources
│       └── application.yaml # Archivo de configuración de Spring Boot
└── test
    └── groovy
        └── com
            └── ntt
                └── evaluation
                    ├── controller    # Pruebas unitarias de controladores
                    ├── dto           # Pruebas unitarias de DTOs
                    ├── mapper        # Pruebas unitarias de mappers
                    └── service       # Pruebas unitarias de servicios
```

## Correr test:
```bash
mvn test
```
## Construir el proyecto con Maven:
```bash
mvn clean install
```
## Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

# Uso de la API
Una vez que la aplicación esté en ejecución, puedes acceder a la documentación interactiva de la API en:
## swagger:
```bash
http://localhost:9090/swagger-ui.html
```
## Endpoint Loging:
`http://localhost:9090/api/login.`

```bash
    email: admin@admin.com
    pass: Just21.
    Estos datos corresponden al usuario con ROl de administrador. 
    Al logearse entregará un token JWT que debe ser unsado en el resto de los endpoint indicando el en header Authorization Bearer {token}
    Ej: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlVXNlciI6IkVESVRPUiIsInN1YiI6ImFkbWluQGFkbWluLmNvbSIsImlhdCI6MTcyMjE4NDQyMCwiZXhwIjoxNzIyMTg1MDIwfQ.fJ5jLJukILNbQ_GSmlzk-1iI3nyt9U-OasYjl3i3M2g
    
```

## Roles de usuario:

```bash
**Rol: ADMIN**
    POST /tl/test/users: Crear usuarios
    GET /tl/test/users: Obtener lista de usuarios
    GET /tl/test/users/{idUser}: Obtener detalles de un usuario por ID
    DELETE /tl/test/users/{idUser}: Eliminar un usuario por ID
    PUT /tl/test/users: Actualizar usuarios
    POST /tl/test/phones: Crear teléfonos
    DELETE /tl/test/phones/{phoneId}/{userId}: Eliminar un teléfono por ID y usuario ID
    PUT /tl/test/phones: Actualizar teléfonos

**Rol: USER**
    GET /tl/test/users: Obtener lista de usuarios
    GET /tl/test/users/{idUser}: Obtener detalles de un usuario por ID

**Rol: EDITOR**
    GET /tl/test/users: Obtener lista de usuarios
    GET /tl/test/users/{idUser}: Obtener detalles de un usuario por ID
    PUT /tl/test/users: Actualizar usuarios
    PUT /tl/test/phones: Actualizar teléfonos

**Rutas Públicas (Acceso sin autenticación)**
    GET /security/loginUser: Acceso a la página de login
```
## Curl de creacion de usuario
    Ej: reemplace el token por el del administrador, paso anterior  he indique el rol que tendrá el usuario  "roles" :["USER"] :
```bash
curl --location 'http://localhost:9090/tl/test/users' \
    --header 'accept: application/json' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlVXNlciI6IkVESVRPUiIsInN1YiI6ImFkbWluQGFkbWluLmNvbSIsImlhdCI6MTcyMjE4Mzg3NCwiZXhwIjoxNzIyMTg0NDc0fQ.59Uc6eFxWvN60BJgEqzAfYv9UtGMsI0FO8_UHBYRJts' \
    --header 'Cookie: JSESSIONID=BEC52BC480F0B393BB2F9ACD43341E93' \
    --data-raw '{
        "email": "angelo.venegas@hotmail.it",
        "name": "Angelo Venegas",
        "password": "Jus12.",
        "phones": [
            {
                "citycode": "45",
                "contrycode": "56",
                "number": "978526198"
            },
            {
                "citycode": "45",
                "contrycode": "56",
                "number": "458796258"
            }
        ],
        "roles" :["USER"] 
    }'
```

```