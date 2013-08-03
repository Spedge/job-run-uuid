package com.spedge.jenkins.jru.formatters;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

// TODO This was copied and pasted - you need to reformat and tidy this up. Not even sure it tests everything correctly - get code coverage on it.
public class TestBuildDataFormatterJsonImpl 
{
	private BuildData data;
	private StaplerRequest reqMock;
	private Project<?, ?> projMock;
	private AbstractBuild<?, ?> buildMock;
	private BuildDataFormatter formatter = new BuildDataFormatterJsonImpl();
	
	private UUID uuid = UUID.randomUUID();
	
	@Before
	public void setUp()
	{
		reqMock = createMock(StaplerRequest.class);
		projMock = createMock(Project.class);
		buildMock = createMock(AbstractBuild.class);
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
	public void testCreateJsonStateOk()
	{
		testUuidConstructor();
		data.setBuildState(BuildDataState.DATA_OK);
		
		expect(buildMock.getNumber()).andReturn(680);
		expect(buildMock.isBuilding()).andReturn(true);
		replay(buildMock);
		
		data.setBuild(buildMock);
		
		assertEquals(createOkJsonString(uuid), formatter.generateOutput(data));		
	}
	
	@Test
	public void testCreateJsonStateBad()
	{
		testUuidConstructor();
		data.setBuildState(BuildDataState.DATA_BAD_UUID);
		
		expect(buildMock.getNumber()).andReturn(680);
		expect(buildMock.isBuilding()).andReturn(true);
		replay(buildMock);
		
		data.setBuild(buildMock);
		
		assertEquals(createBadJsonString(uuid), formatter.generateOutput(data));		
	}
	
	private String createOkJsonString(UUID uuid)
	{
		return "{"
				+ "\"buildId\":680,"
				+ "\"building\":true," 
				+ "\"delay\":44,"
				+ "\"attempts\":23,"
				+ "\"errorCode\":0,"
				+ "\"errorDesc\":\"OK\"," 
				+ "\"params\":{\"attempts\":\"23\",\"delay\":\"44\"}," 
				+ "\"uuid\":\"" + uuid.toString() + "\"}";	
	}
	
	private String createBadJsonString(UUID uuid)
	{
		return "{"
				+ "\"buildId\":680,"
				+ "\"building\":true," 
				+ "\"delay\":44,"
				+ "\"attempts\":23,"
				+ "\"errorCode\":2,"
				+ "\"errorDesc\":\"There is no build associated with this UUID. Confirm it is correct.\"," 
				+ "\"params\":{\"attempts\":\"23\",\"delay\":\"44\"}," 
				+ "\"uuid\":\"" + uuid.toString() + "\"}";	
	}
}
