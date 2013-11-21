package com.spedge.jenkins.jru.response;

import java.util.UUID;

/**
 * Response object that can then be formatted into various content types.
 * 
 * @author Stuart Davidson
 *
 */
public class BuildResponse 
{
    // Unique identifier of the executed job.
    private UUID uuid;

    // Status of the build within Jenkins
    private int buildId = -1;
    private boolean isBuilding = false;
    
    // Status of the build determined by this plugin
    private int errorCode;
    private String errorDesc;
    private int responseCode;

    // Re-iterate the parameters passed in.
    private int attempts;
    private int delay;
    private String format;
    private String version = "woo";
    
    // If we don't store it as a string, when net.sf.JSONObject serializes
    // it, the UUID splits into most and least significant bits. Very annoying.
    public String getUuid()
    {
        return (uuid != null)? uuid.toString() : null;
    }
    
    public UUID uuidObj()
    {
        return uuid;
    }
    
    // Auto-generated bean methods.
    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }
    
    public int getBuildId()
    {
        return buildId;
    }
    
    public void setBuildId(int buildId)
    {
        this.buildId = buildId;
    }
    
    public boolean isBuilding()
    {
        return isBuilding;
    }
    
    public void setBuilding(boolean isBuilding)
    {
        this.isBuilding = isBuilding;
    }
    
    public int getErrorCode()
    {
        return errorCode;
    }
    
    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }
    
    public String getErrorDesc()
    {
        return errorDesc;
    }
    
    public void setErrorDesc(String errorDesc)
    {
        this.errorDesc = errorDesc;
    }
    
    public int getAttempts()
    {
        return attempts;
    }
    
    public void setAttempts(int attempts)
    {
        this.attempts = attempts;
    }
    
    public int getDelay()
    {
        return delay;
    }
    
    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }
}
