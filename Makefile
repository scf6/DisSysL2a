all: c

server:
	javac TinyBookServer.java

run:
	java -classpath ".:../sqlite-jdbc-3.30.1.jar" TinyBookServer
