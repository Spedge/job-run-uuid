package com.spedge.jenkins.jru;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import groovyjarjarantlr.collections.impl.Vector;
import hudson.model.AbstractBuild;
import hudson.model.Project;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.stapler.StaplerRequest;

import com.spedge.jenkins.jru.BuildData;
import com.spedge.jenkins.jru.BuildDataState;
import com.spedge.jenkins.jru.BuildIdCause;

public class BuildDataTest 
{
	private BuildData data;
	private StaplerRequest reqMock;
	private Project<?, ?> projMock;
	private AbstractBuild<?, ?> buildMock;
	
	private UUID uuid = UUID.randomUUID();
	
	@Before
	public void setUp()
	{
		reqMock = createMock(StaplerRequest.class);
		projMock = createMock(Project.class);
		buildMock = createMock(AbstractBuild.class);
	}
	
	@Test
	public void testNoUuidConstructor()
	{
		Vector attr = new Vector();
		attr.appendElement("attempts");
		attr.appendElement("delay");
		
		expect(reqMock.getParameter("attempts")).andReturn("23");
		expect(reqMock.getParameter("delay")).andReturn("44");
		expect(reqMock.getParameterNames()).andReturn(attr.elements());
		replay(reqMock);
		
		data = new BuildData(projMock, reqMock);
		
		verify(reqMock);
		
		assertEquals(23, data.getAttempts());
		assertEquals(44, data.getDelay());
		assertNotNull(data.getCause()); // Can't determine what the UUID will be because we don't pass it in.
		assertEquals(projMock, data.getProject());
		assertFalse(data.isBuildSet());
	}
	
	@Test
	public void testUuidConstructor()
	{
		Vector attr = new Vector();
		attr.appendElement("attempts");
		attr.appendElement("delay");
		
		expect(reqMock.getParameter("attempts")).andReturn("23");
		expect(reqMock.getParameter("delay")).andReturn("44");
		expect(reqMock.getParameterNames()).andReturn(attr.elements());
		replay(reqMock);
		
		data = new BuildData(projMock, uuid, reqMock);
		
		verify(reqMock);
		
		assertEquals(23, data.getAttempts());
		assertEquals(44, data.getDelay());
		assertEquals(new BuildIdCause(uuid), data.getCause());
		assertEquals(projMock, data.getProject());
		assertFalse(data.isBuildSet());
	}
	
	@Test
	public void testSetBuild()
	{
		testUuidConstructor();
		expect(buildMock.getNumber()).andReturn(13);
		expect(buildMock.isBuilding()).andReturn(true);
		replay(buildMock);
		
		data.setBuild(buildMock);
		
		verify(buildMock);
		
		assertTrue(data.isBuildSet());
		assertTrue(data.isBuilding());
		assertEquals(13, data.getBuildId());
	}
}
