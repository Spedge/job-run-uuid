package com.spedge.jenkins.jru.formatters;

import com.spedge.jenkins.jru.BuildData;

/**
 * A BuildDataFormatter will take a {@BuildData} object and re-format it into a
 * String to be returned as the response.
 * @author Spedge
 *
 */
public interface BuildDataFormatter 
{
	/**
	 * Returns the content type of the formatted data type.
	 * @return
	 */
	String getContentType();
	
	/**
	 * Returns a string to be sent as part of the response with the data formed.
	 * @param data
	 * @return
	 */
	String generateOutput(BuildData data);	
}
