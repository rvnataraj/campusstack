<%@include file="../../CommonJSP/pageHeader.jsp" %>

<script src="../../plugins/bootstrap-notify/bootstrap-notify.js"></script>
<script>

    function loadStaff() {
        var DataString = 'dept_id=' + $('#dept_id').val() + '&cat_id=' + $('#cat_id').val() + '&option=loadStaff';
        $.ajax({
            url: "staff_Profile.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadstaff_det').html(data);
                ;
            }
        });
    }
</script>
<%    DBConnect db = new DBConnect();
    try {

        db.getConnection();

%>
<section class="content">
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
                                <div class="col-sm-6">
                                    <div class="form-group form-float">
                                        <div class="form-line">
                                            <select class="form-control show-tick" data-actions-box="true"  name="dept_id" multiple id="dept_id" >

                                                <option value="" disabled>-- Select Department --</option>
                                                <%                                                    db.read("SELECT CONCAT('<option value=\"',md.department_id,'\" >',md.dept_name,'</option>') val FROM camps.master_department md WHERE md.status>0");
                                                    while (db.rs.next()) {
                                                        out.print(db.rs.getString("val"));
                                                    }
                                                %>
                                            </select><label class="form-label">Department</label>
                                        </div>                            
                                    </div>

                                </div>
                                <div class="col-sm-6">
                                    <div class="form-group form-float">
                                        <div class="form-line">
                                            <select class="form-control show-tick" data-actions-box="true" name="cat_id" multiple id="cat_id" onchange="loadStaff()">

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

                                </div>


                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row clearfix">
        <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
            <div class="panel-group full-body" id="accordion_19" role="tablist" aria-multiselectable="true">
                <div class="panel panel-col-blue-grey">
                    <div class="panel-heading" role="tab" id="headingOne_19">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" href="#collapseOne_19" aria-expanded="true" aria-controls="collapseOne_19">
                                <i class="material-icons">perm_contact_calendar</i> Staff List </a>
                        </h4>
                    </div>
                    <div id="collapseOne_19" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_19">
                        <div class="panel-body" id='loadstaff_det'>

                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</section>
<% } catch (Exception e) {
    } finally {
        db.closeConnection();
    }
%>


<%@include file="../../CommonJSP/pageFooter.jsp" %>