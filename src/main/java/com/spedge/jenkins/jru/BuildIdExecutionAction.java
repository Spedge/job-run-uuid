package com.spedge.jenkins.jru;

import hudson.model.Action;
import hudson.model.AbstractProject;
import hudson.model.CauseAction;
import hudson.model.Job;
import hudson.model.ParametersAction;
import hudson.security.Permission;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.servlet.ServletException;

import jenkins.model.Jenkins;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.spedge.jenkins.jru.params.ExecuteParams;
import com.spedge.jenkins.jru.response.BuildResponse;
import com.spedge.jenkins.jru.response.BuildResponseFactory;
import com.spedge.jenkins.jru.response.BuildResponseFormatter;
import com.spedge.jenkins.jru.search.UniqueRunUtils;

public class BuildIdExecutionAction implements Action 
{
	private final Job<?, ?> job;
	private static final String URL_NAME = "buildId";
	
	// Default Constructor - requires a job argument.
	public BuildIdExecutionAction(Job<?, ?> job) 
	{ 
	    this.job = job;
	}
    
    // This method executes a build then returns it's buildId, along with some other information.
	// Consider these builds to always have parameters to be passed in.
    public synchronized void doExecute(StaplerRequest req, StaplerResponse resp) throws IOException, ServletException 
    {
        ExecuteParams params = new ExecuteParams(req);
        BuildResponseFactory brf = new BuildResponseFactory();
          
        // We need to determine the project that this job is associated with
        // so that we can determine if there is one on the queue and if we need to schedule a new one.
        AbstractProject<?, ?> p = UniqueRunUtils.findProject(job);
        brf.setProject(p);
        
        // Provided we find a Project...
    	if(p != null)
    	{
    	    // Make sure we can run it.
    	   	job.getACL().checkPermission(getPermission());
    	
    	   	// If it's not buildable, return that fact.
    	   	if (!job.isBuildable())
    	   	{
    	   		brf.setBuildState(BuildState.NOT_BUILDABLE);
    	   	}
    	   	else
    	   	{    	            
    	   	    // If there are no instances of this project as a job on the queue,
    	   	    // schedule one with our custom cause UUID attached.
    	   		if(Jenkins.getInstance().getQueue().getItem(p) == null)
    	   		{
    	   		    BuildIdCause cause = new BuildIdCause();
		    	   	Jenkins.getInstance().getQueue().schedule(p, 0, new ParametersAction(params.getParameters()), new CauseAction(cause));
		    	   	brf.setBuild(UniqueRunUtils.findBuildForCause(params, cause, job));
		            brf.setCause(cause);
    	   		}
    	   		else
    	   		{
    	   			brf.setBuildState(BuildState.ALREADY_QUEUED);
    	   		}
    	   	}
        }
    	
    	executeResponse(resp, brf);
    }
    
    // This method executes a build then returns it's buildId, along with some other information.
    public synchronized void doFindBuild(StaplerRequest req, StaplerResponse resp) throws IOException, ServletException 
    {
        ExecuteParams params = new ExecuteParams(req);
        BuildResponseFactory brf = new BuildResponseFactory();
        brf.setProject(UniqueRunUtils.findProject(job));

    	if(params.getUUID() != null)
    	{
    	    BuildIdCause cause = new BuildIdCause(params.getUUID());
          	brf.setBuild(UniqueRunUtils.findBuildForCause(params, cause, job));
    	}
    	
    	executeResponse(resp, brf);
    }
    
    // ============ Utility Functions ===============
    
    /**
     * Determines the proper format to use, then sends the response to the requester.
     * @param resp {@StaplerResponse} 
     * @param brf {@BuildResponseFactory}
     * @throws IOException
     */
    private void executeResponse(StaplerResponse resp, BuildResponseFactory brf) throws IOException 
    {       
        // Build the response object.
        BuildResponse response = brf.generateBuildResponse();
        
        // Get the format we want.
        BuildResponseFormatter format = BuildIdFormatters.getFormatter(response.getFormat());
        
        resp.setStatus(response.getResponseCode());
        resp.setHeader("Content-Type", format.getContentType());
        resp.getWriter().print(format.generateOutput(response));
    }
    	
	// Public methods required by the Action Interface.
	public String getUrlName() { return URL_NAME; }
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
