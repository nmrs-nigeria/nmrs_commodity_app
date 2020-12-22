<script type="text/javascript">
    var breadcrumbs = [
    {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
    {
			label: "${ ui.message("openhmis.inventory.page")}",
			link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
    },
   {
            label: "${ ui.message("openhmis.inventory.admin.task.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/pharmacyInventoryDashboard.page'
        },
		{label: "${ ui.message("openhmis.inventory.admin.phamacyConsumptions")}",}
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

</script>

<div id="entities-body">
    <br/>

    <div id="manage-entities-header">
        <span class="h1-substitue-left" style="float:left;">
            ${ui.message('openhmis.inventory.admin.consumptions')}
        </span>
        <span style="float:right;">
            <a class="button confirm" ui-sref="new">
                <i class="icon-plus"></i>
                {{newEntityLabel}}
            </a>
        </span>
    </div>
    <br/><br/><br/>

    <div>
        <div id="entities">

            <table class="search-area" >
<!--                <td>
                    ${ ui.message('openhmis.inventory.department.name') }:
                    <ul>
                        <li>
                            <select ng-model="department" ng-change="searchConsumptions(currentPage)" style="height:33px;"
                            ng-options='department.name for department in departments track by department.uuid'>
                        <option value="" selected="selected">Any</option>
                        </select>
                        </li>

                    </ul>
                </td>-->

                <td>
                    ${ ui.message('openhmis.inventory.item.name') }:
                    <ul>
                        <li>
                            <select ng-model="item" ng-change="searchConsumptions(currentPage)" style="height:33px;"
                            ng-options='item.name for item in items track by item.uuid'>
                        <option value="" selected="selected">Any</option>
                        </select>
                        </li>


                    </ul>
                </td>
            </table>




            <br/><br/>
            <table style="margin-bottom:5px;margin-bottom:5px;" class="manage-entities-table">
                <thead>
                    <tr>
                        <th>${ui.message('openhmis.inventory.item.name')}</th>
                         <th>${ui.message('openhmis.inventory.consumption.batchNumber')}</th>
<!--                        <th>${ui.message('openhmis.inventory.department.name')}</th>-->
                        <th>${ui.message('openhmis.inventory.consumption.wastage')}</th>
                        <th>${ui.message('openhmis.inventory.consumption.quantity')}</th>
                        <th>${ui.message('openhmis.inventory.consumption.consumptionDate')}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="clickable-tr" dir-paginate="entity in fetchedEntities | itemsPerPage: limit"
                    total-items="totalNumOfResults" current-page="currentPage" ui-sref="edit({uuid: entity.uuid})">
                <td ng-style="strikeThrough(entity.retired)">{{entity.item.name}}</td>
                <td ng-style="strikeThrough(entity.retired)">{{entity.batchNumber}}</td>
<!--                <td ng-style="strikeThrough(entity.retired)">{{entity.department.name}}</td>-->
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.wastage}}
                </td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.quantity}}
                </td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.consumptionDate | date: 'dd-MM-yyyy, h:mma' }}
                </td>
                </tr>
                </tbody>
            </table>

            <div ng-show="fetchedEntities.length == 0">
                <br/>
        <!--	${ui.message('openhmis.commons.general.preSearchMessage')} - <b> {{searchField}} </b> - {{postSearchMessage}} -->
                <br/><br/>
                <span><input type="checkbox" ng-checked="includeRetired" ng-model="includeRetired"
                    ng-change="searchConsumptions(currentPage)"></span>
                <span>${ui.message('openhmis.commons.general.includeRetired')}</span>
            </div>
            ${ui.includeFragment("openhmis.commons", "paginationFragment", [onPageChange: "searchConsumptions(currentPage)", onChange: "searchConsumptions(currentPage)"])}
        </div>
    </div>
</div>
