//java -Djava.security.policy=server.policy -Djava.rmi.server.hostname=rath HelloClient rath

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloClient {
    private HelloClient() {}
    public static void main(String[] args) {
	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new SecurityManager());
	}
	String host = args[0];
	System.out.println(host);
	try {
	    Registry registry = LocateRegistry.getRegistry(host, 8888);
	    Hello stub = (Hello) registry.lookup("Hello");
	    String response = stub.sayHello();
	    System.out.println("response: " + response);
	} catch (Exception e) {
	    System.err.println("Client exception!");
	    e.printStackTrace();
	}
    }
}
