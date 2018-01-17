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

import entities.Deportista;
import entities.Pais;
import flexjson.JSONSerializer;

/**
 * Servlet implementation class GetDeportistasEvento
 */
public class GetDeportistasEvento extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDeportistasEvento() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idEvento = request.getParameter("idEvento");
		MensajeApp respuesta = null;
		Connection conn = null;
		List<Deportista> deportistas = new ArrayList<Deportista>();
		Deportista depAux = null;
		Pais paisAux = null;
		//Buscar el evento
		if(idEvento == null) {
			respuesta = new MensajeApp("error","missing");
			response.setContentType("application/json");
			response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
		}else {
			try {
				Class.forName(ServerInfo.JDBC_DRIVER);
				conn = DriverManager.getConnection(ServerInfo.DB_URL,ServerInfo.USER,ServerInfo.PASS);
				String sql;
				sql = "SELECT * FROM "+ServerInfo.DATABASE+".Evento_participa_Deportista WHERE Evento_idEvento='"+idEvento+"';";
				PreparedStatement pS = conn.prepareStatement(sql);
				ResultSet rS = pS.executeQuery(sql);
				while(rS.next()) {
					Integer idDeportista = rS.getInt("Deportista_idDeportista");
					if(idDeportista!=null) {
						sql = "SELECT * FROM "+ServerInfo.DATABASE+".Deportista WHERE idDeportista='"+idDeportista+"';";
						PreparedStatement pS2 = conn.prepareStatement(sql);
						ResultSet rS2 = pS2.executeQuery(sql);
						while(rS2.next()) {
							Integer idPais = rS2.getInt("Pais_idPais");
							sql = "SELECT * FROM sql11208838.Pais WHERE idPais='"+idPais+"';";
							PreparedStatement pS3 = conn.prepareStatement(sql);
							ResultSet rS3 = pS3.executeQuery(sql);
							while(rS3.next()) {
								paisAux = new Pais();
								paisAux.setIdPais(rS3.getInt("idPais"));
								paisAux.setNombre(rS3.getString("Nombre"));
								paisAux.setMedallasOro(rS3.getInt("MedallasOro"));
								paisAux.setMedallasPlata(rS3.getInt("MedallasPlata"));
								paisAux.setMedallasBronce(rS3.getInt("MedallasBronce"));
								paisAux.setPosRanking(rS3.getInt("PosRanking"));
								paisAux.setLastModificaton(rS3.getDate("LastModificaton"));
							}
							rS3.close();
							depAux = new Deportista();
							depAux.setIdDeportista(rS2.getInt("idDeportista"));
							depAux.setNombre(rS2.getString("Nombre"));
							depAux.setEdad(rS2.getInt("Edad"));
							depAux.setPais(paisAux);
							depAux.setLastModification(rS2.getDate("LastModification"));
							depAux.setMedallasOro(rS2.getInt("MedallasOro"));
							depAux.setMedallasPlata(rS2.getInt("MedallasPlata"));
							depAux.setMedallasBronce(rS2.getInt("MedallasBronce"));
							deportistas.add(depAux);							
						}
						rS2.close();
					}
				}
				rS.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				respuesta = new MensajeApp("error","SQLException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			} catch (ClassNotFoundException e) {
				respuesta = new MensajeApp("error","ClassNotFoundException");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
			if(!deportistas.isEmpty()) {
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(deportistas));
			}else {
				respuesta = new MensajeApp("error","nodeportistas");
				response.setContentType("application/json");
				response.getWriter().print(new JSONSerializer().exclude("class").serialize(respuesta));
			}
		}
	}
}
