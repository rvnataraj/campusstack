<%@include file="../../CommonJSP/pageHeader.jsp" %>
<script src="../../plugins/jquery-validation/jquery.validate.js"></script>
<script src="../../plugins/jquery-validation/additional-methods.js"></script>
<script src="../../js/pages/CommonJSP/validation.js"></script>
<script src="../../plugins/bootstrap-notify/bootstrap-notify.js"></script>
<script src="../../plugins/jquery-inputmask/jquery.inputmask.bundle.js"></script>
<script>
    function load_staff(staff_id) {
        var DataString = 'staff_id=' + staff_id + '&option=loadpersonal_det';
        $.ajax({
            url: "staff_Profile.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadstaff_det').html(data);
                validate();
            }
        });
    }
    function load_ac(staff_id) {
        var DataString = 'staff_id=' + staff_id + '&option=loadac_det';
        $.ajax({
            url: "staff_Profile.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadac_det').html(data);
                validate();
            }
        });
    }
    function load_address(staff_id) {
        var DataString = 'staff_id=' + staff_id + '&option=load_address_edit';
        $.ajax({
            url: "staff_Profile.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadaddress_det').html(data);
                validate();
            }
        });
    }
    function load_ed(staff_id) {
        var DataString = 'staff_id=' + staff_id + '&option=loaded_det';
        $.ajax({
            url: "staff_Profile.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loaded_det').html(data);
                validate();
            }
        });
    }
    function load_parent(staff_id) {
        var DataString = 'staff_id=' + staff_id + '&option=loadparent_edit';
        $.ajax({
            url: "staff_Profile.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadparent_det').html(data);
                validate();
            }
        });
    }
    function showDetails() {
        var DataString = 'staff_id=' + $('#staff_id').val() + '&staff_name=' + $('#staff_name').val() + '&dept_id=' + $('#dept_id').val() + '&cat_id=' + $('#cat_id').val() + '&option=loadfilter_det';
        $.ajax({
            url: "staff_Profile.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#student_det').html(data);
                $('.js-modal-buttons .btn').on('click', function () {
                    var color = $(this).data('color');
                    $('#mdModal .modal-content').removeAttr('class').addClass('modal-content modal-col-' + color);
                    $('#mdModal').modal('show');
                });
            }
        });

    }
    function addstu(staff_id) {
        load_staff(staff_id);
        load_ac(staff_id);
        load_ed(staff_id);
        load_parent(staff_id);
        load_address(staff_id);
    }

</script>

<%    DBConnect db = new DBConnect();
    try {

        db.getConnection();

%>
<section class="content">
    <% db.read("SELECT a.fia_id FROM admin.form_ia_role a WHERE FIND_IN_SET(a.role_id,'" + session.getAttribute("roles") + "') AND a.fia_id='1'");
        if (db.rs.next()) {
    %>
    <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div class="card">
                <div class="header">
                    <h2>
                        Staff Filter 
                    </h2>
                </div>
                <div class="body">
                    <div class="row clearfix">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="input-group input-group-sm">                            
                                        <div class="form-line">
                                            <input class="form-control" id="staff_id" placeholder="Staff Id" type="text">
                                        </div>
                                        <span class="input-group-addon">
                                            <button type="button" class="btn bg-purple waves-effect" data-toggle="modal" data-target="#largeModal"  onclick='showDetails()'>
                                                <i class="material-icons">search</i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="input-group input-group-sm">                            
                                        <div class="form-line">
                                            <input class="form-control" id="staff_name" placeholder="Staff Name" type="text">
                                        </div>
                                        <span class="input-group-addon">
                                            <button type="button" class="btn bg-purple waves-effect" data-toggle="modal" data-target="#largeModal"  onclick='showDetails()'>
                                                <i class="material-icons">search</i>
                                            </button>
                                        </span>
                                    </div>
                                </div>

                                <div class="col-md-3">
                                    <div class="input-group input-group-sm">                            
                                        <div class="form-group form-float">
                                            <div class="form-line">
                                                <select class="form-control show-tick" name="dept_id" multiple id="dept_id" >

                                                    <option value="">-- Select Department --</option>
                                                    <%                                                    db.read("SELECT CONCAT('<option value=\"',md.department_id,'\" >',md.dept_name,'</option>') val FROM camps.master_department md WHERE md.status>0");
                                                        while (db.rs.next()) {
                                                            out.print(db.rs.getString("val"));
                                                        }
                                                    %>
                                                </select><label class="form-label">Department</label>
                                            </div>                            
                                        </div>
                                        <span class="input-group-addon">
                                            <button type="button" class="btn bg-purple waves-effect" data-toggle="modal" data-target="#largeModal"  onclick='showDetails()'>
                                                <i class="material-icons">search</i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="input-group input-group-sm">                            

                                        <div class="form-group form-float">
                                            <div class="form-line">
                                                <select class="form-control show-tick" name="cat_id" multiple id="cat_id" >

                                                    <option value="">-- Select Cat --</option>
                                                    <%
                                                        db.read("SELECT CONCAT('<option value=\"',msc.sc_id,'\" >',msc.category_name,'</option>')val FROM camps.master_staff_category msc WHERE msc.status>0");
                                                        while (db.rs.next()) {
                                                            out.print(db.rs.getString("val"));
                                                        }
                                                    %>
                                                </select><label class="form-label">Category</label>
                                            </div>                            
                                        </div>

                                        <span class="input-group-addon">
                                            <button type="button" class="btn bg-purple waves-effect"  onclick='showDetails()'>
                                                <i class="material-icons">search</i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
    <%
        }
else{
out.print("<script>addstu('" + session.getAttribute("ss_id") + "');</script>");
}
    %>

    <div id='print_area' class="row clearfix">
        <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
            <div class="panel-group full-body" id="accordion_19" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-teal">
                    <div class="panel-heading" role="tab" id="headingOne_19">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_19" aria-expanded="true" aria-controls="collapseOne_19">
                                <i class="material-icons">perm_contact_calendar</i> Staff Details </a>
                        </h4>
                    </div>
                    <div id="collapseOne_19" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_19">
                        <div class="panel-body" id='loadstaff_det'>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
            <div class="panel-group full-body" id="accordion_22" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-teal">
                    <div class="panel-heading" role="tab" id="headingOne_22">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_22" aria-expanded="true" aria-controls="collapseOne_20">
                                <i class="material-icons">perm_contact_calendar</i> Communication Details </a>
                        </h4>
                    </div>
                    <div id="collapseOne_22" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_22">
                        <div class="panel-body" id='loadaddress_det'>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
            <div class="panel-group full-body" id="accordion_20" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-teal">
                    <div class="panel-heading" role="tab" id="headingOne_20">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_20" aria-expanded="true" aria-controls="collapseOne_20">
                                <i class="material-icons">perm_contact_calendar</i> Academic Credentials </a>
                        </h4>
                    </div>
                    <div id="collapseOne_20" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_20">
                        <div class="panel-body" id='loadac_det'>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
            <div class="panel-group full-body" id="accordion_21" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-teal">
                    <div class="panel-heading" role="tab" id="headingOne_21">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_21" aria-expanded="true" aria-controls="collapseOne_21">
                                <i class="material-icons">perm_contact_calendar</i> Experience Details </a>
                        </h4>
                    </div>
                    <div id="collapseOne_21" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_20">
                        <div class="panel-body" id='loaded_det'>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
            <div class="panel-group full-body" id="accordion_22" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-teal">
                    <div class="panel-heading" role="tab" id="headingOne_22">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_22" aria-expanded="true" aria-controls="collapseOne_20">
                                <i class="material-icons">perm_contact_calendar</i> Relation Details </a>
                        </h4>
                    </div>
                    <div id="collapseOne_22" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_22">
                        <div class="panel-body" id='loadparent_det'>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>   
</section>
<% }catch (Exception e) {
    }finally {
        db.closeConnection();
    }
%>
<div class="modal fade" id="largeModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="largeModalLabel">Staff Details</h4>
            </div>
            <div class="modal-body" id="student_det">

            </div>
            <div class="modal-footer">                
                <button type="button" class="btn btn-link waves-effect" data-dismiss="modal">CLOSE</button>
            </div>
        </div>
    </div>
</div>

<%@include file="../../CommonJSP/pageFooter.jsp" %>