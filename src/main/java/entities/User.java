package entities;

import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;

public class User {
	
	private String email;
	private String username;
	private Integer edad;
	private String password;
	private Date lastModification;
	//private Set<UsuarioCompraEvento> usuarioCompraEventos = new HashSet<UsuarioCompraEvento>(0);

	public User() {
	}

	public User(String email, String username, String password, Date lastModification) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.lastModification = lastModification;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getEdad() {
		return this.edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastModification() {
		return this.lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

	/*@OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario")
	public Set<UsuarioCompraEvento> getUsuarioCompraEventos() {
		return this.usuarioCompraEventos;
	}

	public void setUsuarioCompraEventos(Set<UsuarioCompraEvento> usuarioCompraEventos) {
		this.usuarioCompraEventos = usuarioCompraEventos;
	}*/

}
