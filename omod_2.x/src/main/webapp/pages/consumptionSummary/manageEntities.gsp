<script type="text/javascript">
    var breadcrumbs = [
    {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
    {
			label: "${ ui.message("openhmis.inventory.page")}",
			link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
    },
    {
			label: "${ ui.message("openhmis.inventory.manage.module")}",
    link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/manageModule.page'
    },
		{label: "${ ui.message("openhmis.inventory.admin.consumptionSummarys")}",}
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

</script>

<div id="entities-body">
    <br/>

    <div id="manage-entities-header">
        <span class="h1-substitue-left" style="float:left;">
            ${ui.message('openhmis.inventory.admin.consumptionSummarys')}
        </span>
      
    </div>
    <br/><br/><br/>

    <div>
        <div id="entities">

            <table class="search-area" >
                <td>
                    ${ ui.message('openhmis.inventory.department.name') }:
                    <ul>
                        <li>
                            <select ng-model="department" ng-change="searchConsumptionSummarys(currentPage)" style="height:33px;"
                            ng-options='department.name for department in departments track by department.uuid'>
                        <option value="" selected="selected">Any</option>
                        </select>
                        </li>

                    </ul>
                </td>

                <td>
                    ${ ui.message('openhmis.inventory.item.name') }:
                    <ul>
                        <li>
                            <select ng-model="item" ng-change="searchConsumptionSummarys(currentPage)" style="height:33px;"
                            ng-options='item.name for item in items track by item.uuid'>
                        <option value="" selected="selected">Any</option>
                        </select>
                        </li>


                    </ul>
                </td>
                   <td>
                    ${ ui.message('openhmis.inventory.summary.startDate') }:
                    <ul>
                        <li>
                            <span class="date">
              ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                                    formFieldName: "startDate",
                                    id: "startDate",
                                    label: "",
                                    useTime: false
                                    
                            ])}
             </span>
                        </li>


                    </ul>
                </td>
                <td>
                    ${ ui.message('openhmis.inventory.summary.endDate') }:
                    <ul>
                        <li>
                            <span class="date">
              ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                                    formFieldName: "endDate",
                                    id: "endDate",
                                    label: "",
                                    useTime: false
                                    
                            ])}
             </span>
                        </li>


                    </ul>
                </td>
            </table>




            <br/><br/>
            <table style="margin-bottom:5px;margin-bottom:5px;" class="manage-entities-table">
                <thead>
                    <tr>
                        <th>${ui.message('openhmis.inventory.item.name')}</th>
                        <th>${ui.message('openhmis.inventory.department.name')}</th>
                        <th>${ui.message('openhmis.inventory.summary.quantityConsumed')}</th>
                        <th>${ui.message('openhmis.inventory.summary.quantityReceived')}</th>
                    
                    </tr>
                </thead>
                <tbody>
                    <tr class="clickable-tr" dir-paginate="entity in fetchedEntities | itemsPerPage: limit"
                    total-items="totalNumOfResults" current-page="currentPage" ui-sref="edit({uuid: entity.uuid})">
                <td ng-style="strikeThrough(entity.retired)">{{entity.item.name}}</td>
                <td ng-style="strikeThrough(entity.retired)">{{entity.department.name}}</td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.totalQuantityConsumed}}
                </td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.totalQuantityReceived}}
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
