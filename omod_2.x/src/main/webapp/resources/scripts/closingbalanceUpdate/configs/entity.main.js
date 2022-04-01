

/* initialize and bootstrap application */
requirejs(['crrfReports/configs/entity.module'], function() {
	angular.bootstrap(document, ['entitiesApp']);
});

emr.loadMessages([
	"openhmis.commons.general.new",
	"openhmis.commons.general.error.notFound",
	"openhmis.commons.general.created.success",
	"openhmis.commons.general.updated.success",
	"openhmis.commons.general.retired.success",
	"openhmis.commons.general.unretired.success",
	"openhmis.commons.general.confirm.delete",
	"openhmis.commons.general.deleted.success",
	"openhmis.commons.general.name.required",
	"openhmis.commons.general.retireReason.required",
	"openhmis.commons.general.unretire",
	"openhmis.commons.general.retire",
	"openhmis.commons.general.delete",
	"openhmis.commons.general.retired.reason",
	"general.edit",	"general.new", "general.name",
	"general.description", "general.cancel",
	"general.save", "general.retireReason",
	"general.purge", "general.retire",	
        "general.unretire",
	"openhmis.commons.general.error.entityName",
	"openhmis.commons.general.error.restName",
	"openhmis.commons.general.error.uuid",
	"openhmis.commons.general.error.retired",
	"openhmis.commons.general.error.retireReason",
	"openhmis.inventory.department.name",
	"openhmis.inventory.department.namePlural",
	"openhmis.inventory.item.name",
	"openhmis.inventory.item.namePlural",
	"openhmis.inventory.item.required.departmentError",
	
        "openhmis.inventory.consumption.consumptionDate",
        "openhmis.inventory.consumption.quantity",
        "openhmis.inventory.consumption.name",
        "openhmis.inventory.consumption.testingPoint",
        "openhmis.inventory.consumption.wastage",
        "openhmis.inventory.consumption.batchNumber",
        "openhmis.inventory.admin.consumptionSummarys",
        "openhmis.inventory.admin.consumptionSummary",
        "openhmis.inventory.summary.quantityConsumed",
        "openhmis.inventory.summary.quantityReceived",
        "openhmis.inventory.summary.startDate",
        "openhmis.inventory.summary.endDate",
        "openhmis.inventory.summary.stockBalance",
        "openhmis.inventory.summary.totalWastage",
        "openhmis.inventory.admin.pharmacyConsumptionSummarys",
        "openhmis.inventory.pharmacyConsumptionSummary.name",
        "openhmis.inventory.pharmacyDispenseSummary.name",
        "openhmis.commons.general.required.quantity",
        
	"openhmis.commons.general.add",
	"openhmis.commons.general.enterSearchPhrase",
	"openhmis.commons.general.edit",
	"openhmis.commons.general.postSearchMessage",
		"openhmis.inventory.search",

		"openhmis.inventory.patient.id",
		"openhmis.inventory.patient.category",
		"openhmis.inventory.treatment.type",
		"openhmis.inventory.visit.type",
		"openhmis.inventory.pickupreason",
		"openhmis.inventory.dateofdispense",
		"openhmis.inventory.pharmacyDispenseSummary.name",
		"openhmis.inventory.admin.dispenseSummarys",
		"openhmis.inventory.operations.noDispenseFound"

]);
