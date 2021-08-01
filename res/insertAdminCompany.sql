
SET FOREIGN_KEY_CHECKS=0;

INSERT INTO Company VALUES ('VATnumber', 'Nome azienda 1', 'MGLLEI00D08D614D', null);

INSERT INTO EmployeeUserDetails VALUES ('Elio', 'Magliari', '785689328', 'VATnumber', 1, 1, null, null, 'MGLLEI00D08D619E');

INSERT INTO EmployeeUserDetails VALUES ('nome dipendente 2', 'cognome dipendente 2', '785689328', 'VATnumber', 1, 1, null, null, '2GLLEI00D08D619D');

INSERT INTO EmployeeUserDetails VALUES ('nome dipendente 3', 'cognome dipendente 3', '785689328', 'VATnumber', 1, 1, null, null, '3GLLEI00D08D619D');

INSERT INTO Auth VALUES ('kanocat772@hyprhost.com', '0', 0, 'MGLLEI00D08D619E', null,  null);

INSERT INTO Auth VALUES ('email2@email.com', 0, 0, '2GLLEI00D08D619D', null, null); /* employee */ 

INSERT INTO Auth VALUES ('email3@email.com', 0, 0, '3GLLEI00D08D619D', null, null); /* employee */
