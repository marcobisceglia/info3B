import requests
import json
import pandas as pd

headers = {
  'Content-Type': 'application/json',
  'Cookie': 'JSESSIONID=FE09E140E9BFC33AB8F7E1437DADB10C'
}

# csv files
boats_file = "./boats.csv"
trips_file = "./trips.csv"
users_file = "./users.csv"
bookings_file = "./bookings.csv"

# emoj
boat_symbol = "\U0001F6A4"
user_symbol = "\U0001F93F"
login_symbol = "\U0001F4F3"
logout_symbol = "\U0001F4F4"
trip_symbol = "\U0001F3CA"
fail_symbol = "\U0000274C"
success_symbol = "\U00002705"
empty_symbol = "\U0001F7E9"
occupied_symbol = "\U0001F7E5"
try_symbol = "\U00002753"


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

print("\033[1m" + "\nRegistrazione utenti" + "\033[0m")
for user in users:
  payload = json.dumps(user)
  response = requests.request("POST", "http://localhost:8080/users/signup", headers=headers, data=payload)
  print(user_symbol + " " + response.json()["firstName"])

print("\033[1m" + "\nLogin utenti" + "\033[0m")
for user in users:
  payload = json.dumps({"email": user["email"], "password": user["password"]})
  response = requests.request("POST", "http://localhost:8080/users/login", headers=headers, data=payload)
  if response.json()["loggedIn"]:
    print(login_symbol + " " + user["firstName"] +" logged")

print("\033[1m" + "\nCreazione escursione" + "\033[0m")
for trip in trips:
  payload = json.dumps(trip)
  response = requests.request("POST", "http://localhost:8080/trips", headers=headers, data=payload)
  print(trip_symbol + " " + response.json()["dateTime"])

print("\033[1m" + "\nCreazione barche" + "\033[0m")
for boat in boats:
  payload = json.dumps(boat)
  response = requests.request("POST", "http://localhost:8080/boats", headers=headers, data=payload)
  print(boat_symbol + " " + response.json()["model"] + " " + (response.json()["seats"]*empty_symbol))

print("\033[1m" + "\nPrenotazioni escursione" + "\033[0m")
for booking in bookings:
  print("\n"+ try_symbol + " " + "Tentativo di prenotazione per gruppo da " + str(booking["numPeople"]) + " persone")
  payload = json.dumps(booking)
  response = requests.request("POST", "http://localhost:8080/bookings", headers=headers, data=payload)
  
  response_json = response.json()
  if type(response_json) is dict:
    print(fail_symbol + " " + response_json["message"])
  else:
    s = "barca"
    if len(response_json)>1:
      s = "barche"
    
    barche_str = ""
    for el in response_json:
      barche_str += el["model"] + " "
    
    print(success_symbol + " Gruppo allocato in " + str(len(response_json)) + " " + s + ": " + barche_str)

  response = requests.request("GET", "http://localhost:8080/trips/1/boats", headers={}, data={})
  
  arr = response.json()
  for i in arr:
    print(boat_symbol + " " + i["model"] + " " + (i["remainingSeats"]*empty_symbol) +  ((i["seats"]-i["remainingSeats"])*occupied_symbol))
# print("\nLogging out users...")
# for user in users:
#   payload = json.dumps({"email": user["email"], "password": user["password"]})
#   response = requests.request("POST", "http://localhost:8080/users/logout", headers=headers, data=payload)
#   print(response.text)
