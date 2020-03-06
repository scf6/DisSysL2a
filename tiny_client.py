import xmlrpclib, sys

#specify server hostname as command line argument
name = "http://"+sys.argv[1]+":8888"

server = xmlrpclib.Server(name)

print server.service.lookup("53477")
print server.service.buy("53477")
print server.service.lookup("53477")
