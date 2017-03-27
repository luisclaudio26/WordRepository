package words.search;

import words.repo.IRepository;
import words.repo.Repository;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.*;

public class SearchGateway extends UnicastRemoteObject implements ISearchGateway {

	public static final int PORT = 1099;
	private Map<String,String> servers;

	public SearchGateway() throws RemoteException
	{
		servers = new TreeMap<String,String>();
	}

	//----------------------------------
	//------ From ISearchGateway -------
	//----------------------------------
	@Override
	public void registerServer(String serverName, String address) throws RemoteException
	{
		try {

			System.out.println("Registering server " + serverName + "@" + address);
			servers.put(serverName, address);

		} catch(Exception e) {
			System.out.println("ERROR WHILE TRYING TO REGISTER SERVER NAME.\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void register(String w) throws RemoteException
	{
		if(servers.isEmpty()) return;

		System.out.println("Enter register()");

		try{
			//this will randomly distribute the words among the repositories
			int serverId = (new Random()).nextInt() % servers.size();
			String serverName = servers.keySet().toArray(new String[0])[serverId];
			String serverAdd = servers.get(serverName);

			System.out.println("Registering word '" + w + "'");
			System.out.println("At " + serverName + ", " + serverId + ", " + serverAdd);

			Registry reg = LocateRegistry.getRegistry(serverAdd, Repository.PORT);
			IRepository repo = (IRepository)reg.lookup( serverAdd );
			
			repo.pushWord(w);
		} catch(Exception e) {
			System.out.println("ERROR WHILE TRYING TO REGISTER WORD.\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String> search(String w) throws RemoteException
	{
		List<String> out = new ArrayList<String>();

		if(servers.isEmpty()) return out;

		try {
			System.out.print("Searching for '" + w + "'...");

			for( String name : servers.keySet().toArray(new String[0]) )
			{
				Registry reg = LocateRegistry.getRegistry(servers.get(name), Repository.PORT);
				IRepository repo = (IRepository)reg.lookup(name);

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
			Registry reg = LocateRegistry.createRegistry(SearchGateway.PORT);
			reg.rebind("query", rep);

			System.out.print("Ok. I'm listening at " + address + ":query.");
		} catch(Exception e) {
			System.err.println("REPOSITORY ERROR:\n" + e.getMessage());
			e.printStackTrace();
		}

	}
}
