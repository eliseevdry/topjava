<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/functions" prefix="f" %>
<html lang="ru">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meals</title>
    </head>
    <body>
        <h3><a href="index.html">Home</a></h3>
        <hr>
        <h2>Meals</h2>
        <h3><a href="addMeal.jsp">Add Meal</a></h3>
        <table border="1">
            <tbody>
                <td>Date</td>
                <td>Description</td>
                <td>Calories</td>
                <c:forEach var = "meal" items="${mealsTo}" varStatus="status">
                    <tr style="${meal.excess ? "color: red;" : "color: green;"}">

                        <td>${f:formatLocalDateTime(meal.dateTime, 'yyyy-MM-dd HH:mm')}</td>
                        <td>${meal.description}</td>
                        <td>${meal.calories}</td>
                        <td><h4><a href="meals">Update</a></h4></td>
                        <td><h4><a href="meals">Delete</a></h4></td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
