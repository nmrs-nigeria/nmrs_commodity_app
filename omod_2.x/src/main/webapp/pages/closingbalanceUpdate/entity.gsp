<script type="text/javascript">
    var breadcrumbs = [
        {
            icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'
        },
        {
            label: "${ ui.message("openhmis.inventory.page")}",
            link: '${ui.pageLink("openhmis.inventory", "inventoryLanding")}'
        },
        {
            label: "${ ui.message("openhmis.inventory.manage.pharmacy.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/pharmacyInventoryDashboard.page'
        },
        {
            label: "${ui.message("openhmis.inventory.admin.closingbalanceUpdate")}"
        }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
</script>

<div ng-show="loading" class="loading-msg">
    <span>${ui.message("openhmis.inventory.stocktake.saving")}</span>
    <br/>
    <span class="loading-img">
        <img src="${ui.resourceLink("uicommons", "images/spinner.gif")}"/>
    </span>
</div>

<div ng-hide="loading">
    <div style="font-size:inherit">
        <table class="header-title">
            <span class="h1-substitue-left" style="float:left;">
                ${ui.message('openhmis.inventory.admin.closingbalanceUpdate')}
            </span>
            <span style="float:right;">

            </span>
        </table>
        <br/>

               <ul class="table-layout">
            <li><label>Reporting Period</label></li>
            <li>
                <select ng-model="reportingPeriod" class="form-control"
                        ng-options="reportingPeriod for reportingPeriod in reportingPeriods">
                </select>

            </li>
        </ul>

        <ul class="table-layout">
            <li><label>Year</label></li>
            <li>
                <select ng-model="year" class="form-control"
                        ng-options="year for year in years">
                </select>

            </li>
        </ul>

        <br/>
    </div>

    <div ng-show="showNoStockroomSelected == true" class="detail-section-border-top">
        <br/>
        <span>
            ${ui.message('openhmis.inventory.stocktake.no.stockroom.selected')}
        </span>
        <br/>
        <br/>
    </div>

    <div ng-show="showNoStockSummaries == true" class="detail-section-border-top">
        <br/>
        <span>
            ${ui.message('openhmis.inventory.stocktake.no.items')}
        </span>
        <br/>
        <br/>
    </div>

    <div id="entities" ng-show="showStockDetails == true" style="font-size: 13px; width: 100%; overflow-x: auto;">
        <br/>
        <table id="stockTakeTable" class='table table-striped table-condensed' >
            <thead>
             <tr>
                <th rowspan='2'>Item</th>
                <th rowspan='2'>Strength</th>
                <th rowspan='2'>Pack Size</th>
                <th colspan='10' style="text-align: center">Closing Balance Quantity at all Dispensaries</th>                
            </tr>
            <tr>
                <th>BULK STOCKROOM</th>
                <th>ATM </th>
                <th>Com-Pharmacy</th>
                <th>Community ART Groups</th>
                <th>Courier Delivery</th>
                <th>Dispensary</th>
                <th>Patent Medicine Store</th>
                <th>Private Clinics</th>
                <th>Other </th>
                <th>Total Closing Balance Quantity</th>              
            </tr>
            </thead>
            <tbody>
            <tr class="clickable-tr" pagination-id="__stockTake"
                dir-paginate="entity in fetchedEntities | itemsPerPage: stockTakeLimit"
                total-items="totalNumOfResults" current-page="stockTakeCurrentPage">
                <td>{{entity.item.name}}</td>
                <td>{{entity.item.strength}}</td>
                <td>{{entity.item.packSize}}</td>
                <td><input name="bulkStockQuantity" min="0"
                           id="{{'bulkStockQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.bulkStockQuantity"
                           ng-blur="getBulkStockQuantity(entity)" style="width:70px"></td>

                <td><input name="atmQuantity" min="0"
                           id="{{'atmQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.atmQuantity"
                           ng-blur="getATMQuantity(entity)" style="width:70px"></td>

                <td><input name="comPharmacyQuantity" min="0"
                           id="{{'comPharmacyQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.comPharmacyQuantity"
                           ng-blur="getComPharmacyQuantity(entity)" style="width:70px"></td>

                <td><input name="communityARTGroupsQuantity" min="0"
                           id="{{'communityARTGroupsQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.communityARTGroupsQuantity"
                           ng-blur="getCommunityARTGroupsQuantity(entity)" style="width:70px"></td>

                <td><input name="courierDeliveryQuantity" min="0"
                           id="{{'courierDeliveryQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.courierDeliveryQuantity"
                           ng-blur="getCourierDeliveryQuantity(entity)" style="width:70px"></td>

                <td><input name="dispensaryQuantity" min="0"
                           id="{{'dispensaryQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.dispensaryQuantity"
                           ng-blur="getDispensaryQuantity(entity)" style="width:70px"></td>

                <td><input name="patentMedicineStoreQuantity" min="0"
                           id="{{'patentMedicineStoreQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.patentMedicineStoreQuantity"
                           ng-blur="getPatentMedicineStoreQuantity(entity)" style="width:70px"></td>

                <td><input name="privateClinicsQuantity" min="0"
                           id="{{'privateClinicsQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.privateClinicsQuantity"
                           ng-blur="getPrivateClinicsQuantity(entity)" style="width:70px"></td>

                <td><input name="othersQuantity" min="0"
                           id="{{'othersQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.othersQuantity"
                           ng-blur="getOthersQuantity(entity)" style="width:70px"></td>


                <td><input name="actualQuantity" min="0"
                           id="{{'actualQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.actualQuantity"
                           ng-blur="getActualQuantity(entity)" style="width:70px"></td>
            </tr>
            </tbody>
        </table>
        ${ui.includeFragment("openhmis.commons", "paginationFragment", [
                paginationId      : "__stockTake",
                onPageChange      : "loadStockDetails(stockTakeCurrentPage)",
                onChange          : "loadStockDetails()",
                model             : "stockTakeLimit",
                pagingFrom        : "stockTakePagingFrom(stockTakeCurrentPage, stockTakeLimit)",
                pagingTo          : "stockTakePagingTo(stockTakeCurrentPage, totalNumOfResults, totalNumOfResults)",
                showRetiredSection: "false"
        ])}
        <br/>
        <br/>
        <br/>
    </div>

    <div ng-show="stockTakeDetails.length != 0" class="detail-section-border-top">
        <br/>
        <p>
            <a ng-show="showStockDetailsTable == false" id="stockTakehchange" class="btn btn-grey" ui-sref="new"
               ng-click="showTableDetails()">
                ${ui.message('openhmis.inventory.stocktake.change.showDetails')}
            </a>
            <a ng-show="showStockDetailsTable == true" id="stockTakehchange" class="btn btn-grey" ui-sref="new"
               ng-click="hideTableDetails()">
                ${ui.message('openhmis.inventory.stocktake.change.hideDetails')}
            </a>
            &nbsp;${ui.message('openhmis.inventory.stocktake.change.counter.label')} {{stockTakeDetails.length}}
        </p>
        <br/>
    </div>

    <div id="showStockDetailsTable" ng-show="showStockDetailsTable == true">
        <table class="manage-entities-table" id="stockTakeChangeDetailsTable">
            <thead>
            <tr>
                <th colspan='2'>Item</th>
                <th colspan='2'>Strength</th>
                <th colspan='2'>Pack Size</th>
                <th rowspan='9'>Closing Balance Quantity at Dispensaries</th>                
            </tr>
            <tr>
                <th>ATM</th>
                <th>Com-Pharmacy</th>
                <th>Community ART Groups</th>
                <th>Courier Delivery</th>
                <th>Dispensary</th>
                <th>Patent Medicine Store</th>
                <th>Private Clinics</th>
                <th>Others</th>
                <th>Total Closing Balance Quantity</th>              
            </tr>
            </thead>
            <tbody><tr class="clickable-tr" pagination-id="__stockTakeChangeReview"
                       total-items="stockTakeChangeCounter"
                       dir-paginate="stock in stockTakeDetails | itemsPerPage: stockTakeLimitReview">
                <td>{{entity.item.name}}</td>
                <td>{{entity.item.strength}}</td>
                <td>{{entity.item.packSize}}</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            </tbody>
        </table>
        <br/>
        <br/>
    </div>

    <div ng-show="showStockDetails == true" class="detail-section-border-top">
        <br/>
        <input type="button" class="cancel" value="{{messageLabels['general.cancel']}}" ng-click="cancel()"/>
        <input type="button" class="confirm right"
               value="{{messageLabels['general.save']}}" ng-click="saveOrUpdate()"/>
        <br/>
    </div>

    <div id="stockroomChange" class="dialog" style="display:none;">
        <div class="dialog-header">
            <span>
                <i class="icon-info-sign"></i>

                <h3>${ui.message('openhmis.inventory.stocktake.stockroom.change.prompt.header')}</h3>
            </span>
            <i class="icon-remove cancel" style="float:right; cursor: pointer;" ng-click="closeThisDialog()"></i>
        </div>

        <div class="dialog-content form">
            <div>
                <p>${ui.message('openhmis.inventory.stocktake.stockroom.change.prompt.message')}</p>
            </div>

            <div class="row ngdialog-buttons detail-section-border-top">
                <br/>
                <input type="button" class="cancel" value="{{messageLabels['general.cancel']}}"
                       ng-click="closeThisDialog('Cancel')"/>
                <input type="submit" class="confirm right"
                       value="${ui.message('openhmis.inventory.stocktake.ok')}"/>
            </div>
        </div>
    </div>
</div>
