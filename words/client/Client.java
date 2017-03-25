package words.client;

import java.rmi.*;
import java.util.*;
import words.search.ISearchGateway;

public class Client
{
	public static void main(String[] args)
	{
		try {

			Scanner in = new Scanner(System.in);
			ISearchGateway gateway = (ISearchGateway)Naming.lookup("//localhost/query");

			while(true)
			{
				int opt; String word;

				System.out.print("Word: "); 
				word = in.next();

				System.out.println("1. Push word");
				System.out.println("2. Lookup word");
				System.out.print("> "); opt = in.nextInt();

				switch(opt)
				{
				case 1:
					{
						gateway.register(word);
						break;
					}
				case 2:
					{
						List<String> out = gateway.search(word);
						System.out.print("In servers: ");
						for(String s : out)	System.out.print(s + "  ");
						break;
					}
				default: 
					{ 
						System.out.println("No such option"); 
						break;	
					}
				}
			}

		} catch(Exception e) {
			System.out.println("Error in the client thread.\n" + e.getMessage());
			e.printStackTrace();
		}
	}
}