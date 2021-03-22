<script type="text/javascript">
	var breadcrumbs = [
		{
			icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'
		},
		{
			label: "${ ui.message("openhmis.inventory.page")}",
			link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
		},
		{
			label: "${ ui.message("openhmis.inventory.admin.task.dashboard")}",
			link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/pharmacyInventoryDashboard.page'
		},
		{
			label: "${ui.message("openhmis.inventory.admin.stockTake.pharmacy")}"
		}
	];

	jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
</script>

	<h1>${ui.message("openhmis.inventory.admin.operations.pharmacy.stock.mgt")}</h1>

	<a style="width: 180px; line-height: 1.3em;" href="../pharmacyStockTakeStockroom/entities.page"  class="button app big" style="font-size:12px;min-height: 10px;">
	    <i class="icon-list"></i>
	    <br/>
	    <p style="font-size: 17px;">${ui.message('openhmis.inventory.admin.operations.pharmacy.stock.mgt.stockroom')}</p>
	</a>
	
	<a style="width: 180px; line-height: 1.3em;" href="../pharmacyStockTakeDispensary/entities.page"  class="button app big" style="font-size:12px;min-height: 10px;">
	    <i class="icon-list"></i>
	    <br/>
	    <p style="font-size: 17px;">${ui.message('openhmis.inventory.admin.operations.pharmacy.stock.mgt.dispensary')}</p>
	</a>


