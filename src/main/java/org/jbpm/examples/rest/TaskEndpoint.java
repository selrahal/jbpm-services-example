package org.jbpm.examples.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.UserTaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.query.QueryFilter;
import org.slf4j.Logger;

@Path("/task")
@Stateless
public class TaskEndpoint {
	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(TaskEndpoint.class);
	
	@Inject 
	private UserTaskService userTaskService;
	
	@Inject
	private RuntimeDataService runtimeDataService;
	
	@GET
	@Path("/list/{userName}")
	public Response list(@PathParam("userName") String userName) {
		LOG.info("Getting task list for " + userName);
		List<TaskSummary> taskSummaries = runtimeDataService.getTasksAssignedAsPotentialOwner(userName, null);
		return Response.ok(taskSummaries).build();
	}

	@GET
	@Path("/claim/{taskId}/{userName}")
	public Response claimTask(@PathParam("taskId") String taskId, @PathParam("userName") String userName) {
		userTaskService.claim(Long.parseLong(taskId), userName);
		return Response.ok().build();
	}
	
	@GET
	@Path("/start/{taskId}/{userName}")
	public Response startTask(@PathParam("taskId") String taskId, @PathParam("userName") String userName) {
		userTaskService.start(Long.parseLong(taskId), userName);
		return Response.ok().build();
	}
	
	@GET
	@Path("/complete/{taskId}/{userName}")
	public Response completeTask(@PathParam("taskId") String taskId, @PathParam("userName") String userName) {
		userTaskService.complete(Long.parseLong(taskId), userName, null);
		return Response.ok().build();
	}
}
