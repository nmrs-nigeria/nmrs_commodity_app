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
            label: "${ ui.message("openhmis.inventory.admin.task.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.inventory/inventory/inventoryTasksDashboard.page'
        },
        {
            label: "${ui.message("openhmis.inventory.admin.labclosingbalanceUpdate")}"
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
                ${ui.message('openhmis.inventory.admin.labclosingbalanceUpdate')}
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
        <table class='table table-striped table-condensed' id="stockTakeTable">
            <thead>
             <tr>
                <th rowspan='2'>Item</th>
                <th rowspan='2'>Strength</th>
                <th rowspan='2'>Pack Size</th>
                <th colspan='20' style="text-align: center">Closing Balance Quantity at all Department</th>                
            </tr>
            <tr>
                <th>LAB STOCKROOM</th>
                <th>ANC</th>
                <th>COM</th>
                <th>EID</th>
                <th>EMERG.</th>
                <th>FP</th>
                <th>IN-PAT</th>
                <th>L-AND-D</th>
                <th>LAB</th>
                <th>MAL</th>     
                <th>MOB</th>
                <th>OPD</th>
                <th>OSS</th>
                <th>OTH</th>
                <th>PAED</th>
                <th>PP</th>
                <th>STI</th>
                <th>TB</th>
                <th>VCT</th>
                <th>Total Closing Balance Quantity</td>
            </tr>
            </thead>
            <tbody>

            <tr class="clickable-tr" pagination-id="__stockTake"
                dir-paginate="entity in fetchedEntities | itemsPerPage: stockTakeLimit"
                total-items="totalNumOfResults" current-page="stockTakeCurrentPage">
                <td>{{entity.item.name}}</td>
                <td>{{entity.item.strength}}</td>
                <td>{{entity.item.packSize}}</td>
                <td><input name="labStockQuantity" min="0"
                           id="{{'labStockQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.labStockQuantity"
                           ng-blur="getLabStockQuantity(entity)" style="width:70px"></td>

                <td><input name="ancQuantity" min="0"
                           id="{{'ancQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.ancQuantity"
                           ng-blur="getAncQuantity(entity)" style="width:70px"></td>

                <td><input name="comQuantity" min="0"
                           id="{{'comQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.comQuantity"
                           ng-blur="getComQuantity(entity)" style="width:70px" ></td>

                <td><input name="eidQuantity" min="0"
                           id="{{'eidQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.eidQuantity"
                           ng-blur="getEidQuantity(entity)" style="width:70px"></td>

                <td><input name="emergQuantity" min="0"
                           id="{{'emergQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.emergQuantity"
                           ng-blur="getEmergQuantity(entity)" style="width:70px"></td>

                <td><input name="fpQuantity" min="0"
                           id="{{'fpQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.fpQuantity"
                           ng-blur="getFpQuantity(entity)" style="width:70px"></td>

                <td><input name="inpatQuantity" min="0"
                           id="{{'inpatQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.inpatQuantity"
                           ng-blur="getInpatQuantity(entity)" style="width:70px"></td>

                <td><input name="landdQuantity" min="0"
                           id="{{'landdQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.landdQuantity"
                           ng-blur="getLanddQuantity(entity)" style="width:70px"></td>

                <td><input name="labQuantity" min="0"
                           id="{{'labQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.labQuantity"
                           ng-blur="getLabQuantity(entity)" style="width:70px"></td>

                <td><input name="malQuantity" min="0"
                           id="{{'malQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.malQuantity"
                           ng-blur="getMalQuantity(entity)" style="width:70px"></td>

                <td><input name="mobQuantity" min="0"
                           id="{{'mobQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.mobQuantity"
                           ng-blur="getMobQuantity(entity)" style="width:70px"></td>

                <td><input name="opdQuantity" min="0"
                           id="{{'opdQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.opdQuantity"
                           ng-blur="getOpdQuantity(entity)" style="width:70px"></td>

                <td><input name="ossQuantity" min="0"
                           id="{{'ossQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.ossQuantity"
                           ng-blur="getOssQuantity(entity)" style="width:70px"></td>

                <td><input name="othQuantity" min="0"
                           id="{{'othQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.othQuantity"
                           ng-blur="getOthQuantity(entity)" style="width:70px"></td>

                <td><input name="paedQuantity" min="0"
                           id="{{'paedQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.paedQuantity"
                           ng-blur="getPaedQuantity(entity)" style="width:70px"></td>

                <td><input name="ppQuantity" min="0"
                           id="{{'ppQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.ppQuantity"
                           ng-blur="getPpQuantity(entity)" style="width:70px" ></td>

                <td><input name="stiQuantity" min="0"
                           id="{{'stiQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.stiQuantity"
                           ng-blur="getStiQuantity(entity)" style="width:70px"></td>

                <td><input name="tbQuantity" min="0"
                           id="{{'tbQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.tbQuantity"
                           ng-blur="getTbQuantity(entity)" style="width:70px"></td>

                <td><input name="vctQuantity" min="0"
                           id="{{'vctQuantity-'+entity.item.uuid}}"
                           type="number" class="form-control input-sm" ng-model="entity.vctQuantity"
                           ng-blur="getVctQuantity(entity)" style="width:70px"></td>

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
                <th rowspan='2'>Item</th>
                <th rowspan='2'>Strength</th>
                <th rowspan='2'>Pack Size</th>
                <th colspan='19' style="text-align: center">Closing Balance Quantity at all Department</th>                
            </tr>
            <tr>
                <th>LAB STOCKROOM</th>
                <th>ANC</th>
                <th>COM</th>
                <th>EID</th>
                <th>EMERG.</th>
                <th>FP</th>
                <th>IN-PAT</th>
                <th>L-AND-D</th>
                <th>LAB</th>
                <th>MAL</th>     
                <th>MOB</th>
                <th>OPD</th>
                <th>OSS</th>
                <th>OTH.</th>
                <th>PAED</th>
                <th>PP</th>
                <th>STI</th>
                <th>TB</th>
                <th>VCT</th>
                <th>Total Closing Balance Quantity</td>
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
                <td></td>
                <td></td>
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
