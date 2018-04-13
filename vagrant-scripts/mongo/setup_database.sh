#!/bin/bash

mongo < /tmp/vagrant-scripts/mongo/setup_database.js
mongoimport --db allegro_tech_labs --collection offers --file /tmp/vagrant-scripts/mongo/allegro_tech_labs.offers.json
