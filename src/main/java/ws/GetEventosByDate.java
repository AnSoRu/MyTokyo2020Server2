package ws;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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
 * Servlet implementation class GetEventosByDate
 */
public class GetEventosByDate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetEventosByDate() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		String fecha = request.getParameter("date");
		Evento event = null;
		Disciplina disciplina = null;
		List<Evento> lRes = new ArrayList<Evento>();
		MensajeApp respuesta = null;
		boolean buscarDisciplina = false;
		Integer idDisciplinaFuera = null;
		boolean respondido = false;
		if(fecha == null) {
			respuesta = new MensajeApp("error","missing");
			response.setContentType("application/json");
			response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
		}else {
			//Open a connection
			try {
				if(fecha.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
					Class.forName(ServerInfo.JDBC_DRIVER);
					conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);

					//Execute a query
					String sql;
					sql = "SELECT * FROM "+ServerInfo.DATABASE+".Evento WHERE Evento.Fecha='"+fecha+"'";
					PreparedStatement pS = conn.prepareStatement(sql);
					ResultSet rS = pS.executeQuery(sql);
					while(rS.next()) {
						event = new Evento();
						Integer idEvento = rS.getInt("idEvento");
						String lugar = rS.getString("Lugar");
						Date fecha2 = rS.getDate("Fecha");
						Date hora = rS.getDate("Hora");
						String tipo = rS.getString("Tipo");
						String resultados = rS.getString("Resultados");
						Integer idDisciplina = rS.getInt("Disciplina_idDisciplina");
						Date d = rS.getDate("LastModification");
						if(idEvento!=null) {
							event.setIdEvento(idEvento);
						}if(lugar!=null) {
							event.setLugar(lugar);
						}if(fecha!=null) {
							event.setFecha(fecha2);
						}if(hora!=null) {
							event.setHora(hora);
						}if(tipo!=null) {
							event.setTipo(tipo);
						}if(resultados!=null) {
							event.setResultados(resultados);
						}if(idDisciplina!=null) {
							idDisciplinaFuera = idDisciplina;
							buscarDisciplina = true;
						}if(d!=null) {
							event.setLastModification(d);
						}
						if(buscarDisciplina) {
							conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
							String sql2 = "SELECT * FROM "+ServerInfo.DATABASE+".Disciplina WHERE Disciplina.idDisciplina = '" + idDisciplinaFuera + "'";
							PreparedStatement pS2 = conn.prepareStatement(sql2);
							ResultSet rS2 = pS2.executeQuery(sql2);
							while(rS2.next()) {
								disciplina = new Disciplina();
								disciplina.setIdDisciplina(rS2.getInt("idDisciplina"));
								disciplina.setNombre(rS2.getString("Nombre"));
								disciplina.setDescripcion(rS2.getString("Descripcion"));
								disciplina.setLastModification(rS2.getDate("LastModification"));
							}
							rS2.close();
							conn.close();
							event.setDisciplina(disciplina);
							lRes.add(event);
						}
					}
					rS.close();
					conn.close();
				}else {
					respondido = true;
					respuesta = new MensajeApp("error","badformat2");
					response.setContentType("application/json");
					response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
				}
			} catch (SQLException e) {
				//e.printStackTrace();
				respuesta = new MensajeApp("error","SQLException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			} catch (ClassNotFoundException e) {
				respuesta = new MensajeApp("error","ClassNotFoundException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
			if(!respondido) {
				if(!lRes.isEmpty()) {
					response.setContentType("application/json");
					response.getWriter().print(new JSONSerializer().exclude("class").serialize(lRes));
				}else {
					respuesta = new MensajeApp("error","noexists");
					response.setContentType("application/json");
					response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
				}
			}
		}
	}
}
