FROM bellsoft/liberica-openjdk-centos:21
LABEL image.name="library-app"
LABEL image.version="1.0.0-SNAPSHOT"
LABEL maintainer="lonecalvary78.de@gmail.com"

WORKDIR /app

# Copy the built artifact from the build stage
COPY target/library-app-*.jar /app/library-app.jar

# Configure environment variables
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xms512m -Xmx1024m"

# Expose port
EXPOSE 8080

# Set the entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/library-app.jar","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"] 