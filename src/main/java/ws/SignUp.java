package ws;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.User;
import flexjson.JSONSerializer;

/**
 * Servlet implementation class SignUp
 */
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
    
    }
    
    private static int randInt(int min, int max) {

	    Random rand = new Random();

	    int result = rand.nextInt() * (max - min) + min;

	    return result;

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String edad = request.getParameter("edad");
		MensajeApp respuesta = null;
		User uRes = null;
		List<User> lRes = new ArrayList<User>();
		if(email!=null) {
			try {
				Class.forName(ServerInfo.JDBC_DRIVER);
				//Open a connection
				conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
				//Execute a query
				String sql;
				sql = "SELECT * FROM "+ServerInfo.DATABASE+".Usuario WHERE Usuario.Email = '" + email +"'";
				PreparedStatement pS = conn.prepareStatement(sql);
				ResultSet rS = pS.executeQuery(sql);
				while(rS.next()) {
					uRes = new User();
					String email2 = rS.getString("Email");
					String username2 = rS.getString("Username");
					String password2 = rS.getString("Password");
					Integer edad2 = rS.getInt("Edad");
					Date d = rS.getDate("LastModification");
					if(email!=null) {
						uRes.setEmail(email2);
					}if(username!=null) {
						uRes.setUsername(username2);
					}if(password!=null){
						uRes.setPassword(password2);
					}if(edad!=null) {
						uRes.setEdad(edad2);
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
				respuesta = new MensajeApp("error","SQLException - 1");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
			if(!lRes.isEmpty()) {
				respuesta = new MensajeApp("error","exists");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}else {
				//Insertar usuario
				try {
					conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
					Date dAux = new Date();
					if(edad == null) {
						edad = String.valueOf(randInt(5,90));
					}
					Timestamp timestamp = new Timestamp(dAux.getTime());
					String sql = "INSERT INTO `"+ServerInfo.DATABASE+"`.`Usuario` (`Email`, `Username`, `Edad`, `Password`, `LastModification`) VALUES ('"+email+"', '"+username+"', '"+edad+"', '"+password+"', '"+timestamp+"');";
					PreparedStatement pS = conn.prepareStatement(sql);
					int resultado = pS.executeUpdate();
					if(resultado == 1) {
						respuesta = new MensajeApp("ok","inserted");
						response.setContentType("application/json");
						response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
					}else {
						respuesta = new MensajeApp("error","noinserted");
						response.setContentType("application/json");
						response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
					}
				} catch (SQLException e) {
					e.printStackTrace();
					respuesta = new MensajeApp("error","SQLException - 2");
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