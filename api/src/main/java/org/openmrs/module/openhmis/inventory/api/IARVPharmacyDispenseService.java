/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api;

import java.util.Date;
import java.util.List;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.inventory.api.model.ARVPharmacyDispense;

/**
 * @author MORRISON.I
 */
public interface IARVPharmacyDispenseService extends IMetadataDataService<ARVPharmacyDispense> {

	List<ARVPharmacyDispense> getARVs(Date startDate, Date endDate, PagingInfo pagingInfo);

}
