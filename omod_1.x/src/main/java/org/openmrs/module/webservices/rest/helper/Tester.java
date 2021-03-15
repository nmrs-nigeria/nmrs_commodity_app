/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openmrs.User;
import org.openmrs.module.openhmis.inventory.api.model.Institution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author MORRISON.I
 */
public class Tester {

	private List<NewPharmacyConsumptionSummaryDemo> loadConsumption() {
		List<NewPharmacyConsumptionSummaryDemo> arConsumptionSummarys = new ArrayList<>();

		//        NewPharmacyConsumptionSummaryDemo n = new NewPharmacyConsumptionSummaryDemo();
		//        n.setDrugCategory("ART");
		//        n.setItem("Item1");
		//        n.setTotalQuantityReceived(50);
		//
		//        arConsumptionSummarys.add(n);
		//
		//        n = new NewPharmacyConsumptionSummaryDemo();
		//        n.setDrugCategory("ART");
		//        n.setItem("Item5");
		//        n.setTotalQuantityReceived(230);
		//        arConsumptionSummarys.add(n);
		//
		//        n = new NewPharmacyConsumptionSummaryDemo();
		//        n.setDrugCategory("ART");
		//        n.setItem("Item6");
		//        n.setTotalQuantityReceived(10);
		//        arConsumptionSummarys.add(n);
		//
		//        n = new NewPharmacyConsumptionSummaryDemo();
		//        n.setDrugCategory("OI");
		//        n.setItem("Item2");
		//        n.setTotalQuantityReceived(40);
		//        arConsumptionSummarys.add(n);
		//
		//        n = new NewPharmacyConsumptionSummaryDemo();
		//        n.setDrugCategory("OI");
		//        n.setItem("Item4");
		//        n.setTotalQuantityReceived(550);
		//        arConsumptionSummarys.add(n);
		//
		//        n = new NewPharmacyConsumptionSummaryDemo();
		//        n.setDrugCategory("OI");
		//        n.setItem("Item4");
		//        n.setTotalQuantityReceived(20);
		//        arConsumptionSummarys.add(n);
		//
		//        n = new NewPharmacyConsumptionSummaryDemo();
		//        n.setDrugCategory("STI");
		//        n.setItem("Item3");
		//        n.setTotalQuantityReceived(30);
		//        arConsumptionSummarys.add(n);

		return arConsumptionSummarys;
	}

	private void sumCollection(List<NewPharmacyConsumptionSummaryDemo> input) {

//        input.stream()
//                .collect(Collectors.groupingBy(con -> new Tuple(con.getDrugCategory(), con.getItem()),
//                        Collectors.summingInt(NewPharmacyConsumptionSummaryDemo::getTotalQuantityReceived)))
//                .entrySet().forEach(a -> {
//                System.out.println(a.getKey().drugCategory+" "+a.getKey().item+" "+a.getValue());
//                });
        Map<String, Map<String, Integer>> collect = input.stream()
                .collect(Collectors.groupingBy(NewPharmacyConsumptionSummaryDemo::getDrugCategory,
                        Collectors.groupingBy(NewPharmacyConsumptionSummaryDemo::getItem,
                                Collectors.summingInt(NewPharmacyConsumptionSummaryDemo::getTotalQuantityReceived))
                ));
                
        collect.entrySet().forEach(a -> {
            System.out.println(a);
            a.getValue().entrySet().forEach(b -> {
                System.out.println(b.getKey()+" "+b.getValue());
            });
        });

    }

	public static void main(String[] args) {
		Tester tb = new Tester();
		tb.sumCollection(tb.loadConsumption());
	}

	class Tuple {

		String drugCategory;
		String item;

		Tuple(String drugCategory, String item) {
			this.drugCategory = drugCategory;
			this.item = item;
		}

	}

	class NewPharmacyConsumptionSummaryDemo {

		private Integer totalQuantityReceived;
		private String item;
		private String uuid;
		private String groupUuid;
		private Integer consumptionSummaryId;
		private String drugCategory;

		public Integer getTotalQuantityReceived() {
			return totalQuantityReceived;
		}

		public void setTotalQuantityReceived(Integer totalQuantityReceived) {
			this.totalQuantityReceived = totalQuantityReceived;
		}

		public String getItem() {
			return item;
		}

		public void setItem(String item) {
			this.item = item;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public String getGroupUuid() {
			return groupUuid;
		}

		public void setGroupUuid(String groupUuid) {
			this.groupUuid = groupUuid;
		}

		public Integer getConsumptionSummaryId() {
			return consumptionSummaryId;
		}

		public void setConsumptionSummaryId(Integer consumptionSummaryId) {
			this.consumptionSummaryId = consumptionSummaryId;
		}

		public String getDrugCategory() {
			return drugCategory;
		}

		public void setDrugCategory(String drugCategory) {
			this.drugCategory = drugCategory;
		}

	}

}
