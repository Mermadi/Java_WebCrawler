## Requirements

1. Java 8
2. MySQL
3. Add JARs listed below to your build path
	- jsoup-1.10.1.jar ( HTML Parser )
	- rs2xml.jar ( ResultSets to JTable )
	- mysql-connector-java-5.1.40-bin.jar ( MySQL Driver )

## Get Running

1. Download and unzip this repo or git clone 

2. Start MySQL server 

3. Login to MySQL command prompt, create a database named crawledInfo, then create a table named scraped

        $ mysql -u yourUsername -p
        $ CREATE DATABASE crawledInfo;
        $ USE crawledInfo;
        $ CREATE TABLE scraped (url VARCHAR(500), links VARCHAR (500), imports VARCHAR (500), media VARCHAR (500), layer VARCHAR(5));

4. Compile and run:

        $ cd Java_WebCrawler/src/
        $ Make DB cred changes to Utils.java
        $ javac *.java
        $ java MainMenu
