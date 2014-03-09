package utils;

public class PageGenerator {
    private String htmlCode;

    public void refreshHtmlPage(int sessionId) {
        htmlCode = createHtml(sessionId);
    }

    public String getPage() {
        return htmlCode;
    }

    private static String createHtml(int sessionId) {
        return new StringBuilder()
                .append("<html>")
                .append("<head>")
                .append("<noscript>")
                .append("<meta http-equiv='refresh' content='2'>")
                .append("</noscript>")
                .append("<script language='JavaScript'>")
                .append("var sURL = unescape(window.location.pathname);")
                .append("function doLoad()")
                .append("{")
                .append("setTimeout( 'refresh()', 1*1000 );")
                .append("setFocus();")
                .append("var id = getUrlVars()['sessionId'];")
                .append("var param = getUrlVars()['param'];")
                .append("function getUrlVars() {")
                .append("var vars = {};")
                .append("var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {")
                .append(" vars[key] = value;")
                .append("});")
                .append("return vars;")
                .append("}")
                .append("if (id != undefined){")
                .append("document.getElementById('ss').value = id;")
                .append("}")
                .append("if (param != undefined){")
                .append("document.getElementById('2').value = param;")
                .append("}")
                .append("}")
                .append("setTimeout( 'refresh()', 0.5*1000 );")
                .append("setFocus();")
                .append("</script>")
                .append("<script language='JavaScript'>")
                .append("function refresh()")
                .append("{")
                .append("if(!document.getElementById('text').value.replace(/s+/g, '').length) {")
                .append("window.location.reload( true );")
                .append("}")
                .append("}")
                .append("function setFocus() {")
                .append("document.getElementById('text').select();")
                .append("document.getElementById('text').focus();")
                .append("}")
                .append("	</script>")
                .append("</head>")
                .append("<body onload='doLoad()'>")
                .append("<script language='JavaScript'>")
                .append("document.write('<b>' + (new Date).toLocaleString() + '</b>');")
                .append("	function change_but(){")
                .append("var counter = 0;")
                .append("for (var i = window.location.toString().length; window.location.toString().charAt(i) != '='; i--) {")
                .append("counter = i;")
                .append("}")
                .append("var url = window.location.toString().substr(0,counter);")
                .append("document.getElementById('2').value++;")
                .append("url +=   document.getElementById('2').value;")
                .append("window.location = url;")
                .append("}")
                .append("</script>")
                .append("<form method=GET>")
                .append("<input id='text' type='text' name='userName' value =")
                .append("")//TODO:userName
                .append(">")
                .append("<input type='submit' name='Button' value ='send' ")
                .append(">")
                .append("<input id = 'ss' type = 'hidden' name='sessionId' value")
                .append("=")
                .append("'")
                .append(sessionId)
                .append("'")
                .append("/>")
                .append("<input type='button' value='Test' onClick='change_but()' id='test_but'><br>")
                .append("<input id = '2'  type = 'hidden' name='param' value = '0'  >")
                .append("</form>")
                .append("</html>").toString();
    }
}
