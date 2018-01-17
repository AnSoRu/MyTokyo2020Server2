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

import entities.User;
import flexjson.JSONSerializer;

/**
 * Servlet implementation class GetUser
 */
public class GetUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetUser() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection conn = null;
		String email = request.getParameter("email");
		MensajeApp respuesta = null;
		User uRes = null;
		List<User> lRes = new ArrayList<User>();
		if(email!=null) {
			try {
				Class.forName(ServerInfo.JDBC_DRIVER);
				//Open a connection
				conn = DriverManager.getConnection(ServerInfo.DB_URL, ServerInfo.USER, ServerInfo.PASS);
				//Execute a query
				String sql;
				sql = "SELECT * FROM "+ServerInfo.DATABASE+".Usuario WHERE Usuario.Email = '" + email +"'";
				PreparedStatement pS = conn.prepareStatement(sql);
				ResultSet rS = pS.executeQuery(sql);
				while(rS.next()) {
					uRes = new User();
					String email2 = rS.getString("Email");
					String username = rS.getString("Username");
					String password = rS.getString("Password");
					Integer edad = rS.getInt("Edad");
					Date d = rS.getDate("LastModification");
					if(email!=null) {
						uRes.setEmail(email2);
					}if(username!=null) {
						uRes.setUsername(username);
					}if(password!=null){
						uRes.setPassword(password);
					}if(edad!=null) {
						uRes.setEdad(edad);
					}if(d!=null) {
						uRes.setLastModification(d);
					}
					lRes.add(uRes);
				}
				rS.close();
				conn.close();			
			} catch (ClassNotFoundException e) {
				respuesta = new MensajeApp("error","ClassNotFoundException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			} catch (SQLException e) {
				e.printStackTrace();
				respuesta = new MensajeApp("error","SQLException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
			if(!lRes.isEmpty()) {
				User uAux = lRes.get(0);
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(uAux));
			}else {
				respuesta = new MensajeApp("error","noexists");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
		}else {
			respuesta = new MensajeApp("error","missing");
			response.setContentType("application/json");
			response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
		}
	}

}
