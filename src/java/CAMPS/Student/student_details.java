package CAMPS.Student;

import CAMPS.Common.form_process;
import CAMPS.Common.getRptImage;
import CAMPS.Common.report_process;
import CAMPS.Connect.DBConnect;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

public class student_details extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        DBConnect db = new DBConnect();
        PrintWriter out = response.getWriter();
        try {
            db.getConnection();
            /* TODO output your page here. You may use following sample code. */
            if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadpersonal_det")) {
                report_process rs = new report_process();
                ArrayList<String> attribute = new ArrayList<>();
                if (session.getAttribute("uType").toString().equalsIgnoreCase("Staff")) {
                    attribute.add(request.getParameter("stu_id"));
                } else {
                    attribute.add(session.getAttribute("ss_id").toString());
                }
                out.print(rs.report_v1("4", attribute));
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadparent_det")) {
                report_process rs = new report_process();
                ArrayList<String> attribute = new ArrayList<>();
                if (session.getAttribute("uType").toString().equalsIgnoreCase("Staff")) {
                    attribute.add(request.getParameter("stu_id"));
                } else {
                    attribute.add(session.getAttribute("ss_id").toString());
                }
                out.print(rs.report_v1("5", attribute));
            }else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_educational_det")) {
                report_process rs = new report_process();
                ArrayList<String> attribute = new ArrayList<>();
                if (session.getAttribute("uType").toString().equalsIgnoreCase("Staff")) {
                    attribute.add(request.getParameter("stu_id"));
                } else {
                    attribute.add(session.getAttribute("ss_id").toString());
                }
                out.print(rs.report_v1("19", attribute));
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadpersonal_edit")) {
                form_process fp = new form_process();
                ArrayList<String> attribute = new ArrayList<>();
                attribute.add(request.getParameter("stu_id"));
                out.print("" + fp.form_horizontal_2col_v1("1", attribute) + " ");
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_registration")) {
                form_process fp = new form_process();
                ArrayList<String> attribute = new ArrayList<>();
                out.print("" + fp.form_v1("9", attribute) + " ");
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadparent_edit")) {
                form_process fp = new form_process();
                ArrayList<String> attribute = new ArrayList<>();
                attribute.add(request.getParameter("stu_id"));
                out.print("" + fp.form_grid_v1("2", attribute) + " ");
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadStudent_TC")) {
                report_process rs = new report_process();
                ArrayList<String> attribute = new ArrayList<>();
                attribute.add(request.getParameter("student_status"));
                attribute.add(request.getParameter("term_id"));
                attribute.add(request.getParameter("branch_id"));
                out.print("" + rs.report_v1("11", attribute) + " ");
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadStudent_Promotion")) {
                report_process rs = new report_process();
                ArrayList<String> attribute = new ArrayList<>();
                attribute.add(request.getParameter("term_id"));
                attribute.add(request.getParameter("branch_id"));
                attribute.add(request.getParameter("section"));
                out.print("" + rs.report_v1("12", attribute) + " ");
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadpast_edu")) {
                form_process fp = new form_process();
                ArrayList<String> attribute = new ArrayList<>();
                attribute.add(request.getParameter("stu_id"));
                out.print("" + fp.form_grid_v1("8", attribute) + " ");
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("updatepersonal_edit")) {
                form_process fp = new form_process();
                ArrayList<String> attribute = new ArrayList<>();
                attribute.add(request.getParameter("stu_id"));
                response.sendRedirect("student_det_update.jsp?am_c=" + fp.form_Update_V1(request));
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("edit_grid")) {
                form_process fp = new form_process();
                out.print(fp.form_Grid_Update_V1(request));
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("add_grid")) {
                form_process fp = new form_process();
                out.print(fp.form_Grid_Add_V1(request, session));
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("delete_grid")) {
                form_process fp = new form_process();
                out.print(fp.form_Grid_Delete_V1(request, session));
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadSemeter")) {
                db.read("SELECT CONCAT('<option value=\"',mt.term_id,'\" ', IF(mt.status=1,'selected',''),' >',mt.term_name,'</option>') val FROM camps.master_term mt WHERE mt.ay_id='" + request.getParameter("ay_id") + "' AND mt.status>0");
                out.print("<option value=\"\" disabled>-- Select Section --</option>");
                while (db.rs.next()) {
                    out.print(db.rs.getString("val"));
                }
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadNClass")) {
                db.read("SELECT CONCAT('<option value=\"',IF(mpp.minimum_years!=1,mppd.prog_period_id,mb.branch_id),'\" ',IF(mppd.prog_period_id='" + (Integer.parseInt(request.getParameter("ppid")) + 1) + "','selected',''),' >',IF(mpp.minimum_years!=1,CONCAT(mppd.year,' Yr / ',mppd.period,' Sem'),mb.branch_name),'</option>') val FROM camps.master_branch mb INNER JOIN camps.master_programme_pattern mpp ON mpp.prog_pattern_id=mb.prog_pattern_id INNER JOIN camps.master_programme_period_det mppd ON mppd.prog_pattern_id=mpp.prog_pattern_id WHERE IF(mpp.minimum_years=1,TRUE,mb.branch_id='" + request.getParameter("branch_id") + "')");
                out.print("<option value=\"\" disabled>-- Select Section --</option>");
                while (db.rs.next()) {
                    out.print(db.rs.getString("val")); 
                }
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadfilter_det")) {
                report_process rs = new report_process();
                ArrayList<String> attribute = new ArrayList<>();
                String att = "True ";
                if (request.getParameter("student_id") != null && !request.getParameter("student_id").equals("")) {
                    att += "AND student_id like '%" + request.getParameter("student_id") + "%'";
                }
                if (request.getParameter("old_admission") != null && !request.getParameter("old_admission").equals("")) {
                    att += "AND old_admission_no like '%" + request.getParameter("old_admission") + "%'";
                }
                if (request.getParameter("fname") != null && !request.getParameter("fname").equals("")) {
                    att += "AND fname like '%" + request.getParameter("fname") + "%'";
                }
                if (request.getParameter("sname") != null && !request.getParameter("sname").equals("")) {
                    att += "AND stu_name like '%" + request.getParameter("sname") + "%'";
                }
                if (request.getParameter("dob") != null && !request.getParameter("dob").equals("")) {
                    att += "AND dob = '" + request.getParameter("dob") + "'";
                }
                if (request.getParameter("pincode") != null && !request.getParameter("pincode").equals("")) {
                    att += "AND per_pincode = '" + request.getParameter("pincode") + "'";
                }
                if (request.getParameter("rollno") != null && !request.getParameter("rollno").equals("")) {
                    att += "AND rollno = '" + request.getParameter("rollno") + "'";
                }
                attribute.add(att);
                out.print(rs.report_v1("6", attribute));
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadSection")) {
                db.read("SELECT mppd.prog_period_id,IF(mpp.minimum_years=1,sp.section,CONCAT(mppd.year,' Yr ',mppd.period, ' Sem ' ,sp.section)) section FROM camps.student_promotion sp LEFT JOIN camps.master_programme_period_det mppd ON mppd.prog_period_id=sp.prog_period_id LEFT JOIN camps.master_programme_pattern mpp ON mpp.prog_pattern_id=mppd.prog_pattern_id  WHERE sp.status=1 AND sp.branch_id='" + request.getParameter("branch_id") + "' AND sp.term_id='" + request.getParameter("term_id") + "' GROUP BY mppd.year,mppd.period,sp.section ORDER BY mppd.year,mppd.period,sp.section");
                out.print("<option value=\"\">-- Select Section --</option>");
                while (db.rs.next()) {
                    out.print("<option data-ppid=\"" + db.rs.getString("prog_period_id") + "\" value=\"" + db.rs.getString("section") + "\">" + db.rs.getString("section") + "</option>");
                }
            } else if (request.getParameter("promotion") != null && request.getParameter("promotion").equalsIgnoreCase("SUBMIT")) {
                db.insert("INSERT IGNORE INTO student_promotion(sp_id,term_id,branch_id,student_id,prog_period_id,roll_no,section,STATUS,inserted_by,inserted_date,updated_by,updated_date) (SELECT NULL,'" + request.getParameter("nterm_id") + "',IF(mpp.minimum_years=1,'" + request.getParameter("nbranch_id") + "',sp.branch_id),sm.student_id,IF(mpp.minimum_years=1,sp.prog_period_id,'" + request.getParameter("nbranch_id") + "'),IF(mpp.minimum_years=1,NULL,sp.roll_no),sp.section,1,'" + session.getAttribute("user_id") + "',NOW(),'" + session.getAttribute("user_id") + "',NOW() FROM student_master sm INNER JOIN student_admission_master sam ON sm.student_id=sam.student_id AND sm.student_id IN (" + String.join(",", request.getParameterValues("tc_select")) + ") AND sam.student_status='Continuing' INNER JOIN student_promotion sp ON sp.sp_id=sam.sp_id INNER JOIN master_branch mb ON mb.branch_id=sp.branch_id INNER JOIN master_programme_pattern mpp ON mpp.prog_pattern_id=mb.prog_pattern_id)");
                db.update("UPDATE student_admission_master sam INNER JOIN student_promotion sp ON sam.student_id=sp.student_id AND sp.term_id='" + request.getParameter("nterm_id") + "' AND sp.student_id IN (" + String.join(",", request.getParameterValues("tc_select")) + ") SET sam.sp_id=sp.sp_id,sam.updated_by='" + session.getAttribute("user_id") + "',sam.updated_date=NOW()");
                response.sendRedirect("promotion_process.jsp");
            } else if (request.getParameter("scompleted") != null && request.getParameter("scompleted").equalsIgnoreCase("SUBMIT")) {
                db.update("UPDATE student_admission_master sam INNER JOIN student_promotion sp ON sam.student_id=sp.student_id AND sp.student_id IN (" + String.join(",", request.getParameterValues("tc_select")) + ") SET sam.student_status='" + request.getParameter("sstatus") + "',sam.updated_by='" + session.getAttribute("user_id") + "',sam.updated_date=NOW()");
                response.sendRedirect("promotion_process.jsp");
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_address_edit")) {
                form_process fp = new form_process();
                ArrayList<String> attribute = new ArrayList<>();
                attribute.add(request.getParameter("stu_id"));
                out.print("" + fp.form_horizontal_v1("3", attribute) + " ");
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_branch")) {
                db.read("SELECT bm.branch_id,CONCAT(IFNULL(mp.programme_code,''),' ',bm.branch_name) branch_name FROM camps.master_branch bm INNER JOIN camps.master_programme mp ON mp.programme_id=bm.programme_id  WHERE bm.status=1 AND mp.programme_level in(" + request.getParameter("degree_level") + ")");
                while (db.rs.next()) {
                    out.print("<option value=\"" + db.rs.getString("branch_id") + "\">" + db.rs.getString("branch_name") + "</option>");
                } 
            } else if (request.getParameter("insert_tc_details") != null && request.getParameter("insert_tc_details").equalsIgnoreCase("SUBMIT")) {
                db.insert("INSERT IGNORE INTO camps.student_transfer_certificate(student_id,tc_date,last_class_date,reason_for_leaving,tc_conduct,next_higher_class,description,previous_tc_no,inserted_by,inserted_date,updated_by,updated_date) (SELECT sm.student_id,STR_TO_DATE('" + request.getParameter("tcdate") + "','%d-%m-%Y'),STR_TO_DATE('" + request.getParameter("classdate") + "','%d-%m-%Y'),student_status,'" + request.getParameter("cc") + "','" + request.getParameter("promotion") + "','" + request.getParameter("reason") + "',IF('" + request.getParameter("reason") + "'!='',IFNULL(MAX(stc.stc_id),0),0),'',NOW(),'',NOW() FROM camps.student_master sm INNER JOIN camps.student_admission_master sam ON sm.student_id=sam.student_id AND sam.student_status<>'continuing' LEFT JOIN camps.student_transfer_certificate stc ON stc.student_id=sam.student_id where sam.student_id in (" + String.join(",", request.getParameterValues("tc_select")) + ") GROUP BY sm.student_id)");
                response.sendRedirect("assign_transfer_certificate.jsp");
            } else if (request.getParameter("insert_tc_details") != null && request.getParameter("insert_tc_details").equalsIgnoreCase("SUBMIT123")) {
                String path = getServletContext().getRealPath("/JSP/student/report/Student_tc.jrxml");
                File reportFile = new File(path);
                if (!reportFile.exists()) {
                    throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");
                }
                JasperDesign jasperDesign = JRXmlLoader.load(reportFile.getPath());
                JRDesignQuery newQuery = new JRDesignQuery();
                newQuery.setText("SELECT sm.student_id,TRIM(CONCAT(IFNULL(sm.first_name,''),' ',IFNULL(sm.middle_name,''),' ',IFNULL(sm.last_name,''),' ')) student_name,sp.roll_no,DATE_FORMAT(sam.doa,'%d-%m-%Y') doa,sm.emi_no,sm.gender,DATE_FORMAT(sm.dob,'%d-%m-%Y') dob,DATE_FORMAT(stc.last_class_date,'%d-%m-%Y') last_class_date,DATE_FORMAT(stc.tc_date,'%d-%m-%Y') tc_date,stc.tc_conduct,stc.description,stc.next_higher_class,stc.reason_for_leaving,stc.previous_tc_no,stc.stc_id tc_no,CONCAT(mp.programme_name,' ',mb.branch_name) branch,mb.branch_name,CONCAT(mppd.year,' Year ',mppd.period,' Sem') year_sem,stp.photo,IFNULL(mr.name,'') father_name,mn.nationality_name,mar.religion_name FROM camps.student_master sm INNER JOIN camps.student_transfer_certificate stc ON sm.student_id=stc.student_id  INNER JOIN camps.student_admission_master sam ON sam.student_id=sm.student_id AND   sm.student_id IN (" + String.join(",", request.getParameterValues("tc_select")) + ") INNER JOIN camps.student_promotion sp ON sp.sp_id=sam.sp_id INNER JOIN camps.master_branch mb ON mb.branch_id=sp.branch_id INNER JOIN  camps.master_programme mp ON mp.programme_id=mb.programme_id INNER JOIN camps.master_programme_period_det mppd ON mppd.prog_period_id=sp.prog_period_id LEFT JOIN camps.master_relation mr ON mr.student_id=sm.student_id AND mr.status>0 AND mr.relationship='father' LEFT JOIN camps.master_nationality mn ON mn.nationality_id=sm.nationality_id LEFT JOIN camps.master_religion mar ON mar.religion_id=sm.religion_id  LEFT JOIN camps.student_photo stp ON stp.student_id=sm.student_id");
                jasperDesign.setQuery(newQuery);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                Map<String, Object> parameters = new HashMap<String, Object>();
                DBConnect ob1 = new DBConnect();
                ob1.getConnection();
                parameters.put("REPORT_CONNECTION", ob1.connection);
                DBConnect ob = new DBConnect();
                ob.getConnection();
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ob.connection);
                session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);

                //JasperViewer.viewReport(jasperPrint,false);
                response.sendRedirect("../Welcome/jasper_viewer.jsp");
            } else if (request.getParameter("profile_report") != null && request.getParameter("profile_report").equalsIgnoreCase("student_profile_report")) {
                String student_status[] = request.getParameterValues("student_status");
                String status = "'0'";
                if (request.getParameterValues("student_status") != null) {
                    for (String status1 : student_status) {
                        status += ",'" + status1 + "'";
                    }
                }
                String yoa[] = request.getParameterValues("yoa");
                String year_ad = "'0'";
                if (request.getParameterValues("yoa") != null) {
                    for (String yoa1 : yoa) {
                        year_ad += ",'" + yoa1 + "'";
                    }
                }
                String branch_id[] = request.getParameterValues("branch_id");
                String branch = "'0'";
                if (request.getParameterValues("branch_id") != null) {
                    for (String branch1 : branch_id) {
                        branch += ",'" + branch1 + "'";
                    }
                }
                String path = getServletContext().getRealPath("/JSP/student/report/Student_profile.jrxml");
                File reportFile = new File(path);
                if (!reportFile.exists()) {
                    throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");
                }
                JasperDesign jasperDesign = JRXmlLoader.load(reportFile.getPath());
                JRDesignQuery newQuery = new JRDesignQuery();
                // String s="SELECT sm.student_id, sp.roll_no, sam.old_admission_no, sm.emi_no, sp.section, DATE_FORMAT(sam.doa, '%d-%m-%Y') doa, TRIM( CONCAT( IFNULL(sm.first_name, ''), ' ', IFNULL(sm.middle_name, ''), ' ', IFNULL(sm.last_name, ''), ' ' ) ) stu_name, YEAR(sam.doa) yoa, YEAR(sam.doa) + mpp.minimum_years yoc, DATE_FORMAT(sm.dob, '%d-%m-%Y') dob, sm.gender, sm.scholar, IFNULL(mbg.blood_group, '') blood_group, sm.student_email_id, mmt.mother_tongue, IF( mr.relationship = 'father', mr.name, '' ) father_name, IF( mr.relationship = 'mother', mr.name, '' ) mother_name, IF( mr.relationship = 'guardian', mr.name, '' ) guardian_name, IFNULL( IF( mr.relationship = 'father', mr.occupation, '' ), IF( mr.relationship = 'mother', mr.occupation, '' ) ) occupation, IFNULL( IF( mr.relationship = 'father', mr.income, '' ), IF( mr.relationship = 'mother', mr.income, '' ) ) income, mn.nationality_name, mrn.religion_name, mcy.community_name, mcst.caste_name, sam.student_status, mp.programme_level, mppd.year, mppd.year, mppd.period sem, IF( IF( mr.relationship = 'mother', mr.name, 1 )=1, IF( mr.relationship = 'father', mr.name, '' ),'' ) nominee_name, IF( IF( mr.relationship = 'mother', mr.relationship, 1 )=1, IF( mr.relationship = 'father', mr.relationship, '' ),'' ) nominee_relationship, DATE_FORMAT( FROM_DAYS(TO_DAYS(NOW()) - TO_DAYS(IF( IF( mr.relationship = 'mother', mr.dob, 1 )=1, IF( mr.relationship = 'father', mr.dob, '' ),'' ) )),'%Y' ) + 0 nominee_dob, CONCAT( mp.programme_name, ' ', br.branch_name ) programme_name, CONCAT( mppd.year, ' Year ', mppd.period, ' Sem ', sp.section ) section, DATE_FORMAT(sm.dob, '%d-%m-%Y') dob, IFNULL(sm.per_address_line1, '') paddr1, IFNULL(sm.per_address_line2, '') paddr2, IFNULL(sm.per_address_line3, '') paddr3, IFNULL(sm.per_pincode, '') ppincode, IFNULL(sm.per_address_line1, '') caddr1, IFNULL(sm.per_address_line2, '') caddr2, IFNULL(sm.per_address_line3, '') caddr3, IFNULL(sm.per_pincode, '') cpincode, sph.photo AS img1, sm.gender, IFNULL(mbg.blood_group, '') blood_group, IF( sed.degree_id = 23, sed.specialization, '' ) xth_spec, IF( sed.degree_id = 24, sed.specialization, '' ) xiith_spec, IF(sed.degree_id = 23, sed.medium, '') xth_medium, IF(sed.degree_id = 24, sed.medium, '') xiith_medium,sm.student_mobile_no,IFNULL( IF( mr.relationship = 'father', mr.phone_no, '' ), IF( mr.relationship = 'mother', mr.phone_no, '' ) ) parent_contact, IF( sed.degree_id = 23, sed.secured_mark, '' ) xth_mark, IF( sed.degree_id = 24, sed.secured_mark, '' ) xiith_mark,IF( sed.degree_id = 23, (sed.secured_mark/sed.max_mark)*100, '' ) xth_per,IF( sed.degree_id = 24, (sed.secured_mark/sed.max_mark)*100, '' ) xiith_per,IF( sed.degree_id = 23, sed.mark_sheet_det, '' ) xth_cert,IF( sed.degree_id = 24, mark_sheet_det, '' ) xiith_cert,IF( sed.degree_id = 23, sed.school, '' ) xth_school,IF( sed.degree_id = 24, sed.school, '' ) xiith_school,IF( sed.degree_id = 23, sed.specialization, '' ) xth_spec,IF( sed.degree_id = 24, sed.specialization, '' ) xiith_spec,IF( sed.degree_id = 23, sed.yop, '' ) xth_yop,IF( sed.degree_id = 24, sed.yop, '' ) xiith_yop FROM camps.student_master sm left JOIN camps.student_admission_master sam ON sm.student_id = sam.student_id left JOIN camps.student_promotion sp ON sp.sp_id = sam.sp_id AND sp.status > 0 left JOIN camps.master_term mt ON mt.term_id = sp.term_id left JOIN camps.master_academic_year ay ON ay.ay_id = mt.ay_id left JOIN camps.master_branch br ON br.branch_id = sp.branch_id left JOIN camps.master_programme mp ON mp.programme_id = br.programme_id AND mp.status > 0 LEFT JOIN camps.student_photo sph ON sph.student_id = sm.student_id AND sph.status > 0 LEFT JOIN camps.master_blood_group mbg ON mbg.blood_group_id = sm.blood_group_id LEFT JOIN camps.master_programme_period_det mppd ON mppd.prog_period_id = sp.prog_period_id LEFT JOIN camps.master_programme_pattern mpp ON mpp.prog_pattern_id = mppd.prog_pattern_id LEFT JOIN camps.master_relation mr ON mr.student_id = sm.student_id AND mr.status > 0 LEFT JOIN camps.master_mother_tongue mmt ON mmt.mother_tongue_id = sm.mother_tongue_id LEFT JOIN camps.master_nationality mn ON mn.nationality_id = sm.nationality_id LEFT JOIN camps.master_religion mrn ON mrn.religion_id = sm.religion_id LEFT JOIN camps.master_community mcy ON mcy.community_id = sm.community_id LEFT JOIN camps.master_caste mcst ON mcst.caste_id = sm.caste_id LEFT JOIN camps.student_education_det sed ON sed.student_id = sm.student_id WHERE YEAR(sam.doa) in (" + year_ad + ") AND sam.student_status IN (" + status + ") AND sam.branch_id in (" + branch + ")";
                newQuery.setText("SELECT sm.student_id, sp.roll_no, sam.old_admission_no, sm.emi_no, sp.section, DATE_FORMAT(sam.doa, '%d-%m-%Y') doa, TRIM( CONCAT( IFNULL(sm.first_name, ''), ' ', IFNULL(sm.middle_name, ''), ' ', IFNULL(sm.last_name, ''), ' ' ) ) stu_name, YEAR(sam.doa) yoa, YEAR(sam.doa) + mpp.minimum_years yoc, DATE_FORMAT(sm.dob, '%d-%m-%Y') dob, sm.gender, sm.scholar, IFNULL(mbg.blood_group, '') blood_group, sm.student_email_id, mmt.mother_tongue, GROUP_CONCAT( DISTINCT IF( mr.relationship = 'father', mr.name, '' ) SEPARATOR '') father_name, GROUP_CONCAT( DISTINCT IF( mr.relationship = 'mother', mr.name, '' ) SEPARATOR '') mother_name, GROUP_CONCAT( DISTINCT IF( mr.relationship = 'guardian', mr.name, '' )SEPARATOR '') guardian_name, IFNULL( IF( mr.relationship = 'father', mr.occupation, '' ), IF( mr.relationship = 'mother', mr.occupation, '' ) ) occupation, IFNULL( IF( mr.relationship = 'father', mr.income, '' ), IF( mr.relationship = 'mother', mr.income, '' ) ) income, mn.nationality_name, mrn.religion_name, mcy.community_name, mcst.caste_name, sam.student_status, mp.programme_level, mppd.year, mppd.year, mppd.period sem, IF( IF( mr.relationship = 'mother', mr.name, 1 )=1, IF( mr.relationship = 'father', mr.name, '' ),'' ) nominee_name, IF( IF( mr.relationship = 'mother', mr.relationship, 1 )=1, IF( mr.relationship = 'father', mr.relationship, '' ),'' ) nominee_relationship, DATE_FORMAT( FROM_DAYS(TO_DAYS(NOW()) - TO_DAYS(IF( IF( mr.relationship = 'mother', mr.dob, 1 )=1, IF( mr.relationship = 'father', mr.dob, '' ),'' ) )),'%Y' ) + 0 nominee_dob, CONCAT( mp.programme_name, ' ', br.branch_name ) programme_name, CONCAT( mppd.year, ' Year ', mppd.period, ' Sem ', sp.section ) section, DATE_FORMAT(sm.dob, '%d-%m-%Y') dob, IFNULL(sm.per_address_line1, '') paddr1, IFNULL(sm.per_address_line2, '') paddr2, IFNULL(sm.per_address_line3, '') paddr3, IFNULL(sm.per_pincode, '') ppincode, IFNULL(sm.per_address_line1, '') caddr1, IFNULL(sm.per_address_line2, '') caddr2, IFNULL(sm.per_address_line3, '') caddr3, IFNULL(sm.per_pincode, '') cpincode, sph.photo AS img1, sm.gender, IFNULL(mbg.blood_group, '') blood_group, GROUP_CONCAT( DISTINCT IF( sed.degree_id = 23, sed.specialization, '' ) SEPARATOR '') xth_spec, GROUP_CONCAT( DISTINCT IF( sed.degree_id = 24, sed.specialization, '' )  SEPARATOR '') xiith_spec, GROUP_CONCAT( DISTINCT IF(sed.degree_id = 23, sed.medium, '') SEPARATOR '') xth_medium, GROUP_CONCAT( DISTINCT IF(sed.degree_id = 24, sed.medium, '') SEPARATOR '') xiith_medium,sm.student_mobile_no,GROUP_CONCAT( DISTINCT IFNULL( IF( mr.relationship = 'father', mr.phone_no, '' ), IF( mr.relationship = 'mother', mr.phone_no, '' ) )  SEPARATOR '') parent_contact,GROUP_CONCAT( DISTINCT  IF( sed.degree_id = 23, sed.secured_mark, '' ) SEPARATOR '') xth_mark, GROUP_CONCAT( DISTINCT IF( sed.degree_id = 24, sed.secured_mark, '' )  SEPARATOR '') xiith_mark,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 23, (sed.secured_mark/sed.max_mark)*100, '' )  SEPARATOR '') xth_per,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 24, (sed.secured_mark/sed.max_mark)*100, '' ) SEPARATOR '') xiith_per,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 23, sed.mark_sheet_det, '' ) SEPARATOR '') xth_cert,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 24, mark_sheet_det, '' ) SEPARATOR '') xiith_cert,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 23, sed.school, '' ) SEPARATOR '') xth_school,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 24, sed.school, '' ) SEPARATOR '') xiith_school,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 23, sed.specialization, '' ) SEPARATOR '') xth_spec,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 24, sed.specialization, '' ) SEPARATOR '') xiith_spec,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 23, sed.yop, '' ) SEPARATOR '') xth_yop,GROUP_CONCAT( DISTINCT IF( sed.degree_id = 24, sed.yop, '' ) SEPARATOR '') xiith_yop FROM camps.student_master sm LEFT JOIN camps.student_admission_master sam ON sm.student_id = sam.student_id LEFT JOIN camps.student_promotion sp ON sp.sp_id = sam.sp_id AND sp.status > 0 LEFT JOIN camps.master_term mt ON mt.term_id = sp.term_id LEFT JOIN camps.master_academic_year ay ON ay.ay_id = mt.ay_id LEFT JOIN camps.master_branch br ON br.branch_id = sp.branch_id LEFT JOIN camps.master_programme mp ON mp.programme_id = br.programme_id AND mp.status > 0 LEFT JOIN camps.student_photo sph ON sph.student_id = sm.student_id AND sph.status > 0 LEFT JOIN camps.master_blood_group mbg ON mbg.blood_group_id = sm.blood_group_id LEFT JOIN camps.master_programme_period_det mppd ON mppd.prog_period_id = sp.prog_period_id LEFT JOIN camps.master_programme_pattern mpp ON mpp.prog_pattern_id = mppd.prog_pattern_id LEFT JOIN camps.master_relation mr ON mr.student_id = sm.student_id AND mr.status > 0 LEFT JOIN camps.master_mother_tongue mmt ON mmt.mother_tongue_id = sm.mother_tongue_id LEFT JOIN camps.master_nationality mn ON mn.nationality_id = sm.nationality_id LEFT JOIN camps.master_religion mrn ON mrn.religion_id = sm.religion_id LEFT JOIN camps.master_community mcy ON mcy.community_id = sm.community_id LEFT JOIN camps.master_caste mcst ON mcst.caste_id = sm.caste_id LEFT JOIN camps.student_education_det sed ON sed.student_id = sm.student_id and sed.status>0  WHERE YEAR(sam.doa) in (" + year_ad + ") AND sam.student_status IN (" + status + ") AND sam.branch_id in (" + branch + ") group by sm.student_id");
                jasperDesign.setQuery(newQuery);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                Map<String, Object> parameters = new HashMap<String, Object>();
                DBConnect ob1 = new DBConnect();
                ob1.getConnection();
                parameters.put("REPORT_CONNECTION", ob1.connection);
                DBConnect ob = new DBConnect();
                ob.getConnection();
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ob.connection);
                session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);

                //JasperViewer.viewReport(jasperPrint,false);
                response.sendRedirect("../Welcome/jasper_viewer.jsp");
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("insert_student_det")) {
                form_process fp = new form_process();
                response.sendRedirect("student_registration.jsp?student_id=" + fp.form_Insert_V1(request, session));
            } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_sem")) {
                db.read("SELECT mppd.prog_period_id,mppd.period_name FROM camps.master_branch mb INNER JOIN camps.master_programme_period_det mppd ON mppd.prog_pattern_id=mb.prog_pattern_id WHERE mb.branch_id=" + request.getParameter("branch_id"));
                while (db.rs.next()) {
                    out.print("<option value=\"" + db.rs.getString("prog_period_id") + "\">" + db.rs.getString("period_name") + "</option>");
                }
            }
            if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_bonafide_det")) {
                String data = "";
                db.read("SELECT sm.emi_no, sp.roll_no, sm.student_id, TRIM( CONCAT( IFNULL(sm.first_name, ''), ' ', IFNULL(sm.middle_name, ''), ' ', IFNULL(sm.last_name, ''), ' ' ) ) stu_name, sm.scholar, IFNULL(hm.hostel_name,'') hostel_name, IFNULL(hrm.room_no,'') room_no FROM camps.student_master sm INNER JOIN camps.student_admission_master sam ON sm.student_id = sam.student_id AND sam.student_status LIKE 'contin%' INNER JOIN camps.student_promotion sp ON sam.sp_id = sp.sp_id INNER JOIN camps.master_term mt ON mt.term_id = sp.term_id INNER JOIN camps.master_academic_year ay ON ay.ay_id = mt.ay_id LEFT JOIN camps.hostel_room_occupancy hro ON hro.student_id = sm.student_id LEFT JOIN camps.hostel_room_master hrm ON hrm.room_id = hro.room_id LEFT JOIN camps.hostel_master hm ON hm.hostel_id = hrm.hostel_id WHERE sm.student_id=" + request.getParameter("stu_id"));
                if (db.rs.next()) {

                    data += "<div class=\"row clearfix\"><div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12\">"
                            + "<div class=\"form-group form-float\">"
                            + "<div class=\"form-line focused\">"
                            + "" + db.rs.getString("roll_no") + ""
                            + "<label class=\"form-label\">Roll No</label>"
                            + "</div>"
                            + "</div></div></div>"
                            + "<div class=\"row clearfix\"><div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12\">"
                            + "<div class=\"form-group form-float\">"
                            + "<div class=\"form-line focused\">"
                            + "" + db.rs.getString("stu_name") + ""
                            + "<label class=\"form-label\">Student Name</label>"
                            + "</div>"
                            + "</div></div></div>"
                            + "<div class=\"row clearfix\"><div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12\">"
                            + "<div class=\"form-group form-float\">"
                            + "<div class=\"form-line focused\">"
                            + "<textarea name=\"txt_bon_purpose\" id=\"txt_bon_purpose\"  class=\"form-control\"  /></textarea>"
                            + "<label class=\"form-label\">Purpose (** to be print in the certificate)</label>"
                            + "</div>"
                            + "</div></div></div>"
                            + "<input type=\"hidden\" name=\"student_id\" id=\"student_id\" value=\"" + db.rs.getString("student_id") + "\" class=\"form-control\"  />"
                            + "<div><center><button class=\"btn bg-orange waves-effect\" type=\"submit\" value=\"submit\" name=\"submit_bonafide\" >Print</button></center></div>    ";

                }
                out.print(data);
            }
            if (request.getParameter("submit_bonafide") != null && request.getParameter("submit_bonafide").equalsIgnoreCase("submit")) {
                String bon_id = db.insertAndGetAutoGenId("INSERT INTO camps.bonafide_certificates VALUES (NULL," + request.getParameter("student_id") + ",'" + request.getParameter("txt_bon_purpose") + "',NOW(),'" + session.getAttribute("user_id") + "',NOW(),'" + session.getAttribute("user_id") + "')");
                String path = getServletContext().getRealPath("/JSP/student/report/student_bonafide.jrxml");
                File reportFile = new File(path);
                if (!reportFile.exists()) {
                    throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");
                }
                JasperDesign jasperDesign = JRXmlLoader.load(reportFile.getPath());
                JRDesignQuery newQuery = new JRDesignQuery();
                newQuery.setText("SELECT sm.student_id enroll_no, IFNULL(sp.roll_no, '') roll_no, ay.acc_year, CONCAT( IF(sm.gender = 'Male', 'Mr.', 'Ms.'), TRIM( CONCAT( IFNULL(sm.first_name, ''), ' ', IFNULL(sm.middle_name, ''), ' ', IFNULL(sm.last_name, ''), ' ' ) ) ) student_name, TRIM(parents_name (sm.student_id)) AS parents_name, UPPER(mn.nationality_name) nationality_name, DATE_FORMAT(sm.dob, '%d-%m-%Y') dob, (SELECT CASE mppd.year WHEN 'I' THEN 'first' WHEN 'II' THEN 'second' WHEN 'III' THEN 'third' WHEN 'IV' THEN 'fourth' WHEN 'V' THEN 'fifth' END) year_of_study, (SELECT CASE sm.gender WHEN 'MALE' THEN 'He' WHEN 'FEMALE' THEN 'She' END) gender, IFNULL(mp.programme_name, '') course_code, IFNULL(mb.branch_name, '') branch_name, (SELECT CASE sm.gender WHEN 'MALE' THEN 'him' WHEN 'FEMALE' THEN 'her' END) gender1, (SELECT CASE sm.gender WHEN 'MALE' THEN 'His' WHEN 'FEMALE' THEN 'Her' END) gender2, sm.scholar, IFNULL(bc.purpose, 'NIL') purpose,hrm.room_no,hm.hostel_name FROM camps.student_master sm INNER JOIN camps.student_admission_master sam ON sm.student_id = sam.student_id AND sam.student_status LIKE 'contin%' INNER JOIN camps.student_promotion sp ON sam.sp_id = sp.sp_id INNER JOIN camps.master_term mt ON mt.term_id = sp.term_id INNER JOIN camps.master_academic_year ay ON ay.ay_id = mt.ay_id LEFT JOIN camps.hostel_room_occupancy hro ON hro.student_id = sm.student_id AND hro.status=1 LEFT JOIN camps.hostel_room_master hrm ON hrm.room_id = hro.room_id LEFT JOIN camps.hostel_master hm ON hm.hostel_id = hrm.hostel_id LEFT JOIN camps.master_nationality mn ON mn.nationality_id = sm.nationality_id INNER JOIN camps.master_programme_period_det mppd ON mppd.prog_period_id = sp.prog_period_id LEFT JOIN camps.bonafide_certificates bc ON bc.student_id = sm.student_id AND bc.bc_id = '" + bon_id + "' LEFT JOIN camps.master_branch mb ON mb.branch_id = sp.branch_id LEFT JOIN camps.master_programme mp ON mp.programme_id = mb.programme_id WHERE sm.student_id = '" + request.getParameter("student_id") + "'");
                jasperDesign.setQuery(newQuery);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                Map<String, Object> parameters = new HashMap<String, Object>();
                DBConnect ob1 = new DBConnect();
                ob1.getConnection();
                parameters.put("REPORT_CONNECTION", ob1.connection);
                parameters.put("header", getRptImage.getImage("3"));
                parameters.put("head", "1");
                DBConnect ob = new DBConnect();
                ob.getConnection();
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ob.connection);
                session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);

                //JasperViewer.viewReport(jasperPrint,false);
                response.sendRedirect("../Welcome/jasper_viewer.jsp");
            }

        } catch (Exception e) {
            out.print(e);
        } finally {
            db.closeConnection();
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
        try {
            //processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(student_details.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(student_details.class.getName()).log(Level.SEVERE, null, ex);
        }
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
