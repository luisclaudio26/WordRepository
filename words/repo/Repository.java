package words.repo;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Set;
import java.util.TreeSet;

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
			String host = "localhost";
			IRepository rep = new Repository();
			Naming.rebind("//localhost/WordRep", rep);
		} catch(Exception e) {
			System.err.println("REPOSITORY ERROR:\n" + e.getMessage());
			e.printStackTrace();
		}
	}
}