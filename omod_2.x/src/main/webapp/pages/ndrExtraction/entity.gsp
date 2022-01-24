<script type="text/javascript" xmlns="http://www.w3.org/1999/html">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        {
            label: "${ ui.message("openhmis.inventory.page")}" ,
            link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
        },
        {
            label: "${ ui.message("openhmis.inventory.admin.task.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/inventoryTasksDashboard.page'
        },
        {
            label: "${ ui.message("openhmis.inventory.admin.ndrextraction")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/ndrExtraction/entities.page#/'
        }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
</script>

<div id="reportPage">

<h2>{{ ui.message("openhmis.inventory.admin.ndrextraction")}}</h2>

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
            <span>Commodity Report</span>
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
            <li><a class="" style="background-color: #666; color: #fff; cursor: pointer;" ng-click="generateReport_ExpiringStock()">Generate Report</a></li>
        </ul>
    </fieldset>
</div>
</div>
