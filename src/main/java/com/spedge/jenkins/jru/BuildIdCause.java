package com.spedge.jenkins.jru;

import hudson.model.Cause;

import java.util.UUID;

public class BuildIdCause extends Cause 
{
	private UUID uuid;
	
	public BuildIdCause(UUID uuid)
	{
		this.uuid = uuid;
	}
	
	@Override
	public String getShortDescription() 
	{
		return "Invoked via REST API, UUID : " + uuid;
	}
	
    @Override
    public boolean equals(Object o) {
        return o instanceof BuildIdCause && uuid.equals(((BuildIdCause)o).uuid);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 77 * hash + (this.uuid != null ? this.uuid.hashCode() : 0);
        return hash;
    }
}
