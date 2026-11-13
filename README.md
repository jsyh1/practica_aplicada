# All Ten

Aplicación de juego matemática desarrollada en Java con JavaFX y conexión a base de datos MySQL. El objetivo principal es resolver ecuaciones usando cuatro números aleatorios para obtener los resultados del 1 al 10.

La aplicación incluye:

- juego principal con interfaz gráfica en JavaFX
- generación automática de números y ecuaciones
- validación de expresiones matemáticas
- historial de partidas y resultados
- consulta de ayuda, reglas y ranking
- persistencia de jugadores y puntuaciones en base de datos

## Descripción del juego

Cada partida genera una combinación de cuatro números. El jugador debe crear expresiones matemáticas con esos números y operadores para obtener todos los valores del 1 al 10.

La lógica del juego se encuentra en la clase `Calculadora`, que valida si la combinación permite encontrar soluciones para cada resultado y genera la ecuación correcta para cada número objetivo.

## Tecnologías

- Java 26
- Maven
- JavaFX
- MySQL / JDBC
- dotenv-java
- JUnit 5

## Requisitos previos

- JDK 26 o superior
- Maven instalado y configurado en el PATH
- Acceso a una base de datos MySQL compatible
- Git para clonar el repositorio

## Configuración de la base de datos

El proyecto usa variables de entorno mediante `dotenv-java`. Debes crear un archivo `.env` en la raíz del proyecto con este formato:

```env
DB_URL=jdbc:mysql://localhost:3306/allten
DB_USER=root
DB_PASSWORD=tu_contraseña
```

Importante:

- no dejes espacios alrededor del signo `=`
- usa la URL correcta de tu servidor MySQL
- asegúrate de que la base de datos exista antes de ejecutar la app

## Ejecución

Desde la raíz del proyecto, ejecuta:

```bash
mvn clean javafx:run
```

También puedes compilar el proyecto con:

```bash
mvn clean package
```

El jar generado quedará en la carpeta `target/`.

## Estructura del proyecto

```text
practica_aplicada/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── co/edu/poli/controller/
│   │   │   ├── co/edu/poli/dao/
│   │   │   ├── co/edu/poli/modelo/
│   │   │   ├── co/edu/poli/servicios/
│   │   │   └── co/edu/poli/vista/
│   │   └── resources/
│   │       └── co/edu/poli/juego/
│   └── test/
│       └── java/
├── pom.xml
├── .env
├── README.md
└── target/
```

## Funcionalidades principales

- inicio de partida con nombre del jugador
- generación aleatoria de números y operadores
- cálculo de expresiones con prioridad de operadores
- validación y comparación de resultados
- registro de partidas finalizadas
- consulta de ranking y resultados previos
- pantallas de ayuda y reglas

## Pantallas

La interfaz principal está construida con FXML y contiene vistas como:

- `juego.fxml` — pantalla principal del juego
- `ranking.fxml` — rankings de jugadores
- `consulta.fxml` — consulta de partidas/resultados
- `reglas.fxml` — reglas del juego
- `ayuda.fxml` — ayuda para el usuario
- `gameOver.fxml` — pantalla final de partida

## Notas

- El proyecto usa JavaFX con carga de recursos dentro del classpath.
- La base de datos se maneja desde la clase `ConexionDB` y los DAOs del paquete `dao`.
- Los tests están ubicados en `src/test/java`.

## Ejecución de pruebas

```bash
mvn test
```

## Autores
Julian Casitllo && Juan Camilo || !Carlie gepeto(GPT).
