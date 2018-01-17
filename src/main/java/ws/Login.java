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

import entities.User;
import flexjson.JSONSerializer;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		MensajeApp respuesta = null;
		if((email == null)||(password == null )) {
			respuesta = new MensajeApp("error","missing");
			response.setContentType("application/json");
			response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
		}else {
			Connection conn = null;
			User uRes = null;
			List<User> lRes = new ArrayList<User>();
			//Open a connection
			try {
				Class.forName(ServerInfo.JDBC_DRIVER);
				conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
				String sql;
				sql = "SELECT * FROM "+ServerInfo.DATABASE+".Usuario WHERE Usuario.Email = '"+email+"'";
				PreparedStatement pS = conn.prepareStatement(sql);
				ResultSet rS = pS.executeQuery(sql);
				while(rS.next()) {
					uRes = new User();
					String password2 = rS.getString("Password");
					if(password2!=null) {
						uRes.setPassword(password2);
					}
					lRes.add(uRes);
				}
				rS.close();
				conn.close();
			} catch (SQLException e) {
				respuesta = new MensajeApp("error","SQLException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			} catch (ClassNotFoundException e) {
				respuesta = new MensajeApp("error","ClassNotFoundException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
			if(!lRes.isEmpty()) {
				User uAux = lRes.get(0);
				if(uAux.getPassword()!=null) {
					if(uAux.getPassword().equals(password)) {
						respuesta = new MensajeApp("ok","user");
						response.setContentType("application/json");
						response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
					}else {
						respuesta = new MensajeApp("error","password");
						response.setContentType("application/json");
						response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
					}
				}
			}else {
				//No existe el usuario
				respuesta = new MensajeApp("error","nouser");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
		}
	}
}