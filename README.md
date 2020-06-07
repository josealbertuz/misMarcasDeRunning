# misMarcasDeRunning

misMarcasDeRunning es una aplicación que permite al usuario registrar sus marcas obtenidas en carreras pudiendo almacenar sus récords personales en las distintas carreras que hay preinstaladas. Este proyecto ha sido realizado para la asignatura de programación de 1º perteneciente al C.F.G.S. de Desarrollo de Aplicaciones Multiplataforma.

El desarrollo de la app fue avanzando a medida que transcurría el curso, pasando de tener un solo usuario a tener listas de usuarios, una estructura basada en el MVC, envío de notificaciones mediante email y Telegram y añadiendo persistencia al programa con serialización.

# Cosas a tener en cuenta

Para que el programa pueda tener persistencia e iniciarse hay que tener cuenta las siguientes considerenciones: 

1. El archivo appProperties.config debe de estar siempre junto al codigo o al ejecutable
Este archivo permite la ejecución del programa y se encuentra dentro de la carpeta
config.

2. Si la carpeta data existe, dentro del archivo appProperties, hay que poner el ruta
de dicha carpeta en el apartado "files_location". Esta carpeta se puede mover donde
se quiera siempre y cuando se cambie la ubicación dentro del appProperties. 

3. Si la carpeta no existe el propio programa al iniciarse creará los directorios
necesarios para la completa integridad de la app.

4. Al igual ocurre con la carpeta "html_resources", sin esta carpeta no se podrán
enviar las notificaciones al correo. Si esta carpeta se mueve también se ha de cambiar
dentro del archivo appProperties.

5. Para poder enviar notificaciones tanto por email como por Telegram es necesario establecer
distintos parámetros dentro del archivo appProperties.config.
