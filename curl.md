# cURL requests to topjava REST API:

###### `\"` - use for Windows. If you have another OS - change to - `"`

___

### WORK WITH USERS :

* USERS FROM ADMIN :
  `curl http://localhost:8080/topjava/rest/admin/users`
* USER FROM ADMIN BY ID :
  `curl http://localhost:8080/topjava/rest/admin/users/100002`
* USER FROM ADMIN BY EMAIL :
  `curl http://localhost:8080/topjava/rest/admin/users/by-email?email=user@gmail.com`
* CREATE NEW USER :
  `curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/topjava/rest/admin/users -d "{\"id\": null,\"name\": \"NewGuest\",\"email\": \"new2guest@gmail.com\",\"password\": \"guest\",\"caloriesPerDay\":2000}"`
* UPDATE USER FROM ADMIN :
  `curl -i -X PUT -H "Content-Type:application/json" http://localhost:8080/topjava/rest/admin/users/100002 -d "{\"name\": \"UpdateGuest\",\"email\": \"guest@gmail.com\",\"password\": \"guest\",\"caloriesPerDay\":1800}"`
* DELETE USER FROM ADMIN :
  `curl -i -X DELETE http://localhost:8080/topjava/rest/admin/users/100019`

___

### WORK WITH CURRENT PROFILE :

* TO CURRENT PROFILE :
  `curl http://localhost:8080/topjava/rest/profile`
* TO RUSSIAN TEXT EXAMPLE :
  `curl http://localhost:8080/topjava/rest/profile/text`

___

### WORK WITH MEALS IN CURRENT PROFILE :

* TO MEALS :
  `curl http://localhost:8080/topjava/rest/meals`
* TO SPECIFIC MEAL :
  `curl http://localhost:8080/topjava/rest/meals/100005`
* TO FILTERED MEALS :
  `curl -G  http://localhost:8080/topjava/rest/meals/filter -d 'startDate=2020-01-31' -d 'endDate=' -d 'startTime=' -d 'endTime=15:00'`
* CREATE NEW MEAL :
  `curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/topjava/rest/meals -d "{\"id\": null,\"dateTime\": \"2020-01-30T18:02\",\"description\": \"MyNewMeal\",\"calories\": 200}"`
* UPDATE MEAL :
  `curl -i -X PUT -H "Content-Type:application/json" http://localhost:8080/topjava/rest/meals/100007 -d "{\"dateTime\": \"2020-01-30T18:10\",\"description\": \"UpdateMeal\",\"calories\": 250}"`
* DELETE MEAL :
  `curl -i -X DELETE http://localhost:8080/topjava/rest/meals/100024`

___
