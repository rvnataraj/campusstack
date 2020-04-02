<%@include file="../../CommonJSP/pageHeader.jsp" %>
<script src="<%=host_string%>js/pages/CommonJSP/sign-in.js"></script>
 <script src="<%=host_string%>plugins/jquery-validation/jquery.validate.js"></script>
 <script src="<%=host_string%>plugins/jquery-validation/additional-methods.js"></script>
<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>Change Password</h2>
             <div class="card">
                <div class="body">
            <form id="sign_in" action="changePassword.do" method="POST">
                <div class="input-group">
                    <span class="input-group-addon">
                        <i class="material-icons">lock</i>
                    </span>
                    <div class="form-line">
                        <input type="password" class="form-control" name="oldPassword" autocomplete="off" data-rule-varchar="true" id="password"  placeholder="Current Password" required>
                    </div>
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        <i class="material-icons">lock</i>
                    </span>
                    <div class="form-line">
                        <input type="password" class="form-control" name="newPassword" minlength="8"  autocomplete="off" data-rule-varchar="true" id="password1" data-rule-checkpasswd="true"  placeholder="New Password" required>
                    </div>
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        <i class="material-icons">lock</i>
                    </span>
                    <div class="form-line">
                        <input type="password" class="form-control" name="confirmPassword" minlength="8" autocomplete="off" data-rule-varchar="true" equalTo="#password1" id="password2"  placeholder="Reenter New Password" required>
                    </div>
                </div>
                <div class="msg">                      
                    <button class="btn btn-block bg-blue waves-effect" type="submit">Reset</button>                        
                </div>
            </form>
        </div>
             </div></div>
    </div>
</section>
<%@include file="../../CommonJSP/pageFooter.jsp" %>