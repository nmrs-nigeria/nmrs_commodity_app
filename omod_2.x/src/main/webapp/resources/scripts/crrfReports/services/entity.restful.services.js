/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 *
 */

(function () {
    'use strict';

    angular.module('app.restfulServices').service('CrrfReportsRestfulService', CrrfReportsRestfulService);

    CrrfReportsRestfulService.$inject = ['EntityRestFactory'];

    function CrrfReportsRestfulService(EntityRestFactory) {
        var service;
        service = {
            getReport: getReport
        };
        return service;

        function getReport(reportId, startDate, endDate, crrfCategory, successCallback) {
            var requestParams = [];
            requestParams['resource'] = INVENTORY_MODULE_CRRF_REPORTS_URL;
            requestParams['reportId'] = reportId;
            requestParams['startDate'] = startDate;
            requestParams['endDate'] = endDate;
            requestParams['crrfCategory'] = crrfCategory;
            

            EntityRestFactory.setCustomBaseUrl(ROOT_URL);
            EntityRestFactory.loadResults(requestParams, successCallback, function (error) {
                console.log(error)
            });
        }

       









    }
})();
