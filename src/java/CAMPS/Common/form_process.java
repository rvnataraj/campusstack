
package CAMPS.Common;

import CAMPS.Connect.DBConnect;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class form_process {

    public String form_v1(String fm_id, ArrayList<String> attribute) {
        DBConnect db = new DBConnect();
        String query = "", hiddendata_new_form = "", display_type = "", data = "", button = "";
        try {
            db.getConnection();
            db.read("SELECT fm.fm_id,fm.operation_type,fm.form_query,fm.action_url,fm.button_details FROM camps.form_master fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'");
            if (db.rs.next()) {
                query = db.rs.getString("form_query");
//                display_type = db.rs.getString("display_type");
//                if (db.rs.getInt("replace_var_count") != attribute.size()) {
//                    return "";
//                }
                data += "<form id='form_" + db.rs.getString("fm_id") + "' method='post' action='" + db.rs.getString("action_url") + "'><input type='hidden' id='form_id' name='form_id' value='" + db.rs.getString("fm_id") + "' />";
                button += db.rs.getString("button_details");
            }
            for (int i = 1; i <= attribute.size(); i++) {
                query = query.replaceAll("__" + i + "__", attribute.get(i - 1));
                hiddendata_new_form += "<input type='hidden' name='filter_condition' value='" + attribute.get(i - 1) + "' /> ";
            }
            if (query != null && !"".equals(query)) {
                db.read(query);
                db.rs.next();
            }
            db.read1("SELECT fa_id,fa_name,fa_attribute,fa_type,fa_list_query,fa_validation,fa_function,script,fa_validation FROM camps.form_attribute fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "' order by fm.order_no");
            while (db.rs1.next()) {
                switch (db.rs1.getString("fa_type")) {
                    case "text":
                        data += "<div class=\"form-group form-float\">"
                                + "<div class=\"form-line\">"
                                + "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + "<label class=\"form-label\">" + db.rs1.getString("fa_name") + "</label>"
                                + "</div>"
                                + "</div>";
                        break;
                    case "label":
                        data += "<div class=\"form-group form-float\">"
                                + "<div class=\"form-line\">"
                                + "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" readonly value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + "<label class=\"form-label\">" + db.rs1.getString("fa_name") + "</label>"
                                + "</div>"
                                + "</div>";
                        break;
                    case "date":
                        data += "<div class=\"form-group form-float\">"
                                + "<div class=\"form-line\">"
                                + "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + "<label class=\"form-label\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<script>$('#" + db.rs1.getString("fa_id") + "').datepicker({    format: \"dd-mm-yyyy\", autoclose: true});</script></div>"
                                + "</div>";
                        break;
                    case "select":
                        data += "<div class=\"form-group form-float\">"
                                + "<div class=\"form-line focused\">"
                                + " <select name=\"" + db.rs1.getString("fa_id") + "\" data-live-search=\"true\" data-size=\"5\" id=\"" + db.rs1.getString("fa_id") + "\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option selected disabled value=\"\"><--Select--></option>";
                        String list_query = db.rs1.getString("fa_list_query");
                        for (int i = 1; i <= attribute.size(); i++) {
                            list_query = list_query.replaceAll("__" + i + "__", attribute.get(i - 1));
                        }
                        db.read2(list_query);
                        while (db.rs2.next()) {
                            data += "<option value='" + db.rs2.getString("opt") + "'  " + (!db.rs.isAfterLast() && db.rs.getString(db.rs1.getString("fa_attribute")).equalsIgnoreCase(db.rs2.getString("opt")) ? " selected " : "") + "   >" + db.rs2.getString("dis") + "</option>";
                        }
                        data += "</select>"
                                + "<label class=\"form-label\">" + db.rs1.getString("fa_name") + "</label>"
                                + "</div>"
                                + "</div>";
                        break;
                    case "select-multiple":
                        data += "<div class=\"form-group form-float\">"
                                + "<div class=\"form-line focused\">"
                                + " <select name=\"" + db.rs1.getString("fa_id") + "\" multiple data-live-search=\"true\" data-size=\"5\" id=\"" + db.rs1.getString("fa_id") + "\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option value=\"\"><--Select--></option>";
                        list_query = db.rs1.getString("fa_list_query");
                        for (int i = 1; i <= attribute.size(); i++) {
                            list_query = list_query.replaceAll("__" + i + "__", attribute.get(i - 1));
                        }
                        db.read2(list_query);
                        while (db.rs2.next()) {
                            data += "<option value='" + db.rs2.getString("opt") + "'>" + db.rs2.getString("dis") + "</option>";
                        }
                        data += "</select><script>$('#" + db.rs1.getString("fa_id") + "').val([" + db.rs.getString(db.rs1.getString("fa_attribute")) + "]).selectpicker('refresh');</script>"
                                + "<label class=\"form-label\">" + db.rs1.getString("fa_name") + "</label>"
                                + "</div>"
                                + "</div>";
                        break;
                    case "radio":
                        data += "<div class=\"form-group form-float\">"
                                + ""
                                + " ";
                        list_query = db.rs1.getString("fa_list_query");
                        for (int i = 1; i <= attribute.size(); i++) {
                            list_query = list_query.replaceAll("__" + i + "__", attribute.get(i - 1));
                        }
                        db.read2(list_query);
                        while (db.rs2.next()) {
                            data += "<input type=\"radio\" name=\"" + db.rs1.getString("fa_id") + "\" value='" + db.rs2.getString("opt") + "' id=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"with-gap\" " + db.rs1.getString("fa_validation") + ">"
                                    + "<label for=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"form-label\">" + db.rs2.getString("dis") + "</label>";
                        }
                        data += "</div>";
                }
                data += "<script>" + db.rs1.getString("script") + "</script>";
            }
            data += button + hiddendata_new_form + "</form>";
        } catch (Exception e) {
            return e.toString();
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    public String form_horizontal_v1(String fm_id, ArrayList<String> attribute) {
        DBConnect db = new DBConnect();
        String query = "", hiddendata_new_form = "", display_type = "", data = "", button = "";
        try {
            db.getConnection();
            db.read("SELECT fm.fm_id,fm.operation_type,fm.form_query,fm.action_url,fm.button_details FROM camps.form_master fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'");
            if (db.rs.next()) {
                query = db.rs.getString("form_query");
//                display_type = db.rs.getString("display_type");
//                if (db.rs.getInt("replace_var_count") != attribute.size()) {
//                    return "";
//                }
                data += "<form class=\"form-horizontal\" role=\"form\" id='form_" + db.rs.getString("fm_id") + "' method='post' action='" + db.rs.getString("action_url") + "'><input type='hidden' id='form_id' name='form_id' value='" + db.rs.getString("fm_id") + "' />";
                button += db.rs.getString("button_details");
            }
            for (int i = 1; i <= attribute.size(); i++) {
                query = query.replaceAll("__" + i + "__", attribute.get(i - 1));
                hiddendata_new_form += "<input type='hidden' name='filter_condition' value='" + attribute.get(i - 1) + "' /> ";
            }
            if (query != null && !"".equals(query)) {
                db.read(query);
                db.rs.next();
            }
            db.read1("SELECT fa_id,fa_name,fa_attribute,fa_type,fa_list_query,fa_validation,fa_function,script,fa_validation FROM camps.form_attribute fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "' order by fm.order_no");
            while (db.rs1.next()) {
                switch (db.rs1.getString("fa_type")) {
                    case "text":
                        data += "<div class=\"form-group form-float\"><label for=\"" + db.rs1.getString("fa_id") + "\" class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + ""
                                + "</div>"
                                + "</div>";
                        break;
                    case "label":
                        data += "<div class=\"form-group form-float\"> <label for=\"" + db.rs1.getString("fa_id") + "\"  class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" style=\"color: #2e2d2d;font-weight: bold;\" readonly value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + ""
                                + "</div>"
                                + "</div>";
                        break;
                    case "date":
                        data += "<div class=\"form-group form-float\"> <label for=\"" + db.rs1.getString("fa_id") + "\"  class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + ""
                                + "<script>$('#" + db.rs1.getString("fa_id") + "').datepicker({    format: \"dd-mm-yyyy\", autoclose: true});</script></div>"
                                + "</div>";
                        break;
                    case "select":
                        data += "<div class=\"form-group form-float\"> <label for=\"" + db.rs1.getString("fa_id") + "\"  class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + " <select name=\"" + db.rs1.getString("fa_id") + "\" data-live-search=\"true\" data-size=\"5\" id=\"" + db.rs1.getString("fa_id") + "\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option selected disabled value=\"\"><--Select--></option>";
                        String list_query = db.rs1.getString("fa_list_query");
                        for (int i = 1; i <= attribute.size(); i++) {
                            list_query = list_query.replaceAll("__" + i + "__", attribute.get(i - 1));
                        }
                        db.read2(list_query);
                        while (db.rs2.next()) {
                            data += "<option value='" + db.rs2.getString("opt") + "'  " + (!db.rs.isAfterLast() && db.rs.getString(db.rs1.getString("fa_attribute")).equalsIgnoreCase(db.rs2.getString("opt")) ? " selected " : "") + "   >" + db.rs2.getString("dis") + "</option>";
                        }
                        data += "</select>"
                                + ""
                                + "</div>"
                                + "</div>";
                        break;
                    case "select-multiple":
                        data += "<div class=\"form-group form-float\"> <label for=\"" + db.rs1.getString("fa_id") + "\"  class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + " <select name=\"" + db.rs1.getString("fa_id") + "\" multiple data-live-search=\"true\" data-size=\"5\" id=\"" + db.rs1.getString("fa_id") + "\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option selected disabled value=\"\"><--Select--></option>";
                        list_query = db.rs1.getString("fa_list_query");
                        for (int i = 1; i <= attribute.size(); i++) {
                            list_query = list_query.replaceAll("__" + i + "__", attribute.get(i - 1));
                        }
                        db.read2(list_query);
                        while (db.rs2.next()) {
                            data += "<option value='" + db.rs2.getString("opt") + "'    >" + db.rs2.getString("dis") + "</option>";
                        }
                        data += "</select><script>$('#" + db.rs1.getString("fa_id") + "').val([" + db.rs.getString(db.rs1.getString("fa_attribute")) + "]).selectpicker('refresh');</script>"
                                + ""
                                + "</div>"
                                + "</div>";
                        break;
                    case "radio":
                        data += "<div class=\"form-group form-float\">"
                                + ""
                                + " ";
                        list_query = db.rs1.getString("fa_list_query");
                        for (int i = 1; i <= attribute.size(); i++) {
                            list_query = list_query.replaceAll("__" + i + "__", attribute.get(i - 1));
                        }
                        db.read2(list_query);
                        while (db.rs2.next()) {
                            data += "<input type=\"radio\" name=\"" + db.rs1.getString("fa_id") + "\" value='" + db.rs2.getString("opt") + "' id=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"with-gap\" " + db.rs1.getString("fa_validation") + ">"
                                    + "<label for=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"form-label\">" + db.rs2.getString("dis") + "</label>";
                        }
                        data += "</div>";
                }
                data += "<script>" + db.rs1.getString("script") + "</script>";
            }
            data += button + hiddendata_new_form + "</form>";
        } catch (Exception e) {
            return e.toString();
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    public String form_horizontal_2col_v1(String fm_id, ArrayList<String> attribute) {
        DBConnect db = new DBConnect();
        String query = "", hiddendata_new_form = "", display_type = "", data = "", button = "";
        try {
            db.getConnection();
            db.read("SELECT fm.fm_id,fm.operation_type,fm.form_query,fm.action_url,fm.button_details FROM camps.form_master fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'");
            if (db.rs.next()) {
                query = db.rs.getString("form_query");
//                display_type = db.rs.getString("display_type");
//                if (db.rs.getInt("replace_var_count") != attribute.size()) {
//                    return "";
//                }
                data += "<form class=\"form-horizontal\" role=\"form\" id='form_" + db.rs.getString("fm_id") + "' method='post' action='" + db.rs.getString("action_url") + "'><input type='hidden' id='form_id' name='form_id' value='" + db.rs.getString("fm_id") + "' />";
                button += db.rs.getString("button_details");
            }
            for (int i = 1; i <= attribute.size(); i++) {
                query = query.replaceAll("__" + i + "__", attribute.get(i - 1));
                hiddendata_new_form += "<input type='hidden' name='filter_condition' value='" + attribute.get(i - 1) + "' /> ";
            }
            if (query != null && !"".equals(query)) {
                db.read(query);
                db.rs.next();
            }
            db.read1("SELECT fa_id,fa_name,fa_attribute,fa_type,fa_list_query,fa_validation,fa_function,script,fa_validation FROM camps.form_attribute fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "' order by fm.order_no");
            while (db.rs1.next()) {
                data += "<div class=\"col-xs-12 ol-sm-6 col-md-6 col-lg-6\" >";
                switch (db.rs1.getString("fa_type")) {
                    case "text":
                        data += "<div class=\"form-group form-float\"><label for=\"" + db.rs1.getString("fa_id") + "\" class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + ""
                                + "</div>"
                                + "</div>";
                        break;
                    case "label":
                        data += "<div class=\"form-group form-float\"> <label for=\"" + db.rs1.getString("fa_id") + "\"  class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" style=\"color: #2e2d2d;font-weight: bold;\" readonly value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + ""
                                + "</div>"
                                + "</div>";
                        break;
                    case "date":
                        data += "<div class=\"form-group form-float\"> <label for=\"" + db.rs1.getString("fa_id") + "\"  class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + ""
                                + "<script>$('#" + db.rs1.getString("fa_id") + "').datepicker({    format: \"dd-mm-yyyy\", autoclose: true});</script></div>"
                                + "</div>";
                        break;
                    case "select":
                        data += "<div class=\"form-group form-float\"> <label for=\"" + db.rs1.getString("fa_id") + "\"  class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + " <select name=\"" + db.rs1.getString("fa_id") + "\" data-live-search=\"true\" data-size=\"5\" id=\"" + db.rs1.getString("fa_id") + "\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option selected disabled value=\"\"><--Select--></option>";
                        String list_query = db.rs1.getString("fa_list_query");
                        for (int i = 1; i <= attribute.size(); i++) {
                            list_query = list_query.replaceAll("__" + i + "__", attribute.get(i - 1));
                        }
                        db.read2(list_query);
                        while (db.rs2.next()) {
                            data += "<option value='" + db.rs2.getString("opt") + "'  " + (!db.rs.isAfterLast() && db.rs.getString(db.rs1.getString("fa_attribute")).equalsIgnoreCase(db.rs2.getString("opt")) ? " selected " : "") + "   >" + db.rs2.getString("dis") + "</option>";
                        }
                        data += "</select>"
                                + ""
                                + "</div>"
                                + "</div>";
                        break;
                    case "select-multiple":
                        data += "<div class=\"form-group form-float\"> <label for=\"" + db.rs1.getString("fa_id") + "\"  class=\"control-label col-sm-4\">" + db.rs1.getString("fa_name") + "</label>"
                                + "<div class=\"col-sm-8\">"
                                + " <select name=\"" + db.rs1.getString("fa_id") + "\" multiple data-live-search=\"true\" data-size=\"5\" id=\"" + db.rs1.getString("fa_id") + "\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option selected disabled value=\"\"><--Select--></option>";
                        list_query = db.rs1.getString("fa_list_query");
                        for (int i = 1; i <= attribute.size(); i++) {
                            list_query = list_query.replaceAll("__" + i + "__", attribute.get(i - 1));
                        }
                        db.read2(list_query);
                        while (db.rs2.next()) {
                            data += "<option value='" + db.rs2.getString("opt") + "'    >" + db.rs2.getString("dis") + "</option>";
                        }
                        data += "</select><script>$('#" + db.rs1.getString("fa_id") + "').val([" + db.rs.getString(db.rs1.getString("fa_attribute")) + "]).selectpicker('refresh');</script>"
                                + ""
                                + "</div>"
                                + "</div>";
                        break;
                    case "radio":
                        data += "<div class=\"form-group form-float\">"
                                + ""
                                + " ";
                        list_query = db.rs1.getString("fa_list_query");
                        for (int i = 1; i <= attribute.size(); i++) {
                            list_query = list_query.replaceAll("__" + i + "__", attribute.get(i - 1));
                        }
                        db.read2(list_query);
                        while (db.rs2.next()) {
                            data += "<input type=\"radio\" name=\"" + db.rs1.getString("fa_id") + "\" value='" + db.rs2.getString("opt") + "' id=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"with-gap\" " + db.rs1.getString("fa_validation") + ">"
                                    + "<label for=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"form-label\">" + db.rs2.getString("dis") + "</label>";
                        }
                        data += "</div>";
                }
                data += "<script>" + db.rs1.getString("script") + "</script></div>";
            }
            data += button + hiddendata_new_form + "</form>";
        } catch (Exception e) {
            return e.toString();
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    public String form_grid_v1(String fm_id, ArrayList<String> attribute) {
        DBConnect db = new DBConnect();
        String query = "", hiddendata_new_form = "", data = "", refresh_function = "", action_url = "";
        ArrayList<String> fa = new ArrayList<>();
        ArrayList<String> fa_values = new ArrayList<>();
        try {
            db.getConnection();
            db.read("SELECT fm.fm_id,fm.operation_type,fm.form_query,fm.action_url,fm.button_details,IFNULL(refresh_function,'')refresh_function FROM camps.form_master fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'");
            if (db.rs.next()) {
                query = db.rs.getString("form_query");
//                display_type = db.rs.getString("display_type");
//                if (db.rs.getInt("replace_var_count") != attribute.size()) {
//                    return "";
//                }

                refresh_function += db.rs.getString("refresh_function");
                action_url = db.rs.getString("action_url");

            }
            for (int i = 1; i <= attribute.size(); i++) {
                query = query.replaceAll("__" + i + "__", attribute.get(i - 1));
                refresh_function = refresh_function.replaceAll("__" + i + "__", attribute.get(i - 1));
                hiddendata_new_form += "<input type='hidden' name='filter_condition' value='" + attribute.get(i - 1) + "' /> ";
            }
            if (query != null && !"".equals(query)) {
                db.read(query);
            }
            data += "<form id='form_" + fm_id + "' method='post' action='" + action_url + "'><table width='100%' class='tbl_new' style=\"border-spacing: 2px;border-collapse: separate;\">";
            db.read1("SELECT fa_id,fa_name,fa_attribute,fa_type,fa_list_query,fa_validation,fa_function,script,fa_validation FROM camps.form_attribute fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "' order by fm.order_no");
            data += "<tr>";
            while (db.rs1.next()) {
                data += "<th>" + db.rs1.getString("fa_name") + "</th>";
            }
            data += "</tr>";
            int i = 0;
            while (db.rs.next()) {
                data += "<tr id='tr_" + fm_id + "_" + db.rs.getString("id") + "'><form id='form_" + db.rs.getString("id") + "' method='post' action='" + action_url + "'><input type='hidden' id='form_id' name='form_id' value='" + fm_id + "' /><input type='hidden' id='update_id' name='update_id' value='" + db.rs.getString("id") + "' />";
                db.read1("SELECT fa_id,fa_name,fa_attribute,fa_type,fa_list_query,fa_validation,fa_function,script,fa_validation FROM camps.form_attribute fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'  order by fm.order_no");

                while (db.rs1.next()) {
                    data += "<td><div class=\"form-group form-float\"><div class=\"form-line\">";
                    fa.add(db.rs1.getString("fa_id"));
                    fa_values.add((db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))));
                    switch (db.rs1.getString("fa_type")) {
                        case "text":
                            data += "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />";
                            break;
                        case "label":
                            data += "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" readonly value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />";
                            break;
                        case "date":
                            data += "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                    + "<script>$('#" + i + "_" + db.rs1.getString("fa_id") + "').datepicker({    format: \"dd-mm-yyyy\", autoclose: true});</script>";
                            break;
                        case "select":
                            data += "<select name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" data-size=\"5\" data-live-search=\"true\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option  selected disabled value=\"\"><--Select--></option>";
                            String qry1 = db.rs1.getString("fa_list_query");
                            if (qry1.contains("__")) {
                                for (int x = 0; x < fa.size(); x++) {
                                    qry1 = qry1.replaceAll("__fa" + fa.get(x) + "__", fa_values.get(x));
                                }
                                for (int x = 1; x <= attribute.size(); x++) {
                                    qry1 = qry1.replaceAll("__" + x + "__", attribute.get(x - 1));
                                }
                            }
                            db.read2(qry1);
                            while (db.rs2.next()) {
                                data += "<option value='" + db.rs2.getString("opt") + "'  " + (!db.rs.isAfterLast() && db.rs.getString(db.rs1.getString("fa_attribute")).equalsIgnoreCase(db.rs2.getString("opt")) ? " selected " : "") + "   >" + db.rs2.getString("dis") + "</option>";
                            }
                            data += "</select>";
                            break;
                        case "select-multiple":
                            data += "<select name=\"" + db.rs1.getString("fa_id") + "\" multiple id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" data-size=\"5\" data-live-search=\"true\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " >";
                            qry1 = db.rs1.getString("fa_list_query");
                            if (qry1.contains("__")) {
                                for (int x = 0; x < fa.size(); x++) {
                                    qry1 = qry1.replaceAll("__fa" + fa.get(x) + "__", fa_values.get(x));
                                }
                                for (int x = 1; x <= attribute.size(); x++) {
                                    qry1 = qry1.replaceAll("__" + x + "__", attribute.get(x - 1));
                                }
                            }
                            db.read2(qry1);
                            while (db.rs2.next()) {
                                data += "<option value='" + db.rs2.getString("opt") + "' " + (!db.rs.isAfterLast() && ("'" + db.rs.getString(db.rs1.getString("fa_attribute")) + "'").contains(db.rs2.getString("opt")) ? " selected " : "") + "   >" + db.rs2.getString("dis") + "</option>";
                            }
                            data += "</select><script>$('#" + i + "_" + db.rs1.getString("fa_id") + "').val([" + db.rs.getString(db.rs1.getString("fa_attribute")) + "]).selectpicker('refresh');</script>";
                            break;

                        case "radio":
                            data += "";
                            db.read2(db.rs1.getString("fa_list_query"));
                            while (db.rs2.next()) {
                                data += "<input type=\"radio\" name=\"" + db.rs1.getString("fa_id") + "\" value='" + db.rs2.getString("opt") + "' id=\"" + i + "_" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"with-gap\" " + db.rs1.getString("fa_validation") + ">"
                                        + "<label for=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"form-label\">" + db.rs2.getString("dis") + "</label>";
                            }
                    }
                    data += "<script>" + db.rs1.getString("script") + "</script></div></div></td>";

                }
                i++;
                data += "<td><div class=\"form-group form-float\"><div class=\"\"><button class=\"btn btn-success waves-effect\" name=\"edit\" id=\"edit_" + db.rs.getString("id") + "\" value='Update' onclick=\"edit_grid_" + fm_id + "('" + db.rs.getString("id") + "')\"  type=\"button\">Update</button> <button class=\"btn btn-danger waves-effect\" name=\"delete\" id=\"delete_" + db.rs.getString("id") + "\" value='Delete' onclick=\"delete_grid_" + fm_id + "('" + db.rs.getString("id") + "')\"  type=\"button\">Delete</button></form></div></div></td> </tr>";
            }
            data += "<tr id='tr_new_" + fm_id + "'><form id='form_new' method='post' action='" + action_url + "'><input type='hidden' id='form_id' name='form_id' value='" + fm_id + "' />";

            db.read1("SELECT fa_id,fa_name,fa_attribute,fa_type,fa_list_query,fa_validation,fa_function,script,fa_validation FROM camps.form_attribute fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "' order by fm.order_no");
            while (db.rs1.next()) {
                data += "<td><div class=\"form-group form-float\"><div class=\"form-line\">";
                switch (db.rs1.getString("fa_type")) {
                    case "text":
                        data += "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" value='' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />";
                        break;
                    case "label":
                        data += "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" readonly value='' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />";
                        break;
                    case "date":
                        data += "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" value='' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                + "<script>$('#" + db.rs1.getString("fa_id") + "').datepicker({    format: \"dd-mm-yyyy\", autoclose: true});</script>";
                        break;
                    case "select":
                        data += "<select name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + db.rs1.getString("fa_id") + "\" data-size=\"5\" data-live-search=\"true\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option  selected disabled value=\"\"><--Select--></option>";
                        String qry1 = db.rs1.getString("fa_list_query");
                        if (qry1.contains("__")) {
                            for (int x = 0; x < fa.size(); x++) {
                                qry1 = qry1.replaceAll("__fa" + fa.get(x) + "__", fa_values.get(x));
                            }
                            for (int x = 1; x <= attribute.size(); x++) {
                                qry1 = qry1.replaceAll("__" + x + "__", attribute.get(x - 1));
                            }
                        }
                        db.read2(qry1);
                        while (db.rs2.next()) {
                            data += "<option value='" + db.rs2.getString("opt") + "'    >" + db.rs2.getString("dis") + "</option>";
                        }
                        data += "</select>";
                        break;
                    case "select-multiple":
                        data += "<select name=\"" + db.rs1.getString("fa_id") + "\" multiple id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" data-size=\"5\" data-live-search=\"true\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " >";
                        qry1 = db.rs1.getString("fa_list_query");
                        if (qry1.contains("__")) {
                            for (int x = 0; x < fa.size(); x++) {
                                qry1 = qry1.replaceAll("__fa" + fa.get(x) + "__", fa_values.get(x));
                            }
                            for (int x = 1; x <= attribute.size(); x++) {
                                qry1 = qry1.replaceAll("__" + x + "__", attribute.get(x - 1));
                            }
                        }
                        db.read2(qry1);
                        while (db.rs2.next()) {
                            data += "<option value='" + db.rs2.getString("opt") + "' >" + db.rs2.getString("dis") + "</option>";
                        }
                        data += "</select>";
                        break;
                    case "radio":
                        data += "";
                        db.read2(db.rs1.getString("fa_list_query"));
                        while (db.rs2.next()) {
                            data += "<input type=\"radio\" name=\"" + db.rs1.getString("fa_id") + "\" value='" + db.rs2.getString("opt") + "' id=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"with-gap\" " + db.rs1.getString("fa_validation") + ">"
                                    + "<label for=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"form-label\">" + db.rs2.getString("dis") + "</label>";
                        }
                }
                data += "<script>" + db.rs1.getString("script") + "</script></div></div></td>";
            }
            data += "<td ><div class=\"form-group form-float\"><div class=\"\"> " + hiddendata_new_form + "<button class=\"btn btn-warning waves-effect\" name=\"add\" id=\"add\" value='Add' onclick=\"add_grid_" + fm_id + "(" + fm_id + ")\"  type=\"button\">Add</button></div></div></td> </tr>";

            data += "</table></form><script> function edit_grid_" + fm_id + "(form){ if($('#tr_" + fm_id + "_'+form +' :input').valid()){ var data_string=$('#tr_" + fm_id + "_'+form +' :input,#tr_" + fm_id + "_'+form +' :selected').serialize();  $.ajax({   type: \"POST\",url: '" + action_url + "',data: data_string+'&option=edit_grid',success: function(data){ $('#result_process').load('../../CommonJSP/alert_message.jsp?am_c='+data);" + refresh_function + " }});}}function delete_grid_" + fm_id + "(form){ var data_string=$('#tr_" + fm_id + "_'+form +' :input,#tr_" + fm_id + "_'+form +' :selected').serialize(); $.ajax({   type: \"POST\",url: '" + action_url + "',data: data_string+'&option=delete_grid',success: function(data){$('#result_process').load('../../CommonJSP/alert_message.jsp?am_c='+data);" + refresh_function + " }});}function add_grid_" + fm_id + "(form){ if($('#tr_new_'+form +' :input').valid()){ var data_string=$('#tr_new_'+form +' :input,#tr_new select').serialize();$.ajax({   type: \"POST\",url: '" + action_url + "',data: data_string+'&option=add_grid',success: function(data){$('#result_process').load('../../CommonJSP/alert_message.jsp?am_c='+data);" + refresh_function + " }});}}</script>";
        } catch (Exception e) {
            return e.toString();
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    public String form_grid_v1_withoutadd(String fm_id, ArrayList<String> attribute) {
        DBConnect db = new DBConnect();
        String query = "", hiddendata_new_form = "", script = "", data = "", refresh_function = "", action_url = "";
        int update_flag = 0, delete_flag = 0, freeze_flag = 0;
        try {
            db.getConnection();
            db.read("SELECT fm.fm_id,fm.operation_type,fm.form_query,fm.action_url,fm.button_details,IFNULL(refresh_function,'')refresh_function FROM camps.form_master fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'");
            if (db.rs.next()) {
                query = db.rs.getString("form_query");
//                display_type = db.rs.getString("display_type");
//                if (db.rs.getInt("replace_var_count") != attribute.size()) {
//                    return "";
//                }

                refresh_function += db.rs.getString("refresh_function");
                action_url = db.rs.getString("action_url");

            }
            for (int i = 1; i <= attribute.size(); i++) {
                query = query.replaceAll("__" + i + "__", attribute.get(i - 1));
                refresh_function = refresh_function.replaceAll("__" + i + "__", attribute.get(i - 1));
                hiddendata_new_form += "<input type='hidden' name='filter_condition' value='" + attribute.get(i - 1) + "' /> ";
            }
            if (query != null && !"".equals(query)) {
                db.read(query);
            }
            data += "<form id='form_" + fm_id + "' method='post' action='" + action_url + "'><table width='100%' class='tbl_new_new' style=\"border-spacing: 2px;border-collapse: separate;\">";
            db.read1("SELECT fa_id,fa_name,fa_attribute,fa_type,fa_list_query,fa_validation,fa_function,script,fa_validation FROM camps.form_attribute fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'");
            data += "<tr>";
            while (db.rs1.next()) {
                data += "<th>" + db.rs1.getString("fa_name") + "</th>";
            }
            data += "</tr>";
            int i = 0;
            while (db.rs.next()) {
                data += "<tr id='tr_" + fm_id + "_" + db.rs.getString("id") + "'><form id='form_" + db.rs.getString("id") + "' method='post' action='" + action_url + "'><input type='hidden' id='form_id' name='form_id' value='" + fm_id + "' /><input type='hidden' id='update_id' name='update_id' value='" + db.rs.getString("id") + "' />";
                db.read1("SELECT fa_id,fa_name,fa_attribute,fa_type,fa_list_query,fa_validation,fa_function,script,fa_validation FROM camps.form_attribute fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'");

                while (db.rs1.next()) {
                    data += "<td><div class=\"form-group form-float\"><div class=\"form-line\">";
                    switch (db.rs1.getString("fa_type")) {
                        case "text":
                            data += "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />";
                            break;
                        case "label":
                            data += "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" readonly value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />";
                            break;
                        case "date":
                            data += "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                    + "<script>$('#" + i + "_" + db.rs1.getString("fa_id") + "').datepicker({    format: \"dd-mm-yyyy\", autoclose: true});</script>";
                            break;
                        case "select":
                            data += "<select name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" data-size=\"5\" data-live-search=\"true\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option  selected disabled value=\"\"><--Select--></option>";
                            db.read2(db.rs1.getString("fa_list_query"));
                            while (db.rs2.next()) {
                                data += "<option value='" + db.rs2.getString("opt") + "'  " + (!db.rs.isAfterLast() && db.rs.getString(db.rs1.getString("fa_attribute")).equalsIgnoreCase(db.rs2.getString("opt")) ? " selected " : "") + "   >" + db.rs2.getString("dis") + "</option>";
                            }
                            data += "</select>";
                            break;
                        case "select-multiple":
                            data += "<select name=\"" + db.rs1.getString("fa_id") + "\" multiple id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" data-size=\"5\" data-live-search=\"true\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option  selected disabled value=\"\"><--Select--></option>";
                            db.read2(db.rs1.getString("fa_list_query"));
                            while (db.rs2.next()) {
                                data += "<option value='" + db.rs2.getString("opt") + "'  " + (!db.rs.isAfterLast() && db.rs.getString(db.rs1.getString("fa_attribute")).equalsIgnoreCase(db.rs2.getString("opt")) ? " selected " : "") + "   >" + db.rs2.getString("dis") + "</option>";
                            }
                            data += "</select><script>$('#" + db.rs1.getString("fa_id") + "').val([" + db.rs.getString(db.rs1.getString("fa_attribute")) + "]).selectpicker('refresh');</script>";
                            break;
                        case "radio":
                            data += "";
                            db.read2(db.rs1.getString("fa_list_query"));
                            while (db.rs2.next()) {
                                data += "<input type=\"radio\" name=\"" + db.rs1.getString("fa_id") + "\" value='" + db.rs2.getString("opt") + "' id=\"" + i + "_" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"with-gap\" " + db.rs1.getString("fa_validation") + ">"
                                        + "<label for=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"form-label\">" + db.rs2.getString("dis") + "</label>";
                            }
                    }
                    data += "<script>" + db.rs1.getString("script") + "</script></div></div></td>";

                }
                i++;

                data += "<td><div class=\"form-group form-float\"><div class=\"\">";
                try {
                    if (db.rs.getString("update_button").equals("1")) {
                        data += "<button class=\"btn btn-success waves-effect\" name=\"edit\" id=\"edit_" + db.rs.getString("id") + "\" value='Update' onclick=\"edit_grid_" + fm_id + "('" + db.rs.getString("id") + "')\"  type=\"button\">Update</button> ";
                        if (update_flag == 0) {
                            script += "function edit_grid_" + fm_id + "(form){  if($('#tr_" + fm_id + "_'+form +' :input').valid()){ var data_string=$('#tr_" + fm_id + "_'+form +' :input,#tr_" + fm_id + "_'+form +' :selected').serialize(); $.ajax({   type: \"POST\",url: '" + action_url + "',data: data_string+'&option=edit_grid',success: function(data){ $('#result_process').load('../../CommonJSP/alert_message.jsp?am_c='+data);" + refresh_function + " }});}}";
                            update_flag = 1;
                        }
                    }

                } catch (SQLException sqlex) {
                    data += "<button class=\"btn btn-success waves-effect\" name=\"edit\" id=\"edit_" + db.rs.getString("id") + "\" value='Update' onclick=\"edit_grid_" + fm_id + "('" + db.rs.getString("id") + "')\"  type=\"button\">Update</button> ";
                    if (update_flag == 0) {
                        script += "function edit_grid_" + fm_id + "(form){  if($('#tr_" + fm_id + "_'+form +' :input').valid()){ var data_string=$('#tr_" + fm_id + "_'+form +' :input,#tr_" + fm_id + "_'+form +' :selected').serialize(); $.ajax({   type: \"POST\",url: '" + action_url + "',data: data_string+'&option=edit_grid',success: function(data){ $('#result_process').load('../../CommonJSP/alert_message.jsp?am_c='+data);" + refresh_function + " }});}}";
                        update_flag = 1;
                    }
                }
                try {
                    if (db.rs.getString("delete_button").equals("1")) {
                        data += "<button class=\"btn btn-danger waves-effect\" name=\"delete\" id=\"delete_" + db.rs.getString("id") + "\" value='Delete' onclick=\"delete_grid_" + fm_id + "('" + db.rs.getString("id") + "')\"  type=\"button\">Delete</button>";
                        if (delete_flag == 0) {
                            script += "function delete_grid_" + fm_id + "(form){ var data_string=$('#tr_" + fm_id + "_'+form +' :input,#tr_" + fm_id + "_'+form +' :selected').serialize(); $.ajax({   type: \"POST\",url: '" + action_url + "',data: data_string+'&option=delete_grid',success: function(data){$('#result_process').load('../../CommonJSP/alert_message.jsp?am_c='+data);" + refresh_function + " }});}";
                            delete_flag = 1;
                        }
                    }

                } catch (SQLException sqlex) {
                    data += "<button class=\"btn btn-danger waves-effect\" name=\"delete\" id=\"delete_" + db.rs.getString("id") + "\" value='Delete' onclick=\"delete_grid_" + fm_id + "('" + db.rs.getString("id") + "')\"  type=\"button\">Delete</button>";
                    if (delete_flag == 0) {
                        script += "function delete_grid_" + fm_id + "(form){ var data_string=$('#tr_" + fm_id + "_'+form +' :input,#tr_" + fm_id + "_'+form +' :selected').serialize(); $.ajax({   type: \"POST\",url: '" + action_url + "',data: data_string+'&option=delete_grid',success: function(data){$('#result_process').load('../../CommonJSP/alert_message.jsp?am_c='+data);" + refresh_function + " }});}";
                        delete_flag = 1;
                    }
                }
                try {
                    if (db.rs.getString("freeze_button").equals("1")) {
                        data += "<button class=\"btn btn-danger waves-effect\" name=\"delete\" id=\"delete_" + db.rs.getString("id") + "\" value='Delete' onclick=\"delete_grid_" + fm_id + "('" + db.rs.getString("id") + "')\"  type=\"button\">Delete</button>";
                        if (delete_flag == 0) {
                            script += "function freeze_grid_" + fm_id + "(form){ var data_string=$('#tr_" + fm_id + "_'+form +' :input,#tr_" + fm_id + "_'+form +' :selected').serialize(); $.ajax({   type: \"POST\",url: '" + action_url + "',data: data_string+'&option=freeze_grid',success: function(data){$('#result_process').load('../../CommonJSP/alert_message.jsp?am_c='+data);" + refresh_function + " }});}";
                            delete_flag = 1;
                        }
                    }

                } catch (SQLException sqlex) {
                }

                data += "</form></div></div></td> </tr>" + hiddendata_new_form;
            }
            data += "</table></form><script> " + script + "</script>";
        } catch (Exception e) {
            return e.toString();
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    public int form_Update_V1(HttpServletRequest request) {
        DBConnect db = new DBConnect();
        String query = "", display_type = "", data = "", button = "", filter_condition[];
        try {
            db.getConnection();
            db.read("SELECT insert_query FROM camps.form_master fm WHERE fm.fm_id='" + request.getParameter("form_id") + "' UNION ((SELECT fq.query FROM camps.form_query fq WHERE fq.fm_id='" + request.getParameter("form_id") + "' AND fq.status>0 AND fq.type='Update' order by fq.fq_id))");
            if (db.rs.next()) {
                query = db.rs.getString("insert_query");
                db.read1("SELECT fa.fa_id,fa.fa_type FROM camps.form_attribute fa WHERE fa.fm_id='" + request.getParameter("form_id") + "' AND fa.status=2");
                while (db.rs1.next()) {
                    if (request.getParameter(db.rs1.getString("fa_id")) != null && !request.getParameter(db.rs1.getString("fa_id")).equalsIgnoreCase("")) {

                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "STR_TO_DATE('" + request.getParameter(db.rs1.getString("fa_id")) + "','%d-%m-%Y')");

                                break;
                            default:
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", request.getParameter(db.rs1.getString("fa_id")));
                                break;
                        }
                    } else {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "NULL");
                                break;
                            default:
                                query = query.replaceAll("'__fa" + db.rs1.getString("fa_id") + "__'", "NULL");
                                break;
                        }
                    }
                }
                if (request.getParameterValues("filter_condition") != null) {
                    filter_condition = request.getParameterValues("filter_condition");
                    for (int i = 0; i < filter_condition.length; i++) {
                        query = query.replaceAll("__" + (i + 1) + "__", filter_condition[i]);
                    }
                }
                //query = query.replaceAll("__ses-user__",session.getAttribute("user_id").toString());
                db.update(query);
                return db.iEffectedRows > 0 ? 1 : 0;
            }
        } catch (Exception e) {
            return 0;
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public int form_Update_V2(HttpServletRequest request, HttpSession session) {
        DBConnect db = new DBConnect();
        String query = "", display_type = "", data = "", button = "", filter_condition[];
        try {
            db.getConnection();
            db.read("SELECT insert_query FROM camps.form_master fm WHERE fm.fm_id='" + request.getParameter("form_id") + "' UNION ((SELECT fq.query FROM camps.form_query fq WHERE fq.fm_id='" + request.getParameter("form_id") + "' AND fq.status>0 AND fq.type='Update' order by fq.fq_id))");
            while (db.rs.next()) {
                query = db.rs.getString("insert_query");
                db.read1("SELECT fa.fa_id,fa.fa_type FROM camps.form_attribute fa WHERE fa.fm_id='" + request.getParameter("form_id") + "' AND fa.status=2");
                while (db.rs1.next()) {
                    if (request.getParameter(db.rs1.getString("fa_id")) != null && !request.getParameter(db.rs1.getString("fa_id")).equalsIgnoreCase("")) {

                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "STR_TO_DATE('" + request.getParameter(db.rs1.getString("fa_id")) + "','%d-%m-%Y')");

                                break;

                            default:
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", String.join(",", request.getParameterValues(db.rs1.getString("fa_id"))));
                                break;
                        }
                    } else {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "NULL");
                                break;
                            default:
                                query = query.replaceAll("'__fa" + db.rs1.getString("fa_id") + "__'", "NULL");
                                break;
                        }
                    }
                }
                if (request.getParameterValues("filter_condition") != null) {
                    filter_condition = request.getParameterValues("filter_condition");
                    for (int i = 0; i < filter_condition.length; i++) {
                        query = query.replaceAll("__" + (i + 1) + "__", filter_condition[i]);
                    }
                }
                query = query.replaceAll("__ses-user__", session.getAttribute("user_id").toString());
                db.update(query);

            }
            return db.iEffectedRows > 0 ? 1 : 0;
        } catch (Exception e) {
            return 0;
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public int form_Insert_V1(HttpServletRequest request, HttpSession session) {
        DBConnect db = new DBConnect();
        DBConnect db1 = new DBConnect();
        String query = "", display_type = "", data = "", button = "", filter_condition[];
        int return_value = 1;
        ArrayList au_id = new ArrayList(), au_value = new ArrayList();
        try {
            db.getConnection();
            db1.getConnection();
            db.read("SELECT fq.query,fq.fq_id,fm.return_value FROM camps.form_master fm INNER JOIN camps.form_query fq ON fq.fm_id=fm.fm_id WHERE fm.fm_id='" + request.getParameter("form_id") + "' ORDER BY fq.fq_id");
            while (db.rs.next()) {
                query = db.rs.getString("query");
                au_id.add(db.rs.getString("fq_id"));

                db.read1("SELECT fa.fa_id,fa.fa_type FROM camps.form_attribute fa WHERE fa.fm_id='" + request.getParameter("form_id") + "' AND fa.status=2");
                while (db.rs1.next()) {
                    if (request.getParameter(db.rs1.getString("fa_id")) != null && !request.getParameter(db.rs1.getString("fa_id")).equalsIgnoreCase("")) {

                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "STR_TO_DATE('" + request.getParameter(db.rs1.getString("fa_id")) + "','%d-%m-%Y')");

                                break;
                            default:
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", String.join(",", request.getParameterValues(db.rs1.getString("fa_id"))));
                                break;
                        }
                    } else {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "NULL");
                                break;
                            default:
                                query = query.replaceAll("'__fa" + db.rs1.getString("fa_id") + "__'", "NULL");
                                break;
                        }
                    }
                }
                if (request.getParameterValues("filter_condition") != null) {
                    filter_condition = request.getParameterValues("filter_condition");
                    for (int i = 0; i < filter_condition.length; i++) {
                        query = query.replaceAll("__" + (i + 1) + "__", filter_condition[i]);
                    }
                }
                for (int i = 0; i < au_value.size(); i++) {
                    query = query.replaceAll("__au-id-" + au_id.get(i) + "__", (String) au_value.get(i));
                }
                query = query.replaceAll("__ses-user__", session.getAttribute("user_id").toString());
                //query = query.replaceAll("__ses-user__",session.getAttribute("user_id").toString());
                au_value.add(db1.insertAndGetAutoGenId(query));
                if (db.rs.getInt("return_value") == db.rs.getInt("fq_id")) {
                    return_value = Integer.parseInt(au_value.get(au_value.size() - 1).toString());
                }
            }
            return db1.iEffectedRows > 0 ? return_value : 0;
        } catch (Exception e) {
            return 0;
        } finally {

            try {
                db.closeConnection();
                db1.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //  return 0;
    }

    public int form_Grid_Update_V1_1(HttpServletRequest request, HttpSession session) {
        DBConnect db = new DBConnect();
        String query = "", display_type = "", data = "", button = "";
        try {
            db.getConnection();
            db.read("SELECT insert_query FROM camps.form_master fm WHERE fm.fm_id='" + request.getParameter("form_id") + "' UNION (SELECT fq.query FROM camps.form_query fq WHERE fq.fm_id='" + request.getParameter("form_id") + "' AND fq.status>0 AND fq.type='Update' order by fq.fq_id)");
            while (db.rs.next()) {
                query = db.rs.getString("insert_query");
                db.read1("SELECT fa.fa_id,fa.fa_type FROM camps.form_attribute fa WHERE fa.fm_id='" + request.getParameter("form_id") + "' AND fa.status=2");
                while (db.rs1.next()) {
                    if (request.getParameter(db.rs1.getString("fa_id")) != null && !request.getParameter(db.rs1.getString("fa_id")).equalsIgnoreCase("")) {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "STR_TO_DATE('" + request.getParameter(db.rs1.getString("fa_id")) + "','%d-%m-%Y')");
                                break;
                            case "select-multiple":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", String.join("','", request.getParameterValues(db.rs1.getString("fa_id"))));
                                break;
                            default:
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", request.getParameter(db.rs1.getString("fa_id")));
                                break;
                        }
                    } else {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "NULL");
                                break;

                            default:
                                query = query.replaceAll("'__fa" + db.rs1.getString("fa_id") + "__'", "NULL");
                                break;
                        }

                    }

                }
                query = query.replaceAll("__uid__", request.getParameter("update_id"));
                query = query.replaceAll("__ses-user__", session.getAttribute("user_id").toString());
                db.update(query);
            }
            return db.iEffectedRows > 0 ? 1 : 0;
        } catch (Exception e) {
            return 0;
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String form_Grid_Ajax_V1(HttpServletRequest request, HttpSession session) {
        DBConnect db = new DBConnect();
        String query = "", display_type = "", data = "";
        String filter_condition[] = null;
        try {
            db.getConnection();
            db.read("SELECT fa.fa_list_query FROM camps.form_attribute fa WHERE CONCAT('fa',fa.fa_id)='" + request.getParameter("update_field_details") + "'  AND fa.fm_id='" + request.getParameter("load_details") + "'");
            if (db.rs.next()) {
                String replace_field = request.getParameter("replace_data_field"), list_query = db.rs.getString("fa_list_query");
                for (String rf : replace_field.split(",")) {
                    list_query = list_query.replaceAll("__" + rf + "__", request.getParameter(rf));
                }
                db.read(list_query);
                while (db.rs.next()) {
                    data += "<option value='" + db.rs.getString("opt") + "'    >" + db.rs.getString("dis") + "</option>";
                }
            }
        } catch (Exception e) {
            return e.toString();
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "<option value=\"\"><--Select--></option>" + data;
    }

    public int form_Grid_Add_V1(HttpServletRequest request, HttpSession session) {
        DBConnect db = new DBConnect(), db1 = new DBConnect();
        String query = "", display_type = "", data = "";
        String filter_condition[] = null;
        ArrayList au_id = new ArrayList(), au_value = new ArrayList();
        int return_value = 1;
        try {
            db.getConnection();
            db1.getConnection();
            db.read("SELECT fq_id,QUERY,fm.return_value FROM camps.form_query fq INNER JOIN  camps.form_master fm ON fq.fm_id=fm.fm_id WHERE fq.fm_id='" + request.getParameter("form_id") + "' and fq.status>0 AND fq.type='insert'");
            while (db.rs.next()) {
                query = db.rs.getString("query");
                au_id.add(db.rs.getString("fq_id"));
                db.read1("SELECT fa.fa_id,fa.fa_type FROM camps.form_attribute fa WHERE fa.fm_id='" + request.getParameter("form_id") + "' and fa.status>0 ");
                while (db.rs1.next()) {
                    if (request.getParameter(db.rs1.getString("fa_id")) != null && !request.getParameter(db.rs1.getString("fa_id")).equalsIgnoreCase("")) {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "STR_TO_DATE('" + request.getParameter(db.rs1.getString("fa_id")) + "','%d-%m-%Y')");

                                break;
                            case "select-multiple":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", String.join("','", request.getParameterValues(db.rs1.getString("fa_id"))));
                                break;
                            default:
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", request.getParameter(db.rs1.getString("fa_id")));
                                break;
                        }
                    } else {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "NULL");
                                break;
                            default:
                                query = query.replaceAll("'__fa" + db.rs1.getString("fa_id") + "__'", "NULL");
                                break;
                        }
                    }

                }
                if (request.getParameterValues("filter_condition") != null) {
                    filter_condition = request.getParameterValues("filter_condition");
                    for (int i = 0; i < filter_condition.length; i++) {
                        query = query.replaceAll("__" + (i + 1) + "__", filter_condition[i]);
                    }
                }
                for (int i = 0; i < au_value.size(); i++) {
                    query = query.replaceAll("__au-id-" + au_id.get(i) + "__", (String) au_value.get(i));
                }
                query = query.replaceAll("__ses-user__", session.getAttribute("user_id").toString());
                //query = query.replaceAll("__ses-user__",session.getAttribute("user_id").toString());
                au_value.add(db1.insertAndGetAutoGenId(query));
                if (db.rs.getInt("return_value") == db.rs.getInt("fq_id")) {
                    return_value = Integer.parseInt(au_value.get(au_value.size() - 1).toString());
                }

            }
            return db1.iEffectedRows > 0 ? return_value : 0;
        } catch (Exception e) {
            return 0;
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //return 0;
    }

    public int form_Grid_Update_V1(HttpServletRequest request) {
        DBConnect db = new DBConnect();
        String query = "", display_type = "", data = "", button = "";
        try {
            db.getConnection();
            db.read("SELECT insert_query FROM camps.form_master fm WHERE fm.fm_id='" + request.getParameter("form_id") + "' UNION (SELECT fq.query FROM camps.form_query fq WHERE fq.fm_id='" + request.getParameter("form_id") + "' AND fq.status>0 AND fq.type='Update' order by fq.fq_id)");
            if (db.rs.next()) {
                query = db.rs.getString("insert_query");
                db.read1("SELECT fa.fa_id,fa.fa_type FROM camps.form_attribute fa WHERE fa.fm_id='" + request.getParameter("form_id") + "' AND fa.status=2");
                while (db.rs1.next()) {
                    if (request.getParameter(db.rs1.getString("fa_id")) != null && !request.getParameter(db.rs1.getString("fa_id")).equalsIgnoreCase("")) {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":

                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "STR_TO_DATE('" + request.getParameter(db.rs1.getString("fa_id")) + "','%d-%m-%Y')");

                                break;
                            default:
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", request.getParameter(db.rs1.getString("fa_id")));
                                break;
                        }
                    } else {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "NULL");
                                break;
                            default:
                                query = query.replaceAll("'__fa" + db.rs1.getString("fa_id") + "__'", "NULL");
                                break;
                        }

                    }

                }
                query = query.replaceAll("__uid__", request.getParameter("update_id"));

                db.update(query);
                return db.iEffectedRows > 0 ? 1 : 0;
            }
        } catch (Exception e) {
            return 0;
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public int form_Grid_Update_V2(HttpServletRequest request, HttpSession session) {
        DBConnect db = new DBConnect();
        String query = "", display_type = "", data = "", button = "";
        try {
            db.getConnection();
            db.read("SELECT insert_query FROM camps.form_master fm WHERE fm.fm_id='" + request.getParameter("form_id") + "' UNION (SELECT fq.query FROM camps.form_query fq WHERE fq.fm_id='" + request.getParameter("form_id") + "' AND fq.status>0 AND fq.type='Update' order by fq.fq_id)");
            if (db.rs.next()) {
                query = db.rs.getString("insert_query");
                db.read1("SELECT fa.fa_id,fa.fa_type FROM camps.form_attribute fa WHERE fa.fm_id='" + request.getParameter("form_id") + "' AND fa.status=2");
                while (db.rs1.next()) {
                    if (request.getParameter(db.rs1.getString("fa_id")) != null && !request.getParameter(db.rs1.getString("fa_id")).equalsIgnoreCase("")) {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":

                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "STR_TO_DATE('" + request.getParameter(db.rs1.getString("fa_id")) + "','%d-%m-%Y')");

                                break;
                            default:
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", request.getParameter(db.rs1.getString("fa_id")));
                                break;
                        }
                    } else {
                        switch (db.rs1.getString("fa_type")) {
                            case "date":
                                query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "NULL");
                                break;
                            default:
                                query = query.replaceAll("'__fa" + db.rs1.getString("fa_id") + "__'", "NULL");
                                break;
                        }
                    }
                }
                query = query.replaceAll("__uid__", request.getParameter("update_id"));
                query = query.replaceAll("__ses-user__", session.getAttribute("user_id").toString());
                db.update(query);
                return db.iEffectedRows > 0 ? 1 : 0;
            }
        } catch (Exception e) {
            return 0;
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public int form_Grid_Delete_V1(HttpServletRequest request, HttpSession session) {
        DBConnect db = new DBConnect();
        String query = "", display_type = "", data = "";
        String filter_condition[] = null;
        try {
            db.getConnection();
            db.read("SELECT QUERY FROM camps.form_query fq WHERE fq.fm_id='" + request.getParameter("form_id") + "' and fq.status>0 AND fq.type='delete'");
            if (db.rs.next()) {
                query = db.rs.getString("query");
                db.read1("SELECT fa.fa_id,fa.fa_type FROM camps.form_attribute fa WHERE fa.fm_id='" + request.getParameter("form_id") + "' and fa.status>0 ");
                while (db.rs1.next()) {
                    switch (db.rs1.getString("fa_type")) {
                        case "date":
                            query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", "STR_TO_DATE('" + request.getParameter(db.rs1.getString("fa_id")) + "','%d-%m-%Y')");
                            break;
                        default:
                            query = query.replaceAll("__fa" + db.rs1.getString("fa_id") + "__", request.getParameter(db.rs1.getString("fa_id")));
                            break;
                    }

                }
//                filter_condition=request.getParameterValues("filter_condition");
//                for(int i=0;i<filter_condition.length;i++){
//                    query = query.replaceAll("__"+(i+1)+"__", filter_condition[i]);
//                }
                query = query.replaceAll("__uid__", request.getParameter("update_id"));
                query = query.replaceAll("__ses-user__", session.getAttribute("user_id").toString());
                db.delete(query);
                return db.iEffectedRows > 0 ? 1 : 0;
            }
        } catch (Exception e) {
            return 0;
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public String form_grid_v2(String fm_id, ArrayList<String> attribute) {
        DBConnect db = new DBConnect();
        String query = "", hiddendata_new_form = "", data = "", refresh_function = "", action_url = "";
        int total = 0;
        try {
            db.getConnection();
            //   <form id='form_" + fm_id + "' method='post' action=\"fee_update_log\">
            data += "<table width='100%' class='tbl_new' style=\"border-spacing: 2px;border-collapse: separate;\">";
            data += "<tr><th>Fee head</th><th>Fixed Amount</th><th>Credit/Debit</th><th>Amount</th><th>Description</th></tr>";
            db.read("SELECT fee_head_id,fee_Type FROM student_fees.fee_header_master");
            while (db.rs.next()) {
                db.read1("SELECT fm.fm_id,fm.operation_type,fm.form_query,fm.action_url,fm.button_details,IFNULL(refresh_function,'')refresh_function FROM camps.form_master fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'");
                if (db.rs1.next()) {
                    query = db.rs1.getString("form_query");
//                display_type = db.rs.getString("display_type");
//                if (db.rs.getInt("replace_var_count") != attribute.size()) {
//                    return "";
//                }

                    refresh_function += db.rs1.getString("refresh_function");
                    action_url = db.rs1.getString("action_url");

                }
                int i = 0;
                for (i = 1; i <= attribute.size(); i++) {
                    query = query.replaceAll("__" + i + "__", attribute.get(i - 1));
                    refresh_function = refresh_function.replaceAll("__" + i + "__", attribute.get(i - 1));
                    hiddendata_new_form += "<input type='hidden' name='filter_condition' value='" + attribute.get(i - 1) + "' /> ";
                }
                query = query.replaceAll("__" + i + "__", db.rs.getString("fee_head_id"));
                if (query != null && !"".equals(query)) {
                    db.read2(query);
                }
                String amount = "";
                if (db.rs2.first()) {
                    amount = db.rs2.getString("amount");
                    total += db.rs2.getInt("amount");
                }
                int j = 0;
                //     while (db.rs2.next()) {
                data += "<tr id='tr_" + fm_id + "_" + db.rs.getString("fee_head_id") + "'>";
                data += "<td style='color:black;'><div class=\"form-group form-float\"><div class=\"form-line\">" + db.rs.getString("fee_Type") + "<input type=\"hidden\" name=\"fee_type[]\" id=\"" + db.rs.getString("fee_head_id") + "\" value=\"" + db.rs.getString("fee_head_id") + "\" /></div></div></td>";
                db.read1("SELECT fa_id,fa_name,fa_attribute,fa_type,fa_list_query,fa_validation,fa_function,script,fa_validation FROM camps.form_attribute fm WHERE fm.status>0 AND fm.fm_id='" + fm_id + "'");

                while (db.rs1.next()) {
                    data += "<td style='color:black;'><div class=\"form-group form-float\"><div class=\"form-line\">";
                    switch (db.rs1.getString("fa_type")) {
                        case "text":

                            // data += "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "[]\" id=\"" + j + "_" + db.rs1.getString("fa_id") + "\" class=\"form-control amt\" " + db.rs1.getString("fa_validation") + " />";
                            data += "<input type=\"text\" name=\"fa_" + db.rs1.getString("fa_id") + "[]\" id=\"" + db.rs.getString("fee_head_id") + "\" onchange='load_tot()' class=\"amt\" " + db.rs1.getString("fa_validation") + " value='0' />";
                            break;
                        case "label":
                            //   data += "<input type=\"text\" name=\"" + db.rs1.getString("fa_id") + "[]\" id=\"" + j + "_" + db.rs1.getString("fa_id") + "\" readonly value='" + amount + "' onchange='load_tot()' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />";
                            data += "<input type=\"text\" name=\"fa_" + db.rs1.getString("fa_id") + "[]\" id=\"fix_amt" + db.rs.getString("fee_head_id") + "\" readonly value='" + amount + "'  class=\"form-control\" " + db.rs1.getString("fa_validation") + " />";
                            break;
                        case "date":
                            data += "<input type=\"text\" pattern=\"[0-9]{2}-[0-9]{2}-[0-9]{4}\" name=\"" + db.rs1.getString("fa_id") + "\" id=\"" + j + "_" + db.rs1.getString("fa_id") + "\" value='" + (db.rs.isAfterLast() ? "" : db.rs.getString(db.rs1.getString("fa_attribute"))) + "' class=\"form-control\" " + db.rs1.getString("fa_validation") + " />"
                                    + "<script>$('#" + j + "_" + db.rs1.getString("fa_id") + "').bootstrapMaterialDatePicker({format: 'DD-MM-YYYY', weekStart: 0, time: false});</script>";
                            break;
                        case "select":
                            data += "<select name=\"fa_" + db.rs1.getString("fa_id") + "[]\" id=\"acc_type" + db.rs.getString("fee_head_id") + "\" data-live-search=\"true\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option value=\"0\"><--Select--></option>";
                            db.read2(db.rs1.getString("fa_list_query"));
                            while (db.rs2.next()) {
                                data += "<option value='" + db.rs2.getString("opt") + "'  >" + db.rs2.getString("dis") + "</option>";
                            }
                            data += "</select>";
                            break;
                        case "select-multiple":
                            data += "<select name=\"" + db.rs1.getString("fa_id") + "\" multiple id=\"" + i + "_" + db.rs1.getString("fa_id") + "\" data-size=\"5\" data-live-search=\"true\" class=\"form-control\" " + db.rs1.getString("fa_validation") + " " + db.rs1.getString("fa_function") + " ><option  selected disabled value=\"\"><--Select--></option>";
                            db.read2(db.rs1.getString("fa_list_query"));
                            while (db.rs2.next()) {
                                data += "<option value='" + db.rs2.getString("opt") + "'  " + (!db.rs.isAfterLast() && db.rs.getString(db.rs1.getString("fa_attribute")).equalsIgnoreCase(db.rs2.getString("opt")) ? " selected " : "") + "   >" + db.rs2.getString("dis") + "</option>";
                            }
                            data += "</select><script>$('#" + db.rs1.getString("fa_id") + "').val([" + db.rs.getString(db.rs1.getString("fa_attribute")) + "]).selectpicker('refresh');</script>";
                            break;
                        case "radio":
                            data += "";
                            db.read2(db.rs1.getString("fa_list_query"));
                            while (db.rs2.next()) {
                                data += "<input type=\"radio\" name=\"" + db.rs1.getString("fa_id") + "\" value='" + db.rs2.getString("opt") + "' id=\"" + i + "_" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"with-gap\" " + db.rs1.getString("fa_validation") + ">"
                                        + "<label for=\"" + db.rs1.getString("fa_id") + "_" + db.rs2.getString("opt") + "\" class=\"form-label\">" + db.rs2.getString("dis") + "</label>";
                            }
                            break;
                        case "textarea":
                            data += "<textArea name=\"fa_" + db.rs1.getString("fa_id") + "[]\" id=\"desc" + db.rs.getString("fee_head_id") + "\"  value='' class=\"form-control\" " + db.rs1.getString("fa_validation") + " /></textArea>";

                    }
                    data += "<script>" + db.rs1.getString("script") + "</script></div></div></td>";

                }
                j++;
                //   data += "<td><div class=\"form-group form-float\"><div class=\"\"><button class=\"btn btn-primary waves-effect\" name=\"edit\" id=\"edit_" + db.rs.getString("id") + "\" value='Update' onclick=\"edit_grid_" + fm_id + "('" + db.rs.getString("id") + "')\"  type=\"button\">Update</button> <button class=\"btn btn-primary waves-effect\" name=\"delete\" id=\"delete_" + db.rs.getString("id") + "\" value='Delete' onclick=\"delete_grid_" + fm_id + "('" + db.rs.getString("id") + "')\"  type=\"button\">Delete</button></form></div></div></td> </tr>";
                data += "</div></div></td> </tr>";
                //   }  style='background-color:#507CD1;'

            }
            data += "<tr style='background-color:#507CD1;'><td align='center'><div class=\"form-group form-float\"><b>Total</b></div></td><td style='color:black;'><div class=\"form-group form-float\"><label name='tot' class='form-control'>" + total + "</label></div></td><td><div class=\"form-group form-float\"></div></td><td style='color:black;'><div class=\"form-group form-float\"><div class='form-inline'><input type='text' name='sum' id='sum'></div></div></td><td><div class=\"form-group form-float\"></div></td></tr></table>";  //</form>
        } catch (Exception e) {
            return e.toString();
        } finally {

            try {
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(form_process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

}
