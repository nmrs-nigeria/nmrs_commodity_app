
curl(
	{ baseUrl: openhmis.url.resources },
	[
		openhmis.url.backboneBase + 'js/lib/jquery',
		openhmis.url.backboneBase + 'js/openhmis',
		openhmis.url.backboneBase + 'js/lib/backbone-forms',
		openhmis.url.inventoryBase + 'js/model/stockroom',
		openhmis.url.inventoryBase + 'js/model/itemExpirationSummary',
		openhmis.url.backboneBase + 'js/view/generic',

		openhmis.url.backboneBase + 'js/view/list',
		openhmis.url.backboneBase + 'js/view/editors',
		openhmis.url.inventoryBase + 'js/view/editors',
		openhmis.url.inventoryBase + 'js/view/itemExpirationSummary',
	],
	function($, openhmis) {
		$(function() {
			openhmis.startAddEditScreen(openhmis.ItemStockSummary, {
				listView: openhmis.InventoryStockTakeSearchableListView,
                listElement: $("#stockTakeList"),
                addEditViewType: openhmis.InventoryStockTakeAddEditView,
                searchView: openhmis.StockroomStockTakeSearchView,
                itemView: openhmis.InventoryStockTakeListItemView,
				listFields: ['item', 'expiration', 'quantity']
			});
		});


	}
);
