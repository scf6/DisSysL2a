import xmlrpclib, sys, time
from numpy import mean
from threading import Thread
import multiprocessing
import matplotlib.pyplot as plt

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

	queue.put(str(sum(diffs)/float(len(diffs))))

p_list = list()
queue = multiprocessing.Queue()

for i in range(100):
	process = multiprocessing.Process(target = thread_job, args=())
	p_list.append(process)
	process.start()

for p in p_list:
	p.join()


vals = list()
while not queue.empty():
	vals.append(float(queue.get()))

print(vals)

plt.hist(vals)
plt.xlabel("Average response time in seconds")
plt.savefig("times_histogram.png")
plt.close()
