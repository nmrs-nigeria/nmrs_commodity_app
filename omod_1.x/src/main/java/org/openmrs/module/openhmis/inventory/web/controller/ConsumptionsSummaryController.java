/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import java.io.IOException;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author MORRISON.I
 */

@Controller(value = "invConsumptionSummarysController")
@RequestMapping(ModuleWebConstants.CONSUMPTIONS_SUMMARY_ROOT)
public class ConsumptionsSummaryController {
	@RequestMapping(method = RequestMethod.GET)
	public void consumptions(ModelMap model) throws IOException {
		model.addAttribute("modelBase", "openhmis.inventory.consumptionSummary");
	}
}
