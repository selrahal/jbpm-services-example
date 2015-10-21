package org.jbpm.examples;

import java.util.List;

import org.kie.api.task.UserGroupCallback;

public class CustomCallback implements UserGroupCallback{

	public boolean existsUser(String userId) {
		return true;
	}

	public boolean existsGroup(String groupId) {
		return true;
	}

	public List<String> getGroupsForUser(String userId, List<String> groupIds,
			List<String> allExistingGroupIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
