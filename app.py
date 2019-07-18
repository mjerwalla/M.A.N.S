from flask import Flask, render_template, request
from flaskext.mysql import MySQL
import json
import pymysql
app = Flask(__name__)

conn = pymysql.connect(host='localhost', port=3306, user='root', passwd='456455Nabil', db='Test',autocommit=True)
cur = conn.cursor()
# app.config['MYSQL_DATABASE_HOST'] = 'localhost'
# app.config['MYSQL_DATABASE_USER'] = 'root'
# app.config['MYSQL_DATABASE_PASSWORD'] = '456455Nabil'
# app.config['MYSQL_DATABASE_DB'] = 'Test'
#
# mysql = MySQL(app)
# conn = mysql.connect()
# cursor =conn.cursor()


@app.route('/', methods=['GET', 'POST'])
def hello_world():
    print(request.args)
    return 'Hello, World!'


@app.route('/addUser', methods=['GET', 'POST'])
def addUser():
    if request.method == "POST":
        jsonData = request.get_json()
        userName = jsonData['userName']
        password = jsonData['password']
        firstName = jsonData['firstName']
        lastName = jsonData['lastName']
        userType = jsonData['userType']
        print(userName + password + firstName + lastName + userType)
        cur.execute("""INSERT INTO Users (userName, password, firstName, lastName, userType) VALUES (%s, %s,%s,%s,%s)""", (userName, password, firstName, lastName, userType))
        return 'success'

@app.route('/getAllUsers', methods=['GET'])
def abc():
   cur.execute('SELECT * FROM Users')
   row_headers=[x[0] for x in cur.description] #this will extract row headers
   rv = cur.fetchall()
   json_data=[]
   for result in rv:
        json_data.append(dict(zip(row_headers,result)))
   return json.dumps(json_data)

if __name__ == '__main__':
   app.run(host='0.0.0.0',port=80,debug=True)
