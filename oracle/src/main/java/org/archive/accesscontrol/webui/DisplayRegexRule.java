package org.archive.accesscontrol.webui;

import java.util.List;

import org.archive.accesscontrol.model.RegexReplacement;
import org.archive.accesscontrol.model.RegexRule;

public class DisplayRegexRule extends DisplayRule {
    
    public DisplayRegexRule(RegexRule rule, boolean inherited) {
        super(rule, inherited);
    }
    
    public List<RegexReplacement> getRegexReplacements() {
        RegexRule rule = (RegexRule)getRule();

        return rule.getRegexReplacements();
    }
}
