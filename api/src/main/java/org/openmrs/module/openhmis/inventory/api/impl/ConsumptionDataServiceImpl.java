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
import org.hibernate.Query;
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
import org.openmrs.module.openhmis.inventory.api.model.ConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockSummary;
import org.openmrs.module.openhmis.inventory.api.model.SearchConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationItem;
import org.openmrs.module.openhmis.inventory.api.model.ViewInvStockonhandPharmacyDispensary;
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

	@Override
    public List<ConsumptionSummary> retrieveConsumptionSummary(List<StockOperation> stockOperations,
            SearchConsumptionSummary searchConsumptionSummary, PagingInfo pagingInfo, List<Item> distinctItems) {
        List<ConsumptionSummary> fromConsumption = null;
        List<ConsumptionSummary> fromReceived = null;
        List<ConsumptionSummary> finalConsumptionSummarys = null;
        List<Consumption> consumptions = new ArrayList<>();
        
        System.out.println("stock operations result: " + stockOperations.size());
        
        fromReceived = getSummaryFromStockOperation(stockOperations,
                searchConsumptionSummary.getDepartment(), distinctItems);
        
        System.out.println("After stock summary: " + fromReceived.size());
        
        consumptions = getConsumptionByConsumptionDate(searchConsumptionSummary.getStartDate(),
                searchConsumptionSummary.getEndDate(), pagingInfo);
        
        System.out.println("stock consumptions result: " + consumptions.size());

        //filter out the Department
        consumptions = consumptions.stream().filter(a -> a.getDepartment().equals(searchConsumptionSummary.getDepartment()))
                .collect(Collectors.toList());
        fromConsumption = getSummaryFromConsumption(consumptions, searchConsumptionSummary.getDepartment(), distinctItems);
        
        System.out.println("stock consumptions aggregate result  : " + fromConsumption.size());
        
        List<ConsumptionSummary> tempConsumptionSummary = new ArrayList<>();
        
        tempConsumptionSummary = mergeSummary(fromConsumption, fromReceived,
                searchConsumptionSummary.getDepartment(), distinctItems);
        
        finalConsumptionSummarys = calculateStockBalance(tempConsumptionSummary);
        
        System.out.println("final consumption summary is " + finalConsumptionSummarys.size());
        
        return finalConsumptionSummarys;
        
    }

	private List<ConsumptionSummary> calculateStockBalance(List<ConsumptionSummary> consumptionSummarys) {

		List<ConsumptionSummary> consumptionSummarysWithBalance = new ArrayList<>();

		for (ConsumptionSummary summary : consumptionSummarys) {
			ConsumptionSummary each = new ConsumptionSummary();
			each.setItem(summary.getItem());
			each.setDepartment(summary.getDepartment());
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

	private List<ConsumptionSummary> mergeSummary(List<ConsumptionSummary> fromConsumption,
            List<ConsumptionSummary> fromReceived, Department department, List<Item> distinctItems) {
        
        List<ConsumptionSummary> mergedConsumptionSummarys = new ArrayList<>();
        
        distinctItems.forEach(a -> {
            ConsumptionSummary each = new ConsumptionSummary();
            //get sum from received
            int sumReceived = fromReceived.stream().filter(b -> b.getItem().equals(a))
                    .map(ConsumptionSummary::getTotalQuantityReceived).findFirst().get();
            each.setTotalQuantityReceived(sumReceived);
            each.setDepartment(department);
            int sumConsumed = fromConsumption.stream().filter(b -> b.getItem().equals(a))
                    .map(ConsumptionSummary::getTotalQuantityConsumed).findFirst().get();
            each.setTotalQuantityConsumed(sumConsumed);
            
            int sumWastage = fromConsumption.stream().filter(b -> b.getItem().equals(a))
                    .map(ConsumptionSummary::getTotalQuantityWasted).findFirst().get();
            
            each.setTotalQuantityWasted(sumWastage);
            
            each.setItem(a);
            mergedConsumptionSummarys.add(each);
            
        });
        
        return mergedConsumptionSummarys;
        
    }

	private List<ConsumptionSummary> getSummaryFromConsumption(List<Consumption> consumptions,
            Department department, List<Item> distinctItems) {
        
        List<ConsumptionSummary> aggregateConsumption = new ArrayList<>();
        
        distinctItems.forEach(a -> {
            
            ConsumptionSummary consumptionSummary = new ConsumptionSummary();
            
            int totalQuantityUsed = 0;
            int totalQuantityWastated = 0;
            
            List<Consumption> filteredConsumptions = null;
            
            filteredConsumptions = consumptions.stream().filter(b -> b.getItem().equals(a))
                    .collect(Collectors.toList());
            consumptionSummary.setDepartment(department);
            consumptionSummary.setItem(a);
            
            for (Consumption c : filteredConsumptions) {
                int tempTotalWasted = c.getWastage() == null ? 0 : c.getWastage();
                int tempTotalQuantityUsed = c.getQuantity() == null ? 0 : c.getQuantity();
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

	protected List<ConsumptionSummary> getSummaryFromStockOperation(List<StockOperation> stockOperations,
	        Department department, List<Item> distinctItems) {

		List<ConsumptionSummary> rConsumptions = new ArrayList<>();
		List<ConsumptionSummary> aggregateConsumption = new ArrayList<>();

		for (StockOperation stockOperation : stockOperations) {

			rConsumptions.addAll(fillUpItems(stockOperation.getItems(), stockOperation));

		}

		aggregateConsumption = aggregateItems(rConsumptions, department, distinctItems);

		return aggregateConsumption;

	}

	private List<ConsumptionSummary> aggregateItems(List<ConsumptionSummary> consumptionSummarys,
            Department department, List<Item> distinctItems) {
//        distinctItems = consumptionSummarys.stream()
//                .map(ConsumptionSummary::getItem).distinct().collect(Collectors.toList());
        
        System.out.println("The items count is " + distinctItems.size());
        
        List<ConsumptionSummary> aggregateConsumption = new ArrayList<>();
        
        distinctItems.forEach(a -> {
            // assume a department would be selected at all time
            ConsumptionSummary consumptionSummary = new ConsumptionSummary();
            consumptionSummary.setDepartment(department);
            consumptionSummary.setItem(a);
            long itemCount = consumptionSummarys.stream().filter(b -> b.getItem().equals(a))
                    .map(ConsumptionSummary::getTotalQuantityReceived).mapToInt(Integer::intValue).sum();
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

	private List<ConsumptionSummary> fillUpItems(Set<StockOperationItem> items, StockOperation stockOperation) {
        List<ConsumptionSummary> rConsumptions = new ArrayList<>();
        
        items.stream().forEach(a -> {
            ConsumptionSummary consumptionSummary = new ConsumptionSummary();
            consumptionSummary.setDepartment(stockOperation.getDepartment());
            consumptionSummary.setItem(a.getItem());
            consumptionSummary.setTotalQuantityReceived(a.getQuantity());
            rConsumptions.add(consumptionSummary);
        });
        
        return rConsumptions;
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
	public int deleteConsumption(Consumption consumption) {
		int query = 0;
		System.out.println("about to execute delete query for consumption: " + consumption.getId());

		try {
			String hql = "delete "
			        + "from Consumption as detail "
			        + "where detail.id = " + consumption.getId();

			query = getRepository().createQuery(hql).executeUpdate();
			System.out.println("Delete Successful? : " + consumption.getId());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return query;
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

	@Override
	public Consumption save(Consumption object) {
		if (!object.getDataSystem().equals("emr")) {
			object.setDataSystem("mobile");
		}
		//subtract quantity from updateable quantity on inv_stockonhand_pharmacy_dispensary table and update
		updateStockOnHandAtDepartment(object);
		return super.save(object); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void updateStockOnHandAtDepartment(Consumption consumption) {
		ViewInvStockonhandPharmacyDispensary vispd = getUpdateableQuantity(consumption);
		int updateableQuantity = vispd.getUpdatableQuantity() - consumption.getQuantity();

		String hql = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
		        + "updatableQuantity = " + updateableQuantity + " "
		        + "where id = " + vispd.getId();

		Query query = getRepository().createQuery(hql);
		int sql = query.executeUpdate();
		System.out.println("Updated Executed: " + sql);
	}

	@Override
	public ViewInvStockonhandPharmacyDispensary getUpdateableQuantity(Consumption consumption) {
		// Create the query
		String hql = "select detail.updatableQuantity as sumQty, detail.id "
		        + "from ViewInvStockonhandPharmacyDispensary as detail "
		        + "where detail.department.id = " + consumption.getDepartment().getId() + " and "
		        + "detail.itemBatch = '" + consumption.getBatchNumber() + "' and "
		        + "detail.item.id = " + consumption.getItem().getId();

		Query query = getRepository().createQuery(hql);
		//List<ViewInvStockonhandPharmacyDispensary> vspd = new ArrayList<ViewInvStockonhandPharmacyDispensary>(list.size());
		ViewInvStockonhandPharmacyDispensary vspd = new ViewInvStockonhandPharmacyDispensary();

		List list = query.list();

		for (Object obj : list) {
			//ViewInvStockonhandPharmacyDispensary> vspd = new ArrayList<ViewInvStockonhandPharmacyDispensary>(list.size());
			Object[] row = (Object[])obj;
			vspd.setUpdatableQuantity((int)row[0]);
			vspd.setId((int)row[1]);
		}
		return vspd;
	}

}
