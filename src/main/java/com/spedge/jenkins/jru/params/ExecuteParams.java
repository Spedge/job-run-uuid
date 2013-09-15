package com.spedge.jenkins.jru.params;

import hudson.model.ParameterValue;
import hudson.model.StringParameterValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kohsuke.stapler.StaplerRequest;

/**
 * We use this class to parse the StaplerRequest
 * and retrieve the parameters we want from it.
 * 
 * @author Stuart Davidson
 *
 */
public class ExecuteParams 
{
    private static final String DELAY_NAME = "delay";
    private static final String ATTEMPTS_NAME = "attempts";
    private static final String FORMAT_NAME = "format";
    private static final String UUID_NAME = "uuid";
    
    private static final int DELAY_DEFAULT = 10;
    private static final int ATTEMPTS_DEFAULT = 3;
    private static final String FORMAT_DEFAULT = "json";

    private int attempts = 0;
    private int delay = 0;
    private String format;
    private UUID uuid = null;
    
    //TODO Break this up into OO - as it scaled, far too much repetition of tasks.
    public ExecuteParams(StaplerRequest req)
    {
        this.delay = NumberUtils.toInt(req.getParameter(DELAY_NAME), DELAY_DEFAULT);
        this.attempts = NumberUtils.toInt(req.getParameter(ATTEMPTS_NAME), ATTEMPTS_DEFAULT);
        this.format = StringUtils.defaultIfEmpty(req.getParameter(FORMAT_NAME), FORMAT_DEFAULT);
        
        try
        {
            this.uuid = UUID.fromString(req.getParameter(UUID_NAME));
        }
        catch(NullPointerException npe) { uuid = null; }
        catch(IllegalArgumentException iae) { }
    }
    
    public int getAttempts()
    {
        return attempts;
    }
    
    public int getDelay()
    {
        return delay;
    }
    
    public String getFormat()
    {
        return format;
    }
    
    public UUID getUUID()
    {
        return uuid;
    }
    
    public List<ParameterValue> getParameters()
    {
        List<ParameterValue> values = new ArrayList<ParameterValue>();
        values.add(new StringParameterValue(DELAY_NAME, "" + delay));
        values.add(new StringParameterValue(ATTEMPTS_NAME, "" + attempts));
        values.add(new StringParameterValue(FORMAT_NAME, "" + format));
        values.add(new StringParameterValue(UUID_NAME, "" + uuid));
        return values;
    }
    
    @Override
    public String toString()
    {
        return "Delay : " + delay + ", Attempts : " + attempts + ", Format : " + format + ", UUID : " + uuid.toString();
    }
}
