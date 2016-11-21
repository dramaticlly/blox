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

package operations

// This file was generated by the swagger tool.
// Editing this file might prove futile when you re-run the swagger generate command

import (
	"time"

	"golang.org/x/net/context"

	"github.com/go-openapi/errors"
	"github.com/go-openapi/runtime"
	cr "github.com/go-openapi/runtime/client"

	strfmt "github.com/go-openapi/strfmt"
)

// NewListEnvironmentsParams creates a new ListEnvironmentsParams object
// with the default values initialized.
func NewListEnvironmentsParams() *ListEnvironmentsParams {
	var ()
	return &ListEnvironmentsParams{

		timeout: cr.DefaultTimeout,
	}
}

// NewListEnvironmentsParamsWithTimeout creates a new ListEnvironmentsParams object
// with the default values initialized, and the ability to set a timeout on a request
func NewListEnvironmentsParamsWithTimeout(timeout time.Duration) *ListEnvironmentsParams {
	var ()
	return &ListEnvironmentsParams{

		timeout: timeout,
	}
}

// NewListEnvironmentsParamsWithContext creates a new ListEnvironmentsParams object
// with the default values initialized, and the ability to set a context for a request
func NewListEnvironmentsParamsWithContext(ctx context.Context) *ListEnvironmentsParams {
	var ()
	return &ListEnvironmentsParams{

		Context: ctx,
	}
}

/*ListEnvironmentsParams contains all the parameters to send to the API endpoint
for the list environments operation typically these are written to a http.Request
*/
type ListEnvironmentsParams struct {

	/*NextToken
	  Pagination token

	*/
	NextToken *string

	timeout time.Duration
	Context context.Context
}

// WithTimeout adds the timeout to the list environments params
func (o *ListEnvironmentsParams) WithTimeout(timeout time.Duration) *ListEnvironmentsParams {
	o.SetTimeout(timeout)
	return o
}

// SetTimeout adds the timeout to the list environments params
func (o *ListEnvironmentsParams) SetTimeout(timeout time.Duration) {
	o.timeout = timeout
}

// WithContext adds the context to the list environments params
func (o *ListEnvironmentsParams) WithContext(ctx context.Context) *ListEnvironmentsParams {
	o.SetContext(ctx)
	return o
}

// SetContext adds the context to the list environments params
func (o *ListEnvironmentsParams) SetContext(ctx context.Context) {
	o.Context = ctx
}

// WithNextToken adds the nextToken to the list environments params
func (o *ListEnvironmentsParams) WithNextToken(nextToken *string) *ListEnvironmentsParams {
	o.SetNextToken(nextToken)
	return o
}

// SetNextToken adds the nextToken to the list environments params
func (o *ListEnvironmentsParams) SetNextToken(nextToken *string) {
	o.NextToken = nextToken
}

// WriteToRequest writes these params to a swagger request
func (o *ListEnvironmentsParams) WriteToRequest(r runtime.ClientRequest, reg strfmt.Registry) error {

	r.SetTimeout(o.timeout)
	var res []error

	if o.NextToken != nil {

		// query param nextToken
		var qrNextToken string
		if o.NextToken != nil {
			qrNextToken = *o.NextToken
		}
		qNextToken := qrNextToken
		if qNextToken != "" {
			if err := r.SetQueryParam("nextToken", qNextToken); err != nil {
				return err
			}
		}

	}

	if len(res) > 0 {
		return errors.CompositeValidationError(res...)
	}
	return nil
}
