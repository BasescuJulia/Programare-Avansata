package entities;

import java.io.Serializable;
import java.util.List;

public class Catalog implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ID;
	private List<Document> documents;
	private static Catalog instance;

	private Catalog() {
	}

	public static Catalog getInstance(){
		if(instance == null){
			instance = new Catalog();
		}
		return instance;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public String toPrint() {
		StringBuilder documentString = new StringBuilder("");
		for (Document document : documents) {
			documentString.append(document.toPrint()).append(";\n");
		}

		return "Catalog{" +
				"ID='" + ID + '\'' +
				", documents=" + documentString +
				'}';
	}
}
