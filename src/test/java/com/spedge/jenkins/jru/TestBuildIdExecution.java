package com.spedge.jenkins.jru;

import static org.junit.Assert.assertEquals;
import hudson.model.Action;
import hudson.model.AbstractProject;

import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;

public class TestBuildIdExecution 
{
    @Test
    public void testCreateFor()
    {
        AbstractProject<?, ?> p = EasyMock.createMock(AbstractProject.class);
        
        BuildIdExecution bie = new BuildIdExecution();
        Collection<? extends Action> list = bie.createFor(p);
        
        assertEquals(1, list.size());        
    }
}
