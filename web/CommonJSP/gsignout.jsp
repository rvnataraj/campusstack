
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    </head>
    <body onload="var win = window.open
                    ('http://accounts.google.com/logout',
                            '1366002941508', 'width=500,height=200,left=375,top=330');
            setTimeout(function () {
                win.close
                        ();
                window.location.href = '../';
            }, 1200);">

    </body>
</html>
