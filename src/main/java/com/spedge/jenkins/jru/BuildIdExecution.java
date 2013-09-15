package com.spedge.jenkins.jru;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;
import hudson.model.AbstractProject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is where we add our identifier Action to the Project.
 * @author Stuart Davidson
 *
 */
@Extension
public class BuildIdExecution extends TransientProjectActionFactory 
{
    @Override
    public Collection<? extends Action> createFor(@SuppressWarnings("rawtypes") AbstractProject p) 
    {
        ArrayList<BuildIdExecutionAction> list = new ArrayList<BuildIdExecutionAction>();
        list.add(new BuildIdExecutionAction(p));
        return list;
    }        
}

