package com.spedge.jenkins.jru;

import java.util.ArrayList;
import java.util.Vector;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.Job;
import hudson.model.Project;
import jenkins.model.Jenkins;

public class UniqueRunUtils 
{
    private UniqueRunUtils() {}
    
    // Finds the project that the job references so that we can add an instance of it to the queue.
    public static Project<?, ?> findProject(Job<?, ?> job)
    {
        for(Project<?, ?> p : Jenkins.getInstance().getProjects())
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
    public static void findBuildForCause(BuildData data, Job<?, ?> job)
    {
        for(int i=0; i<data.getAttempts(); i++)
        {
            if (job instanceof AbstractProject) {
                AbstractProject<?,?> p = (AbstractProject<?, ?>) job;
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
}
