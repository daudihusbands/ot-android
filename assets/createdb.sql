DROP TABLE IF EXISTS CONFIGURATION;
DROP TABLE IF EXISTS LOCATION;
DROP TABLE IF EXISTS CLAIM;
DROP TABLE IF EXISTS PERSON;
DROP TABLE IF EXISTS ATTACHMENT;
DROP TABLE IF EXISTS METADATA;
DROP TABLE IF EXISTS VERTEX;

CREATE TABLE CONFIGURATION
(CONFIGURATION_ID VARCHAR(255) PRIMARY KEY,
NAME VARCHAR(255) NOT NULL,
VALUE VARCHAR(255));

CREATE UNIQUE INDEX CONFIGURATION_NAME_IDX ON CONFIGURATION(NAME);

CREATE TABLE PERSON
(PERSON_ID VARCHAR(255) PRIMARY KEY,
UPLOADED BOOLEAN NOT NULL DEFAULT FALSE,
FIRST_NAME VARCHAR(255) NOT NULL,
LAST_NAME VARCHAR(255) NOT NULL,
DATE_OF_BIRTH DATE,
PLACE_OF_BIRTH VARCHAR(255),
EMAIL_ADDRESS VARCHAR(255),
POSTAL_ADDRESS VARCHAR(255),
MOBILE_PHONE_NUMBER VARCHAR(255),
CONTACT_PHONE_NUMBER VARCHAR(255));

CREATE TABLE LOCATION
(LOCATION_ID VARCHAR(255) PRIMARY KEY, 
NAME VARCHAR(255) NOT NULL, 
LAT DECIMAL(15,10) NOT NULL, 
LON DECIMAL(15,10) NOT NULL);

CREATE UNIQUE INDEX LOCATION_NAME_IDX ON LOCATION(NAME);

CREATE TABLE CLAIM
(CLAIM_ID VARCHAR(255) PRIMARY KEY, 
UPLOADED BOOLEAN NOT NULL DEFAULT FALSE,
NAME VARCHAR(255) NOT NULL, 
PERSON_ID VARCHAR(255) NOT NULL, 
CHALLENGED_CLAIM_ID VARCHAR(255),
FOREIGN KEY (CHALLENGED_CLAIM_ID)
REFERENCES CLAIM(CLAIM_ID),
FOREIGN KEY (PERSON_ID)
REFERENCES PERSON(PERSON_ID));

CREATE UNIQUE INDEX CLAIM_NAME_IDX ON CLAIM(NAME);

CREATE TABLE VERTEX
(VERTEX_ID VARCHAR(255) PRIMARY KEY, 
UPLOADED BOOLEAN NOT NULL DEFAULT FALSE,
CLAIM_ID VARCHAR(255) NOT NULL,
SEQUENCE_NUMBER INT NOT NULL,
GPS_LAT DECIMAL(15,10),
GPS_LON DECIMAL(15,10),
MAP_LAT DECIMAL(15,10) NOT NULL,
MAP_LON DECIMAL(15,10) NOT NULL,
FOREIGN KEY (CLAIM_ID)
REFERENCES CLAIM(CLAIM_ID));

CREATE UNIQUE INDEX CLAIM_VERTEX_IDX ON VERTEX(CLAIM_ID,SEQUENCE_NUMBER);

CREATE TABLE METADATA
(METADATA_ID VARCHAR(255) PRIMARY KEY,
UPLOADED BOOLEAN NOT NULL DEFAULT FALSE,
CLAIM_ID VARCHAR(255) NOT NULL, 
NAME VARCHAR(255) NOT NULL, 
VALUE VARCHAR(255), 
FOREIGN KEY (CLAIM_ID)
REFERENCES CLAIM(CLAIM_ID));

CREATE UNIQUE INDEX CLAIM_METADATA_IDX ON METADATA(CLAIM_ID,NAME);

CREATE TABLE ATTACHMENT
(ATTACHMENT_ID VARCHAR(255) PRIMARY KEY, 
UPLOADED BOOLEAN NOT NULL DEFAULT FALSE,
CLAIM_ID VARCHAR(255) NOT NULL, 
DESCRIPTION VARCHAR(255) NOT NULL, 
FILE_NAME VARCHAR(255) NOT NULL, 
FILE_TYPE VARCHAR(255) NOT NULL, 
MIME_TYPE VARCHAR(255) NOT NULL, 
MD5SUM VARCHAR(255), 
PATH VARCHAR(255) NOT NULL,
FOREIGN KEY (CLAIM_ID)
REFERENCES CLAIM(CLAIM_ID));
