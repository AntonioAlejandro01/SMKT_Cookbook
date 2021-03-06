# SMKT_Cookbook

Service to manage recipes in SmartKitchen App

![JAVA](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Mongo](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) 

[![Build Dev](https://github.com/AntonioAlejandro01/SMKT_Cookbook/actions/workflows/buildDevVersion.yml/badge.svg?branch=develop)](https://github.com/AntonioAlejandro01/SMKT_Cookbook/actions/workflows/buildDevVersion.yml) [![Build Snapshot](https://github.com/AntonioAlejandro01/SMKT_Cookbook/actions/workflows/BuildSnapshot.yml/badge.svg)](https://github.com/AntonioAlejandro01/SMKT_Cookbook/actions/workflows/BuildSnapshot.yml) [![Build Stable Version](https://github.com/AntonioAlejandro01/SMKT_Cookbook/actions/workflows/BuildRelease.yml/badge.svg)](https://github.com/AntonioAlejandro01/SMKT_Cookbook/actions/workflows/BuildRelease.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AntonioAlejandro01_SMKT_Cookbook&metric=alert_status)](https://sonarcloud.io/dashboard?id=AntonioAlejandro01_SMKT_Cookbook) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AntonioAlejandro01_SMKT_Cookbook&metric=coverage)](https://sonarcloud.io/dashboard?id=AntonioAlejandro01_SMKT_Cookbook)

## Use With Docker

Use this Service with Docker as Docker container. The Repo have 3 types of images. 

### Types

- **Stable**: These are the images that in her tag have a specific version ex.: ```antonioalejandro01/smkt-cookbook:vX.X.X```. The last tag version have tag latest. 
```bash
    docker pull antonioalejandro01/smkt-cookbook:v1.0.0
    # The last stable version
    docker pull antonioalejandro01/smkt-cookbook:latest
 ```

- **Snapshot**: This is the image that in her tag have snapshot word ex.: ```antonioalejandro01/smkt-cookbook:snapshot```
```bash 
    docker pull antonioalejandro01/smkt-cookbook:snapshot
```

- **Dev**: This image is only for developers and in her tag have dev word ```antonioalejandro01/smkt-cookbook:dev```
```bash
    docker pull antonioalejandro01/smkt-cookbook:dev
 ```

### Environment variables for Docker image

<table align="center" width="100%" style="margin:1em;">
<thead>
    <tr>
        <th>Name</th>
        <th>Default Value</th>
        <th>Description</th>
    </tr>
</thead>
<tbody>
    <tr>
        <td>PORT</td>
        <td>4080</td>
        <td>Micro service port</td>
    </tr>
    <tr>
        <td>EUREKA_URL</td>
        <td>http://smkt-eureka:8761/eureka</td>
        <td>The url where the smkt-eureka be</td>
    </tr>
    <tr>
        <td>LEVEL</td>
        <td>INFO</td>
        <td>Log level for all log relational for this repo. <i>Recommend only change for development</i></td>
    </tr>
    <tr>
        <td>DB_NAME</td>
        <td>smkt</td>
        <td>Name for mongo database</td>
    </tr>
    <tr>
        <td>DB_COLLECTION</td>
        <td>recipes</td>
        <td>Name for Collection in mongo database</td>
    </tr>
    <tr>
        <td>DB_CONNECTION</td>
        <td>mongodb://root:secret@localhost:27017/</td>
        <td>String connection to mongo database</td>
    </tr>
    <tr>
        <td>id_files_instance</td>
        <td>smkt-files</td>
        <td>Id that service <a href="http://github.com/antonioAlejandro01/SMKT_Files">smkt-files</a> have it in <a href="http://github.com/antonioAlejandro01/SMKT_Eureka">smkt-eureka</a></td>
    </tr>
    <tr>
        <td>id_oauth_instance</td>
        <td>smkt-oauth</td>
        <td>Id that service <a>smkt-oauth</a> have it in <a href="http://github.com/antonioAlejandro01/SMKT_Eureka">smkt-eureka</a></td>
    </tr>
    
</tbody>
</table>


#### Docker command

```bash
    docker run -d -p4080:4080 -ePORT=4080 -eEUREKA_URL=http://127.0.0.1:8761/eureka -eDB_NAME=smkt -eDB_COLLECTION=recipes -eDB_CONNECTION=mongodb://root:secret@127.0.0.1:27017/ -t antonioalejandro01/smkt-cookbook:latest
 ```

## Use in Docker Compose

```yaml
    cookbook:
        image: antonioalejandro01/smkt-cookbook:latest
        container_name: smkt-cookbook
        environment:
            PORT: 4080
            EUREKA_URL: http://127.0.0.1:8761/eureka
            DB_NAME: smkt
            DB_COLLECTION: recipes
            DB_CONNECTION: mongodb://root@secret@mongo:27017/
        expose:
            - "4080"
        ports: 
            - "4080:4080"
    mongo: # Mongo database for microservice
        image: mongo
        container_name: smkt-mongo
        restart: always
        environment:
            MONGO_INITDB_ROOT_USERNAME: root
            MONGO_INITDB_ROOT_PASSWORD: secret
```


