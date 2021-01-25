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
		{label: "${ ui.message("openhmis.inventory.admin.dispenseSummarys")}",}
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

</script>

<div id="entities-body">
    <br/>
    <div id="manage-entities-header">
        <span class="h1-substitue-left" style="float:left;">
            ${ui.message('openhmis.inventory.admin.dispenseSummarys')}
        </span>
    </div>
    <br/><br/><br/>
    <div>
        <div id="entities">
            <form style="" class="search">
                <fieldset class="search">
                    <table class="search" >
                        <tr>
                            <td>
                            ${ ui.message('openhmis.inventory.summary.startDate') }:
                            <ul class="table-layout">
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
                        </tr>                  
                        <tr>
                            <td>
                            ${ ui.message('openhmis.inventory.summary.endDate') }:
                            <ul class="table-layout">
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
                        </tr>                       
                        <tr>
                            <td>
                                <span> 
                                    <fieldset class="format">
                                        <span>            
                                            <button type="button" data-action="load" ng-click="searchDispenseSummarys(currentPage)">${ ui.message('openhmis.inventory.search') }</button>
                                        </span>
                                    </fieldset>
                                </span>
                            </td>
                        </tr>                  
                    </table>
                </fieldset>   
             </form>          

            <br/><br/>
            <table style="margin-bottom:5px;margin-bottom:5px;" class="manage-entities-table manage-stockOperations-table">
                <thead>
                    <tr>
                        <th>${ui.message('openhmis.inventory.patient.id')}</th>
                        <th>${ui.message('openhmis.inventory.patient.category')}</th>
                        <th>${ui.message('openhmis.inventory.treatment.type')}</th>
                        <th>${ui.message('openhmis.inventory.visit.type')}</th>
                        <th>${ui.message('openhmis.inventory.pickupreason')}</th>
                        <th>${ui.message('openhmis.inventory.dateofdispense')}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="clickable-tr" dir-paginate="entity in fetchedEntities | itemsPerPage: limit"
                    total-items="totalNumOfResults" current-page="currentPage" onClick="viewARVDispensedItem(entity.items)">
                        <td ng-style="strikeThrough(entity.retired)">{{entity.patientID}}</td>
                        <td ng-style="strikeThrough(entity.retired)">{{entity.patientCategory}}</td>
                        <td ng-style="strikeThrough(entity.retired)">
                            {{entity.treatmentType}}
                        </td>
                        <td ng-style="strikeThrough(entity.retired)">
                            {{entity.visitType}}
                        </td>
                        <td ng-style="strikeThrough(entity.retired)">
                            {{entity.pickupReason}}
                        </td>
                        <td ng-style="strikeThrough(entity.retired)">
                            {{entity.dateOfDispensed}}
                        </td>
                      
                    </tr>
                </tbody>
            </table>

            <div ng-show="fetchedEntities.length == 0">
                <br/>
        <!--	${ui.message('openhmis.commons.general.preSearchMessage')} - <b> {{searchField}} </b> - {{postSearchMessage}} -->
                <br/><br/>
                <span><input type="checkbox" ng-checked="includeRetired" ng-model="includeRetired"
                    ng-change="searchDispenseSummarys(currentPage)"></span>
                <span>${ui.message('openhmis.commons.general.includeRetired')}</span>
            </div>
            ${ui.includeFragment("openhmis.commons", "paginationFragment", [onPageChange: "searchDispenseSummarys(currentPage)", onChange: "searchDispenseSummarys(currentPage)"])}
        </div>
    </div>
</div>

<!-- View Dispense Samples Modal -->
<div class="modal fade" id="mySampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl" role="document">
        <div class="modal-content">
            <div class="modal-header" style="background-color: #00463f;">
            <h5 class="modal-title" id="exampleModalLabel" style="color: white;"><i class="icon-folder-open"></i> ARV Pharmacy Dispense Items</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            </div>
            <div class="modal-body" id="sample-details">
            </div>
            <div class="modal-footer">
            <button type="button" class="btn btn-secondary" style="color: black" data-dismiss="modal">Ok</button>
            </div>
        </div>
        </div>
</div>

<script type="application/javascript">
    var jq = jQuery;

    function viewARVDispensedItem(items) {
        var conts = "";
        conts += "<table>";
        conts += "<tr><th>S/N</th><th>Item Name</th><th>Quantity Prescribed</th><th>Quantity Dispensed</th><th>Duration</th>";
        for (i = 0; i < items.length; i++) {
            var item = items[i];
            conts += "<tr>";
            conts += "<td>" + counter + "</td>";
            conts += "<td>" + item.itemName + "</td>";
            conts += "<td>" + item.quantityPrescribed + "</td>";
            conts += "<td>" + item.quantityDispensed + "</td>";
            conts += "<td>" + item.duration + "</td>";
            conts += "</tr>";
            counter++;
        }        
        conts += "</table>";
        var element = document.getElementById("sample-details");
        element.innerHTML = conts;
        jq('#mySampleModal').modal('show');
    }

</script>