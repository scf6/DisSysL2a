import xmlrpclib, sys, time
from numpy import mean
from threading import Thread
import multiprocessing

#specify server hostname as command line argument
name = "http://"+sys.argv[1]+":8888"

server = xmlrpclib.Server(name)

def thread_job():
	diffs = []
	for i in range(500):
		start = time.time()
		server.service.search("College Life")
		end = time.time()
		diffs.append(end-start)

	print "Average response time was " + str(sum(diffs)/float(len(diffs)))

p_list = list()

for i in range(100):
	process = multiprocessing.Process(target = thread_job, args=())
	p_list.append(process)
	process.start()

for p in p_list:
	p.join()
