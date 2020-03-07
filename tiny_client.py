import xmlrpclib, sys

#specify server hostname as command line argument
name = "http://"+sys.argv[1]+":8888"

server = xmlrpclib.Server(name)

while True:

	request = sys.stdin.readline()

	cmd = request.strip().split(' ')
	print cmd

	if cmd[0] == "lookup":
		print server.service.lookup(cmd[1])
	elif cmd[0] == "buy":
		print server.service.buy(cmd[1])
	elif cmd[0] == "search":
		print server.service.search(cmd[1])
	elif cmd[0] == "exit":
		break
	else:
		print "usage: lookup item number | buy item number | search topic | exit"


