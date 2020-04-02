package CAMPS.Admin;

import CAMPS.Common.escapeSpecialChars;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import CAMPS.Connect.DBConnect;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

public class authentication extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession ses = request.getSession(true);
        PrintWriter out = response.getWriter();
        DBConnect db = new DBConnect();
        try {
            request.getServletContext().setAttribute("hostname", (Object) (request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/"));
            db.getConnection();
            escapeSpecialChars objesc = new escapeSpecialChars();
            ResourceBundle rb = ResourceBundle.getBundle("CAMPS.Admin.private_key");
            String redirect_url = "";
            boolean Admin = false;
            String ip = request.getHeader("X-FORWARDED-FOR");
            request.getHeader("VIA");
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            ip = ipAddress;
            String signin_status = "";
            String gkey = (String) ses.getAttribute("gkey");
            if (gkey != null) {
                db.read("SELECT IFNULL(sm.department_id,sam.branch_id) db_id,um.user_id,um.roles,um.user_name,um.status,NOW() lastlogin,CASE WHEN um.staff_id IS NOT NULL THEN 'Staff' WHEN stu.student_id IS NOT NULL THEN 'Student' WHEN mr.student_id IS NOT NULL THEN 'Parent' END ss_det ,IF(um.staff_id IS NOT NULL,sm.staff_id,stu.student_id) staff_id,IF(um.staff_id IS NOT NULL,CONCAT(sm.first_name,' ',sm.middle_name,' ',sm.last_name),CONCAT(stu.first_name,' ',stu.middle_name,' ',stu.last_name)) staff_name,IF(um.staff_id IS NOT NULL,sm.institute_email_id,stu.institute_email_id)institute_email_id FROM admin.user_master um LEFT JOIN camps.staff_master sm ON um.staff_id=sm.staff_id LEFT JOIN camps.student_master stu ON stu.student_id=um.student_id LEFT JOIN camps.master_relation mr ON mr.student_id=um.student_id LEFT JOIN student_admission_master sam ON sam.student_id=um.student_id WHERE stu.institute_email_id='" + gkey + "' OR sm.institute_email_id='" + gkey + "' OR mr.email_id='" + gkey + "' AND um.status>0 ");
                if(db.rs.next()){
                   db.rs.beforeFirst();
                }else{
                     db.read("SELECT sam.branch_id db_id,sam.student_id user_id,30 roles,stu.first_name user_name,1 status,NOW() lastlogin,'Student' ss_det,stu.student_id staff_id,CONCAT(IFNULL(stu.first_name,''),' ',IFNULL(stu.middle_name,''),' ',IFNULL(stu.last_name,'')) staff_name,stu.institute_email_id FROM camps.student_master stu INNER JOIN camps.student_admission_master sam ON stu.student_id=sam.student_id WHERE stu.institute_email_id='" + gkey + "' AND sam.student_status LIKE 'conti%'");
                
                }
            } else {
                db.read("SELECT IFNULL(sm.department_id,sam.branch_id) db_id,um.user_id,um.roles,um.user_name,um.status,now() lastlogin,IF(um.staff_id IS NOT NULL,'Staff','Student') ss_det ,IF(um.staff_id IS NOT NULL,sm.staff_id,stu.student_id) staff_id,IF(um.staff_id IS NOT NULL,CONCAT(sm.first_name,' ',sm.middle_name,' ',sm.last_name),CONCAT(stu.first_name,' ',stu.middle_name,' ',stu.last_name)) staff_name,IF(um.staff_id IS NOT NULL,sm.institute_email_id,stu.institute_email_id)institute_email_id FROM admin.user_master um LEFT JOIN camps.staff_master sm ON um.staff_id=sm.staff_id LEFT JOIN camps.student_master stu ON stu.student_id=um.student_id LEFT JOIN student_admission_master sam ON sam.student_id=um.student_id WHERE (um.user_password=SHA1(SHA1(MD5(CONCAT(um.pwdsalt,'" + rb.getString("current_key.first") + "','" + objesc.escapeSpecialChar(request.getParameter("pword")) + "','" + rb.getString("current_key.last") + "',um.pwdsalt)))) or um.user_password=SHA1(SHA1(MD5(CONCAT(um.pwdsalt,'" + rb.getString("next_key.first") + "','" + objesc.escapeSpecialChar(request.getParameter("pword")) + "','" + rb.getString("next_key.last") + "',um.pwdsalt))))) and STRCMP(um.user_name,'admin')=0 AND um.status>0");
                if (db.rs.first()) {
                    Admin = true;
                }
                if (Admin) {
                    db.read("SELECT IFNULL(sm.department_id,sam.branch_id) db_id,um.user_id,um.roles,um.user_name,um.status,now() lastlogin,IF(um.staff_id IS NOT NULL,'Staff','Student') ss_det ,IF(um.staff_id IS NOT NULL,sm.staff_id,stu.student_id) staff_id,IF(um.staff_id IS NOT NULL,CONCAT(sm.first_name,' ',sm.middle_name,' ',sm.last_name),CONCAT(stu.first_name,' ',stu.middle_name,' ',stu.last_name)) staff_name,IF(um.staff_id IS NOT NULL,sm.institute_email_id,stu.institute_email_id)institute_email_id FROM admin.user_master um LEFT JOIN camps.staff_master sm ON um.staff_id=sm.staff_id LEFT JOIN camps.student_master stu ON stu.student_id=um.student_id LEFT JOIN student_admission_master sam ON sam.student_id=um.student_id WHERE  STRCMP(um.user_name,'" + objesc.escapeSpecialChar(request.getParameter("uname")) + "')=0 AND um.status>0");
                } else {
                    db.read("SELECT IFNULL(sm.department_id,sam.branch_id) db_id,um.user_id,um.roles,um.user_name,um.status,now() lastlogin,IF(um.staff_id IS NOT NULL,'Staff','Student') ss_det ,IF(um.staff_id IS NOT NULL,sm.staff_id,stu.student_id) staff_id,IF(um.staff_id IS NOT NULL,CONCAT(sm.first_name,' ',sm.middle_name,' ',sm.last_name),CONCAT(stu.first_name,' ',stu.middle_name,' ',stu.last_name)) staff_name,IF(um.staff_id IS NOT NULL,sm.institute_email_id,stu.institute_email_id)institute_email_id FROM admin.user_master um LEFT JOIN camps.staff_master sm ON um.staff_id=sm.staff_id LEFT JOIN camps.student_master stu ON stu.student_id=um.student_id LEFT JOIN student_admission_master sam ON sam.student_id=um.student_id WHERE (um.user_password=SHA1(SHA1(MD5(CONCAT(um.pwdsalt,'" + rb.getString("current_key.first") + "','" + objesc.escapeSpecialChar(request.getParameter("pword")) + "','" + rb.getString("current_key.last") + "',um.pwdsalt)))) or um.user_password=SHA1(SHA1(MD5(CONCAT(um.pwdsalt,'" + rb.getString("next_key.first") + "','" + objesc.escapeSpecialChar(request.getParameter("pword")) + "','" + rb.getString("next_key.last") + "',um.pwdsalt))))) and STRCMP(um.user_name,'" + objesc.escapeSpecialChar(request.getParameter("uname")) + "')=0 AND um.status>0");
                }
            }
            if (db.rs.first()) {
                if (db.rs.getRow() == 1) {
                    if (Admin) {
                        signin_status = "99";
                    } else {
                        signin_status = "1";
                    }
                    ses.setAttribute("login", db.rs.getString("user_id"));
                    ses.setAttribute("roles", db.rs.getString("roles"));
                    ses.setAttribute("user_id", db.rs.getString("user_id"));
                    ses.setAttribute("lastlogintime", db.rs.getString("lastlogin"));
                    ses.setAttribute("OTPstatus", "0");
                    ses.setAttribute("user_name", db.rs.getString("staff_name"));
                    ses.setAttribute("ss_id", db.rs.getString("staff_id"));
                    ses.setAttribute("depId", db.rs.getString("db_id"));
                    ses.setAttribute("email", db.rs.getString("institute_email_id"));
                    ses.setAttribute("uType", db.rs.getString("ss_det"));
                    db.read1("SELECT MIN(startuppage) startuppage,group_concat(role_name) role_name FROM admin.role_master WHERE role_id IN (" + db.rs.getString("roles") + ") ORDER BY startuppage DESC");
                    if (db.rs1.next()) {
                        ses.setAttribute("role_name", db.rs1.getString("role_name"));
                        switch (db.rs.getInt("status")) {
                            case 1:
                                if (db.rs1.getString("startuppage") != null && !db.rs1.getString("StartUpPage").isEmpty()) {
                                    redirect_url=db.rs1.getString("startuppage");
                                } else {
                                    redirect_url="JSP/Welcome/welcomePage.jsp";
                                }
                                break;
                            case 2:
                                redirect_url="JSP/Welcome/change_password.jsp";
                                break;
                            default:
                                redirect_url="JSP/Welcome/welcomePage.jsp";
                        }
                    }
                }
            } else {
                signin_status = "0";
                redirect_url=this.getServletContext().getAttribute("hostname").toString() + "CommonJSP/signin.jsp?er_c=0";
            }
            if (gkey != null) {
                db.insert("INSERT INTO admin.authentication_log VALUES('" + ip + "',NOW(),'" + gkey + "','" + signin_status + "');");
            } else {
                db.insert("INSERT INTO admin.authentication_log VALUES('" + ip + "',NOW(),'" + objesc.escapeSpecialChar(request.getParameter("uname")) + "','" + signin_status + "');");
            }
            response.sendRedirect(redirect_url);

        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            try {                
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(authentication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }
}
