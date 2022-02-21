/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseObjectDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.f.Action1;
import org.openmrs.module.openhmis.inventory.api.IItemStockDetailDataService;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockDetail;
import org.openmrs.module.openhmis.inventory.api.security.BasicObjectAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockSummary;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.ViewInvStockonhandPharmacyDispensary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.CrrfArvCotrim;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationItem;

import com.google.common.primitives.Ints;

/**
 * Data service implementation class for {@link ItemStockDetail}.
 */
@Transactional
public class ItemStockDetailDataServiceImpl
        extends BaseObjectDataServiceImpl<ItemStockDetail, BasicObjectAuthorizationPrivileges>
        implements IItemStockDetailDataService {

	private static final int THREE = 3;
	private static final int TWO = 2;
	private static final int FOUR = 4;
	private static final int FIVE = 5;

	public ItemStockDetailDataServiceImpl() {}

	@Override
	protected BasicObjectAuthorizationPrivileges getPrivileges() {
		return new BasicObjectAuthorizationPrivileges();
	}

	@Override
	protected void validate(ItemStockDetail object) {

	}

	@Override
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	public List<ItemStockDetail> getItemStockDetailsByStockroom(final Stockroom stockroom, PagingInfo pagingInfo) {
		if (stockroom == null) {
			throw new IllegalArgumentException("The stockroom must be defined.");
		}

		return executeCriteria(ItemStockDetail.class, pagingInfo, new Action1<Criteria>() {
			@Override
			public void apply(Criteria criteria) {
				criteria.createAlias("item", "i");
				criteria.add(Restrictions.eq("stockroom", stockroom));
			}
		}, Order.asc("i.name"));
	}

	@Override
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	public List<ItemStockSummary> getItemStockSummaryByStockroom(final Stockroom stockroom, PagingInfo pagingInfo) {
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
		String hql = "select i, detail.expiration, sum(detail.quantity) as sumQty, detail.itemBatch "
		        + "from ItemStockDetail as detail inner join detail.item as i "
		        + "where detail.stockroom.id = " + stockroom.getId() + " "
		        + "group by i, detail.expiration "
		        + "having sum(detail.quantity) <> 0"
		        + "order by i.name asc, detail.expiration asc";
		Query query = getRepository().createQuery(hql);
		query = this.createPagingQuery(pagingInfo, query);

		List list = query.list();

		// Parse the aggregate query into an ItemStockSummary object
		List<ItemStockSummary> results = new ArrayList<ItemStockSummary>(list.size());
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			ItemStockSummary summary = new ItemStockSummary();
			summary.setItem((Item)row[0]);

			// If the expiration column is null it does not appear to be included in the row array
			if (row.length == THREE) {
				summary.setExpiration(null);
				Integer quantity = Ints.checkedCast((Long)row[1]);
				String batch = (String)row[2];
				summary.setItemBatch(batch);
				// skip record if the sum of item stock quantities == 0
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			} else {
				summary.setExpiration((Date)row[1]);
				Integer quantity = Ints.checkedCast((Long)row[2]);
				String batch = (String)row[THREE];
				summary.setItemBatch(batch);
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
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	public List<ItemStockSummary> getItemStockSummaryByDepartment(final Department department, PagingInfo pagingInfo) {
		if (department == null) {
			throw new IllegalArgumentException("The department must be defined.");
		}

		// We cannot use a normal Criteria query here because criteria does not support a group by with a having statement
		// so HQL it is!

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			// Load the record count (for paging)	
			String countHql = "select 1 "
			        + "from ViewItemExpirationByDept as detail "
			        + "where department.id = " + department.getId();

			Query countQuery = getRepository().createQuery(countHql);

			Integer count = countQuery.list().size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		// Create the query and optionally add paging	
		String hql = "select i, detail.expiration, detail.quantity as sumQty "
		        + "from ViewItemExpirationByDept as detail inner join detail.item as i "
		        + "where detail.department.id = " + department.getId() + " "
		        + "order by i.name asc, detail.expiration asc";

		Query query = getRepository().createQuery(hql);
		query = this.createPagingQuery(pagingInfo, query);

		List list = query.list();

		// Parse the aggregate query into an ItemStockSummary object
		List<ItemStockSummary> results = new ArrayList<ItemStockSummary>(list.size());
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			ItemStockSummary summary = new ItemStockSummary();
			summary.setItem((Item)row[0]);

			// If the expiration column is null it does not appear to be included in the row array
			if (row.length == 2) {
				summary.setExpiration(null);
				Integer quantity = (int)row[1];
				// skip record if the sum of item stock quantities == 0
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			} else {
				summary.setExpiration((Date)row[1]);
				Integer quantity = (int)row[2];
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

	public List<ItemStockSummary> getItemStockSummaryByDepartmentPharmacy(final Department department,
	        PagingInfo pagingInfo) {
		if (department == null) {
			throw new IllegalArgumentException("The department must be defined.");
		}

		// We cannot use a normal Criteria query here because criteria does not support a group by with a having statement
		// so HQL it is!

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			// Load the record count (for paging)	
			String countHql = "select 1 "
			        + "from ViewInvStockonhandPharmacyDispensary as detail "
			        + "where department.id = " + department.getId();

			Query countQuery = getRepository().createQuery(countHql);

			Integer count = countQuery.list().size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		// Create the query and optionally add paging	
		String hql = "select i, detail.expiration, detail.updatableQuantity as sumQty, detail.id, detail.itemBatch "
		        + "from ViewInvStockonhandPharmacyDispensary as detail inner join detail.item as i "
		        + "where detail.department.id = " + department.getId() + " "
		        + "order by i.name asc, detail.expiration asc";

		Query query = getRepository().createQuery(hql);
		query = this.createPagingQuery(pagingInfo, query);

		List list = query.list();

		// Parse the aggregate query into an ItemStockSummary object
		List<ItemStockSummary> results = new ArrayList<ItemStockSummary>(list.size());
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			ItemStockSummary summary = new ItemStockSummary();
			summary.setItem((Item)row[0]);

			// If the expiration column is null it does not appear to be included in the row array
			if (row.length == FOUR) {
				summary.setExpiration(null);
				Integer quantity = (int)row[1];
				Integer pharmId = (int)row[2];
				String batch = (String)row[THREE];
				// skip record if the sum of item stock quantities == 0
				if (quantity != 0) {
					summary.setQuantity(quantity);
					summary.setPharmStockOnHandId(pharmId);
					summary.setItemBatch(batch);
				} else {
					continue;
				}
			} else {
				summary.setExpiration((Date)row[1]);
				Integer quantity = (int)row[2];
				Integer pharmId = (int)row[THREE];
				String batch = (String)row[FOUR];
				if (quantity != 0) {
					summary.setQuantity(quantity);
					summary.setPharmStockOnHandId(pharmId);
					summary.setItemBatch(batch);
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
	public void updatePharmacyAtDispensary(List<ViewInvStockonhandPharmacyDispensary> viewInvStockonhandPharmacyDispensary) {

		for (ViewInvStockonhandPharmacyDispensary obj : viewInvStockonhandPharmacyDispensary) {

			String hql = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
			        + "updatableQuantity = " + obj.getUpdatableQuantity() + " "
			        + "where id = " + obj.getId();

			Query query = getRepository().createQuery(hql);
			int sql = query.executeUpdate();
			System.out.println("Updated Executed: " + sql);
		}
	}

	@Override
	public void addNewDistributionDataPharmacyAtDispensary(StockOperation operation) {

		//check that operation is not null
		if (operation == null) {
			throw new IllegalArgumentException("The operation must be defined.");
		}

		//get list of all stock operation items
		Iterator<StockOperationItem> operationItems = operation.getItems().iterator();

		while (operationItems.hasNext()) {

			StockOperationItem opItem = operationItems.next();

			ViewInvStockonhandPharmacyDispensary vs = new ViewInvStockonhandPharmacyDispensary();
			vs.setItem(opItem.getItem());
			vs.setExpiration(opItem.getExpiration());
			vs.setQuantity(opItem.getQuantity());
			vs.setItemBatch(opItem.getItemBatch());
			vs.setStockOperationId(operation.getId());
			vs.setOperationTypeId(operation.getInstanceType().getId());
			vs.setItemDrugType(opItem.getItemDrugType());
			vs.setCommodityType(operation.getCommodityType());
			vs.setUpdatableQuantity(opItem.getQuantity());
			vs.setDepartment(operation.getDepartment());
			vs.setDateCreated(operation.getDateCreated());

			int updateAbleQuantity = 0;
			int finalQuantity = 0;
			int dispensaryId = 0;

			//check if there is existing record with same itemID, itemBatch, commodityType, expiration, departmentID
			String hql = "select detail.updatableQuantity as sumQty, detail.id, detail.quantity "
			        + "from ViewInvStockonhandPharmacyDispensary as detail "
			        + "where detail.department.id = " + vs.getDepartment().getId() + " and "
			        + "detail.itemBatch = '" + vs.getItemBatch() + "' and "
			        + "detail.commodityType = '" + vs.getCommodityType() + "' and "
			        + "detail.expiration = '" + vs.getExpiration() + "' and "
			        + "detail.item.id = " + vs.getItem().getId();

			Query query = getRepository().createQuery(hql);
			List list = query.list();

			// Parse the aggregate query into an ViewInvStockonhandPharmacyDispensary object
			List<ViewInvStockonhandPharmacyDispensary> results =
			        new ArrayList<ViewInvStockonhandPharmacyDispensary>(list.size());
			for (Object obj : list) {
				Object[] row = (Object[])obj;

				ViewInvStockonhandPharmacyDispensary summary = new ViewInvStockonhandPharmacyDispensary();
				summary.setUpdatableQuantity((Integer)row[0]);
				summary.setId((Integer)row[1]);
				summary.setQuantity((Integer)row[2]);

				results.add(summary);
			}

			if (results.size() == 1) {
				for (ViewInvStockonhandPharmacyDispensary viewInvStockonhandPharmacyDispensary : results) {
					updateAbleQuantity += viewInvStockonhandPharmacyDispensary.getUpdatableQuantity();
					finalQuantity += viewInvStockonhandPharmacyDispensary.getQuantity();
					dispensaryId = viewInvStockonhandPharmacyDispensary.getId();
				}
				updateAbleQuantity += vs.getUpdatableQuantity();
				finalQuantity += vs.getQuantity();
				String hql2 = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
				        + "updatableQuantity = " + updateAbleQuantity + ", "
				        + "quantity = " + finalQuantity + " "
				        + "where id = " + dispensaryId;

				Query query2 = getRepository().createQuery(hql2);
				query2.executeUpdate();

			} else if (results.size() > 1) {
				int id[] = new int[results.size()];
				int i = 0;
				for (ViewInvStockonhandPharmacyDispensary viewInvStockonhandPharmacyDispensary : results) {
					updateAbleQuantity += viewInvStockonhandPharmacyDispensary.getUpdatableQuantity();
					finalQuantity += viewInvStockonhandPharmacyDispensary.getQuantity();
					id[i] = viewInvStockonhandPharmacyDispensary.getId();
					i++;
				}
				int counter = 1;
				int index = 0;
				while (counter < results.size()) {
					dispensaryId = id[index];
					String hql4 = "DELETE FROM ViewInvStockonhandPharmacyDispensary as v "
					        + "where id = " + dispensaryId;

					Query query4 = getRepository().createQuery(hql4);
					query4.executeUpdate();
					counter++;
					index++;
				}
				updateAbleQuantity += vs.getUpdatableQuantity();
				finalQuantity += vs.getQuantity();
				dispensaryId = id[index];
				String hql3 = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
				        + "updatableQuantity = " + updateAbleQuantity + ", "
				        + "quantity = " + finalQuantity + " "
				        + "where id = " + dispensaryId;

				Query query3 = getRepository().createQuery(hql3);
				query3.executeUpdate();

			} else {
				//insert record to inv_stockonhand_pharmacy_dispensary		
				getRepository().save(vs);
			}

		}

	}

	@Override
	public List<CrrfArvCotrim> getItemStockSummaryByPharmacy(Date startDate, Date endDate, PagingInfo pagingInfo) {
		System.out.println("Stock CRRF Tobe");
		return null;
	}

}
