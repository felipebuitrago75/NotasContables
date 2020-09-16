Aplicativo NOTAS CONTABLES - BBVA
====================

- - - 

## Accesos Ambiente DESARROLLO

**Web APP:** [http://82.255.39.52:9081/NotasContables](http://82.255.39.52:9081/NotasContables)

**WAS:** [https://82.255.39.52:9047/ibm/console](https://82.255.39.52:9047/ibm/console)

- - - 

## Configuración Entorno de Desarrollo

### Herramientas

+ Java IDE
	* [Eclipse IDE](https://www.eclipse.org/downloads/)
	* [IntelliJ IDEA](https://www.jetbrains.com/es-es/idea/)
	* [Visual Studio Code](https://code.visualstudio.com/)

> Se recomienda algunos de los anteriores IDEs - Libre elección 


+ [GIT](https://git-scm.com/downloads)


+ [JAVA JDK v1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)

 
+ [Apache Maven 3.6.3](https://maven.apache.org/download.cgi)



### Configurar Variables de Entorno

+ Crear `JAVA_HOME` con el path del JDK.



+ Crear `MAVEN_HOME` con el path de la instalación de Maven.



+ Adicionar al `PATH` las siguientes rutas:
	* `%JAVA_HOME%\bin;`
	* `%MAVEN_HOME%\bin;`



### Modificar archivo `settings.xml`

Se debe **cambiar** o **modificar** el archivo `settings.xml` en la instalación de MAVEN.

_PATH:_ `..\apache-maven-3.6.3\conf`

> La configuración anterior para que Maven descargue las librerias del repositorio de _ARTIFACTORY_


- - - 

## Repositorio Artifactory

[https://globaldevtools.bbva.com/artifactory/list/repository-notas-contables/](https://globaldevtools.bbva.com/artifactory/list/repository-notas-contables/)


- - - 

## GIT

### Configuración de GIT

Se debe configurar el usuario y el correo electronico con dominio banco.
	
	$ git config --global user.name "Nombre Apellido"
	$ git config --global user.email correo-electronico@bbva.com

### Comandos Básicos

~~~
$git clone ssh://git@globaldevtools.bbva.com:7999/bbvacnc/notas_contables.git
~~~
~~~
$git pull
~~~
~~~ 
$git status
~~~
~~~
$git add <.> | <cambio específico>
~~~
~~~
$git push
~~~

### Estándar comentarios para los `COMMIT`

_**Estructura**_

	[TIPO] - Descripción

### Tipos

+ `[ADD]` --> Se crea un nuevo componente (Clase, Librería, Archivo, etc...).
.
+ `[FEAT]` --> Se crea una nueva funcionalidad en un componente existente.  

+ `[FIX]` --> Se ajusta un componente o Se soluciona un error.

+ `[DELETE]` --> Se elimina un componente existente.

+ `[DOCS]` --> Se agrega/actualiza/elimina documentación.


_**Ejemplos:**_

	git commit -m "[ADD] - Nueva clase para validar acceso"
	git commit -m "[FEAT] - Nuevo método en la clase Personas que ejecuta un batch"
	git commit -m "[FIX] - Ajuste para la consulta de registros"
	git commit -m "[DELETE] - Se eliminan archivos temporal"
	git commit -m "[DOCS] - Se actualiza la documentación de la clase Usuario"

* * *
* * *
`@autor <David Rodriguez>` <davidandres.rodriguez@bbva.com>
* * *

