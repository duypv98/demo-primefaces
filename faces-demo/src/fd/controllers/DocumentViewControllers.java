package fd.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fd.models.Document;
import fd.models.User;
import fd.services.DocumentServices;

@ManagedBean
@ApplicationScoped
public class DocumentViewControllers {
	@ManagedProperty(value = "#{user}")
	User user;
	
	private StreamedContent originFile;

	static DocumentServices documentServices = new DocumentServices();

	public List<Document> getAllDocuments() throws Exception {
		List<Document> documents = documentServices.getAllDocuments();
		return documents;
	}
	private String test;
	
	
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public StreamedContent getOriginFile() {
		return originFile;
	}

	public void setOriginFile(StreamedContent originFile) {
		this.originFile = originFile;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean userOp(Document document) {
		return (document.getUserID() == user.getId());
	}
	
	public String viewDocument(int id) throws Exception {
		Document refDoc = documentServices.getDocumentByID(id);
		String fileName = refDoc.getFileName();
		String base = "E:\\Git\\demo-primefaces\\faces-demo\\samples\\";
		File file = new File(base + fileName);
		DefaultStreamedContent sc = new DefaultStreamedContent(new FileInputStream(file), "application/pdf",
				fileName.substring(0, fileName.lastIndexOf(".")));
		this.test = fileName;
		this.originFile = sc;
		return "view";
	}
	
	public String removeDocument(int id) throws Exception {
		documentServices.deleteDocumentByID(id);
		return "index" + "?faces-redirect=true";
	}
}
