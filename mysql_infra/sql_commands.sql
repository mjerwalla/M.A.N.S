CREATE TABLE medaid.Users (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `firstName` varchar(30) NOT NULL,
  `lastName` varchar(30) NOT NULL,
  `userType` int(11) NOT NULL,
  PRIMARY KEY (`userID`)
);

CREATE TABLE medaid.CareTakers(
   careTakerID INT NOT NULL,
   patientID INT NOT NULL,
   PRIMARY KEY(careTakerID,patientID),
   FOREIGN KEY(careTakerID) REFERENCES Users(userID),
   FOREIGN KEY(patientID) REFERENCES Users(userID)
);

CREATE TABLE medaid.Medications(
   medicationID INT NOT NULL AUTO_INCREMENT,
   userID INT NOT NULL,
   medName VARCHAR(20) NOT NULL,
   startDate DATETIME NOT NULL,
   endDate DATETIME,
   selectedDaysPerWeek VARCHAR(30),
   numTimesPerDay INT,
   timesToBeReminded VARCHAR(30),
   dosagePerIntake INT,
   takenInPast INT,
   PRIMARY KEY(medicationID),
   FOREIGN KEY(userID) REFERENCES Users(userID)
);

CREATE TABLE medaid.Vaccinations(
    vaccinationID INTEGER NOT NULL AUTO_INCREMENT,
	userID	INTEGER NOT NULL,
	vacName	TEXT NOT NULL,
	timeOfVac	DATETIME,
	PRIMARY KEY(vaccinationID),
    FOREIGN KEY(userID) REFERENCES Users(userID)
);

CREATE TABLE medaid.Appointments(
    appointmentID INTEGER NOT NULL AUTO_INCREMENT,
    userID INTEGER NOT NULL,
    appointmentName VARCHAR(40),
    timeOfApt DATETIME,
    PRIMARY KEY(appointmentID),
    FOREIGN KEY(userID) REFERENCES Users(userID)
);

CREATE TABLE medaid.Reports (
  reportID INTEGER NOT NULL AUTO_INCREMENT,
  userID INTEGER NOT NULL,
  reportName TEXT NOT NULL,
  pdfName TEXT NOT NULL,
  uri TEXT NOT NULL,
  PRIMARY KEY(reportID),
  FOREIGN KEY(userID) REFERENCES Users(userID)
);

