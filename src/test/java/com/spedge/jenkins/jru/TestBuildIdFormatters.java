package com.spedge.jenkins.jru;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.spedge.jenkins.jru.response.BuildResponseFormatter;
import com.spedge.jenkins.jru.response.BuildResponseFormatterJsonImpl;

public class TestBuildIdFormatters 
{
    @Test
    public void testGetFormatter()
    {
        BuildResponseFormatter formatter = BuildIdFormatters.getFormatter("gravy");
        assertEquals("application/json;charset=UTF-8", formatter.getContentType());
        
        BuildResponseFormatter formatter2 = BuildIdFormatters.getFormatter(null);
        assertEquals("application/json;charset=UTF-8", formatter2.getContentType());
        
        BuildResponseFormatter formatter3 = BuildIdFormatters.getFormatter(BuildResponseFormatterJsonImpl.NAME);
        assertEquals(formatter, formatter3);
    }
}
