<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Add Meals</h2>
<form method="post" action="meals">
    <input type="hidden" name="id" value="${id}">
    <label><input type="datetime-local" name="date" value="${dateTime}"></label>DateTime<br>
    <label><input type="text" name="description" value="${description}"></label>Description<br>
    <label><input type="number" name="calories" value="${calories}"></label>Calories<br>
    <input type="submit" value="Save" name="Save"><br>
    <input type="reset" value="Cancel" name="Cancel"><br>
</form>

</body>
</html>
