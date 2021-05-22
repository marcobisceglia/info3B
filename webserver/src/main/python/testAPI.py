import requests
import json
import pandas as pd

headers = {
		'Content-Type': 'application/json',
		'Cookie': 'JSESSIONID=FE09E140E9BFC33AB8F7E1437DADB10C'
}

boats_file = "./boats.csv"
trips_file = "./trips.csv"
users_file = "./users.csv"
bookings_file = "./bookings.csv"

# boats
df = pd.read_csv(boats_file)
keys = df.columns.to_list()
boats = []

for index, row in df.iterrows():
  boat = dict()
  i = 0
  for word in row:
    boat[keys[i]] = word
    i+=1
  boats.append(boat)

# trips
df = pd.read_csv(trips_file)
keys = df.columns.to_list()
trips = []

for index, row in df.iterrows():
  trip = dict()
  i = 0
  for word in row:
    trip[keys[i]] = word
    i+=1
  trips.append(trip)

# users
df = pd.read_csv(users_file)
keys = df.columns.to_list()
users = []

for index, row in df.iterrows():
  user = dict()
  i = 0
  for word in row:
    user[keys[i]] = word
    i+=1
  users.append(user)

# bookings
df = pd.read_csv(bookings_file)
keys = df.columns.to_list()
bookings = []

for index, row in df.iterrows():
  booking = dict()
  i = 0
  for word in row:
    booking[keys[i]] = word
    i+=1
  bookings.append(booking)

# call api

for boat in boats:
  payload = json.dumps(boat)
  response = requests.request("POST", "http://localhost:8080/boats", headers=headers, data=payload)
  print(response.text)

for trip in trips:
  payload = json.dumps(trip)
  response = requests.request("POST", "http://localhost:8080/trips", headers=headers, data=payload)
  print(response.text)

for user in users:
  payload = json.dumps(user)
  response = requests.request("POST", "http://localhost:8080/users/signup", headers=headers, data=payload)
  print(response.text)

for booking in bookings:
  payload = json.dumps(booking)
  response = requests.request("POST", "http://localhost:8080/bookings", headers=headers, data=payload)
  print(response.text)
