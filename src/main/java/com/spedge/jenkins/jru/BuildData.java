package com.spedge.jenkins.jru;

import hudson.model.ParameterValue;
import hudson.model.AbstractBuild;
import hudson.model.ParametersAction;
import hudson.model.Project;
import hudson.model.StringParameterValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * This contains all the data we pass back as a response.
 * @author Spedge
 *
 */
public class BuildData 
{
	private int buildId = 0;
	private UUID uuid = null;
	private final int delay = 600;
	private final int attempts = 10;
	private BuildDataState state;
	private Project proj;
	private BuildIdCause remoteCause;
	private boolean isBuilding = false;
	private HashMap<String, String> params;
	
	public BuildData(Project proj, StaplerRequest req)
	{
		this.proj = proj;
		this.uuid = UUID.randomUUID();
		this.remoteCause = new BuildIdCause(this.uuid);
		this.params = createStringParamMap(req);
	}

	public BuildData(Project proj, UUID uuid, StaplerRequest req)
	{
		this.proj = proj;
		this.uuid = uuid;
		this.remoteCause = new BuildIdCause(this.uuid);
		this.params = createStringParamMap(req);
	}
	
	public BuildDataState getState() { return state; }
	public HashMap<String, String> getParams() { return params; }
	public UUID getUuid() {return uuid; }
	public BuildIdCause getCause() { return remoteCause; }
	public Project getProject() { return proj; }
	public int getAttempts() { try { return Integer.parseInt(getParameter("attempts")); } catch(Exception e) { return attempts; } }
	public int getDelay() { try { return Integer.parseInt(getParameter("delay")); } catch(Exception e) { return delay; } } 
	public boolean isBuildSet() { return (buildId != 0); }
	public int getBuildId() { return buildId; }
	public boolean isBuilding() { return isBuilding; }
	public void setBuildState(BuildDataState state) { this.state = state; }
	public String getParameter(String key) { return params.get(key); }

	// We do it this way, rather than taking straight from the build object, so we can set some defaults for the JSON later.
	public void setBuild(AbstractBuild build) 
	{
		if(build != null)
		{
			this.buildId = build.getNumber();
			this.isBuilding = build.isBuilding();
		}
	}
	
	// TODO Move this out into a different class - this will just be a bean for containing information.
	public void prepareResponse(StaplerResponse resp) throws IOException 
	{	   	
		resp.setStatus(state.getServletResponse());
		resp.setHeader("Content-Type", "application/json;charset=UTF-8");
	   	//resp.getWriter().print(this.createJson());
	}
	
	private HashMap<String, String> createStringParamMap(StaplerRequest req) 
	{
		HashMap<String, String> values = new HashMap<String, String>();
		
		for (Enumeration<String> e = req.getParameterNames(); e.hasMoreElements();)
		{
			String name = e.nextElement();
			values.put(name, req.getParameter(name));
		}

		return values;
	}

	public ParametersAction getParametersAction() 
	{		
		List<ParameterValue> values = new ArrayList<ParameterValue>();
		for(String key : params.keySet())
		{
			values.add(new StringParameterValue(key, getParameter(key)));
		}
		return new ParametersAction(values);
	}
}
