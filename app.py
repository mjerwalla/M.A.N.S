from flask import Flask, render_template, request
from flaskext.mysql import MySQL
import json
import pymysql
app = Flask(__name__)

conn = pymysql.connect(host='127.0.0.1', port=3306, user='test', passwd='test', db='medaid',autocommit=True)

@app.route('/', methods=['GET', 'POST'])
def hello_world():
    print(request.args)
    return 'Hello, World!'


@app.route('/addUser', methods=['GET', 'POST'])
def addUser():
    if request.method == "POST":
        cur = conn.cursor()
        jsonData = request.get_json()
        userName = jsonData['userName']
        password = jsonData['password']
        firstName = jsonData['firstName']
        lastName = jsonData['lastName']
        userType = jsonData['userType']
        print(userName + password + firstName + lastName + userType)
        cur.execute("""INSERT INTO Users (userName, password, firstName, lastName, userType) VALUES (%s, %s,%s,%s,%s)""", (userName, password, firstName, lastName, userType))
        cur.execute("""SELECT LAST_INSERT_ID()""");
        row_headers=[x[0] for x in cur.description] #this will extract row headers
        rv = cur.fetchall()
        json_data=[]
        for result in rv:
            json_data.append(dict(zip(row_headers,result)))
        cur.close()
        return json.dumps(json_data, default=str)

@app.route('/addReport', methods=['GET', 'POST'])
def addReport():
    if request.method == "POST":
        cur = conn.cursor()
        jsonData = request.get_json()
        userID = jsonData['userID']
        reportName = jsonData['reportName']
        pdfName = jsonData['pdfName']
        uri = jsonData['uri']
        print(userID + reportName + pdfName + uri)
        cur.execute("""INSERT INTO Reports (userID, reportName, pdfName, uri) VALUES (%s,%s,%s,%s)""", (userID, reportName, pdfName, uri))
        cur.execute("""SELECT * FROM Reports WHERE userID = %s""", (userID))
        row_headers=[x[0] for x in cur.description] #this will extract row headers
        rv = cur.fetchall()
        json_data=[]
        for result in rv:
            json_data.append(dict(zip(row_headers,result)))
        cur.close()
        return json.dumps(json_data, default=str)

@app.route('/addVaccination', methods=['GET', 'POST'])
def addVaccination():
    if request.method == "POST":
        cur = conn.cursor()
        jsonData = request.get_json()
        userID = jsonData['userID']
        vacName = jsonData['vacName']
        timeOfVac = jsonData['timeOfVac']
        print(userID + vacName + timeOfVac)
        cur.execute("""INSERT INTO Vaccinations (userID, vacName, timeOfVac) VALUES (%s,%s,%s)""", (userID, vacName, timeOfVac))
        cur.execute("""SELECT * FROM Vaccinations WHERE userID = %s""", (userID))
        row_headers=[x[0] for x in cur.description] #this will extract row headers
        rv = cur.fetchall()
        json_data=[]
        for result in rv:
            json_data.append(dict(zip(row_headers,result)))
        cur.close()
        return json.dumps(json_data, default=str)

@app.route('/addPatientToCareTaker', methods=['GET', 'POST'])
def addPatientToCareTaker():
    if request.method == "POST":
        cur = conn.cursor()
        jsonData = request.get_json()
        careTakerID = jsonData['careTakerID']
        patientID = jsonData['patientID']
        print(careTakerID + patientID)
        cur.execute("""INSERT INTO CareTakers (careTakerID, patientID) VALUES (%s,%s)""", (careTakerID, patientID))
        cur.execute("""SELECT patientID FROM CareTakers WHERE careTakerID = %s""", (careTakerID))
        row_headers=[x[0] for x in cur.description] #this will extract row headers
        rv = cur.fetchall()
        json_data=[]
        for result in rv:
            json_data.append(dict(zip(row_headers,result)))
        cur.close()
        return json.dumps(json_data, default=str)

@app.route('/addAppointment', methods=['GET', 'POST'])
def addAppointment():
    if request.method == "POST":
        cur = conn.cursor()
        jsonData = request.get_json()
        userID = jsonData['userID']
        appointmentName = jsonData['appointmentName']
        timeOfApt = jsonData['timeOfApt']
        print(userID + appointmentName + timeOfApt)
        cur.execute("""INSERT INTO Appointments (userID, appointmentName, timeOfApt) VALUES (%s,%s,%s)""", (userID, appointmentName, timeOfApt))
        cur.execute("""SELECT * FROM Appointments WHERE userID = %s""", (userID))
        row_headers=[x[0] for x in cur.description] #this will extract row headers
        rv = cur.fetchall()
        json_data=[]
        for result in rv:
            json_data.append(dict(zip(row_headers,result)))
        cur.close()
        return json.dumps(json_data, default=str)


@app.route('/addMedication', methods=['GET', 'POST'])
def addMedication():
    if request.method == "POST":
        cur = conn.cursor()
        jsonData = request.get_json()
        userID = jsonData['userID']
        medName = jsonData['medName']
        startDate = jsonData['startDate']
        endDate = jsonData['endDate']
        selectedDaysPerWeek = jsonData['selectedDaysPerWeek']
        numTimesPerDay = jsonData['numTimesPerDay']
        timesToBeReminded = jsonData['timesToBeReminded']
        dosagePerIntake = jsonData['dosagePerIntake']
        takenInPast = jsonData['takenInPast']
        totalNumPills = jsonData['totalNumPills']
        notes = jsonData['notes']
        print(userID)
        print(medName)
        print(startDate)
        print(endDate)
        print(selectedDaysPerWeek)
        print(numTimesPerDay)
        print(timesToBeReminded)
        print(dosagePerIntake)
        print(takenInPast)
        print(totalNumPills)
        print(notes)
        cur.execute("""INSERT INTO Medications (userID, medName, startDate, endDate, selectedDaysPerWeek, numTimesPerDay, timesToBeReminded, dosagePerIntake, takenInPast, totalNumPills, notes) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)""",
        (userID, medName, startDate, endDate, selectedDaysPerWeek, numTimesPerDay, timesToBeReminded, dosagePerIntake,takenInPast,totalNumPills,notes))
        cur.execute("""SELECT * FROM Medications WHERE userID = %s""", (userID))
        row_headers=[x[0] for x in cur.description] #this will extract row headers
        rv = cur.fetchall()
        json_data=[]
        for result in rv:
             json_data.append(dict(zip(row_headers,result)))
        cur.close()
        return json.dumps(json_data, default=str)


@app.route('/getUser/<userID>', methods=['GET'])
def getUser(userID):
    cur = conn.cursor()
    cur.execute("""SELECT firstName,lastName FROM Users WHERE userID = %s""", (userID))
    row_headers=[x[0] for x in cur.description] #this will extract row headers
    rv = cur.fetchall()
    json_data=[]
    for result in rv:
         json_data.append(dict(zip(row_headers,result)))
    cur.close()
    return json.dumps(json_data, default=str)

@app.route('/getAppointments/<userID>', methods=['GET'])
def getAppointments(userID):
    cur = conn.cursor()
    cur.execute("""SELECT * FROM Appointments WHERE userID = %s""", (userID))
    row_headers=[x[0] for x in cur.description] #this will extract row headers
    rv = cur.fetchall()
    json_data=[]
    for result in rv:
         json_data.append(dict(zip(row_headers,result)))
    cur.close()
    return json.dumps(json_data, default=str)

@app.route('/getUserMedicalHistory/<userID>', methods=['GET'])
def getUserMedicalHistory(userID):
    cur = conn.cursor()
    cur.execute("""SELECT * FROM Medications WHERE userID = %s""", (userID))
    row_headers=[x[0] for x in cur.description] #this will extract row headers
    rv = cur.fetchall()
    json_data=[]
    for result in rv:
         json_data.append(dict(zip(row_headers,result)))
    cur.close()
    return json.dumps(json_data, default=str)

@app.route('/getVaccinations/<userID>', methods=['GET'])
def getVaccinations(userID):
    cur = conn.cursor()
    cur.execute("""SELECT * FROM Vaccinations WHERE userID = %s""", (userID))
    row_headers=[x[0] for x in cur.description] #this will extract row headers
    rv = cur.fetchall()
    json_data=[]
    for result in rv:
         json_data.append(dict(zip(row_headers,result)))
    cur.close()
    return json.dumps(json_data, default=str)

@app.route('/getReports/<userID>', methods=['GET'])
def getReports(userID):
    cur = conn.cursor()
    cur.execute("""SELECT * FROM Reports WHERE userID = %s""", (userID))
    row_headers=[x[0] for x in cur.description] #this will extract row headers
    rv = cur.fetchall()
    json_data=[]
    for result in rv:
         json_data.append(dict(zip(row_headers,result)))
    cur.close()
    return json.dumps(json_data, default=str)

@app.route('/getCurrentMeds/<userID>', methods=['GET'])
def getCurrentMeds(userID):
    cur = conn.cursor()
    cur.execute("""SELECT * FROM Medications WHERE NOW() <= endDate AND startDate <= NOW() AND takenInPast = 0 AND userID = %s""", (userID))
    row_headers=[x[0] for x in cur.description] #this will extract row headers
    rv = cur.fetchall()
    json_data=[]
    for result in rv:
         json_data.append(dict(zip(row_headers,result)))
    cur.close()
    return json.dumps(json_data, default=str)


@app.route('/validateUser/<username>/<password>', methods=['GET'])
def validateUser(username,password):
    cur = conn.cursor()
    cur.execute("""SELECT userID,userType FROM Users WHERE userName = %s AND password = %s""", (username,password))
    row_headers=[x[0] for x in cur.description] #this will extract row headers
    rv = cur.fetchall()
    json_data=[]
    for result in rv:
         json_data.append(dict(zip(row_headers,result)))
    cur.close()
    return json.dumps(json_data, default=str)

@app.route('/getAllUsers', methods=['GET'])
def getAllUsers():
   cur = conn.cursor()
   cur.execute('SELECT * FROM Users')
   row_headers=[x[0] for x in cur.description] #this will extract row headers
   rv = cur.fetchall()
   json_data=[]
   for result in rv:
        json_data.append(dict(zip(row_headers,result)))
   cur.close()
   return json.dumps(json_data)

if __name__ == '__main__':
   app.run(host='0.0.0.0',port=5000,debug=True)
