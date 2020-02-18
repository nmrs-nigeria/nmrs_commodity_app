/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
curl(
	{ baseUrl: openhmis.url.resources },
	[
		openhmis.url.backboneBase + 'js/lib/jquery',
		openhmis.url.backboneBase + 'js/openhmis',
		openhmis.url.backboneBase + 'js/lib/backbone-forms',
		openhmis.url.inventoryBase + 'js/model/item',
		openhmis.url.inventoryBase + 'js/model/department',
                openhmis.url.inventoryBase + 'js/model/consumption',
		openhmis.url.backboneBase + 'js/view/generic',
		openhmis.url.backboneBase + 'js/view/list',
		openhmis.url.backboneBase + 'js/view/editors',
		openhmis.url.inventoryBase + 'js/view/editors',
		openhmis.url.backboneBase + 'js/view/search',
		openhmis.url.inventoryBase + 'js/view/search',
		openhmis.url.inventoryBase + 'js/view/consumption'
	],
	function($, openhmis) {
		$(function() {
			openhmis.startAddEditScreen(openhmis.Consumption, {
				listView: openhmis.GenericSearchableListView,
				searchView: openhmis.DepartmentAndNameSearchView,
				addEditViewType: openhmis.ConsumptionAddEditView,
				listFields: ['item', 'department', 'consumptionDate', 'quantity','wastage']
			});
		});
	}
);


