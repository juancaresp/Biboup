# Usamos la imagen de OpenJDK como base para nuestra imagen
FROM openjdk:17-jdk-slim

# Agregamos metadatos a la imagen
LABEL maintainer="Carlos"

# Copiamos el archivo pom.xml en la imagen
COPY pom.xml /app/

# Establecemos el directorio de trabajo
WORKDIR /app


# Copiamos el archivo .jar generado por Maven en la imagen
COPY target/boup-0.0.1-SNAPSHOT.jar /app/boup-0.0.1-SNAPSHOT.jar

# Exponemos el puerto 8080 para la API de Spring Boot
EXPOSE 8080

# Iniciamos la aplicación al ejecutar el contenedor Docker
CMD ["java", "-jar", "boup-0.0.1-SNAPSHOT.jar"]
