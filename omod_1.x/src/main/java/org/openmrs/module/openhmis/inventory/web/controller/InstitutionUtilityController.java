/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.IInstitutionDataService;
import org.openmrs.module.openhmis.inventory.api.model.Institution;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MORRISON.I
 */
@Controller(value = "institutionUtility")
@RequestMapping(ModuleWebConstants.MODULE_INSTITUTION_UTILITY_ROOT)
public class InstitutionUtilityController {

	private static final Log LOG = LogFactory.getLog(InstitutionUtilityController.class);

	private IInstitutionDataService iInstitutionDataService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SimpleObject get(@RequestParam(value = "state", required = true) String state,
	        @RequestParam(value = "lga", required = true) String lga) {

		this.iInstitutionDataService = Context.getService(IInstitutionDataService.class);

		SimpleObject result = new SimpleObject();

		try {

			ObjectMapper mapper = new ObjectMapper();

			List<Institution> institutions =
			        iInstitutionDataService.getInstitutionByStateAndLga(state, lga);

			List<Institution> cleanedInstitutions = cleanUpInstitution(institutions);

			result.put("results", cleanedInstitutions);

		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			result.put("error", ex.getMessage());
		}
		System.out.println("About to return results");
		return result;

	}

	private List<Institution> cleanUpInstitution(List<Institution> institutions){
        
            List<Institution> response = new ArrayList<>();
            
            institutions.stream().forEach(a -> {
            a.setChangedBy(null);
            a.setCreator(null);
            
            response.add(a);
            });
            
            return response;
            
        }
}
