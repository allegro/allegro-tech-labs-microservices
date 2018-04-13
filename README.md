Allegro Tech Labs - Microservices a'la allegro
====

# Requirements

* Vagrant 2.0+

# Environment

Everything is run in Ubuntu 16 box on Vagrant. Box contains:

* MongoDB 3.6
* [Toxiproxy 2.x](https://github.com/Shopify/toxiproxy#cli-example)
* [Graphite](https://graphiteapp.org/) - [Link to Graphite on Vagrant](http://10.10.10.10:3080/)
* [Grafana](https://grafana.com/) - [Link to Grafana on Vagrant](http://10.10.10.10:3000/) user: admin, pass: admin
* [wrk2](https://github.com/giltene/wrk2)
* [jmxtrans](https://github.com/jmxtrans/jmxtrans/) - stats stored in Graphite in `servers.*`

## Setup

Start Vagrant box by running the following command in project root directory:

```
vagrant up
```

Directory in which Vagrantfile is present will be mounted on virtual machine as `/vagrant`. Any changes done on
host system (editing files etc) are immediately visible to virtual machine and vice versa.

## Check the setup

After starting Vagrant box you should be able to see Grafana login window at `10.10.10.10:3000`.
Credentials are `admin`/`admin`.

## Applications

Upon start no applications are running. Apps are controlled using `supervisord`.

To start applications use `supervisorctl`:

```
sudo supervisorctl
start data-server
start main-service
```

To restart running application:

```
sudo supervisorctl
restart main-service
```

They are started using simple `./gradlew bootRun` in `/vagrant/<app>` directory.
This means that you can edit source files in IDE on host system and restart application in virtual machine for changes
to take effect.

## Database

Data is located in `allegro_tech_labs` database, `offers` collection. There should be 100 000 entries.

```
mongo
> use allegro_tech_labs;
> db.offers.size();
100000
```

## Toxic proxies

After startup, there should be 3 proxies present and used by main-service. To find out just type:

```
toxiproxy-cli list
```

and you should see a similar list:

```
Name			Listen		Upstream		Enabled		Toxics
======================================================================================
description_service	127.0.0.1:9998	localhost:8888		enabled		None
gallery_service		127.0.0.1:9999	localhost:8888		enabled		None
mongo			127.0.0.1:27117	localhost:27017		enabled		None

Hint: inspect toxics with `toxiproxy-cli inspect <proxyName>`
```

#### Few examples:

- Create toxic `mongo_latency` for proxy `mongo` that adds constant delay of 10 ms to 5% of MongoDB requests:

```
toxiproxy-cli toxic add mongo -n mongo_latency -t latency --tox 0.05 -a latency=10
```

now calling `toxiproxy-cli list` should print following results:

```
Name			Listen		Upstream		Enabled		Toxics
======================================================================================
description_service	127.0.0.1:9998	localhost:8888		enabled		None
gallery_service		127.0.0.1:9999	localhost:8888		enabled		None
mongo			127.0.0.1:27117	localhost:27017		enabled		1

Hint: inspect toxics with `toxiproxy-cli inspect <proxyName>`
```

- Modify toxic `mongo_latency`: increase amount of affected requests to 10% and add jitter of 50 ms:

```
toxiproxy-cli toxic update -n mongo_latency --tox 0.1 -a jitter=50 mongo
```

now calling `toxiproxy-cli inspect mongo` should print following results:

```
Name: mongo	Listen: 127.0.0.1:27117	Upstream: localhost:27017
======================================================================
Upstream toxics:
Proxy has no Upstream toxics enabled.

Downstream toxics:
mongo_latency:	type=latency	stream=downstream	toxicity=0.10	attributes=[	jitter=50	latency=10	]

Hint: add a toxic with `toxiproxy-cli toxic add`
```

- Remove toxic `mongo_latency` from proxy `mongo`:

```
toxiproxy-cli toxic remove -n mongo_latency mongo
```

## Wrk2

"wrk2 (as is wrk) is a modern HTTP benchmarking tool capable of generating significant load when run on a single
 multi-core CPU. It combines a multithreaded design with scalable event notification systems such as epoll and kqueue."

 #### Few examples

 - Generate constant HTTP GET `http://127.0.0.1:8080/offers/1` traffic of 100 RPS (`-R100`) with two threads (`-t2`) and reusing 20 connections (`-c20`). Measure
 latency and print latency histogram (`--latency`) over the 60 seconds (`-d60s`) time:

 ```
 wrk -t2 -c20 -d60s -R100 --latency http://127.0.0.1:8080/offers/1
 ```

 - Randomize requests by choosing random offer id 1 to 100000

Call `wrk`, pass extra `-s random_offers.lua` argument and modify HTTP address to server root:

 ```
 wrk -t2 -c20 -d60s -R100 --latency -s random_offers.lua http://127.0.0.1:8080
 ```

This will cause Wrk2 to randomly query for different offers (uniformly distributed).
