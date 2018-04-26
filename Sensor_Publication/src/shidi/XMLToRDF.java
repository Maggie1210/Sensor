package shidi;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author pm0018
 */
public class XMLToRDF extends HttpServlet {

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
            throws ServletException, IOException  {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        ServletContext context = getServletConfig().getServletContext();
        String contextPath = context.getRealPath("");
        
        String fileName= contextPath + "/Data/devicelist.xml";
        String rdfFile=contextPath + "/Data/devicelist.rdf"; 
        
        
        
        
        // source of XSLT file xxx.xsl 
        String xsltSource= "/Data/RDFTemplate.xsl";
        
        try {
            // open and parse the XML file
            File xmlFile = new File(fileName);
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (xmlFile);
            
            boolean Checkpint = doc.hasChildNodes();
            //transofrmation
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            
            Source source = new StreamSource(context.getRealPath(xsltSource));
            
            Result result = new StreamResult(new File(rdfFile));
            
            // Apply the XSLT transform to conver XML to RDF form accoring to the XSL file template
            trans = transfac.newTransformer(source);
            // save RDF document
            Source xmlDoc = new DOMSource(doc);
            trans.transform(xmlDoc, result);
            
        } catch (ParserConfigurationException ex) {
            System.out.println ("1:"+ex.getMessage());
        }
        catch (SAXException ex) {
            System.out.println("2:"+ex.getMessage());
        }
        catch (TransformerException ex) {
            System.out.println("3:"+ex.getMessage());
        }
        
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet XMLTORDF</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>STEP 2 completed</h2>");
            out.println("<br>");
            out.println("RDF data is created successfully;<br>");
            out.println("<br>");
            //out.println("The file is saved to:" + rdfFile + "<br>");
            out.println("<br>");
            out.println("Click <a href=\"/Sensor_Publication/Data/devicelist.xml\">here</a> to see the XML file");
            out.println("<br>");
            out.println("<br>");
            out.println("Click <a href=\"/Sensor_Publication/Data/devicelist.rdf\">here</a> to see the RDF file");
            out.println("<br>");
            out.println("<br>");
            out.println("Click <a href=\"index.jsp\">here</a> to go to home page");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
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
