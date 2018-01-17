package entities;

public class Compra {
	
	private UsuarioCompraEvento uCE;
	private Evento ev;
	
	public Compra(UsuarioCompraEvento uCE, Evento ev) {
		super();
		this.uCE = uCE;
		this.ev = ev;
	}

	public UsuarioCompraEvento getuCE() {
		return uCE;
	}

	public void setuCE(UsuarioCompraEvento uCE) {
		this.uCE = uCE;
	}

	public Evento getEv() {
		return ev;
	}

	public void setEv(Evento ev) {
		this.ev = ev;
	}
}
