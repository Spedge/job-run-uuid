package com.spedge.jenkins.jru;

import hudson.model.Action;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.Job;
import hudson.model.Project;
import hudson.model.Queue.Item;
import hudson.security.Permission;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.ServletException;

import jenkins.model.Jenkins;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

// TODO Contains everything we need - and that's the problem. Take a look at seperating these actions out into more managable chunks
public class BuildIdExecutionAction implements Action 
{
	private final Job job;
	private String urlName = "buildId";
	
	// Default Constructor - requires a job argument.
	BuildIdExecutionAction(Job job) { this.job = job; }
    
    // This method executes a build then returns it's buildId, along with some other information.
	// Consider these builds to always have parameters to be passed in.
    public synchronized void doExecute(StaplerRequest req, StaplerResponse resp) throws IOException, ServletException 
    {
    	BuildData data = new BuildData(BuildIdExecutionAction.findProject(job), req);
    	
    	if(data.getProject() != null)
    	{
    	   	job.getACL().checkPermission(getPermission());
    	
    	   	if (!job.isBuildable())
    	   	{
    	   		data.setBuildState(BuildDataState.NOT_BUILDABLE);
    	   		data.prepareResponse(resp);
    	   	}
    	   	else
    	   	{    	            
    	   		if(Jenkins.getInstance().getQueue().getItem(data.getProject()) == null)
    	   		{
		    	   	Jenkins.getInstance().getQueue().schedule(data.getProject(), 0, data.getParametersAction(), new CauseAction(data.getCause()));
		    	   	
		    	   	findBuildForCause(data);
		        	assessState(data);
		    	   	
		    	   	data.prepareResponse(resp);
    	   		}
    	   		else
    	   		{
    	   			data.setBuildState(BuildDataState.ALREADY_QUEUED);
    	   			data.prepareResponse(resp);
    	   		}
    	   	}
        }
    }
    
    // This method executes a build then returns it's buildId, along with some other information.
    public synchronized void doFindBuild( StaplerRequest req, StaplerResponse resp ) throws IOException, ServletException 
    {
    	String uuid = req.getParameter("uuid");
    	Project proj = BuildIdExecutionAction.findProject(job);
    	
    	if(uuid != null)
    	{
        	BuildData data = new BuildData(proj, UUID.fromString(uuid), req);

        	findBuildForCause(data);
        	assessState(data);

        	data.prepareResponse(resp);
    	}
    	else
    	{
    		BuildData data = new BuildData(proj, req);
    		data.setBuildState(BuildDataState.DATA_NO_UUID);
    		data.prepareResponse(resp);
    	}
    }
    
    // ============ Utility Functions ===============
            	
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
	
	// Finds the project that the job references so that we can add an instance of it to the queue.
	public static Project findProject(Job job)
	{
		for(Project p : Jenkins.getInstance().getProjects())
    	{
    		if(p.getSearchName().equals(job.getSearchName())) { return p; }
    	}
		return null;
	}

	// Finds the build that applies to the AbstractProject/Build we just kicked off.
	// Because the build number isn't applied until the build actually starts running,
	// we've got to wait until it's executed before we can get the build number.
	// This method waits for it. The attempts and delay time can be modified as 
	// parameters within the query - ?attempts=10&delay=100
	private void findBuildForCause(BuildData data)
	{
		for(int i=0; i<data.getAttempts(); i++)
		{
		   	if (job instanceof AbstractProject) {
	            AbstractProject<?,?> p = (AbstractProject) job;
	            for (AbstractBuild<?,?> build : p.getBuilds()) 
	            {
	            	Vector<CauseAction> causeActions = (Vector<CauseAction>) build.getActions(CauseAction.class);
	            	
	            	for(CauseAction ca : causeActions)
	            	{
	            		ArrayList<Cause> causes = (ArrayList<Cause>) ca.getCauses();
	            		
	            		for(Cause c : causes)
	            		{
	            			if(c.equals(data.getCause())) { data.setBuild(build); return; }
	            		}
	            	}
	            }
		   	}
		   	// If this is interrupted, this is no big deal.
		   	try { Thread.sleep(data.getDelay()); } catch (InterruptedException e) { }
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
