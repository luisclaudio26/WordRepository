package words.repo;

import java.util.Set;
import java.util.TreeSet;

public class Repository {

	private Set<String> words;

	public Repository() {
		words = new TreeSet<String>();
	}

	//-----------------------
	//------ Interface ------
	//-----------------------
	public void pushWord(String w) {
		System.out.print("[Repository] Pushing word '" + w + "'");
		words.add(w);
	}

	public void removeWord(String w) {
		System.out.print("[Repository] Removing word '" + w + "'");
		words.remove(w);
	}

	public boolean hasWord(String w) {
		System.out.println("[Repository] Looking for word '" + w + "'");
		return words.contains(w);
	}
}

