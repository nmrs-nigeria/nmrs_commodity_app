/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseCustomizableMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.commons.api.f.Action1;
import org.openmrs.module.openhmis.inventory.api.IConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.model.Consumption;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.search.ConsumptionSearch;
import org.openmrs.module.openhmis.inventory.api.util.HibernateCriteriaConstants;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MORRISON.I
 */
public class ConsumptionDataServiceImpl extends BaseMetadataDataServiceImpl<Consumption>
        implements IConsumptionDataService, IMetadataAuthorizationPrivileges {

	private static final int MAX_ITEM_CODE_LENGTH = 255;

	// use temp varaiable for privilege

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		return this;
	}

	@Override
	protected void validate(Consumption e) {
		return;
	}

	@Override
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	// @Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	@Transactional(readOnly = true)
	public List<Consumption> getConsumptionByDepartment(final Department department, final boolean includeRetired,
	        PagingInfo pagingInfo) {
		if (department == null) {
			throw new NullPointerException("The department must be defined");
		}
		return executeCriteria(Consumption.class, pagingInfo, new Action1<Criteria>() {
			@Override
			public void apply(Criteria criteria) {
				criteria.add(Restrictions.eq(HibernateCriteriaConstants.DEPARTMENT, department));
				if (!includeRetired) {
					criteria.add(Restrictions.eq(HibernateCriteriaConstants.RETIRED, false));
				}
			}
		}, getDefaultSort());
	}

	@Override
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	//@Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	@Transactional(readOnly = true)
	public List<Consumption> getConsumptionByItem(final Item item, final boolean includeRetired, PagingInfo pagingInfo) {
		if (item == null) {
			throw new NullPointerException("The Item must be defined");
		}

		return executeCriteria(Consumption.class, pagingInfo, new Action1<Criteria>() {
			@Override
			public void apply(Criteria criteria) {
				criteria.add(Restrictions.eq(HibernateCriteriaConstants.ITEM, item));
				if (!includeRetired) {
					criteria.add(Restrictions.eq(HibernateCriteriaConstants.RETIRED, false));
				}
			}
		}, getDefaultSort());

	}

	@Override
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	//@Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	@Transactional(readOnly = true)
	public List<Consumption> getConsumptions(Department department, Item item, boolean includeRetired) {
		return getConsumptions(department, item, includeRetired, null);
	}

	@Override
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	//@Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	@Transactional(readOnly = true)
	public List<Consumption> getConsumptions(final Department department, final Item item,
	        final boolean includeRetired, PagingInfo pagingInfo) {

		if (department == null) {
			throw new NullPointerException("The department must be defined");
		}

		if (item == null) {
			throw new NullPointerException("The item must be defined");
		}

		//        if(ConsumptionDate == null){
		//        throw new NullPointerException("The consumption date must be defined");
		//        }

		return executeCriteria(Consumption.class, pagingInfo, new Action1<Criteria>() {
			@Override
			public void apply(Criteria criteria) {
				criteria.add(Restrictions.eq(HibernateCriteriaConstants.DEPARTMENT, department))
				        .add(
				            Restrictions.eq(HibernateCriteriaConstants.ITEM, item));

				if (!includeRetired) {
					criteria.add(Restrictions.eq(HibernateCriteriaConstants.RETIRED, false));
				}
			}
		}, getDefaultSort());

	}

	@Override
	public List<Consumption> getConsumptionsByConsumptionSearch(ConsumptionSearch consumptionSearch) {
		return getConsumptionsByConsumptionSearch(consumptionSearch, null);
	}

	@Override
	public List<Consumption> getConsumptionsByConsumptionSearch(final ConsumptionSearch consumptionSearch,
	        PagingInfo pagingInfo) {

		if (consumptionSearch == null) {
			throw new NullPointerException("The consumption search  must be defined");
		}

		if (consumptionSearch.getTemplate() == null) {
			throw new NullPointerException("The consumption search template must be defined");
		}

		return executeCriteria(Consumption.class, pagingInfo, new Action1<Criteria>() {
			@Override
			public void apply(Criteria criteria) {
				consumptionSearch.updateCriteria(criteria);
			}
		}, getDefaultSort());

	}

	@Override
	public List<Consumption> getConsumptionByConsumptionDate(final Date startDate, final Date endDate,
	        PagingInfo pagingInfo) {
		return getConsumptionsByDate(startDate, endDate, pagingInfo, null,
		    Order.asc(HibernateCriteriaConstants.CONSUMPTION_QUANTITY),
		    Order.asc(HibernateCriteriaConstants.CONSUMPTION_DATE));
	}

	private List<Consumption> getConsumptionsByDate(final Date startDate, final Date endDate,
	        PagingInfo paging, final Integer maxResults,
	        Order... orders) {
		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException("The date to search for must be defined.");
		}

		return executeCriteria(Consumption.class, paging, new Action1<Criteria>() {
			@Override
			public void apply(Criteria criteria) {
				criteria.add(createDateRestriction(startDate, endDate));
				if (maxResults != null && maxResults > 0) {
					criteria.setMaxResults(maxResults);
				}
			}
		}, orders);
	}

	private Criterion createDateRestriction(Date startDate, Date endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		Utility.clearCalendarTime(cal);
		final Date start = cal.getTime();

		cal.setTime(endDate);
		//cal.add(Calendar.DAY_OF_MONTH, 1);
		//cal.add(Calendar.MILLISECOND, -1);
		final Date end = cal.getTime();

		return Restrictions.between(HibernateCriteriaConstants.CONSUMPTION_DATE, start, end);
	}

	@Override
	protected Order[] getDefaultSort() {
		return new Order[] { Order.asc("consumptionDate") };
		// return super.getDefaultSort(); 
	}

	@Override
	public List<Consumption> getByNameFragment(String nameFragment, boolean includeRetired, PagingInfo pagingInfo) {
		return super.getAll(includeRetired, pagingInfo);
	}

	@Override
	public List<Consumption> getByNameFragment(String nameFragment, boolean includeRetired) {
		return getByNameFragment(nameFragment, includeRetired, null);
	}

	@Override
	public String getRetirePrivilege() {
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	public String getSavePrivilege() {
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	public String getPurgePrivilege() {
		return PrivilegeConstants.PURGE_CONSUMPTION;
	}

	@Override
	public String getGetPrivilege() {
		return PrivilegeConstants.VIEW_CONSUMPTIONS;
	}

}
