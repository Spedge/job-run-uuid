package com.spedge.jenkins.jru.response;

import net.sf.json.JSONObject;

/**
 * Takes a BuildData object and changes it into JSON.
 * 
 * @author Stuart Davidson
 *
 */
public class BuildResponseFormatterJsonImpl implements BuildResponseFormatter 
{
    public static String NAME = "json";
    
    public String getContentType()
    {
        return "application/json;charset=UTF-8";
    }
    
	public String generateOutput(BuildResponse data) 
	{
	    data.setFormat(NAME);
     	return JSONObject.fromObject(data).toString();
	}
}
