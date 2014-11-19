package org.archive.accesscontrol.webui;

import org.archive.accesscontrol.model.HibernateRuleDao;
import org.archive.accesscontrol.model.Rule;
import org.springframework.beans.factory.annotation.Autowired;


public class AdminController extends AbstractAdminController<HibernateRuleDao, Rule, DisplayRule> {

    @Autowired
    public AdminController(HibernateRuleDao ruleDao) {
        this.ruleDao = ruleDao;        
    }
    
    protected Rule getNewRule() {
        return new Rule();        
    }
    
    protected DisplayRule getNewDisplayRule(Rule rule, boolean inherited) {
        return new DisplayRule(rule, inherited);
    }
}
