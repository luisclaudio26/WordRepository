package words.client;

import java.rmi.*;
import java.util.*;
import words.search.ISearchGateway;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.net.*;

public class Client
{
	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			System.err.println("Please input gateway address and client operating port.");
			return;
		}

		try {

			String address = args[0];
			int port = Integer.parseInt(args[1]);
			Scanner in = new Scanner(System.in);
			
			Registry reg = LocateRegistry.getRegistry(address, 1099,
							new RMIClientSocketFactory() {
								public Socket createSocket(String host, int port) {
									Socket s = null;

									try {
										s = new Socket(host, port);
									} catch(Exception e) {
										System.err.println("ERROR WHILE OPENING CLIENT SOCKET:\n" + e.getMessage());
										e.printStackTrace();
									}

									return s;
								}
							});


			ISearchGateway gateway = (ISearchGateway)reg.lookup("query");

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