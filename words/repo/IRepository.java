package words.repo;

import java.rmi.*;

public interface IRepository extends java.rmi.Remote
{
	void pushWord(String w) throws RemoteException;
	void removeWord(String w) throws RemoteException;
	boolean hasWord(String w) throws RemoteException;
}