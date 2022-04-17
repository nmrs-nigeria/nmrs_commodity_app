<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("openhmis.inventory.admin.crrfReports") ])

//include css
ui.includeCss("openhmis.commons", "bootstrap.css")
ui.includeCss("openhmis.inventory", "extCss/custom.css")
ui.includeCss("openhmis.inventory", "extCss/dataTables.bootstrap.css")
    

//include js
ui.includeJavascript("openhmis.inventory", "crrfReports/configs/extJs/jquery-3.5.1.js")
ui.includeJavascript("openhmis.inventory", "crrfReports/configs/extJs/bootstrap.min.js")
ui.includeJavascript("openhmis.inventory", "crrfReports/configs/extJs/jquery.csv.min.js")


    ui.includeJavascript("openhmis.inventory", "crrfReports/configs/extJs/jquery.table2excel.js")

%>

<script type="text/javascript" xmlns="http://www.w3.org/1999/html">
    var breadcrumbs = [
    { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
    {
            label: "${ ui.message("openhmis.inventory.page")}" ,
            link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
    },
    {
            label: "${ ui.message("openhmis.inventory.manage.pharmacy.dashboard")}",
    link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/pharmacyInventoryDashboard.page'
    },
    {
            label: "${ ui.message("openhmis.inventory.admin.crrfReports")}",
    link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/ndrExtraction/entities.page#/'
    }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
</script>

<h1>COMBINED REPORT AND REQUISITION FORM (CRRF) - Antiretroviral and OIs</h1>

<div id="crrf-list-table"></div>
<div id="crrfbody-list-table" style="overflow: scroll"></div>
<div id="crrfbodycomment-list-table"></div>

<script type="application/javascript">
 	var jq = jQuery;
    const dataURL = JSON.parse(localStorage.getItem("preview_url"));   
    console.log("dataURL: " + dataURL);     
     
    displayDataBody();
    
    function calculateFunction(id){
    	var arrayS = id.split("_");
    	var packSizeId = arrayS[0];
    	var packSizeValue = document.getElementById('' + packSizeId).value;
    	if(packSizeValue == "" || packSizeValue <= 0){
    		packSizeValue = 1;
    	}    
    	
        var receivedId = arrayS[1];
        var receivedQty = arrayS[2];
        var finalReceived = parseFloat(receivedQty) / parseFloat(packSizeValue);
        var finalReceivedRounded = parseFloat(finalReceived).toFixed(1);
    	document.getElementById('' + receivedId).innerHTML = finalReceivedRounded;
          
        var beginingBalanceId = arrayS[3];
        var beginingBalanceQty = arrayS[4];
        var finalbeginingBalance = parseFloat(beginingBalanceQty) / parseFloat(packSizeValue);
        var finalbeginingBalanceRounded = parseFloat(finalbeginingBalance).toFixed(1);
    	document.getElementById('' + beginingBalanceId).innerHTML = finalbeginingBalanceRounded;
        
        var dispensedId = arrayS[5];
        var dispensedQty = arrayS[6];
        var finaldispensed = parseFloat(dispensedQty) / parseFloat(packSizeValue);
        var finaldispensedRounded = parseFloat(finaldispensed).toFixed(1);
    	document.getElementById('' + dispensedId).innerHTML = finaldispensedRounded;
        
        var positiveAdjustmentId = arrayS[7];
        var positiveAdjustmentQty = arrayS[8];
        var finalpositiveAdjustment = parseFloat(positiveAdjustmentQty) / parseFloat(packSizeValue);
        var finalpositiveAdjustmentRounded = parseFloat(finalpositiveAdjustment).toFixed(1);
    	document.getElementById('' + positiveAdjustmentId).innerHTML = finalpositiveAdjustmentRounded;
        
        var negativeAdjustmentId = arrayS[9];
        var negativeAdjustmentQty = arrayS[10];
        var finalnegativeAdjustment = parseFloat(negativeAdjustmentQty) / parseFloat(packSizeValue);
        var finalnegativeAdjustmentRounded = parseFloat(finalnegativeAdjustment).toFixed(1);
    	document.getElementById('' + negativeAdjustmentId).innerHTML = finalnegativeAdjustmentRounded;
        
        var receivedLossesId = arrayS[11];
        var receivedLossesQty = arrayS[12];
        var finalreceivedLosses = parseFloat(receivedLossesQty) / parseFloat(packSizeValue);
        var finalreceivedLossesRounded = parseFloat(finalreceivedLosses).toFixed(1);
    	document.getElementById('' + receivedLossesId).innerHTML = finalreceivedLossesRounded;
        
        var physicalCountId = arrayS[13];
        var physicalCountQty = arrayS[14];
        var finalphysicalCount = parseFloat(physicalCountQty) / parseFloat(packSizeValue);
        var finalphysicalCountRounded = parseFloat(finalReceived).toFixed(1);
    	document.getElementById('' + physicalCountId).innerHTML = finalphysicalCountRounded;
        
        var maximumStockId = arrayS[15];
        var maximumStockQty = arrayS[16];
        var finalmaximumStock = parseFloat(maximumStockQty) / parseFloat(packSizeValue);
        var finalmaximumStockRounded = parseFloat(finalmaximumStock).toFixed(1);
    	document.getElementById('' + maximumStockId).innerHTML = finalmaximumStockRounded;
        
        var quantityOrderId = arrayS[17];
        var quantityOrderQty = arrayS[18];
        var finalquantityOrder = parseFloat(quantityOrderQty) / parseFloat(packSizeValue);
        var finalquantityOrderRounded = parseFloat(finalquantityOrder).toFixed(1);
    	document.getElementById('' + quantityOrderId).innerHTML = finalquantityOrderRounded;   		
        
    }
    

    function displayDataBody() {
        var rsd = dataURL.reportingPeriodStart.split("T");
        var repDateStart = rsd[0];
        var red = dataURL.reportingPeriodEnd.split("T");
        var repDateEnd = red[0];
        var dp = dataURL.datePrepared.split("T");
        var repDatePrepared = dp[0];
        var contents = "";
        contents += "<table id='fileexport1' class='table table-striped table-condensed' style='font-size: 12px; width: 100%;'>";
        contents += "<thead>";

        contents += "<tr><th>Facility Name: </th><td colspan='4'>" + dataURL.facilityName + "</td><th colspan='3'>Reporting Period Start: </th><td colspan='6'>" + repDateStart + "</td></tr>";
        contents += "<tr><th>Facility Code: </th><td colspan='4'>" + dataURL.facilityCode + "</th><th colspan='3'>Reporting Period End: </th><td colspan='2'>" + repDateEnd + "</td><th colspan='3'>Maximum Stock Level: </th><td>4 Months</td></tr>";
        contents += "<tr><th>LGA: </th><td colspan='4'>" + dataURL.lga + "</td><th colspan='3'>Date Prepared: </th><td colspan='2'>" + repDatePrepared + "</td><th colspan='3'>Minimum Stock Level:</th><td> 2 Months</td></tr>";
        contents += "<tr><th>State: </th><td colspan='14'>" + dataURL.state + "</td></tr>";

        contents += "<tr><th style='text-align: center;' colspan='14'>Antiretroviral Drugs</th></tr>";
        contents += "<tr><th style='text-align: center;' colspan='9'>REPORT</th><th style='text-align: center;' colspan='5'>REQUISITION</th></tr>";

        contents += "<tr><th>Drugs</th><th>Strength</th><th>Pack Size</th><th>Basic Unit</th><th>Beginning Balance</th><th>Quantity Received</th><th>Quantity Dispensed</th>";
        contents += "<th>Positive Adjustments</th><th>Negative Adjustments</th><th>Losses (Damages/Expiries)</th><th>Physical Count</th>";
        contents += "<th>Maximum Stock Quantity</th><th>Quantity to Order</th><th>Remarks</th></tr>";
        contents += "</thead><tbody>";
        
        contents += "<tr><td colspan='13' style='font-weight: bold'>Adult Regimen</td></tr>";
        adult = dataURL.crrfAdultRegimenCategory;     
        for (i = 0; i < adult.length; i++) {
          	var adultElement = adult[i];
          	var packsizeadult = "packsizeadult" + i;
          	var qtyReceivedId = "packsizeadult" + i + "r";
          	var qtyBeginingBalanceId = "packsizeadult" + i + "bb";
          	var qtyDispensedId = "packsizeadult" + i + "d";
          	var qtyPositiveAdjustmentId = "packsizeadult" + i + "pa";
          	var qtyNegativeAdjustmentId = "packsizeadult" + i + "na";
          	var qtyReceivedLossesId = "packsizeadult" + i + "l";
          	var qtyPhysicalCountId = "packsizeadult" + i + "pc";
          	var qtyMaximumStockId = "packsizeadult" + i + "ms";
          	var qtyQuantityOrderId = "packsizeadult" + i + "qo";
          	
          	var final = packsizeadult + "_" + qtyReceivedId + "_" + adultElement.quantityReceived +  "_" + qtyBeginingBalanceId + "_" + adultElement.beginningBalance + "_" + qtyDispensedId + "_" + adultElement.quantityDispensed + "_" + qtyPositiveAdjustmentId + "_" + adultElement.positiveAdjustments + "_" + qtyNegativeAdjustmentId + "_" + adultElement.negativeAdjustments + "_" + qtyReceivedLossesId + "_" + adultElement.lossesdDamagesExpiries +  "_" + qtyPhysicalCountId + "_" + adultElement.physicalCount + "_" + qtyMaximumStockId + "_" + adultElement.maximumStockQuantity +  "_" + qtyQuantityOrderId + "_" + adultElement.quantityToOrder;
          	
         	contents += "<tr><td>" + adultElement.drugs +  "</td><td>" + adultElement.strength +  "</td><td><input type='text' size='5' id='" + packsizeadult + "' onkeyup=\"calculateFunction('" + final + "')\" /></td><td>" + adultElement.basicUnit +  "</td><td id='" + qtyBeginingBalanceId + "'>" + adultElement.beginningBalance +  "</td>";
         	contents += "<td id='" + qtyReceivedId + "'>" + adultElement.quantityReceived +  "</td><td id='" + qtyDispensedId + "'>" + adultElement.quantityDispensed +  "</td><td id='" + qtyPositiveAdjustmentId + "'>" + adultElement.positiveAdjustments +  "</td>";
         	contents += "<td id='" + qtyNegativeAdjustmentId + "'>" + adultElement.negativeAdjustments +  "</td><td id='" + qtyReceivedLossesId + "'>" + adultElement.lossesdDamagesExpiries +  "</td><td id='" + qtyPhysicalCountId + "'>" + adultElement.physicalCount +  "</td>";
         	contents += "<td id='" + qtyMaximumStockId + "'>" + adultElement.maximumStockQuantity +  "</td><td id='" + qtyQuantityOrderId + "'>" + adultElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
         
        contents += "<tr><td colspan='12' style='font-weight: bold'>Paediatric Regimen</td></tr>";
        paediatric = dataURL.crrfPediatricRegimenCategory;     
        for (j = 0; j < paediatric.length; j++) {
          	var paediatricElement = paediatric[j];
          	
          	var packsizepaediatric = "packsizepaediatric" + j;
          	var qtyReceivedId = "packsizepaediatric" + j + "r";
          	var qtyBeginingBalanceId = "packsizepaediatric" + j + "bb";
          	var qtyDispensedId = "packsizepaediatric" + j + "d";
          	var qtyPositiveAdjustmentId = "packsizepaediatric" + j + "pa";
          	var qtyNegativeAdjustmentId = "packsizepaediatric" + j + "na";
          	var qtyReceivedLossesId = "packsizepaediatric" + j + "l";
          	var qtyPhysicalCountId = "packsizepaediatric" + j + "pc";
          	var qtyMaximumStockId = "packsizepaediatric" + j + "ms";
          	var qtyQuantityOrderId = "packsizepaediatric" + j + "qo";
          	
          	var finalpaediatric = packsizepaediatric + "_" + qtyReceivedId + "_" + paediatricElement.quantityReceived +  "_" + qtyBeginingBalanceId + "_" + paediatricElement.beginningBalance + "_" + qtyDispensedId + "_" + paediatricElement.quantityDispensed + "_" + qtyPositiveAdjustmentId + "_" + paediatricElement.positiveAdjustments + "_" + qtyNegativeAdjustmentId + "_" + paediatricElement.negativeAdjustments + "_" + qtyReceivedLossesId + "_" + paediatricElement.lossesdDamagesExpiries +  "_" + qtyPhysicalCountId + "_" + paediatricElement.physicalCount + "_" + qtyMaximumStockId + "_" + paediatricElement.maximumStockQuantity +  "_" + qtyQuantityOrderId + "_" + paediatricElement.quantityToOrder;
  	
         	contents += "<tr><td>" + paediatricElement.drugs +  "</td><td>" + paediatricElement.strength +  "</td><td><input type='text' size='5' id='" + packsizepaediatric + "' onkeyup=\"calculateFunction('" + finalpaediatric + "')\" /></td><td>" + paediatricElement.basicUnit +  "</td><td id='" + qtyBeginingBalanceId + "'>" + paediatricElement.beginningBalance +  "</td>";
         	contents += "<td id='" + qtyReceivedId + "'>" + paediatricElement.quantityReceived +  "</td><td id='" + qtyDispensedId + "'>" + paediatricElement.quantityDispensed +  "</td><td id='" + qtyPositiveAdjustmentId + "'>" + paediatricElement.positiveAdjustments +  "</td>";
         	contents += "<td id='" + qtyNegativeAdjustmentId + "'>" + paediatricElement.negativeAdjustments +  "</td><td id='" + qtyReceivedLossesId + "'>" + paediatricElement.lossesdDamagesExpiries +  "</td><td id='" + qtyPhysicalCountId + "'>" + paediatricElement.physicalCount +  "</td>";
         	contents += "<td id='" + qtyMaximumStockId + "'>" + paediatricElement.maximumStockQuantity +  "</td><td id='" + qtyQuantityOrderId + "'>" + paediatricElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
         
        contents += "<tr><td colspan='12' style='font-weight: bold'>Opportunistic Infections Drugs</td></tr>";
        oi = dataURL.crrfOIRegimenCategory;     
        for (k = 0; k < oi.length; k++) {
          	var oiElement = oi[k];

            var packsizeoi = "packsizeoi" + k;
          	var qtyReceivedId = "packsizeoi" + k + "r";
          	var qtyBeginingBalanceId = "packsizeoi" + k + "bb";
          	var qtyDispensedId = "packsizeoi" + k + "d";
          	var qtyPositiveAdjustmentId = "packsizeoi" + k + "pa";
          	var qtyNegativeAdjustmentId = "packsizeoi" + k + "na";
          	var qtyReceivedLossesId = "packsizeoi" + k + "l";
          	var qtyPhysicalCountId = "packsizeoi" + k + "pc";
          	var qtyMaximumStockId = "packsizeoi" + k + "ms";
          	var qtyQuantityOrderId = "packsizeoi" + k + "qo";
          	
          	var finaloi = packsizeoi + "_" + qtyReceivedId + "_" + oiElement.quantityReceived +  "_" + qtyBeginingBalanceId + "_" + oiElement.beginningBalance + "_" + qtyDispensedId + "_" + oiElement.quantityDispensed + "_" + qtyPositiveAdjustmentId + "_" + oiElement.positiveAdjustments + "_" + qtyNegativeAdjustmentId + "_" + oiElement.negativeAdjustments + "_" + qtyReceivedLossesId + "_" + oiElement.lossesdDamagesExpiries +  "_" + qtyPhysicalCountId + "_" + oiElement.physicalCount + "_" + qtyMaximumStockId + "_" + oiElement.maximumStockQuantity +  "_" + qtyQuantityOrderId + "_" + oiElement.quantityToOrder;

            contents += "<tr><td>" + oiElement.drugs +  "</td><td>" + oiElement.strength +  "</td><td><input type='text' size='5' id='" + packsizeoi + "' onkeyup=\"calculateFunction('" + finaloi + "')\" /></td><td>" + oiElement.basicUnit +  "</td><td id='" + qtyBeginingBalanceId + "'>" + oiElement.beginningBalance +  "</td>";
         	contents += "<td id='" + qtyReceivedId + "'>" + oiElement.quantityReceived +  "</td><td id='" + qtyDispensedId + "'>" + oiElement.quantityDispensed +  "</td><td id='" + qtyPositiveAdjustmentId + "'>" + oiElement.positiveAdjustments +  "</td>";
         	contents += "<td id='" + qtyNegativeAdjustmentId + "'>" + oiElement.negativeAdjustments +  "</td><td id='" + qtyReceivedLossesId + "'>" + oiElement.lossesdDamagesExpiries +  "</td><td id='" + qtyPhysicalCountId + "'>" + oiElement.physicalCount +  "</td>";
         	contents += "<td id='" + qtyMaximumStockId + "'>" + oiElement.maximumStockQuantity +  "</td><td id='" + qtyQuantityOrderId + "'>" + oiElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";            

        }
        
        contents += "<tr><td colspan='12' style='font-weight: bold'>Advanced HIV Disease Drugs</td></tr>";
        advanceHIV = dataURL.crrfAdvanceHIVRegimenCategory;
        for (l = 0; l < advanceHIV.length; l++) {
          	var advanceHIVElement = advanceHIV[l];

            var packsizeadvance = "packsizeadvance" + l;
          	var qtyReceivedId = "packsizeadvance" + l + "r";
          	var qtyBeginingBalanceId = "packsizeadvance" + l + "bb";
          	var qtyDispensedId = "packsizeadvance" + l + "d";
          	var qtyPositiveAdjustmentId = "packsizeadvance" + l + "pa";
          	var qtyNegativeAdjustmentId = "packsizeadvance" + l + "na";
          	var qtyReceivedLossesId = "packsizeadvance" + l + "l";
          	var qtyPhysicalCountId = "packsizeadvance" + l + "pc";
          	var qtyMaximumStockId = "packsizeadvance" + l + "ms";
          	var qtyQuantityOrderId = "packsizeadvance" + l + "qo";
          	
          	var finalpacksizeadvance = packsizeadvance + "_" + qtyReceivedId + "_" + advanceHIVElement.quantityReceived +  "_" + qtyBeginingBalanceId + "_" + advanceHIVElement.beginningBalance + "_" + qtyDispensedId + "_" + advanceHIVElement.quantityDispensed + "_" + qtyPositiveAdjustmentId + "_" + advanceHIVElement.positiveAdjustments + "_" + qtyNegativeAdjustmentId + "_" + advanceHIVElement.negativeAdjustments + "_" + qtyReceivedLossesId + "_" + advanceHIVElement.lossesdDamagesExpiries +  "_" + qtyPhysicalCountId + "_" + advanceHIVElement.physicalCount + "_" + qtyMaximumStockId + "_" + advanceHIVElement.maximumStockQuantity +  "_" + qtyQuantityOrderId + "_" + advanceHIVElement.quantityToOrder;

            contents += "<tr><td>" + advanceHIVElement.drugs +  "</td><td>" + advanceHIVElement.strength +  "</td><td><input type='text' size='5' id='" + packsizeadvance + "' onkeyup=\"calculateFunction('" + finalpacksizeadvance+ "')\" /></td><td>" + advanceHIVElement.basicUnit +  "</td><td id='" + qtyBeginingBalanceId + "'>" + advanceHIVElement.beginningBalance +  "</td>";
         	contents += "<td id='" + qtyReceivedId + "'>" + advanceHIVElement.quantityReceived +  "</td><td id='" + qtyDispensedId + "'>" + advanceHIVElement.quantityDispensed +  "</td><td id='" + qtyPositiveAdjustmentId + "'>" + advanceHIVElement.positiveAdjustments +  "</td>";
         	contents += "<td id='" + qtyNegativeAdjustmentId + "'>" + advanceHIVElement.negativeAdjustments +  "</td><td id='" + qtyReceivedLossesId + "'>" + advanceHIVElement.lossesdDamagesExpiries +  "</td><td id='" + qtyPhysicalCountId + "'>" + advanceHIVElement.physicalCount +  "</td>";
         	contents += "<td id='" + qtyMaximumStockId + "'>" + advanceHIVElement.maximumStockQuantity +  "</td><td id='" + qtyQuantityOrderId + "'>" + advanceHIVElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";            

        }
        
        contents += "<tr><td colspan='12' style='font-weight: bold'>STI Drugs</td></tr>";
        sti = dataURL.crrfSTIRegimenCategory;
        for (m = 0; m < sti.length; m++) {
          	var stiElement = sti[m];

            var packsizesti = "packsizesti" + m;
          	var qtyReceivedId = "packsizesti" + m + "r";
          	var qtyBeginingBalanceId = "packsizesti" + m + "bb";
          	var qtyDispensedId = "packsizesti" + m + "d";
          	var qtyPositiveAdjustmentId = "packsizesti" + m + "pa";
          	var qtyNegativeAdjustmentId = "packsizesti" + m + "na";
          	var qtyReceivedLossesId = "packsizesti" + m + "l";
          	var qtyPhysicalCountId = "packsizesti" + m + "pc";
          	var qtyMaximumStockId = "packsizesti" + m + "ms";
          	var qtyQuantityOrderId = "packsizesti" + m + "qo";
          	
          	var finalpacksizesti = packsizesti + "_" + qtyReceivedId + "_" + stiElement.quantityReceived +  "_" + qtyBeginingBalanceId + "_" + stiElement.beginningBalance + "_" + qtyDispensedId + "_" + stiElement.quantityDispensed + "_" + qtyPositiveAdjustmentId + "_" + stiElement.positiveAdjustments + "_" + qtyNegativeAdjustmentId + "_" + stiElement.negativeAdjustments + "_" + qtyReceivedLossesId + "_" + stiElement.lossesdDamagesExpiries +  "_" + qtyPhysicalCountId + "_" + stiElement.physicalCount + "_" + qtyMaximumStockId + "_" + stiElement.maximumStockQuantity +  "_" + qtyQuantityOrderId + "_" + stiElement.quantityToOrder;

            contents += "<tr><td>" + stiElement.drugs +  "</td><td>" + stiElement.strength +  "</td><td><input type='text' size='5' id='" + packsizesti + "' onkeyup=\"calculateFunction('" + finalpacksizesti+ "')\" /></td><td>" + stiElement.basicUnit +  "</td><td id='" + qtyBeginingBalanceId + "'>" + stiElement.beginningBalance +  "</td>";
         	contents += "<td id='" + qtyReceivedId + "'>" + stiElement.quantityReceived +  "</td><td id='" + qtyDispensedId + "'>" + stiElement.quantityDispensed +  "</td><td id='" + qtyPositiveAdjustmentId + "'>" + stiElement.positiveAdjustments +  "</td>";
         	contents += "<td id='" + qtyNegativeAdjustmentId + "'>" + stiElement.negativeAdjustments +  "</td><td id='" + qtyReceivedLossesId + "'>" + stiElement.lossesdDamagesExpiries +  "</td><td id='" + qtyPhysicalCountId + "'>" + stiElement.physicalCount +  "</td>";
         	contents += "<td id='" + qtyMaximumStockId + "'>" + stiElement.maximumStockQuantity +  "</td><td id='" + qtyQuantityOrderId + "'>" + stiElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";            

        }

        contents += "<tr><td colspan='12' style='font-weight: bold'>TB Drugs</td></tr>";
        tb = dataURL.crrfTBRegimenCategory;
        for (n = 0; n < tb.length; n++) {
          	var tbElement = tb[n];

            var packsizetb = "packsizetb" + n;
          	var qtyReceivedId = "packsizetb" + n + "r";
          	var qtyBeginingBalanceId = "packsizetb" + n + "bb";
          	var qtyDispensedId = "packsizetb" + n + "d";
          	var qtyPositiveAdjustmentId = "packsizetb" + n + "pa";
          	var qtyNegativeAdjustmentId = "packsizetb" + n + "na";
          	var qtyReceivedLossesId = "packsizetb" + n + "l";
          	var qtyPhysicalCountId = "packsizetb" + n + "pc";
          	var qtyMaximumStockId = "packsizetb" + n + "ms";
          	var qtyQuantityOrderId = "packsizetb" + n + "qo";
          	
          	var finalpacksizetb = packsizetb + "_" + qtyReceivedId + "_" + tbElement.quantityReceived +  "_" + qtyBeginingBalanceId + "_" + tbElement.beginningBalance + "_" + qtyDispensedId + "_" + tbElement.quantityDispensed + "_" + qtyPositiveAdjustmentId + "_" + tbElement.positiveAdjustments + "_" + qtyNegativeAdjustmentId + "_" + tbElement.negativeAdjustments + "_" + qtyReceivedLossesId + "_" + tbElement.lossesdDamagesExpiries +  "_" + qtyPhysicalCountId + "_" + tbElement.physicalCount + "_" + qtyMaximumStockId + "_" + tbElement.maximumStockQuantity +  "_" + qtyQuantityOrderId + "_" + tbElement.quantityToOrder;

            contents += "<tr><td>" + tbElement.drugs +  "</td><td>" + tbElement.strength +  "</td><td><input type='text' size='5' id='" + packsizetb + "' onkeyup=\"calculateFunction('" + finalpacksizetb+ "')\" /></td><td>" + tbElement.basicUnit +  "</td><td id='" + qtyBeginingBalanceId + "'>" + tbElement.beginningBalance +  "</td>";
         	contents += "<td id='" + qtyReceivedId + "'>" + tbElement.quantityReceived +  "</td><td id='" + qtyDispensedId + "'>" + tbElement.quantityDispensed +  "</td><td id='" + qtyPositiveAdjustmentId + "'>" + tbElement.positiveAdjustments +  "</td>";
         	contents += "<td id='" + qtyNegativeAdjustmentId + "'>" + tbElement.negativeAdjustments +  "</td><td id='" + qtyReceivedLossesId + "'>" + tbElement.lossesdDamagesExpiries +  "</td><td id='" + qtyPhysicalCountId + "'>" + tbElement.physicalCount +  "</td>";
         	contents += "<td id='" + qtyMaximumStockId + "'>" + tbElement.maximumStockQuantity +  "</td><td id='" + qtyQuantityOrderId + "'>" + tbElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";    
        }

        contents += "<tr style='background-color: white'><th colspan='14'>Comments:</th></tr>";
        contents += "<tr style='background-color: white'><th colspan='14'>1. Please provide details (expiry dates, lot numbers & quantities) of any commodity that will expire in six months time.</th></tr>";
        contents += "<tr style='background-color: white'><th colspan='14'>2. Any other information</th></tr>";
        contents += "<tr style='background-color: white'><th colspan='2'>S/N</th><th colspan='6'>Description</th><th colspan='2'>Batch/Lot Number</th><th colspan='2'>Expiry date</th><th colspan='2'>Quantity</th></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='6'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='6'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='6'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='6'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='6'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";        
        contents += "<tr style='background-color: white'><th colspan='14'>Reporting Officers Details</th></tr>";   
        contents += "<tr style='background-color: white'><th colspan='6'>Report Prepared by (Full Name):</th><th colspan='4'>Telephone:</th><th colspan='4'>Date:</th></tr>";
        contents += "<tr style='background-color: white'><th colspan='6'>Report Approved by (Full Name):</th><th colspan='4'>Telephone:</th><th colspan='4'>Date:</th></tr>";        
        contents += "</tbody></table>";

        contents += "<button id='exportToExcel'>Export to Excel </button>";

        jq("#crrfbody-list-table").append(contents);

    }

    function refreshDatatable(){
         var jq = jQuery;
         var table;
         jq(document).ready( function () {
             table= jq('#fileexport1').DataTable({
                 "paging":   true,
                 "info":     true,
                 "LengthChange": true,
                 "aLengthMenu": [[20,5, 10, 15, -1], [20,5, 10, 15, "All"]],
                 dom: 'Bfrtip',
                 buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
                 ]
             });
             jq('.buttons-copy, .buttons-csv, .buttons-print, .buttons-pdf, .buttons-excel').addClass('btn btn-success mr-1');
             table.columns().eq( 0 ).each( function ( colIdx ) {
                 jq( 'input', table.column( colIdx ).header() ).on( 'keyup change', function () {
                     table
                         .column( colIdx )
                         .search( this.value )
                         .draw(); //<--here is the draw
                 } );
             } );

         } );
     }

     var jqy = jQuery;
     jqy(function() {
         jqy("#exportToExcel").click(function(e){
                 console.log('Hi tables');
                 jqy("#fileexport1").table2excel({
                     name: "Excel Document Name",
                     filename: "Antiretroviral-and-OIs",
                     fileext: ".xls",
                     exclude_img: true,
                     exclude_links: true,
                     exclude_inputs: true
                 });

         });

     });
     
     //localStorage.clear();

</script>
