package com.gog.civilregistry.adoption.service;


import com.gog.civilregistry.adoption.model.ClaimApplicationWfRequest;
import com.gog.civilregistry.adoption.model.GetWfNextStageRequest;
import com.gog.civilregistry.adoption.model.GetWfNextUserRequest;
import com.gog.civilregistry.adoption.model.ReleaseApplicationWfRequest;
import com.gog.civilregistry.adoption.model.common.ServiceResponse;

public interface WorkflowService {
	
	ServiceResponse getWfNextStage(GetWfNextStageRequest request);

	ServiceResponse getWfNextUser(GetWfNextUserRequest request);
	
	ServiceResponse claimApplicationWorkflow(ClaimApplicationWfRequest request);

	ServiceResponse releaseApplicationWorkflow(ReleaseApplicationWfRequest request);

}
