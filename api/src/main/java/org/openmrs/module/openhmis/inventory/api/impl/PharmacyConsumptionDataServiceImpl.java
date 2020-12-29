/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.commons.api.f.Action1;
import org.openmrs.module.openhmis.inventory.api.IPharmacyConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumption;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.SearchConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationItem;
import org.openmrs.module.openhmis.inventory.api.search.PharmacyConsumptionSearch;
import org.openmrs.module.openhmis.inventory.api.util.HibernateCriteriaConstants;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MORRISON.I
 */
public class PharmacyConsumptionDataServiceImpl extends BaseMetadataDataServiceImpl<PharmacyConsumption>
        implements IPharmacyConsumptionDataService, IMetadataAuthorizationPrivileges {

	private static final int MAX_ITEM_CODE_LENGTH = 255;

	// use temp varaiable for privilege

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		return this;
	}

	@Override
	protected void validate(PharmacyConsumption e) {
		return;
	}

	@Override
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	// @Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	@Transactional(readOnly = true)
	public List<PharmacyConsumption> getConsumptionByDepartment(final Department department, final boolean includeRetired,
	        PagingInfo pagingInfo) {
		if (department == null) {
			throw new NullPointerException("The department must be defined");
		}
		return executeCriteria(PharmacyConsumption.class, pagingInfo, new Action1<Criteria>() {
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
	public List<PharmacyConsumption> getConsumptionByItem(final Item item, final boolean includeRetired,
	        PagingInfo pagingInfo) {
		if (item == null) {
			throw new NullPointerException("The Item must be defined");
		}

		return executeCriteria(PharmacyConsumption.class, pagingInfo, new Action1<Criteria>() {
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
	public List<PharmacyConsumption> getConsumptions(Department department, Item item, boolean includeRetired) {
		return getConsumptions(department, item, includeRetired, null);
	}

	@Override
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	//@Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	@Transactional(readOnly = true)
	public List<PharmacyConsumption> getConsumptions(final Department department, final Item item,
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

		return executeCriteria(PharmacyConsumption.class, pagingInfo, new Action1<Criteria>() {
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
	public List<PharmacyConsumption> getConsumptionsByConsumptionSearch(PharmacyConsumptionSearch consumptionSearch) {
		return getConsumptionsByConsumptionSearch(consumptionSearch, null);
	}

	@Override
	public List<PharmacyConsumption> getConsumptionsByConsumptionSearch(final PharmacyConsumptionSearch consumptionSearch,
	        PagingInfo pagingInfo) {

		if (consumptionSearch == null) {
			throw new NullPointerException("The consumption search  must be defined");
		}

		if (consumptionSearch.getTemplate() == null) {
			throw new NullPointerException("The consumption search template must be defined");
		}

		return executeCriteria(PharmacyConsumption.class, pagingInfo, new Action1<Criteria>() {
			@Override
			public void apply(Criteria criteria) {
				consumptionSearch.updateCriteria(criteria);
			}
		}, getDefaultSort());

	}

	@Override
	public List<PharmacyConsumption> getConsumptionByConsumptionDate(final Date startDate, final Date endDate,
	        PagingInfo pagingInfo) {
		return getConsumptionsByDate(startDate, endDate, pagingInfo, null,
		    Order.asc(HibernateCriteriaConstants.CONSUMPTION_QUANTITY),
		    Order.asc(HibernateCriteriaConstants.CONSUMPTION_DATE));
	}

	@Override
	public List<PharmacyConsumptionSummary> retrieveConsumptionSummary(List<StockOperation> stockOperations,
	        SearchConsumptionSummary searchConsumptionSummary, PagingInfo pagingInfo, List<Item> distinctItems) {
		List<PharmacyConsumptionSummary> fromConsumption = null;
		List<PharmacyConsumptionSummary> fromReceived = null;
		List<PharmacyConsumptionSummary> finalConsumptionSummarys = null;
		List<PharmacyConsumption> consumptions = new ArrayList<>();

		System.out.println("stock operations result: " + stockOperations.size());

		fromReceived = getSummaryFromStockOperation(stockOperations,
		    searchConsumptionSummary.getDepartment(), distinctItems);

		System.out.println("After stock summary: " + fromReceived.size());

		consumptions = getConsumptionByConsumptionDate(searchConsumptionSummary.getStartDate(),
		    searchConsumptionSummary.getEndDate(), pagingInfo);

		System.out.println("stock consumptions result: " + consumptions.size());

		//filter out the Department
		//	        consumptions = consumptions.stream().filter(a -> a.getDepartment()
		//	.equals(searchConsumptionSummary.getDepartment()))
		//	                .collect(Collectors.toList());
		fromConsumption = getSummaryFromConsumption(consumptions, searchConsumptionSummary
		        .getDepartment(), distinctItems);

		System.out.println("stock consumptions aggregate result  : " + fromConsumption.size());

		List<PharmacyConsumptionSummary> tempConsumptionSummary = new ArrayList<>();

		tempConsumptionSummary = mergeSummary(fromConsumption, fromReceived,
		    searchConsumptionSummary.getDepartment(), distinctItems);

		finalConsumptionSummarys = calculateStockBalance(tempConsumptionSummary);

		System.out.println("final consumption summary is " + finalConsumptionSummarys.size());

		return finalConsumptionSummarys;

	}

	private List<PharmacyConsumptionSummary> calculateStockBalance(List<PharmacyConsumptionSummary> consumptionSummarys) {

		List<PharmacyConsumptionSummary> consumptionSummarysWithBalance = new ArrayList<>();

		for (PharmacyConsumptionSummary summary : consumptionSummarys) {
			PharmacyConsumptionSummary each = new PharmacyConsumptionSummary();
			each.setItem(summary.getItem());
			// each.setDepartment(summary.getDepartment());
			each.setTotalQuantityConsumed(summary.getTotalQuantityConsumed());
			each.setTotalQuantityReceived(summary.getTotalQuantityReceived());
			each.setTotalQuantityWasted(summary.getTotalQuantityWasted());
			if (each.getTotalQuantityReceived() > 0) {
				each.setStockBalance(each.getTotalQuantityReceived()
				        - (each.getTotalQuantityConsumed() + each.getTotalQuantityWasted()));
			} else {
				each.setStockBalance(0);
			}

			consumptionSummarysWithBalance.add(each);
		}

		return consumptionSummarysWithBalance;

	}

	private List<PharmacyConsumptionSummary> mergeSummary(List<PharmacyConsumptionSummary> fromConsumption, 
            List<PharmacyConsumptionSummary> fromReceived, Department department, List<Item> distinctItems) {

        List<PharmacyConsumptionSummary> mergedConsumptionSummarys = new ArrayList<>();

        distinctItems.forEach(a -> {
            PharmacyConsumptionSummary each = new PharmacyConsumptionSummary();
            //get sum from received
            int sumReceived = fromReceived.stream().filter(b -> b.getItem().equals(a))
                    .map(PharmacyConsumptionSummary::getTotalQuantityReceived).findFirst().get();
            each.setTotalQuantityReceived(sumReceived);
           // each.setDepartment(department);
            int sumConsumed = fromConsumption.stream().filter(b -> b.getItem().equals(a))
                    .map(PharmacyConsumptionSummary::getTotalQuantityConsumed).findFirst().get();
            each.setTotalQuantityConsumed(sumConsumed);
            
            int sumWastage = fromConsumption.stream().filter(b -> b.getItem().equals(a))
                    .map(PharmacyConsumptionSummary::getTotalQuantityWasted).findFirst().get();
            
            each.setTotalQuantityWasted(sumWastage);
            
            
            each.setItem(a);
            mergedConsumptionSummarys.add(each);

        });
        
        return mergedConsumptionSummarys;

    }

	private List<PharmacyConsumptionSummary> getSummaryFromConsumption(List<PharmacyConsumption> consumptions, 
	            Department department,List<Item> distinctItems) {
	
	        List<PharmacyConsumptionSummary> aggregateConsumption = new ArrayList<>();
	
	        distinctItems.forEach(a -> {
	
	            PharmacyConsumptionSummary consumptionSummary = new PharmacyConsumptionSummary();
	
	            int totalQuantityUsed = 0;
	            int totalQuantityWastated = 0;
	
	            List<PharmacyConsumption> filteredConsumptions = null;
	
	            filteredConsumptions = consumptions.stream().filter(b -> b.getItem().equals(a))
	                    .collect(Collectors.toList());
	          //  consumptionSummary.setDepartment(department);
	            consumptionSummary.setItem(a);
	
	            for (PharmacyConsumption c : filteredConsumptions) {
	                int tempTotalWasted = c.getWastage();
	                int tempTotalQuantityUsed = c.getQuantity();
	                totalQuantityUsed += tempTotalQuantityUsed;
	                totalQuantityWastated += tempTotalWasted;
	                
	
	            }
	           // System.out.println("Total consumption for item "+a.getName()+" is"+totalQuantityUsed);
	
	            consumptionSummary.setTotalQuantityConsumed(totalQuantityUsed);
	            consumptionSummary.setTotalQuantityWasted(totalQuantityWastated);
	
	            aggregateConsumption.add(consumptionSummary);
	        });
	
	        return aggregateConsumption;
	
	    }

	protected List<PharmacyConsumptionSummary> getSummaryFromStockOperation(List<StockOperation> stockOperations,
	        Department department, List<Item> distinctItems) {

		List<PharmacyConsumptionSummary> rConsumptions = new ArrayList<>();
		List<PharmacyConsumptionSummary> aggregateConsumption = new ArrayList<>();

		for (StockOperation stockOperation : stockOperations) {

			rConsumptions.addAll(fillUpItems(stockOperation.getItems(), stockOperation));

		}

		aggregateConsumption = aggregateItems(rConsumptions, department, distinctItems);

		return aggregateConsumption;

	}

	private List<PharmacyConsumptionSummary> aggregateItems(List<PharmacyConsumptionSummary> consumptionSummarys, 
            Department department,List<Item> distinctItems) {
//        distinctItems = consumptionSummarys.stream()
//                .map(ConsumptionSummary::getItem).distinct().collect(Collectors.toList());
        
            System.out.println("The items count is "+distinctItems.size());

        List<PharmacyConsumptionSummary> aggregateConsumption = new ArrayList<>();

        distinctItems.forEach(a -> {
            // assume a department would be selected at all time
            PharmacyConsumptionSummary consumptionSummary = new PharmacyConsumptionSummary();
           // consumptionSummary.setDepartment(department);
            consumptionSummary.setItem(a);
            long itemCount = consumptionSummarys.stream().filter(b -> b.getItem().equals(a))
                    .map(PharmacyConsumptionSummary::getTotalQuantityReceived).mapToInt(Integer::intValue).sum();
            if (itemCount > 0) {
                System.out.println("");
                consumptionSummary.setTotalQuantityReceived((int) itemCount);
            } else {
                consumptionSummary.setTotalQuantityReceived(0);
            }
            aggregateConsumption.add(consumptionSummary);
        });

        return aggregateConsumption;

    }

	private List<PharmacyConsumptionSummary> fillUpItems(Set<StockOperationItem> items, StockOperation stockOperation) {
        List<PharmacyConsumptionSummary> rConsumptions = new ArrayList<>();

        items.stream().forEach(a -> {
            PharmacyConsumptionSummary consumptionSummary = new PharmacyConsumptionSummary();
          //  consumptionSummary.setDepartment(stockOperation.getDepartment());
            consumptionSummary.setItem(a.getItem());
            consumptionSummary.setTotalQuantityReceived(a.getQuantity());
            rConsumptions.add(consumptionSummary);
        });

        return rConsumptions;
    }

	private List<PharmacyConsumption> getConsumptionsByDate(final Date startDate, final Date endDate,
	        PagingInfo paging, final Integer maxResults,
	        Order... orders) {
		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException("The date to search for must be defined.");
		}

		return executeCriteria(PharmacyConsumption.class, paging, new Action1<Criteria>() {
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
	public List<PharmacyConsumption> getByNameFragment(String nameFragment, boolean includeRetired, PagingInfo pagingInfo) {
		return super.getAll(includeRetired, pagingInfo);
	}

	@Override
	public List<PharmacyConsumption> getByNameFragment(String nameFragment, boolean includeRetired) {
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
