FROM maven:3-openjdk-11 as build

WORKDIR /opt/build

COPY . .

RUN mvn clean compile install

RUN mv ./target/smkt-cookbook.jar /app.jar

FROM openjdk:11

WORKDIR /opt/server

COPY --from=build /app.jar  ./app.jar

ENV PORT=4080
ENV EUREKA_URL=http://smkt-eureka:8761/eureka
ENV LEVEL=INFO
ENV DB_NAME=smkt
ENV DB_COLLECTION=recipes
ENV DB_CONNECTION=mongodb://root:secret@localhost:27017/
ENV ID_FILES_INSTANCE=smkt-files
ENV ID_OAUTH_INSTANCE=smkt-oauth
ENV OAUTH_BASIC_AUTH=c21hcnRraXRjaGVuYXBwOnNlY3JldA==

EXPOSE ${PORT}

CMD java -jar app.jar --server.port="${PORT}" --eureka.client.service-url.defaultZone="${EUREKA_URL}" --logging.level.'[com.antonioalejandro.smkt.cookbook]'="${LEVEL}" --mongodb.connection="${DB_CONNECTION}" --mongodb.database.name="${DB_NAME}" --mongo.database.collection="${DB_COLLECTION}" --id_files_instance="${ID_FILES_INSTANCE}"  --id_oauth_instance="${ID_OAUTH_INSTANCE}" --oauthBasicAuth="${OAUTH_BASIC_AUTH}"

