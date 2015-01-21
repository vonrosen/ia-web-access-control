package org.archive.accesscontrol;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.archive.accesscontrol.model.RegexReplacement;
import org.archive.accesscontrol.model.RegexRule;
import org.archive.accesscontrol.model.RegexRuleSet;
import org.archive.accesscontrol.model.RuleSet;

import com.thoughtworks.xstream.XStream;

/**
 * The HTTP Rule Data Access Object enables a rule database to be queried via
 * the REST interface.
 * 
 * For details of the protocol, see:
 * http://webteam.archive.org/confluence/display/wayback/Exclusions+API
 * 
 * @author aosborne
 * 
 */
public class HttpRuleDao implements RuleDao {
    protected HttpClient http = new HttpClient(
            new MultiThreadedHttpConnectionManager());
    protected XStream xstream = new XStream();
    protected String oracleUrl;
    
    public final static String ORACLE_NUM_RULES = "X-Archive-Wayback-Oracle-Num-Rules";

    public HttpRuleDao(String oracleUrl) {
        this.oracleUrl = oracleUrl;
        xstream.alias("rule", RegexRule.class);
        xstream.alias("ruleSet", RegexRuleSet.class);
        xstream.alias("regexReplacement", RegexReplacement.class);
    }

    /**
     * @throws RuleOracleUnavailableException 
     * @see RuleDao#getRuleTree(String)
     */
    public RuleSet getRuleTree(String surt) throws RuleOracleUnavailableException {
        HttpMethod method = new GetMethod(oracleUrl + "/rules/tree/" + surt);
        RuleSet rules;

        try {
            http.executeMethod(method);
//            String response = method.getResponseBodyAsString();
//            System.out.println(response);
            rules = (RuleSet) xstream.fromXML(method.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new RuleOracleUnavailableException(surt, e);
        } finally {
        	method.releaseConnection();
        }
        return rules;
    }
    
    public boolean hasNewRulesSince(String timestamp, String who) throws RuleOracleUnavailableException
    {
    	StringBuilder sb = new StringBuilder(oracleUrl);
    	sb.append("/rules?modifiedAfter=");
    	sb.append(timestamp);
    	
    	if (who != null) {
    		sb.append("&who=");
    		sb.append(who);
    	}
    	
        HttpMethod method = new HeadMethod(sb.toString());
        try {
            http.executeMethod(method);
            Header header = method.getResponseHeader(ORACLE_NUM_RULES);
            if (header == null) {
            	return false;
            }
            return !header.getValue().equals("0");
        	
        } catch (IOException e) {
        	throw new RuleOracleUnavailableException(e);
        } finally {
            method.releaseConnection();
        }
    }

    /**
     * @return the oracleUrl
     */
    public String getOracleUrl() {
        return oracleUrl;
    }

    /**
     * @param oracleUrl
     *            the oracleUrl to set
     */
    public void setOracleUrl(String oracleUrl) {
        this.oracleUrl = oracleUrl;
    }

    public void prepare(Collection<String> surts) {
        // no-op
    }

}
