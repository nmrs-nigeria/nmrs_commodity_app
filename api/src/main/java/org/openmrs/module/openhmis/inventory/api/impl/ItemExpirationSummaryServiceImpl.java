/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseObjectDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummary;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;
import org.openmrs.module.openhmis.inventory.api.IItemExpirationSummaryService;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
import org.openmrs.module.openhmis.inventory.api.security.BasicObjectAuthorizationPrivileges;

/**
 * @author MORRISON.I
 */
public class ItemExpirationSummaryServiceImpl
        extends BaseObjectDataServiceImpl<ItemExpirationSummary, BasicObjectAuthorizationPrivileges>
        implements IItemExpirationSummaryService, IMetadataAuthorizationPrivileges {

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_ITEMS })
	@Override
	public List<ItemExpirationSummary> getItemExipirationSummary(PagingInfo pagingInfo) {
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	public List<ItemExpirationSummary> getItemStockSummaryByStockroom(final Stockroom stockroom, PagingInfo pagingInfo) {
		if (stockroom == null) {
			throw new IllegalArgumentException("The stockroom must be defined.");
		}

		// We cannot use a normal Criteria query here because criteria does not support a group by with a having statement
		// so HQL it is!
		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			// Load the record count (for paging)
			String countHql = "select 1 "
			        + "from ItemStockDetail as detail "
			        + "where stockroom.id = " + stockroom.getId() + " "
			        + "group by item, expiration "
			        + "having sum(detail.quantity) <> 0";
			Query countQuery = getRepository().createQuery(countHql);

			Integer count = countQuery.list().size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		// Create the query and optionally add paging
		String hql = "select i, detail.expiration, sum(detail.quantity) as sumQty "
		        + "from ItemStockDetail as detail inner join detail.item as i "
		        + "where detail.stockroom.id = " + stockroom.getId() + " "
		        + "group by i, detail.expiration "
		        + "having sum(detail.quantity) <> 0"
		        + "order by i.name asc, detail.expiration asc";
		Query query = getRepository().createQuery(hql);
		query = this.createPagingQuery(pagingInfo, query);

		List list = query.list();

		// Parse the aggregate query into an ItemStockSummary object
		List<ItemExpirationSummary> results = new ArrayList<ItemExpirationSummary>(list.size());
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			ItemExpirationSummary summary = new ItemExpirationSummary();
			summary.setItem((Item)row[0]);

			// If the expiration column is null it does not appear to be included in the row array
			if (row.length == 2) {
				summary.setExpiration(null);
				Integer quantity = Ints.checkedCast((Long)row[1]);
				// skip record if the sum of item stock quantities == 0
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			} else {
				summary.setExpiration((Date)row[1]);
				Integer quantity = Ints.checkedCast((Long)row[2]);
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			}

			results.add(summary);
		}

		// We done.
		return results;
	}

	public List<ItemExpirationSummary> getItemStockSummaryByDepartment(final Department department, PagingInfo pagingInfo) {
		if (department == null) {
			throw new IllegalArgumentException("The testing point must be defined.");
		}

		// We cannot use a normal Criteria query here because criteria does not support a group by with a having statement
		// so HQL it is!
		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {

						String query = "SELECT 1 "
						        + "FROM StockOperation a "
						        + "INNER JOIN StockOperationItem b "
						        + "ON a.id=b.operation "
						        + "LEFT JOIN Consumption c "
						        + "ON b.itemBatch = c.batchNumber "
						        + "AND c.department= " + department.getId() + " "
						        + "WHERE a.department= " + department.getId() + " "
						        + "GROUP BY b.itemBatch ";			

			Query countQuery = getRepository().createQuery(query);

			Integer count = countQuery.list().size();
			System.out.println("HQL Count " + count);

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);

		}

		// Create the query and optionally add paging	
		String hql = "SELECT b.item, b.expiration, b.quantity - coalesce(SUM(c.quantity + c.wastage), 0) as sumQty "
		        + "FROM StockOperation AS a "
		        + "INNER JOIN StockOperationItem AS b "
		        + "ON a.id=b.operation "
		        + "LEFT JOIN Consumption AS c "
		        + "ON b.itemBatch = c.batchNumber "
		        + "AND c.department.id= " + department.getId() + " "
		        + "WHERE a.department.id= " + department.getId() + " "
		        + "GROUP BY b.itemBatch";

		Query query = getRepository().createQuery(hql);
		query = this.createPagingQuery(pagingInfo, query);

		List list = query.list();
		System.out.println("HQL list Size " + list.size());

		// Parse the aggregate query into an ItemStockSummary object
		List<ItemExpirationSummary> results = new ArrayList<ItemExpirationSummary>(list.size());
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			ItemExpirationSummary summary = new ItemExpirationSummary();
			summary.setItem((Item)row[0]);

			// If the expiration column is null it does not appear to be included in the row array
			if (row.length == 2) {
				summary.setExpiration(null);
				Integer quantity = Ints.checkedCast((Long)row[1]);
				// skip record if the sum of item stock quantities == 0
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			} else {
				summary.setExpiration((Date)row[1]);
				Integer quantity = Ints.checkedCast((Long)row[2]);
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			}

			results.add(summary);
		}

		// We done.
		return results;
	}

	@Override
	protected void validate(ItemExpirationSummary object) {

	}

	@Override
	public String getRetirePrivilege() {
		return PrivilegeConstants.VIEW_ITEMS;
	}

	@Override
	public String getSavePrivilege() {
		return PrivilegeConstants.VIEW_ITEMS;
	}

	@Override
	public String getPurgePrivilege() {
		return PrivilegeConstants.VIEW_ITEMS;
	}

	@Override
	public String getGetPrivilege() {
		return PrivilegeConstants.VIEW_ITEMS;
	}

	@Override
	protected BasicObjectAuthorizationPrivileges getPrivileges() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
