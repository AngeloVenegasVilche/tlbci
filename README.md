# Proyecto Spring Boot - Gestión de Usuarios

El ejercicio se consideró la creación de usuarios y sus teléfonos, para esto se asignaron dos roles dentro de la aplicación:
## Rol Administrador:
Su función es dar de altos los usuarios del sistema y puede modificar, eliminar, cambiar estados a cualquier usuario del sistema, así como sus teléfonos.

##  Rol Usuario:
Solo puede modificar sus propios datos y modificar sus teléfonos.
Se omitió intencionalmente devolver el token dentro de la creación puesto que es considerado un problema de seguridad.


## Características

- Gestión completa de usuarios y sus teléfonos con operaciones CRUD.
- Autenticación segura y autorización con Spring Security.
- Documentación interactiva de la API con OpenAPI/Swagger.
- Configuración avanzada de seguridad utilizando `SecurityFilterChain`.
- Implementación de DTOs para una transferencia de datos eficiente y segura.
- Mapeo  entre entidades y DTOs.
- Pruebas unitarias  con Spock Framework.
- Manejo de excepciones personalizado para una mejor experiencia de usuario.
- Manejo de logs info a criterio.

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

## Probar el micro servicio:
```
Probar la Aplicación
Inicio de Sesión: Para comenzar, ingresa al endpoint de login en http://localhost:9090/api/login 
utilizando las credenciales de administrador previamente descritas.

Acceso a Endpoints de Administrador: 
Después de iniciar sesión, tendrás acceso a todos los endpoints para el rol "ADMIN" en admin/*. 
Las funcionalidades disponibles incluyen:

Gestión de Usuarios:
            -Activar o desactivar usuarios.
            -Crear nuevos usuarios.
            -Modificar datos personales de los usuarios.
            -Eliminar usuarios.
            -Listar todos los usuarios.
            -Obtener detalles de un usuario específico.
Gestión de Teléfonos:
            -Agregar teléfonos.
            -Modificar detalles de teléfonos.
            -Eliminar teléfonos.

Usuarios con Rol "USER": 
Los usuarios creados con el rol "USER" tienen acceso a las siguientes funcionalidades:

Gestión de Datos Personales:
            -Modificar sus propios datos personales.
            -Obtener sus datos personales.
Gestión de Teléfonos:
            -Modificar sus propios teléfonos.
            -Agregar teléfonos.
            -Eliminar teléfonos.
Para más detalles, consulta la documentación generada con Swagger : http://localhost:9090/swagger-ui/index.html

Además, puedes importar la colección de Postman "xxxxxxxxx" disponible en la carpeta resource.
```



