package fd.controllers;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.StreamedContent;

import fd.models.Document;
import fd.services.DocumentServices;

@ManagedBean
@ApplicationScoped
public class DocumentControllers {
	@ManagedProperty(value = "#{document}")
	Document document;

	static DocumentServices documentServices = new DocumentServices();
	private StreamedContent originFile;

	public StreamedContent getOriginFile() {
		return originFile;
	}

	public void setOriginFile(StreamedContent originFile) {
		this.originFile = originFile;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
