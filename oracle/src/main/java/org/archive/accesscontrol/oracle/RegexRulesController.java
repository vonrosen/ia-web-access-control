package org.archive.accesscontrol.oracle;

import org.archive.accesscontrol.model.HibernateRegexRuleDao;
import org.springframework.beans.factory.annotation.Autowired;

public class RegexRulesController extends RulesController {

    @Autowired
    public RegexRulesController(HibernateRegexRuleDao ruleDao, AutoFormatView view) {
        super(ruleDao, view);
    }
    
    
}
