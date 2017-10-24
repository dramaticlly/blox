/*
 * Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may
 * not use this file except in compliance with the License. A copy of the
 * License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.blox.scheduling;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.blox.dataservicemodel.v1.client.DataService;
import com.amazonaws.blox.dataservicemodel.v1.model.DeploymentType;
import com.amazonaws.blox.dataservicemodel.v1.model.EnvironmentVersion;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.DescribeTargetEnvironmentRevisionResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.DescribeTargetEnvironmentRevisionRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.ListClustersResponse;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.ListEnvironmentsRequest;
import com.amazonaws.blox.dataservicemodel.v1.model.wrappers.ListEnvironmentsResponse;
import com.amazonaws.blox.lambda.TestLambdaFunction;
import com.amazonaws.blox.scheduling.manager.ManagerHandler;
import com.amazonaws.blox.scheduling.manager.ManagerInput;
import com.amazonaws.blox.scheduling.manager.ManagerOutput;
import com.amazonaws.blox.scheduling.reconciler.CloudWatchEvent;
import com.amazonaws.blox.scheduling.reconciler.ReconcilerHandler;
import com.amazonaws.blox.scheduling.scheduler.SchedulerHandler;
import com.amazonaws.blox.scheduling.scheduler.SchedulerInput;
import com.amazonaws.blox.scheduling.scheduler.SchedulerOutput;
import com.amazonaws.blox.scheduling.scheduler.engine.SchedulerFactory;
import com.amazonaws.blox.scheduling.state.ClusterSnapshot;
import com.amazonaws.blox.scheduling.state.ClusterSnapshot.ContainerInstance;
import com.amazonaws.blox.scheduling.state.ECSState;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.ecs.ECSAsyncClient;
import software.amazon.awssdk.services.ecs.model.StartTaskRequest;
import software.amazon.awssdk.services.ecs.model.StartTaskResponse;

@Log4j2
public class SchedulingSystemTest {

  private static final String CLUSTER_ARN = "arn:::::cluster1";
  private static final String ENVIRONMENT_ARN = "arn:::::environment1";
  private static final String INSTANCE_ARN = "arn:::::instance1";
  private static final String TASKDEF_ARN = "arn:::::task:1";

  private final ClusterSnapshot snapshot =
      new ClusterSnapshot(CLUSTER_ARN, Collections.emptyMap(), new HashMap<>());

  private final DataService dataService = mock(DataService.class);

  private final ECSState ecsState = mock(ECSState.class);
  private final ECSAsyncClient ecs = mock(ECSAsyncClient.class);

  private final SchedulerFactory schedulerFactory = new SchedulerFactory();
  private final SchedulerHandler scheduler =
      new SchedulerHandler(dataService, ecs, schedulerFactory);
  private final TestLambdaFunction<SchedulerInput, SchedulerOutput> schedulerClient =
      new TestLambdaFunction<>(scheduler);

  private final ManagerHandler manager = new ManagerHandler(dataService, ecsState, schedulerClient);
  private final TestLambdaFunction<ManagerInput, ManagerOutput> managerClient =
      new TestLambdaFunction<>(manager);

  @Test
  public void runSingleReconciliation() {
    when(dataService.listClusters(any()))
        .thenReturn(new ListClustersResponse(Arrays.asList(CLUSTER_ARN)));

    when(ecsState.snapshotState(CLUSTER_ARN)).thenReturn(snapshot).getMock();

    when(dataService.listEnvironments(new ListEnvironmentsRequest(CLUSTER_ARN)))
        .thenReturn(new ListEnvironmentsResponse(Arrays.asList(ENVIRONMENT_ARN)));

    when(dataService.describeTargetEnvironmentRevision(
            new DescribeTargetEnvironmentRevisionRequest(ENVIRONMENT_ARN)))
        .thenReturn(
            new DescribeTargetEnvironmentRevisionResponse(
                EnvironmentVersion.builder()
                    .deploymentType(DeploymentType.SingleTask)
                    .taskDefinition(TASKDEF_ARN)
                    .build()));

    when(ecs.startTask(any()))
        .thenReturn(
            CompletableFuture.completedFuture(StartTaskResponse.builder().failures().build()));

    snapshot.getInstances().put(INSTANCE_ARN, new ContainerInstance(INSTANCE_ARN));

    ReconcilerHandler recon = new ReconcilerHandler(dataService, managerClient);
    recon.handleRequest(new CloudWatchEvent<>(), null);

    ArgumentCaptor<StartTaskRequest> startArgument =
        ArgumentCaptor.forClass(StartTaskRequest.class);
    verify(ecs).startTask(startArgument.capture());

    StartTaskRequest request = startArgument.getValue();
    assertThat(request.cluster(), equalTo(CLUSTER_ARN));
    assertThat(request.containerInstances(), hasItem(INSTANCE_ARN));
    assertThat(request.taskDefinition(), equalTo(TASKDEF_ARN));
  }
}