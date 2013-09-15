package com.spedge.jenkins.jru.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

public class TestBuildResponse 
{
    @Test
    public void testGettersAndSetters()
    {
        BuildResponse resp = new BuildResponse();
        UUID uuid = UUID.randomUUID();
        
        assertNull(resp.getUuid());
        
        resp.setAttempts(12);
        resp.setBuildId(13);
        resp.setBuilding(true);
        resp.setDelay(14);
        resp.setErrorCode(3);
        resp.setErrorDesc("Errors!");
        resp.setFormat("gravy");
        resp.setResponseCode(15);
        resp.setUuid(uuid);
        
        assertEquals(12, resp.getAttempts());
        assertEquals(13, resp.getBuildId());
        assertTrue(resp.isBuilding());
        assertEquals(14, resp.getDelay());
        assertEquals(3, resp.getErrorCode());
        assertEquals("Errors!", resp.getErrorDesc());
        assertEquals("gravy", resp.getFormat());
        assertEquals(15, resp.getResponseCode());
        assertEquals(uuid, resp.getUuid());
    }
}
