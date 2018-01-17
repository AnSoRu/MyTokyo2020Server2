package entities;

import java.sql.Date;

public class UsuarioCompraEvento {
	
	private String usuario;
	private Integer evento;
	private Float precio;
	private Date fecha_de_compra;
	private Date lastModification;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public Integer getEvento() {
		return evento;
	}
	public void setEvento(Integer evento) {
		this.evento = evento;
	}
	public Float getPrecio() {
		return precio;
	}
	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	public Date getFecha_de_compra() {
		return fecha_de_compra;
	}
	public void setFecha_de_compra(Date fecha_de_compra) {
		this.fecha_de_compra = fecha_de_compra;
	}
	public Date getLastModification() {
		return lastModification;
	}
	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}
		
}
