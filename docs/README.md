EduCore — Sistema de Administración de Centro Educativo



Sistema de gestión integral para centros educativos: estudiantes, personal
docente/administrativo, edificios, aulas y oferta académica.



Proyecto 1 · Programación III (ISI-BSI-09) · Universidad Americana
Tag de entrega: `v1.0-p1`



Integrante

* Tania Quesada Rojas

Requisitos previos

* Java 21 (JDK)
* Maven 3.8+
* Git



Verifica tus versiones instaladas:

```bash
java -version
mvn -version
```

Clonar el proyecto

```bash
git clone https://github.com/taniaq158-rgb/educore.git
cd educore
git checkout v1.0-p1
```

Compilación

Desde la raíz del proyecto (donde está `pom.xml`):

```bash
mvn clean compile
```

Esto descarga las dependencias necesarias y compila todas las clases en
`target/classes`.

Ejecución

Opción A — Desde la terminal (Maven)

```bash
mvn compile
mvn exec:java -Dexec.mainClass=edu.uam.educore.Main
```

Opción B — Desde NetBeans

1. Abrir el proyecto (`File, luego Open Project`, seleccionar la carpeta `educore`).
2. Clic derecho sobre el proyecto y Run (o `F6`).
3. La aplicación se ejecuta en la consola integrada de NetBeans.

Ejecutar las pruebas

```bash
mvn test
```

Debe mostrar `BUILD SUCCESS` con todos los tests en verde, incluyendo los
provistos por el docente (`EstudianteRegularTest`, `EstudianteBecadoTest`) y
los del equipo (`EmpleadoTest`, `EdificioTest`, `SeccionControllerTest`).

Verificar formato de código

```bash
mvn fmt:check
```

Si reporta archivos sin formato correcto, corrígelos automáticamente con:

```bash
mvn fmt:format
```

Uso del sistema

Al iniciar, el programa muestra el menú principal:

```
--- MENÚ PRINCIPAL ---
1. Gestión de Estudiantes
2. Gestión de Empleados
3. Gestión Académica (Edificios, Aulas, Secciones)
0. Salir
```

Ingresa el número de la opción deseada y presiona Enter. Cada módulo se
maneja con su propio sub-menú; la opción `0` siempre regresa al nivel
anterior.



1\. Gestión de Estudiantes

Permite registrar estudiantes (Regulares o Becados), listar, buscar por ID,
actualizar y eliminar (con confirmación previa).

2\. Gestión de Empleados

Permite registrar empleados indicando nombre, apellidos, email, salario,
fecha de ingreso (formato `AAAA-MM-DD`) y tipo (Docente, Administrativo,
Guarda, Misceláneo, Mantenimiento). Incluye listar, buscar, actualizar y
eliminar.

3\. Gestión Académica

Submenú con dos áreas:

```
--- GESTIÓN ACADÉMICA ---
1. Edificios y Aulas
2. Secciones
0. Volver al menú principal
```

Edificios y Aulas:

* Registrar un edificio (código y nombre).
* Listar edificios, ver sus aulas.
* Agregar un aula a un edificio existente (número, capacidad, tipo:
Regular/Laboratorio/Auditorio).
* Eliminar edificios o aulas (no se puede eliminar un edificio que aún
tenga aulas registradas).

Secciones:

* Registrar una sección indicando código, nombre, el ID de un aula y el
ID de un empleado ya existentes (el empleado debe ser de tipo
Docente).
* Inscribir o remover estudiantes por su ID.
* Listar secciones y ver el detalle de inscritos.
* Eliminar una sección (no se puede si todavía tiene estudiantes
inscritos).



> \\\*\\\*Importante:\\\*\\\* para registrar una sección, primero deben existir al menos
> un edificio con un aula, y un empleado de tipo Docente. Regístralos desde
> las opciones 2 y 3.1 del menú antes de entrar a Secciones.

Notas sobre la persistencia (Proyecto 1)

Este proyecto usa repositorios en memoria (listas `ArrayList`). Todos
los datos se pierden al cerrar el programa — esto es esperado en P1 y se
reemplazará por persistencia en MySQL en el Proyecto 2, sin modificar los
Controllers.

Estructura del proyecto

```
src/main/java/edu/uam/educore/
├── controller/      Lógica de negocio y validaciones
├── dao/              Repositorios (persistencia en memoria, Proyecto 1)
├── db/               Conexión a base de datos (se activa en Proyecto 2)
├── enums/            TipoEmpleado, TipoAula
├── model/
│   ├── academico/    Seccion
│   ├── infraestructura/  Edificio, Aula
│   └── personas/     Persona, Estudiante, Empleado y subtipos
├── util/             Validador
└── view/             Vistas de consola (Menú, CRUD por módulo)
```

