package entities;

import java.util.Date;

public class Deportista {
	
	private int idDeportista;
	private Pais pais;
	private String nombre;
	private Integer edad;
	private Date lastModification;
	private Integer medallasOro;
	private Integer medallasPlata;
	private Integer medallasBronce;
	
		
	public Deportista(int idDeportista, Pais pais, String nombre, Integer edad, Date lastModification,
			Integer medallasOro, Integer medallasPlata, Integer medallasBronce) {
		super();
		this.idDeportista = idDeportista;
		this.pais = pais;
		this.nombre = nombre;
		this.edad = edad;
		this.lastModification = lastModification;
		this.medallasOro = medallasOro;
		this.medallasPlata = medallasPlata;
		this.medallasBronce = medallasBronce;
	}
	
	
	public Deportista() {
	}


	public int getIdDeportista() {
		return idDeportista;
	}
	public void setIdDeportista(int idDeportista) {
		this.idDeportista = idDeportista;
	}
	public Pais getPais() {
		return pais;
	}
	public void setPais(Pais pais) {
		this.pais = pais;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	public Date getLastModification() {
		return lastModification;
	}
	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}
	public Integer getMedallasOro() {
		return medallasOro;
	}
	public void setMedallasOro(Integer medallasOro) {
		this.medallasOro = medallasOro;
	}
	public Integer getMedallasPlata() {
		return medallasPlata;
	}
	public void setMedallasPlata(Integer medallasPlata) {
		this.medallasPlata = medallasPlata;
	}
	public Integer getMedallasBronce() {
		return medallasBronce;
	}
	public void setMedallasBronce(Integer medallasBronce) {
		this.medallasBronce = medallasBronce;
	}
	
	

}
