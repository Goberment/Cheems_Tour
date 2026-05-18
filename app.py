from flask import Flask, jsonify, request
from entities.trip import Trip 


app = Flask(__name__)

@app.route('/', methods=['GET'])
def index():
    return jsonify({"success": True, "message": "Helo world"}), 200

@app.route('/trips', methods=["GET"])
def trips():
    trips = Trip.get_all()
    return trips

@app.route('/trip', methods=['POST'])
def save_trip():
    data = request.json
    if not data:
        return jsonify({
            "success": False,
            "message": "No data received"
        }), 400

    trip = Trip(name=data['name'], city=data['city'], latitude=data['latitude'], longitude=data['longitude'])
    id = trip.save()
    success = id is not None
    return jsonify(success), 201

if __name__ == "__main__":
    app.run(host = ('0.0.0.0'), port=5000)