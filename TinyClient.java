import java.util.*;
import java.net.URL;
import org.apache.xmlrpc.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.util.Scanner;

public class TinyClient {
   public static void main (String [] args) {
		String host = (args.length < 1) ? null : args[0];
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		XmlRpcClient client=null;

		try {
			 config.setServerURL(new URL("http://" + host + ":" + 8888));
			 client = new XmlRpcClient();
			 client.setConfig(config);
		} catch (Exception e) {
			 System.out.println("Problem connecting to server");
		}

		Scanner sc = new Scanner(System.in);
		System.out.println("usage: lookup item number | buy item number | search topic | exit");
		System.out.print("> ");
		Set<String> commands = new HashSet<String>();
		commands.add("lookup");
		commands.add("buy");
		commands.add("search");
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			Vector<String> params = new Vector<String>();
			String[] tokens = line.split(" ");
			if(tokens.length > 0 && tokens[0].equals("exit")) {
				System.out.println("Exiting client");
				return;
			}
			if((tokens.length > 2 && !tokens[0].equals("search")) || tokens.length == 0) {
				System.out.println("Invalid input");
				continue;
			}
			if(commands.contains(tokens[0])) {
				if(tokens.length < 2) {
					System.out.println("Too few arguments");
					continue;
				} else {
					String arg = "";
					for(int i = 1; i< tokens.length; i++){
						arg = arg + " " + tokens[i];
					}
					params.addElement(arg.substring(1));    
				}
			}

			try {
				switch(tokens[0]) {
				case "lookup":
					 String lookupResult = (String)client.execute("service.lookup", params.toArray());
					 System.out.println(lookupResult);
					 break;
				case "buy":
					 String buyResult = (String)client.execute("service.buy", params.toArray());
					 System.out.println(buyResult);
					 break;
				case "search":
					 Object[] searchResult = (Object[])client.execute("service.search", params.toArray());
					 System.out.println(Arrays.toString(searchResult));
					 break;
				default:
					 System.out.println("usage: lookup item number | buy item number | search topic | exit");
					 continue;
				}
			} catch (Exception exception) {
				System.err.println("Client: " + exception);
			} 
			System.out.print("> ");
		}	    
    }
}
