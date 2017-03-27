package words.search;

import java.rmi.*;
import java.util.*;

public interface ISearchGateway extends java.rmi.Remote
{
	void registerServer(String serverName, String address) throws RemoteException;

	void register(String w) throws RemoteException;
	List<String> search(String w) throws RemoteException;
}