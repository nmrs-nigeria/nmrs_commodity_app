/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.resource;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDetailDataService;
import org.openmrs.module.openhmis.inventory.api.IStockroomDataService;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummary;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.helper.ConstantUtils;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.openhmis.inventory.api.IItemExpirationSummaryService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * @author MORRISON.I
 */
@Resource(name = ModuleRestConstants.ITEM_EXPIRATION_SUMMARY_RESOURCE, supportedClass = ItemExpirationSummary.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class ItemExipirationSummaryResource extends DelegatingCrudResource<ItemExpirationSummary> {

	private IItemDataService itemDataService;
	private IDepartmentDataService departmentService;
	private IStockroomDataService stockroomDataService;
	private IItemStockDetailDataService itemStockDetailDataService;
	private IItemExpirationSummaryService itemExpirationSummaryService;

	public ItemExipirationSummaryResource() {

		this.itemDataService = Context.getService(IItemDataService.class);
		this.departmentService = Context.getService(IDepartmentDataService.class);
		this.stockroomDataService = Context.getService(IStockroomDataService.class);
		this.itemStockDetailDataService = Context.getService(IItemStockDetailDataService.class);
		this.itemExpirationSummaryService = Context.getService(IItemExpirationSummaryService.class);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("item", Representation.DEFAULT);
		description.addProperty("expiration", Representation.DEFAULT);
		description.addProperty("quantity", Representation.DEFAULT);
		//	description.addProperty("actualQuantity", Representation.DEFAULT);
		description.addProperty("itemBatch", Representation.DEFAULT);

		return description;
	}

	@PropertySetter("expiration")
	public void setExpiration(ItemExpirationSummary instance, String dateText) {
		instance.setExpiration(Utility.parseOpenhmisDateString(dateText));
	}

	@Override
    protected PageableResult doSearch(RequestContext context) {

        PageableResult result = null;
        String stockroomUuid = context.getParameter("stockroom_uuid");
        String departmentUuid = context.getParameter("department_uuid");
        
        if (StringUtils.isNotBlank(stockroomUuid) || StringUtils.isNotBlank(departmentUuid)) {
            PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
            System.out.println("stockroomUuid" + stockroomUuid);
            System.out.println("departmentUuid" + departmentUuid);
            if(stockroomUuid != null && !stockroomUuid.isEmpty()) {
	            Stockroom stockroom = stockroomDataService.getByUuid(stockroomUuid);
	       	 	System.out.println("stockroom: " + stockroom);
	            List<ItemExpirationSummary> itemStockDetails
	                    = itemExpirationSummaryService.getItemStockSummaryByStockroom(stockroom, pagingInfo);
	
	            System.out.println("itemStockDetails: " + itemStockDetails);
	            //    = itemExpirationSummaryService.getItemStockDetailsByStockroom(stockroom, pagingInfo);
	            itemStockDetails = itemStockDetails.stream().filter(a -> a.getQuantity() > 0)
	                    .filter(a -> getDaysDiff(new Date(),a.getExpiration()) <= ConstantUtils.EXPIRYDAYSCOUNT)
	                    .collect(Collectors.toList());
	            System.out.println("itemStockDetails 2: " + itemStockDetails);
	            result
	            = new AlreadyPagedWithLength<ItemExpirationSummary>(context, itemStockDetails, pagingInfo.hasMoreResults(),
	                            pagingInfo.getTotalRecordCount());
            }else if(departmentUuid != null && !departmentUuid.isEmpty()) {
            	Department department = departmentService.getByUuid(departmentUuid);
	       	 	System.out.println("department: " + department);
	            List<ItemExpirationSummary> itemStockDetails
	                    = itemExpirationSummaryService.getItemStockSummaryByDepartment(department, pagingInfo);
	
	            System.out.println("itemStockDetails: " + itemStockDetails);
	            
	            itemStockDetails = itemStockDetails.stream().filter(a -> a.getQuantity() > 0)
	                    .filter(a -> getDaysDiff(new Date(),a.getExpiration()) <= ConstantUtils.EXPIRYDAYSCOUNT)
	                    .collect(Collectors.toList());
	            System.out.println("itemStockDetails 2: " + itemStockDetails);
	            result
	            = new AlreadyPagedWithLength<ItemExpirationSummary>(context, itemStockDetails, pagingInfo.hasMoreResults(),
	                            pagingInfo.getTotalRecordCount());
            }
            
        } else {
            result = super.doSearch(context);
        }
        return result;

    }

	private int getDaysDiff(Date startDate, Date endDate) {
		int daysdiff = Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays();
		System.out.println("days diff for exp. date " + startDate + " is " + daysdiff);
		return daysdiff;

	}

	@Override
	public ItemExpirationSummary newDelegate() {
		return new ItemExpirationSummary();
	}

	@Override
	public ItemExpirationSummary getByUniqueId(String string) {
		return null;
	}

	@Override
	protected void delete(ItemExpirationSummary t, String string, RequestContext rc) throws ResponseException {

	}

	@Override
	public void purge(ItemExpirationSummary t, RequestContext rc) throws ResponseException {

	}

	@Override
	public SimpleObject getAll(RequestContext context) throws ResponseException {
		return new SimpleObject();
	}

	@Override
	public ItemExpirationSummary save(ItemExpirationSummary delegate) {
		return null;
	}

}
