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
		repoList.add( serverIP );
	}
	
	public void register(String w) throws AxisFault
	{
		//this will randomly distribute the words among the repositories
		int id = (new Random()).nextInt() % repoList.size();
		String repo = "http://" + repoList.get(id) + ":8080/axis2/services/RepositoryService";

		System.out.println("Storing word at " + repo);

		RPCServiceClient serviceClient = new RPCServiceClient();
		Options options = serviceClient.getOptions();
		options.setTo( new EndpointReference(repo) );

		QName pushWord = new QName("http://repo.words", "pushWord");
		Object[] pushWordArgs = new Object[] { w };
		serviceClient.invokeRobust(pushWord, pushWordArgs);
	}
	
	public List<String> search(String w)
	{
		return null;
	}
}
