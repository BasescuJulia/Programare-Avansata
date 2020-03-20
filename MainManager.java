package manager;

import entities.Catalog;
import entities.Document;
import entities.ReportType;
import exceptions.EmptyCommandException;
import exceptions.InvalidCommandException;
import exceptions.InvalidPathException;
import freemarker.template.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.tika.Tika;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class MainManager {

	public static String save(String filePath, Catalog catalog) {
		try {
			validateFilePath(filePath);

			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(catalog);

			objectOut.close();
			fileOut.close();
		} catch (InvalidPathException ex) {
			ex.printStackTrace();
			return "The specified file path is invalid: " + filePath;
		} catch (IOException e) {
			e.printStackTrace();
			return "Error on trying to save catalog: " + e.getMessage();
		}
		return "The catalog was successfully written to: " + filePath;
	}

	private static void validateFilePath(String filePath) throws InvalidPathException {
		if (filePath == null || filePath.isEmpty()) {
			throw new InvalidPathException("File path cannot be empty!");
		}
	}

	public static void saveText(String filePath, Catalog catalog) {

	}

	public static Catalog load(String filePath) {
		Catalog catalog = null;
		try {
			validateFilePath(filePath);
			FileInputStream fileIn = new FileInputStream(new File(filePath));
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			catalog = (Catalog) objectIn.readObject();

			objectIn.close();
			fileIn.close();

			return catalog;
		} catch (InvalidPathException ex) {
			ex.printStackTrace();
			return null;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return catalog;
	}

	public static Catalog loadText(String filePath) {
		return null;
	}

	public static String view(String filePath) {
		try {
			validateFilePath(filePath);
			Desktop desktop = Desktop.getDesktop();
			desktop.open(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
			return "Error launching app: " + e.getMessage();
		} catch (InvalidPathException e) {
			e.printStackTrace();
			return "The specified file path is invalid: " + filePath;
		}
		return "App launched successfully!\n";
	}

	public static void reportHtml() throws IOException, TemplateException {
		// 1. Configure FreeMarker
		Configuration cfg = new Configuration();

		// Where do we load the templates from:
		cfg.setClassForTemplateLoading(MainManager.class, "templates");

		// Some other recommended settings:
		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// 2. Proccess template(s)
		// 2.1. Prepare the template input:
		Map<String, Object> input = new HashMap<>();
		input.put("title", "Catalog");
		input.put("catalog", Catalog.getInstance());

		// 2.2. Get the template
		Template template = cfg.getTemplate("helloworld.ftl");

		// 2.3. Generate the output
		// Write output to the console
		Writer consoleWriter = new OutputStreamWriter(System.out);
		template.process(input, consoleWriter);

		// Write output into the file:
		try (Writer fileWriter = new FileWriter(new File("output.html"))) {
			template.process(input, fileWriter);
		}
	}

	public static String info(String filePath) {
		try {
			validateFilePath(filePath);
			Tika tika = new Tika();
			return tika.detect(filePath);
		} catch (InvalidPathException e) {
			e.printStackTrace();
			return "The specified file path is invalid: " + filePath;
		}
	}

	public static void report(ReportType reportType) {
		switch (reportType) {
			case HTML:
				try {
					reportHtml();
				} catch (IOException | TemplateException e) {
					System.out.print("Error trying to generate HTML report: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case PDF:

				break;
			case WORD:
				try {
					HWPFDocument document = new HWPFDocument(new FileInputStream("Report doc.doc"));
					FileOutputStream fileOut = new FileOutputStream("Report doc.doc");
					ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
					objectOut.writeObject(Catalog.getInstance());
					document.write();

					objectOut.close();
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
	}

	public static void main(String[] args) {
		String command = null;

		while (!"exit".equals(command)) {
			initExampleDocuments();
// create a scanner so we can read the command-line input
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter the command and the path: ");
			command = scanner.next();
			String path = scanner.next();

			try {
				validateCommand(command);

				switch (command) {
					case "save":
						System.out.println(save(path, Catalog.getInstance()));
						break;
					case "saveText":
						saveText(path, Catalog.getInstance());
						break;
					case "load":
						Catalog catalog = load(path);
						if (catalog != null) {
							System.out.println(catalog.toPrint());
						}
						break;
					case "loadText":
						Catalog catalogText = loadText(path);
						if (catalogText != null) {
							System.out.println(catalogText.toPrint());
						}
						break;
					case "view":
						System.out.println(view(path));
						break;
					case "reportHtml":
						reportHtml();
						break;
					case "report":
						report(ReportType.valueOf(path));
						break;
					case "info":
						System.out.println(info(path));
						break;
					case "exit":
						System.out.println("Exiting app.");
						break;
					default:
						break;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private static void initExampleDocuments() {
		Document doc1 = new Document();
		doc1.setID("doc_" + System.currentTimeMillis());
		doc1.setName("Exemplu 1");
		doc1.setPath("exemplu 1.txt");
		doc1.setType(Document.DocumentType.ARTICLE);

		Document doc2 = new Document();
		doc2.setID("doc_" + System.currentTimeMillis());
		doc2.setName("Exemplu 2");
		doc2.setPath("exemplu 2.doc");
		HashMap<String, String> tags2 = new HashMap<>();
		tags2.put("Titlu", "Eseu de test");
		tags2.put("Autor", "Julia B.");
		doc2.setTags(tags2);
		doc2.setType(Document.DocumentType.ESSAY);

		Catalog catalog = Catalog.getInstance();
		catalog.setID("cat_" + System.currentTimeMillis());
		List<Document> documents = new LinkedList<>();
		documents.add(doc1);
		documents.add(doc2);
		catalog.setDocuments(documents);
	}

	private static void validateCommand(String command) throws EmptyCommandException, InvalidCommandException {
		if (command == null || command.trim().isEmpty()) {
			throw new EmptyCommandException("Command cannot be empty!");
		}
		List<String> allowedCommands = Arrays.asList("save", "saveText", "load", "loadText", "view", "reportHtml", "report", "info", "exit");
		if (!allowedCommands.contains(command)) {
			throw new InvalidCommandException("Invalid command!");
		}
	}
}
