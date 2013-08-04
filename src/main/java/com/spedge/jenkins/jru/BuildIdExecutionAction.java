package com.spedge.jenkins.jru;

import hudson.model.Action;
import hudson.model.CauseAction;
import hudson.model.Job;
import hudson.model.Queue.Item;
import hudson.security.Permission;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletException;

import jenkins.model.Jenkins;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.spedge.jenkins.jru.formatters.BuildDataFormatter;
import com.spedge.jenkins.jru.formatters.BuildDataFormatterJsonImpl;

// TODO Contains everything we need - and that's the problem. Take a look at seperating these actions out into more managable chunks
public class BuildIdExecutionAction implements Action 
{
	private final Job<?, ?> job;
	private String urlName = "buildId";
	private static final String DEFAULT_FORMAT = "json";
	private HashMap<String, BuildDataFormatter> formatters;
	
	// Default Constructor - requires a job argument.
	public BuildIdExecutionAction(Job<?, ?> job) 
	{ 
	    this.job = job; 
	    this.formatters = new HashMap<String, BuildDataFormatter>();
	    formatters.put("json", new BuildDataFormatterJsonImpl());
	}
    
    // This method executes a build then returns it's buildId, along with some other information.
	// Consider these builds to always have parameters to be passed in.
    public synchronized void doExecute(StaplerRequest req, StaplerResponse resp) throws IOException, ServletException 
    {
    	BuildData data = new BuildData(UniqueRunUtils.findProject(job), req);
    	
    	if(data.getProject() != null)
    	{
    	   	job.getACL().checkPermission(getPermission());
    	
    	   	if (!job.isBuildable())
    	   	{
    	   		data.setBuildState(BuildDataState.NOT_BUILDABLE);
    	   	}
    	   	else
    	   	{    	            
    	   		if(Jenkins.getInstance().getQueue().getItem(data.getProject()) == null)
    	   		{
		    	   	Jenkins.getInstance().getQueue().schedule(data.getProject(), 0, data.getParametersAction(), new CauseAction(data.getCause()));
		    	   	UniqueRunUtils.findBuildForCause(data, job);
		        	assessState(data);
    	   		}
    	   		else
    	   		{
    	   			data.setBuildState(BuildDataState.ALREADY_QUEUED);
    	   		}
    	   	}
        }
    	
    	executeResponse(resp, data, req.getParameter("format"));
    }
    
    // This method executes a build then returns it's buildId, along with some other information.
    public synchronized void doFindBuild(StaplerRequest req, StaplerResponse resp) throws IOException, ServletException 
    {
    	String uuid = req.getParameter("uuid");
    	BuildData data = new BuildData(UniqueRunUtils.findProject(job), req);
    	
    	if(uuid != null)
    	{
        	data.setUUID(UUID.fromString(uuid));
        	UniqueRunUtils.findBuildForCause(data, job);
        	assessState(data);
    	}
    	
    	executeResponse(resp, data, req.getParameter("format"));
    }
    
    // ============ Utility Functions ===============
    
    /**
     * Determines the proper format to use, then sends the response to the requester.
     * @param resp {@StaplerResponse} 
     * @param data
     * @param formatter
     * @throws IOException
     */
    private void executeResponse(StaplerResponse resp, BuildData data, String format) throws IOException 
    {       
        BuildDataFormatter formatter = ((format != null) && (formatters.containsKey(format))) ?
                                        formatters.get(format) :
                                        formatters.get(DEFAULT_FORMAT);
        
        resp.setStatus(data.getResponseStatus());
        resp.setHeader("Content-Type", formatter.getContentType());
        resp.getWriter().print(formatter.generateOutput(data));
    }
    
            	
	private void assessState(BuildData data) 
	{
    	if(data.isBuildSet()) { data.setBuildState(BuildDataState.DATA_OK); }
	   	else 
	   	{ 
	   		Item queueItem = Jenkins.getInstance().getQueue().getItem(data.getProject());
	   		if(queueItem != null) { data.setBuildState(BuildDataState.DATA_NOT_EXECUTING); }
	   		else { data.setBuildState(BuildDataState.DATA_BAD_UUID); }
	   	}
	}
			
	// Public methods required by the Action Interface.
	public String getUrlName() { return urlName; }
    public Permission getPermission() { return Job.BUILD; }
    
	// Jenkins-y nonsense that doesn't apply to this particular action.
	public String getIconFileName() {return null;}
	public String getDisplayName() {return null;}
	public boolean isEnabled() { return true; }
	public void actionPerformed(ActionEvent e) {}
	public Object getValue(String key) { return null; }
	public void putValue(String key, Object value) {}
	public void setEnabled(boolean b) {}
	public void addPropertyChangeListener(PropertyChangeListener listener) {}
	public void removePropertyChangeListener(PropertyChangeListener listener) {}
}
