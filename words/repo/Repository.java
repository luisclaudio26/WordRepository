package words.repo;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Set;
import java.util.TreeSet;
import words.search.*;

public class Repository extends UnicastRemoteObject implements IRepository {
	Set<String> words;

	public Repository() throws RemoteException {
		words = new TreeSet<String>();
	}

	//------------------------------
	//------ From IRepository ------
	//------------------------------
	@Override
	public void pushWord(String w) {
		words.add(w);
	}

	@Override
	public void removeWord(String w) {
		words.remove(w);
	}

	@Override
	public boolean hasWord(String w) {
		return words.contains(w);
	}

	//----- repository thread -----
	public static void main(String[] args) {

		if(args.length < 3)
		{
			System.err.println("Provide server name, address and gateway address as argument.");
			return;
		}

		try {
			String serverName = args[0];
			String serverAddress = args[1];
			String gatewayAddress = args[2];

			//Register this repository in gateway server
			System.out.print("Registering in gateway...");
			ISearchGateway gateway = (ISearchGateway)Naming.lookup("//" + gatewayAddress + "/query");
			gateway.registerServer(serverName);
			System.out.print("done.\n");

			//wake up this server
			System.out.println("Waking up server...");
			IRepository rep = new Repository();
			Naming.rebind("//" + serverAddress + "/" + serverName, rep);
			System.out.print("Ok. I'm listening at //" + serverAddress + "/" + serverName);

		} catch(Exception e) {
			System.err.println("REPOSITORY ERROR:\n" + e.getMessage());
			e.printStackTrace();
		}
	}
}
