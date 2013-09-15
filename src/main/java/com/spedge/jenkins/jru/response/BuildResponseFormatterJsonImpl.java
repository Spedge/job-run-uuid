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
    
	@Override
	public String getContentType()
	{
		return "application/json;charset=UTF-8";
	}
	
	@Override
	public String generateOutput(BuildResponse data) 
	{
     	return JSONObject.fromObject(data).toString();
	}
}
