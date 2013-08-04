package com.spedge.jenkins.jru;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import hudson.model.AbstractBuild;
import hudson.model.Project;

import java.util.UUID;
import java.util.Vector;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.kohsuke.stapler.StaplerRequest;

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
		reqMock = EasyMock.createMock(StaplerRequest.class);
		projMock = EasyMock.createMock(Project.class);
		buildMock = EasyMock.createMock(AbstractBuild.class);
	}
	
	@Test
	public void testNoUuidConstructor()
	{
		Vector<String> attr = new Vector<String>();
		attr.addElement("attempts");
		attr.addElement("delay");
		
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
		Vector<String> attr = new Vector<String>();
		attr.addElement("attempts");
		attr.addElement("delay");
		
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
