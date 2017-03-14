package words.search;

import words.repo.IRepository;
import java.rmi.*;

public class SearchGateway {
	public static void main(String[] args) {
		try {
			IRepository wordRepo = (IRepository)Naming.lookup("//localhost/WordRep");

			wordRepo.pushWord("HELLO");

			if( wordRepo.hasWord("HELLO") )
				System.out.println("IT'S ALIVE!");

		} catch(Exception e) {
			System.err.println("SEARCH GATEWAY ERROR:\n" + e.getMessage());
			e.printStackTrace();
		}
	}
}