<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("openhmis.inventory.admin.labcrrfReports") ])

//include css
ui.includeCss("openhmis.commons", "bootstrap.css")
ui.includeCss("openhmis.inventory", "extCss/custom.css")
ui.includeCss("openhmis.inventory", "extCss/dataTables.bootstrap.css")
    

//include js
ui.includeJavascript("openhmis.inventory", "labCrrfReports/configs/extJs/jquery-3.5.1.js")
ui.includeJavascript("openhmis.inventory", "labCrrfReports/configs/extJs/bootstrap.min.js")
ui.includeJavascript("openhmis.inventory", "labCrrfReports/configs/extJs/jquery.csv.min.js")
ui.includeJavascript("openhmis.inventory", "labCrrfReports/configs/extJs/jquery.dataTables.min.js")
ui.includeJavascript("openhmis.inventory", "labCrrfReports/configs/extJs/dataTables.bootstrap.js")
ui.includeJavascript("openhmis.inventory", "labCrrfReports/configs/extJs/jquery.table2excel.js")

%>

<script type="text/javascript" xmlns="http://www.w3.org/1999/html">
    var breadcrumbs = [
    { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
    {
            label: "${ ui.message("openhmis.inventory.page")}" ,
            link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
    },
    {
            label: "${ ui.message("openhmis.inventory.admin.task.dashboard")}",
    link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/inventoryTasksDashboard.page'
    },
    {
            label: "${ ui.message("openhmis.inventory.admin.labcrrfReports")}"
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

        var physicalBalanceId = arrayS[19];
        var physicalBalanceQty = arrayS[20];
        var finalphysicalBalance = parseFloat(physicalBalanceQty) / parseFloat(packSizeValue);
        var finalphysicalBalanceRounded = parseFloat(finalphysicalBalance).toFixed(1);
    	document.getElementById('' + physicalBalanceId).innerHTML = finalphysicalBalanceRounded;
        
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
        contents += "<tr><th style='text-align: center' colspan='13'>HIV Rapid Test Kit (RTKs), Dried Blood Spot (DBS) Kit and Other RDT</th></tr>";
	    contents += "<tr><th colspan='2'>Facility Name: </th><td colspan='4'>" + dataURL.facilityName + "</td><th colspan='2'>Reporting Period Start: </th><td colspan='5'>" + repDateStart + "</td></tr>";
	    contents += "<tr><th colspan='2'>Facility Code: </th><td colspan='4'>" + dataURL.facilityCode + "</th><th colspan='2'>Reporting Period End: </th><td>" + repDateEnd + "</td><th colspan='3'>Maximum Stock Level: </th><td>4 Months</td></tr>";
	    contents += "<tr><th colspan='2'>LGA: </th><td colspan='4'>" + dataURL.lga + "</td><th colspan='2'>Date Prepared: </th><td>" + repDatePrepared + "</td><th colspan='3'>Minimum Stock Level:</th><td> 2 Months</td></tr>";
	    contents += "<tr><th colspan='2'>State: </th><td colspan='11'>" + dataURL.state + "</td></tr>";
	    contents += "</thead>";
        
        contents += "<tr><th style='text-align: center;' colspan='13'>HIV RTK & DBS</th></tr>";
        contents += "<tr><th style='text-align: center;' colspan='9'>REPORT</th><th style='text-align: center;' colspan='3'>REQUISITION</th></tr>";
       
        contents += "<tr><th>Test Kit</th><th>Pack Size</th><th>Reporting Unit</th><th>Beginning Balance</th><th>Quantity Received</th><th>Quantity Consumed</th>";
        contents += "<th>Positive Adjustments</th><th>Negative Adjustments</th><th>Losses (Damages/Expiries)</th><th>Calculated Balance</th><th>Physical Count</th>";
        contents += "<th>Maximum Stock Quantity</th><th>Quantity to Order</th><th>Remarks</th></tr>";
        
        contents += "<tr><td colspan='13' style='font-weight: bold'>HIV RTK & DBS</td></tr>";         
        rtk = dataURL.crrfRTKRegimenCategory; 
       
        for (i = 0; i < rtk.length; i++) {
          	var rtkElement = rtk[i];
          	var ru = rtkElement.basicUnit.split(" ");
     		var packSize = ru[0];
     		var repUnit = ru[1];

            var packsizertk = "packsizertk" + i;
          	var qtyReceivedId = "packsizertk" + i + "r";
          	var qtyBeginingBalanceId = "packsizertk" + i + "bb";
          	var qtyDispensedId = "packsizertk" + i + "d";
          	var qtyPositiveAdjustmentId = "packsizertk" + i + "pa";
          	var qtyNegativeAdjustmentId = "packsizertk" + i + "na";
          	var qtyReceivedLossesId = "packsizertk" + i + "l";
          	var qtyPhysicalCountId = "packsizertk" + i + "pc";
            var qtyCalculatedCountId = "packsizertk" + i + "cc";
          	var qtyMaximumStockId = "packsizertk" + i + "ms";
          	var qtyQuantityOrderId = "packsizertk" + i + "qo";
          	
          	var final = packsizertk + "_" + qtyReceivedId + "_" + rtkElement.quantityReceived +  "_" + qtyBeginingBalanceId + "_" + rtkElement.beginningBalance + "_" + qtyDispensedId + "_" + rtkElement.quantityDispensed + "_" + qtyPositiveAdjustmentId + "_" + rtkElement.positiveAdjustments + "_" + qtyNegativeAdjustmentId + "_" + rtkElement.negativeAdjustments + "_" + qtyReceivedLossesId + "_" + rtkElement.lossesdDamagesExpiries +  "_" + qtyPhysicalCountId + "_" + rtkElement.physicalCount + "_" + qtyMaximumStockId + "_" + rtkElement.maximumStockQuantity +  "_" + qtyQuantityOrderId + "_" + rtkElement.quantityToOrder +  "_" + qtyCalculatedCountId + "_" + rtkElement.physicalBalance;
          	
         	contents += "<tr><td>" + rtkElement.drugs +  "</td><td><input type='text' size='5' id='" + packsizertk + "' onkeyup=\"calculateFunction('" + final + "')\" /></td><td>" + repUnit +  "</td><td id='" + qtyBeginingBalanceId + "'>" + rtkElement.beginningBalance +  "</td>";
         	contents += "<td id='" + qtyReceivedId + "'>" + rtkElement.quantityReceived +  "</td><td id='" + qtyDispensedId + "'>" + rtkElement.quantityDispensed +  "</td><td id='" + qtyPositiveAdjustmentId + "'>" + rtkElement.positiveAdjustments +  "</td>";
         	contents += "<td id='" + qtyNegativeAdjustmentId + "'>" + rtkElement.negativeAdjustments +  "</td><td id='" + qtyReceivedLossesId + "'>" + rtkElement.lossesdDamagesExpiries +  "</td><td id='" + qtyPhysicalCountId + "'>" + rtkElement.physicalCount +  "</td><td id='" + qtyCalculatedCountId + "'>" + rtkElement.physicalBalance +  "</td>";
         	contents += "<td id='" + qtyMaximumStockId + "'>" + rtkElement.maximumStockQuantity +  "</td><td id='" + qtyQuantityOrderId + "'>" + rtkElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";

        }
         
        contents += "<tr style='background-color: white'><th colspan='13'>BIMONTHLY SUMMARY OF HIV RAPID TEST BY PURPOSE</th></tr>"; 
	    contents += "<tr style='background-color: white'><th></th><th>HTS</th><th colspan='2'>PMTCT</th><th colspan='2'>CLINICAL DIAGNOSIS</th><th colspan='2'>QUALITY CONTROL</th><th colspan='2'>TRAINING</th><th colspan='2'>RECRUIT/OUTREACH SCREENING</th><th>TOTAL</th></tr>"; 
	    contents += "<tr style='background-color: white'><td>1st Screening</td><td></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td><td></td></tr>"; 
	    contents += "<tr style='background-color: white'><td>Confirmatory</td><td></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td><td></td></tr>"; 
	    contents += "<tr style='background-color: white'><td>Tie-breaker</td><td></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td><td></td></tr>";
	    
        contents += "<tr style='background-color: white'><th colspan='13'> BIMONTHLY TEST SUMMARY OF HIV TESTING & EARLY INFANT DIAGNOSIS (EID)</th></tr>"; 
	    
	    contents += "<tr style='background-color: white'><th colspan='13'> HIV TESTING</th></tr>"; 	   
	    contents += "<tr style='background-color: white'><td colspan='11'>Number of people tested</td><td colspan='2'></td></tr>"; 
	    contents += "<tr style='background-color: white'><td colspan='11'>Number of people who received counseling and testing and got result (HTS, PMTCT, TB/HIV combined)</td><td colspan='2'></td></tr>"; 
	    contents += "<tr style='background-color: white'><td colspan='11'>Number of people tested with positive results (HTS, PMTCT, TB/HIV combined) </td><td colspan='2'></td></tr>";
	    contents += "<tr style='background-color: white'><th colspan='13'> EID TESTING</th></tr>"; 	   
	    contents += "<tr style='background-color: white'><td colspan='11'>Number of HIV exposed Babies</td><td colspan='2'></td></tr>"; 
	    contents += "<tr style='background-color: white'><td colspan='11'>Number of Infants received EID testing</td><td colspan='2'></td></tr>"; 	   
	 
        contents += "<tr style='background-color: white'><th colspan='13'> Expiry Details </th></tr>";
	    contents += "<tr style='background-color: white'><th colspan='4'>Please provide details (expiry dates, lot numbers & quantities) of any commodity that will expire in three months time </th><th colspan='9'>Additional Remarks:</th></tr>"; 	   
	    contents += "<tr style='background-color: white'><th>Description</th><th>Lot No</th><th>Exp date</th><th>Quantity</th><th colspan='9' rowspan='6'></th></tr>"; 
	    contents += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>"; 
	    contents += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>";
	    contents += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>"; 	   
	    contents += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>"; 
	    contents += "<tr style='background-color: white'><td></td><td></td><td></td><td></td></tr>"; 	   

        contents += "<tr><th colspan='13'>Reporting Officers Details:</th></tr>"; 	    
	    contents += "<tr style='background-color: white'><th colspan='5'>Report Prepared by (Full Name):</th><th colspan='4'>Telephone:</th><th colspan='4'>Date:</th></tr>"; 
	    contents += "<tr style='background-color: white'><th colspan='5'>Report Approved by (Full Name):</th><th colspan='4'>Telephone:</th><th colspan='4'>Date:</th></tr>"; 
	    contents += "</tbody>"; 
	    contents += "</table>";
        
        contents += "<button id='exportToExcel'>Export to Excel</button>";

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
                     filename: "RTK-and-DBS",
                     fileext: ".xls",
                     exclude_img: true,
                     exclude_links: true,
                     exclude_inputs: true
                 });

         });

     });

    localStorage.clear();
</script>







