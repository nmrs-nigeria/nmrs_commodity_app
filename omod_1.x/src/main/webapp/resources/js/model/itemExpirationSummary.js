
define(
    [
        openhmis.url.backboneBase + 'js/openhmis',
        openhmis.url.backboneBase + 'js/lib/i18n',
        openhmis.url.backboneBase + 'js/model/generic',
        openhmis.url.backboneBase + 'js/model/concept',
        openhmis.url.inventoryBase + 'js/model/stockroom',
    ],
    function(openhmis, __) {
     
        openhmis.ItemExpirationSummary = openhmis.GenericModel.extend({
            meta: {
            	name: __(openhmis.getMessage('openhmis.inventory.item.expiration.summary.name')),
                namePlural: __(openhmis.getMessage('openhmis.inventory.item.expiration.summary.namePlural')),
                openmrsType: 'metadata',
                restUrl: openhmis.url.inventoryModelBase + 'itemExpirationSummary'
            },

            schema: {
                quantity: { type: "BasicNumber" },
                expiration: { type: "Date", format: openhmis.dateFormat },
                item: { type: "Object", objRef: true },
            },

            parse: function(resp) {
				if (resp) {
					if (resp.item && _.isObject(resp.item)) {
						resp.item = new openhmis.Item(resp.item);
					}

					if (resp.expiration) {
						var date = new Date(resp.expiration);
						resp.expiration = openhmis.dateFormat(date);
					}

				}
				return resp;
			},
        });

        return openhmis;
    }
);