/RegisterChoiceServlet<%@page import="logic.dao.StatoOccupazionaleDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Registrazione</title>
	</head>
	
	<body lang="it">
		
		<form>
			<p>
				<label for="email"> Email</label>
				<input type="text" name="register" id="email" placeholder="Inserisci la tua email">
			</p>
			<p>
				<label for="password"> Password</label>
				<input type="password" name="register" id="password" placeholder="Inserisci la tua password">
			</p>
			<p>
				<label for="confirm-password"> Conferma Password</label>
				<input type="password" name="register" id="confirm-password" placeholder="Conferma la tua password">
			</p>
			
			<p><b>Dati Personali</b></p>
			
			<p>
				<label for="name"> Nome </label>
				<input type="text" name="register" id="name" placeholder="Inserisci il tuo nome">
			</p>
			<p>
				<label for="surname"> Cognome </label>
				<input type="text" name="register" id="surname" placeholder="Inserisci il tuo cognome">
			</p>
			<p>
				<label for="fiscal-code"> Codice Fiscale </label>
				<input type="text" name="register" id="fiscal-code" placeholder="Inserisci il tuo codice fiscale">
			</p>
			<p><b>Dati di contatto</b></p>
			
			<p>
				<label for="town"> Comune </label>
				<input type="text" name="register" id="town" placeholder="Inserisci il tuo comune">
			</p>
			<p>
				<label for="address"> Indirizzo </label>
				<input type="text" name="register" id="address" placeholder="Inserisci il tuo indirizzo">
			</p>
			<p>
				<label for="phone-number"> Cellulare </label>
				<input type="text" name="register" id="phone-number" placeholder="Inserisci il tuo numero di telefono">
			</p>
			<p>			
				<input type="text" name="register" id="province" placeholder="Provincia">
				<input type="text" name="register" id="cap" placeholder="CAP">
			</p>
			
			<p><b>Stato Occupazionale</b></p>
			
			<select name="employment-status" size="1">
				<option>Italia</option>
				<option>Inghilterra</option>
				<option>Francia</option>
			</select>
						
		</form>
		
	</body>
</html>