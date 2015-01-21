package org.archive.accesscontrol.oracle;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class RegexRuleAutoFormatView extends AutoFormatView {

    @Override
    public Object deserializeRequest(HttpServletRequest request) throws IOException {
        String ctype = request.getContentType();
        if (ctype == null) {
            ctype = "application/xml";
        }
        XStreamRegexRuleView view = (XStreamRegexRuleView)viewByContentType(ctype);
        return view.getXstream().fromXML(request.getInputStream());
    }    
}
