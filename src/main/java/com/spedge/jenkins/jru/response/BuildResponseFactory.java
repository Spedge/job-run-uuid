package com.spedge.jenkins.jru.response;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Queue.Item;
import jenkins.model.Jenkins;

import com.spedge.jenkins.jru.BuildState;
import com.spedge.jenkins.jru.BuildIdCause;
import com.spedge.jenkins.jru.params.ExecuteParams;

public class BuildResponseFactory 
{
    private BuildState state;
    private BuildIdCause cause;
    private ExecuteParams params;
    private AbstractBuild<?, ?> build;
    private AbstractProject<?, ?> project;
    
    public void setProject(AbstractProject<?, ?> project)
    {
        this.project = project;
    }
    
    public void setBuildState(BuildState state)
    {
       this.state = state;
    }
    
    public void setCause(BuildIdCause cause)
    {
        this.cause = cause;
    }
    
    public void setExecuteParams(ExecuteParams params)
    {
        this.params = params;
    }
    
    public void setBuild(AbstractBuild<?, ?> build) 
    {
        this.build = build;
    }
    
    private void assessBuildState()
    {
        if(state == null)
        {
            if(build != null)
            {
                if(build.getNumber() > 0) { state = BuildState.DATA_OK; }
                else 
                { 
                    Item queueItem = Jenkins.getInstance().getQueue().getItem(project);
                    if(queueItem != null) { state = BuildState.DATA_NOT_EXECUTING; }
                    else { state = BuildState.DATA_BAD_UUID; }
                }
            }
        }
    }

    // Generates a BuildResponse object from the information it has.
    public BuildResponse generateBuildResponse()
    {
        BuildResponse response = new BuildResponse();
        assessBuildState();
        
        if(build != null)
        {
            response.setBuildId(build.getNumber());
            response.setBuilding(build.isBuilding());
        }
        
        if(params != null)
        {
            response.setDelay(params.getDelay());
            response.setAttempts(params.getAttempts());
        }
        
        if(state != null)
        {
            response.setErrorCode(state.getErrorCode());
            response.setErrorDesc(state.getErrorMessage());
            response.setResponseCode(state.getServletResponse());
        }
        
        if(cause != null)
        {
            response.setUuid(cause.getUuid());
        }
        
        return response;
    }
}
