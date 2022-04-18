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

        <ul class="table-layout">
            <li><label>CRRF Type</label></li>
            <li>
                <select class="form-control" ng-model="entity.stockroom"
                        ng-options='stockroom.name for stockroom in stockrooms track by stockroom.uuid'
                        ng-change="stockroomDialog('stockroomChange',stockTakeCurrentPage)">
                    <option value="" selected="selected">Any</option>
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

    <div id="entities" ng-show="showStockDetails == true" class="detail-section-border-top">
        <br/>
        <table class="manage-entities-table" id="stockTakeTable">
            <thead>
            <tr>
                <th>Item</th>
                <th>Strength</th>
                <th>Pack Size</th>
                <th>Closing Balance Quantity</th>

            </tr>
            </thead>
            <tbody>
            <tr class="clickable-tr" pagination-id="__stockTake"
                dir-paginate="entity in fetchedEntities | itemsPerPage: stockTakeLimit"
                total-items="totalNumOfResults" current-page="stockTakeCurrentPage">
                <td>{{entity.itemName}}</td>
                <td>{{entity.drugStrenght}}</td>
                <td>{{entity.packSize}}</td>
                <td><input name="actualQuantity" min="0"
                           id="{{'actualQuantity-'+entity.itemUUID}}"
                           type="number" class="form-control input-sm" ng-model="entity.actualQuantity"
                           ng-blur="getActualQuantity(entity)"></td>

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
                <th>Item</th>
                <th>Strength</th>
                <th>Pack Size</th>
                <th>Closing Balance Quantity</th>
            </tr>
            </thead>
            <tbody><tr class="clickable-tr" pagination-id="__stockTakeChangeReview"
                       total-items="stockTakeChangeCounter"
                       dir-paginate="stock in stockTakeDetails | itemsPerPage: stockTakeLimitReview">
                <td>{{entity.itemName}}</td>
                <td>{{entity.drugStrenght}}</td>
                <td>{{entity.packSize}}</td>
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
