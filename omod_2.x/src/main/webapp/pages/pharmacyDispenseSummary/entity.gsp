<script type="text/javascript">
    var breadcrumbs = [
    {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
    {
			label: "${ ui.message("openhmis.inventory.page")}",
			link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
    },
   {
            label: "${ ui.message("openhmis.inventory.manage.pharmacy.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/pharmacyInventoryDashboard.page'
        },
		{label: "${ ui.message("openhmis.inventory.admin.dispenseSummarys")}",}
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

</script>

<table class="header-title">
    <tr>
        <td>
            <h1>${ui.message('openhmis.inventory.admin.dispenseSummarys')}</h1>
        </td>
    </tr>
</table>

<div class="detail-section-border-bottom">
    <ul class="table-layout">
        <li>
            <span>Fullname: </span>
        </li>
        <li>
            <span>Test</span>
        </li>
    </ul>
    <ul class="table-layout">
        <li>
            <span>Gender: </span>
        </li>
        <li>
            <span>Test</span>
        </li>
    </ul>
    <ul class="table-layout">
        <li>
            <span>Age: </span>
        </li>
        <li>
            <span>Test</span>
        </li>
    </ul>
     <ul class="table-layout">
        <li>
            <span>Regimen: </span>
        </li>
        <li>
            <span>Paderatic firstline : ABC</span>
        </li>
    </ul>
   
</div>

<br/>
<hr>
  
    <div id="items" style="border: 0px;">
        <table style="margin-bottom:5px; border:0px" class="manage-entities-table manage-stockOperations-item-table">
            <thead>
            <tr>
                <th>Drug</th>
                <th>Strength</th>
                <th>Duration</th>
                <th>Quantity Prescribed</th>
                <th>Quantity Dispensed</th>
            </tr>
            </thead>
            <tbody>
            <tr class="clickable-tr" pagination-id="__items"
                dir-paginate="item in fetchedData.items | itemsPerPage: stockOperationItemLimit"
                total-items="totalNumOfResults" current-page="stockOperationItemCurrentPage">
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            </tbody>
        </table>

        <div class="not-found" ng-show="stockOperationItems.length == 0">
            ${ui.message('No Drug found')}
        </div> 
    </div>

<hr/><br/>

<div class="detail-section-border-top">
    <br/>
    <p>
        <span>
            <input type="button" class="cancel" value="{{messageLabels['openhmis.commons.general.close']}}" ng-click="cancel()"/>
        </span>
    </p>
</div>


