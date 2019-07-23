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

	public void upload(FileUploadEvent event) {
		document.setType(documentType);
		String altName = document.getName();
		String fileName = event.getFile().getFileName().trim();
		if (altName == "")
			document.setName(fileName);
		document.setFileName(fileName);
		FacesMessage msg = new FacesMessage("Success !", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage("messages", msg);
		try {
			copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void copyFile(String fileName, InputStream in) {
		Dotenv dotenv = Dotenv.configure().directory("E:\\Git\\demo-primefaces\\faces-demo").ignoreIfMalformed()
				.ignoreIfMissing().load();
		String destination = dotenv.get("UPLOAD_DIR");
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
		documentServices.uploadNewDocument(document.getName(), document.getType(), user.getId(),
				document.getFileName());
		return "index" + "?faces-redirect=true";
	}
}
