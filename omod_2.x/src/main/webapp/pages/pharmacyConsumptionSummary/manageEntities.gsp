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
		{label: "${ ui.message("openhmis.inventory.admin.pharmacyConsumptionSummarys")}",}
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

</script>

<div ng-show="loading" class="loading-msg">
    <span>${ui.message("openhmis.inventory.admin.create.processing")}</span>
    <br />
    <span class="loading-img">
        <img src="${ ui.resourceLink("uicommons", "images/spinner.gif") }"/>
    </span>
</div>

<div id="entities-body">
    <br/>

    <br/><br/><br/>

    <div>
        <div id="entities">

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
                        <li><a class="btn btn-grey" ng-click="searchConsumptionSummarys(currentPage)">Search</a></li>
                    </ul>
                </fieldset>
            </form>



            <br/><br/>
            <table style="margin-bottom:5px;margin-bottom:5px;" class="manage-entities-table manage-stockOperations-table">
                <thead>
                    <tr>
                        <th>${ui.message('openhmis.inventory.item.name')}</th>
                        <th>${ui.message('openhmis.inventory.summary.quantityConsumed')}</th>
                        <th>Drug Category</th>

                    </tr>
                </thead>
                <tbody>
                    <tr  dir-paginate="entity in fetchedEntities | itemsPerPage: limit"
                    total-items="totalNumOfResults" current-page="currentPage" >
                <td ng-style="strikeThrough(entity.retired)">{{entity.item}}</td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.totalQuantityReceived}}
                </td>
                <td ng-style="strikeThrough(entity.retired)">
                    {{entity.drugCategory}}
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
