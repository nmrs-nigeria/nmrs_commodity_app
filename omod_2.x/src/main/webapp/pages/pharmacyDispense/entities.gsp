<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("openhmis.inventory.admin.create.pharmacy.dispense") ])

    /* load stylesheets */
    ui.includeCss("openhmis.commons", "bootstrap.css")
    ui.includeCss("openhmis.commons", "entities2x.css")
    ui.includeCss("uicommons", "ngDialog/ngDialog.min.css")
    ui.includeCss("openhmis.inventory", "entity.css")

    /* load angular libraries */
    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-ui/angular-ui-router.min.js")
    ui.includeJavascript("uicommons", "angular-ui/ui-bootstrap-tpls-0.11.2.min.js")
    ui.includeJavascript("uicommons", "angular-common.js")
    ui.includeJavascript("uicommons", "ngDialog/ngDialog.js")
    ui.includeJavascript("uicommons", "datetimepicker/bootstrap-datetimepicker.min.js")
    ui.includeCss("uicommons", "datetimepicker.css")

    /* load re-usables/common modules */
    ui.includeFragment("openhmis.commons", "loadReusableModules")

    /* load stockroom modules */
    ui.includeJavascript("openhmis.inventory", "pharmacyDispense/models/entity.model.js")
    ui.includeJavascript("openhmis.inventory", "pharmacyDispense/models/line.item.model.js")
    ui.includeJavascript("openhmis.inventory", "pharmacyDispense/services/entity.restful.services.js")
    ui.includeJavascript("openhmis.inventory", "pharmacyDispense/controllers/entity.controller.js")
    ui.includeJavascript("openhmis.inventory", "pharmacyDispense/services/entity.functions.js")
    ui.includeJavascript("openhmis.inventory", "constants.js")
    ui.includeJavascript("openhmis.inventory", "pharmacyDispense/configs/lga.js")
%>

<script data-main="pharmacyDispense/configs/entity.main" src="/${ ui.contextPath() }/moduleResources/uicommons/scripts/require/require.js"></script>

<div id="createOperationApp">
    <div ui-view></div>
</div>
