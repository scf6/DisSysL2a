all: server

server:
	javac TinyBookServer.java
	javac TinyClient.java
run:
	java TinyBookServer
