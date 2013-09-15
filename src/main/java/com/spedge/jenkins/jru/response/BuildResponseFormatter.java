package com.spedge.jenkins.jru.response;

/**
 * A BuildResponseFormatter will take a {@com.spedge.jenkins.jru.response.BuildResponse} object and re-format it into a
 * String to be returned as the response.
 * 
 * @author Stuart Davidson
 *
 */
public interface BuildResponseFormatter 
{
	/**
	 * Returns the content type of the formatted data type.
	 * 
	 * @return Type of content being returned.
	 */
	String getContentType();
	
	/**
	 * Returns a string to be sent as part of the response with the data formed.
	 * 
	 * @param data
	 * @return BuildResponse object formatted into a string to be returned.
	 */
	String generateOutput(BuildResponse data);	
}
