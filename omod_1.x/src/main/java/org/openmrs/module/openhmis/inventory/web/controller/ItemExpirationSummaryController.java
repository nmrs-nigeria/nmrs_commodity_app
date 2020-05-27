/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import java.io.IOException;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.WellKnownOperationTypes;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.webservices.rest.helper.IdgenHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author MORRISON.I
 */
@Controller
@RequestMapping(ModuleWebConstants.ITEM_EXPIRATION_SUMMARY_ROOT)
public class ItemExpirationSummaryController {
	@RequestMapping(method = RequestMethod.GET)
	public void inventory(ModelMap model) throws IOException {

		model.addAttribute("showItemExpirationSummaryLink", Context.getAuthenticatedUser() != null
		        && WellKnownOperationTypes.getAdjustment().userCanProcess(Context.getAuthenticatedUser()));
		model.addAttribute("isOperationNumberAutoGenerated",
		    IdgenHelper.isOperationNumberGenerated());
	}
}
