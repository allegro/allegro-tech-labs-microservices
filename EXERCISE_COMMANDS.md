# 1.1

wrk -t2 -c10 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080


compile('org.apache.httpcomponents:httpclient:4.5.5')

# 1.2

HttpClientBuilder clientBuilder = HttpClients.custom()
                .setConnectionManager(manager)
                .setDefaultRequestConfig(requestConfig)

new HttpComponentsClientHttpRequestFactory(...)


wrk -t2 -c10 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080


# 1.3

wrk -t2 -c10 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080

# 2.1

toxiproxy-cli toxic add -n gallery_service_latency -t latency -tox 1.0 -a latency=1 -a jitter=1500 -d gallery_service

wrk -t2 -c10 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080

# 2.2

toxiproxy-cli toxic add -n gallery_service_latency -t latency -tox 1.0 -a latency=1 -a jitter=1500 -d gallery_service


wrk -t2 -c10 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080


# 3.1

toxiproxy-cli toxic add -n mongo_latency -t latency -tox 1.0 -a latency=1 -a jitter=1500 -d mongo


wrk -t2 -c10 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080


# 3.2

wrk -t2 -c10 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080


# 4.1

wrk -t2 -c10 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080


# 4.2

wrk -t2 -c10 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080


# 5.1

toxiproxy-cli toxic add -n mongo_latency -t latency -tox 1.0 -a latency=10000 -d mongo


wrk -t2 -c20 -d120s -R100 --latency -s random_offers.lua http://127.0.0.1:8080


http http://127.0.0.1:8080/actuate/info


# 5.2

wrk -t2 -c20 -d120s -R100 --latency -s random_offers.lua http://127.0.0.1:8080


# 6.1

toxiproxy-cli toxic add -n gallery_service_latency -t latency -tox 1.0 -a latency=5000 -d gallery_service


toxiproxy-cli toxic add -n description_service_latency -t latency -tox 1.0 -a latency=500 -d description_service


time http http://127.0.0.1:8080/offers/12345

# 6.3

wrk -t2 -c20 -d30s -R50 --latency -s random_offers.lua http://127.0.0.1:8080


