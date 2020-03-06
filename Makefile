all: c

server:
	javac TinyBookServer.java

run:
	java -classpath ".:./jars/sqlite-jdbc-3.30.1.jar:./jars/xmlrpc-server-3.1.3.jar:./jars/xmlrpc-common-3.1.3.jar:./jars/xmlrpc-client-3.1.3.jar:./jars/ws-commons-util-1.0.2.jar:./jars/commons-logging-1.1.jar" TinyBookServer
