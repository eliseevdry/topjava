# cURL requests to topjava REST API:

___

### GET REQUEST :

* TO ADMIN UI:
  `curl http://localhost:8080/topjava/rest/admin/users`
* TO ADMIN UI BY ID:
  `curl http://localhost:8080/topjava/rest/admin/users/100002`
* TO ADMIN UI BY EMAIL:
  `curl http://localhost:8080/topjava/rest/admin/users/by-email?email=user@gmail.com`
* TO CURRENT PROFILE:
  `curl http://localhost:8080/topjava/rest/profile`
* TO RUSSIAN TEXT EXAMPLE:
  `curl http://localhost:8080/topjava/rest/profile/text`
* TO MEALS :
  `curl http://localhost:8080/topjava/rest/meals`
* TO SPECIFIC MEAL :
  `curl http://localhost:8080/topjava/rest/meals/100005`
* TO FILTERED MEALS :
  `curl -G  http://localhost:8080/topjava/rest/meals/filter -d 'startDate=2020-01-31' -d 'endDate=' -d 'startTime=' -d 'endTime=15:00'`

___

### POST REQUEST :

* CREATE NEW USER :
  `curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/topjava/rest/admin/users -d "{\"id\": null,\"name\": \"NewGuest\",\"email\": \"new2guest@gmail.com\",\"password\": \"guest\",\"caloriesPerDay\":2000}"`

* CREATE NEW MEAL :
  `curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/topjava/rest/meals -d "{\"id\": null,\"dateTime\": \"2020-01-30T18:02\",\"description\": \"MyNewMeal\",\"calories\": 200}"`

___

### PUT REQUEST :

* UPDATE USER FROM ADMIN UI :
  `curl -i -X PUT -H "Content-Type:application/json" http://localhost:8080/topjava/rest/admin/users/100002 -d "{\"name\": \"UpdateGuest\",\"email\": \"guest@gmail.com\",\"password\": \"guest\",\"caloriesPerDay\":1800}"`
* UPDATE MEAL :
  `curl -i -X PUT -H "Content-Type:application/json" http://localhost:8080/topjava/rest/meals/100007 -d "{\"dateTime\": \"2020-01-30T18:10\",\"description\": \"UpdateMeal\",\"calories\": 250}"`

___

### DELETE REQUEST :

* DELETE USER FROM ADMIN UI :
  `curl -i -X DELETE http://localhost:8080/topjava/rest/admin/users/100019`

* DELETE MEAL :
  `curl -i -X DELETE http://localhost:8080/topjava/rest/meals/100024`

___