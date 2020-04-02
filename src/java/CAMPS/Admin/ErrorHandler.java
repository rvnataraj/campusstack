package CAMPS.Admin;

import CAMPS.Connect.DBConnect;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ErrorHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String em_id="";
        // Analyze the servlet exception
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        // Set response content type
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        
           
            DBConnect db = new DBConnect();
            try {
                db.getConnection();
                String query = "INSERT INTO admin.exception_master(type, request_uri, message, inserted_by, inserted_date) VALUES('" + throwable.getClass().getName() + "','" + requestUri + "','" + throwable.getMessage().replaceAll("'", "\\\\'") + "'," + (request.getSession().getAttribute("user_id") != null ? "\'" + request.getSession().getAttribute("user_id") + "\'" : "NULL") + ",NOW())";
                //out.write(query);
                 em_id=db.insertAndGetAutoGenId(query);
                out.print("<h1><mark style=\"background: red;color: yellow;\">Error Code:<b>"+em_id+"</b></mark></h1>");
                db.closeConnection();
            } catch (Exception ex) {
                out.print(ex);
            }

        //response.sendRedirect(this.getServletContext().getAttribute("hostname").toString() +"CommonJSP/error_message.jsp?err="+em_id);
    }
}
