package com.spedge.jenkins.jru.formatters;

import net.sf.json.JSONObject;

import com.spedge.jenkins.jru.BuildData;

public class BuildDataFormatterJsonImpl implements BuildDataFormatter 
{
	@Override
	public String getContentType()
	{
		return "application/json;charset=UTF-8";
	}
	
	@Override
	public String generateOutput(BuildData data) 
	{
		JSONObject json = new JSONObject();
	   	json.put("buildId", data.getBuildId());
	   	json.put("building", data.isBuilding());
	   	json.put("delay", data.getDelay());
	   	json.put("attempts", data.getAttempts());
	   	json.put("errorCode", data.getState().getErrorCode());
	   	json.put("errorDesc", data.getState().getErrorMessage());
	   	json.put("params", data.getParams());
	   	json.put("uuid", "" + data.getUuid());
	   	
	   	return json.toString();
	}

}
