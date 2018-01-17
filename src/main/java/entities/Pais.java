package entities;

import java.util.Date;

public class Pais {
	
	private int idPais;
	private String nombre;
	private Integer medallasOro;
	private Integer medallasPlata;
	private Integer medallasBronce;
	private Integer posRanking;
	private Date lastModificaton;
	
	
	public Pais(int idPais, String nombre, Integer medallasOro, Integer medallasPlata, Integer medallasBronce,
			Integer posRanking, Date lastModificaton) {
		super();
		this.idPais = idPais;
		this.nombre = nombre;
		this.medallasOro = medallasOro;
		this.medallasPlata = medallasPlata;
		this.medallasBronce = medallasBronce;
		this.posRanking = posRanking;
		this.lastModificaton = lastModificaton;
	}
	
	public Pais() {
		
	}


	public int getIdPais() {
		return idPais;
	}


	public void setIdPais(int idPais) {
		this.idPais = idPais;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
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


	public Integer getPosRanking() {
		return posRanking;
	}


	public void setPosRanking(Integer posRanking) {
		this.posRanking = posRanking;
	}


	public Date getLastModificaton() {
		return lastModificaton;
	}


	public void setLastModificaton(Date lastModificaton) {
		this.lastModificaton = lastModificaton;
	}
}