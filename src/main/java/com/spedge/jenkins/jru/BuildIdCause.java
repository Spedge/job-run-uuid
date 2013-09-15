package com.spedge.jenkins.jru;

import hudson.model.Cause;

import java.util.UUID;

/**
 * This is the identifier we attach to the build in order to retrieve
 * it later if required. Because it is applied as a Cause, it happens
 * on instantiation of the run rather than once it leaves the queue 
 * like the Run ID is.
 * 
 * @author Stuart Davidson
 *
 */
public class BuildIdCause extends Cause 
{
	private UUID uuid;
	
	/**
	 * No-arg constructor - generates a random UUID if no UUID is passed in.
	 */
	public BuildIdCause()
	{
	    this.uuid = UUID.randomUUID();
	}

	public BuildIdCause(UUID uuid)
	{
		this.uuid = uuid;
	}

	/**
	 * Get the UUID associated with this Build.
	 * @return UUID of the build.
	 */
	public UUID getUuid()
	{
	    return uuid;
	}
	
	@Override
	public String getShortDescription() 
	{
		return "Invoked via job-run-uuid API, UUID : " + uuid;
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
