
package CAMPS.Staff;

import CAMPS.Common.report_process;
import CAMPS.Connect.DBConnect;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class leave_Details extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        DBConnect db = new DBConnect();
        try (PrintWriter out = response.getWriter()) {
            try {
                db.getConnection();
                if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadleave_det")) {
                    db.insert("INSERT INTO camps.staff_leave_count (SELECT a.* FROM (SELECT sm.staff_id,slm.slm_id,CASE WHEN slm.reset_period='Yearly' THEN IF(slc.effective_date IS NULL,CONCAT(YEAR(NOW()),'-06-01'),slc.effective_date) WHEN slm.reset_period='Monthly' THEN  IF(slc.effective_date IS NULL,CONCAT(YEAR(NOW()),'-',MONTH(NOW()),'-01'),slc.effective_date) END f,CASE WHEN slm.reset_period='Yearly' THEN IF(slc.effective_date IS NULL,CONCAT(YEAR(NOW() + Interval 1 Year ),'-05-31') ,slc.effective_date+ INTERVAL 12 MONTH - INTERVAL 1 DAY) WHEN slm.reset_period='Monthly' THEN  IF(slc.effective_date IS NULL,LAST_DAY(NOW()),slc.effective_date+ INTERVAL 12 MONTH - INTERVAL 1 DAY) END t,CASE WHEN slc.status=2 AND TIMESTAMPDIFF(YEAR,sm.doj,DATE(NOW()))>=1 THEN slc.leave_count WHEN slc.status=2 AND TIMESTAMPDIFF(YEAR,sm.doj,DATE(IF(slc.effective_date IS NULL,NOW(),slc.effective_date)))<=1 THEN slc.leave_count- TIMESTAMPDIFF(MONTH,CONCAT(YEAR(IF(slc.effective_date IS NULL,NOW(),slc.effective_date)),'-06-01'),sm.doj) ELSE slc.leave_count END da,CASE WHEN slm.reset_period='Yearly' THEN YEAR(IF(slc.effective_date IS NULL,NOW(),slc.effective_date)) WHEN slm.reset_period='Monthly' THEN DATE_FORMAT(IF(slc.effective_date IS NULL,NOW(),slc.effective_date),'%M-%Y')  END period,1,'" + session.getAttribute("user_id") + "' i,NOW() it,'" + session.getAttribute("user_id") + "' u,NOW() ut FROM camps.staff_leave_master slm INNER JOIN camps.staff_leave_condition slc ON slm.slm_id=slc.slm_id AND IF(slc.effective_date IS NULL,TRUE,slc.effective_date<=DATE(NOW()))  INNER JOIN camps.staff_master sm ON sm.staff_id='" + session.getAttribute("ss_id") + "' AND CASE WHEN slm.gender='Male' THEN sm.gender='Male' WHEN slm.gender='Female' THEN sm.gender='Female' ELSE TRUE END)a LEFT JOIN camps.staff_leave_count slct ON slct.slm_id=a.slm_id AND slct.staff_id=a.staff_id AND slct.period_name=a.period WHERE slct.slm_id IS NULL)");
                    db.read("SELECT slm.slm_id,slm.leave_name,slc.leave_count,COUNT(count_data.slm_id)*CASE WHEN slm.leave_count=1 THEN 0.5 WHEN slm.leave_count=2 THEN 1 END leave_avalied,(slc.leave_count-COUNT(count_data.slm_id)*CASE WHEN slm.leave_count=1 THEN 0.5 WHEN slm.leave_count=2 THEN 1 END) balance,ifnull(slm.count_query,'')count_query FROM staff_leave_master slm INNER JOIN staff_leave_count slc ON ((slc.slm_id=slm.slm_id AND DATE(NOW()) BETWEEN slc.from_date AND slc.to_date  AND slc.status>0) OR slm.count_query IS NOT NULL) AND slm.status>0 LEFT JOIN (SELECT slr.slm_id,slr.from_date,slr.from_session,slr.to_date,slr.to_session,ct.dt,ses FROM camps.calendar_table ct JOIN (SELECT 'FN' ses,1 orn UNION SELECT 'AN',2)ses INNER JOIN staff_leave_request slr ON ct.dt BETWEEN DATE(slr.from_date) AND DATE(slr.to_date) AND slr.status in (1,2) AND slr.staff_id='" + session.getAttribute("ss_id") + "' AND (CASE WHEN DATE(slr.from_date)=ct.dt AND slr.from_session='AN' THEN slr.from_session=ses WHEN DATE(slr.to_date)=ct.dt AND slr.to_session='FN' THEN slr.to_session=ses ELSE TRUE END) ORDER BY ct.dt,orn) count_data ON count_data.dt BETWEEN slc.from_date AND slc.to_date AND slm.slm_id=count_data.slm_id where slc.staff_id='" + session.getAttribute("ss_id") + "' GROUP BY slm.slm_id");
                    String data = "<div class=\"body table-responsive\"><table width='100%' border='1' class=\"table table-condensed\" ><tr><th>Leave Type</th><th>Leave Authorized</th><th>Leave Availed</th><th>Balance</th></tr>";
                    while (db.rs.next()) {
                        if (db.rs.getString("count_query").equalsIgnoreCase("")) {
                            data += "<tr><td>" + db.rs.getString("leave_name") + "</td><td>" + db.rs.getString("leave_count") + "</td><td>" + db.rs.getString("leave_avalied") + "</td><td>" + db.rs.getString("balance") + "</td></tr>";
                        } else {
                            db.read1(db.rs.getString("count_query").replaceAll("__staff_id__", session.getAttribute("ss_id").toString()));
                            if (db.rs1.next()) {
                                data += "<tr><td>" + db.rs.getString("leave_name") + "</td><td>" + db.rs1.getString("leave_count") + "</td><td>-</td></tr>";
                            }
                        }
                    }
                    data += "</table></div>";
                    out.print(data);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_leave_from_to")) {
                    db.read("SELECT slm.slm_id,slm.leave_name,slm.count_query,slc.leave_count,IFNULL(slm.form_model,'default') form_model,COUNT(count_data.slm_id)*0.5 leave_avalied,IF(slc.leave_count IS NULL,7, slc.leave_count-COUNT(count_data.slm_id)*0.5) remaining  FROM staff_leave_master slm LEFT JOIN staff_leave_count slc ON slc.slm_id=slm.slm_id AND DATE(NOW()) BETWEEN slc.from_date AND slc.to_date AND slm.status>0 AND slc.status>0 LEFT JOIN (SELECT slr.slm_id,slr.from_date,slr.from_session,slr.to_date,slr.to_session,ct.dt,ses FROM camps.calendar_table ct JOIN (SELECT 'FN' ses,1 orn UNION SELECT 'AN',2)ses INNER JOIN staff_leave_request slr ON ct.dt BETWEEN DATE(slr.from_date) AND DATE(slr.to_date) AND slr.status=2 AND slr.staff_id='" + session.getAttribute("ss_id") + "' AND (CASE WHEN DATE(slr.from_date)=ct.dt AND slr.from_session='AN' THEN slr.from_session=ses WHEN DATE(slr.to_date)=ct.dt AND slr.to_session='FN' THEN slr.to_session=ses ELSE TRUE END) ORDER BY ct.dt,orn) count_data ON count_data.dt BETWEEN slc.from_date AND slc.to_date AND slm.slm_id=count_data.slm_id WHERE (slc.staff_id='" + session.getAttribute("ss_id") + "' OR slm.reset_period IS NULL) AND slm.slm_id='" + request.getParameter("leave_type") + "' GROUP BY slm.slm_id HAVING (leave_count-leave_avalied)>0 OR leave_count IS NULL");
                    String data = "";
                    if (db.rs.next()) {
                        switch (db.rs.getString("form_model")) {
                            case "one_day":
                                data = "<div class=\"row clearfix\"><div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"from_date\" id=\"from_date\"  class=\"form-control\"  onchange='load_alter(),nodays(1)'/>"
                                        + "<label class=\"form-label\">Date</label>"
                                        + "<script>$('#from_date').bootstrapMaterialDatePicker({format: 'DD-MM-YYYY', weekStart: 0, time: false});</script></div>"
                                        + "</div></div></div></div>";
                                data += "<div class=\"row clearfix\"><div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<input type=\"text\"  name=\"Reason\" id=\"Reason\"  class=\"form-control\" />"
                                        + "<label class=\"form-label\">Reason</label>"
                                        + "</div>"
                                        + "</div></div></div>";

                                break;
                            case "one_session":
                                data = "<div class=\"row clearfix\"><div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-6\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"from_date\" id=\"from_date\"  class=\"form-control\"  onchange='load_alter(),nodays(1)'/>"
                                        + "<label class=\"form-label\">Date</label>"
                                        + "<script>$('#from_date').bootstrapMaterialDatePicker({format: 'DD-MM-YYYY', weekStart: 0, time: false});</script></div>"
                                        + "</div></div>";
                                data += "<div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-6\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<select id='from_ses' name='from_ses' class=\"form-control\" onchange='load_alter(),nodays(1)'><option value=''><--Select--></option> <option value='FN'>FN</option><option value='AN'>AN</option></select> "
                                        + "<label class=\"form-label\">Session</label>"
                                        + "</div>"
                                        + "</div></div></div>";
                                data += "<div class=\"row clearfix\"><div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<input type=\"text\"  name=\"Reason\" id=\"Reason\"  class=\"form-control\" />"
                                        + "<label class=\"form-label\">Reason</label>"
                                        + "</div>"
                                        + "</div></div></div>";
                                break;
                            default:
                                data = "<div class=\"row clearfix\"><div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-6\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"from_date\" id=\"from_date\"  class=\"form-control\"  onchange=\"load_alter(),nodays(1)\"/>"
                                        + "<label class=\"form-label\">From Date</label>"
                                        + "<script>$('#from_date').bootstrapMaterialDatePicker({format: 'DD-MM-YYYY', weekStart: 0, time: false});</script></div>"
                                        + "</div></div>";
                                data += "<div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-6\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<select id='from_ses' name='from_ses' class=\"form-control\"  onchange=\"load_alter(),nodays(1)\"><option value=''><--Select--></option> <option value='FN'>FN</option><option value='AN'>AN</option></select> "
                                        + "<label class=\"form-label\">From Session</label>"
                                        + "</div>"
                                        + "</div></div></div>";
                                data += "<div class=\"row clearfix\"><div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-6\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"to_date\" id=\"to_date\"  class=\"form-control\"  onchange=\"load_alter(),nodays(1)\" />"
                                        + "<label class=\"form-label\">To Date</label>"
                                        + "<script>$('#to_date').bootstrapMaterialDatePicker({format: 'DD-MM-YYYY', weekStart: 0, time: false});</script></div>"
                                        + "</div></div>";
                                data += "<div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-6 \">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused \">"
                                        + "<select id='to_ses' name='to_ses' class=\"form-control\"  onchange=\"load_alter(),nodays(1)\"><option value=''><--Select--></option> <option value='FN'>FN</option><option value='AN'>AN</option></select>"
                                        + "<label class=\"form-label\">To Session</label>"
                                        + "</div>"
                                        + "</div></div></div>";
                                data += "<div class=\"row clearfix\"><div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<input type=\"text\" name=\"Reason\" id=\"Reason\"  class=\"form-control\"  />"
                                        + "<label class=\"form-label\">Reason</label>"
                                        + "</div>"
                                        + "</div></div></div>";
                                data += "<div class=\"row clearfix\"><div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12\">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused\">"
                                        + "<input type=\"text\"  name=\"days1\" id=\"days1\"  class=\"form-control\" onclick='nodays(1)' readonly />"
                                        + "<label class=\"form-label\">Days</label>"
                                        + "</div>"
                                        + "</div></div></div>";
                        }
                        if (db.rs.getString("count_query") != null) {
                            db.read1(db.rs.getString("count_query").replaceAll("__staff_id__", session.getAttribute("ss_id").toString()));
                            if (db.rs1.next()) {
                                data += "<div class=\"row clearfix\"><div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12 \">"
                                        + "<div class=\"form-group form-float\">"
                                        + "<div class=\"form-line focused \">"
                                        + "<select id='reference' name='reference' class=\"form-control\">" + db.rs1.getString("opt") + "</select>"
                                        + "<label class=\"form-label\">Refered From</label>"
                                        + "</div>"
                                        + "</div></div></div>";
                            }
                        }

                        data += "<div class=\"col-xs-15 ol-sm-15 col-md-15 col-lg-15\">\n"
                                + "                <div class=\"panel-group full-body\" id=\"accordion_21\" role=\"tablist\" aria-multiselectable=\"true\">\n"
                                + "                    <div class=\"panel panel-col-cyan\">\n"
                                + "                        <div class=\"panel-heading\" role=\"tab\" id=\"headingOne_21\">\n"
                                + "                            <h4 class=\"panel-title\">\n"
                                + "                                <a role=\"button\" data-toggle=\"collapse\" href=\"#collapseOne_21\" aria-expanded=\"true\" aria-controls=\"collapseOne_21\">\n"
                                + "                                    <i class=\"material-icons\">view_comfy</i> Alter details </a>\n"
                                + "                            </h4>\n"
                                + "                        </div>\n"
                                + "                        <div id=\"collapseOne_21\" class=\"panel-collapse collapse in\"  role=\"tabpanel\" aria-labelledby=\"headingOne_21\">\n"
                                + "                            <div class=\"panel-body\" id='load_alter_status'>\n"
                                + "\n"
                                + "                            </div>\n"
                                + "                        </div>\n"
                                + "                    </div>\n"
                                + "                </div>\n"
                                + "            </div>";
                        data += "<div><center><button class=\"btn bg-orange waves-effect\" type=\"button\" value=\"submit\" name=\"submit_leave\" onclick='nodays(2)'>Submit</button></center></div>    ";
                        // data += "<div><center><button class=\"btn bg-orange waves-effect\" type=\"button\" value=\"submit\" name=\"submit_leave\" onclick='submit_leave1()'>Submit</button></center></div>    ";
                    }
                    out.print(data);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_leave_status")) {
                    db.read("SELECT  slr.slr_id,slm.leave_name,CONCAT(DATE_FORMAT(slr.from_date,'%d-%m-%Y'),'-',slr.from_session) from_date,CONCAT(DATE_FORMAT(slr.to_date,'%d-%m-%Y'),'-',slr.to_session) to_date,GROUP_CONCAT(IF(slra.status=1,CONCAT('<td bgcolor=\" #908f78 \">',itm.incharge_name,' approval Pending</td>'),IF(slra.status=2,CONCAT('<td bgcolor=\" #76a77e \">',itm.incharge_name,' approved</td>'),CONCAT('<td bgcolor=\"#fd8e76 \">',itm.incharge_name,' declined</td>'))) ORDER BY slm.leave_name,slaf.level SEPARATOR '') leave_status FROM staff_leave_request slr INNER JOIN camps.staff_leave_master slm ON slr.slm_id=slm.slm_id  AND slr.status in (1,3) INNER JOIN staff_leave_request_approval slra ON slra.slr_id=slr.slr_id  INNER JOIN staff_leave_approval_flow slaf ON slaf.slaf_id=slra.slaf_id  INNER JOIN incharge_type_master itm ON itm.itm_id=slaf.itm_id WHERE slr.staff_id='" + session.getAttribute("ss_id") + "' GROUP BY slr.slr_id ORDER BY slm.leave_name,slaf.level");
                    String data = "<div class=\"body table-responsive\"><table class=\"table table-condensed\" width='100%' border='1'><tr><th>Leave Type</th><th>Leave From</th><th>Leave To</th><th>Alter Acceptance</th></tr>";
                    while (db.rs.next()) {
                        data += "<tr><td>" + db.rs.getString("leave_name") + "</td><td>" + db.rs.getString("from_date") + "</td><td>" + db.rs.getString("to_date") + "</td><td> <div class=\"col-xs-12 col-sm-6 col-md-6 col-lg-6\">\n"
                                + "                                   <button type=\"button\" class=\"btn bg-green waves-effect\" data-toggle=\"modal\" data-target=\"#largeModal\"  onclick='showDetails(" + db.rs.getString("slr.slr_id") + ")'><i class=\"material-icons\">verified_user</i>Click to View</button>"
                                + "                                </div></td>" + db.rs.getString("leave_status") + "<td><button type=\"button\" class=\"btn btn-danger waves-effect\" onclick='cancel_leave(" + db.rs.getString("slr.slr_id") + ")'>Cancel</button></td></tr>";
                    }
                    data += "</table></div>";
                    out.print(data);

                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_staff_alter")) {
                    String query = "",ay_id="";
                    db.read1("SELECT department_id,dept_code FROM `master_department` WHERE sc_id IN (1,2)");
                    String dept_str = "<option value=''>--select--</option>";
                    while (db.rs1.next()) {
                        dept_str += "<option value='" + db.rs1.getInt("department_id") + "' >" + db.rs1.getString("dept_code") + "</option>";
                    }
                    db.read1("SELECT slm.form_model,slm.alter_visibility FROM camps.staff_leave_master slm WHERE slm.slm_id=" + request.getParameter("leave_type"));
                    db.read2("SELECT * FROM master_academic_year a WHERE a.cur_year=1");
                    if(db.rs2.next()){
                        ay_id=db.rs2.getString("ay_id");
                    }
                    if (db.rs1.next()) {
                        if (db.rs1.getString("form_model") != null && db.rs1.getString("form_model").equalsIgnoreCase("one_session") && db.rs1.getString("alter_visibility").equalsIgnoreCase("true")) {
                            db.read("SELECT ttm.ttm_id,DATE_FORMAT(ct.dt, '%d-%m-%Y') AS dt, dm.real_day, dm.day_name,concat(htm.start_time,'-',htm.end_time) as hour, htm.htm_id, ssa.ssa_id, concat(IF( ssa.csm_id IS NOT NULL, IFNULL(sm.dis_code, sm.sub_code), nsm.sub_code ) ,'-' ,IF( ssa.csm_id IS NOT NULL, sm.sub_name, nsm.sub_name )) sub_name, dm.dm_id, dom.dm_id FROM studacad.subject_staff_allocation ssa INNER JOIN studacad.time_table_master_"+ay_id+" ttm ON ttm.ssa_id = ssa.ssa_id AND ttm.status > 0 INNER JOIN studacad.hour_timing_master htm ON htm.htm_id = ttm.htm_id AND htm.status > 0 AND htm.htm_id IN (SELECT a.htm_id FROM studacad.hour_timing_master a WHERE  a.status=1 AND a.actual_hour=IF('FN'='" + request.getParameter("from_ses") + "',1,6)) INNER JOIN studacad.day_master dm ON dm.dm_id = ttm.dm_id AND dm.status > 0 LEFT JOIN studacad.day_order_mapping dom ON dom.dm_id = dm.dm_id and dom.status>0 LEFT JOIN ( studacad.core_subject_master csm LEFT OUTER JOIN curriculum.subject_master sm ON sm.subject_id = csm.subject_id ) ON ssa.csm_id = csm.csm_id AND NOW() BETWEEN csm.start_date AND csm.end_date  LEFT JOIN studacad.noncore_subject_master nsm ON nsm.nsm_id = ssa.nsm_id AND nsm.status > 0 AND NOW() BETWEEN nsm.start_date AND nsm.end_date  LEFT JOIN camps.calendar_table ct ON IF( dom.doc IS NOT NULL and dom.doc = ct.dt, dom.doc = ct.dt, dm.day_name = ct.dayName)  WHERE ssa.staff_id = '" + session.getAttribute("ss_id") + "' AND (csm.csm_id is not NULL or nsm.nsm_id is not NULL) AND ct.dt BETWEEN IF( STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') < DATE(NOW()), DATE(NOW()), STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') ) AND STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND CASE WHEN ct.dt = STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND 'AN' = '" + request.getParameter("from_ses") + "' THEN htm.session = 'AN' WHEN ct.dt = STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND 'FN' = '" + request.getParameter("from_ses") + "' THEN htm.session = 'FN' END AND ssa.status > 0 ORDER BY dt,htm.htm_id");
                        } else if (db.rs1.getString("form_model") != null && db.rs1.getString("form_model").equalsIgnoreCase("time") && db.rs1.getString("alter_visibility").equalsIgnoreCase("true")) {
                         //   db.read("SELECT ttm.ttm_id,DATE_FORMAT(ct.dt, '%d-%m-%Y') AS dt, dm.real_day, dm.day_name,concat(htm.start_time,'-',htm.end_time) as hour, htm.htm_id, ssa.ssa_id, concat(IF( ssa.csm_id IS NOT NULL, IFNULL(sm.dis_code, sm.sub_code), nsm.sub_code ) ,'-' ,IF( ssa.csm_id IS NOT NULL, sm.sub_name, nsm.sub_name )) sub_name, dm.dm_id, dom.dm_id FROM studacad.subject_staff_allocation ssa INNER JOIN studacad.time_table_master_21 ttm ON ttm.ssa_id = ssa.ssa_id AND ttm.status > 0 INNER JOIN studacad.hour_timing_master htm ON htm.htm_id = ttm.htm_id AND htm.status > 0 AND htm.htm_id IN (SELECT a.htm_id FROM studacad.hour_timing_master a WHERE a.start_time BETWEEN DATE_FORMAT(STR_TO_DATE('" + request.getParameter("from_date") + "','%d-%m-%Y %H:%i:%s'),'%H:%i:%s') AND DATE_FORMAT(STR_TO_DATE('" + request.getParameter("from_date") + "','%d-%m-%Y %H:%i:%s'),'%H:%i:%s') OR a.end_time BETWEEN DATE_FORMAT(STR_TO_DATE('" + request.getParameter("from_date") + "','%d-%m-%Y %H:%i:%s'),'%H:%i:%s') AND DATE_FORMAT(STR_TO_DATE('" + request.getParameter("from_date") + "','%d-%m-%Y %H:%i:%s'),'%H:%i:%s')) INNER JOIN studacad.day_master dm ON dm.dm_id = ttm.dm_id AND dm.status > 0 LEFT JOIN studacad.day_order_mapping dom ON dom.dm_id = dm.dm_id LEFT JOIN ( studacad.core_subject_master csm LEFT OUTER JOIN curriculum.subject_master sm ON sm.subject_id = csm.subject_id ) ON ssa.csm_id = csm.csm_id LEFT JOIN studacad.noncore_subject_master nsm ON nsm.nsm_id = ssa.nsm_id AND nsm.status > 0 LEFT JOIN camps.calendar_table ct ON IF( dom.doc IS NOT NULL and dom.doc = ct.dt, dom.doc = ct.dt, dm.day_name = ct.dayName)  WHERE ssa.staff_id = '" + session.getAttribute("ss_id") + "' AND ct.dt BETWEEN IF( STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') < DATE(NOW()), DATE(NOW()), STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') ) AND STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND CASE WHEN ct.dt = STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND 'AN' = '" + request.getParameter("from_ses") + "' THEN htm.session = 'AN' WHEN ct.dt = STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND 'FN' = '" + request.getParameter("from_ses") + "' THEN htm.session = 'FN' END AND ssa.status > 0 ORDER BY dt,htm.htm_id");
                        } else if (db.rs1.getString("alter_visibility").equalsIgnoreCase("true")) {
                            db.read("SELECT ttm.ttm_id,DATE_FORMAT(ct.dt, '%d-%m-%Y') AS dt, dm.real_day, dm.day_name,concat(htm.start_time,'-',htm.end_time) as hour, htm.htm_id, ssa.ssa_id, concat(IF( ssa.csm_id IS NOT NULL, IFNULL(sm.dis_code, sm.sub_code), nsm.sub_code ) ,'-' ,IF( ssa.csm_id IS NOT NULL, sm.sub_name, nsm.sub_name )) sub_name, dm.dm_id, dom.dm_id FROM studacad.subject_staff_allocation ssa INNER JOIN studacad.time_table_master_"+ay_id+" ttm ON ttm.ssa_id = ssa.ssa_id AND ttm.status > 0 INNER JOIN studacad.hour_timing_master htm ON htm.htm_id = ttm.htm_id AND htm.status > 0 INNER JOIN studacad.day_master dm ON dm.dm_id = ttm.dm_id AND dm.status > 0 LEFT JOIN studacad.day_order_mapping dom ON dom.dm_id = dm.dm_id and dom.status>0 LEFT JOIN ( studacad.core_subject_master csm LEFT OUTER JOIN curriculum.subject_master sm ON sm.subject_id = csm.subject_id ) ON ssa.csm_id = csm.csm_id AND NOW() BETWEEN csm.start_date AND csm.end_date  LEFT JOIN studacad.noncore_subject_master nsm ON nsm.nsm_id = ssa.nsm_id AND nsm.status > 0 AND NOW() BETWEEN nsm.start_date AND nsm.end_date  LEFT JOIN camps.calendar_table ct ON IF( dom.doc IS NOT NULL and dom.doc = ct.dt, dom.doc = ct.dt, dm.day_name = ct.dayName ) WHERE ssa.staff_id = '" + session.getAttribute("ss_id") + "' AND (csm.csm_id is not NULL or nsm.nsm_id is not NULL) AND ct.dt BETWEEN IF( STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') < DATE(NOW()), DATE(NOW()), STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') ) AND STR_TO_DATE('" + request.getParameter("to_date") + "', '%d-%m-%Y')  AND CASE WHEN ct.dt = STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND 'AN' = '" + request.getParameter("from_ses") + "' THEN htm.session = 'AN' WHEN ct.dt = STR_TO_DATE('" + request.getParameter("to_date") + "', '%d-%m-%Y') AND 'FN' = '" + request.getParameter("to_ses") + "' THEN htm.session = 'FN' ELSE htm.session IN ('FN', 'AN') END AND ssa.status > 0 ORDER BY dt,htm.htm_id");
                        }
                    }
                    if (db.rs1.getString("alter_visibility").equalsIgnoreCase("true")) {
                        String data = "<table class=\"table table-condensed\" width='100%' border='1'><tr><th>Self alter</th><th>Date</th><th>Day</th><th>Hour</th><th>Subject</th><th>Department</th><th>Staff</th></tr>";
                        int i = 1;;
                        while (db.rs.next()) {
                            data += "<tr><td><input type=\"checkbox\" disabled name=\"no_cls_alter" + i + "\" id=\"no_cls_alter" + i + "\" class=\"filled-in chk-col-orange\"  onclick=\"uncheck(" + i + ")\" />\n"
                                    + "<label for=\"no_cls_alter" + i + "\"></label></td><input type='hidden' id='ttm_id" + i + "' name='ttm_id" + i + "' value='" + db.rs.getString("ttm_id") + "' /><input type='hidden' id='a_date" + i + "' name='a_date" + i + "' value='" + db.rs.getString("dt") + "' /> <td>" + db.rs.getString("dt") + "</td><td>" + db.rs.getString("day_name") + "</td><td>" + db.rs.getString("hour") + "</td><td>" + db.rs.getString("sub_name") + "</td>"
                                    + " <td><select style='width:75px;' name='dep_id" + i + "' id='dep_id" + i + "' class='validate[required]' required onchange='load_staff(this.value," + i + ")' >" + dept_str + "</select> </td> "
                                    + " <td><select style='width:250px;'   name='staff_id_drp" + i + "' id='staff_id_drp" + i + "' required class='validate[required]' ><option value=''>--select--</option></select> </td></tr>";
                            i++;
                        }

                        data += "</table>";

                        out.print(data);
                    }
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_alter_indi")) {
                    String str = "", str_color = "";
                    int i = 0;
                    db.read("SELECT ttm.ttm_id, DATE_FORMAT(slca.alter_date, '%d-%m-%Y') AS dt, mcr.room_no, dm.day_name, CONCAT( TIME_FORMAT(htm.start_time, '%h:%i %p'), '-', TIME_FORMAT(htm.end_time, '%h:%i %p') ) AS hr, concat(IF( ssa.csm_id IS NOT NULL, IFNULL(sm.dis_code, sm.sub_code), nsm.sub_code ) ,'-', IF( ssa.csm_id IS NOT NULL, sm.sub_name, nsm.sub_name )) sub_name, concat( concat(spv.first_name,'',spv.middle_name,'',spv.last_name),'-',spv.staff_id) staff_id, slca.status AS cls_alter_approval FROM camps.staff_leave_cls_alter slca INNER JOIN studacad.time_table_master_21 ttm ON ttm.ttm_id = slca.ttm_id INNER JOIN studacad.subject_staff_allocation ssa ON ssa.ssa_id = ttm.ssa_id INNER JOIN studacad.day_master dm ON dm.dm_id = ttm.dm_id INNER JOIN studacad.hour_timing_master htm ON htm.htm_id = ttm.htm_id INNER JOIN camps.master_class_room mcr ON mcr.class_room_id = ttm.class_room_id LEFT OUTER JOIN studacad.core_subject_master csm ON csm.csm_id = ssa.csm_id LEFT OUTER JOIN curriculum.subject_master sm ON sm.subject_id=csm.subject_id LEFT OUTER JOIN studacad.noncore_subject_master nsm ON nsm.nsm_id = ssa.nsm_id INNER JOIN camps.staff_master spv ON spv.staff_id = slca.alter_staff_id WHERE slca.status>0 and slca.slr_id = " + request.getParameter("leave_id"));
                    String data = "<table class=\"table table-condensed\" width='100%'><tr><th>S No</th><th>Date</th><th>Day</th><th>Hour</th><th>Subject</th><th>Venue</th><th>Alter Staff</th></tr>";
                    while (db.rs.next()) {
                        if (db.rs.getString("cls_alter_approval").equalsIgnoreCase("1")) {
                            str = "Acceptance Pending";
                            str_color = "#FFFFB3";
                        } else if (db.rs.getString("cls_alter_approval").equalsIgnoreCase("2")) {
                            str = "Accepted";
                            str_color = "#9BFF9B";
                        } else if (db.rs.getString("cls_alter_approval").equalsIgnoreCase("3")) {
                            str = "Declined";
                            str_color = "#FFA6A6";
                        }
                        data += "<tr style=\"background-color: " + str_color + "\"><td>" + ++i + " </td><td>" + db.rs.getString("dt") + "</td><td>" + db.rs.getString("day_name") + "</td><td>" + db.rs.getString("hr") + "</td><td>" + db.rs.getString("sub_name") + "</td><td>" + db.rs.getString("room_no") + "</td><td>" + db.rs.getString("staff_id") + "</td></tr>";
                    }
                    data += "<tr><td colspan='7'>**Yellow- Pending, Green-Accepted, Red-Declined</td></tr></table>";
                    out.print(data);

                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_alter_details")) {
                    int i = 0;
                    String btn = "", query = "", query1 = "";
                    String section = "", str_color = "", cancel_str = "", cancel_color = "";
                    String approve = "class=\"btn btn-success waves-effect\"";
                    String decline = "class=\"btn btn-danger waves-effect\"";
                    String data = "<table class=\"table table-condensed\" width='100%'> <tr style=\"height:30px;\"> <th style=\"text-align:center\">S.NO</th> <th style=\"text-align:center\">REQUEST STAFF</th> <th style=\"text-align:center\">LEAVE TYPE</th> <th style=\"text-align:center\">LEAVE DATE</th> <th>DAY</th> <th style=\"text-align:center\">HOUR</th> <th style=\"text-align:center\">SUBJECT</th> <th style=\"text-align:center\">CLASS ROOM</th> <th></th><th  ></th> </tr>";
                    db.read("SELECT slca_id AS id, ttm.ttm_id, DATE_FORMAT(slca.alter_date, '%d-%m-%Y') AS dt, dcm.room_no, dm.day_name, CONCAT( TIME_FORMAT(htm.start_time, '%h:%i %p'), '-', TIME_FORMAT(htm.end_time, '%h:%i %p') ) AS hr, CONCAT( IF( ssa.csm_id IS NOT NULL, IFNULL(sm.dis_code, sm.sub_code), nsm.sub_code ), '-', IF( ssa.csm_id IS NOT NULL, sm.sub_name, nsm.sub_name ) ) sub_name, CONCAT( CONCAT( spv.first_name, '', spv.middle_name, '', spv.last_name ), '-', spv.staff_id ) staff_id, IF( DATE_FORMAT(NOW(), '%Y-%m-%d') > slca.alter_date, 1, 0 ) AS date_con, slm.leave_name, slca.status AS cls_alter_approval FROM camps.staff_leave_cls_alter slca INNER JOIN studacad.time_table_master_21 ttm ON ttm.ttm_id = slca.ttm_id INNER JOIN studacad.subject_staff_allocation ssa ON ssa.ssa_id = ttm.ssa_id INNER JOIN studacad.day_master dm ON dm.dm_id = ttm.dm_id INNER JOIN studacad.hour_timing_master htm ON htm.htm_id = ttm.htm_id INNER JOIN camps.master_class_room dcm ON dcm.class_room_id = ttm.class_room_id LEFT OUTER JOIN studacad.core_subject_master csm ON csm.csm_id = ssa.csm_id LEFT OUTER JOIN curriculum.subject_master sm ON sm.subject_id = csm.subject_id LEFT OUTER JOIN studacad.noncore_subject_master nsm ON nsm.nsm_id = ssa.nsm_id INNER JOIN camps.staff_master spv ON spv.staff_id = slca.inserted_by INNER JOIN camps.staff_leave_request slr ON slr.slr_id = slca.slr_id INNER JOIN camps.staff_leave_master slm ON slm.slm_id = slr.slm_id WHERE slca.alter_staff_id = '" + session.getAttribute("ss_id") + "' AND (slca.status=1 OR slca.alter_date = DATE(NOW())) ");
                    while (db.rs.next()) {

                        if (db.rs.getString("cls_alter_approval").equalsIgnoreCase("1")) {
                            if (db.rs.getString("date_con").equals("1")) {
                                btn = "<td align=\"center\" ><input class=\"btn btn-success waves-effect\" disabled=\"disabled\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Date Elapsed.Inform alter requested faculty.\" type=\"button\" name=\"approve\" id=\"approve" + i + "\" value=\"Accept\" onclick=\"approve_alter(" + db.rs.getString("id") + ")\" /></td> <td align=\"center\"><input class=\"btn btn-danger waves-effect\"  type=\"button\" name=\"decline\" id=\"approve" + i + "\" disabled=disable data-toggle=\"tooltip\" data-placement=\"top\" title=\"Date Elapsed.Inform alter requested faculty.\" value=\"Decline\" onclick=\"approve_alter(" + db.rs.getString("id") + ")\" /></td>";
                            } else {
                                btn = "<td align=\"center\" ><input class=\"btn btn-success waves-effect\"  type=\"button\" name=\"approve\" id=\"approve" + i + "\" value=\"Accept\" onclick=\"approve_alter(" + db.rs.getString("id") + ")\" /></td> <td align=\"center\"><input class=\"btn btn-danger waves-effect\"  type=\"button\" name=\"decline\" id=\"approve" + i + "\" value=\"Decline\" onclick=\"decline_alter(" + db.rs.getString("id") + ")\" /></td>";
                            }
                        } else if (db.rs.getString("cls_alter_approval").equalsIgnoreCase("2")) {

                            btn = "<td align=\"center\"><span class=\"label label-success\">Alter Accepted</span></td>";

                        } else if (db.rs.getString("cls_alter_approval").equalsIgnoreCase("3")) {
                            btn = "<td align=\"center\"><span class=\"label label label-danger\">Alter Declined</span></td>";

                        } else if (db.rs.getString("cls_alter_approval").equalsIgnoreCase("0")) {
                            btn = "<td align=\"center\"><span class=\"label label label-danger\">Leave Request Cancelled</span></td>";

                        }
                        // data += "<tr> <td style=\"background-color: "+str_color+";\"align=\"center\">"+ ++i +"<input type=\"hidden\" name=\"cls_alter_id\" id=\"cls_alter_id\" value=\" "+db.rs.getString("id")+" \" /></td> <td style=\"background-color: "+str_color+"; \"align=\"left\">"+db.rs.getString("staff_id")+"<br /></td> <td style=\"background-color: "+str_color+"; \"align=\"center\">"+db.rs.getString("leave_name")+"</td> <td style=\"background-color: "+str_color+";\"align=\"center\">"+db.rs.getString("dt")+"</td> <td style=\"background-color: "+str_color+";\"align=\"center\">"+db.rs.getString("day_name")+"</td> <td style=\"background-color: "+str_color+";\"align=\"center\">"+db.rs.getString("hr")+"</td> <td style=\"background-color: "+str_color+";\"align=\"center\">"+db.rs.getString("sub_name")+"</td> <td style=\"background-color:"+str_color+";\"align=\"center\">"+db.rs.getString("room_no")+"</td> <td align=\"center\" style=\"background-color: "+str_color+";\"><input "+disabled+" type=\"button\" name=\"approve\" id=\"approve"+i+"\" value=\"Accept\" onclick=\"approvalid("+db.rs.getString("id")+",'Approved')\" /></td> <td style=\"background-color: "+str_color+"; %>;\"align=\"center\"><input "+disabled+" type=\"button\" name=\"decline\" id=\"approve"+i+"\" value=\"Decline\" onclick=\"approvalid("+db.rs.getString("id")+",'Declined')\" /></td> <td style=\"background-color: "+cancel_color+" \"align=\"center\">"+cancel_str+"</td> </tr>";
                        data += "<tr> <td>" + ++i + "<input type=\"hidden\" name=\"cls_alter_id\" id=\"cls_alter_id\" value=\" " + db.rs.getString("id") + " \" /></td> <td>" + db.rs.getString("staff_id") + "<br /></td> <td>" + db.rs.getString("leave_name") + "</td> <td>" + db.rs.getString("dt") + "</td> <td>" + db.rs.getString("day_name") + "</td> <td>" + db.rs.getString("hr") + "</td> <td>" + db.rs.getString("sub_name") + "</td> <td>" + db.rs.getString("room_no") + "</td> " + btn + " </tr>";
                    }
                    out.print(data);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_alter_details_admin")) {
                    int i = 0;
                    String btn = "", query = "", query1 = "";
                    String data = "<table class=\"table table-condensed\" width='100%'> <tr style=\"height:30px;\"> <th style=\"text-align:center\">S.NO</th> <th style=\"text-align:center\">REQUEST STAFF</th> <th style=\"text-align:center\">LEAVE TYPE</th> <th style=\"text-align:center\">LEAVE DATE</th> <th>DAY</th> <th style=\"text-align:center\">HOUR</th> <th style=\"text-align:center\">SUBJECT</th> <th style=\"text-align:center\">CLASS ROOM</th> <th></th><th  ></th> </tr>";
                    db.read("SELECT slca_id AS id, ttm.ttm_id, DATE_FORMAT(slca.alter_date, '%d-%m-%Y') AS dt, dcm.room_no, dm.day_name, CONCAT( TIME_FORMAT(htm.start_time, '%h:%i %p'), '-', TIME_FORMAT(htm.end_time, '%h:%i %p') ) AS hr, CONCAT( IF( ssa.csm_id IS NOT NULL, IFNULL(sm.dis_code, sm.sub_code), nsm.sub_code ), '-', IF( ssa.csm_id IS NOT NULL, sm.sub_name, nsm.sub_name ) ) sub_name, CONCAT( CONCAT( spv.first_name, '', spv.middle_name, '', spv.last_name ), '-', spv.staff_id ) staff_id, IF( DATE_FORMAT(NOW(), '%Y-%m-%d') > slca.alter_date, 1, 0 ) AS date_con, slm.leave_name, slca.status AS cls_alter_approval FROM camps.staff_leave_cls_alter slca INNER JOIN studacad.time_table_master_21 ttm ON ttm.ttm_id = slca.ttm_id INNER JOIN studacad.subject_staff_allocation ssa ON ssa.ssa_id = ttm.ssa_id INNER JOIN studacad.day_master dm ON dm.dm_id = ttm.dm_id INNER JOIN studacad.hour_timing_master htm ON htm.htm_id = ttm.htm_id INNER JOIN camps.master_class_room dcm ON dcm.class_room_id = ttm.class_room_id LEFT OUTER JOIN studacad.core_subject_master csm ON csm.csm_id = ssa.csm_id LEFT OUTER JOIN curriculum.subject_master sm ON sm.subject_id = csm.subject_id LEFT OUTER JOIN studacad.noncore_subject_master nsm ON nsm.nsm_id = ssa.nsm_id INNER JOIN camps.staff_master spv ON spv.staff_id = slca.inserted_by INNER JOIN camps.staff_leave_request slr ON slr.slr_id = slca.slr_id INNER JOIN camps.staff_leave_master slm ON slm.slm_id = slr.slm_id WHERE slca.inserted_by = '" + request.getParameter("staff_id") + "' AND slca.status=1 AND slca.alter_date<DATE(NOW()) ");
                    while (db.rs.next()) {
                        btn = "<td align=\"center\" ><input class=\"btn btn-success waves-effect\"  type=\"button\" name=\"approve\" id=\"approve" + i + "\" value=\"Accept\" onclick=\"approve_alter_admin(" + db.rs.getString("id") + ")\" /></td></td>";
                        data += "<tr> <td>" + ++i + "<input type=\"hidden\" name=\"cls_alter_id\" id=\"cls_alter_id\" value=\" " + db.rs.getString("id") + " \" /></td> <td>" + db.rs.getString("staff_id") + "<br /></td> <td>" + db.rs.getString("leave_name") + "</td> <td>" + db.rs.getString("dt") + "</td> <td>" + db.rs.getString("day_name") + "</td> <td>" + db.rs.getString("hr") + "</td> <td>" + db.rs.getString("sub_name") + "</td> <td>" + db.rs.getString("room_no") + "</td> " + btn + " </tr>";
                    }
                    out.print(data);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_staff")) {
                    db.read("SELECT sm.staff_id,sm.staff_id,CONCAT(sm.first_name,'',sm.middle_name,'',sm.last_name,'-',sm.staff_id) staff_name  FROM camps.staff_master sm WHERE sm.staff_id not in ('" + session.getAttribute("ss_id") + "') and sm.department_id=" + request.getParameter("dep_id"));
                    String data = "<option value=''>--select--</option>";
                    while (db.rs.next()) {
                        data += "<option value='" + db.rs.getInt("staff_id") + "' >" + db.rs.getString("staff_name") + "</option>";
                    }
                    out.print(data);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_hod_leave")) {

                    int i = 1;
                    String data = "", disabled = "";
                    String check_level = "";
                    if (Integer.parseInt(request.getParameter("level")) > 1) {
                        int level = Integer.parseInt(request.getParameter("level")) - 1;
                        check_level = "INNER JOIN (camps.staff_leave_request_approval slra3 INNER JOIN camps.staff_leave_approval_flow slaf3 ) ON   slra3.slr_id=slra.slr_id   AND slra3.status=2  AND slaf3.slaf_id=slra3.slaf_id AND slaf3.level=" + level;
                    }
                    String ig_staff = "and sm.staff_id not in('0'";
                    String query = "SELECT slr.slr_id,slaf.slaf_id,sm.staff_id,CONCAT(sm.first_name,'',sm.middle_name,'',sm.last_name,'-',sm.staff_id) staff_name,slm.leave_name,CONCAT(DATE_FORMAT(slr.from_date,'%d-%m-%Y'),'-',slr.from_session) from_date,CONCAT(DATE_FORMAT(slr.to_date,'%d-%m-%Y'),'-',slr.to_session) to_date,IFNULL(slr.reason,'') reason FROM camps.staff_leave_request slr INNER JOIN camps.staff_leave_request_approval slra ON slr.slr_id=slra.slr_id AND slr.status=1 AND slra.status=1 INNER JOIN staff_leave_approval_flow slaf ON slaf.slaf_id=slra.slaf_id AND slaf.level=" + request.getParameter("level") + " and slaf.status=1 INNER JOIN staff_master sm ON sm.staff_id=slr.staff_id INNER JOIN camps.staff_leave_master slm ON slm.slm_id=slr.slm_id " + check_level + " WHERE slr.staff_id IN ";
                    //db.read(query + "(SELECT imm.member_id FROM camps.incharge_member_mapping imm WHERE imm.incharge_staff_id=1003 AND imm.itm_id=2 AND imm.status>0)");
                    db.read(query + "(SELECT imm.member_id FROM camps.incharge_member_mapping imm INNER JOIN staff_leave_approval_flow sla ON sla.itm_id=imm.itm_id AND sla.level=" + request.getParameter("level") + " WHERE imm.incharge_staff_id='" + session.getAttribute("ss_id") + "'  AND imm.status>0)");
                    data = "<div class=\"body table-responsive\"><table class=\"table table-condensed\" width='100%' border='1'><tr><th>S No</th><th>Staff Name</th><th>Leave Type</th><th>Leave From</th><th>Leave To</th><th>Reason</th><th>Alter Details</th><th></th></tr>";
                    while (db.rs.next()) {
                        db.read2("SELECT * FROM camps.staff_leave_cls_alter slca WHERE slca.slr_id=" + db.rs.getString("slr.slr_id") + "  AND slca.status  IN (1,3)");
                        if (db.rs2.next()) {
                            disabled = "disabled data-toggle=\"tooltip\" data-placement=\"top\" title=\"Check Alter Details\"";
                        }
                        data += "<tr><td>" + i++ + "</td><td>" + db.rs.getString("staff_name") + "</td><td>" + db.rs.getString("leave_name") + "</td><td>" + db.rs.getString("from_date") + "</td><td>" + db.rs.getString("to_date") + "</td><td>" + db.rs.getString("reason") + "</td><td><button type=\"button\" class=\"btn bg-green waves-effect\" data-toggle=\"modal\" data-target=\"#largeModal\"  onclick='showDetails(" + db.rs.getString("slr.slr_id") + ")'><i class=\"material-icons\">verified_user</i>Click to View</button></td><td><button type=\"button\"  class=\"btn btn-success waves-effect\" " + disabled + " onclick='approve_leave(" + db.rs.getString("slr.slr_id") + "," + db.rs.getString("slaf.slaf_id") + "," + request.getParameter("level") + ")'>Approve</button>&nbsp;&nbsp;&nbsp;<button type=\"button\" " + disabled + " class=\"btn btn-danger waves-effect\" onclick='decline_leave(" + db.rs.getString("slr.slr_id") + "," + db.rs.getString("slaf.slaf_id") + "," + request.getParameter("level") + ")'>Decline</button></td></tr>";
                        ig_staff += ",'" + db.rs.getString("staff_id") + "'";
                        disabled = "";
                    }

                    ig_staff += ")";
                    db.read("SELECT itm.member_query  FROM camps.incharge_type_master itm inner join camps.staff_leave_approval_flow slaf on slaf.itm_id=itm.itm_id WHERE slaf.level=" + request.getParameter("level") + " having itm.member_query is not null");
                    if (db.rs.next()) {

                        db.read1(query + db.rs.getString("member_query").replaceAll("__department_id__", session.getAttribute("depId").toString()).replaceAll("__staff_id__", session.getAttribute("ss_id").toString()) + ig_staff);
                        while (db.rs1.next()) {
                            db.read2("SELECT * FROM camps.staff_leave_cls_alter slca WHERE slca.slr_id=" + db.rs1.getString("slr.slr_id") + " AND slca.status  IN (1,3)");
                            if (db.rs2.next()) {
                                disabled = "disabled data-toggle=\"tooltip\" data-placement=\"top\" title=\"Check Alter Details\"";
                            }
                            data += "<tr><td>" + i++ + "</td><td>" + db.rs1.getString("staff_name") + "</td><td>" + db.rs1.getString("leave_name") + "</td><td>" + db.rs1.getString("from_date") + "</td><td>" + db.rs1.getString("to_date") + "</td><td>" + db.rs1.getString("reason") + "</td><td><button type=\"button\" class=\"btn bg-green waves-effect\" data-toggle=\"modal\" data-target=\"#largeModal\"  onclick='showDetails(" + db.rs1.getString("slr.slr_id") + ")'><i class=\"material-icons\">verified_user</i>Click to View</button></td><td><button type=\"button\" class=\"btn btn-success waves-effect\" " + disabled + " onclick='approve_leave(" + db.rs1.getString("slr.slr_id") + "," + db.rs1.getString("slaf.slaf_id") + "," + request.getParameter("level") + ")'>Approve</button>&nbsp;&nbsp;&nbsp;<button type=\"button\" class=\"btn btn-danger waves-effect\" " + disabled + "  onclick='decline_leave(" + db.rs1.getString("slr.slr_id") + "," + db.rs1.getString("slaf.slaf_id") + "," + request.getParameter("level") + ")'>Decline</button></td></tr>";
                            disabled = "";
                        }

                    }
                    data += "</table></div>";
                    out.print(data);

                }
                if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("submit_leave1")) {
                    String query = "";
                    String leave_id = db.insertAndGetAutoGenId("INSERT ignore INTO camps.staff_leave_request (slr_id,slm_id,staff_id,from_date,to_date,from_session,to_session,reason,STATUS,inserted_by,inserted_date,updated_by,updated_date) VALUES (null,'" + request.getParameter("leave_type") + "','" + session.getAttribute("ss_id") + "',STR_TO_DATE('" + request.getParameter("from_date") + "','%d-%m-%Y'),STR_TO_DATE('" + (request.getParameter("to_date") == null ? request.getParameter("from_date") : request.getParameter("to_date")) + "','%d-%m-%Y'),'" + request.getParameter("from_ses") + "','" + (request.getParameter("to_ses") == null ? request.getParameter("from_ses") : request.getParameter("to_ses")) + "','" + request.getParameter("Reason") + "',1,'" + session.getAttribute("user_id") + "',    now(),     '" + session.getAttribute("user_id") + "',  now()) on duplicate key update status=1,to_date=values(to_date),to_session=values(to_session),reason=values(reason),updated_by='" + session.getAttribute("user_id") + "',updated_date=now()");
                    if (db.iEffectedRows != 0) {
                        int i = 1;
                        db.insert("INSERT ignore INTO camps.staff_leave_request_approval (SELECT " + leave_id + ",slaf.slaf_id,1,'" + session.getAttribute("user_id") + "',    now(),     '" + session.getAttribute("user_id") + "',  now() FROM camps.staff_leave_approval_flow slaf WHERE slaf.slm_id='" + request.getParameter("leave_type") + "' AND slaf.status=1) on duplicate key update status=1,updated_date=now(),updated_by=values(updated_by)");

                        db.read1("SELECT slm.form_model FROM camps.staff_leave_master slm WHERE slm.slm_id=" + request.getParameter("leave_type"));
                        if (db.rs1.next()) {
                            if (db.rs1.getString("form_model") != null && db.rs1.getString("form_model").equalsIgnoreCase("one_session")) {

                            } else if (db.rs1.getString("form_model") != null && db.rs1.getString("form_model").equalsIgnoreCase("one_day")) {

                            } else {
                                query = "SELECT * FROM studacad.subject_staff_allocation ssa INNER JOIN studacad.time_table_master_21 ttm ON ttm.ssa_id = ssa.ssa_id AND ttm.status > 0 INNER JOIN studacad.hour_timing_master htm ON htm.htm_id = ttm.htm_id AND htm.status > 0 INNER JOIN studacad.day_master dm ON dm.dm_id = ttm.dm_id AND dm.status > 0 LEFT JOIN studacad.day_order_mapping dom ON dom.dm_id = dm.dm_id LEFT JOIN ( studacad.core_subject_master csm LEFT OUTER JOIN curriculum.subject_master sm ON sm.subject_id = csm.subject_id ) ON ssa.csm_id = csm.csm_id LEFT JOIN studacad.noncore_subject_master nsm ON nsm.nsm_id = ssa.nsm_id AND nsm.status > 0 LEFT JOIN camps.calendar_table ct ON IF( dom.doc IS NOT NULL and dom.doc = ct.dt, dom.doc = ct.dt, dm.day_name = ct.dayName ) WHERE ssa.staff_id = '" + session.getAttribute("ss_id") + "' AND ct.dt BETWEEN IF( STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') < DATE(NOW()), DATE(NOW()), STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') ) AND STR_TO_DATE('" + request.getParameter("to_date") + "', '%d-%m-%Y') AND CASE WHEN ct.dt = STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND 'AN' = '" + request.getParameter("from_ses") + "' THEN htm.session = 'AN' WHEN ct.dt = STR_TO_DATE('" + request.getParameter("to_date") + "', '%d-%m-%Y') AND 'FN' = '" + request.getParameter("to_ses") + "' THEN htm.session = 'FN' ELSE htm.session IN ('FN', 'AN') END AND ssa.status > 0 ORDER BY dt,htm.htm_id";
                            }
                        }
                        db.read1(query);

                        while (db.rs1.next()) {
                            String dup = "";
                            String Query1 = "INSERT INTO `staff_leave_cls_alter` VALUES (NULL,"
                                    + "'" + leave_id + "',"
                                    + "'" + request.getParameter("ttm_id" + i) + "',"
                                    + "STR_TO_DATE('" + request.getParameter("a_date" + i) + "','%d-%m-%Y'),";
                            if (request.getParameter("no_cls_alter" + i) != null && request.getParameter("no_cls_alter" + i).equalsIgnoreCase("on")) {
                                Query1 += "'" + session.getAttribute("ss_id") + "',NULL,"
                                        + "'" + 2 + "',";
                                dup += "status=2,alter_staff_id='" + session.getAttribute("ss_id") + "'";
                            } else {
                                Query1 += "'" + request.getParameter("staff_id_drp" + i) + "',NULL,"
                                        + "'" + 1 + "',";
                                dup += "status=1,alter_staff_id='" + request.getParameter("staff_id_drp" + i) + "'";
                            }
                            Query1 += "'" + session.getAttribute("ss_id") + "',"
                                    + "NOW(),"
                                    + "'" + session.getAttribute("ss_id") + "',"
                                    + "NOW()) on duplicate key update " + dup + ",updated_by='" + session.getAttribute("ss_id") + "',updated_date=now()";

                            db.insert(Query1);
                            i++;

                        }

                        out.print(1);
                    } else {
                        out.print(0);
                    }
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("cancel_leave")) {
                    db.update("UPDATE camps.staff_leave_request slr set slr.status=0,slr.updated_by='" + session.getAttribute("user_id") + "',slr.updated_date=NOW() where slr.slr_id=" + request.getParameter("leave_id") + "");
                    db.update("UPDATE camps.staff_leave_cls_alter slca set slca.status=0,slca.updated_by='" + session.getAttribute("user_id") + "',slca.updated_date=NOW() where slca.slr_id=" + request.getParameter("leave_id") + "");
                    //db.update("UPDATE camps.staff_leave_request_approval slrp set slrp.status=0,slrp.updated_by='" + session.getAttribute("user_id") + "',slrp.updated_date=NOW() where slrp.slr_id=" + request.getParameter("leave_id") + "");
                    out.print(db.iEffectedRows > 0 ? 1 : 0);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("approve_leave")) {
                    db.update("UPDATE camps.staff_leave_request_approval slaf set slaf.status=2,slaf.updated_by='" + session.getAttribute("user_id") + "',slaf.updated_date=NOW() where slaf.slaf_id=" + request.getParameter("slaf_id") + " and slaf.slr_id=" + request.getParameter("leave_id") + "");
                    db.read("SELECT MAX(slaf.level) max_level FROM camps.staff_leave_request slr INNER JOIN camps.staff_leave_approval_flow slaf ON slr.slm_id=slaf.slm_id WHERE slr.slr_id=" + request.getParameter("leave_id") + " and slaf.status>0 having max_level=" + request.getParameter("level"));
                    if (db.rs.next()) {
                        db.update("UPDATE camps.staff_leave_request slr set slr.status=2,slr.updated_by='" + session.getAttribute("user_id") + "',slr.updated_date=NOW() where slr.slr_id=" + request.getParameter("leave_id") + "");
                    }
                    out.print(db.iEffectedRows > 0 ? 1 : 0);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("decline_leave")) {
                    db.update("UPDATE camps.staff_leave_request_approval slaf set slaf.status=3,slaf.updated_by='" + session.getAttribute("user_id") + "',slaf.updated_date=NOW() where slaf.slaf_id=" + request.getParameter("slaf_id") + " and slaf.slr_id=" + request.getParameter("leave_id") + "");
                    db.update("UPDATE camps.staff_leave_request slr set slr.status=3,slr.updated_by='" + session.getAttribute("user_id") + "',slr.updated_date=NOW() where slr.slr_id=" + request.getParameter("leave_id") + "");

                    out.print(db.iEffectedRows > 0 ? 1 : 0);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("approve_alter")) {
                    db.update("UPDATE camps.staff_leave_cls_alter slca SET slca.status=2,slca.updated_by='" + session.getAttribute("user_id") + "',slca.updated_date=NOW() where slca.slca_id=" + request.getParameter("slca_id"));
                    out.print(db.iEffectedRows > 0 ? 1 : 0);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("decline_alter")) {
                    db.update("UPDATE camps.staff_leave_cls_alter slca SET slca.status=3,slca.updated_by='" + session.getAttribute("user_id") + "',slca.updated_date=NOW() where slca.slca_id=" + request.getParameter("slca_id"));
                    out.print(db.iEffectedRows > 0 ? 1 : 0);
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_leave_report")) {
                    String staff_id = "";
                    db.read1("SELECT DISTINCT(fia.fia_id) fia_id FROM admin.form_ia_role fir INNER JOIN admin.form_item_access fia ON fia.fia_id=fir.fia_id WHERE fir.role_id IN (" + session.getAttribute("roles") + ")  AND fia.fia_id in(5) AND fia.status>0 AND fir.status>0");
                    if (db.rs1.next()) {
                        staff_id = "and slr.staff_id in (" + request.getParameter("staff_id") + ")";
                    } else {
                        staff_id = "and slr.staff_id in (" + session.getAttribute("ss_id") + ")";
                    }
                    db.read("SELECT sm.staff_id,CONCAT(sm.first_name,'',sm.middle_name,'',sm.last_name) staff_name,md.dept_name,slm.leave_name,mdesig.desigination,CONCAT(DATE_FORMAT(slr.from_date,'%d-%m-%Y'),'-',slr.from_session) from_date,CONCAT(DATE_FORMAT(slr.to_date,'%d-%m-%Y'),'-',slr.to_session) to_date,IFNULL(slr.reason,'') reason,IF(slr.status=1,'Pending',IF(slr.status=2,'Approved','Declined')) leave_status FROM camps.staff_leave_request slr INNER JOIN staff_leave_master slm ON slr.slm_id=slm.slm_id INNER JOIN staff_master sm ON sm.staff_id=slr.staff_id inner join camps.master_department md on md.department_id=sm.department_id inner join camps.staff_promotion sp on sp.staff_id=sm.staff_id and sp.status=2 and sp.to_date is null inner join camps.master_desigination mdesig on mdesig.md_id=sp.md_id WHERE slr.slm_id IN (" + request.getParameter("leave_type") + ") AND slr.status IN (" + request.getParameter("approval_type") + ") AND (( slr.from_date BETWEEN str_to_date('" + request.getParameter("from_date") + "','%d-%m-%Y') AND str_to_date('" + request.getParameter("to_date") + "','%d-%m-%Y')) OR ( slr.to_date BETWEEN  str_to_date('" + request.getParameter("from_date") + "','%d-%m-%Y') AND str_to_date('" + request.getParameter("to_date") + "','%d-%m-%Y'))) " + staff_id + "  order by sm.department_id,sm.staff_id,slm.leave_name ");
                    String data = "  <table class=\"table table-bordered table-striped table-hover dataTable js-exportable\"><thead><tr><td>Staff ID</td><td>Staff Name</td><td>Designation</td><td>Department</td><td>Leave Type</td><td>From</td><td>To</td><td>Reason</td><td>Status</td></tr></thead> <tfoot><tr><td>Staff ID</td><td>Staff Name</td><td>Designation</td><td>Department</td><td>Leave Type</td><td>From</td><td>To</td><td>Reason</td><td>Status</td></tr></tfoot><tbody>";
                    while (db.rs.next()) {
                        data += "<tr><td>" + db.rs.getString("staff_id") + "</td><td>" + db.rs.getString("staff_name") + "</td><td>" + db.rs.getString("desigination") + "</td><td>" + db.rs.getString("dept_name") + "</td><td>" + db.rs.getString("leave_name") + "</td><td>" + db.rs.getString("from_date") + "</td><td>" + db.rs.getString("to_date") + "</td><td>" + db.rs.getString("reason") + "</td><td>" + db.rs.getString("leave_status") + "</td></tr>";
                    }
                    data += "</tbody></table>";
                    out.print(data);

                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_leave_consolidated_report")) {
                    String query = "SELECT sm.staff_id,TRIM(CONCAT(IFNULL(sm.first_name,''),' ',IFNULL(sm.middle_name,''),' ',IFNULL(sm.last_name,''),' ')) staff_name", data = "<table border='1' class='tbl'><tr><td rowspan='2'>S No</td><td rowspan='2'>Staff ID</td><td rowspan='2'>Staff Name</td>", data1 = "";
                    int count = 0, sno = 1;
                    db.read("SELECT DATE_FORMAT(ct.dt,'%d-%m-%Y') dis,ct.dt FROM camps.calendar_table ct WHERE ct.dt BETWEEN  STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND STR_TO_DATE('" + request.getParameter("to_date") + "', '%d-%m-%Y') ORDER BY dt");
                    while (db.rs.next()) {
                        query += ",GROUP_CONCAT(IF(ct.dt='" + db.rs.getString("dt") + "' AND a.sess='FN',IF(slm.leave_shortname IS NOT NULL,slm.leave_shortname,IF(sbp.present_session IS NOT NULL,'PR','NA')),'') SEPARATOR '') a" + count++ + ",GROUP_CONCAT(IF(ct.dt='" + db.rs.getString("dt") + "' AND a.sess='AN',IF(slm.leave_shortname IS NOT NULL,slm.leave_shortname,IF(sbp.present_session IS NOT NULL,'PR','NA')),'') SEPARATOR '') a" + count++;
                        data += "<td colspan='2'>" + db.rs.getString("dis") + "</td>";
                        data1 += "<td>FN</td><td>AN</td>";
                    }
                    data += "</tr>" + data1 + "</tr>";
                    db.read1(query + " FROM camps.calendar_table ct JOIN (SELECT 'FN' sess UNION SELECT 'AN' )a ON ct.dt BETWEEN STR_TO_DATE('" + request.getParameter("from_date") + "', '%d-%m-%Y') AND STR_TO_DATE('" + request.getParameter("to_date") + "', '%d-%m-%Y') JOIN camps.staff_master sm LEFT JOIN (camps.staff_leave_request slr INNER JOIN camps.staff_leave_master slm ON slm.slm_id=slr.slm_id) ON slr.status=2 AND slr.staff_id=sm.staff_id AND ct.dt BETWEEN slr.from_date AND slr.to_date AND CASE WHEN DATE(ct.dt)=slr.from_date AND slr.from_session='AN' THEN slr.from_session=a.sess WHEN DATE(ct.dt)=slr.to_date AND slr.to_session='FN' THEN slr.from_session=a.sess ELSE TRUE END LEFT JOIN camps.staff_biometric_present sbp  ON ct.dt=sbp.present_date AND a.sess=sbp.present_session AND sbp.late_present=0  AND sm.staff_id=sbp.staff_id WHERE sm.working_status='working' and sm.department_id in (" + request.getParameter("dept_id") + ") and sm.sc_id in (" + request.getParameter("sc_id") + ") GROUP BY sm.staff_id");
                    while (db.rs1.next()) {
                        data += "<tr><td>" + sno++ + "</td><td>" + db.rs1.getString("staff_id") + "</td><td>" + db.rs1.getString("staff_name") + "</td>";
                        for (int i = 0; i < count; i++) {
                            data += "<td>" + db.rs1.getString("a" + i) + "</td>";
                        }
                        data += "</tr>";
                    }
                    data += "</table>";
                    out.print(data);
                }
                 else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("addholiday_det")) {
                  db.insert("INSERT INTO camps.staff_leave_request ( slr_id,  slm_id,  staff_id,  from_date,  to_date,  from_session,  to_session,  reason,  STATUS,  inserted_by,  inserted_date,  updated_by,  updated_date)SELECT NULL,10,sm.staff_id,STR_TO_DATE('" + request.getParameter("h_date") + "','%d-%m-%Y'),STR_TO_DATE('" + request.getParameter("h_date") + "','%d-%m-%Y'),'FN','AN','" + request.getParameter("reason") + "',2,'"+session.getAttribute("user_id")+"',NOW(),'"+session.getAttribute("user_id")+"',NOW() FROM camps.staff_master sm WHERE sm.staff_id IN ('" + String.join("','",request.getParameterValues("staff_id"))+ "') ON DUPLICATE KEY UPDATE STATUS=2,updated_by=VALUES(updated_by),updated_date=NOW()");
                  response.sendRedirect("staff_holiday_entry.jsp?am_c="+(db.iEffectedRows>0?1:0) );
                 }
                else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadstaffdet_holiday")) {
                   report_process rs = new report_process();
                ArrayList<String> attribute = new ArrayList<>();
                attribute.add(request.getParameter("h_date"));
                out.print("" + rs.report_v1("16", attribute) + " "); 
                }
                else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("loadstaffdet_resigned")) {
                   report_process rs = new report_process();
                ArrayList<String> attribute = new ArrayList<>();
                attribute.add(request.getParameter("dept_id"));
                attribute.add(request.getParameter("cat_id"));
                out.print("" + rs.report_v1("17", attribute) + " "); 
                }
                else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("delete_holiday_slr")) {
                 db.update("update camps.staff_leave_request slr set slr.status=0,updated_by='"+session.getAttribute("user_id")+"',updated_date=now() where slr.slr_id='" + request.getParameter("slr_id") + "'");
                }
                
            } catch (Exception e) {
                out.print(e);
            } finally {
                try {
                    db.closeConnection();
                } catch (SQLException ex) {
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
