package org.archive.accesscontrol.oracle;

import org.archive.accesscontrol.model.HibernateRuleDao;
import org.archive.accesscontrol.model.Rule;
import org.springframework.beans.factory.annotation.Autowired;


public class RulesController extends AbstractRulesController<HibernateRuleDao, Rule> {
    
    @Autowired
    public RulesController(HibernateRuleDao ruleDao, AutoFormatView view) {
        this.ruleDao = ruleDao;
        this.view = view;

        String[] methods = { "GET", "PUT", "DELETE", "POST", "HEAD" };
        this.setSupportedMethods(methods);
    }
    
}
