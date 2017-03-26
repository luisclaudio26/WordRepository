package words.search;

import words.repo.IRepository;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

public class SearchGateway extends UnicastRemoteObject implements ISearchGateway {

	List<String> servers;

	public SearchGateway() throws RemoteException
	{
		servers = new ArrayList<String>();
	}

	//----------------------------------
	//------ From ISearchGateway -------
	//----------------------------------
	@Override
	public void registerServer(String serverName) throws RemoteException
	{
		try {
			System.out.print("Registering server...");

			servers.add(serverName);

			System.out.print("done.\n");
		} catch(Exception e) {
			System.out.println("ERROR WHILE TRYING TO REGISTER SERVER NAME.\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void register(String w) throws RemoteException
	{
		try{
			System.out.print("Registering word '" + w + "'...");
			
			int serverId = (new Random()).nextInt() % servers.size();
			IRepository repo = (IRepository)Naming.lookup( servers.get(serverId) );
			
			repo.pushWord(w);

			System.out.print("done.\n");
		} catch(Exception e) {
			System.out.println("ERROR WHILE TRYING TO REGISTER WORD.\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String> search(String w) throws RemoteException
	{
		List<String> out = new ArrayList<String>();

		try {
			System.out.print("Searching for '" + w + "'...");

			for(String name : servers)
			{
				IRepository repo = (IRepository)Naming.lookup(name);
				if( repo.hasWord(w) ) out.add(name);
			}

			System.out.print("done.\n");

		} catch(Exception e) {
			System.out.println("ERROR WHILE TRYING TO RETRIEVE WORD.\n" + e.getMessage());
			e.printStackTrace();
		} finally {
			return out;
		}
	}

	//------ search gateway thread -------
	public static void main(String[] args) throws RemoteException {

		if(args.length < 1)
		{
			System.err.println("Please, input server IP address as argument");
			return;
		}

		String address = args[0];
	  	System.setProperty("java.rmi.server.hostname", address);
		
		try {
			System.out.println("Waking up query server...");

			ISearchGateway rep = new SearchGateway();
			Naming.rebind("//" + address + "/query", rep);

			System.out.print("Ok. I'm listening at //" + address + "/query.");
		} catch(Exception e) {
			System.err.println("REPOSITORY ERROR:\n" + e.getMessage());
			e.printStackTrace();
		}

	}
}
