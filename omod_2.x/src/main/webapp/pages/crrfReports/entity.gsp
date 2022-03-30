<script type="text/javascript" xmlns="http://www.w3.org/1999/html">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        {
            label: "${ ui.message("openhmis.inventory.page")}" ,
            link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
        },
        {
            label: "${ ui.message("openhmis.inventory.manage.pharmacy.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/pharmacyInventoryDashboard.page'
        },
        {
            label: "${ ui.message("openhmis.inventory.admin.crrfReports")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/ndrExtraction/entities.page#/'
        }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
</script>

<div id="reportPage">

<h2>{{ ui.message("openhmis.inventory.admin.crrfReports")}}</h2>

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
            <span>CRRF Report</span>
        </legend>
        
         <ul class="table-layout">
            <li><label>CRRF Type</label></li>
            <li>               
                <select ng-model="category" class="form-control"
                    	ng-options="category for category in categories">
                </select>
               
            </li>
        </ul>
        
        <ul class="table-layout">
            <li><label>Reporting Period</label></li>
            <li>               
                <select ng-model="reportingPeriod" class="form-control"
                    	ng-options="reportingPeriod for reportingPeriod in reportingPeriods">
                </select>
               
            </li>
        </ul>
        
        <ul class="table-layout">
            <li><label>Year</label></li>
            <li>               
                <select ng-model="year" class="form-control"
                    	ng-options="year for year in years">
                </select>
               
            </li>
        </ul>

        <ul class="table-layout">
            <li></li>
            <li><a class="btn btn-grey" ng-click="generateCRFFReport(currentPage)">Generate Report</a></li>
        </ul>
    </fieldset>
    

    
</div>
</div>
