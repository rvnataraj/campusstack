<%@include file="../../CommonJSP/pageHeader.jsp" %>
<script src="../../plugins/jquery-validation/jquery.validate.js"></script>
<script src="../../plugins/jquery-validation/additional-methods.js"></script>
<script src="../../js/pages/CommonJSP/validation.js"></script>
<script src="../../plugins/bootstrap-notify/bootstrap-notify.js"></script>
<script src="../../plugins/jquery-inputmask/jquery.inputmask.bundle.js"></script>
<script src="../../plugins/jquery-validation/jquery-validate.bootstrap-tooltip.js"></script>
<script>
    function load_stu(stu_id) {
        $('#loadstu_det').html("");
        var DataString = 'stu_id=' + stu_id + '&option=loadpersonal_edit';
        $.ajax({
            url: "student_details.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadstu_det').html(data);
                loadstu_photo(stu_id);
                validate();
            }
        });
    }

    function loadstu_photo(stu_id) {
        $('#loadstu_photo').load("<%=host_string%>JSP/Welcome/upload_image.jsp?user_id=" + stu_id + "&user_type=student");
    }
    function load_parent(stu_id) {
        var DataString = 'stu_id=' + stu_id + '&option=loadparent_edit';
        $.ajax({
            url: "student_details.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadparent_det').html(data);
                validate();
            }
        });
    }
    function load_address(stu_id) {
        var DataString = 'stu_id=' + stu_id + '&option=load_address_edit';
        $.ajax({
            url: "student_details.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadaddress_det').html(data);
                validate();
            }
        });
    }
    function loadpast_edu(stu_id) {
        var DataString = 'stu_id=' + stu_id + '&option=loadpast_edu';
        $.ajax({
            url: "student_details.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadpast_edu').html(data);
                validate();
            }
        });
    }
    function showDetails() {
        var DataString = 'student_id=' + $('#student_id').val() + '&old_admission=' + $('#old_admission').val() + '&rollno=' + $('#rollno').val() + '&fname=' + $('#fname').val() + '&sname=' + $('#sname').val() + '&dob=' + $('#dob').val() + '&pincode=' + $('#pincode').val() + '&option=loadfilter_det';
        $.ajax({
            url: "student_details.do", data: DataString, type: "post",
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

    function addstu(stu_id) {
        load_stu(stu_id);
        load_parent(stu_id);
        load_address(stu_id);
        loadpast_edu(stu_id);

    }
</script>
<% DBConnect db = new DBConnect();
    try {
        db.getConnection();
%>
<section class="content">
    <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div class="card">
                <div class="header">
                    <h2>
                        Student Filter 
                    </h2>
                </div>
                <div class="body">
                    <div class="row clearfix">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <% db.read("SELECT a.fia_id FROM admin.form_ia_role a WHERE FIND_IN_SET(a.role_id,'" + session.getAttribute("roles") + "') AND a.fia_id='2'");
                                if (db.rs.next()) { %>
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="input-group input-group-sm">                            
                                        <div class="form-line">
                                            <input class="form-control" id="student_id" placeholder="Student Id" type="text">
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
                                            <input class="form-control" id="old_admission" placeholder="Note Admission No" type="text">
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
                                            <input class="form-control" id="fname" placeholder="Father Name" type="text">
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
                                            <input class="form-control" id="rollno" placeholder="Roll No" type="text">
                                        </div>
                                        <span class="input-group-addon">
                                             <button type="button" class="btn bg-purple waves-effect" data-toggle="modal" data-target="#largeModal"  onclick='showDetails()'>
                                                <i class="material-icons">search</i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="input-group input-group-sm">                            
                                        <div class="form-line">
                                            <input class="form-control" id="sname" placeholder="Student Name " type="text">
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
                                            <input class="form-control" id="dob" placeholder="Date of Birth(DD-MM-YYYY)" type="text">
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
                                            <input class="form-control" id="pincode" placeholder="Pincode" type="text">
                                        </div>
                                        <span class="input-group-addon">
                                            <button type="button" class="btn bg-purple waves-effect" data-toggle="modal" data-target="#largeModal"  onclick='showDetails()'>
                                                <i class="material-icons">search</i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                                <!-- <div class="col-md-3">
                                     <div class="input-group input-group-sm">                            
                                         <div class="form-line">
                                             <input class="form-control" id="rno" placeholder="Register No." type="text">
                                         </div>
                                         <span class="input-group-btn">
                                             <button type="button" class="btn bg-purple waves-effect"  onclick='showDetails()'>
                                                 <i class="material-icons">search</i>
                                             </button>
                                         </span>
                                     </div>
                                 </div>-->
                            </div>
                            <% } else {
                            %>
                            <div class="row">                             
                                <div class="col-sm-12">
                                    <div class="form-group form-float">
                                        <div class="form-line">
                                            <select class="form-control show-tick" data-live-search="true" name="incharge_id" id="incharge_id" onchange="addstu($(this).val())" >

                                                <option value="">-- Select  --</option>
                                                <%
                                                    db.read("select CONCAT('<option value=\"',sm.student_id,'\" >',sm.student_id,' - ',TRIM(CONCAT(IFNULL(sm.first_name,''),' ',IFNULL(sm.middle_name,''),' ',IFNULL(sm.last_name,''),' ')),' - ',sp.roll_no,'</option>') val,sm.student_id,TRIM(CONCAT(IFNULL(sm.first_name,''),' ',IFNULL(sm.middle_name,''),' ',IFNULL(sm.last_name,''),' ')) stu_name,sp.roll_no from camps.student_master sm inner join camps.student_admission_master sam on sm.student_id=sam.student_id AND sam.student_status LIKE 'contin%' inner join camps.student_promotion sp on sp.sp_id=sam.sp_id  inner join camps.incharge_member_mapping imm on imm.member_id=sm.student_id where imm.incharge_staff_id='"+session.getAttribute("ss_id")+"' and imm.status>0 and imm.itm_id='1'");
                                                    while (db.rs.next()) {
                                                        out.print(db.rs.getString("val"));
                                                    }
                                                %>
                                            </select><label class="form-label">Student Name</label>
                                        </div>                            
                                    </div>
                                </div>
                            </div>
                            <%
                                }
                            %>


                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row clearfix">
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
        <div class="col-xs-12 ol-sm-12 col-md-6 col-lg-6">
            <div class="panel-group full-body" id="accordion_21" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-teal">
                    <div class="panel-heading" role="tab" id="headingOne_21">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_19" aria-expanded="true" aria-controls="collapseOne_19">
                                <i class="material-icons">perm_contact_calendar</i> Student Photo </a>
                        </h4>
                    </div>
                    <div id="collapseOne_21" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_19">
                        <div class="panel-body" id='loadstu_photo'>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12 ol-sm-12 col-md-6 col-lg-6">
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
                            <a role="button" data-toggle="collapse" href="#collapseOne_19" aria-expanded="true" aria-controls="collapseOne_20">
                                <i class="material-icons">perm_contact_calendar</i> Relation Details </a>
                        </h4>
                    </div>
                    <div id="collapseOne_20" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_20">
                        <div class="panel-body" id='loadparent_det'>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
            <div class="panel-group full-body" id="accordion_23" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-teal">
                    <div class="panel-heading" role="tab" id="headingOne_23">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_23" aria-expanded="true" aria-controls="collapseOne_23">
                                <i class="material-icons">perm_contact_calendar</i> Educational Details </a>
                        </h4>
                    </div>
                    <div id="collapseOne_23" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_23">
                        <div class="panel-body" id='loadpast_edu'>

                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</section>
<div class="modal fade" id="largeModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="largeModalLabel">Student Details</h4>
            </div>
            <div class="modal-body" id="student_det">

            </div>
            <div class="modal-footer">
                
                <button type="button" class="btn btn-link waves-effect" data-dismiss="modal">CLOSE</button>
            </div>
        </div>
    </div>
</div>
<%          } catch (Exception e) {
    } finally {
        db.closeConnection();
    }
%>
<%@include file="../../CommonJSP/pageFooter.jsp" %>