package ws;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.Evento;
import entities.UsuarioCompraEvento;
import flexjson.JSONSerializer;

/**
 * Servlet implementation class BuyTicket
 */
public class BuyTicket extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static boolean yaComprado;



	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BuyTicket() {
		super();
		BuyTicket.yaComprado = false;
	}
	
	private static float randFloat(float min, float max) {

	    Random rand = new Random();

	    float result = rand.nextFloat() * (max - min) + min;

	    return result;

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String idEvento = request.getParameter("idEvento");
		MensajeApp respuesta = null;
		Connection conn = null;
		Evento ev = null;
		//Integer disciplinaId = null;
		Integer idEventoInt = null;
		if(idEvento == null || email == null) {
			respuesta = new MensajeApp("error","missing");
			response.setContentType("application/json");
			response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
		}else {
			//Buscar el evento 
			try {
				Class.forName(ServerInfo.JDBC_DRIVER);
				//Open a connection
				conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
				//Execute a query
				//Existe el evento?
				String sql;
				sql ="SELECT * FROM " +ServerInfo.DATABASE+".Evento WHERE Evento.idEvento='"+idEvento+"'";
				PreparedStatement pS = conn.prepareStatement(sql);
				ResultSet rS = pS.executeQuery(sql);
				while(rS.next()) {
					ev = new Evento();
					idEventoInt = rS.getInt("idEvento");
					String lugar = rS.getString("Lugar");
					Date fecha = rS.getDate("Fecha");
					Date hora = rS.getDate("Hora");
					String tipo = rS.getString("Tipo");
					String resultados = rS.getString("Resultados");
					//disciplinaId = rS.getInt("Disciplina_idDisciplina");
					Date d = rS.getDate("LastModification");
					ev.setIdEvento(idEventoInt);
					ev.setLugar(lugar);
					ev.setFecha(fecha);
					ev.setHora(hora);
					ev.setTipo(tipo);
					ev.setResultados(resultados);
					ev.setLastModification(d);
				}
				rS.close();
				conn.close();
				//Aqui se si existe o no el evento
				if(ev!=null) {
					List<UsuarioCompraEvento> eventosUser = new ArrayList<UsuarioCompraEvento>();
					//Compruebo si ya se ha comprado
					conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
					String sql2;
					sql2 ="SELECT * FROM "+ServerInfo.DATABASE+".Usuario_compra_Evento WHERE Usuario_compra_Evento.Usuario_Email='"+email+"' AND Usuario_compra_Evento.Evento_idEvento='"+ev.getIdEvento()+"';";
					PreparedStatement pS2 = conn.prepareStatement(sql2);
					ResultSet rS2 = pS2.executeQuery(sql2);
					while(rS2.next()) {
						UsuarioCompraEvento uAux = new UsuarioCompraEvento();
						String usuarioEmail = rS2.getString("Usuario_Email");
						Integer eventoId = rS2.getInt("Evento_idEvento");
						Float precio = rS2.getFloat("Precio");
						Date fechaCompra = rS2.getDate("Fecha_de_Compra");
						Date d = rS2.getDate("LastModification");
						uAux.setEvento(eventoId);
						uAux.setFecha_de_compra(fechaCompra);
						uAux.setLastModification(d);
						uAux.setPrecio(precio);
						uAux.setUsuario(usuarioEmail);
						eventosUser.add(uAux);
					}
					rS2.close();
					conn.close();
					//Comprobar si está o no vacía
					if(eventosUser.isEmpty()) {
						//No ha comprado el evento
						conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
						Timestamp fechaCompra = new Timestamp(System.currentTimeMillis());
						Float precio = randFloat(5.5f,60.0f);
						sql2 = "INSERT INTO `"+ServerInfo.DATABASE+"`.`Usuario_compra_Evento` (`Usuario_Email`, `Evento_idEvento`, `Precio`, `Fecha_de_Compra`, `LastModification`) VALUES ('"+email+"', '"+idEventoInt+"', '"+precio+"', '"+fechaCompra+"', '"+fechaCompra+"');";
						PreparedStatement pS3 = conn.prepareStatement(sql2);
						int resultado = pS3.executeUpdate();
						if(resultado == 1) {
							SendEmail sE = new SendEmail();
							String relativeWebPath = "//WEB-INF//resources";
							String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
							sE.SendMail(email,absoluteDiskPath,"Tokyo 2020 receipt confirmation", "Hi " + email + 
									"\n\nThis is an automatic response from MyTokyo2020.\n\n"
									+ "Your purchase for the event " + ev.getIdEvento() + " ,which will take place on " + new SimpleDateFormat("dd/MM/yyyy").format(ev.getFecha()).toString()
									+ " has been made correctly.\n\nPlease confirm the following information:\n\n"
									+"\t- User email: " + email +"\n"
									+"\t- Event id: " + ev.getIdEvento()+"\n"
									+"\t- Event Place: " + ev.getLugar()+"\n"
									+"\t- Event Date: " + new SimpleDateFormat("dd/MM/yyyy").format(ev.getFecha())+"\n"
									+"\t- Event Hour: " + new SimpleDateFormat("HH:MM:SS").format(ev.getHora())+"\n"
									+"\t- Event Type: " + ev.getTipo()+"\n"
									+"\t- Price paid: " + precio + "\n\n"
									+"A pdf file with your copy of the ticket has been attached to this email.\n\n"
									+"If you have any problem please contact dam2017g3@gmail.com\n"
									+"\nYours faithfully,"
									+"\n\nMyTokyo2020 support team.");
							respuesta = new MensajeApp("ok","bought");
							response.setContentType("application/json");
							response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
						}else {
							respuesta = new MensajeApp("error","nobought");
							response.setContentType("application/json");
							response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
						}
						pS3.close();
						conn.close();
					}else {
						//Ya ha comprado el evento
						respuesta = new MensajeApp("error","yetbought");
						response.setContentType("application/json");
						response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
					}
				}
				else {
					respuesta = new MensajeApp("error","noEvent");
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
		}
	}
}