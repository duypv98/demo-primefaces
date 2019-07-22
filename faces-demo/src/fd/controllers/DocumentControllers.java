package fd.controllers;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import fd.models.Document;
import fd.models.User;
import fd.services.DocumentServices;

@ManagedBean
@ApplicationScoped
public class DocumentControllers {
	@ManagedProperty(value = "#{user}")
	User user;
	
	
	private int documentID;
	static DocumentServices documentServices = new DocumentServices();
	private StreamedContent originFile;
	private UploadedFile file;

	public StreamedContent getOriginFile() {
		return originFile;
	}

	public void setOriginFile(StreamedContent originFile) {
		this.originFile = originFile;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public List<Document> getAllDocuments() throws Exception {
		List<Document> documents = documentServices.getAllDocuments();
		return documents;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getDocumentID() {
		return documentID;
	}

	public boolean userOp(Document document) {
		return (document.getUserID() == user.getId());
	}

	public String removeDocument() throws Exception {
		documentServices.deleteDocumentById(documentID);
		return "index" + "?faces-redirect=true";
	}
}
