# Meteo Measurements in Poland by Matiej

A Spring Boot service to obtain data from air and synoptic weather stations in Poland. 
The application utilizes an external RESTful API providing data to government institutions.
The synoptic measurements are received from https://danepubliczne.imgw.pl/ in JSON format and consist of basic synoptic data. 
The measurements are received from http://powietrze.gios.gov.pl/pjp/content/api in the same way. 
The application provides the readings from all stations online. 
The data can be filtered by city name, coldest, and warmest place in Poland.
Thank to conjunction of data of the two APIs, the user has access to synoptic measurements and 
air condition measurements simultaneously. Additionally, all the measurement data are being saved to an external MySQL database. 
This allows for review of archived measurements and filtering by date, air condition and temperature.
Used technologies:

-	JDK 8 SE,
-	Spring Boot,
-	MySQL DB,
-	Maven,
-	Spring JPA,
-   QuerryDSL
-   Apache POIgit
-	JUnit,
-	Mockito.
-	Swagger

Add measurements to database : add-measurements-controller

| HTTP Method | URI |Description |
| --- | --- | --- |
| GET | `/add/station` | Add all measurements from API. Warning! It takes a lot of time |
| GET | `/add//station/all?id={station_id}` | Add measurements from selected station |

Get measurements from database : get-measurements-controller

| HTTP Method | URI |Description |
| --- | --- | --- |
| GET | `/get/stations/all` | Get all measurements |
| GET | `/get/measurements/coldest` | Get coldest measurements by given date: YYYY-MM-DD|
| GET | `/get/measurements/hottest` | Get hottest measurements by given date: YYYY-MM-DD|
| GET | `/get/measurements/coldestTop` | Get top coldest measurements|
| GET | `/get/measurements/hottestTop` | Get top hottest measurements|
| GET | `/get/measurements/air` | Get air measurements by air quality|
| GET | `/get/measurements/date` | Get air measurements from given date|
| GET | `/get/measurements/synoptic` | Get synoptic measurements from given date |

Get measurements directly from API : measuring-online-controller

| HTTP Method | URI |Description |
| --- | --- | --- |
| GET | `/online/coldeststation` |  Get coldest station|
| GET | `/online/stations/all` |  Get all stations|
| GET | `/online/stations/select/name?id={city}` | Get all measurements stations for given city name|
| GET | `/online/stations/coldest` |  Get hottest station|

## Tested on

- Java SE Development Kit 8
- Maven 3.0+
- MySQL 5.0+ _(should work with any other relational database, but you need to change dependency)_


## Getting Started

Import the Maven project straight to your Java IDE,

## TO DO

- [ ] Add frontend
