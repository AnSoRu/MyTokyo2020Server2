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

import entities.Deportista;
import entities.Pais;
import flexjson.JSONSerializer;


/**
 * Servlet implementation class GetDeportista
 */
public class GetDeportista extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetDeportista() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		String nombre = request.getParameter("nombre");
		MensajeApp respuesta = null;
		Deportista dRes = null;
		Pais pRes = null;
		List<Deportista> lRes = new ArrayList<Deportista>();
		boolean buscarPais = false;
		Integer idPaisFuera = null;
		if(nombre!=null) {
			try {
				Class.forName(ServerInfo.JDBC_DRIVER);
				//Open a connection
				conn = DriverManager.getConnection(ServerInfo.DB_URL, ServerInfo.USER, ServerInfo.PASS);
				//Execute a query
				String sql = "SELECT * FROM "+ServerInfo.DATABASE+".Deportista WHERE Deportista.Nombre='"+nombre+"';";
				PreparedStatement pS = conn.prepareStatement(sql);
				ResultSet rS = pS.executeQuery(sql);
				while(rS.next()) {
					dRes = new Deportista();
					Integer id = rS.getInt("idDeportista");
					String nombre2 = rS.getString("Nombre");
					Integer edad = rS.getInt("Edad");
					Integer idPais = rS.getInt("Pais_idPais");
					Date d = rS.getDate("LastModification");
					Integer medallasOro = rS.getInt("MedallasOro");
					Integer medallasPlata = rS.getInt("MedallasPlata");
					Integer medallasBronce = rS.getInt("medallasBronce");
					if(id!=null) {
						dRes.setIdDeportista(id);
					}if(nombre2!=null) {
						dRes.setNombre(nombre2);
					}if(edad!=null) {
						dRes.setEdad(edad);
					}if(idPais!=null) {
						idPaisFuera = idPais;
						buscarPais = true;
					}if(d!=null) {
						dRes.setLastModification(d);
					}if(medallasOro!=null) {
						dRes.setMedallasOro(medallasOro);
					}if(medallasPlata!=null) {
						dRes.setMedallasPlata(medallasPlata);
					}if(medallasBronce!=null) {
						dRes.setMedallasBronce(medallasBronce);
					}
					if(buscarPais) {
						//Buscar el pais
						conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
						String sql2 = "SELECT * FROM "+ServerInfo.DATABASE+".Pais WHERE Pais.idPais = '" + idPaisFuera +"'";
						PreparedStatement pS2 = conn.prepareStatement(sql2);
						ResultSet rS2 = pS2.executeQuery(sql2);
						while(rS2.next()) {
							pRes = new Pais();
							idPais = rS2.getInt("idPais");
							String nombrePais = rS2.getString("Nombre");
							Integer medallasPOro = rS2.getInt("MedallasOro");
							Integer medallasPPlata = rS2.getInt("MedallasPlata");
							Integer medallasPBronce = rS2.getInt("MedallasBronce");
							Integer posRanking = rS2.getInt("PosRanking");
							Date dP = rS2.getDate("LastModificaton");
							pRes.setIdPais(idPais);
							pRes.setNombre(nombrePais);
							pRes.setMedallasOro(medallasPOro);
							pRes.setMedallasPlata(medallasPPlata);
							pRes.setMedallasBronce(medallasPBronce);
							pRes.setPosRanking(posRanking);
							pRes.setLastModificaton(dP);
						}
						rS2.close();
						conn.close();
						dRes.setPais(pRes);
						lRes.add(dRes);
					}
				}
				rS.close();
				conn.close();
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
				respuesta = new MensajeApp("error","ClassNotFoundException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}				catch (SQLException e) {
				//e.printStackTrace();
				respuesta = new MensajeApp("error","SQLException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
			if(!lRes.isEmpty()) {
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").deepSerialize(lRes));
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
