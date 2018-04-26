package shidi;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pm0018
 */
public class QueryProcessing extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        PrintWriter out = response.getWriter();
        ServletContext context = getServletConfig().getServletContext();
        String contextPath = context.getRealPath("");
        String rdfFile=contextPath + "/Data/devicelist.rdf"; 
        
        String queryString= request.getParameter("query");
        String outputFormat = request.getParameter("format");
        
        if (outputFormat.equalsIgnoreCase("xml"))
            response.setContentType("text/xml;charset=UTF-8");
        else
            response.setContentType("text/plain;charset=UTF-8");
        
        // Create an empty in-memory model and populate it from the graph
        Model model = ModelFactory.createDefaultModel();
        
        FileInputStream fileStream = new FileInputStream(rdfFile);
        InputStream inputStream = fileStream;
        
        model.read(inputStream,"http://shidi/Sensor_Publication/Assignment#");
        
        Query sparql = QueryFactory.create(queryString);
	QueryExecution qe = QueryExecutionFactory.create(sparql, model);
	ResultSet rs = qe.execSelect();
	// For debugging
	//ResultSetFormatter.out(System.out, rs);	
	if (outputFormat.compareTo("XML")==0 )
		out.println(ResultSetFormatter.asXMLString(rs));
	else 
		out.println(ResultSetFormatter.asText(rs));
				
	qe.close();
        fileStream.close(); 
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

