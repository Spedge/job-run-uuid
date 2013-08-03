job-run-uuid
----=============----

This is a Jenkins Plugin that allows you to call a build using <job>/buildId/execute 
and get back a set of JSON that gives you the build ID and a UUID associated with the build.

You can then use either piece of information to determine the state of the builds you have launched.
This plug-in will allow you to identify a build even if it hasn't got out of the queue yet (meaning it has no run id). 
Very useful for parallel job tracking.

You can use the UUID of the build you executed to track it, even if it hasn't executed yet. 
Pass the uuid in as a parameter in <job>/buildId/findBuild and if it's there, this will find it.

----=============----

Example : Execute

http://localhost:8080/job/Test%20Job/buildId/execute

returns...

HTTP : 200
{"attempts":10,"buildId":6,"building":false,"delay":600,"errorCode":0,"errorDesc":"OK","uuid":"e20d1a22-befc-4222-aa06-98766c3e6d3d"}
...if successful. The buildId is the build number for the job.

For other error information, look at BuildDataState.java

----=============----

Example : FindBuild

http://localhost:8080/job/Test%20Job/buildId/findBuild?uuid=e20d1a22-befc-4222-aa06-987266c3e6d3d

returns...

HTTP : 200
{"attempts":10,"buildId":6,"building":false,"delay":600,"errorCode":0,"errorDesc":"OK","uuid":"e20d1a22-befc-4222-aa06-98766c3e6d3d"}
...if successful. The buildId is the build number for the job.

For other error information, look at BuildDataState