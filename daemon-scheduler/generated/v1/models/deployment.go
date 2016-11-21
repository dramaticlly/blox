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

package models

// This file was generated by the swagger tool.
// Editing this file might prove futile when you re-run the swagger generate command

import (
	"encoding/json"

	strfmt "github.com/go-openapi/strfmt"
	"github.com/go-openapi/swag"

	"github.com/go-openapi/errors"
	"github.com/go-openapi/validate"
)

// Deployment deployment
// swagger:model Deployment
type Deployment struct {

	// List of ECS task ARNs started by this deployment
	CurrentTasks []string `json:"currentTasks,omitempty"`

	// environment name
	// Required: true
	EnvironmentName *string `json:"environmentName"`

	// List of ECS container-instance ARNs where deployment failed
	FailedInstances []string `json:"failedInstances,omitempty"`

	// id
	// Required: true
	ID *string `json:"id"`

	// status
	// Required: true
	Status *string `json:"status"`

	// task definition
	// Required: true
	TaskDefinition *string `json:"taskDefinition"`
}

// Validate validates this deployment
func (m *Deployment) Validate(formats strfmt.Registry) error {
	var res []error

	if err := m.validateCurrentTasks(formats); err != nil {
		// prop
		res = append(res, err)
	}

	if err := m.validateEnvironmentName(formats); err != nil {
		// prop
		res = append(res, err)
	}

	if err := m.validateFailedInstances(formats); err != nil {
		// prop
		res = append(res, err)
	}

	if err := m.validateID(formats); err != nil {
		// prop
		res = append(res, err)
	}

	if err := m.validateStatus(formats); err != nil {
		// prop
		res = append(res, err)
	}

	if err := m.validateTaskDefinition(formats); err != nil {
		// prop
		res = append(res, err)
	}

	if len(res) > 0 {
		return errors.CompositeValidationError(res...)
	}
	return nil
}

func (m *Deployment) validateCurrentTasks(formats strfmt.Registry) error {

	if swag.IsZero(m.CurrentTasks) { // not required
		return nil
	}

	return nil
}

func (m *Deployment) validateEnvironmentName(formats strfmt.Registry) error {

	if err := validate.Required("environmentName", "body", m.EnvironmentName); err != nil {
		return err
	}

	return nil
}

func (m *Deployment) validateFailedInstances(formats strfmt.Registry) error {

	if swag.IsZero(m.FailedInstances) { // not required
		return nil
	}

	return nil
}

func (m *Deployment) validateID(formats strfmt.Registry) error {

	if err := validate.Required("id", "body", m.ID); err != nil {
		return err
	}

	return nil
}

var deploymentTypeStatusPropEnum []interface{}

const (
	deploymentStatusPending   string = "pending"
	deploymentStatusRunning   string = "running"
	deploymentStatusCompleted string = "completed"
)

// prop value enum
func (m *Deployment) validateStatusEnum(path, location string, value string) error {
	if deploymentTypeStatusPropEnum == nil {
		var res []string
		if err := json.Unmarshal([]byte(`["pending","running","completed"]`), &res); err != nil {
			return err
		}
		for _, v := range res {
			deploymentTypeStatusPropEnum = append(deploymentTypeStatusPropEnum, v)
		}
	}
	if err := validate.Enum(path, location, value, deploymentTypeStatusPropEnum); err != nil {
		return err
	}
	return nil
}

func (m *Deployment) validateStatus(formats strfmt.Registry) error {

	if err := validate.Required("status", "body", m.Status); err != nil {
		return err
	}

	// value enum
	if err := m.validateStatusEnum("status", "body", *m.Status); err != nil {
		return err
	}

	return nil
}

func (m *Deployment) validateTaskDefinition(formats strfmt.Registry) error {

	if err := validate.Required("taskDefinition", "body", m.TaskDefinition); err != nil {
		return err
	}

	return nil
}
