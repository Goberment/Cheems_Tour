import mysql.connector 


def get_connection():
    connection = mysql.connector.connect(
       host="localhost",
       port="3307",
       user="root",
       password="goberment31",
       database="cheemsdb"
        )
    
    return connection