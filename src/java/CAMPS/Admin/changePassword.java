package CAMPS.Admin;

import CAMPS.Connect.DBConnect;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class changePassword extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession ses = request.getSession(false);
        PrintWriter out = response.getWriter();
        try {
            DBConnect db = new DBConnect();
            db.getConnection();
            ResourceBundle rb = ResourceBundle.getBundle("CAMPS.Admin.private_key");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            String userId = ses.getAttribute("user_id").toString();
            if (newPassword.equals(confirmPassword)) {
                db.read("SELECT * FROM admin.user_master um WHERE (um.user_password=SHA1(SHA1(MD5(CONCAT('" + rb.getString("current_key.first") + "',um.pwdSalt,'" + oldPassword + "',um.pwdSalt,'" + rb.getString("current_key.last") + "')))) OR  um.user_password=SHA1(SHA1(MD5(CONCAT(um.pwdSalt,'" + rb.getString("next_key.first") + "','" + oldPassword + "','" + rb.getString("next_key.last") + "',um.pwdSalt)))) ) AND um.user_id='" + userId + "'");
                if (db.rs.first() && db.rs.getRow() == 1)//check username password exits
                {
                    db.update("UPDATE admin.user_master SET pwdSalt=SHA1(MD5(SHA1(RAND()))),user_password=SHA1(SHA1(MD5(CONCAT('" + rb.getString("next_key.first") + "',pwdSalt,'" + newPassword + "',pwdSalt,'" + rb.getString("next_key.last") + "')))),updated_date=NOW(),status=1 WHERE user_id='" + userId + "' ");
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + "logout");
                } else {
                    out.print("<div class=\"warning\"><img src=\"" + request.getServletContext().getAttribute("hostname").toString() + "/Images/alert.png\">Old Password Incorrect</div>");
                }
            } else {
                out.print("<div class=\"warning\"><img src=\"" + request.getServletContext().getAttribute("hostname").toString() + "/Images/alert.png\">New Password Does Not Match</div>");
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace(out);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
