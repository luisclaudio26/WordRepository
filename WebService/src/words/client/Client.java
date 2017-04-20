package words.client;

import javax.xml.namespace.QName;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import java.util.*;

public class Client
{
	public static void main(String[] args) throws AxisFault
	{
		if( args.length < 1 )
		{
			System.out.println("Please input search gateway address.");
			return;
		}
		String gatewayIP = "http://" + args[0] + ":8080/axis2/services/SearchService";
		Scanner in = new Scanner(System.in);

		//remote call stuff
		RPCServiceClient serviceClient = new RPCServiceClient();
		Options options = serviceClient.getOptions();
		options.setTo( new EndpointReference(gatewayIP) );

		while(true)
		{
			int opt; String word;

			System.out.print("\nWord: "); 
			word = in.next();

            System.out.println("1. Push word");
            System.out.println("2. Lookup word");
            System.out.print("> "); opt = in.nextInt();

            switch(opt)
            {
            case 1:
                {
					//Upar uma palavraqq
					QName register = new QName("http://search.words", "register");
					
					Object[] registerArgs = new Object[] { word };
					serviceClient.invokeRobust(register, registerArgs);
                    
                    break;
                }
            case 2:
                {
                	//Procurar palavra
                	QName register = new QName("http://search.words", "search");
					Object[] registerArgs = new Object[] { word };
					Class[] returnTypes = new Class[]{ String[].class };

					Object[] response = serviceClient.invokeBlocking(register, registerArgs, returnTypes);
						
					if( response[0] == null)
						System.err.println("Search gateway returned null!");
					else
					{
						String[] repo = (String[])(response[0]);

						System.out.print(word + "is in servers: [");
						for(String s : repo)
							System.out.print(s + ", ");
						System.out.print("]");
					}

                    break;
                }
            default: 
                {
                	System.out.println("Escolha uma opção válida.");
                    break;
                }
            }

		}


		/*
		RPCServiceClient serviceClient = new RPCServiceClient();
		Options options = serviceClient.getOptions();
		EndpointReference targetEPR = new EndpointReference("http://localhost:8080/axis2/services/RepositoryService");
		options.setTo(targetEPR);

		//Upar uma palavraqq
		QName opPushWord = new QName("http://repo.words", "pushWord");

		Object[] opPushWordArgs = new Object[] { "TESTE" };
		serviceClient.invokeRobust(opPushWord, opPushWordArgs);
		*/


	}
}

