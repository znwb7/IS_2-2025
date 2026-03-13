# Proyecto Comedor Universitario — IS 2 (2025)

Aplicación desarrollada en **Java** que simula el funcionamiento de un sistema para la gestión del comedor universitario de la Universidad Central de Venezuela. El sistema permite gestionar operaciones relacionadas con usuarios, recargas de saldo, pagos y control de acceso al servicio del comedor mediante una interfaz gráfica basada en **Swing**.

El proyecto fue desarrollado como parte de la asignatura **Ingeniería de Software II**, aplicando prácticas de modelado y desarrollo incremental utilizando **RUP** y **Extreme Programming (XP)**.

---

## Cronograma de Entregas

| Entrega   | Descripción                                                 | Fecha      |
| :-------- | :---------------------------------------------------------- | :--------- |
| Entrega 1 | RUP: Modelo de Dominio, Casos de Uso, Prototipo de Interfaz | 12/12/2025 |
| Entrega 2 | XP: Historias de Usuario y Planificación de Sprints         | 17/12/2025 |
| Entrega 3 | XP: Incremento 1                                            | 09/02/2026 |
| Entrega 4 | XP: Incremento 2                                            | 26/02/2026 |
| Entrega 5 | XP: Incremento 3, Reflexión y Presentación Final            | 12/03/2026 |

---

## Stack Tecnológico

El sistema fue desarrollado utilizando el siguiente conjunto de tecnologías:

### Lenguaje y Frameworks

**Lenguaje:** Java (OpenJDK 17)

**Interfaz Gráfica:** Swing

**Pruebas Unitarias:** JUnit

**Gestión de Dependencias y Build:** Maven

### Herramientas de Desarrollo

Durante el desarrollo se utilizaron herramientas integradas en **Visual Studio Code** para facilitar la programación, depuración y pruebas del proyecto.

**Extension Pack for Java**
Proporciona soporte completo para desarrollo en Java.

**Debugger for Java**
Permite depurar aplicaciones Java directamente desde el entorno de desarrollo.

**Test Runner for Java**
Facilita la ejecución y visualización de pruebas unitarias basadas en JUnit.

**Project Manager for Java**
Permite administrar proyectos Java y su estructura dentro del editor.

**Maven for Java**
Integra el sistema de construcción Maven dentro del entorno de desarrollo.

### Herramientas de Modelado

**Git Graph**
Visualización gráfica del historial de ramas y commits del repositorio.

**PlantUML**
Generación de diagramas UML a partir de definiciones en texto.

---

## Requisitos del Sistema

Para compilar y ejecutar el proyecto desde el código fuente se requiere tener instalado:

* **Java Development Kit (JDK) 17 o superior**
* **Apache Maven 3.8 o superior**
* Sistema operativo compatible con Java (Windows, Linux o macOS)

Verificación de instalación:

```
java -version
mvn -version
```

---

## Compilación del Proyecto

Clonar el repositorio:

```
git clone https://github.com/znwb7/IS_2-2025.git
cd IS_2-2025
```

Compilar el proyecto utilizando Maven:

```
mvn clean package
```

Este comando compila el proyecto, ejecuta las pruebas y genera el archivo ejecutable `.jar` dentro del directorio:

```
target/
```

---

## Ejecución del Proyecto (Desde Build)

Una vez compilado el proyecto, se puede ejecutar el archivo `.jar` con:

```
java -jar target/*.jar
```

El nombre del archivo `.jar` puede variar dependiendo de la configuración del `pom.xml`.

---

## Ejecución mediante Releases (Compilado)

También es posible descargar directamente el archivo ejecutable `.jar` desde la sección de **Releases** del repositorio.

Allí se encuentran versiones compiladas listas para ejecutar sin necesidad de compilar el proyecto desde src.

Descargar desde:

[Releases](https://github.com/znwb7/IS_2-2025/releases)

Luego ejecutar:

```
java -jar comedor-app-<version>.jar
```
Reemplazar `<version>` por la versión correspondiente a la release descargada.

---

## Estructura del Proyecto

El proyecto sigue la **estructura estándar de Maven**, lo que permite separar claramente el código fuente, los recursos y las pruebas del sistema.

```
.
├── src
│   ├── main
│   │   ├── java
│   │   └── resources
│   └── test
│       └── java
├── target
└── pom.xml
```

Descripción de los directorios principales:

* **src/main/java**: contiene el código fuente principal de la aplicación.
* **src/main/resources**: contiene archivos de recursos utilizados por el sistema.
* **src/test/java**: contiene las pruebas unitarias del proyecto.
* **target**: directorio generado automáticamente por Maven donde se almacenan los archivos compilados y los artefactos generados (por ejemplo, el `.jar` ejecutable).
* **pom.xml**: archivo de configuración de Maven donde se definen dependencias, plugins y parámetros de construcción.

---

## Organización del Proyecto

Dentro de la estructura Maven, el sistema se organiza siguiendo el patrón **Model–View–Controller (MVC)** para separar responsabilidades entre la lógica del sistema, la interfaz gráfica y el manejo de eventos.

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── ucv
│   │           ├── model
│   │           │   └── Entidades y lógica de negocio del sistema
│   │           │
│   │           ├── controller
│   │           │   └── Controladores que gestionan la interacción entre vista y modelo
│   │           │
│   │           └── view
│   │               ├── Vistas principales de la interfaz gráfica
│   │               └── components
│   │                   └── Componentes reutilizables utilizados por distintas vistas
│   │
│   └── resources
│       ├── Bases de datos utilizadas por el sistema en modo lectura
│       └── com
│           └── Imágenes (.png) utilizadas por la interfaz gráfica
│
└── test
    └── java
        └── com
            └── ucv
                └── Pruebas unitarias del sistema (JUnit)
```

---

### Directorio de Construcción

El directorio **target** es generado automáticamente por **Maven** durante el proceso de compilación y empaquetado del proyecto.

```
target
├── classes
├── test-classes
├── Output
└── comedor-app-<version>.jar
```

Este directorio incluye (entre otras):

* **classes**: archivos `.class` generados a partir del código fuente.
* **test-classes**: clases compiladas correspondientes a las pruebas unitarias.
* **Output**: archivos .txt de bases de datos para escritura.
* **archivo .jar**: artefacto ejecutable generado por Maven.

El contenido de este directorio **no debe modificarse manualmente**, ya que se recrea automáticamente cada vez que se ejecuta el proceso de compilación mediante:

```
mvn clean package
```
---

## Licencia

Este proyecto fue desarrollado con fines académicos para la asignatura **Ingeniería de Software II**.
