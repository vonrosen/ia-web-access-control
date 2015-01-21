package org.archive.accesscontrol.oracle;

import java.io.FileInputStream;

import org.archive.accesscontrol.model.RegexReplacement;
import org.archive.accesscontrol.model.RegexRule;
import org.archive.accesscontrol.model.Rule;
import org.archive.accesscontrol.model.RuleChange;
import org.archive.accesscontrol.model.RuleSet;

import com.thoughtworks.xstream.XStream;

public class TestOracle {

    public static void main(String [] args) {
        
        XStream xstream = new XStream();
        //xstream.alias("rule", RegexRule.class);
        //xstream.alias("ruleSet", RegexRuleSet.class);
        //xstream.alias("regexReplacements", PersistentSortedSet.class);
        //xstream.alias("regexReplacement", RegexReplacement.class);
        //xstream.alias("error", SimpleError.class);
        //xstream.alias("ruleChange", RuleChange.class);
        
        
        
        
        xstream.alias("ruleSet", RuleSet.class);
        xstream.alias("rule", RegexRule.class);
        xstream.alias("regexReplacement", RegexReplacement.class);
        xstream.alias("error", SimpleError.class);
        xstream.alias("ruleChange", RuleChange.class);
        
        //xstream.alias("regexReplacements", TreeSet.class);
        //xstream.addImplicitCollection(RegexRule.class, "bag");
        
        
        xstream.omitField(RegexReplacement.class, "rules");
        
        xstream.omitField(RegexRule.class, "initialized");
        xstream.omitField(RegexRule.class, "owner");
        xstream.omitField(RegexRule.class, "cachedSize");
        xstream.omitField(RegexRule.class, "role");
        xstream.omitField(RegexRule.class, "dirty");
        xstream.omitField(RegexRule.class, "storedSnapshot");
        //xstream.omitField(RegexRule.class, "bag");
        
        
        /*
        xstream.addDefaultImplementation(PersistentList.class, List.class); 
        xstream.addDefaultImplementation(PersistentBag.class, List.class); 
        xstream.addDefaultImplementation(PersistentSet.class, Set.class); 
        xstream.addDefaultImplementation(PersistentSortedSet.class, SortedSet.class); 
        xstream.addDefaultImplementation(PersistentMap.class, Map.class); 
        xstream.addDefaultImplementation(PersistentSortedMap.class, SortedMap.class);
        */
        
        

        try {
            FileInputStream fin = new FileInputStream ("/home/hstern/oracletest.xml");
            
            //System.out.println(xstream.fromXML(fin));
            RuleSet rules = (RuleSet) xstream.fromXML(fin);
            
            for (Rule rule : rules) {
                
                RegexRule rr = (RegexRule)rule;
                
                for (RegexReplacement re: rr.getRegexReplacements()) {
                    System.out.println(re.getRegex());
                }
            }
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }
        
    }
    
}
