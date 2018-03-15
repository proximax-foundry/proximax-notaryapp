# Proxima-X Notary App (for showcase only)

Decentralized Notary App using Proxima-X SDK.

## Technology Stack

*   Java 1.8
*   JFoenix
*   Proxima-X daemon

## Development
Clone the repository

```shell
//	For collaborators only.
git clone https://github.com/alvin-reyes/proximax-notaryapp.git
```

Install [Jfoenix](https://github.com/jfoenixadmin/JFoenix "Jfoenix") SceneBuilder

Download the Proxima-X Java SDK JAR and import it on your local maven repository

```shell
mvn install:install-file -DgroupId=io.nem.xpx -DartifactId=xpx-java-sdk -Dversion=1.0.0 -Dpackaging=jar -Dfile=lib/xpx-java-sdk-1.0.0-jar-with-dependencies
```

Build Project

```shell
mvn clean install
```

## Running the Application
The application relies on the proximax light weight node. When the application is run, the daemon process will also run on the background.

## Author
Alvin P. Reyes
