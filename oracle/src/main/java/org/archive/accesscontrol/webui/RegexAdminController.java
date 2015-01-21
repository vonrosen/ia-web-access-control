package org.archive.accesscontrol.webui;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.archive.accesscontrol.model.HibernateRegexRuleDao;
import org.archive.accesscontrol.model.RegexReplacement;
import org.archive.accesscontrol.model.RegexRule;
import org.archive.accesscontrol.model.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;


public class RegexAdminController extends AdminController {

    @Autowired
    public RegexAdminController(HibernateRegexRuleDao ruleDao) {
        super(ruleDao);
    }
    
    protected void addRegexes(HttpServletRequest request, RegexRule rule) {
        
        String [] regexIds = request.getParameterValues("regexid");
        String [] regexes = request.getParameterValues("regex");
        String [] replacements = request.getParameterValues("replacement");
        
        if (regexes == null || replacements == null) return;
        
        List<RegexReplacement>regexReplacements = new ArrayList<RegexReplacement>();
        
        for (int i = 0; i < regexes.length; ++i) {
            if ((regexes[i] == null || regexes[i].trim().isEmpty()) || (replacements[i] == null || replacements[i].trim().isEmpty()))
                continue;
            
            String existingId = (regexIds != null &&  i < regexIds.length) ? regexIds[i] : null;
            
            RegexReplacement regexReplacement = null;
            
            if (existingId == null) {
                regexReplacement = new RegexReplacement();

                regexReplacement.setRegex(regexes[i]);
                regexReplacement.setReplacement(replacements[i]);                
                regexReplacement.addRule(rule);
            }
            else {
                regexReplacement = rule.getRegexReplacement(Long.valueOf(existingId));

                //someone may have deleted regex while other person modifying it, let first editor win
                if (regexReplacement == null) {
                    continue;
                }
                
                regexReplacement.setRegex(regexes[i]);
                regexReplacement.setReplacement(replacements[i]);
            }

            regexReplacements.add(regexReplacement);
        }
        
        rule.setRegexReplacements(regexReplacements);
    }
    
    protected ModelAndView saveRule(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String surt = request.getParameter("surt");
        
        RegexRule rule;
        Long ruleId = Long.decode(request.getParameter("edit"));
        if (ruleId == NEW_RULE) {
            rule = new RegexRule();
        } else {
            rule = (RegexRule)ruleDao.getRule(ruleId);
        }
        rule.setSurt(surt);
        rule.setPolicy(request.getParameter("policy"));
        rule.setWho(request.getParameter("who"));
        rule.setCaptureStart(parseDate(request.getParameter("captureStart")));
        rule.setCaptureEnd(parseDate(request.getParameter("captureEnd")));
        rule.setRetrievalStart(parseDate(request.getParameter("retrievalStart")));
        rule.setRetrievalEnd(parseDate(request.getParameter("retrievalEnd")));
        rule.setSecondsSinceCapture(parseInteger(request.getParameter("secondsSinceCapture")));
        rule.setPrivateComment(request.getParameter("privateComment"));
        rule.setPublicComment(request.getParameter("publicComment"));
        rule.setExactMatch(request.getParameter("exactMatch") != null);
        addRegexes(request, rule);
        
        boolean saved = true;
        
        // If adding a new rule, make sure it doesn't match any existing rules
        // or we'll have duplicates (and only one of the dups will show up in the list)
        if (ruleId == NEW_RULE) {
            saved = ruleDao.saveRuleIfNotDup(rule);
        } else {
            ruleDao.saveRule(rule);
        }
        
        return redirectToSurt(request, response, surt, saved ? ErrorStatus.SUCCESS : ErrorStatus.DUP_RULE);
    }
 
    protected DisplayRule getNewDisplayRule(RegexRule rule, boolean inherited) {
        return new DisplayRegexRule(rule, inherited);
    }    

    protected Rule getNewRule() {
        return new RegexRule();
    }
    
}
