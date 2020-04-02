
<%@page import="CAMPS.Common.Tools"%>
<%@page import="javax.mail.internet.*"%>
<%@page import="CAMPS.Connect.DBConnect" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@page import="javax.mail.*"%>
<%@page import="com.google.api.client.googleapis.auth.oauth2.GoogleCredential"%>
<%@page import="com.google.api.client.http.javanet.NetHttpTransport"%>
<%@page import="com.google.api.client.json.jackson2.JacksonFactory"%>
<%@page import="com.google.api.services.admin.directory.*"%>
<%@page import="java.security.*,javax.mail.PasswordAuthentication"%>
<%@page import="javax.xml.bind.DatatypeConverter"%>
<%@page import=" com.google.api.services.admin.directory.model.*"%>

<html>

    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <title>Forgot Password | Bootstrap Based Admin Template - Material Design</title>
        <!-- Favicon-->
        <link rel="icon" href="../../favicon.ico" type="image/x-icon">

        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Roboto:400,700&subset=latin,cyrillic-ext" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet" type="text/css">

        <!-- Bootstrap Core Css -->
        <link href="../plugins/bootstrap/css/bootstrap.css" rel="stylesheet">

        <!-- Waves Effect Css -->
        <link href="../plugins/node-waves/waves.css" rel="stylesheet" />

        <!-- Animation Css -->
        <link href="../plugins/animate-css/animate.css" rel="stylesheet" />

        <!-- Custom Css -->
        <link href="../css/style.css" rel="stylesheet">

        <!-- Jquery Core Js -->
        <script src="../plugins/jquery/jquery.min.js"></script>

        <!-- Bootstrap Core Js -->
        <script src="../plugins/bootstrap/js/bootstrap.js"></script>

        <!-- Waves Effect Plugin Js -->
        <script src="../plugins/node-waves/waves.js"></script>

        <!-- Validation Plugin Js -->
        <script src="../plugins/jquery-validation/jquery.validate.js"></script>     
        <script src="../plugins/jquery-validation/additional-methods.js"></script>
        <!-- Custom Js -->
        <script src="../js/admin.js"></script>
        <script src="../js/pages/CommonJSP/sign-in.js"></script>
    </head>

    <body class="fp-page">
        <div class="fp-box">        
            <div class="card">
                <div class="body">
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <title>Password Reset</title>
                    <script language="javascript">

                        $(document).ready(function () {

                            $("#process").hide();
                            $('#submit_cs1').hide();
                        });

                        function show()
                        {

                        }

                        $(function () {

                            $("input[name='ptype']").click(function () {
                                if ($("#camps").is(":checked")) {
                                    $("#submit_ps1").hide();
                                    $("#submit_cs1").show();
                                } else {
                                    $("#submit_cs1").hide();
                                    $("#submit_ps1").show();
                                }
                            });
                        });
                    </script>
                    <%!
                        public Directory getDirectory() throws Exception, GeneralSecurityException, URISyntaxException {
                            final NetHttpTransport httpTransport = new NetHttpTransport();
                            String workingDirectory = System.getProperty("user.dir");

                            final JacksonFactory jsonFactory = new JacksonFactory();
                            final File p12 = new File(workingDirectory + File.separator + "bavn-admin.p12");
                            final GoogleCredential credential = new GoogleCredential.Builder()
                                    .setTransport(httpTransport)
                                    .setJsonFactory(jsonFactory)
                                    .setServiceAccountUser("administrator@bavnsathy.ac.in")
                                    .setServiceAccountId("administrator@camps-223702.iam.gserviceaccount.com") //the one that ends in "@developer.gserviceaccount.com"
                                    .setServiceAccountScopes(getCredentials())
                                    .setServiceAccountPrivateKeyFromP12File(p12)
                                    .build();
                            return new Directory.Builder(httpTransport, jsonFactory, null)
                                    .setHttpRequestInitializer(credential)
                                    .setApplicationName("API Project") //Not necessary, but silences a runtime warning using any not-blank string here
                                    .build();
                        }

                        public List<String> getCredentials() throws Exception {
                            final List<String> toReturn = new LinkedList<String>();
                            toReturn.add(DirectoryScopes.ADMIN_DIRECTORY_GROUP);
                            toReturn.add(DirectoryScopes.ADMIN_DIRECTORY_USER);
                            toReturn.add(DirectoryScopes.ADMIN_DIRECTORY_USER_READONLY);
                            toReturn.add(DirectoryScopes.ADMIN_DIRECTORY_USER_SECURITY);
                            return toReturn;
                        }

                        public Users getUser(final Directory dir, final String domain, final String username) throws Exception {

                            Directory.Users.List diruserlist = dir.users().list()
                                    .setDomain(domain)
                                    .setOrderBy("email")
                                    .setQuery("email:" + username + "*");
                            return diruserlist.execute();
                        }

                        public User updatePassword(final User user, final String password) throws Exception {
                            final MessageDigest md = MessageDigest.getInstance("MD5");    //I've been warned that this is not thread-safe
                            final byte[] digested = md.digest(password.getBytes("UTF-8"));
                            final String newHashword = DatatypeConverter.printHexBinary(digested);
                            return user.setHashFunction("MD5") //only accepts MD5, SHA-1, or CRYPT
                                    .setPassword(newHashword);
                        }

                        public void updateDirectory(final Directory dir, final User user) throws IOException {
                            final Directory.Users.Update updateRequest = dir.users().update(user.getPrimaryEmail(), user);
                            updateRequest.execute();
                        }
                    %>


                    <form id="sign_in"  method="post" autocomplete="off" action="forgot-password.jsp">
                        <table  border="0" align="center">
                            <tr>
                                <td align="center"></td>
                            </tr><tr><td align="center">
                                    <%

                                        if (request.getParameter("submit") != null && ("Reset").equalsIgnoreCase(request.getParameter("submit"))) {
                                            try {
                                                String uqry = "", idname = "", dept = "", email = "", pdate = "", psess = "", mqry = "", deptname = "", emailcc = "";
                                                String to = "";
                                                String from = "";
                                                String subject = "";
                                                String messageText = "";
                                                String cc = "";
                                                String sid = "", sname = "";
                                                emailcc = "";

                                                int i = 0;

                                                DBConnect con = new DBConnect();
                                                con.getConnection();
                                                String id = request.getParameter("id").replaceAll("'", "\\\\'");
                                                con.read("SELECT um.staff_id,um.user_id,TRIM(CONCAT(IFNULL(sm.first_name,''),' ',IFNULL(sm.middle_name,''),' ',IFNULL(sm.last_name,''),' ')) name,um.user_name,IFNULL(pro.attempt,3) attempt,sm.email_id,sm.institute_email_id  FROM admin.user_master um INNER JOIN camps.staff_master sm ON sm.staff_id=um.staff_id LEFT JOIN admin.password_reset_otp pro ON pro.user_name=um.user_name WHERE um.user_name='" + id + "' ");

                                                ////////////////////start email
                                                if (con.rs.next()) {
                                                    if (con.rs.getString("email_id") != null) {
                                                        if (con.rs.getInt("attempt") > 0) {
                                                            to = con.rs.getString("email_id");
                                                            from = "noreply@bavnsathy.ac.in";
                                                            subject = "Reset BAVN Password - reg";
                                                            final String alphabet = "0123456789ABCDE";
                                                            final int N = alphabet.length();

                                                            Random r = new Random();
                                                            String passwd = "";
                                                            for (i = 0; i < 10; i++) {
                                                                passwd += (alphabet.charAt(r.nextInt(N)));
                                                            }
                                                            messageText = "Hello " + con.rs.getString("name") + ",<br> <br>Click on the following link to reset the password <br> <a href='http://camps.bavnsathy.ac.in/CAMPS/CommonJSP/forgot-password.jsp?id=" + request.getParameter("id") + "&otp=" + passwd + "'>Reset Link</a><br><br> For Further queries contact SDC-BIT.<br><br> \t***This is an system generated E-mail, do not reply for it***";
                                                            Properties props = System.getProperties();
                                                            props.put("mail.smtp.auth", "true");
                                                            props.put("mail.smtp.starttls.enable", "true");
                                                            props.put("mail.smtp.host", "smtp.gmail.com");
                                                            props.put("mail.smtp.port", "587");
                                                            Session mailSession = Session.getInstance(props,
                                                                    new javax.mail.Authenticator() {
                                                                protected PasswordAuthentication getPasswordAuthentication() {
                                                                    return new PasswordAuthentication("noreply@bavnsathy.ac.in", "bitsathy@123");
                                                                }
                                                            });
                                                            Message msg = new MimeMessage(mailSession);
                                                            msg.setFrom(new InternetAddress(from));
                                                            InternetAddress[] address = {new InternetAddress(to)};
                                                            msg.setRecipients(Message.RecipientType.TO, address);

                                                            msg.setSubject(subject);
                                                            msg.setSentDate(new Date());
                                                            //msg.setText(messageText,"utf-8", "html");
                                                            msg.setContent(messageText, "text/html");
                                                            Transport.send(msg);
                                                            con.insert("insert into admin.password_reset_otp values('" + Tools.escapeSpecialChar(request.getParameter("id")) + "',DATE(NOW()),'" + passwd + "','" + (con.rs.getInt("attempt") - 1) + "') ON DUPLICATE KEY UPDATE otp='" + passwd + "',attempt=" + con.rs.getString("attempt") + "-1");
                                                            out.print("<div class=\"success\" style=\"background: white;\"> <h3> Password Reset Link Sent to</h3>  <h1 style=\"color:#152910\"> " + to + "</h1><h3>If not exist in INBOX,Check SPAM. For further Queries Contact SDC-BIT. </h1><h4 style=\"color:red\"> " + (con.rs.getInt("attempt") - 1) + " more attempt(s)</h4></div>");

                                                        } else {
                                                            out.print("<div class=\"error\">  <img src=\"../Images/cross.png\" alt=\"failed\"> You Have Crossed the Maximum Attempts.Try Again Later </div>");
                                                        }

                                                    } else if (con.rs.getString("email_id") == null) {
                                                        out.print("<div class=\"warning\" ><img src=\"../Images/alert.png\" alt=\"failed\">  Personal Mail Id not yet Registered. ");
                                                    } else {
                                                        out.print("<div class=\"error\">  <img src=\"../Images/cross.png\" alt=\"failed\">  Bitsathy Mail Id not found. Contact SDC-BIT </div>");
                                                    }

                                                } else {
                                                    out.print("<div class=\"error\"><img src=\"../Images/cross.png\" alt=\"failed\"> Incorrect Roll No </div>");
                                                }
                                                con.closeConnection();
                                            } catch (Exception e) {
                                                out.println(e);
                                            }

                                        } else if (request.getParameter("reg") != null && ("register").equalsIgnoreCase(request.getParameter("reg"))) {
                                    %>
                                </td></tr>
                            <tr><td class="otcontent" align="center">
                                    <fieldset>
                                        <legend>Reset Password</legend>
                                        <table >
                                            <tr>
                                                <td width="125"    >Roll No</td>
                                                <td width="95"   >
                                                    <input type="text" value="<%= request.getParameter("id")%>" readonly="true" name="id" id="id" class="validate[required]">
                                                </td>



                                            </tr><tr>
                                                <td width="150"    >Date of Birth (DD-MM-YYYY)</td>
                                                <td width="206"   >
                                                    <input type="text" name="dob" id="dob" class="validate[required,funcCall[checkDate]]">

                                                </td>

                                            </tr><tr>
                                                <td width="125"    >Personal Mail ID</td>
                                                <td width="95"   >
                                                    <input type="text" name="pmail" id="pmail" class="validate[required,custom[email]]">

                                                </td></tr>
                                            <tr>
                                                <td colspan="2" align="center" >
                                                    <input type="submit"  name="submit" id="submit" class="myButton" value="Update" /> 
                                                </td></tr></table></fieldset>


                                    <%
                                    } else if (request.getParameter("otp") != null && request.getParameter("id") != null) {
                                        try {
                                            DBConnect con = new DBConnect();
                                            con.getConnection();
                                            con.read("SELECT um.staff_id,'2' student,um.user_id,um.user_name,IFNULL(pro.attempt,3) attempt,sm.email_id,sm.institute_email_id FROM admin.user_master um INNER JOIN camps.staff_master sm ON sm.staff_id=um.staff_id LEFT JOIN admin.password_reset_otp pro ON pro.user_name=um.user_name WHERE um.user_name='" + Tools.escapeSpecialChar(request.getParameter("id")) + "' AND pro.otp='" + Tools.escapeSpecialChar(request.getParameter("otp")) + "'");
                                            if (con.rs.next()) {
                                                out.print("<input type=\"hidden\" name=\"id\" value=\"" + request.getParameter("id") + "\" />");
                                                out.print("<input type=\"hidden\" name=\"otp1\" value=\"" + request.getParameter("otp") + "\" />");

                                    %></td>
                            </tr>        

                            <tr><td class="otcontent" align="center">
                                    <fieldset>
                                        <legend>Reset Password</legend>
                                        <table  border="0">
                                            <% if (con.rs.getString("student").equalsIgnoreCase("2")) { %>
                                            <tr><td colspan="2" align="center">

                                                    <span><b>Password reset for?</b></span><br/>

                                                    <input type="radio" checked="true" id="bitsathy" name="ptype" />
                                                    <label for="bitsathy">
                                                        BAVNSATHY MAIL ID
                                                    </label>
                                                    <input type="radio" id="camps" name="ptype" />
                                                    <label for="camps">
                                                        CAMPS
                                                    </label>
                                                </td></tr> <% }  %>
                                            <tr>                                               

                                                <td colspan='2'>
                                                    <div class="input-group">
                                                        <span class="input-group-addon">
                                                            <i class="material-icons">lock</i>
                                                        </span>
                                                        <div class="form-line">
                                                            <input type="password" class="form-control" name="password" minlength="8"  autocomplete="off" data-rule-varchar="true" id="password" data-rule-checkpasswd="true"  placeholder="New Password" required>
                                                        </div>
                                                    </div>

                                                </td></tr><tr>
                                                <td colspan='2'>
                                                    <div class="input-group">
                                                        <span class="input-group-addon">
                                                            <i class="material-icons">lock</i>
                                                        </span>
                                                        <div class="form-line">
                                                            <input type="password" class="form-control" name="ps2" equalTo="#password" minlength="8"  autocomplete="off" data-rule-varchar="true" id="ps2" data-rule-checkpasswd="true"  placeholder="Re-enter Password" required>
                                                        </div>
                                                    </div>

                                                </td>
                                            </tr>
                                            <tr>

                                                <td colspan="2" align="center" >

                                                    <div id="submit_ps1"><input type="submit"  name="submit_ps" id="submit_ps" class="myButton" value="Reset" onclick="show()" /> </div>
                                                        <% if (con.rs.getString("student").equalsIgnoreCase("2")) { %>
                                                    <div id="submit_cs1"> <input type="submit"  name="submit_cs" id="submit_cs" class="myButton" value="Reset" onclick="show()" /></div> 
                                                        <% } %>
                                                </td>
                                            </tr>
                                        </table>
                                    </fieldset>
                                </td></tr>
                                <%                                         } else
                                            out.print("<div class=\"error\"> <i class=\"material-icons\">report_problem</i>Link Expired</div>");
                                        con.closeConnection();
                                    } catch (Exception e) {
                                        out.println(e);
                                    }
                                } else if (request.getParameter("submit_ps") != null && ("Reset").equalsIgnoreCase(request.getParameter("submit_ps"))) {
                                    try {
                                        DBConnect con = new DBConnect();
                                        con.getConnection();
                                        con.read("SELECT um.staff_id,'2' student,um.user_id,um.user_name,IFNULL(pro.attempt,3) attempt,sm.email_id,sm.institute_email_id FROM admin.user_master um INNER JOIN camps.staff_master sm ON sm.staff_id=um.staff_id LEFT JOIN admin.password_reset_otp pro ON pro.user_name=um.user_name WHERE um.user_name='" + Tools.escapeSpecialChar(request.getParameter("id")) + "' AND pro.otp='" + Tools.escapeSpecialChar(request.getParameter("otp1")) + "'");
                                        if (con.rs.next()) {
                                            Directory dir = getDirectory();
                                            User studentUser = updatePassword(getUser(dir, "bavnsathy.ac.in", con.rs.getString("institute_email_id")).getUsers().get(0), request.getParameter("password"));
                                            studentUser.setChangePasswordAtNextLogin(false);
                                            updateDirectory(dir, studentUser);
                                            con.delete("delete from admin.password_reset_otp where otp='" + request.getParameter("otp1") + "' and user_name='" + request.getParameter("id") + "'");
                                            out.print("<div class=\"success\"><i class=\"material-icons\">done</i>  Password Resetted Successfully </div>");
                                        } else {
                                            out.print("<div class=\"error\"> <i class=\"material-icons\">report_problem</i> Link Expired / Not Valid </div>");
                                        }
                                        con.closeConnection();
                                    } catch (Exception e) {
                                        out.println("test" + e);
                                    }
                                } else if (request.getParameter("submit_cs") != null && ("Reset").equalsIgnoreCase(request.getParameter("submit_cs"))) {
                                    try {
                                        DBConnect con = new DBConnect();
                                        con.getConnection();
                                        con.update("UPDATE admin.user_master SET pwdSalt=SHA1(MD5(SHA1(RAND()))),user_password=SHA1(SHA1(MD5(CONCAT(pwdSalt,'" + Tools.escapeSpecialChar(request.getParameter("password")) + "',pwdSalt)))),updated_date=NOW() WHERE  user_name='" + Tools.escapeSpecialChar(request.getParameter("id")) + "'");
                                        if (con.iEffectedRows != 0) {
                                            con.delete("delete from admin.password_reset_otp where otp='" + Tools.escapeSpecialChar(request.getParameter("otp1")) + "' and user_name='" + Tools.escapeSpecialChar(request.getParameter("id")) + "'");
                                            out.print("<div class=\"success\"><i class=\"material-icons\">done</i>  Password Resetted Successfully </div>");
                                        } else {
                                            out.print("<div class=\"error\"> <i class=\"material-icons\">report_problem</i> Invalid Link </div>");
                                        }
                                        con.closeConnection();
                                    } catch (Exception e) {
                                        out.println(e);
                                    }
                                } else {
                                %>
                            </td>
                            </tr>   
                            <%
                                if (request.getParameter("submit") != null && ("Update").equalsIgnoreCase(request.getParameter("submit"))) {
                                    out.print("<tr><td align=\"center\">");
                                    try {
                                        DBConnect con = new DBConnect();
                                        con.getConnection();
                                        if (Tools.escapeSpecialChar(request.getParameter("id")).length() > 7) {
                                            con.read("select s.student_email_id email,s.student_official_email_id oemail FROM student_all_det s WHERE (s.roll_no='" + Tools.escapeSpecialChar(request.getParameter("id")) + "' or s.enroll_no='" + Tools.escapeSpecialChar(request.getParameter("id")) + "' ) AND s.dob=STR_TO_DATE('" + Tools.escapeSpecialChar(request.getParameter("dob")) + "','%d-%m-%Y' ) ");
                                            if (con.rs.next()) {
                                                if (con.rs.getString("email") == null) {
                                                    con.update("update student_all_det set student_email_id='" + Tools.escapeSpecialChar(request.getParameter("pmail")) + "' WHERE roll_no='" + Tools.escapeSpecialChar(request.getParameter("id")) + "' or enroll_no='" + Tools.escapeSpecialChar(request.getParameter("id")) + "'");
                                                    out.print("<div class=\"success\"> <img src=\"../Images/tick.png\" alt=\"success\"> Inserted Successfully</div>");
                                                } else {
                                                    out.print("<div class=\"warning\"> <i class=\"material-icons\">report_problem</i> Already Registered</div>");
                                                }

                                            } else {
                                                out.print("<div class=\"error\"> <i class=\"material-icons\">report_problem</i> Reset Password Correctly</div>");
                                            }
                                        } else {
                                            con.read("select s.permanent_email email,s.current_email oemail,s.staff_id FROM staff_personal_view s WHERE (s.staff_id='" + Tools.escapeSpecialChar(request.getParameter("id")) + "' or s.staffid='" + Tools.escapeSpecialChar(request.getParameter("id")) + "' ) AND s.Date_of_birth=STR_TO_DATE('" + Tools.escapeSpecialChar(request.getParameter("dob")) + "','%d-%m-%Y' ) ");
                                            if (con.rs.next()) {
                                                if (con.rs.getString("email") == null) {
                                                    con.update("update staff_personal_copy set per_emailid='" + Tools.escapeSpecialChar(request.getParameter("pmail")) + "' WHERE staff_id='" + con.rs.getString("staff_id") + "'");
                                                    out.print("<div class=\"success\"> <img src=\"../Images/tick.png\" alt=\"success\"> Inserted Successfully</div>");
                                                } else {
                                                    out.print("<div class=\"warning\"> <i class=\"material-icons\">report_problem</i> Already Registered</div>");
                                                }

                                            } else {
                                                out.print("<div class=\"error\"> <i class=\"material-icons\">report_problem</i>Reset Password Correctly</div>");
                                            }
                                        }
                                        con.closeConnection();
                                    } catch (Exception e) {
                                        out.println(e);
                                    }
                                }

                            %>
                            <tr><td class="otcontent" align="center">
                                    <fieldset>
                                        <legend>Reset Password</legend>
                                        <table  border="0">
                                            <tr>
                                                <td colspan='2'>
                                                    <div class="input-group">
                                                        <span class="input-group-addon">
                                                            <i class="material-icons">lock</i>
                                                        </span>
                                                        <div class="form-line">
                                                            <input  type="text" name="id" id="id"  class="form-control" autocomplete="off" placeholder="User Name" required>
                                                        </div>
                                                    </div>

                                                </td>
                                            </tr>

                                            <tr>
                                                <td colspan="2" align="center" >
                                                    <input type="submit"  name="submit" id="submit" class="myButton" value="Reset" /> 

                                                </td>

                                            </tr>
                                        </table>
                                    </fieldset>
                                </td></tr>
                                <% }%>
                            <tr><td align="center">
                                </td></tr>
                        </table></form>




                    </body>
                    </html>

                </div>
            </div>
        </div>

    </body>
</html>