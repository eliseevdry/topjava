<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                    <c:if test="${meal.excess == false}">
                        <tr style="color: green;">
                    </c:if>
                    <c:if test="${meal.excess == true}">
                        <tr style="color: red;">
                    </c:if>

                        <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))}</td>
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
