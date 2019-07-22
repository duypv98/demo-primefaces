package fd.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import fd.models.User;
import fd.services.UserServices;

@ManagedBean
@RequestScoped
public class UserControllers {
	
	static UserServices userServices = new UserServices();
	
	@ManagedProperty(value = "#{user}")
	User user;
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String register() throws Exception {
		if (userServices.register(user.getUsername(), user.getPassword())) {
			return "login" + "?faces-redirect=true";
		} else {
			return "register" + "?faces-redirect=true";
		}
	}
	
	public String login() throws Exception {
		if (userServices.login(user.getUsername(), user.getPassword()) != null) {
			user.setLoggedIn(true);
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
}
