package org.jbpm.examples.rest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.services.api.DeploymentService;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessDefinition;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.jbpm.services.api.model.UserTaskInstanceDesc;
import org.slf4j.Logger;

@Path("/process")
@Stateless
public class ProcessEndpoint {
	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ProcessEndpoint.class);
	
	@Inject 
	private DeploymentService deploymentService;
	
	@Inject
	private ProcessService processService;
	
	@Inject
	private RuntimeDataService runtimeDataService;
	

	@GET
	@Path("/start/{gav}/{processId}")
	public Response startProcess(@PathParam("gav") String gav, @PathParam("processId") String processId) {
		if (!deploymentService.isDeployed(gav)) {
			LOG.info("Deploying " + gav);
			String[] tokens = gav.split(":");
			KModuleDeploymentUnit deploymentUnit = new KModuleDeploymentUnit(tokens[0], tokens[1], tokens[2]);
			deploymentService.deploy(deploymentUnit);
		}
		
		Long processInstanceId = processService.startProcess(gav, processId);
		return Response.ok(processInstanceId).build();
	}
	
	@GET
	@Path("/status/{processInstanceId}")
	public Response getStatus(@PathParam("processInstanceId") Long processInstanceId) {
		ProcessInstanceDesc processInstanceDesc = runtimeDataService.getProcessInstanceById(processInstanceId);
		String deploymentId = processInstanceDesc.getDeploymentId();
		Integer state = processInstanceDesc.getState();
		String processName = processInstanceDesc.getProcessName();
		List<UserTaskInstanceDesc> activeTasks = processInstanceDesc.getActiveTasks();
		StringBuilder sb = new StringBuilder(deploymentId + ", " + processName + "," + state + "\n");
		for (UserTaskInstanceDesc utid : activeTasks) {
			sb.append(utid.getName() + "," + utid.getStatus() + "," + utid.getActualOwner() + "," + utid.getTaskId()+ "\n");
		}
		return Response.ok(sb.toString()).build();
	}
	
	@GET
	@Path("/tasks/{processInstanceId}")
	public Response getTasks(@PathParam("processInstanceId") String processInstanceId) {
		ProcessDefinition processInstanceDesc = runtimeDataService.getProcessById(processInstanceId);
		Map<String, String> servicetasks = processInstanceDesc.getServiceTasks();
		Map<String, Collection<String>> associatedEntities = processInstanceDesc.getAssociatedEntities();

		return Response.ok(servicetasks).build();
	}
}
