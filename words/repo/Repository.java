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
		try {

			System.out.print("Registering in gateway...");
			ISearchGateway gateway = (ISearchGateway)Naming.lookup("//localhost/query");
			gateway.registerServer("WordRep");
			System.out.print("done.\n");

			System.out.println("Waking up server...");
			String host = "localhost";
			IRepository rep = new Repository();
			Naming.rebind("//" + host + "/WordRep", rep);
			System.out.print("Ok. I'm listening at //" + host + "/WordRep.");

		} catch(Exception e) {
			System.err.println("REPOSITORY ERROR:\n" + e.getMessage());
			e.printStackTrace();
		}
	}
}