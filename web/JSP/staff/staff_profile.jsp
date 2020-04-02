<%@include file="../../CommonJSP/pageHeader.jsp" %>

<script src="../../plugins/bootstrap-notify/bootstrap-notify.js"></script>
<script src="../../plugins/printthis/printThis.js"></script>
<script>
    function load_stu(stu_id) {
        var DataString = 'stu_id=' + stu_id + '&option=loadpersonal_det';
        $.ajax({
            url: "staff_Profile.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadstu_det').html(data);
            }
        });
    }
    function load_parent(stu_id) {
        var DataString = 'stu_id=' + stu_id + '&option=loadparent_det';
        $.ajax({
            url: "staff_Profile.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadparent_det').html(data);
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
    function addstu(stu_id) {
        load_stu(stu_id);
        load_parent(stu_id);
    }




    function printout() {
        $("#print_area").printThis({
            debug: false, // show the iframe for debugging
            importCSS: true, // import parent page css
            importStyle: false, // import style tags
            printContainer: true, // print outer container/$.selector
            loadCSS: "", // path to additional css file - use an array [] for multiple
            pageTitle: "", // add title to print page
            removeInline: false, // remove inline styles from print elements
            removeInlineSelector: "*", // custom selectors to filter inline styles. removeInline must be true
            printDelay: 333, // variable print delay
            header: "<h1>Student Profile</h1>", // prefix to html
            footer: null, // postfix to html
            base: false, // preserve the BASE tag or accept a string for the URL
            formValues: true, // preserve input/form values
            canvas: false, // copy canvas content
            doctypeString: '...', // enter a different doctype for older markup
            removeScripts: false, // remove script tags from print content
            copyTagClasses: false, // copy classes from the html & body tag
            beforePrintEvent: null, // function for printEvent in iframe
            beforePrint: null, // function called before iframe is filled
            afterPrint: null            // function called before iframe is removed
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
                        <div class="panel-body" id='loadstu_det'>

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
                                <i class="material-icons">perm_contact_calendar</i> Working Details </a>
                        </h4>
                    </div>
                    <div id="collapseOne_20" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_20">
                        <div class="panel-body" id='loadparent_det'>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <center><input type="button" onclick='printout()'/></center>
</section>
<% } catch (Exception e) {
    } finally {
        db.closeConnection();
    }
%>
<div class="modal fade" id="largeModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="largeModalLabel">Student Details</h4>
            </div>
            <div class="modal-body" id="student_det">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-link waves-effect">SAVE CHANGES</button>
                <button type="button" class="btn btn-link waves-effect" data-dismiss="modal">CLOSE</button>
            </div>
        </div>
    </div>
</div>

<%@include file="../../CommonJSP/pageFooter.jsp" %>