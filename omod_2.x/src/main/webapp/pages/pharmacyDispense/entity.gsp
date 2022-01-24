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
    {
            label: "${ ui.message("openhmis.inventory.admin.create.pharmacy.dispense")}",
    },
    ];
    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
    jQuery(".tabs").tabs();
    jQuery(function() {
    jQuery('body').on('focus', ".date", function(){
    jQuery(this).datetimepicker({
    minView : 2,
    autoclose : true,
    pickerPosition : "bottom-left",
    todayHighlight : false,
    format: "dd M yyyy",
    startDate : new Date(),
    });
    });
    });
</script>

<div ng-show="loading" class="loading-msg">
    <span>${ui.message("openhmis.inventory.admin.create.processing")}</span>
    <br />
    <span class="loading-img">
        <img src="${ ui.resourceLink("uicommons", "images/spinner.gif") }"/>
    </span>
</div>

<div ng-hide="loading">
    <h1>${ui.message('openhmis.inventory.admin.create.pharmacy.dispensing')}</h1>
    <form name="entityForm" class="entity-form create-operation-content" ng-class="{'submitted': submitted}">
        <fieldset class="content pharmacyOperation">
           
            ${ui.includeFragment("openhmis.commons", "fieldTypesFragment")}


            ${ui.includeFragment("openhmis.commons", "patientSearchFragment", [
showPatientDetails: "selectedPatient !== ''",
showPatientSearchBox: "selectedPatient === ''"
])}
         

<span>            
    <button type="button" class="btn success-button" id="{{selectedPatient.uuid}}_{{visit.uuid}}" onclick="dispenseDrug(this.id)">${ ui.message('openhmis.inventory.admin.create.pharmacy.dispensing') }</button>
</span>

        </fieldset>
        <br/>
    
    </form>

<script type="application/javascript">
    var jq = jQuery;
    function dispenseDrug(id) {
    	var res = id.split("_");
    	var patientId = res[0];
        var visitId = res[1];           
        if(visitId == "" || visitId == null || visitId == undefined){
            visitId = 0;
        }

        if (patientId == "" || patientId == null || patientId == undefined) {
            console.log(patientId);
            emr.errorAlert("Please select a patient.");
            return false;
        }else{
            var formUuid = "4a238dc4-a76b-4c0f-a100-229d98fd5758";
            var returnUrl = "%2openmrs%2Fcoreapps%2Fclinicianfacing%2Fpatient.page%3FpatientId%3D2af91cc0-6c62-4ee1-adb4-ff3e6e75b9be%26";
        
            var urlToForm = '${ui.pageLink("htmlformentryui","htmlform/enterHtmlFormWithStandardUi",[patientId: "patientIdElement", visitId: "visitIdElement", formUuid: "formUuidElement", returnUrl: "returnUrlElement"])}'.replace("patientIdElement", patientId).replace("visitIdElement", visitId).replace("formUuidElement", formUuid).replace("returnUrlElement", returnUrl);
            location.href = 'urlToForm'.replace("urlToForm", urlToForm);   
        }
    }
</script>

</div>


