package entities;

import java.util.Date;

public class Evento {
	
	private int idEvento;
	private Disciplina disciplina;
	private String lugar;
	private Date fecha;
	private Date hora;
	private String tipo;
	private String resultados;
	private Date lastModification;
	
	
	public Evento(int idEvento, Disciplina disciplina, String lugar, Date fecha, Date hora, String tipo,
			String resultados, Date lastModification) {
		super();
		this.idEvento = idEvento;
		this.disciplina = disciplina;
		this.lugar = lugar;
		this.fecha = fecha;
		this.hora = hora;
		this.tipo = tipo;
		this.resultados = resultados;
		this.lastModification = lastModification;
	}
	
	public Evento() {
		
	}


	public int getIdEvento() {
		return idEvento;
	}


	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento;
	}


	public Disciplina getDisciplina() {
		return disciplina;
	}


	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}


	public String getLugar() {
		return lugar;
	}


	public void setLugar(String lugar) {
		this.lugar = lugar;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public Date getHora() {
		return hora;
	}


	public void setHora(Date hora) {
		this.hora = hora;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getResultados() {
		return resultados;
	}


	public void setResultados(String resultados) {
		this.resultados = resultados;
	}


	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

}
