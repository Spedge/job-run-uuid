package com.spedge.jenkins.jru;

import java.util.HashMap;

import com.spedge.jenkins.jru.response.BuildResponseFormatter;
import com.spedge.jenkins.jru.response.BuildResponseFormatterJsonImpl;

/**
 * Static object that contains all the formatters available to this plugin
 * to return the responses. Want the responses in a different format? Add it here.
 * 
 * @author Stuart Davidson
 *
 */
public class BuildIdFormatters 
{
    private static HashMap<String, BuildResponseFormatter> formatters = new HashMap<String, BuildResponseFormatter>();
    private static String DEFAULT_FORMAT = BuildResponseFormatterJsonImpl.NAME;
    private static BuildIdFormatters instance;
    
    private BuildIdFormatters()
    {
        formatters.put(BuildResponseFormatterJsonImpl.NAME, new BuildResponseFormatterJsonImpl());
    }
    
    /**
     * Get a formatter associated with a particular name.
     * @param format
     * @return BuildResponseFormatter with the name of the string passed in, or the default formatter.
     */
    public static BuildResponseFormatter getFormatter(String format)
    {
        if(instance == null) { instance = new BuildIdFormatters(); }
        
        return ((format != null) && (formatters.containsKey(format))) ?
                formatters.get(format) :
                formatters.get(DEFAULT_FORMAT);
    }
}
