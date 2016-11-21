// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License"). You may
// not use this file except in compliance with the License. A copy of the
// License is located at
//
//     http://aws.amazon.com/apache2.0/
//
// or in the "license" file accompanying this file. This file is distributed
// on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied. See the License for the specific language governing
// permissions and limitations under the License.

// Automatically generated by MockGen. DO NOT EDIT!
// Source: pkg/facade/css.go

package mocks

import (
	models "github.com/blox/blox/daemon-scheduler/pkg/clients/css/models"
	gomock "github.com/golang/mock/gomock"
)

// Mock of ClusterState interface
type MockClusterState struct {
	ctrl     *gomock.Controller
	recorder *_MockClusterStateRecorder
}

// Recorder for MockClusterState (not exported)
type _MockClusterStateRecorder struct {
	mock *MockClusterState
}

func NewMockClusterState(ctrl *gomock.Controller) *MockClusterState {
	mock := &MockClusterState{ctrl: ctrl}
	mock.recorder = &_MockClusterStateRecorder{mock}
	return mock
}

func (_m *MockClusterState) EXPECT() *_MockClusterStateRecorder {
	return _m.recorder
}

func (_m *MockClusterState) ListInstances(cluster string) ([]*models.ContainerInstance, error) {
	ret := _m.ctrl.Call(_m, "ListInstances", cluster)
	ret0, _ := ret[0].([]*models.ContainerInstance)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

func (_mr *_MockClusterStateRecorder) ListInstances(arg0 interface{}) *gomock.Call {
	return _mr.mock.ctrl.RecordCall(_mr.mock, "ListInstances", arg0)
}

func (_m *MockClusterState) ListTasks(cluster string) ([]*models.Task, error) {
	ret := _m.ctrl.Call(_m, "ListTasks", cluster)
	ret0, _ := ret[0].([]*models.Task)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

func (_mr *_MockClusterStateRecorder) ListTasks(arg0 interface{}) *gomock.Call {
	return _mr.mock.ctrl.RecordCall(_mr.mock, "ListTasks", arg0)
}
