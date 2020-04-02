<%@include file="../../CommonJSP/pageHeader.jsp" %>
<script src="../../plugins/jquery-validation/jquery.validate.js"></script>
<script src="../../plugins/jquery-validation/additional-methods.js"></script>
<script src="../../js/pages/CommonJSP/validation.js"></script>
<script src="../../plugins/bootstrap-notify/bootstrap-notify.js"></script>
<script src="../../plugins/jquery-inputmask/jquery.inputmask.bundle.js"></script>
<script src="../../plugins/jquery-validation/jquery-validate.bootstrap-tooltip.js"></script>
<%    DBConnect db = new DBConnect();
    try {

        db.getConnection();

%>

<section class="content">

    <div id='print_area' class="row clearfix">
        <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
            <%   if (request.getParameter("staff_id") == null) {
            %>
            <div class="panel-group full-body" id="accordion_19" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-blue">
                    <div class="panel-heading" role="tab" id="headingOne_19">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_19" aria-expanded="true" aria-controls="collapseOne_19">
                                <i class="material-icons">perm_contact_calendar</i> Staff Registration  </a>
                        </h4>
                    </div>
                    <div id="collapseOne_19" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_19">
                        <div class="panel-body" id='loadstaff_det'>
                            <form id="form_validation" method="POST" action="staff_Profile.do">
                                <div class="form-group form-float" >
                                    <div class="form-line focused">
                                        <select class="form-control" required name='sc_id'>
                                            <option value=''><--Select--></option>
                                            <%                                                db.read("SELECT sc.sc_id,sc.category_name FROM camps.master_staff_category sc WHERE sc.status>0");
                                                while (db.rs.next()) {
                                                    out.print("<option value='" + db.rs.getString("sc_id") + "' >" + db.rs.getString("category_name") + "</option>");
                                                }
                                            %>
                                        </select><label class="form-label">Category</label>
                                    </div>
                                </div>
                                <div class="form-group form-float" >
                                    <div class="form-line focused">
                                        <select class="form-control" required name='legend'>
                                            <option value=''><--Select--></option>
                                            <option value='Mr'>Mr</option>
                                            <option value='Ms'>Ms</option>
                                            <option value='Dr'>Dr</option>
                                        </select><label class="form-label">Legend</label>
                                    </div>
                                </div>
                                <div class="form-group form-float">
                                    <div class="form-line">
                                        <input type="text" class="form-control" name="name" required>
                                        <label class="form-label">Name</label>
                                    </div>
                                </div>
                                <div class="form-group form-float" >
                                    <div class="form-line focused">
                                        <select class="form-control" required name='designation'>
                                            <option value=''><--Select--></option>
                                            <%
                                                db.read("SELECT md.md_id,md.desigination FROM camps.master_desigination md WHERE md.status>0");
                                                while (db.rs.next()) {
                                                    out.print("<option value='" + db.rs.getString("md_id") + "' >" + db.rs.getString("desigination") + "</option>");
                                                }
                                            %>
                                        </select><label class="form-label">Designation</label>
                                    </div>
                                </div>
                                <div class="form-group form-float">
                                    <div class="form-line">
                                        <input type="text" class="form-control" name="dob" id="dob" required>
                                        <label class="form-label">Date of Birth</label>
                                        <script>$('#dob').bootstrapMaterialDatePicker({format: 'DD-MM-YYYY', weekStart: 0, time: false});</script>
                                    </div>
                                </div> 
                                <div class="form-group form-float" >
                                    <div class="form-line focused">
                                        <select class="form-control"  required name='department' id='department'>
                                            <option value=''><--Select--></option>
                                            <%
                                                db.read("SELECT md.department_id,md.dept_name FROM camps.master_department md WHERE md.status>0");
                                                while (db.rs.next()) {
                                                    out.print("<option value='" + db.rs.getString("department_id") + "' >" + db.rs.getString("dept_name") + "</option>");
                                                }
                                            %>
                                        </select><label class="form-label">Department</label>
                                    </div>
                                </div>
                                <div class="form-group form-float">
                                    <div class="form-line">
                                        <input type="text" class="form-control" name="doj" id="doj" required>
                                        <label class="form-label">Date of Joining</label>
                                        <script>$('#doj').bootstrapMaterialDatePicker({format: 'DD-MM-YYYY', weekStart: 0, time: false});</script>
                                    </div>
                                </div>                                 
                                <button class="btn bg-orange waves-effect" name="option" id="option" value="addpersonal_edit" type="submit">SUBMIT</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <%
            } else {
                db.read("SELECT sm.staff_id,sm.legend,TRIM(CONCAT(IFNULL(sm.first_name,''),' ',IFNULL(sm.middle_name,''),' ',IFNULL(sm.last_name,''),' ')) staff_name,md.desigination,IFNULL(sm.gender,'')gender,IFNULL(sm.email_id,'')email_id,IFNULL(sm.institute_email_id,'') institute_email_id,IFNULL(sm.mobile_no,'')mobile_no,IFNULL(sm.aadhaar_no,'')aadhaar_no,IFNULL(DATE_FORMAT(sm.dob,'%d-%m-%Y'),'')dob,IFNULL(DATE_FORMAT(sm.doj,'%d-%m-%Y'),'')doj FROM camps.staff_master sm INNER JOIN camps.staff_promotion sp ON sm.staff_id=sp.staff_id AND sp.status=2 INNER JOIN camps.master_desigination md ON md.md_id=sp.md_id WHERE sm.staff_id='" + request.getParameter("staff_id") + "' ORDER BY sm.staff_id");
                while (db.rs.next()) {
            %>
            <div class="panel-group full-body" id="accordion_19" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-teal">
                    <div class="panel-heading" role="tab" id="headingOne_19">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_19" aria-expanded="true" aria-controls="collapseOne_19">
                                <i class="material-icons">perm_contact_calendar</i> Staff Registration  </a>
                        </h4>
                    </div>
                    <div id="collapseOne_19" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_19">
                        <div class="panel-body" id='loadstaff_det'>
                            <div class="col-xs-6 col-sm-4 col-md-3 col-lg-3">
                                <div class="demo-color-box bg-pink">
                                    <div><h1>Staff ID: <%=db.rs.getString("staff_id")%></h1></div>
                                    <div class="color-name"><%=db.rs.getString("legend")%>. <%=db.rs.getString("staff_name")%></div>
                                    <div class="color-code"><%=db.rs.getString("desigination")%></div>
                                </div>
                            </div>

                            <h2><a href="staff_profile_update.jsp" style="color: #ffffff">Update More Details Click Here</a></h2>

                        </div>
                    </div>
                </div>
            </div>
            <%
                    }
                }
            %>
        </div>        
    </div> 

</section>
<% } catch (Exception e) {
    } finally {
        db.closeConnection();
    }
%>


<%@include file="../../CommonJSP/pageFooter.jsp" %>