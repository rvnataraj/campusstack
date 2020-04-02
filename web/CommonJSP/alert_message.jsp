<%
switch (request.getParameter("am_c")){
    case "1":
        out.print("<script>showNotification('alert-success', 'Process Completed...','top', 'right',  '', '',false,'glyphicon glyphicon-cd glyphicon-spin');</script>");
        break;
    default:
        out.print("<script> showNotification('alert-danger', 'Some Thing went Wrong!!!','top', 'right',  '', '',false,'glyphicon glyphicon-thumbs-down');</script>");
        break;
}
%>