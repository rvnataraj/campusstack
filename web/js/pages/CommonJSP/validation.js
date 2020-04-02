$(document).ready(function () {
    validate();
});
function validate(){
    $(":input").inputmask();
    $.AdminBSB.dropdownMenu.activate();
    $.AdminBSB.input.activate();
    $.AdminBSB.select.activate();
$('form').each(function () {   // <- selects every <form> on page
    
        $(this).validate({

            highlight: function (input) {
                $(input).parents('.form-line').addClass('error');
                $(input).parents('.form-line').children('.form-label').addClass('error_label');
            },
            unhighlight: function (input) {
                $(input).parents('.form-line').removeClass('error');
                $(input).parents('.form-line').children('.form-label').removeClass('error_label');
            },
            errorPlacement: function (error, element) {
                $(element).parents('.form-group').append(error);
            },
            success: function (element) {
                $(element).parents('.form-line').removeClass('error').addClass('success');
            },
            onfocusout: function (element) {
                $(element).parents('.form-line').children('.form-label').removeClass('focus');
                return $(element).valid();
            },
            onfocusin: function (element) {
                $(element).parents('.form-line').children('.form-label').removeClass('error').addClass('focus');
            }
        });
    });
}