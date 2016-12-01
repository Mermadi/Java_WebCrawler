## Requirements

1. Java 8
2. MySQL
3. Add JARs listed below to your build path
- jsoup-1.10.1.jar ( HTML Parser )
- rs2xml.jar ( ResultSets to JTable )
- mysql-connector-java-5.1.40-bin.jar ( MySQL Driver )

## Getting Running

1. Download and unzip this repo or git clone 

2. Start MySQL server 

3. Login to MySQL command prompt, create a database named crawledInfo, then create a table named scraped

        $ mysql -u yourUsername -p
        $ CREATE DATABASE crawledInfo;
        $ USE crawledInfo;
        $ CREATE TABLE scraped ( url VARCHAR(300), links VARCHAR (300), layer VARCHAR(5) );

3. Set up and Run:

        $ make build     # creates db and migrates allauth data
        $ python manage.py migrate #migrates polls app data
        $ make run         # starts the development server

## Credits

Credits go here 