<script type="text/javascript" xmlns="http://www.w3.org/1999/html">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        {
            label: "${ ui.message("openhmis.inventory.page")}" ,
            link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
        },
        {
            label: "${ ui.message("openhmis.inventory.admin.task.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/pharmacyInventoryDashboard.page'
        },
        {
            label: "${ ui.message("openhmis.inventory.admin.pharmacyReports")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/ndrExtraction/entities.page#/'
        }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
</script>

<div id="reportPage">

<h2>{{ ui.message("openhmis.inventory.admin.pharmacyReports")}}</h2>

<hr>

    <div ng-show="loading" class="loading-msg">
    <span>${ui.message("openhmis.commons.general.processingPage")}</span>
    <br />
    <span class="loading-img">
        <img src="${ ui.resourceLink("uicommons", "images/spinner.gif") }"/>
    </span>
</div>
    
<div class="report">


    <fieldset>
        <legend>
            <i class="icon-list-alt"></i>
            <span>Dispensary Consumption Report</span>
        </legend>
        <small>select date range</small>
         <ul class="table-layout">
            <li><label>Start Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "startDate",
                        id: "startDate",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>

        <ul class="table-layout">
            <li><label>End Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "endDate",
                        id: "endDate",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>

        <ul class="table-layout">
            <li></li>
            <li><a class="btn btn-grey" ng-click="generateReport_DispensaryConsumption()">Generate Report</a></li>
        </ul>
    </fieldset>
    
        <fieldset>
        <legend>
            <i class="icon-list-alt"></i>
            <span>Dispensary Consumption Report By Modalities</span>
        </legend>
        <small>select date range</small>
         <ul class="table-layout">
            <li><label>Start Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "startDate_amod",
                        id: "startDate_amod",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>

        <ul class="table-layout">
            <li><label>End Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "endDate_amod",
                        id: "endDate_amod",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>
        
        
         <ul class="table-layout">
          <li> <span>Treatment Category</span></li>

            <li>
                  <select ng-model="category" class="form-control"
                    ng-options="category for category in categories">
                    </select>
            </li>
            </ul>

        <ul class="table-layout">
            <li></li>
            <li><a class="btn btn-grey" ng-click="generateReport_DispensaryModalitiesConsumption()">Generate Report</a></li>
        </ul>
    </fieldset>
    
    <br><br>
    <fieldset>
        <legend>
            <i class="icon-list-alt"></i>
            <span>Stockroom Consumption Report</span>
        </legend>
        <small>select date range</small>
         <ul class="table-layout">
            <li><label>Start Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "startDate_stc",
                        id: "startDate_stc",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>

        <ul class="table-layout">
            <li><label>End Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "endDate_stc",
                        id: "endDate_stc",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>

        <ul class="table-layout">
            <li></li>
            <li><a class="btn btn-grey" ng-click="generateReport_StockroomConsumption()">Generate Report</a></li>
        </ul>
    </fieldset>

     <br><br>
     <fieldset>
        <legend>
            <i class="icon-list-alt"></i>
            <span>Dispensary Stock on Hand Report</span>
        </legend>
        <small>select date range</small>
         <ul class="table-layout">
            <li><label>Start Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "stockonhandDispensary_startDate",
                        id: "stockonhandDispensary_startDate",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>

        <ul class="table-layout">
            <li><label>End Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "stockonhandDispensary_endDate",
                        id: "stockonhandDispensary_endDate",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>

        <ul class="table-layout">
            <li></li>
            <li><a class="btn btn-grey" ng-click="generateReport_DispensaryStockOnHand()">Generate Report</a></li>
        </ul>
    </fieldset>

     <br><br>
     <fieldset>
        <legend>
            <i class="icon-list-alt"></i>
            <span>Stockroom Stock on Hand Report</span>
        </legend>
        <small>select date range</small>
         <ul class="table-layout">
            <li><label>Start Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "stockonhandStockroom_startDate",
                        id: "stockonhandStockroom_startDate",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>

        <ul class="table-layout">
            <li><label>End Date</label></li>
            <li>
                ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                        formFieldName: "stockonhandStockroom_endDate",
                        id: "stockonhandStockroom_endDate",
                        label: "",
                        useTime: false
                ])}
            </li>
        </ul>

        <ul class="table-layout">
            <li></li>
            <li><a class="btn btn-grey" ng-click="generateReport_StockroomStockOnHand()">Generate Report</a></li>
        </ul>
    </fieldset>
    
</div>
</div>
