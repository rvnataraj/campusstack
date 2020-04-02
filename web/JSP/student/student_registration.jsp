<%@include file="../../CommonJSP/pageHeader.jsp" %>
<script src="../../plugins/jquery-validation/jquery.validate.js"></script>
<script src="../../plugins/jquery-validation/additional-methods.js"></script>
<script src="../../js/pages/CommonJSP/validation.js"></script>
<script src="../../plugins/bootstrap-notify/bootstrap-notify.js"></script>
<script src="../../plugins/jquery-inputmask/jquery.inputmask.bundle.js"></script>
<%    DBConnect db = new DBConnect();
    try {
        db.getConnection();
%>
<script>
    function load_stu( ) {
        $('#loadstu_det').html("");
        var DataString = '&option=load_registration';
        $.ajax({
            url: "student_details.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadstu_det').html(data);
                validate();
            }
        });
    }
     function load_sem( ) {
        $('#sem').html("");
        var DataString = 'branch_id='  + $("#89").val() + '&option=load_sem';
        $.ajax({
            url: "student_details.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#110').html(data);
                $('#110').html(data).selectpicker('refresh');
                validate();
            }
        });
    }
    load_stu();
</script>


<section class="content">

    <div id='print_area' class="row clearfix">
        <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
            <%   if (request.getParameter("student_id") == null) {
            %>

            <form id="form_validation" method="POST" action="student_details.do">

                <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
                    <div class="panel-group full-body" id="accordion_19" role="tablist" aria-multiselectable="true">
                        <div class="panel panel-col-teal">
                            <div class="panel-heading" role="tab" id="headingOne_19">
                                <h4 class="panel-title">
                                    <a role="button" data-toggle="collapse" href="#collapseOne_19" aria-expanded="true" aria-controls="collapseOne_19">
                                        <i class="material-icons">perm_contact_calendar</i> Student Details </a>
                                </h4>
                            </div>
                            <div id="collapseOne_19" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_19">
                                <div class="panel-body" id='loadstu_det'>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</div>
<%
} else {
    db.read("SELECT sm.student_id,TRIM(CONCAT(IFNULL(sm.first_name,''),' ',IFNULL(sm.middle_name,''),' ',IFNULL(sm.last_name,''),' ')) stu_name,CONCAT(mp.programme_name,' ',br.branch_name) programme_name FROM camps.student_master sm INNER JOIN camps.student_admission_master sam ON sm.student_id=sam.student_id INNER JOIN camps.student_promotion sp ON sp.sp_id=sam.sp_id AND sp.status>0 INNER JOIN camps.master_term mt ON mt.term_id=sp.term_id INNER JOIN camps.master_academic_year ay ON ay.ay_id=mt.ay_id INNER JOIN camps.master_branch br ON br.branch_id=sp.branch_id INNER JOIN camps.master_programme mp ON mp.programme_id=br.programme_id AND mp.status>0  WHERE sm.student_id=" + request.getParameter("student_id"));
    while (db.rs.next()) {
%>
<div class="panel-group full-body" id="accordion_19" role="tablist" aria-multiselectable="true">
    <div class="panel panel-col-teal">
        <div class="panel-heading" role="tab" id="headingOne_19">
            <h4 class="panel-title">
                <a role="button" data-toggle="collapse" href="#collapseOne_19" aria-expanded="true" aria-controls="collapseOne_19">
                    <i class="material-icons">perm_contact_calendar</i>Student Details  </a>
            </h4>
        </div>
        <div id="collapseOne_19" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_19">
            <div class="panel-body" id='loadstaff_det'>
                <div class="col-xs-6 col-sm-4 col-md-3 col-lg-3">
                    <div class="demo-color-box bg-pink">
                        <div><h1>Student ID: <%=db.rs.getString("student_id")%></h1></div>
                        <div class="color-name"> <%=db.rs.getString("stu_name")%></div>
                        <div class="color-code"><%=db.rs.getString("programme_name")%></div>
                    </div>
                </div>
                <h2><a href="student_registration.jsp" style="color: #ffffff">New Admission Click Here</a></h2>
                <h2><a href="student_det_update.jsp" style="color: #ffffff">Update More Details Click Here</a></h2>

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