package ws;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.Disciplina;
import entities.Evento;
import flexjson.JSONSerializer;

/**
 * Servlet implementation class GetEventosDisciplina
 */
public class GetEventosDisciplina extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetEventosDisciplina() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		String nombre = request.getParameter("nombre");
		MensajeApp respuesta = null;
		Disciplina dRes = null;
		List<Evento> eventosDisciplina = new ArrayList<Evento>();
		Evento evAux = null;
		boolean respondido = false;
		//Primero obtener idDisciplina
		//SELECT Disciplina.idDisciplina FROM sql11208838.Disciplina WHERE Disciplina.Nombre='Atletismo';
		if(nombre!=null) {
			try {
				Class.forName(ServerInfo.JDBC_DRIVER);
				//Open a connection
				conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
				//Execute a query
				String sql = "SELECT Disciplina.idDisciplina FROM "+ServerInfo.DATABASE+".Disciplina WHERE Disciplina.Nombre='"+nombre+"';";
				PreparedStatement pS = conn.prepareStatement(sql);
				ResultSet rS = pS.executeQuery(sql);
				while(rS.next()) {
					dRes = new Disciplina();
					Integer idDisciplina = rS.getInt("idDisciplina");
					if(idDisciplina!=null) {
						dRes.setIdDisciplina(idDisciplina);
					}
				}
				rS.close();
				conn.close();
				if(dRes!=null) {
					conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
					//Execute a query
					String sql2 = "SELECT * FROM "+ServerInfo.DATABASE+".Evento WHERE Evento.Disciplina_idDisciplina='"+dRes.getIdDisciplina()+"'order by Fecha ASC;";
					PreparedStatement pS2 = conn.prepareStatement(sql2);
					ResultSet rS2 = pS2.executeQuery(sql2);
					while(rS2.next()) {
						evAux = new Evento();
						evAux.setDisciplina(dRes);
						evAux.setFecha(rS2.getDate("Fecha"));
						evAux.setHora(rS2.getDate("Hora"));
						evAux.setIdEvento(rS2.getInt("idEvento"));
						evAux.setLastModification(rS2.getDate("LastModification"));
						evAux.setLugar(rS2.getString("Lugar"));
						evAux.setResultados(rS2.getString("Resultados"));
						evAux.setTipo(rS2.getString("Tipo"));
						eventosDisciplina.add(evAux);
					}
					rS2.close();
					conn.close();					
				}else {
					respondido = true;
					respuesta = new MensajeApp("error","nodiscipline");
					response.setContentType("application/json");
					response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
				}				
			} catch (ClassNotFoundException e) {
				respuesta = new MensajeApp("error","ClassNotFoundException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			} catch (SQLException e) {
				respuesta = new MensajeApp("error","SQLException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
			if(!respondido) {
				if(!eventosDisciplina.isEmpty()) {
					response.setContentType("application/json");
					response.getWriter().print(new JSONSerializer().exclude("class").serialize(eventosDisciplina));
				}else {
					respuesta = new MensajeApp("error","noevents");
					response.setContentType("application/json");
					response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
				}
			}
		}else {
			respuesta = new MensajeApp("error","missing");
			response.setContentType("application/json");
			response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
		}
	}
}
