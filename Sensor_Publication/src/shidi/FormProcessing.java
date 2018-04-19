package shidi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class FormProcessing extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
           
        // retrieving the physical path of the Context (where the main files are 
        // physically located; we will use this to write and read the XML and RDF files
        // there is a folder Data that the files are stored into that file;
        ServletContext context = getServletConfig().getServletContext();
        String contextPath = context.getRealPath("");
        
        // this will read all the parameters submitted by the form
        // one array will keep the parameter names (HTML form items' names)
        // the other array will store the values
        Enumeration e = request.getParameterNames();
        ArrayList xmlNodes = new ArrayList();
        ArrayList xmlNodeValues = new ArrayList();
        
        while(e.hasMoreElements()){
              String attrName  = (String)e.nextElement();
              String attrValue = (String) request.getParameter(attrName);
              
              xmlNodes.add(attrName);
              xmlNodeValues.add(attrValue);
            }
        
        // if you use and fixed path and use Linux or MACOS don't forget to change this to 
        // an appropriate path; same also in XMLtoRDF Class
        // with the current code and using NetBeans your file will be
        // saved/openend at <<pathofyourprojectfolder>>\MAWS\build\web\data\...
       
        String fileName= contextPath + "/Data/devicelist.xml";
        System.out.println(fileName);
        
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc= CreateXMLDocument(doc,xmlNodes, xmlNodeValues);
            SaveXMLDocument(doc, fileName);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FormProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println("<br>");
            out.println("<br>");
            out.println("<h2> STEP1 completed<br> </h2>");
            out.println("<br>");
            out.println("XML data is created successfully;<br>");
            out.println("<br>");
            //out.println("The file is saved to:" + fileName + "<br>");
            out.println("<br>");
            out.println("Click  <a href=\"XMLTORDF\">here</a> to start Step 2 (Convert the XML format to RDF)<br>");
            out.println("<br>");
            out.println("<br>");
            out.println("Click <a href=\"/Data/devicelist.xml\">here</a> to see the XML file");
            out.println("Click <a href=\"index.jsp\">here</a> to go to home page");
            out.println("and quary page. <br>");
        
        
        
    }

    Document CreateXMLDocument (Document doc, ArrayList node, ArrayList value) throws ParserConfigurationException {

        Element root = doc.createElement("DeviceList");
        doc.appendChild(root);

        int i = 0;

        Element firstLevel = doc.createElement("Device");
        root.appendChild(firstLevel);

        while (i < node.size()){
            if (node !=null && value !=null && value.size() >0 ){
                String elementNode = (String) node.get(i);
                String elementText = (String) value.get(i);

                if (elementNode !=null && elementText!=null) {
                    Element child = doc.createElement(elementNode);
                    firstLevel.appendChild(child);

                    Text text = doc.createTextNode(elementText);
                    child.appendChild(text);
                }
            }
            i++;
        }

        return doc;
    }
    
    protected void SaveXMLDocument(Document doc, String fileName) {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            Result xmlFile = new StreamResult(new File(fileName));
            Source xmlDoc = new DOMSource(doc);
            try {
                transformer.transform(xmlDoc, xmlFile);
            } catch (TransformerException ex) {
                Logger.getLogger(FormProcessing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(FormProcessing.class.getName()).log(Level.SEVERE, null, ex);
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
