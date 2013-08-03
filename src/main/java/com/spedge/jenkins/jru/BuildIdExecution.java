package com.spedge.jenkins.jru;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;
import hudson.model.AbstractProject;

import java.util.ArrayList;
import java.util.Collection;

@Extension
public class BuildIdExecution extends TransientProjectActionFactory {
    @Override
    public Collection<? extends Action> createFor(AbstractProject p) {
        ArrayList<BuildIdExecutionAction> list = new ArrayList<BuildIdExecutionAction>();
        list.add( new BuildIdExecutionAction(p));
        return list;
    }        
}

