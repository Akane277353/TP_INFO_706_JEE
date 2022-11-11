<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Borne 2</title>
</head>
<body>
<h1>Borne 2</h1>
	<form action="Borne2" method="post">
	 	numero : <input type="number" name ="numero">
        <input type="submit" value="voir info ticket">
    </form>
    <form action="allerPayer" method="post">
        <input type="submit" value="Payer">
    </form>
    <form action="allerBorne3" method="post">
        <input type="submit" value="aller borne 3">
    </form>
</body>
</html>