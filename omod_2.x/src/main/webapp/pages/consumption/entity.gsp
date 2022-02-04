<script type="text/javascript">
    var breadcrumbs = [
    { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
    { label: "${ ui.message("openhmis.inventory.page")}" , link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'},
     {
            label: "${ ui.message("openhmis.inventory.admin.task.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/inventoryTasksDashboard.page'
        },
    { label: "${ ui.message("openhmis.inventory.admin.consumptions")}", link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/consumption/entities.page##/'},
    { label: "${ui.message("openhmis.inventory.consumption.name")}"}
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

</script>

<div ng-show="loading" class="loading-msg">
    <span>${ui.message("openhmis.commons.general.processingPage")}</span>
    <br />
    <span class="loading-img">
        <img src="${ ui.resourceLink("uicommons", "images/spinner.gif") }"/>
    </span>
</div>

<form ng-hide="loading" name="consumptionForm" class="entity-form" ng-class="{'submitted': submitted}" style="font-size:inherit">
    ${ ui.includeFragment("openhmis.commons", "editEntityHeaderFragment")}

    <input type="hidden" ng-model="entity.uuid" />

    <fieldset class="format">
        
         <ul class="table-layout">
            <li class="required">
                <span> {{messageLabels['openhmis.inventory.consumption.consumptionDate']}}</span>
            </li>
            <li>
                <span class="date">
              ${ ui.includeFragment("uicommons", "field/datetimepicker", [
                                    formFieldName: "consumptionDate",
                                    id: "consumptionDate",
                                    label: "",
                                    useTime: false,
                                   
                            ])}
             </span>
                </li>
           
        </ul>

        
        <ul class="table-layout">
            <li class="required">
                <span>Testing Points</span>
            </li>
            <li>
                <select ng-model="department" required
                ng-options='department.name for department in departments track by department.uuid'>
                </select>
            </li>
        </ul>
        <ul class="table-layout">
            <li class="required">
                <span>{{messageLabels['openhmis.inventory.item.name']}}</span>
            </li>
            <li>
                <select ng-model="item" required
                ng-options='item.name for item in items track by item.uuid'
                ng-change="changeItemBatch(item.uuid)" >
                </select>
            </li>
        </ul>
         <ul class="table-layout">
            <li class="required">
                <span>{{messageLabels['openhmis.inventory.consumption.batchNumber']}}</span>
            </li>
            <li>
                <select ng-model="entity.batchNumber" required
                ng-options='batchNumber for batchNumber in itemBatchs' >
                </select>
            </li>
        </ul>
        <ul class="table-layout">
            <li class="required">
                <span>{{messageLabels['openhmis.inventory.consumption.quantity']}}</span>
            </li>
            <li>
                <input type="number" ng-model="entity.quantity" class="minimized"/>
            </li>
        </ul>
        <ul class="table-layout">
            <li class="not-required">
                <span>{{messageLabels['openhmis.inventory.consumption.wastage']}}</span>
            </li>
            <li>
               <input type="number" ng-model="entity.wastage" class="minimized"/>
            </li>
        </ul>
          <ul class="table-layout">
            <li class="not-required">
                <span>Total Qty Used.</span>
            </li>
            <li>
               {{entity.wastage + entity.quantity}}
            </li>
        </ul>
          <ul class="table-layout">
            <li class="required">
                <span>{{messageLabels['openhmis.inventory.consumption.testPurpose']}}</span>
            </li>
            <li>
                 <select ng-model="entity.testPurpose" class="form-control"
                            ng-options="testPurpose for testPurpose in testPurposeTypes">
                    </select>
            </li>
        </ul>

    </fieldset>

    <br />

    <fieldset class="format">
        <span>
            <input type="button" class="cancel" value="{{messageLabels['general.cancel']}}" ng-click="cancel()" />
            <input type="button" class="confirm right" value="{{messageLabels['general.save']}}" ng-click="saveOrUpdate()" />
        </span>
    </fieldset>



</form>    

${ ui.includeFragment("openhmis.commons", "retireUnretireDeleteFragment", [retireUnretireCall : "retireUnretire()"]) }
