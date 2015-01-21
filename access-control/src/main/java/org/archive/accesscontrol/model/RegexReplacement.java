package org.archive.accesscontrol.model;

import java.util.HashSet;
import java.util.Set;

public class RegexReplacement implements Comparable<RegexReplacement> {

    private String regex;
    private String replacement;
    private Long id;

    private Set<RegexRule> rules = new HashSet<RegexRule>();
    
    public void addRule(RegexRule r) {
        rules.add(r);
    }
    
    public Set<RegexRule> getRules() {
        return rules;
    }
    public void setRules(Set<RegexRule> rules) {
        this.rules = rules;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRegex() {
        return regex;
    }
    public void setRegex(String regex) {
        this.regex = regex;
    }
    public String getReplacement() {
        return replacement;
    }
    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    @Override
    public int compareTo(RegexReplacement arg0) {
        if (arg0 == null) throw new NullPointerException();
        
        if (regex == null && arg0.regex == null) return 0;
        
        if (regex == null) return -1;
        if (arg0.regex == null) return 1;
        
        return regex.compareTo(arg0.regex);
    }
    
    public boolean equals(RegexReplacement arg0) {
        if (arg0 == null) throw new NullPointerException();
        
        if (regex == null && arg0.regex == null) return true;
        
        if (regex == null) return false;
        if (arg0.regex == null) return false;
        
        return regex.equals(arg0.regex);
    }    
}
