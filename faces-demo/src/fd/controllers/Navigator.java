package fd.controllers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class Navigator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4367809453523922642L;

	public String login() {
		return "login" + "?faces-redirect=true";
	}
	
	public String register() {
		return "login" + "?faces-redirect=true";
	}
	
	public String home() {
		return "index" + "?faces-redirect=true";
	}
	
	public String userInfo() {
		return "info" + "?faces-redirect=true";
	}
}
