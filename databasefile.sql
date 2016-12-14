-- Gemaakt door Martin Donovic (0885879) INF3C
-- In deze code worden alle tabellen aangemaakt met bijbehorende constraints
-- Constraints worden gedaan met reguliere expressies of vergelijken van waardes
-- Aangeven welke waardes null mogen zijn, welke datatypes en primary en foreign keys
-- Er is geen dekking voor overlapping
-- Je kan niet op dezelfde tijd inrosteren

CREATE TABLE student(
	studentnummer 		VARCHAR(7)    NOT NULL,
	PRIMARY KEY(studentnummer),
	CHECK (studentnummer~'^[a-zA-Z0-9]*$'),
	CHECK (studentnummer != '')); 						--er moet iets worden ingevoerd

CREATE TABLE docent(
	medewerkerscode 	VARCHAR(6)    NOT NULL,
  PRIMARY KEY(medewerkerscode),
  CHECK (medewerkerscode~'^[^" "]*$'),
	CHECK (medewerkerscode != ''));

CREATE TABLE persona(
  studentnummer   VARCHAR(7)      REFERENCES student (studentnummer) 				ON DELETE CASCADE, --als er een iets wordt verwijderd wat een verwijzing heeft naar een of meerdere tabellen, wordt de waarde in deze tabellen ook verwijderd
  medewerkerscode VARCHAR(6)      REFERENCES docent (medewerkerscode) 			ON DELETE CASCADE, --met references geven we foreign keys aan
  voornaam 				VARCHAR(30)     NOT NULL,
	achternaam 			VARCHAR(30)     NOT NULL,
	tussenvoegsel	  VARCHAR(15)             ,
	geboortedatum 	DATE          	NOT NULL,
	geslacht 				VARCHAR(9)      		,
	telefoonnummer 	VARCHAR(15)     NOT NULL,
  straat					VARCHAR(30)     NOT NULL,
	huisnummer 			INT             NOT NULL,
	toevoeging 			VARCHAR(1)              ,
	postcode 				VARCHAR(6)      NOT NULL,
	woonplaats 			VARCHAR(50)     NOT NULL,
  CHECK (voornaam~'^[^0-9!?;:]*$'),               	--Voornaam, achternaam en tussenvoegsel mogen alle tekens bevatten behalve "0 tm 9, '!', '?', ';', ':'"
	CHECK (achternaam~'^[^0-9!?;:]*$'),
	CHECK (tussenvoegsel~'^[^0-9!?;:]*$'),
	CHECK (geboortedatum < current_date),
  CHECK (geslacht IN('man', 'vrouw','onbepaald')),
  CHECK (telefoonnummer~'^[0-9.()" "-]*$'),       	--Telefoonnummer mag alleen 0 tm 9 bevatten, de speciale tekens '.','(',')','-' en spaties
	CHECK (straat~'^[a-zA-Z" "-]*$'),               	--Straat en woonplaats mogen grote en kleine letters bevatten, het teken '-' en spaties
	CHECK (toevoeging~'^[a-zA-Z]*$'),               	--Toevoeging alleen maar grote en kleine letters
	CHECK (postcode~'^[1-9][0-9]{3} ?[a-zA-Z]{2}$'), 	--Postcode moet bestaan uit vier cijfers en twee grote of kleine letters
	CHECK (woonplaats~'^[a-zA-Z" "-]*$'));

CREATE TABLE cursus(
	cursuscode 			VARCHAR(30)     NOT NULL,
	omschrijving 		VARCHAR(255)    NOT NULL,
	docent 					VARCHAR(6)      REFERENCES docent (medewerkerscode) 			ON DELETE CASCADE,
	startdatum 			TIMESTAMP       NOT NULL,
	einddatum 			TIMESTAMP       NOT NULL,
	PRIMARY KEY(cursuscode),
  CHECK (cursuscode~'^[A-Z]*$'),
  CHECK (einddatum > startdatum));


CREATE TABLE groep(
  groepsnaam      VARCHAR(20)     NOT NULL,
	startdatum 			TIMESTAMP,
	einddatum 			TIMESTAMP,
  PRIMARY KEY(groepsnaam),
	CHECK(startdatum < einddatum));

	CREATE TABLE rooster(
	groep 				VARCHAR(20)     REFERENCES groep (groepsnaam) 						ON DELETE CASCADE,
	startdatum		TIMESTAMP       NOT NULL,
	einddatum			TIMESTAMP       NOT NULL,
	docent			  VARCHAR(6)      REFERENCES docent (medewerkerscode) 			ON DELETE CASCADE,
	lokaal 			  VARCHAR(20)     NOT NULL,
	cursuscode		VARCHAR(30)			REFERENCES cursus (cursuscode) 						ON DELETE CASCADE,
	PRIMARY KEY(groep, startdatum, einddatum),
  CHECK(startdatum < einddatum));

CREATE TABLE student_in_groep(
	groep_naam 							VARCHAR(20) NOT NULL 	REFERENCES groep (groepsnaam) 			ON DELETE CASCADE,
	student_studentnummer 	VARCHAR(7) 	NOT NULL	REFERENCES student (studentnummer) 	ON DELETE CASCADE,
	PRIMARY KEY (student_studentnummer, groep_naam));

CREATE TABLE student_heeft_cursus(
	cursus_cursuscode     	VARCHAR(20) NOT NULL 	REFERENCES cursus (cursuscode) 		ON DELETE CASCADE,
	student_studentnummer 	VARCHAR(7) 	NOT NULL	REFERENCES student (studentnummer) 	ON DELETE CASCADE,
	PRIMARY KEY (student_studentnummer, cursus_cursuscode));

CREATE TABLE groep_heeft_cursus(
	cursus_cursuscode   	VARCHAR(20) NOT NULL 	REFERENCES cursus (cursuscode) 		ON DELETE CASCADE,
	groep_groepsnaam	 	VARCHAR(20) 	NOT NULL	REFERENCES groep (groepsnaam) 		ON DELETE CASCADE,
	PRIMARY KEY (groep_groepsnaam, cursus_cursuscode));

CREATE OR REPLACE FUNCTION check_rooster_tijd() returns trigger AS $BODY$
	BEGIN

		if(SELECT max(einddatum) from rooster where groep = new.groep) < new.startdatum THEN
			if(SELECT max(einddatum) from rooster where docent = new.docent) < new.startdatum THEN
				if(SELECT max(einddatum) FROM rooster WHERE lokaal = new.lokaal) < new.startdatum THEN
					return new;
				else
					RAISE EXCEPTION '%. Message: %', 'in LOKAAL','Lokaal is al in gebruik.';
				END IF;
				else
					RAISE EXCEPTION '%. Message: %', 'in DOCENT','Docent geeft al les.';
			END IF;
			else
					RAISE EXCEPTION '%. Message: %', 'in GROEP','Groep heeft al les.';
		END IF;
		IF (new.einddatum < new.startdatum) THEN
			RAISE exception ' %. Message: %', 'in TIJD','eindtijd is eerder dan begintijd';
		END IF;
		return null;
	END;
$BODY$ language plpgsql;

--CREATE trigger check_rooster_tijd_trigger BEFORE INSERT or UPDATE ON rooster
--FOR EACH ROW EXECUTE PROCEDURE check_rooster_tijd();
