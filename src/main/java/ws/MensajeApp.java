package ws;

public class MensajeApp {
	
	private String tipo;
	private String msg;
	
	public MensajeApp(String tipo, String msg) {
		super();
		this.tipo = tipo;
		this.msg = msg;
	}
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
