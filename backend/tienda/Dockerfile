#Fase 1: Construir el proyecto
FROM maven:3.9.14-eclipse-temurin-17 AS build
#Descarga la imagen de Maven con Java 17.
WORKDIR /app
#Crea una carpeta dentro del contenedor se posiciona ahí                                       
COPY pom.xml .                                  
#Copia el archivo pom.xml al contenedor
COPY src ./src
#Copia la carpeta src al contenedor                                  
RUN mvn clean package -DskipTests
#Compila el proyecto y genera el .jar en la carpeta target. Con -Dskiptests evita ejeecutar los test durante la construcción.               

#Fase 2: Crear la imagen final
FROM eclipse-temurin:17-jre 
#Empieza una nueva fase con una imagen más ligera *solo java*, no necesitamos maven al estar ya compilado el .jar                    
WORKDIR /app 
#Crea una carpeta dentro del contenedor se posiciona ahí                                                                  
COPY --from=build /app/target/*.jar app.jar 
#Coge el .jar y lo copia en esta imagen con el nombre app.jar    
ENTRYPOINT ["java", "-jar", "app.jar"]          
#Es equivalente a escribir java  -jar app.jar en la terminal, es el comando que se ejecutará al iniciar el contenedor. Ejecuta la aplicación Java.