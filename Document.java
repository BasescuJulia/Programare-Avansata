package entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Document implements Serializable {
	private static final long serialVersionUID = 2L;

	private String ID;
	private DocumentType type;
	private String name;
	private String path;
	private HashMap<String, String> tags;

	public enum DocumentType {
		ARTICLE, BOOK, ESSAY;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(HashMap<String, String> tags) {
		this.tags = tags;
	}

	public String toPrint() {
		final List<String> tagsString = new LinkedList<>();
		if (tags != null) {
			this.tags.forEach((key, value) -> tagsString.add(key + " - " + value));
		}
		return "ID: " + this.ID +
				"\nType: " + this.type.toString() +
				"\nName: " + this.name +
				"\nPath: " + this.path +
				"\nTags: " + String.join(";", tagsString);
	}
}
