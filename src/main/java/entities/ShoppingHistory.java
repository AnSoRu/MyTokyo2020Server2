package entities;

import java.util.List;

public class ShoppingHistory {

	List<Compra> historial;
	
	public ShoppingHistory() {
		
	}

	public List<Compra> getHistorial() {
		return historial;
	}

	public void setHistorial(List<Compra> historial) {
		this.historial = historial;
	}
	
}
