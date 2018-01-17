package entities;

import java.util.Date;

public class Disciplina {

	private int idDisciplina;
	private String nombre;
	private String descripcion;
	private Date lastModification;
	
	public Disciplina(int idDisciplina, String nombre, String descripcion, Date lastModification) {
		super();
		this.idDisciplina = idDisciplina;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.lastModification = lastModification;
	}
	
	public Disciplina() {
		
	}

	public int getIdDisciplina() {
		return idDisciplina;
	}

	public void setIdDisciplina(int idDisciplina) {
		this.idDisciplina = idDisciplina;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}
}
