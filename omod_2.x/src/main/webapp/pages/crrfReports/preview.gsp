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

 <div class="container" style="margin-top:20px; width: 80%;">

      <h1>Report Preview</h1>
      
      <!--<div id='table-container'></div>-->

     <table class='table table-striped table-condensed' style="font-size: 12px; width: 80%;">
     <thead>
     <th>Drugs</th>   <th>Basic Unit</th>   <th>Beginning Balance</th>  <th>Quantity Received</th> <th>Quantity Dispensed</th>
     <th>Positive Adjustments </th><th>Negative Adjustments</th><th>Losses Damages/Expires</th><th>Physical Count</th>
     <th>Maximum Stock Quantity</th><th>Maximum Stock Quantity</th>
     <th>Quantity to Order</th> <th>Remarks</th>
     </thead>
     <tbody>
     <tr><td colspan="13" style="font-weight: bold">Fixed Dose Combination Adult first line</td></tr>
     <tr><td>ABC | 3TC (600/300mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>AZT | 3TC (300/150mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>TDF | 3TC (300/300mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>TDF | FTC (300/200mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>TDF | 3TC | EFV  (300/300/400mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>TDF | 3TC | EFV  (300/300/400mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>TDF | 3TC | DTG  (300/300/50mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>TDF | 3TC | DTG  (300/300/50mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>TDF | 3TC | DTG  (300/300/50mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td colspan="13" style="font-weight: bold">Single Dose Adult first line</td></tr>
     <tr><td>Dolutegravir DTG 50mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Efavirenz EFV 600 mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Zidovudine AZT 300mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td colspan="13" style="font-weight: bold">Adult Second Line Drugs</td></tr>
     <tr><td>ATV | r  (300/100mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>LPV | r  (200/50mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td colspan="13" style="font-weight: bold">Adult Third Line Drugs</td></tr>
     <tr><td>Darunavir (DRV)  600mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Ritonavir (r)  100mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td colspan="13" style="font-weight: bold">Paediatric Fixed Dose Combination</td></tr>
     <tr><td>ABC | 3TC (120/60mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>ABC | 3TC | LPV | r (30/15/40/10mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>AZT | 3TC (60/30mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>LPV | r  (40/10mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>LPV | r  (100/25mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td colspan="13" style="font-weight: bold">Paediatric Single Dose </td></tr>
     <tr><td>Abacavir ABC (60mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Efavirenz EFV 200mg Scored</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Nevirapine NVP 50mg/5mL</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Zidovudine AZT 50mg/5mL</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td colspan="13" style="font-weight: bold">Paediatric Second Line Drugs</td></tr>
     <tr><td>LPV | r  (100/25mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Raltegravir 25mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td colspan="13" style="font-weight: bold">Paediatric Third  Line Drugs</td></tr>
     <tr><td>Darunavir (DRV)  75mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td colspan="13" style="font-weight: bold">Opportunistic Infections Drugs</td></tr>
     <tr><td>Co-trimoxazole 120mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Co-trimoxazole 960mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Co-trimoxazole/Isoniazid/Pyridoxine (960/300/25mg)</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Fluconazole 50mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Fluconazole Oral Suspension 50mg/5ml</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Fluconazole 100mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Isoniazid (INH) 100mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Isoniazid (INH) 300mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Nystatin Oral Suspension 100,000 IU/ml</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Pyridoxine 50mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td colspan="13" style="font-weight: bold">Advanced HIV Disease Drugs</td></tr>
     <tr><td>Flucytosine 500mg Tabs</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Liposomal Amphotericin B 50mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
     <tr><td>Amphotericin Deoxycholate 50mg</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>


     </tbody>
 </table>

    </div>
    
    

    <!--<script>
        
     const dataURL = JSON.parse(localStorage.getItem("preview_url"));    
        
      init_table({
        csv_path: dataURL, 
        element: 'table-container', 
        allow_download: true,
        csv_options: {separator: ',', delimiter: '"'},
        datatables_options: {"paging": false}
      });
    </script>-->