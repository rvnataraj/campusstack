package CAMPS.Common;

import CAMPS.Connect.DBConnect;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

public class report_process {

    public String report_v1(String rm_id, ArrayList<String> attribute) {
        DBConnect db = new DBConnect();
        String query = "", display_type = "", data = "";
        try {
            db.getConnection();
            db.read("SELECT rm.report_query,rm.replace_var_count,rm.display_type FROM camps.report_master rm WHERE rm.rm_id='" + rm_id + "'");
            if (db.rs.next()) {
                query = db.rs.getString("report_query");
                display_type = db.rs.getString("display_type");
                if (db.rs.getInt("replace_var_count") != attribute.size()) {
                    return "";
                }
            }
            for (int i = 1; i <= attribute.size(); i++) {
                query = query.replaceAll("__" + i + "__", attribute.get(i - 1));
            }
            db.read(query);
            if (db.rs.next()) {
                db.rs.beforeFirst();
                db.read1("SELECT ra.ra_query_name,ra.ra_display_name FROM camps.report_attribute ra WHERE ra.rm_id='" + rm_id + "' and ra.status>0 ORDER BY ra.order_no");
                if (display_type.equalsIgnoreCase("table")) {
                    data += "<table class='table table-bordered' width='100%'><thead><tr>";
                    while (db.rs1.next()) {
                        data += "<th>" + db.rs1.getString("ra_display_name") + "</th>";
                    }
                    data += "</tr></thead>";
                    while (db.rs.next()) {
                        db.rs1.beforeFirst();
                        data += "<tr>";
                        while (db.rs1.next()) {
                            data += "<td data-column=\"" + db.rs1.getString("ra_display_name") + "\">" + db.rs.getString(db.rs1.getString("ra_query_name")) + "</td>";
                        }
                        data += "</tr>";
                    }
                } else if (display_type.equalsIgnoreCase("data")) {
                    data += "<table class='tbl_3col' width='100%'>";
                    while (db.rs.next()) {
                        db.rs1.beforeFirst();
                        while (db.rs1.next()) {
                            data += "<tr><td width='15%' style='word-break:break-all;'>" + db.rs1.getString("ra_display_name") + "</td><td width='1%'>:</td><td width='35%' style='word-break:break-all;'><b>" + db.rs.getString(db.rs1.getString("ra_query_name")) + "</b></td>";
                            if (db.rs1.next()) {
                                data += "<td width='15%' style='word-break:break-all;'>" + db.rs1.getString("ra_display_name") + "</td><td width='1%'>:</td><td width='35%' style='word-break:break-all;'><b>" + db.rs.getString(db.rs1.getString("ra_query_name")) + "</b></td>";
                            }
                            data += "</tr>";
                        }
                    }
                }
                data += "</table>";
            } else {
                data = "No Record Found";
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(report_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    public String json_v1(String rm_id, ArrayList<String> attribute, HttpServletRequest request) {
        DBConnect db = new DBConnect();
        String query = "", tot_query = "", filter_query = "", order_by = " ORDER BY ", where_condition = "", display_type = "", data = "", json = "";
        int wc_count = 0, tot_record = 0, filter_record = 0;
        try {
            db.getConnection();
            db.read("SELECT rm.report_query,rm.replace_var_count,rm.display_type,GROUP_CONCAT(ra.ra_query_name order by ra.order_no SEPARATOR ',''''),ifnull(' ) q_data  FROM camps.report_master rm INNER JOIN camps.report_attribute ra ON rm.rm_id=ra.rm_id AND ra.status>0 WHERE rm.rm_id='" + rm_id + "'");
            if (db.rs.next() && db.rs.getString("display_type").equalsIgnoreCase("json")) {
                tot_query = db.rs.getString("report_query").replaceAll("__JSON__", "count(*) t_count");
                query = db.rs.getString("report_query").replaceAll("__JSON__", " JSON_ARRAY(ifnull(" + db.rs.getString("q_data") + ",''))datacol,ifnull(" + db.rs.getString("q_data") + ",'') ");
                display_type = db.rs.getString("display_type");
                if (db.rs.getInt("replace_var_count") != attribute.size()) {
                    return "";
                }
            }
            db.read1("SELECT ra.ra_query_name,ra.ra_display_name FROM camps.report_attribute ra WHERE ra.rm_id='" + rm_id + "' and ra.status>0 ORDER BY ra.order_no");
            while (db.rs1.next()) {
                where_condition += " AND " + db.rs1.getString("ra_query_name") + " like '%" + request.getParameter("columns[" + wc_count++ + "][search][value]") + "%' ";
                if (request.getParameter("order[0][column]").equalsIgnoreCase(String.valueOf(wc_count - 1))) {
                    order_by += "" + db.rs1.getString("ra_query_name") + " " + request.getParameter("order[0][dir]") + "";
                }
            }
            for (int i = 1; i <= attribute.size(); i++) {
                query = query.replaceAll("__" + i + "__", attribute.get(i - 1));
            }
            query = query.replaceAll("__WC__", where_condition);
            query = query.replaceAll("__OB__", order_by);
            filter_query = tot_query.replaceAll("__LB__", "  ").replaceAll("__WC__", where_condition).replaceAll("__OB__", " ");
            tot_query = tot_query.replaceAll("__LB__", "  ").replaceAll("__WC__", " ").replaceAll("__OB__", " ");
            db.read(tot_query);
            if (db.rs.next()) {
                tot_record = db.rs.getInt("t_count");
            }
            db.read(filter_query);
            if (db.rs.next()) {
                filter_record = db.rs.getInt("t_count");
            }
            query = query.replaceAll("__LB__", " LIMIT " + request.getParameter("start") + ", " + (request.getParameter("length").equalsIgnoreCase("-1") ? filter_record : request.getParameter("length")) + " ");
            db.read(query);
            if (db.rs.next()) {
                data += db.rs.getString("datacol");
            }
            while (db.rs.next()) {
                data += "," + db.rs.getString("datacol");
            }
            json = "{ \"draw\": " + request.getParameter("draw") + ",  \"recordsTotal\": " + tot_record + ", \"recordsFiltered\": " + filter_record + ",\"data\":[" + data + "]}";

        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(report_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return json;
    }

}
