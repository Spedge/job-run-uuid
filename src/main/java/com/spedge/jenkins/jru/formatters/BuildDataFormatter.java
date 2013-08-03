package com.spedge.jenkins.jru.formatters;

import com.spedge.jenkins.jru.BuildData;

public interface BuildDataFormatter 
{
	String getHeader();
	String generateOutput(BuildData data);	
}
