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
package com.amazonaws.blox.frontend.operations;

import static org.mockito.Mockito.mock;

import com.amazonaws.blox.dataservicemodel.v1.client.DataService;
import com.amazonaws.blox.dataservicemodel.v1.model.Attribute;
import com.amazonaws.blox.dataservicemodel.v1.model.DeploymentConfiguration;
import com.amazonaws.blox.dataservicemodel.v1.model.Environment;
import com.amazonaws.blox.dataservicemodel.v1.model.EnvironmentId;
import com.amazonaws.blox.dataservicemodel.v1.model.EnvironmentRevision;
import com.amazonaws.blox.dataservicemodel.v1.model.EnvironmentType;
import com.amazonaws.blox.dataservicemodel.v1.model.InstanceGroup;
import com.amazonaws.blox.frontend.MapperConfiguration;
import com.amazonaws.serverless.proxy.internal.model.ApiGatewayRequestContext;
import com.amazonaws.serverless.proxy.internal.servlet.AwsProxyHttpServletRequestReader;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

// TODO: We only use the Spring runner in order to wire in the needed mappers. See the comment on
// MapperConfiguration for details.
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EnvironmentControllerTestCase.Config.class)
@ActiveProfiles("api_test")
public abstract class EnvironmentControllerTestCase {

  protected static final String ROLE = "TestRole";
  protected static final String HEALTHY = "Healthy";
  protected static final String STATUS = "Active";
  protected static final String DEPLOYMENT_METHOD = "ReplaceAfterTerminate";
  protected static final String ENVIRONMENT_REVISION_ID = "TestEnvironmentRevision";
  protected static final String TASK_DEFINITION = "TestTaskDefinition";
  protected static final String ENVIRONMENT_NAME = "TestEnvironment";
  protected static final String TEST_CLUSTER = "TestCluster";
  protected static final String ENVIRONMENT_TYPE_STRING = "Daemon";
  protected static final EnvironmentType ENVIRONMENT_TYPE = EnvironmentType.Daemon;
  protected static final String ATTRIBUTE_NAME = "TestAttributeName";
  protected static final String ATTRIBUTE_VALUE = "TestAttributeValue";
  protected static final String ACCOUNT_ID = "1234567890";
  @Autowired DataService dataService;
  @Autowired HttpServletRequest servletRequest;

  @Configuration
  @Import(MapperConfiguration.class)
  @ComponentScan("com.amazonaws.blox.frontend.operations")
  @Profile("api_test")
  static class Config {
    @Bean
    public DataService dataService() {
      return mock(DataService.class);
    }

    @Bean
    public HttpServletRequest httpServletRequest() {
      return new MockHttpServletRequest();
    }
  }

  @Before
  public void setupRequest() {
    ApiGatewayRequestContext requestContext = new ApiGatewayRequestContext();
    requestContext.setAccountId(ACCOUNT_ID);
    servletRequest.setAttribute(
        AwsProxyHttpServletRequestReader.API_GATEWAY_CONTEXT_PROPERTY, requestContext);
  }

  // TODO: Pull these helper methods out into a fixture generator class, so that we can do e.g:
  // fixtures.DS.instanceGroup("key", "value");
  // fixtures.FE.instanceGroup("key", "value");

  protected com.amazonaws.blox.frontend.models.InstanceGroup instanceGroupWithAttributeFE(
      String attributeName, String attributeValue) {
    return com.amazonaws.blox.frontend.models.InstanceGroup.builder()
        .attributes(
            new HashSet<>(
                Arrays.asList(
                    com.amazonaws.blox.frontend.models.Attribute.builder()
                        .name(attributeName)
                        .value(attributeValue)
                        .build())))
        .build();
  }

  protected InstanceGroup instanceGroupDS() {
    return instanceGroupWithAttributeDS(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
  }

  protected InstanceGroup instanceGroupWithAttributeDS(
      String attributeName, String attributeValue) {
    return InstanceGroup.builder()
        .attributes(
            new HashSet<>(
                Arrays.asList(
                    Attribute.builder().name(attributeName).value(attributeValue).build())))
        .build();
  }

  protected DeploymentConfiguration deploymentConfigurationDS() {
    return DeploymentConfiguration.builder().build();
  }

  protected com.amazonaws.blox.frontend.models.DeploymentConfiguration deploymentConfigurationFE() {
    return com.amazonaws.blox.frontend.models.DeploymentConfiguration.builder().build();
  }

  protected EnvironmentRevision environmentRevisionDS(
      final EnvironmentId id, final InstanceGroup instanceGroup) {
    return environmentRevisionDS(id, TASK_DEFINITION, instanceGroup);
  }

  protected EnvironmentRevision environmentRevisionDS(
      final EnvironmentId id, final String taskDefinition, final InstanceGroup instanceGroup) {
    return EnvironmentRevision.builder()
        .environmentId(id)
        .environmentRevisionId(ENVIRONMENT_REVISION_ID)
        .instanceGroup(instanceGroup)
        .taskDefinition(taskDefinition)
        .createdTime(Instant.now())
        .build();
  }

  protected Environment environmentDS(
      final EnvironmentId id, final DeploymentConfiguration deploymentConfiguration) {
    return Environment.builder()
        .environmentId(id)
        .role(ROLE)
        .environmentType(ENVIRONMENT_TYPE)
        .environmentHealth(HEALTHY)
        .environmentStatus(STATUS)
        .deploymentMethod(DEPLOYMENT_METHOD)
        .deploymentConfiguration(deploymentConfiguration)
        .createdTime(Instant.now())
        .lastUpdatedTime(Instant.now())
        .build();
  }
}
