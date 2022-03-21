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
ui.includeJavascript("openhmis.inventory", "crrfReports/configs/extJs/jquery.dataTables.min.js")
ui.includeJavascript("openhmis.inventory", "crrfReports/configs/extJs/dataTables.bootstrap.js")
ui.includeJavascript("openhmis.inventory", "crrfReports/configs/extJs/csv_to_html_table.js")

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
     
     displayData();
     
     function displayData() {
     	var rsd = dataURL.reportingPeriodStart.split("T");
     	var repDateStart = rsd[0];
     	var red = dataURL.reportingPeriodEnd.split("T");
     	var repDateEnd = red[0];
     	var dp = dataURL.datePrepared.split("T");
     	var repDatePrepared = dp[0];
     
        var content = "";
        content += "<table class='table table-striped table-condensed' style='font-size: 12px; width: 100%;'>";
        content += "<thead>";
	    content += "<tr><th>Facility Name: </th><td>" + dataURL.facilityName + "</td><th>Reporting Period Start: </th><td colspan='3'>" + repDateStart + "</td></tr>";
	    content += "<tr><th>Facility Code: </th><td>" + dataURL.facilityCode + "</th><th>Reporting Period End: </th><td>" + repDateEnd + "</td><th>Maximum Stock Level: </th><td>4 Months</td></tr>";
	    content += "<tr><th>LGA: </th><td>" + dataURL.lga + "</td><th>Date Prepared: </th><td>" + repDatePrepared + "</td><th>Minimum Stock Level:</th><td> 2 Months</td></tr>";
	    content += "<tr><th>State: </th><td colspan='5'>" + dataURL.state + "</td></tr>";
	    content += "</thead>";
	    content += "</table>";            
        jq("#crrf-list-table").append(content);
    }

    displayDataBody();
    
    function displayDataBody() {
        var contents = "";
        contents += "<table class='table table-striped table-condensed' style='font-size: 12px; width: 80%;'>";
        contents += "<thead>";
        contents += "<tr><th style='text-align: center;' colspan='13'>Antiretroviral Drugs</th></tr>";
        contents += "<tr><th style='text-align: center;' colspan='9'>REPORT</th><th style='text-align: center;' colspan='3'>REQUISITION</th></tr>";
       
        contents += "<tr><th>Drugs</th><th>Basic Unit</th><th>Beginning Balance</th><th>Quantity Received</th><th>Quantity Dispensed</th>";
        contents += "<th>Positive Adjustments</th><th>Negative Adjustments</th><th>Losses (Damages/Expiries)</th><th>Physical Count</th>";
        contents += "<th>Maximum Stock Quantity</th><th>Quantity to Order</th><th>Remarks</th></tr>";
        contents += "</thead><tbody>";
        
        contents += "<tr><td colspan='13' style='font-weight: bold'>Adult Regimen</td></tr>";         
        adult = dataURL.crrfAdultRegimenCategory;     
        for (i = 0; i < adult.length; i++) {
          	var adultElement = adult[i];
         	contents += "<tr><td>" + adultElement.drugs +  "</td><td>" + adultElement.basicUnit +  "</td><td>" + adultElement.beginningBalance +  "</td>";
         	contents += "<td>" + adultElement.quantityReceived +  "</td><td>" + adultElement.quantityDispensed +  "</td><td>" + adultElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + adultElement.negativeAdjustments +  "</td><td>" + adultElement.lossesdDamagesExpiries +  "</td><td>" + adultElement.physicalCount +  "</td>";
         	contents += "<td>" + adultElement.maximumStockQuantity +  "</td><td>" + adultElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
         
        contents += "<tr><td colspan='13' style='font-weight: bold'>Paediatric Regimen</td></tr>";         
        paediatric = dataURL.crrfPediatricRegimenCategory;     
        for (j = 0; j < paediatric.length; j++) {
          	var paediatricElement = paediatric[j];
         	contents += "<tr><td>" + paediatricElement.drugs +  "</td><td>" + paediatricElement.basicUnit +  "</td><td>" + paediatricElement.beginningBalance +  "</td>";
         	contents += "<td>" + paediatricElement.quantityReceived +  "</td><td>" + paediatricElement.quantityDispensed +  "</td><td>" + paediatricElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + paediatricElement.negativeAdjustments +  "</td><td>" + paediatricElement.lossesdDamagesExpiries +  "</td><td>" + paediatricElement.physicalCount +  "</td>";
         	contents += "<td>" + paediatricElement.maximumStockQuantity +  "</td><td>" + paediatricElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
         
        contents += "<tr><td colspan='13' style='font-weight: bold'>Opportunistic Infections Drugs</td></tr>";         
        oi = dataURL.crrfOIRegimenCategory;     
        for (k = 0; k < oi.length; k++) {
          	var oiElement = oi[k];
         	contents += "<tr><td>" + oiElement.drugs +  "</td><td>" + oiElement.basicUnit +  "</td><td>" + oiElement.beginningBalance +  "</td>";
         	contents += "<td>" + oiElement.quantityReceived +  "</td><td>" + oiElement.quantityDispensed +  "</td><td>" + oiElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + oiElement.negativeAdjustments +  "</td><td>" + oiElement.lossesdDamagesExpiries +  "</td><td>" + oiElement.physicalCount +  "</td>";
         	contents += "<td>" + oiElement.maximumStockQuantity +  "</td><td>" + oiElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
        
        contents += "<tr><td colspan='13' style='font-weight: bold'>Advanced HIV Disease Drugs</td></tr>";         
        advanceHIV = dataURL.crrfAdvanceHIVRegimenCategory;     
        for (l = 0; l < advanceHIV.length; l++) {
          	var advanceHIVElement = advanceHIV[l];
         	contents += "<tr><td>" + advanceHIVElement.drugs +  "</td><td>" + advanceHIVElement.basicUnit +  "</td><td>" + advanceHIVElement.beginningBalance +  "</td>";
         	contents += "<td>" + advanceHIVElement.quantityReceived +  "</td><td>" + advanceHIVElement.quantityDispensed +  "</td><td>" + advanceHIVElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + advanceHIVElement.negativeAdjustments +  "</td><td>" + advanceHIVElement.lossesdDamagesExpiries +  "</td><td>" + advanceHIVElement.physicalCount +  "</td>";
         	contents += "<td>" + advanceHIVElement.maximumStockQuantity +  "</td><td>" + advanceHIVElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
        
        contents += "<tr><td colspan='13' style='font-weight: bold'>STI Drugs</td></tr>";         
        sti = dataURL.crrfSTIRegimenCategory;     
        for (m = 0; m < sti.length; m++) {
          	var stiElement = sti[m];
         	contents += "<tr><td>" + stiElement.drugs +  "</td><td>" + stiElement.basicUnit +  "</td><td>" + stiElement.beginningBalance +  "</td>";
         	contents += "<td>" + stiElement.quantityReceived +  "</td><td>" + stiElement.quantityDispensed +  "</td><td>" + stiElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + stiElement.negativeAdjustments +  "</td><td>" + stiElement.lossesdDamagesExpiries +  "</td><td>" + stiElement.physicalCount +  "</td>";
         	contents += "<td>" + stiElement.maximumStockQuantity +  "</td><td>" + stiElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
        
        contents += "<tr><td colspan='13' style='font-weight: bold'>TB Drugs</td></tr>";         
        tb = dataURL.crrfTBRegimenCategory;     
        for (n = 0; n < tb.length; n++) {
          	var tbElement = tb[n];
         	contents += "<tr><td>" + tbElement.drugs +  "</td><td>" + tbElement.basicUnit +  "</td><td>" + tbElement.beginningBalance +  "</td>";
         	contents += "<td>" + tbElement.quantityReceived +  "</td><td>" + tbElement.quantityDispensed +  "</td><td>" + tbElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + tbElement.negativeAdjustments +  "</td><td>" + tbElement.lossesdDamagesExpiries +  "</td><td>" + tbElement.physicalCount +  "</td>";
         	contents += "<td>" + tbElement.maximumStockQuantity +  "</td><td>" + tbElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
        
        contents += "</tbody></table>";
        
        jq("#crrfbody-list-table").append(contents);
    }
    
    displayDataComment();
    
	function displayDataComment() {
	    var contentss = "";
	    contentss += "<table class='table table-striped table-condensed' style='font-size: 12px; width: 100%;''>";  
	    contentss += "<tbody>"; 
	    contentss += "<tr style='background-color: white'><th colspan='13'>Comments:</th></tr>"; 
	    contentss += "<tr style='background-color: white'><th colspan='13'>1. Please provide details (expiry dates, lot numbers & quantities) of any commodity that will expire in six months time.</th></tr>"; 
	    contentss += "<tr style='background-color: white'><th colspan='13'>2. Any other information</th></tr>"; 
	    contentss += "<tr style='background-color: white'><th>S/N</th><th>Description</th><th>Batch/Lot Number</th><th>Expiry date</th><th>Quantity</th></tr>"; 
	    contentss += "<tr style='background-color: white'><td></td><td></td><td></td><td></td><td></td></tr>"; 
	    contentss += "<tr style='background-color: white'><td></td><td></td><td></td><td></td><td></td></tr>"; 
	    contentss += "<tr style='background-color: white'><td></td><td></td><td></td><td></td><td></td></tr>"; 
	    contentss += "<tr style='background-color: white'><td></td><td></td><td></td><td></td><td></td></tr>"; 
	    contentss += "<tr style='background-color: white'><td></td><td></td><td></td><td></td><td></td></tr>"; 
	    contentss += "</tbody>"; 
	    contentss += "</table>";        
	    contentss += "<table class='table table-striped table-condensed' style='font-size: 12px; width: 100%;'>"; 
	    contentss += "<tbody>"; 
	    contentss += "<tr style='background-color: white'><th>Report Prepared by (Full Name):</th><th>Telephone:</th><th>Date:</th></tr>"; 
	    contentss += "<tr style='background-color: white'><th>Report Approved by (Full Name):</th><th>Telephone:</th><th>Date:</th></tr>"; 
	    contentss += "</tbody>"; 
	    contentss += "</table>"; 
	    jq("#crrfbodycomment-list-table").append(contentss);
	}
    
     
</script>







