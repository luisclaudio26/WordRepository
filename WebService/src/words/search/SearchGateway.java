package words.search;

import words.repo.Repository;
import java.util.*;
import javax.xml.namespace.QName;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

public class SearchGateway
{
	List<String> repoList;

	public SearchGateway()
	{
		repoList = new ArrayList<String>();
	}

	//------------------------
	//------ Interface -------
	//------------------------
	public void registerServer(String serverIP)
	{
		System.out.println("[Search gateway] Registering server " + serverIP);
		repoList.add( serverIP );
	}
	
	public void register(String w) throws AxisFault
	{
		if(repoList.size() == 0)
		{
			System.out.println("[Search gateway] No repository registered");
			return;
		}
		
		//this will randomly distribute the words among the repositories
		int id = (new Random()).nextInt() % repoList.size();
		String repo = "http://" + repoList.get(id) + ":8080/axis2/services/RepositoryService";

		System.out.println("[Search gateway] Storing word at " + repo);

		RPCServiceClient serviceClient = new RPCServiceClient();
		Options options = serviceClient.getOptions();
		options.setTo( new EndpointReference(repo) );
		options.setAction("urn:pushWord");

		QName pushWord = new QName("http://repo.words", "pushWord");
		Object[] pushWordArgs = new Object[] { w };
		serviceClient.invokeRobust(pushWord, pushWordArgs);
	}
	
	public String[] search(String w) throws AxisFault
	{
		List<String> found = new ArrayList<String>();
		for(String r: repoList){
			String repo = "http://" + r + ":8080/axis2/services/RepositoryService";
			System.out.println("[Search gateway] Looking up at " + repo);
			RPCServiceClient serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			options.setTo( new EndpointReference(repo) );
			options.setAction("urn:hasWord");
			
			QName hasWord = new QName("http://repo.words", "hasWord");
			Object[] hasWordArgs = new Object[] { w };
			Class[] returnTypes = new Class[]{ Boolean.class }; // boolean
			Object[] response = serviceClient.invokeBlocking(hasWord, hasWordArgs, returnTypes);
			System.out.println(response[0]);

			if((Boolean)response[0]){
				found.add(repo);
			}
		}

		System.out.print("[Search gateway] Returning this: [");
		for(String r : found)
			System.out.print(r + ", ");
		System.out.println("]");

		return found.toArray( new String[found.size()] );
	}
}

