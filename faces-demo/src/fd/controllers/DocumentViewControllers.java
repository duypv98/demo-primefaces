package fd.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.util.List;

import javax.annotation.PostConstruct;
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
	@ManagedProperty(value = "#{documentControllers}")
	DocumentControllers documentControllers;
	
	static Dotenv dotenv = Dotenv.configure().directory("E:\\Git\\demo-primefaces\\faces-demo").ignoreIfMalformed()
			.ignoreIfMissing().load();

	private StreamedContent originFile;

	private String contentTitle = "All Documents";
	private String searchString;
	private String searchType;
	private java.util.Date dateFrom;
	private java.util.Date dateTo;
	private List<Document> allDocuments;
	static DocumentServices documentServices = new DocumentServices();

	@PostConstruct
	public void init() {
		try {
			allDocuments = documentServices.getAllDocuments();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DocumentControllers getDocumentControllers() {
		return documentControllers;
	}

	public void setDocumentControllers(DocumentControllers documentControllers) {
		this.documentControllers = documentControllers;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public List<Document> getAllDocuments() {
		return allDocuments;
	}

	public void setAllDocuments(List<Document> allDocuments) {
		this.allDocuments = allDocuments;
	}

	public void getDocumentsBySearch() throws Exception {
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
		allDocuments.clear();
		contentTitle = "Search Result";
		allDocuments = documentServices.getDocumentsBySearch(searchString, searchType, dFrom, dTo);
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
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

	public void removeDocument(int id) throws Exception {
		File file = new File(dotenv.get("ASSETS_DIR") + documentServices.getDocumentByID(id).getFileName());
		file.delete();
		documentServices.deleteDocumentByID(id);
		allDocuments.clear();
		allDocuments = documentServices.getAllDocuments();
	}

	public void showAllDocuments() throws Exception {
		searchString = null;
		searchType = null;
		dateFrom = null;
		dateTo = null;
		contentTitle = "All Documents";
		allDocuments.clear();
		allDocuments = documentServices.getAllDocuments();
	}
	public void uploadDocument() throws Exception {
		documentControllers.submitUpload();
		allDocuments.clear();
		allDocuments = documentServices.getAllDocuments();
	}
	
	public void editDocument() throws Exception {
		documentControllers.submitEdit();
		allDocuments.clear();
		allDocuments = documentServices.getAllDocuments();
	}
}
