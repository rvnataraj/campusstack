
package CAMPS.Staff;

import CAMPS.Common.form_process;
import CAMPS.Common.report_process;
import CAMPS.Connect.DBConnect;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class staff_Profile extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        DBConnect db = new DBConnect();
        try (PrintWriter out = response.getWriter()) {
            try {
                db.getConnection();
                if (request.getParameter("staff_det_insert") != null && request.getParameter("staff_det_insert").equalsIgnoreCase("submit")) {
                    db.insert("INSERT INTO camps.staff_master (   staff_id,   legend,   first_name,   middle_name,   last_name,   department_id,   gender,   dob,   blood_group_id,   per_address_line1,   per_address_line2,   per_address_line3,   per_pincode,   cur_address_line1,   cur_address_line2,   cur_address_line3,   cur_pincode,   aadhaar_no,   passport_no,   intercom_no,   mobile_no,   email_id,   institute_email_id,   nationality_id,   community_id,   religion_id,   caste_id,   inserted_by,   inserted_date,   updated_by,   updated_date ) VALUES   (    NULL,     '" + request.getParameter("legend") + "',     '" + request.getParameter("firstname") + "',     '" + request.getParameter("midname") + "',     '" + request.getParameter("lastname") + "',     '" + request.getParameter("dept") + "',     '" + request.getParameter("gender") + "',    Str_to_Date( '" + request.getParameter("dob") + "','%d-%m-%Y'),     '" + request.getParameter("bg") + "',     '" + (request.getParameter("sameas").equalsIgnoreCase("on") ? request.getParameter("ca1") : request.getParameter("pa1")) + "',     '" + (request.getParameter("sameas").equalsIgnoreCase("on") ? request.getParameter("ca2") : request.getParameter("pa2")) + "',     '" + (request.getParameter("sameas").equalsIgnoreCase("on") ? request.getParameter("ca3") : request.getParameter("pa3")) + "',     '" + (request.getParameter("sameas").equalsIgnoreCase("on") ? request.getParameter("capin") : request.getParameter("papin")) + "',     '" + request.getParameter("ca1") + "',     '" + request.getParameter("ca2") + "',     '" + request.getParameter("ca3") + "',     '" + request.getParameter("capin") + "',     '" + request.getParameter("aadhaar") + "',     '" + request.getParameter("passport") + "',     null,     '" + request.getParameter("mobile") + "',      '" + request.getParameter("email") + "',     null,     '" + request.getParameter("nation") + "',     '" + request.getParameter("community") + "',     '" + request.getParameter("religion") + "',     '" + request.getParameter("community") + "',     '" + session.getAttribute("user_id") + "',    now(),     '" + session.getAttribute("user_id") + "',     now()   ); ");
                    response.sendRedirect("staff_registration.jsp");
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadStaff")) {
                    report_process rp = new report_process();
                    ArrayList<String> attribute = new ArrayList<>();
                    attribute.add(request.getParameter("dept_id"));
                    attribute.add(request.getParameter("cat_id"));
                    out.print(rp.report_v1("8", attribute));
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadfilter_det")) {
                    report_process rs = new report_process();
                    ArrayList<String> attribute = new ArrayList<>();
                    String att = "True ";
                    if (request.getParameter("staff_id") != null && !request.getParameter("staff_id").equals("")) {
                        att += "AND staff_id like '%" + request.getParameter("staff_id") + "%' ";
                    }
                    if (request.getParameter("staff_name") != null && !request.getParameter("staff_name").equals("")) {
                        att += "AND staff_name like '%" + request.getParameter("staff_name") + "%' ";
                    }
                    if (request.getParameter("dept_id") != null && !request.getParameter("dept_id").equals("") && !request.getParameter("dept_id").equals("null")) {
                        att += "AND department_id in  (" + request.getParameter("dept_id") + ") ";
                    }
                    if (request.getParameter("cat_id") != null && !request.getParameter("cat_id").equals("") && !request.getParameter("cat_id").equals("null")) {
                        att += "AND sc_id in (" + request.getParameter("cat_id") + ")";
                    }
                    
                    attribute.add(att);
                    out.print(rs.report_v1("9", attribute));
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadpersonal_det")) {
                    form_process fp = new form_process();                    
                    ArrayList<String> attribute = new ArrayList<>();
                    attribute.add(request.getParameter("staff_id"));
                    out.print("" + fp.form_v1("4", attribute) + " ");
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadDailyAtt")) {
                    report_process rs = new report_process();
                    ArrayList<String> attribute = new ArrayList<>();
                    attribute.add(request.getParameter("date"));
                    attribute.add(request.getParameter("session"));                    
                    attribute.add(request.getParameter("category").replaceAll(",", "','"));
                    attribute.add(request.getParameter("scategory").replaceAll(",", "','"));
                    out.print("" + rs.report_v1("13", attribute) + " ");
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadac_det")) {
                    form_process fp = new form_process();                    
                    ArrayList<String> attribute = new ArrayList<>();
                    attribute.add(request.getParameter("staff_id"));
                    out.print("" + fp.form_grid_v1("5", attribute) + " ");
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_address_edit")) {
                    form_process fp = new form_process();
                    ArrayList<String> attribute = new ArrayList<>();
                    attribute.add(request.getParameter("staff_id"));
                    out.print("" + fp.form_v1("15", attribute) + " ");
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loaded_det")) {
                    form_process fp = new form_process();                    
                    ArrayList<String> attribute = new ArrayList<>();
                    attribute.add(request.getParameter("staff_id"));
                    out.print("" + fp.form_grid_v1("6", attribute) + " ");
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadparent_edit")) {
                    form_process fp = new form_process();
                    ArrayList<String> attribute = new ArrayList<>();
                    attribute.add(request.getParameter("staff_id"));
                    out.print("" + fp.form_grid_v1("7", attribute) + "");
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("edit_grid")) {
                    form_process fp = new form_process();
                    out.print(fp.form_Grid_Update_V1(request));
                    //response.sendRedirect("student_det_update.jsp?am_c="+ fp.form_Grid_Update_V1(request));
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("add_grid")) {
                    form_process fp = new form_process();
                    out.print(fp.form_Grid_Add_V1(request, session));
                    //response.sendRedirect("student_det_update.jsp?am_c="+ fp.form_Grid_Update_V1(request));
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("delete_grid")) {
                    form_process fp = new form_process();
                    out.print(fp.form_Grid_Delete_V1(request, session));
                    //response.sendRedirect("student_det_update.jsp?am_c="+ fp.form_Grid_Update_V1(request));
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("updatepersonal_edit")) {
                    form_process fp = new form_process();
                    out.print(fp.form_Update_V1(request));
                    response.sendRedirect("staff_profile_update.jsp?am_c=" + fp.form_Update_V1(request));
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("addpersonal_edit")) {
                    String staff_id = db.insertAndGetAutoGenId("INSERT INTO camps.staff_master (sc_id,legend,first_name,middle_name,last_name,department_id,dob,doj,working_status,inserted_by,inserted_date,updated_by,updated_date) VALUES ('" + request.getParameter("sc_id") + "','" + request.getParameter("legend") + "','" + request.getParameter("name") + "','','','" + request.getParameter("department") + "',STR_TO_DATE('" + request.getParameter("dob") + "','%d-%m-%Y'),STR_TO_DATE('" + request.getParameter("doj") + "','%d-%m-%Y'),'Working','" + session.getAttribute("user_id") + "',NOW(),'" + session.getAttribute("user_id") + "',NOW())");
                    db.insert("INSERT INTO camps.staff_promotion(staff_id,md_id,from_date,STATUS,inserted_by,inserted_date,updated_by,updated_date) values ('" + staff_id + "','" + request.getParameter("designation") + "',STR_TO_DATE('" + request.getParameter("doj") + "','%d-%m-%Y'),2,'" + session.getAttribute("user_id") + "',NOW(),'" + session.getAttribute("user_id") + "',NOW())");
                    if (request.getParameter("sc_id").equalsIgnoreCase("1")) {
                        ResourceBundle rb = ResourceBundle.getBundle("CAMPS.Admin.private_key");                    
                        db.insert("INSERT INTO admin.user_master (   user_id,   user_name,   user_password,   pwdsalt,   roles,   student_id,   staff_id,   STATUS,   inserted_by,   inserted_date,   updated_by,   updated_date ) SELECT NULL,'" + staff_id + "',SHA1(SHA1(MD5(CONCAT(a.salt,'" + rb.getString("next_key.first") + "',STR_TO_DATE('" + request.getParameter("doj") + "','%d-%m-%Y'),'" + rb.getString("next_key.last") + "',a.salt)))),a.salt,'3,25',NULL,'" + staff_id + "',2," + session.getAttribute("user_id") + ",NOW()," + session.getAttribute("user_id") + ",NOW() FROM (SELECT SHA1(MD5(SHA1(RAND()))) AS salt) a ");
                    }
                    response.sendRedirect("staff_registration.jsp?staff_id=" + staff_id);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadstaff1")) {
                    db.read("select sm.staff_id,TRIM(CONCAT(IFNULL(sm.first_name,''),' ',IFNULL(sm.middle_name,''),' ',IFNULL(sm.last_name,''),' - ',sm.staff_id,'')) staff_name from staff_master sm where sm.working_status='working' and  sm.sc_id in (" + request.getParameter("cat_id") + ") and sm.department_id in(" + request.getParameter("dept_id") + ")");
                    while (db.rs.next()) {
                        out.print("<option value='" + db.rs.getString("staff_id") + "'>" + db.rs.getString("staff_name") + "</option>");
                    }
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadpromotion_det")) {
                    form_process fp = new form_process();                    
                    ArrayList<String> attribute = new ArrayList<>();
                    attribute.add(request.getParameter("staff_id"));
                    out.print("" + fp.form_v1("13", attribute) + " ");                    
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("updatepromotion_det")) {
                    db.update("UPDATE camps.staff_promotion sp SET sp.to_date=STR_TO_DATE('" + request.getParameter("108") + "','%d-%m-%Y') -INTERVAL 1 DAY,sp.status=1,sp.updated_by='" + session.getAttribute("user_id") + "',sp.updated_date=NOW() where sp.to_date is null and sp.status=2 and sp.staff_id=" + request.getParameter("102") + "");
                    db.insert("INSERT INTO camps.staff_promotion (staff_id,md_id,from_date,STATUS,inserted_by,inserted_date,updated_by,updated_date) VALUES(" + request.getParameter("102") + "," + request.getParameter("106") + ",str_to_date('" + request.getParameter("108") + "','%d-%m-%Y'),2,'" + session.getAttribute("user_id") + "',NOW(),'" + session.getAttribute("user_id") + "',NOW())");
                    out.print(db.iEffectedRows > 0 ? 1 : 0);
                }
                if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_promotion")) {
                    String data = "";
                    String id = "";
                    data = "<div class=\"body table-responsive\"><table class=\"table table-condensed\" width='100%' border='1'><tr><td>Designation</td><td>From Date</td><td>To Date</td><td></td></tr>";
                    db.read("SELECT md.desigination,DATE_FORMAT(sp.from_date,'%d-%m-%Y') from_date,IFNULL(DATE_FORMAT(sp.to_date,'%d-%m-%Y'),'Uptodate') to_date,IF(sp.to_date IS NULL,CONCAT('<input type=\"button\" id=\"delpro\" name=\"delpro\"  class=\"btn btn-danger waves-effect\" value=\"Delete\" onClick=\\\"del_promotion1(',sp.sp_id,',',sp.staff_id,',\\'',sp.from_date,'\\')\\\" />'),'') btn FROM camps.staff_promotion sp INNER JOIN camps.master_desigination md ON md.md_id=sp.md_id WHERE sp.staff_id='" + request.getParameter("staff_id") + "' and sp.status in (1,2) ORDER BY sp.from_date DESC");
                    while (db.rs.next()) {
                        
                        data += "<tr><td>" + db.rs.getString("desigination") + "</td><td>" + db.rs.getString("from_date") + "</td><td>" + db.rs.getString("to_date") + "</td><td>" + db.rs.getString("btn") + "</td></tr>";
                    }
                    data += "</table></div>";
                    out.print(data);
                }
                if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("del_promotion1")) {
                    String data = "";
                    db.update("UPDATE camps.staff_promotion sp SET sp.status=0,sp.updated_by='" + session.getAttribute("user_id") + "',sp.updated_date=NOW() WHERE sp.sp_id=" + request.getParameter("sp_id") + " AND sp.to_date IS NULL");
                    db.update("UPDATE camps.staff_promotion sp SET sp.to_date=null,sp.status=2,sp.updated_by='" + session.getAttribute("user_id") + "',sp.updated_date=NOW() WHERE sp.staff_id=" + request.getParameter("staff_id") + " AND sp.to_date='" + request.getParameter("date") + "'-interval 1 day");
                    out.print(db.iEffectedRows > 0 ? 1 : 0);
                }
                
            } catch (Exception e) {
                out.print(e);
            } finally {
                try {
                    db.closeConnection();
                } catch (SQLException ex) {
                    Logger.getLogger(staff_Profile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
