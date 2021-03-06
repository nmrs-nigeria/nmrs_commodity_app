/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

define(
    [
        openhmis.url.backboneBase + 'js/view/generic',
        openhmis.url.backboneBase + 'js/view/openhmis',
        openhmis.url.backboneBase + 'js/view/editors',
        openhmis.url.backboneBase + 'js/lib/backbone-forms'
    ],
    function(openhmis) {
        openhmis.ConsumptionSummaryAddEditView = openhmis.GenericAddEditView.extend({
            initialize: function(options) {
                _.bindAll(this);
                this.events = _.extend({}, this.events, {
                  
                });
                openhmis.GenericAddEditView.prototype.initialize.call(this, options);

                              
            },

            fetch: function(options) {
                options.queryString = openhmis.addQueryStringParameter(options.queryString, "consumption_uuid=" + this.model.id);
            },

            prepareModelForm: function(model, options) {
                var modelForm = openhmis.GenericAddEditView.prototype.prepareModelForm.call(this, model, options);
               // modelForm.on('prices:change', this.updatePriceOptions);
                return modelForm;
            },

            render: function() {
                openhmis.GenericAddEditView.prototype.render.call(this);

                if (this.model.id) {
                    var el = this.$(".submit");             
                }
            },

           
            
            onRemove: function() {
//                $('#conceptLink').hide();
//                $('#conceptMessage').hide();
//                $('#conceptBox').show();
//                this.modelForm.fields.concept.editor.value = '';
            }
            
            
            
           
            
        });

        return openhmis;
    }
);
