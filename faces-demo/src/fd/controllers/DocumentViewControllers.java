package fd.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fd.models.Document;
import fd.models.User;
import fd.services.DocumentServices;
import io.github.cdimascio.dotenv.Dotenv;

@ManagedBean
@ApplicationScoped
public class DocumentViewControllers {
	@ManagedProperty(value = "#{user}")
	User user;

	static Dotenv dotenv = Dotenv.configure().directory("E:\\Git\\demo-primefaces\\faces-demo").ignoreIfMalformed()
			.ignoreIfMissing().load();

	private StreamedContent originFile;

	private String searchString;
	private String searchCondition = "name";
	private java.util.Date dateFrom;
	private java.util.Date dateTo;
	static DocumentServices documentServices = new DocumentServices();

	public List<Document> getAllDocuments() throws Exception {
		List<Document> documents = documentServices.getAllDocuments();
		return documents;
	}
	
	public List<Document> getDocumentsBySearch() throws Exception {
		List<Document> documents = null;
		Date dFrom;
		Date dTo;
		if (dateFrom != null) {
			dFrom = new Date(dateFrom.getTime());
		} else {
			dFrom = null;
		}
		if (dateTo != null) {
			dTo = new Date(dateTo.getTime());
		} else {
			dTo = null;
		}
		if (searchCondition.equals("name")) {
			
			documents = documentServices.getDocumentsByName(searchString, dFrom, dTo);
		}
		if (searchCondition.equals("type")) {
			documents = documentServices.getDocumentsByType(searchString, dFrom, dTo);
		}
		return documents;
	}
	

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public java.util.Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(java.util.Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public java.util.Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(java.util.Date dateTo) {
		this.dateTo = dateTo;
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
		String base = dotenv.get("ASSETS_DIR").trim();
		File file = new File(base + fileName);
		DefaultStreamedContent sc = new DefaultStreamedContent(new FileInputStream(file), "application/pdf",
				fileName.substring(0, fileName.lastIndexOf(".")));
		this.originFile = sc;
		return "view";
	}
	
	public String removeDocument(int id) throws Exception {
		File file = new File(dotenv.get("ASSETS_DIR") + documentServices.getDocumentByID(id).getFileName());
		file.delete();
		documentServices.deleteDocumentByID(id);
		return "index" + "?faces-redirect=true";
	}
	
	public String searchDocument() {
		return "search";
	}
	
	public String returnIndexFromSearch() {
		setSearchString(null);
		setSearchCondition(null);
		setDateFrom(null);
		setDateTo(null);
		return "index" + "?faces-redirect=true";
	}
}
