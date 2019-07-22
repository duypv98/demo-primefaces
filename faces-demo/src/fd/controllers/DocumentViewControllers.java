package fd.controllers;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import fd.models.Document;
import fd.models.User;
import fd.services.DocumentServices;

@ManagedBean
@RequestScoped
public class DocumentViewControllers {
	@ManagedProperty(value = "#{user}")
	User user;

	static DocumentServices documentServices = new DocumentServices();

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

	public boolean userOp(Document document) {
		return (document.getUserID() == user.getId());
	}
}
