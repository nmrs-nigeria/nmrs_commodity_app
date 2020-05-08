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
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemExipirationSummaryService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDetailDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationDataService;
import org.openmrs.module.openhmis.inventory.api.IStockroomDataService;
import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummary;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockDetail;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.helper.ConstantUtils;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

/**
 * @author MORRISON.I
 */
@Resource(name = ModuleRestConstants.ITEM_EXPIRATION_SUMMARY_RESOURCE, supportedClass = ItemExpirationSummary.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class ItemExipirationSummaryResource extends BaseRestMetadataResource<ItemExpirationSummary> {

	private IItemDataService itemDataService;
	private IDepartmentDataService departmentService;
	private IStockOperationDataService stockOperationDataService;
	private IStockroomDataService stockroomDataService;
	private IItemStockDetailDataService itemStockDetailDataService;

	public ItemExipirationSummaryResource() {

		this.itemDataService = Context.getService(IItemDataService.class);
		this.departmentService = Context.getService(IDepartmentDataService.class);
		this.stockOperationDataService = Context.getService(IStockOperationDataService.class);
		this.stockroomDataService = Context.getService(IStockroomDataService.class);
		this.itemStockDetailDataService = Context.getService(IItemStockDetailDataService.class);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.addProperty("item", Representation.REF);
		description.addProperty("department", Representation.REF);
		description.addProperty("expirationDate");

		return description;
	}

	@Override
    protected PageableResult doSearch(RequestContext context) {

        PageableResult result;     
        String stockroomUuid = context.getParameter("stockroom_uuid");
        if (StringUtils.isNotBlank(stockroomUuid)) {
            PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
            Stockroom stockroom = stockroomDataService.getByUuid(stockroomUuid);
            List<ItemStockDetail> itemStockDetails
                    = itemStockDetailDataService.getItemStockDetailsByStockroom(stockroom, pagingInfo);

            itemStockDetails = itemStockDetails.stream().filter(a -> a.getQuantity() > 0)
                    .filter(a -> getDaysDiff(a.getExpiration(), new Date()) <= ConstantUtils.EXPIRYDAYSCOUNT)
                    .collect(Collectors.toList());
            result
         = new AlreadyPagedWithLength<ItemStockDetail>(context, itemStockDetails, pagingInfo.hasMoreResults(),
          pagingInfo.getTotalRecordCount());
        } else {
            result = super.doSearch(context);
        }
        return result;

    }

	private int getDaysDiff(Date startDate, Date endDate) {

		return Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays();

	}

	@Override
	public ItemExpirationSummary newDelegate() {
		return new ItemExpirationSummary();
	}

	@Override
	public Class<? extends IMetadataDataService<ItemExpirationSummary>> getServiceClass() {
		return IItemExipirationSummaryService.class;
	}

}
