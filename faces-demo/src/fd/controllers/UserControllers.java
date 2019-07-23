package fd.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import fd.models.Document;
import fd.models.User;
import fd.services.UserServices;

@ManagedBean
@RequestScoped
public class UserControllers {

	@ManagedProperty(value = "#{user}")
	User user;
	@ManagedProperty(value = "#{document}")
	Document document;
	static UserServices userServices = new UserServices();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String register() throws Exception {
		if (userServices.register(user.getUsername(), user.getPassword())) {
			return "login" + "?faces-redirect=true";
		} else {
			return "register" + "?faces-redirect=true";
		}
	}

	public String login() throws Exception {
		User u = userServices.login(user.getUsername(), user.getPassword());
		if (u != null) {
			user.setLoggedIn(true);
			user.setUsername(u.getUsername());
			user.setId(u.getId());
			document.setUserID(u.getId());
			return "index" + "?faces-redirect=true";
		} else {
			return "login" + "?faces-redirect=true";
		}
	}

	public String logout() {
		user.setId(0);
		user.setUsername(null);
		user.setPassword(null);
		user.setLoggedIn(false);
		return "index" + "?faces-redirect=true";
	}
	
	public String getUsernameById(int id) throws Exception {
		String res = userServices.getUsernameById(id);
		return res;
	}
}
