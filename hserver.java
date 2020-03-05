//java -Djava.security.policy=server.policy -Djava.rmi.server.hostname=rath HelloServer

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloServer implements Hello {
    public HelloServer() {}
    public String sayHello() {  return "Hello, world!";  }
    public static void main(String args[]) {
	try {
	    if (System.getSecurityManager() == null) {
		System.setSecurityManager(new SecurityManager());
	    }
	    HelloServer obj = new HelloServer();
	    Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);
	    Registry registry = LocateRegistry.createRegistry(8888);
	    registry.bind("Hello", stub);
	} catch (Exception e) {  System.err.println("Server exception: " + e.toString());  }
    }
}
