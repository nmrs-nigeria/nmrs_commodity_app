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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.ICrrfReportDataService;
import org.openmrs.module.openhmis.inventory.api.model.ARVDispensedItem;
import org.openmrs.module.openhmis.inventory.api.model.ClosingBalanceUpdateModel;
import org.openmrs.module.openhmis.inventory.api.model.Crrf;
import org.openmrs.module.openhmis.inventory.api.model.NewPharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;

/**
 * @author Toyeeb
 */
public class CrrfReportDataServiceImpl extends BaseMetadataDataServiceImpl<Crrf>
        implements ICrrfReportDataService, IMetadataAuthorizationPrivileges {

	public List<ClosingBalanceUpdateModel> getClosingBalance(String repPeriod, String repYear, String stockroomType) {

		String queryString = "select a from ClosingBalanceUpdateModel " + "a where a.reportingPeriod = :repPeriod "
		        + "and a.reportingYear = :repYear and a.stockroomType = :stockroomType ";

		Query query = getRepository().createQuery(queryString);
		// Query query = getRepository().createQuery(queryString);
		query.setString("repPeriod", repPeriod);
		query.setString("repYear", repYear);
		query.setString("stockroomType", stockroomType);

		List<ClosingBalanceUpdateModel> closingBalanceUpdateModelQuery = (List<ClosingBalanceUpdateModel>)query.list();
		System.out.println("closingBalanceUpdateModelQuery: " + closingBalanceUpdateModelQuery.size());

		List<ClosingBalanceUpdateModel> closingBalanceUpdateModels = new ArrayList<>();

		if (closingBalanceUpdateModelQuery != null && closingBalanceUpdateModelQuery.size() >= 1) {
			for (ClosingBalanceUpdateModel cbu : closingBalanceUpdateModelQuery) {
				ClosingBalanceUpdateModel closingBalanceUpdateModel = new ClosingBalanceUpdateModel();
				closingBalanceUpdateModel.setCalculatedClosingBalance(cbu.getCalculatedClosingBalance());
				closingBalanceUpdateModel.setId(cbu.getId());
				closingBalanceUpdateModel.setItem(cbu.getItem());
				closingBalanceUpdateModel.setPackSize(cbu.getPackSize());
				closingBalanceUpdateModel.setReportingPeriod(cbu.getReportingPeriod());
				closingBalanceUpdateModel.setReportingYear(cbu.getReportingYear());
				closingBalanceUpdateModel.setStockroomType(cbu.getStockroomType());
				closingBalanceUpdateModel.setStrength(cbu.getStrength());
				closingBalanceUpdateModel.setUpdatedClosingBalance(cbu.getUpdatedClosingBalance());
				closingBalanceUpdateModel.setDateCreated(DateUtils.truncate(cbu.getDateCreated(), Calendar.DATE));

				closingBalanceUpdateModels.add(closingBalanceUpdateModel);
			}
		} else {
			ClosingBalanceUpdateModel closingBalanceUpdateModel = new ClosingBalanceUpdateModel();

			closingBalanceUpdateModel.setCalculatedClosingBalance(0);
			closingBalanceUpdateModel.setId(0);
			closingBalanceUpdateModel.setItem(null);
			closingBalanceUpdateModel.setPackSize("");
			closingBalanceUpdateModel.setReportingPeriod("");
			closingBalanceUpdateModel.setReportingYear("");
			closingBalanceUpdateModel.setStockroomType("");
			closingBalanceUpdateModel.setStrength("");
			closingBalanceUpdateModel.setUpdatedClosingBalance(0);
			closingBalanceUpdateModel.setDateCreated(null);

			closingBalanceUpdateModels.add(closingBalanceUpdateModel);

		}

		return closingBalanceUpdateModels;
	}

	@Override
	public String getGetPrivilege() {
		// TODO Auto-generated method stub
		return PrivilegeConstants.VIEW_CONSUMPTIONS;
	}

	@Override
	public String getPurgePrivilege() {
		// TODO Auto-generated method stub
		return PrivilegeConstants.PURGE_CONSUMPTION;
	}

	@Override
	public String getSavePrivilege() {
		// TODO Auto-generated method stub
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	public String getRetirePrivilege() {
		// TODO Auto-generated method stub
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected void validate(Crrf arg0) {
		throw new UnsupportedOperationException("Not supported yet.");

	}

}
