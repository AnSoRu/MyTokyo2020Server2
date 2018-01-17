package ws;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.Compra;
import entities.Disciplina;
import entities.Evento;
import entities.UsuarioCompraEvento;
import flexjson.JSONSerializer;

/**
 * Servlet implementation class GetShoppingHistory
 */
public class GetShoppingHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetShoppingHistory() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		MensajeApp respuesta = null;
		List<Compra> eventosUser = new ArrayList<Compra>();
		UsuarioCompraEvento uCE = null;
		Integer eventId = null;
		Evento ev = null;
		Disciplina disp = null;
		if(email==null) {
			respuesta = new MensajeApp("error","missing");
			response.setContentType("application/json");
			response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
		}else {
			Connection conn = null;
			try {
				Class.forName(ServerInfo.JDBC_DRIVER);
				//Open a connection
				conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
				//Execute a query
				//Existe el evento?
				String sql;
				sql ="SELECT * FROM "+ServerInfo.DATABASE+".Usuario_compra_Evento WHERE Usuario_Email='"+email+"' order by Fecha_de_Compra ASC;";
				PreparedStatement pS = conn.prepareStatement(sql);
				ResultSet rS = pS.executeQuery(sql);
				while(rS.next()) {
					//Primero buscar el evento
					eventId = rS.getInt("Evento_idEvento");
					Float precio = rS.getFloat("Precio");
					Date fechaCompra = rS.getDate("Fecha_de_Compra");
					Date lastModification = rS.getDate("LastModification");
					String sql2 = "SELECT * FROM "+ServerInfo.DATABASE+".Evento WHERE idEvento='"+eventId+"'";
					PreparedStatement pS2 = conn.prepareStatement(sql2);
					ResultSet rS2 = pS2.executeQuery(sql2);
					while(rS2.next()) {
						Integer idDisciplina = rS2.getInt("Disciplina_idDisciplina");
						String sql3 = "SELECT * FROM "+ServerInfo.DATABASE+".Disciplina WHERE idDisciplina='"+idDisciplina+"'";
						PreparedStatement pS3 = conn.prepareStatement(sql3);
						ResultSet rS3 = pS3.executeQuery(sql3);
						while(rS3.next()) {
							disp = new Disciplina();
							disp.setDescripcion(rS3.getString("Descripcion"));
							disp.setNombre(rS3.getString("Nombre"));
							disp.setLastModification(rS3.getDate("LastModification"));
						}
						rS3.close();
						ev = new Evento();
						ev.setIdEvento(rS2.getInt("idEvento"));
						ev.setLugar(rS2.getString("Lugar"));
						ev.setFecha(rS2.getDate("Fecha"));
						ev.setHora(rS2.getDate("Hora"));
						ev.setTipo(rS2.getString("Tipo"));
						ev.setDisciplina(disp);
						ev.setResultados(rS2.getString("Resultados"));
						ev.setLastModification(rS2.getDate("LastModification"));
					}
					rS2.close();
					uCE = new UsuarioCompraEvento();
					uCE.setEvento(eventId);
					uCE.setPrecio(precio);
					uCE.setFecha_de_compra(fechaCompra);
					uCE.setLastModification(lastModification);
					uCE.setEvento(ev.getIdEvento());
					Compra cAux = new Compra(uCE, ev);
					eventosUser.add(cAux);
				}
				rS.close();
				conn.close();
			} catch (ClassNotFoundException e) {
				respuesta = new MensajeApp("error","ClassNotFoundException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			} catch (SQLException e) {
				respuesta = new MensajeApp("error","SQLException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
			if(!eventosUser.isEmpty()) {
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(eventosUser));
			}else {
				respuesta = new MensajeApp("error","noEvents");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
		}
	}
}
