
SET FOREIGN_KEY_CHECKS=0;

INSERT INTO TypeOfContract VALUES ('contratto');

INSERT INTO Qualification VALUES ('qualifica');

INSERT INTO JobCategory VALUES ('Hired on a temporary basis');

INSERT INTO JobPosition VALUES ('ingegnere');

INSERT INTO EmployeeUserDetails VALUES ('nome dipendente 1', 'cognome dipendente 1', '785689328', '00743110157', 1, 0, null, null, '1GLLEI00D08D619D');

INSERT INTO Offer VALUES (1, 'Nome 1', 'Descrizione', 'company', '00743110157', 5000, 'photo', 'qualche ora', 'ingegnere', 'qualifica', 'contratto', STR_TO_DATE('11-11-2021', '%d-%m-%Y'), 1, null, 1, 'Hired on a temporary basis', '1GLLEI00D08D619D');

INSERT INTO JobSeekerUserDetails VALUES ('Elio', 'Elio', 'MGLLEI00D08D612D', STR_TO_DATE('08-04-2000', '%d-%m-%Y'), '6478798322', null, null, null, 'Sora', '03039', 'Hired on a temporary basis', null, null, null, null, null);

INSERT INTO Candidature VALUES (1, 'MGLLEI00D08D612D', STR_TO_DATE('14-01-2021', '%d-%m-%Y'));

INSERT INTO Auth VALUES ('email1@email.com', 0, 0, '1GLLEI00D08D619D', null, null); /* employee */ 

INSERT INTO Auth VALUES ('gahig78372@1uscare.com', '0x2432612431322445594130765332E6A543738702F476E79', 0, null, 'MGLLEI00D08D612D',  null); /*job seeker */
