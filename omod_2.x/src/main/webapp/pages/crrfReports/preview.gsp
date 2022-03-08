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

 <div class="container" style="margin-top:20px; overflow: scroll">

      <h1>Report Preview</h1>
      
      <div id='table-container'></div>

    </div>
    
    

    <script>
        
     const dataURL = JSON.parse(localStorage.getItem("preview_url"));    
        
      init_table({
        csv_path: dataURL, 
        element: 'table-container', 
        allow_download: true,
        csv_options: {separator: ',', delimiter: '"'},
        datatables_options: {"paging": false}
      });
    </script>