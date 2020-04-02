<%@include file="../../CommonJSP/pageHeader.jsp" %>
<script>
   
   function load_branch() {
        var DataString = 'degree_level=' + $('#degree_level').val() + '&option=load_branch';
       $.ajax({
            url: "student_details.do", data: DataString, type: "post",
            success: function (data)
            {
                $('#branch_id').html(data).selectpicker('refresh');
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
    <form id="form1" method="post" action="student_details.do" >
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
                                        <div class="col-sm-6">
                                        <div class="form-group form-float">
                                            <div class="form-line focused">
                                                <select class="form-control show-tick" data-actions-box="true" multiple name="yoa" id="yoa" >
                                                     <%
                                                        db.read("SELECT DISTINCT(YEAR(a.doa)) yoa FROM camps.student_admission_master a WHERE a.doa IS NOT NULL ORDER BY a.doa DESC");
                                                        while (db.rs.next()) {
                                                            out.print("<option value=\"" + db.rs.getString("yoa") + "\">" + db.rs.getString("yoa") + "</option>");
                                                        }
                                                    %>
                                                </select><label class="form-label">Year of Admission</label>
                                            </div>  

                                        </div>

                                    </div>
                                                 <div class="col-sm-6">
                                        <div class="form-group form-float">
                                            <div class="form-line focused">
                                                <select class="form-control show-tick" data-actions-box="true" multiple name="degree_level" id="degree_level" onchange="load_branch()">
                                                    <%
                                                        db.read("SELECT DISTINCT(a.programme_level) degree_level FROM camps.master_programme a");
                                                        while (db.rs.next()) {
                                                            out.print("<option value=\"'" + db.rs.getString("degree_level") + "'\">" + db.rs.getString("degree_level") + "</option>");
                                                        }
                                                    %>
                                                </select><label class="form-label">Programme Level</label>
                                            </div>  

                                        </div>

                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-float">
                                            <div class="form-line focused">
                                                <select class="form-control show-tick" data-actions-box="true" multiple  name="branch_id" id="branch_id" >                                                
                                                  
                                                </select><label class="form-label">Programme Name</label>
                                            </div>                            
                                        </div>

                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-float">
                                            <div class="form-line focused">
                                                <select class="form-control show-tick" data-actions-box="true" multiple name="student_status" id="student_status" >
                                                     <%
                                                        db.read("select a.status_name from camps.student_status_detail a where a.status=1 order by a.order_no");
                                                        while (db.rs.next()) {
                                                            out.print("<option value=\"" + db.rs.getString("status_name") + "\">" + db.rs.getString("status_name") + "</option>");
                                                        }
                                                    %>
                                                </select><label class="form-label">Student Status</label>
                                            </div>  

                                        </div>

                                    </div>
                                             
                                    <center>
                                        <button class="btn btn-primary waves-effect" type="submit" value="student_profile_report" name="profile_report">Report</button>
                                    </center>
                                </div>
                                                
                            </div>

                        </div>
                    </div>
                </div>

            </div></div>




    </form>
</section>
<% } catch (Exception e) {
        out.print(e);
    } finally {
        db.closeConnection();
    }
%>
<script>
    loadSemeter();
</script>
<%@include file="../../CommonJSP/pageFooter.jsp" %>