// Default scripts for app
// Author: ataulislam.raihan

// override jquery validate plugin defaults
$.validator.setDefaults({
    highlight: function (element) {
        $(element).closest('.form-group').addClass('has-error');
    },
    unhighlight: function (element) {
        $(element).closest('.form-group').removeClass('has-error');
    },
    errorElement: 'span',
    errorClass: 'help-block',
    errorPlacement: function (error, element) {
        if (element.parent('.input-group').length) {
            error.insertAfter(element.parent());
        } else {
            error.insertAfter(element);
        }
    }
});

// ajax caller
function callAjax(location, handleData) {
    //console.log('calling ajax to ' + location);
    $.ajax({
        url: location,
        success: function (data) {
            handleData(data);
        }
    });
}

// for showing dropdown properly inside table-responsive
$(document).ready(function(){
    $('.table-responsive').on('show.bs.dropdown', function () {
        $('.table-responsive').css( "overflow", "inherit" );
    });

    $('.table-responsive').on('hide.bs.dropdown', function () {
         $('.table-responsive').css( "overflow", "auto" );
    });
});