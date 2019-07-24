package fd.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import fd.models.Document;
import fd.models.User;
import fd.services.DocumentServices;
import io.github.cdimascio.dotenv.Dotenv;

@ManagedBean
@ApplicationScoped
public class DocumentControllers {
	@ManagedProperty(value = "#{document}")
	Document document;
	@ManagedProperty(value = "#{user}")
	User user;

	static DocumentServices documentServices = new DocumentServices();
	static String fileName;
	static String altName;
	static InputStream fileStream;

	private String documentType;
	private Map<String, String> documentTypes = new HashMap<String, String>();

	@PostConstruct
	public void init() {
		documentTypes = new HashMap<String, String>();
		List<String> allTypes = new ArrayList<String>();
		try {
			allTypes = documentServices.getAllDocumentTypes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (String type : allTypes) {
			documentTypes.put(type, type);
		}
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Map<String, String> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(Map<String, String> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void resetValue() {
		document.setName(null);
		document.setType(null);
		document.setFileName(null);
	}

	public void upload(FileUploadEvent event) throws IOException {
		document.setType(documentType);
		document.setFileName(event.getFile().getFileName());
		FacesMessage msg = new FacesMessage("Success !", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage("messages", msg);
		fileName = event.getFile().getFileName();
		fileStream = event.getFile().getInputstream();
		altName = fileName;
	}

	public void copyFile(String fileName, InputStream in) throws Exception {
		Dotenv dotenv = Dotenv.configure().directory("E:\\Git\\demo-primefaces\\faces-demo").ignoreIfMalformed()
				.ignoreIfMissing().load();
		String destination = dotenv.get("UPLOAD_DIR");
		int exFiles = documentServices.existedFiles(fileName.substring(0, fileName.lastIndexOf(".")));
		if (exFiles > 0) {
			fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_v" + exFiles + ".pdf";
			altName = fileName;
		}
		try {
			OutputStream out = new FileOutputStream(new File(destination + fileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			in.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String submitUpload() throws Exception {
		try {
			copyFile(fileName, fileStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (document.getName().isEmpty()) {
			documentServices.uploadNewDocument(document.getFileName(), document.getType(), user.getId(),
					altName);
		} else {
			documentServices.uploadNewDocument(document.getName(), document.getType(), user.getId(),
					altName);
		}
		documentTypes.put(document.getType(), document.getType());
		document.setName(null);
		document.setFileName(null);
		return "index" + "?faces-redirect=true";

	}
}
