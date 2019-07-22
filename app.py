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
        print(userID)
        print(medName)
        print(startDate)
        print(endDate)
        print(selectedDaysPerWeek)
        print(numTimesPerDay)
        print(timesToBeReminded)
        print(dosagePerIntake)
        print(takenInPast)
        cur.execute("""INSERT INTO Medications (userID, medName, startDate, endDate, selectedDaysPerWeek, numTimesPerDay, timesToBeReminded, dosagePerIntake, takenInPast) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s)""",
        (userID, medName, startDate, endDate, selectedDaysPerWeek, numTimesPerDay, timesToBeReminded, dosagePerIntake,takenInPast))
        cur.execute("""SELECT * FROM Medications WHERE userID = %s""", (userID))
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

@app.route('/getCurrentMeds/<userID>', methods=['GET'])
def getCurrentMeds(userID):
    cur = conn.cursor()
    cur.execute("""SELECT * FROM Medications WHERE NOW() <= endDate AND startDate <= NOW() AND userID = %s""", (userID))
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
