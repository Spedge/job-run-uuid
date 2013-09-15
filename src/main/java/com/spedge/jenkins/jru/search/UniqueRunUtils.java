package com.spedge.jenkins.jru.search;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.Job;

import java.util.ArrayList;
import java.util.Vector;

import com.spedge.jenkins.jru.params.ExecuteParams;

import jenkins.model.Jenkins;

/**
 * Utility Class containing some of the more complicated searches we do
 * on features of a Jenkins Build.
 * 
 * @author Spedge
 *
 */
public class UniqueRunUtils 
{
    // Going to be used as a Utility class, so no constructor allowed.
    private UniqueRunUtils() {}
    
    // Finds the project that the job references so that we can add an instance of it to the queue.
    public static AbstractProject<?, ?> findProject(Job<?, ?> job)
    {
        for(AbstractProject<?, ?> p : Jenkins.getInstance().getAllItems(AbstractProject.class))
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
    public static AbstractBuild<?, ?> findBuildForCause(ExecuteParams params, Cause cause, Job<?, ?> job)
    {
        for(int i=0; i<params.getAttempts(); i++)
        {
            if (job instanceof AbstractProject) 
            {
                AbstractProject<?,?> p = (AbstractProject<?, ?>) job;
                
                for (AbstractBuild<?,?> build : p.getBuilds()) 
                {
                    Vector<CauseAction> causeActions = (Vector<CauseAction>) build.getActions(CauseAction.class);
                    
                    for(CauseAction ca : causeActions)
                    {
                        ArrayList<Cause> causes = (ArrayList<Cause>) ca.getCauses();
                        
                        for(Cause c : causes)
                        {
                            if(c.equals(cause)) { return build; }
                        }
                    }
                }
            }
            
            // If this is interrupted, this is no big deal.
            try { Thread.sleep(params.getDelay()); } catch (InterruptedException e) { }
        }
        
        // Can't find a suitable project.
        return null;
    }
}
