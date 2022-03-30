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

        contents += "<tr><th colspan='2'>Facility Name: </th><td colspan='2'>" + dataURL.facilityName + "</td><th colspan='2'>Reporting Period Start: </th><td colspan='6'>" + repDateStart + "</td></tr>";
        contents += "<tr><th colspan='2'>Facility Code: </th><td colspan='2'>" + dataURL.facilityCode + "</th><th colspan='2'>Reporting Period End: </th><td colspan='2'>" + repDateEnd + "</td><th colspan='2'>Maximum Stock Level: </th><td colspan='2'>4 Months</td></tr>";
        contents += "<tr><th colspan='2'>LGA: </th><td colspan='2'>" + dataURL.lga + "</td><th colspan='2'>Date Prepared: </th><td colspan='2'>" + repDatePrepared + "</td><th colspan='2'>Minimum Stock Level:</th><td colspan='2'> 2 Months</td></tr>";
        contents += "<tr><th colspan='2'>State: </th><td colspan='10'>" + dataURL.state + "</td></tr>";

        contents += "<tr><th style='text-align: center;' colspan='12'>Antiretroviral Drugs</th></tr>";
        contents += "<tr><th style='text-align: center;' colspan='9'>REPORT</th><th style='text-align: center;' colspan='3'>REQUISITION</th></tr>";

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
          	
          	var packsizepaediatric = "packsizepaediatric" + i;
          	var qtyReceivedId = "packsizepaediatric" + i + "r";
          	var qtyBeginingBalanceId = "packsizepaediatric" + i + "bb";
          	var qtyDispensedId = "packsizepaediatric" + i + "d";
          	var qtyPositiveAdjustmentId = "packsizepaediatric" + i + "pa";
          	var qtyNegativeAdjustmentId = "packsizepaediatric" + i + "na";
          	var qtyReceivedLossesId = "packsizepaediatric" + i + "l";
          	var qtyPhysicalCountId = "packsizepaediatric" + i + "pc";
          	var qtyMaximumStockId = "packsizepaediatric" + i + "ms";
          	var qtyQuantityOrderId = "packsizepaediatric" + i + "qo";
          	
          	var finalpaediatric = packsizepaediatric + "_" + qtyReceivedId + "_" + paediatricElement.quantityReceived +  "_" + qtyBeginingBalanceId + "_" + paediatricElement.beginningBalance + "_" + qtyDispensedId + "_" + paediatricElement.quantityDispensed + "_" + qtyPositiveAdjustmentId + "_" + paediatricElement.positiveAdjustments + "_" + qtyNegativeAdjustmentId + "_" + paediatricElement.negativeAdjustments + "_" + qtyReceivedLossesId + "_" + paediatricElement.lossesdDamagesExpiries +  "_" + qtyPhysicalCountId + "_" + paediatricElement.physicalCount + "_" + qtyMaximumStockId + "_" + paediatricElement.maximumStockQuantity +  "_" + qtyQuantityOrderId + "_" + paediatricElement.quantityToOrder;
  	
         	contents += "<tr><td>" + paediatricElement.drugs +  "</td><td>" + paediatricElement.strength +  "</td><td></td><td><input type='text' size='5' id='" + packsizepaediatric + "' onkeyup=\"calculateFunction('" + finalpaediatric + "')\" /></td><td>" + paediatricElement.basicUnit +  "</td><td id='" + qtyBeginingBalanceId + "'>" + paediatricElement.beginningBalance +  "</td>";
         	contents += "<td id='" + qtyReceivedId + "'>" + paediatricElement.quantityReceived +  "</td><td id='" + qtyDispensedId + "'>" + paediatricElement.quantityDispensed +  "</td><td id='" + qtyPositiveAdjustmentId + "'>" + paediatricElement.positiveAdjustments +  "</td>";
         	contents += "<td id='" + qtyNegativeAdjustmentId + "'>" + paediatricElement.negativeAdjustments +  "</td><td id='" + qtyReceivedLossesId + "'>" + paediatricElement.lossesdDamagesExpiries +  "</td><td id='" + qtyPhysicalCountId + "'>" + paediatricElement.physicalCount +  "</td>";
         	contents += "<td id='" + qtyMaximumStockId + "'>" + paediatricElement.maximumStockQuantity +  "</td><td id='" + qtyQuantityOrderId + "'>" + paediatricElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
         
        contents += "<tr><td colspan='12' style='font-weight: bold'>Opportunistic Infections Drugs</td></tr>";
        oi = dataURL.crrfOIRegimenCategory;     
        for (k = 0; k < oi.length; k++) {
          	var oiElement = oi[k];
         	contents += "<tr><td>" + oiElement.drugs +  "</td><td>" + adultElement.strength +  "</td><td><td>" + oiElement.basicUnit +  "</td><td>" + oiElement.beginningBalance +  "</td>";
         	contents += "<td>" + oiElement.quantityReceived +  "</td><td>" + oiElement.quantityDispensed +  "</td><td>" + oiElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + oiElement.negativeAdjustments +  "</td><td>" + oiElement.lossesdDamagesExpiries +  "</td><td>" + oiElement.physicalCount +  "</td>";
         	contents += "<td>" + oiElement.maximumStockQuantity +  "</td><td>" + oiElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
        
        contents += "<tr><td colspan='12' style='font-weight: bold'>Advanced HIV Disease Drugs</td></tr>";
        advanceHIV = dataURL.crrfAdvanceHIVRegimenCategory;
        for (l = 0; l < advanceHIV.length; l++) {
          	var advanceHIVElement = advanceHIV[l];
         	contents += "<tr><td>" + advanceHIVElement.drugs +  "</td><td>" + adultElement.strength +  "</td><td><td>" + advanceHIVElement.basicUnit +  "</td><td>" + advanceHIVElement.beginningBalance +  "</td>";
         	contents += "<td>" + advanceHIVElement.quantityReceived +  "</td><td>" + advanceHIVElement.quantityDispensed +  "</td><td>" + advanceHIVElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + advanceHIVElement.negativeAdjustments +  "</td><td>" + advanceHIVElement.lossesdDamagesExpiries +  "</td><td>" + advanceHIVElement.physicalCount +  "</td>";
         	contents += "<td>" + advanceHIVElement.maximumStockQuantity +  "</td><td>" + advanceHIVElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }
        
        contents += "<tr><td colspan='12' style='font-weight: bold'>STI Drugs</td></tr>";
        sti = dataURL.crrfSTIRegimenCategory;
        for (m = 0; m < sti.length; m++) {
          	var stiElement = sti[m];
         	contents += "<tr><td>" + stiElement.drugs +  "</td><td>" + adultElement.strength +  "</td><td><td>" + stiElement.basicUnit +  "</td><td>" + stiElement.beginningBalance +  "</td>";
         	contents += "<td>" + stiElement.quantityReceived +  "</td><td>" + stiElement.quantityDispensed +  "</td><td>" + stiElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + stiElement.negativeAdjustments +  "</td><td>" + stiElement.lossesdDamagesExpiries +  "</td><td>" + stiElement.physicalCount +  "</td>";
         	contents += "<td>" + stiElement.maximumStockQuantity +  "</td><td>" + stiElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }

        contents += "<tr><td colspan='12' style='font-weight: bold'>TB Drugs</td></tr>";
        tb = dataURL.crrfTBRegimenCategory;
        for (n = 0; n < tb.length; n++) {
          	var tbElement = tb[n];
         	contents += "<tr><td>" + tbElement.drugs +  "</td><td>" + adultElement.strength +  "</td><td><td>" + tbElement.basicUnit +  "</td><td>" + tbElement.beginningBalance +  "</td>";
         	contents += "<td>" + tbElement.quantityReceived +  "</td><td>" + tbElement.quantityDispensed +  "</td><td>" + tbElement.positiveAdjustments +  "</td>";
         	contents += "<td>" + tbElement.negativeAdjustments +  "</td><td>" + tbElement.lossesdDamagesExpiries +  "</td><td>" + tbElement.physicalCount +  "</td>";
         	contents += "<td>" + tbElement.maximumStockQuantity +  "</td><td>" + tbElement.quantityToOrder +  "</td><td></td>";         	            
         	contents += "</tr>";
        }

        contents += "<tr style='background-color: white'><th colspan='12'>Comments:</th></tr>";
        contents += "<tr style='background-color: white'><th colspan='12'>1. Please provide details (expiry dates, lot numbers & quantities) of any commodity that will expire in six months time.</th></tr>";
        contents += "<tr style='background-color: white'><th colspan='12'>2. Any other information</th></tr>";
        contents += "<tr style='background-color: white'><th colspan='2'>S/N</th><th colspan='4'>Description</th><th colspan='2'>Batch/Lot Number</th><th colspan='2'>Expiry date</th><th colspan='2'>Quantity</th></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='4'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='4'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='4'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='4'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";
        contents += "<tr style='background-color: white'><td colspan='2'></td><td colspan='4'></td><td colspan='2'></td><td colspan='2'></td><td colspan='2'></td></tr>";        
        contents += "<tr style='background-color: white'><th colspan='12'>Reporting Officers Details</th></tr>";   
        contents += "<tr style='background-color: white'><th colspan='4'>Report Prepared by (Full Name):</th><th colspan='4'>Telephone:</th><th colspan='4'>Date:</th></tr>";
        contents += "<tr style='background-color: white'><th colspan='4'>Report Approved by (Full Name):</th><th colspan='4'>Telephone:</th><th colspan='4'>Date:</th></tr>";        
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







