<script type="text/javascript">
    var breadcrumbs = [
    {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
    {
			label: "${ ui.message("openhmis.inventory.page")}",
			link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
    },
   {
            label: "${ ui.message("openhmis.inventory.admin.task.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/inventoryTasksDashboard.page'
        },
		{label: "${ ui.message("openhmis.inventory.admin.consumptionSummarys")}",}
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

</script>

<div id="entities-body">
    <br/>


    <br/><br/><br/>

    <div>
        <div id="entities">

            <div class="report">

                <form style="" class="search">
                <fieldset>
                    <legend>
                        <i class="icon-list-alt"></i>
                        <span>View Consumption Summary</span>
                    </legend>
                    <label>Select date range</label>
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
                        <li><label>Department</label></li>
                    <li>
                        <select ng-model="department" ng-change="searchConsumptionSummarys(currentPage)" style="height:33px;"
                                ng-options='department.name for department in departments track by department.uuid'>
                        </select>
                    </li>
                    </ul>
                </fieldset>
                </form>
            </div>


            <br/><br/>
            <table style="margin-bottom:5px;margin-bottom:5px;" class="manage-entities-table manage-stockOperations-table">
                <thead>
                    <tr>
                        <th>${ui.message('openhmis.inventory.item.name')}</th>
                        <th>${ui.message('openhmis.inventory.department.name')}</th>
                        <th>${ui.message('openhmis.inventory.summary.quantityReceived')}</th>
                        <th>${ui.message('openhmis.inventory.summary.quantityConsumed')}</th>
                        <th>${ui.message('openhmis.inventory.consumption.wastage')}</th>
                        <th>${ui.message('openhmis.inventory.summary.stockBalance')}</th>


                    </tr>
                </thead>
                <tbody>
                    <tr  dir-paginate="entity in fetchedEntities | itemsPerPage: limit"
                    total-items="totalNumOfResults" current-page="currentPage" >
                <td ng-style="strikeThrough(entity.retired)">{{entity.item.name}}</td>
                <td ng-style="strikeThrough(entity.retired)">{{entity.department.name}}</td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.totalQuantityReceived}}
                </td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.totalQuantityConsumed}}
                </td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.totalQuantityWasted}}
                </td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.stockBalance}}
                </td>

                </tr>
                </tbody>
            </table>

            <div ng-show="fetchedEntities.length == 0">
                <br/>
        <!--	${ui.message('openhmis.commons.general.preSearchMessage')} - <b> {{searchField}} </b> - {{postSearchMessage}} -->
                <br/><br/>
                <span><input type="checkbox" ng-checked="includeRetired" ng-model="includeRetired"
                    ng-change="searchConsumptionSummarys(currentPage)"></span>
                <span>${ui.message('openhmis.commons.general.includeRetired')}</span>
            </div>
            ${ui.includeFragment("openhmis.commons", "paginationFragment", [onPageChange: "searchConsumptionSummarys(currentPage)", onChange: "searchConsumptionSummarys(currentPage)"])}
        </div>
    </div>
</div>
