import xmlrpclib, sys

#specify server hostname as command line argument
name = "http://"+sys.argv[1]+":8888"

server = xmlrpclib.Server(name)

answer = server.sample.SumAndDifference(10, 40)

print "Sum:", answer[0]
print "Difference:", answer[1]
