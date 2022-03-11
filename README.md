# Description

work-planning application that serve  a work planning service.
Business requirements:

    A worker has shifts
    A shift is 8 hours long
    A worker never has two shifts on the same day
    It is a 24 hour timetable 0-8, 8-16, 16-24


### Prerequisites

Jdk 11 or higher and maven installed.

### Technical stack

JDK 11, Spring Boot, Spring WebFlux, maven, MongoDB (Atlas)


### Start the application

Once you clone the source code using git clone please run following commands :

```
cd work-planning
mvn spring-boot:run
```

### Get all planning from DB

A GET request on `localhost:8080/planning` will retrieve all entries from db. To test, use postman or curl as follow:

```
curl -X GET -i http://localhost:8080/planning
```

### Persist a new planning into DB

A POST request on `localhost:8080/planning` will insert into db a new worker and its shift according to business requirements.
If validation does not pass, an explicit json error will display

To test,  Use postman or curl as follow:

```
curl -X POST -H 'Content-Type: application/json' -H 'Accept-Language: application/json' -i http://localhost:8080/planning --data '{
	"worker" : {
        "firstName": "Foo",
        "lastName": "Bar"
    },
    "shift": {
        "start": "2021-09-12T08:00",
        "end":   "2021-09-12T16:00"
    }
}'
```

### Retrieve one entry

A GET request on `localhost:8080/planning/{ID}` will retrieve one entry from db according to its ID.

Example with ID 6229de69b47bd257e4992174 :

```
curl -X GET -i http://localhost:8080/planning/6229de69b47bd257e4992174
```

### Delete one entry

A DELETE request on `localhost:8080/planning/{ID}` will delete from db

You must provide an id as a path param

### Update existing entry

A PUT request on `localhost:8080/planning` will update and existing entry in DB

Please be aware that in that case the id field gets mandatory but the same validation is applied as a POST request

Example :

```
curl -X PUT -H 'Content-Type: application/json' -H 'Accept-Language: application/json' -i http://localhost:8080/planning --data '{
    "id": "6229de69b47bd257e4992174",
    "worker": {
        "firstName": "Karl",
        "lastName": "Zero"
    },
    "shift": {
        "start": "2012-09-12T00:00:00",
        "end": "2012-09-12T08:00:00"
    }
}'
```

### Unit tests

Feel free to run from project root folder

```
mvn test
```
