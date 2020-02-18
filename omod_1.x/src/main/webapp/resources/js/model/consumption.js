/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(
	[
				openhmis.url.backboneBase + 'js/lib/underscore',
		openhmis.url.backboneBase + 'js/model/generic',
		openhmis.url.backboneBase + 'js/lib/i18n',
        openhmis.url.backboneBase + 'js/model/openhmis',
		openhmis.url.inventoryBase + 'js/model/department',
                openhmis.url.inventoryBase + 'js/model/item'
	],
	function(_,openhmis, __) {
		openhmis.Consumption = openhmis.GenericModel.extend({
			meta: {
				name: __(openhmis.getMessage('openhmis.inventory.consumption.name')),
				namePlural: __(openhmis.getMessage('openhmis.inventory.consumption.namePlural')),
				openmrsType: 'metadata',
				restUrl: openhmis.url.inventoryModelBase + 'consumption'
			},

			schema: {
				department: {
					type: 'DepartmentSelect',
					options: new openhmis.GenericCollection(null, {
						model: openhmis.Department,
						url: openhmis.url.inventoryModelBase + 'department'
					}),
					objRef: true
				},
                                item: {
					type: 'ItemSelect',
					options: new openhmis.GenericCollection(null, {
						model: openhmis.Item,
						url: openhmis.url.inventoryModelBase + 'item'
					}),
					objRef: true
				},
                                quantity: { type: "BasicNumber" },
                                consumptionDate: {
                    type: 'OperationDateEditor',
                    format: openhmis.dateTimeFormatLocale
                },
                wastage:{type : "BasicNumber"}
                
			}

		
		});

		return openhmis;
	}
);


