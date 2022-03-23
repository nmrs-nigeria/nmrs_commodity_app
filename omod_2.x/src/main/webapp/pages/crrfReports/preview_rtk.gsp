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

<h1>COMBINED REPORT AND REQUISITION FORM (CRRF) - Rapid Test Kits</h1>

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
        content += "<tr><th style='text-align: center' colspan='6'>HIV Rapid Test Kit (RTKs), Dried Blood Spot (DBS) Kit and Other RDT</th></tr>";
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
        contents += "<tr><th style='text-align: center;' colspan='13'>HIV RTK & DBS</th></tr>";
        contents += "<tr><th style='text-align: center;' colspan='9'>REPORT</th><th style='text-align: center;' colspan='3'>REQUISITION</th></tr>";
       
        contents += "<tr><th>Test Kit</th><th>Pack Size</th><th>Reporting Unit</th><th>Beginning Balance</th><th>Quantity Received</th><th>Quantity Dispensed</th>";
        contents += "<th>Positive Adjustments</th><th>Negative Adjustments</th><th>Losses (Damages/Expiries)</th><th>Physical Count</th>";
        contents += "<th>Maximum Stock Quantity</th><th>Quantity to Order</th><th>Remarks</th></tr>";
        contents += "</thead><tbody>";
        
        contents += "<tr><td colspan='13' style='font-weight: bold'>HIV RTK & DBS</td></tr>";         
        rtk = dataURL.crrfRTKRegimenCategory; 
       
        for (i = 0; i < rtk.length; i++) {
          	var rtkElement = rtk[i];
          	var ru = rtkElement.basicUnit.split(" ");
     		var packSize = ru[0];
     		var repUnit = ru[1];
         	contents += "<tr><td>" + rtkElement.drugs +  "</td><td>" + packSize +  "</td><td>" + repUnit +  "</td><td>" + rtkElement.beginningBalance +  "</td>";
         	contents += "<td>" + rtkElement.quantityReceived +  "</td><td>" + rtkElement.quantityDispensed +  "</td><td>" + rtkElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + rtkElement.negativeAdjustments +  "</td><td>" + rtkElement.lossesdDamagesExpiries +  "</td><td>" + rtkElement.physicalCount +  "</td>";
         	contents += "<td>" + rtkElement.maximumStockQuantity +  "</td><td>" + rtkElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
         
        contents += "</tbody></table>";
        
        jq("#crrfbody-list-table").append(contents);
    }
    
    displayBiMonthly();
    
	function displayBiMonthly() {
	    var bimonthly = "";
	    bimonthly += "<table class='table table-striped table-condensed' style='font-size: 12px; width: 100%;'>";  
	    bimonthly += "<tbody>"; 
	    bimonthly += "<tr style='background-color: white'><th colspan='12'>BIMONTHLY SUMMARY OF HIV RAPID TEST BY PURPOSE</th></tr>"; 
	    bimonthly += "<tr style='background-color: white'><th></th><th>HTS</th><th>PMTCT</th><th>CLINICAL DIAGNOSIS</th><th>QUALITY CONTROL</th><th>TRAINING</th><th>RECRUIT/OUTREACH SCREENING</th><th>TOTAL</th></tr>"; 
	    bimonthly += "<tr style='background-color: white'><td>1st Screening</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>"; 
	    bimonthly += "<tr style='background-color: white'><td>Confirmatory</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>"; 
	    bimonthly += "<tr style='background-color: white'><td>Tie-breaker</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>";
	    bimonthly += "</tbody>"; 
	    
	    bimonthly += "<table style='font-size: 12px; width: 100%;'>";   
	    bimonthly += "<tbody>"; 
	    bimonthly += "<tr style='background-color: white'><th colspan='2'> BIMONTHLY TEST SUMMARY OF HIV TESTING & EARLY INFANT DIAGNOSIS (EID)</th></tr>"; 
	    
	    bimonthly += "<tr style='background-color: white'><th colspan='2'> HIV TESTING</th></tr>"; 	   
	    bimonthly += "<tr style='background-color: white'><td>Number of people tested</td><td></td></tr>"; 
	    bimonthly += "<tr style='background-color: white'><td>Number of people who received counseling and testing and got result (HTS, PMTCT, TB/HIV combined)</td><td></td></tr>"; 
	    bimonthly += "<tr style='background-color: white'><td>Number of people tested with positive results (HTS, PMTCT, TB/HIV combined) </td><td></td></tr>";
	    bimonthly += "<tr style='background-color: white'><th colspan='2'> EID TESTING</th></tr>"; 	   
	    bimonthly += "<tr style='background-color: white'><td>Number of HIV exposed Babies</td><td></td></tr>"; 
	    bimonthly += "<tr style='background-color: white'><td>Number of Infants received EID testing</td><td></td></tr>"; 	   
	    bimonthly += "</tbody>"; 
	    
	    bimonthly += "<table style='font-size: 12px; width: 100%;'>";    
	    bimonthly += "<tbody>"; 
	    bimonthly += "<tr style='background-color: white'><th colspan='5'> Expiry Details </th></tr>";
	    bimonthly += "<tr style='background-color: white'><th colspan='4'>Please provide details (expiry dates, lot numbers & quantities) of any commodity that will expire in three months time </th><th>Additional Remarks:</th></tr>"; 	   
	    bimonthly += "<tr style='background-color: white'><th>Description</th><th>Lot No</th><th>Exp date</th><th>Quantity</th><th rowspan='6'></th></tr>"; 
	    bimonthly += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>"; 
	    bimonthly += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>";
	    bimonthly += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>"; 	   
	    bimonthly += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>"; 
	    bimonthly += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>"; 	   
	    bimonthly += "</tbody>"; 
	    
	    bimonthly += "</table>";        
	    bimonthly += "<table class='table table-striped table-condensed' style='font-size: 12px; width: 100%;'>"; 
	    bimonthly += "<tbody>"; 
	    bimonthly += "<tr><th colspan='3'>Reporting Officers Details:</th></tr>"; 	    
	    bimonthly += "<tr style='background-color: white'><th>Report Prepared by (Full Name):</th><th>Telephone:</th><th>Date:</th></tr>"; 
	    bimonthly += "<tr style='background-color: white'><th>Report Approved by (Full Name):</th><th>Telephone:</th><th>Date:</th></tr>"; 
	    bimonthly += "</tbody>"; 
	    bimonthly += "</table>"; 
	    jq("#crrfbodycomment-list-table").append(bimonthly);
	}   
	
    localStorage.clear();
</script>







