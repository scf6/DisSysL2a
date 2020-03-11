import xmlrpclib, sys, time
from numpy import mean

#specify server hostname as command line argument
name = "http://"+sys.argv[1]+":8888"

server = xmlrpclib.Server(name)

diffs = []
for i in range(500):
	start = time.time()
	server.service.buy("12365")
	end = time.time()
	diffs.append(end-start)

print "Average response time was " + str(sum(diffs)/float(len(diffs)))

