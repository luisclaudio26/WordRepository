package words.client;

import javax.xml.namespace.QName;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

public class Client
{
	public static void main(String[] args) throws AxisFault
	{
		RPCServiceClient serviceClient = new RPCServiceClient();
		Options options = serviceClient.getOptions();
		EndpointReference targetEPR = new EndpointReference("http://localhost:8080/axis2/services/RepositoryService");
		options.setTo(targetEPR);

		//Upar uma palavraqq
		QName opPushWord = new QName("http://repo.words", "pushWord");

		Object[] opPushWordArgs = new Object[] { "TESTE" };
		serviceClient.invokeRobust(opPushWord, opPushWordArgs);
	}
}