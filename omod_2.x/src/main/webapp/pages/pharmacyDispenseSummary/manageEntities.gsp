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
                        <th>Treatment Age</th>
                        <th>Regimen Line</th>                       
                        <th>${ui.message('openhmis.inventory.pickupreason')}</th>
                        <th>${ui.message('openhmis.inventory.dateofdispense')}</th>
                        <th>Dosage</th>
                        <th>Duration</th>
                        <th>Quantity Dispensed</th>
                    </tr>
                </thead>
                <tbody>                    
                    <tr class="clickable-tr" dir-paginate="entity in fetchedEntities | itemsPerPage: limit"
                    total-items="totalNumOfResults" current-page="currentPage" id="{{entity.patientDBId}}_{{entity.encounterId}}" onclick="viewARVDispensedItem(this.id)" >  
                        <td ng-style="strikeThrough(entity.retired)">
                            {{entity.patientID}}
                        </td>         
                         <td ng-style="strikeThrough(entity.retired)">
                            {{entity.treatmentAge}}
                        </td>   
                         <td ng-style="strikeThrough(entity.retired)">
                            {{entity.currentRegimen}}
                        </td>                 
                       
                        <td ng-style="strikeThrough(entity.retired)">
                            {{entity.pickupReason}}
                        </td>
                        <td ng-style="strikeThrough(entity.retired)">
                            {{entity.dateOfDrugDispensed}}
                        </td>          
                        
                        <td ng-style="strikeThrough(entity.retired)">
                            {{entity.items[0].singleDose}}  {{entity.items[0].frequency}}
                        </td>
                        <td ng-style="strikeThrough(entity.retired)">
                            {{entity.items[0].duration}}
                        </td>                        
                        <td ng-style="strikeThrough(entity.retired)">
                           {{entity.items[0].quantityDispensed}}
                        </td>
                        
                    </tr>
                    
                </tbody>
            </table>

            <div class="not-found" ng-show="fetchedEntities.length == 0 && searchField == ''">
                ${ui.message('openhmis.inventory.operations.noDispenseFound')}
            </div>
        
            <div ng-show="fetchedEntities.length == 0 && searchField != ''">
                <br/>
                ${ui.message('openhmis.commons.general.preSearchMessage')} - <b> {{searchField}} </b> - {{postSearchMessage}}
                <br/><br/>
                <span><input type="checkbox" ng-checked="includeRetired" ng-model="includeRetired"
                             ng-change="searchDispenseSummarys(currentPage)"></span>
                <span>${ui.message('openhmis.commons.general.includeRetired')}</span>
            </div>
            ${ui.includeFragment("openhmis.commons", "paginationFragment", [
                    showRetiredSection  : "false",
                    onPageChange : "searchDispenseSummarys(currentPage)",
                    onChange : "searchDispenseSummarys(currentPage)"
            ])}
        </div>
    </div>
</div>

<div id="arvItemsDialog" class="dialog ng-scope" style="display: none; width: 800px;">
    <div class="dialog-header">
        <span>
            <i class="icon-list"></i>
            <h3>ARV Pharmacy Dispensed Items</h3>
        </span>
        <i class="icon-remove cancel show-cursor" style="float:right;" ng-click="closeThisDialog()"></i>
    </div>
    <div class="dialog-content form" id="item-dispense-details">
    </div>
</div>

<script type="application/javascript">
    var jq = jQuery;
    function viewARVDispensedItem(id) {
    	var res = id.split("_");
    	var pid = res[0];
    	var encid = res[1];    	
    	var urlToForm = '${ui.pageLink("htmlformentryui","htmlform/editHtmlFormWithStandardUi",[patientId: "patientIdElement", encounterId: "encouterIdElement"])}'.replace("patientIdElement", pid).replace("encouterIdElement", encid);
    	location.href = 'urlToForm'.replace("urlToForm", urlToForm);        
    }
</script>