package words.repo;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.Set;
import java.util.TreeSet;
import words.search.*;
import words.client.*;

public class Repository extends UnicastRemoteObject implements IRepository {
	public static int PORT = 1100;
	private Set<String> words;

	public Repository() throws RemoteException {
		words = new TreeSet<String>();
	}

	//------------------------------
	//------ From IRepository ------
	//------------------------------
	@Override
	public void pushWord(String w) {
		System.out.print("Pushing word '" + w + "'...");
		words.add(w);
		System.out.print("done.\n");
	}

	@Override
	public void removeWord(String w) {
		System.out.print("Remove word '" + w + "'...");
		words.remove(w);
		System.out.print("done.\n");
	}

	@Override
	public boolean hasWord(String w) {
		System.out.println("Checking for word '" + w + "'...");
		return words.contains(w);
	}

	//----- repository thread -----
	public static void main(String[] args) {

		if(args.length < 4)
		{
			System.err.println("Provide server name, address, port and gateway address as argument.");
			return;
		}

		String serverName = args[0];
		String serverAddress = args[1];
		int serverPort = Integer.parseInt(args[2]);
		String gatewayAddress = args[3];


	  	System.setProperty("java.rmi.server.hostname", serverAddress);

		try {
			//Register this repository in gateway server
			System.out.print("Registering in gateway...");
			
			Registry gatewayReg = LocateRegistry.getRegistry(gatewayAddress, SearchGateway.PORT);
			ISearchGateway gateway = (ISearchGateway)gatewayReg.lookup("query");
			gateway.registerServer(serverName, serverAddress, serverPort);
			
			System.out.print("done.\n");

			//wake up this server
			System.out.println("Waking up server...");
			
			IRepository rep = new Repository();
			Registry reg = LocateRegistry.createRegistry(serverPort);
			reg.rebind(serverName, rep);
			
			System.out.println("Ok. I'm listening at " + serverAddress + ":" + serverName);

		} catch(Exception e) {
			System.err.println("REPOSITORY ERROR:\n" + e.getMessage());
			e.printStackTrace();
		}
	}
}
