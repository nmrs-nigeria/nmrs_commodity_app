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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.commons.api.f.Action1;
import org.openmrs.module.openhmis.inventory.api.IPharmacyConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.model.*;
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
    public List<PharmacyConsumptionSummary>
            retrieveConsumptionSummaryForStockroom(List<StockOperation> receiptStockOperations,
                    List<StockOperation> distributeStockOperations, PagingInfo pagingInfo, List<Item> distinctItems) {
        List<PharmacyConsumptionSummary> fromConsumption = null;
        List<PharmacyConsumptionSummary> fromReceived = null;
        List<PharmacyConsumptionSummary> finalConsumptionSummarys = null;
        List<PharmacyConsumption> consumptions = new ArrayList<>();

        System.out.println("stock operations result: " + receiptStockOperations.size());

        fromReceived = getSummaryFromStockroomStockOperation(receiptStockOperations,
                null, distinctItems);

        System.out.println("After stock summary: " + fromReceived.size());

        fromConsumption = getSummaryFromStockroomStockOperation(distributeStockOperations,
                null, distinctItems);

        List<PharmacyConsumptionSummary> temps = new ArrayList<>();

        fromConsumption.stream().forEach(a -> {
            PharmacyConsumptionSummary temp = a;
            temp.setTotalQuantityConsumed(a.getTotalQuantityReceived());
            temp.setTotalQuantityReceived(0);
            temps.add(temp);
        });

        fromConsumption = temps;

        System.out.println("stock consumptions aggregate result  : " + fromConsumption.size());

        List<PharmacyConsumptionSummary> tempConsumptionSummary = new ArrayList<>();

        tempConsumptionSummary = mergeStockRoomSummary(fromConsumption, fromReceived,
                null, distinctItems);

        finalConsumptionSummarys = calculateStockRoomStockBalance(tempConsumptionSummary);

        System.out.println("final consumption summary is " + finalConsumptionSummarys.size());

        return finalConsumptionSummarys;

    }

	@Override
	public List<CrffOperationsSummary>
	        retrieveConsumptionSummaryForStockroom(List<StockOperation> adjustmentStockOperations,
	                List<StockOperation> transferStockOperations, List<StockOperation> disposedStockOperations,
	                PagingInfo pagingInfo, List<Item> distinctItems) {
		List<CrffOperationsSummary> fromAdjustment = null;
		List<CrffOperationsSummary> fromTransfer = null;
		List<CrffOperationsSummary> fromDisposed = null;
		List<CrffOperationsSummary> finalCrrfOperationSummarys = null;

		final String ADJUSTMENT_KEY = "adjustment";
		final String TRANSFER_KEY = "transfer";
		final String DISPOSED_KEY = "disposed";

		//adjustment
		System.out.println("stock operations result adjustmentStockOperations: " + adjustmentStockOperations.size());
		fromAdjustment = getSummaryFromStockroomStockOperationCrrf(adjustmentStockOperations,
		    null, distinctItems, ADJUSTMENT_KEY);
		System.out.println("After stock summary fromAdjustment: " + fromAdjustment.size());

		//transfer
		System.out.println("stock operations result transferStockOperations: " + transferStockOperations.size());
		fromTransfer = getSummaryFromStockroomStockOperationCrrf(transferStockOperations,
		    null, distinctItems, TRANSFER_KEY);
		System.out.println("After stock summary fromTransfer: " + fromTransfer.size());

		//disposed
		System.out.println("stock operations result disposedStockOperations: " + disposedStockOperations.size());
		fromDisposed = getSummaryFromStockroomStockOperationCrrf(disposedStockOperations,
		    null, distinctItems, DISPOSED_KEY);
		System.out.println("After stock summary fromDisposed: " + fromDisposed.size());
		
		System.out.println("fromAdjustment  : " + fromAdjustment.size());

		List<CrffOperationsSummary> tempsTransfer = new ArrayList<>();		
		fromTransfer.stream().forEach(a -> {
			CrffOperationsSummary tempT = a;
			tempT.setTotalPositiveAdjustment(0);
			tempT.setTotalNegativeAdjustment(a.getTotalNegativeAdjustment());
			tempT.setTotalLossDamagesExpires(0);
			
			System.out.println("fromTransfer getTotalPositiveAdjustment : " 
			+ (a.getTotalPositiveAdjustment() == null ? 0 : a.getTotalPositiveAdjustment()));
			System.out.println("fromTransfer getTotalNegativeAdjustment : " 
			+ (a.getTotalNegativeAdjustment() == null ? 0 : a.getTotalNegativeAdjustment()));
			System.out.println("fromTransfer getTotalLossDamagesExpires  : " 
			+ (a.getTotalLossDamagesExpires() == null ? 0 : a.getTotalLossDamagesExpires()));
			
			tempsTransfer.add(tempT);
		});		
		fromTransfer = tempsTransfer;
		System.out.println("fromTransfer  : " + fromTransfer.size());
		
		
		List<CrffOperationsSummary> tempsDisposed = new ArrayList<>();		
		fromDisposed.stream().forEach(a -> {
			CrffOperationsSummary tempD = a;
			tempD.setTotalPositiveAdjustment(0);
			tempD.setTotalNegativeAdjustment(0);
			tempD.setTotalLossDamagesExpires(a.getTotalLossDamagesExpires());
			
			System.out.println("fromDisposed getTotalPositiveAdjustment : " 
			+ (a.getTotalPositiveAdjustment() == null ? 0 : a.getTotalPositiveAdjustment()));
			System.out.println("fromDisposed getTotalNegativeAdjustment : " 
			+ (a.getTotalNegativeAdjustment() == null ? 0 : a.getTotalNegativeAdjustment()));
			System.out.println("fromDisposed getTotalLossDamagesExpires  : "
			+ (a.getTotalLossDamagesExpires() == null ? 0 : a.getTotalLossDamagesExpires()));
		
			tempsDisposed.add(tempD);
		});		
		fromDisposed = tempsDisposed;
		System.out.println("fromDisposed  : " + fromDisposed.size());
		

		List<CrffOperationsSummary> tempConsumptionSummary = new ArrayList<>();

		tempConsumptionSummary = mergeStockRoomSummaryCrrf(fromAdjustment, fromTransfer,
		    fromDisposed, null, distinctItems);

		finalCrrfOperationSummarys = tempConsumptionSummary;

		System.out.println("final finalCrrfOperationSummarys summary is: " + finalCrrfOperationSummarys.size());

		return finalCrrfOperationSummarys;

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
        consumptions = consumptions.stream().filter(a -> a.getDepartment()
                .equals(searchConsumptionSummary.getDepartment()))
                .collect(Collectors.toList());
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

	private List<PharmacyConsumptionSummary>
	        calculateStockRoomStockBalance(List<PharmacyConsumptionSummary> consumptionSummarys) {

		List<PharmacyConsumptionSummary> consumptionSummarysWithBalance = new ArrayList<>();

		for (PharmacyConsumptionSummary summary : consumptionSummarys) {
			PharmacyConsumptionSummary each = new PharmacyConsumptionSummary();
			each.setItem(summary.getItem());
			each.setDepartment(summary.getDepartment());
			each.setTotalQuantityConsumed(summary.getTotalQuantityConsumed());
			each.setTotalQuantityReceived(summary.getTotalQuantityReceived());
			each.setTotalQuantityWasted(summary.getTotalQuantityWasted());
			if (each.getTotalQuantityReceived() > 0) {
				each.setStockBalance(each.getTotalQuantityReceived()
				        - (each.getTotalQuantityConsumed()));
			} else {
				each.setStockBalance(0);
			}

			consumptionSummarysWithBalance.add(each);
		}

		return consumptionSummarysWithBalance;

	}

	private List<PharmacyConsumptionSummary> calculateStockBalance(List<PharmacyConsumptionSummary> consumptionSummarys) {

		List<PharmacyConsumptionSummary> consumptionSummarysWithBalance = new ArrayList<>();

		for (PharmacyConsumptionSummary summary : consumptionSummarys) {
			PharmacyConsumptionSummary each = new PharmacyConsumptionSummary();
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

	private List<PharmacyConsumptionSummary> mergeSummary(List<PharmacyConsumptionSummary> fromConsumption,
            List<PharmacyConsumptionSummary> fromReceived, Department department, List<Item> distinctItems) {

        List<PharmacyConsumptionSummary> mergedConsumptionSummarys = new ArrayList<>();

        distinctItems.forEach(a -> {
            PharmacyConsumptionSummary each = new PharmacyConsumptionSummary();
            //get sum from received
            int sumReceived = fromReceived.stream().filter(b -> b.getItem().equals(a))
                    .map(PharmacyConsumptionSummary::getTotalQuantityReceived).findFirst().get();
            each.setTotalQuantityReceived(sumReceived);
            each.setDepartment(department);
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

	private List<PharmacyConsumptionSummary> mergeStockRoomSummary(List<PharmacyConsumptionSummary> fromConsumption,
            List<PharmacyConsumptionSummary> fromReceived, Department department, List<Item> distinctItems) {

        List<PharmacyConsumptionSummary> mergedConsumptionSummarys = new ArrayList<>();

        distinctItems.forEach(a -> {
            PharmacyConsumptionSummary each = new PharmacyConsumptionSummary();
            //get sum from received
            int sumReceived = fromReceived.stream().filter(b -> b.getItem().equals(a))
                    .map(PharmacyConsumptionSummary::getTotalQuantityReceived).findFirst().get();
            each.setTotalQuantityReceived(sumReceived);
            each.setDepartment(department);
            int sumConsumed = fromConsumption.stream().filter(b -> b.getItem().equals(a))
                    .map(PharmacyConsumptionSummary::getTotalQuantityConsumed).findFirst().get();
            each.setTotalQuantityConsumed(sumConsumed);

//            int sumWastage = fromConsumption.stream().filter(b -> b.getItem().equals(a))
//                    .map(PharmacyConsumptionSummary::getTotalQuantityWasted).findFirst().get();

            each.setTotalQuantityWasted(0);

            each.setItem(a);
            mergedConsumptionSummarys.add(each);

        });

        return mergedConsumptionSummarys;

    }

	private List<CrffOperationsSummary> mergeStockRoomSummaryCrrf(List<CrffOperationsSummary> fromAdjustment,
            List<CrffOperationsSummary> fromTransfer, List<CrffOperationsSummary> fromDisposed, 
            Department department, List<Item> distinctItems) {

        List<CrffOperationsSummary> mergedConsumptionSummarysCrrf = new ArrayList<>();

        distinctItems.forEach(a -> {
        	CrffOperationsSummary each = new CrffOperationsSummary();
        	
            //get sum from adjustment
            int sumAdjusted = fromAdjustment.stream().filter(b -> b.getItem().equals(a))
                    .map(CrffOperationsSummary::getTotalPositiveAdjustment).findFirst().get();
            each.setTotalPositiveAdjustment(sumAdjusted);
            
            //get sum from transfer
            int sumTransfer = fromTransfer.stream().filter(b -> b.getItem().equals(a))
                    .map(CrffOperationsSummary::getTotalNegativeAdjustment).findFirst().get();
            each.setTotalNegativeAdjustment(sumTransfer);
            
            //get sum from disposed
            int sumDisposed = fromDisposed.stream().filter(b -> b.getItem().equals(a))
                    .map(CrffOperationsSummary::getTotalLossDamagesExpires).findFirst().get();
            each.setTotalLossDamagesExpires(sumDisposed);

            each.setItem(a);
            mergedConsumptionSummarysCrrf.add(each);

        });

        return mergedConsumptionSummarysCrrf;

    }

	private List<PharmacyConsumptionSummary> getSummaryFromConsumption(List<PharmacyConsumption> consumptions,
            Department department, List<Item> distinctItems) {

        List<PharmacyConsumptionSummary> aggregateConsumption = new ArrayList<>();

        distinctItems.forEach(a -> {

            PharmacyConsumptionSummary consumptionSummary = new PharmacyConsumptionSummary();

            int totalQuantityUsed = 0;
            int totalQuantityWastated = 0;

            List<PharmacyConsumption> filteredConsumptions = null;

            filteredConsumptions = consumptions.stream().filter(b -> b.getItem().equals(a))
                    .collect(Collectors.toList());
            consumptionSummary.setDepartment(department);
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

	protected List<PharmacyConsumptionSummary>
	        getSummaryFromStockroomStockOperation(List<StockOperation> stockOperations,
	                Department department, List<Item> distinctItems) {

		List<PharmacyConsumptionSummary> rConsumptions = new ArrayList<>();
		List<PharmacyConsumptionSummary> aggregateConsumption = new ArrayList<>();

		for (StockOperation stockOperation : stockOperations) {

			rConsumptions.addAll(fillUpStockroomItems(stockOperation.getItems(), stockOperation));

		}

		aggregateConsumption = aggregateItems(rConsumptions, department, distinctItems);

		return aggregateConsumption;

	}

	protected List<CrffOperationsSummary>
	        getSummaryFromStockroomStockOperationCrrf(List<StockOperation> stockOperations,
	                Department department, List<Item> distinctItems, String operationTypeKey) {

		List<CrffOperationsSummary> aConsumptions = new ArrayList<>();
		List<CrffOperationsSummary> aggregateConsumption = new ArrayList<>();

		for (StockOperation stockOperation : stockOperations) {

			if (operationTypeKey.equalsIgnoreCase("adjustment")) {
				aConsumptions.addAll(fillUpStockroomItemsCrrf(stockOperation.getItems(),
				    stockOperation, operationTypeKey));
			}
			if (operationTypeKey.equalsIgnoreCase("transfer")) {
				aConsumptions.addAll(fillUpStockroomItemsCrrfTransfer(stockOperation.getItems(),
				    stockOperation, operationTypeKey));
			}
			if (operationTypeKey.equalsIgnoreCase("disposed")) {
				aConsumptions.addAll(fillUpStockroomItemsCrrfDisposed(stockOperation.getItems(),
				    stockOperation, operationTypeKey));
			}

		}
		System.out.println("aConsumptions Size : " + operationTypeKey + " " + aConsumptions.size());

		if (operationTypeKey.equalsIgnoreCase("adjustment")) {
			aggregateConsumption = aggregateItemsCrrf(aConsumptions, department, distinctItems);
		}
		if (operationTypeKey.equalsIgnoreCase("transfer")) {
			aggregateConsumption = aggregateItemsCrrfTransfer(aConsumptions, department, distinctItems);
		}
		if (operationTypeKey.equalsIgnoreCase("disposed")) {
			aggregateConsumption = aggregateItemsCrrfDisposed(aConsumptions, department, distinctItems);
		}

		return aggregateConsumption;

	}

	private List<PharmacyConsumptionSummary> aggregateItems(List<PharmacyConsumptionSummary> consumptionSummarys,
            Department department, List<Item> distinctItems) {
//        distinctItems = consumptionSummarys.stream()
//     .map(ConsumptionSummary::getItem).distinct().collect(Collectors.toList());

        System.out.println("The items count is " + distinctItems.size());

        List<PharmacyConsumptionSummary> aggregateConsumption = new ArrayList<>();

        distinctItems.forEach(a -> {
            // assume a department would be selected at all time
            PharmacyConsumptionSummary consumptionSummary = new PharmacyConsumptionSummary();
            consumptionSummary.setDepartment(department);
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

	private List<CrffOperationsSummary> aggregateItemsCrrf(List<CrffOperationsSummary> consumptionSummarys,
            Department department, List<Item> distinctItems) {

        System.out.println("The items count is " + distinctItems.size());

        List<CrffOperationsSummary> aggregateConsumption = new ArrayList<>();

        distinctItems.forEach(a -> {
            // assume a department would be selected at all time
        	CrffOperationsSummary consumptionSummary = new CrffOperationsSummary();
            consumptionSummary.setItem(a);
            //positive
            long itemCount = consumptionSummarys.stream().filter(b -> b.getItem().equals(a))
                    .map(CrffOperationsSummary::getTotalPositiveAdjustment).mapToInt(Integer::intValue).sum();
            if (itemCount > 0) {
                System.out.println("");
                consumptionSummary.setTotalPositiveAdjustment((int) itemCount);
            } else {
                consumptionSummary.setTotalPositiveAdjustment(0);
            }
            
            aggregateConsumption.add(consumptionSummary);
        });

        return aggregateConsumption;

    }

	private List<CrffOperationsSummary> aggregateItemsCrrfTransfer(List<CrffOperationsSummary> consumptionSummarys,
            Department department, List<Item> distinctItems) {

        System.out.println("The items count is " + distinctItems.size());

        List<CrffOperationsSummary> aggregateConsumption = new ArrayList<>();

        distinctItems.forEach(a -> {
            // assume a department would be selected at all time
        	CrffOperationsSummary consumptionSummary = new CrffOperationsSummary();
            consumptionSummary.setItem(a);
             
            //negative
            long itemCount = consumptionSummarys.stream().filter(b -> b.getItem().equals(a))
                    .map(CrffOperationsSummary::getTotalNegativeAdjustment).mapToInt(Integer::intValue).sum();
            if (itemCount > 0) {
                System.out.println("");
                consumptionSummary.setTotalNegativeAdjustment((int) itemCount);
            } else {
                consumptionSummary.setTotalNegativeAdjustment(0);
            }
            
            aggregateConsumption.add(consumptionSummary);
        });

        return aggregateConsumption;

    }

	private List<CrffOperationsSummary> aggregateItemsCrrfDisposed(List<CrffOperationsSummary> consumptionSummarys,
            Department department, List<Item> distinctItems) {

        System.out.println("The items count is " + distinctItems.size());

        List<CrffOperationsSummary> aggregateConsumption = new ArrayList<>();

        distinctItems.forEach(a -> {
            // assume a department would be selected at all time
        	CrffOperationsSummary consumptionSummary = new CrffOperationsSummary();
            consumptionSummary.setItem(a);
            //losses
            long itemCount = consumptionSummarys.stream().filter(b -> b.getItem().equals(a))
                    .map(CrffOperationsSummary::getTotalLossDamagesExpires).mapToInt(Integer::intValue).sum();
            if (itemCount > 0) {
                System.out.println("");
                consumptionSummary.setTotalLossDamagesExpires((int) itemCount);
            } else {
                consumptionSummary.setTotalLossDamagesExpires(0);
            }
            
            aggregateConsumption.add(consumptionSummary);
        });

        return aggregateConsumption;

    }

	private List<PharmacyConsumptionSummary> fillUpItems(Set<StockOperationItem> items, StockOperation stockOperation) {
        List<PharmacyConsumptionSummary> rConsumptions = new ArrayList<>();

        items.stream().forEach(a -> {
            PharmacyConsumptionSummary consumptionSummary = new PharmacyConsumptionSummary();
            consumptionSummary.setDepartment(stockOperation.getDepartment());
            consumptionSummary.setItem(a.getItem());
            consumptionSummary.setTotalQuantityReceived(a.getQuantity());
            rConsumptions.add(consumptionSummary);
        });

        return rConsumptions;
    }

	private List<PharmacyConsumptionSummary> fillUpStockroomItems(Set<StockOperationItem> items, 
            StockOperation stockOperation) {
        List<PharmacyConsumptionSummary> rConsumptions = new ArrayList<>();

        items.stream().forEach(a -> {
            PharmacyConsumptionSummary consumptionSummary = new PharmacyConsumptionSummary();
            // consumptionSummary.setDepartment(stockOperation.getDepartment());
            consumptionSummary.setItem(a.getItem());
            consumptionSummary.setTotalQuantityReceived(a.getQuantity());
            rConsumptions.add(consumptionSummary);
        });

        return rConsumptions;
    }

	private List<CrffOperationsSummary> fillUpStockroomItemsCrrf(Set<StockOperationItem> items, 
            StockOperation stockOperation, String operationTypeKey) {
        List<CrffOperationsSummary> aConsumptions = new ArrayList<>();

        items.stream().forEach(a -> {
        	CrffOperationsSummary consumptionSummary = new CrffOperationsSummary();
            consumptionSummary.setItem(a.getItem());
            consumptionSummary.setTotalPositiveAdjustment(a.getQuantity());
            aConsumptions.add(consumptionSummary);
        });

        return aConsumptions;
    }

	private List<CrffOperationsSummary> fillUpStockroomItemsCrrfTransfer(Set<StockOperationItem> items, 
            StockOperation stockOperation, String operationTypeKey) {
        List<CrffOperationsSummary> aConsumptions = new ArrayList<>();

        items.stream().forEach(a -> {
        	CrffOperationsSummary consumptionSummary = new CrffOperationsSummary();
            consumptionSummary.setItem(a.getItem());
           	consumptionSummary.setTotalNegativeAdjustment(a.getQuantity());
            aConsumptions.add(consumptionSummary);
        });

        return aConsumptions;
    }

	private List<CrffOperationsSummary> fillUpStockroomItemsCrrfDisposed(Set<StockOperationItem> items, 
            StockOperation stockOperation, String operationTypeKey) {
        List<CrffOperationsSummary> aConsumptions = new ArrayList<>();

        items.stream().forEach(a -> {
        	CrffOperationsSummary consumptionSummary = new CrffOperationsSummary();
            consumptionSummary.setItem(a.getItem());        
            consumptionSummary.setTotalLossDamagesExpires(a.getQuantity());           
            aConsumptions.add(consumptionSummary);
        });

        return aConsumptions;
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

	///Added by Tobechi
	@Override
	public PharmacyConsumption save(PharmacyConsumption object) {
		//subtract quantity from updateable quantity on inv_stockonhand_pharmacy_dispensary table and update
		updateStockOnHandAtDepartment(object);
		return super.save(object); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void updateStockOnHandAtDepartment(PharmacyConsumption consumption) {
		ViewInvStockonhandPharmacyDispensary vispd = getUpdateableQuantity(consumption);
		int updateableQuantity = 0;
		if (consumption.getWastage() == null) {
			updateableQuantity = vispd.getUpdatableQuantity() - consumption.getQuantity();
		} else {
			updateableQuantity = vispd.getUpdatableQuantity() - (consumption.getQuantity() + consumption.getWastage());
		}
		String hql = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
		        + "updatableQuantity = " + updateableQuantity + " "
		        + "where id = " + vispd.getId();

		Query query = getRepository().createQuery(hql);
		int sql = query.executeUpdate();
		System.out.println("Updated Executed: " + sql);
	}

	@Override
	public ViewInvStockonhandPharmacyDispensary getUpdateableQuantity(PharmacyConsumption consumption) {
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
