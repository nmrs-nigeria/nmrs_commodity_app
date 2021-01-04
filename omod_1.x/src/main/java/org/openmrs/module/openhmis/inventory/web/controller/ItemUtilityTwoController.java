/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDataService;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemStock;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockDetailBase;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Toyeeb
 */
@Controller(value = "itemUtilityTwo")
@RequestMapping(ModuleWebConstants.MODULE_ITEM_UTILITY_TWO_ROOT)
public class ItemUtilityTwoController {

	private static final Log LOG = LogFactory.getLog(ItemUtilityTwoController.class);

	private IItemStockDataService itemStockDataService;
	private IItemDataService itemDataService;

	@ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public SimpleObject get(@RequestParam(value = "itemUUID", required = true) String itemUUID,
                            @RequestParam(value = "itemExpiration", required = true) String itemExpiration) {

        this.itemStockDataService = Context.getService(IItemStockDataService.class);
        this.itemDataService = Context.getService(IItemDataService.class);

        SimpleObject result = new SimpleObject();

        try {
            Item find_item = getItem(itemUUID);

            List<ItemStock> itemStocks = itemStockDataService.getItemStockByItemWithOutPaging(find_item);
            List<String> item_Batches = new ArrayList<String>();

            String itemExpr = formatStringToSQLDateString(itemExpiration);

            List<String> item_Batchess = itemStocks.stream().findFirst()
                    .get().getDetails().stream().map(ItemStockDetailBase::getItemBatch)
                    .collect(Collectors.toList()).stream()
                    .filter(a->Objects.nonNull(a)).collect(Collectors.toList());

            List<java.util.Date> expiryDates = itemStocks.stream().findFirst()
                    .get().getDetails().stream().map(ItemStockDetailBase::getExpiration)
                    .collect(Collectors.toList()).stream()
                    .filter(a->Objects.nonNull(a)).collect(Collectors.toList());

            System.out.println("Item Exp Dates " + itemExpiration + " or " + itemExpr);
            System.out.println("Dates " + expiryDates);
			System.out.println("Batches " + item_Batchess);

            int k = 0;
            for (java.util.Date exp : expiryDates) {
                String expFormat = formatUtilDateToString(exp.toString());
                Date expSQLFormat = formatStringToSQLDate(expFormat);
                Date itemExprSQLFormat = formatStringToSQLDate(itemExpr);
                if(expSQLFormat.equals(itemExprSQLFormat)) {
                    item_Batches.add(item_Batchess.get(k));
                }
                k++;
            }
         	List<String> itemBatches = item_Batches.stream().distinct().collect(Collectors.toList());

			System.out.println("itemBatches " + itemBatches);
			result.put("results", itemBatches);

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            result.put("error", ex.getMessage());
        }

        return result;

    }

	private static String formatUtilDateToString(String itemExpir) {
		String[] res = itemExpir.split("[ ]", 0);
		String[] itemExp = new String[res.length];
		int i = 0;
		for (String myStr : res) {
			itemExp[i] = myStr;
			i++;
		}
		String itemExpr = itemExp[0];
		return itemExpr;
	}

	private static String formatStringToSQLDateString(String itemExpir) {
		String[] res = itemExpir.split("[-]", 0);
		String[] itemExp = new String[res.length];
		int i = 0;
		for (String myStr : res) {
			itemExp[i] = myStr;
			i++;
		}
		String itemExpr = itemExp[2] + "-" + itemExp[1] + "-" + itemExp[0];
		return itemExpr;
	}

	private static Date formatStringToSQLDate(String itemExpir) {
		Date dt = Date.valueOf(itemExpir);
		return dt;
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
