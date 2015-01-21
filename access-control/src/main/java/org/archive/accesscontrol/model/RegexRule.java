package org.archive.accesscontrol.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class RegexRule extends Rule {

    @XStreamImplicit(itemFieldName="regexReplacement")
    private List<RegexReplacement> regexReplacements = Collections.synchronizedList(new ArrayList<RegexReplacement>());
    
    public List<RegexReplacement> getRegexReplacements() {
        return regexReplacements;
    }

    public void setRegexReplacements(List<RegexReplacement> regexReplacements) {
        this.regexReplacements = regexReplacements;
    }
    
    public RegexReplacement getRegexReplacement(Long id) {
        for (RegexReplacement rr: regexReplacements) {
            
            if (rr.getId().longValue() == id.longValue()) {
                return rr;
            }
        }

        return null;
    }
    
}