/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationDataService;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemStock;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockDetailBase;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationItem;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MORRISON.I
 */
@Controller(value = "itemUtility")
@RequestMapping(ModuleWebConstants.MODULE_ITEM_UTILITY_ROOT)
public class ItemUtilityController {

	private static final Log LOG = LogFactory.getLog(ItemUtilityController.class);

	private IItemStockDataService itemStockDataService;
	private IItemDataService itemDataService;
	private IStockOperationDataService stockOperationDataService;

	@ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public SimpleObject get(@RequestParam(value = "itemUUID", required = true) String itemUUID) {

        this.itemStockDataService = Context.getService(IItemStockDataService.class);
        this.itemDataService = Context.getService(IItemDataService.class);
        this.stockOperationDataService = Context.getService(IStockOperationDataService.class);
        

        SimpleObject result = new SimpleObject();
        
        
        // new implementation       
            try {
            Item find_item = getItem(itemUUID);
            List<StockOperationItem> itemStockOpsItem = stockOperationDataService.getItemsByItem(find_item);

            List<String> item_Batches = itemStockOpsItem.stream()
                    .map(ItemStockDetailBase::getItemBatch)
                    .collect(Collectors.toList()).stream().distinct()
                    .filter(a->Objects.nonNull(a)).collect(Collectors.toList());
            

            result.put("results", item_Batches);

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            result.put("error", ex.getMessage());
        }

            
        //old implementation    
//        try {
//            Item find_item = getItem(itemUUID);
//            List<ItemStock> itemStocks = itemStockDataService.getItemStockByItemWithOutPaging(find_item);
//
//            List<String> item_Batches = itemStocks.stream().findFirst()
//                    .get().getDetails().stream().map(ItemStockDetailBase::getItemBatch)
//                    .collect(Collectors.toList()).stream().distinct()
//                    .filter(a->Objects.nonNull(a)).collect(Collectors.toList());
//            
//
//            result.put("results", item_Batches);
//
//        } catch (Exception ex) {
//            LOG.error(ex.getMessage());
//            result.put("error", ex.getMessage());
//        }

        return result;

    }

	private Item getItem(String itemUUID) {
		Item item = null;

		if (StringUtils.isNotEmpty(itemUUID)) {
			item = itemDataService.getByUuid(itemUUID);
			if (item == null) {
				LOG.warn("Could not parse Item '" + itemUUID + "'");
				throw new IllegalArgumentException("The item '" + itemUUID
				        + "' is not a valid operation type.");
			}
		}
		return item;
	}

}
