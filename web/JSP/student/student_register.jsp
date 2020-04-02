<%@page import="java.util.ArrayList"%>
<%@page import="CAMPS.Common.form_process"%>
<%@include file="../../CommonJSP/pageHeader.jsp" %>
<script src="../../plugins/jquery-validation/jquery.validate.js"></script>
<script src="../../plugins/jquery-validation/additional-methods.js"></script>
<script src="../../js/pages/CommonJSP/validation.js"></script>
<script>
    function load_stu(stu_id) {
        var DataString = 'stu_id=' + stu_id + '&option=loadpersonal_det';
        $.ajax({
            url: "student_details.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadstu_det').html(data);
            }
        });
    }
    function load_parent(stu_id) {
        var DataString = 'stu_id=' + stu_id + '&option=loadparent_det';
        $.ajax({
            url: "student_details.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#loadparent_det').html(data);
            }
        });
    }
    function showDetails() {
        var DataString = 'option=loadfilter_det';
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
        alert(stu_id);
        load_stu(stu_id);
        load_parent(stu_id);
    }
</script>

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
                                            <input class="form-control" id="old_admission" placeholder="Old Admission No" type="text">
                                        </div>
                                        <span class="input-group-btn">
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
                                <!--<div class="col-md-3">
                                    <div class="input-group input-group-sm">                            
                                        <div class="form-line">
                                            <input class="form-control" id="ano" placeholder="Application No" type="text">
                                        </div>
                                        <span class="input-group-btn">
                                            <button type="button" class="btn bg-purple waves-effect"  onclick='showDetails()'>
                                                <i class="material-icons">search</i>
                                            </button>
                                        </span>
                                    </div>
                                </div>-->
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
                                        <span class="input-group-btn">
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
                            <form id="form_validation" method="POST">
                                <div class="form-group form-float">
                                    <div class="form-line">
                                        <input type="text" class="form-control" name="name" required>
                                        <label class="form-label">Name</label>
                                    </div>
                                </div>
                                <div class="form-group form-float">
                                    <div class="form-line">
                                        <input type="text" class="form-control" name="surname" required>
                                        <label class="form-label">Surname</label>
                                    </div>
                                </div>
                                <div class="form-group form-float">
                                    <div class="form-line">
                                        <input type="email" class="form-control" name="email" required>
                                        <label class="form-label">Email</label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input type="radio" name="gender" id="male" class="with-gap" required>
                                    <label for="male" class="form-label">Male</label>

                                    <input type="radio" name="gender" id="female" class="with-gap" required>
                                    <label for="female" class="form-label">Female</label>
                                </div>
                                <div class="form-group form-float">
                                    <div class="form-line">
                                        <textarea name="description" cols="30" rows="5" class="form-control no-resize" required></textarea>
                                        <label class="form-label">Description</label>
                                    </div>
                                </div>
                                <div class="form-group form-float" >
                                    <div class="form-line focused">
                                        <select class="form-control required">
                                            <option value="" selected>United States</option>
                                            <option  value="1">Canada</option>
                                            <option  value="2">...</option>
                                        </select><label class="form-label">Country</label>
                                    </div>
                                </div>

                                <div class="form-group form-float" required>
                                    <div class="form-line ">
                                        <input type="password" class="form-control" name="password" required>
                                        <label class="form-label">Password</label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input type="checkbox" id="checkbox" name="checkbox">
                                    <label for="checkbox" >I have read and accept the terms</label>
                                </div>
                                <button class="btn btn-primary waves-effect" type="submit">SUBMIT</button>
                            </form>
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
                                <i class="material-icons">perm_contact_calendar</i> Parent Details </a>
                        </h4>
                    </div>
                    <div id="collapseOne_20" class="panel-collapse collapse in"  role="tabpanel" aria-labelledby="headingOne_20">
                        <div class="panel-body" id='loadparent_det'>
                            <form id="form_validation1" method="get">
                                <%  
                                    form_process fp = new form_process();
                                    ArrayList<String> attribute = new ArrayList<>();
                                    attribute.add("1");
                                    out.print(fp.form_v1("1", attribute));
                                %>
                                <button class="btn btn-primary waves-effect" name="aa" id="aa"  type="submit">SUBMIT</button>
                            </form>
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
                <button type="button" class="btn btn-link waves-effect">SAVE CHANGES</button>
                <button type="button" class="btn btn-link waves-effect" data-dismiss="modal">CLOSE</button>
            </div>
        </div>
    </div>
</div>
<%@include file="../../CommonJSP/pageFooter.jsp" %>