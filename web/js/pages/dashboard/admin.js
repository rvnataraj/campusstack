function loadrole() {
    var DataString = 'option=loadrole';
    $.ajax({
        url: "admin.do", data: DataString, type: "post",
        success: function (data)
        {
            $('#collapseFour_1pd').html(data);
        }
    });
}
function loadresourse() {
    var DataString = 'option=loadresourse';
    $.ajax({
        url: "admin.do", data: DataString, type: "post",
        success: function (data)
        {
            $('#collapseFour_2pd').html(data);
        }
    });
}
function loaduser() {
    var DataString = 'option=loaduser';
    $.ajax({
        url: "admin.do", data: DataString, type: "post",
        success: function (data)
        {
            $('#collapseFour_3pd').html(data)
            $('#collapseFour_3pd .table').DataTable({
                "processing": true,
                "lengthMenu": [[100, 250, 500, 1000], [100, 250, 500, 1000]],
               // dom: 'Bfrtip',
                dom: 'lBfrtip',
                responsive: true,
                buttons: [
                    'copy', 'csv', 'excel', 'pdf', 'print'
                ]

            });

        }
    });
}