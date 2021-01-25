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
			label: "${ui.message("openhmis.inventory.admin.itemExpirationSummary")}"
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
				${ui.message('openhmis.inventory.admin.itemExpirationSummary')}
			</span>
			<span style="float:right;">

			</span>
		</table>
		<br/>

		<div class="row" style="padding-top: 10px">
			<div class="col-xs-9">
				<div class="col-xs-2">
					<strong>
						${ui.message('openhmis.inventory.stockroom.name')}:
					</strong>
				</div>

				<div class="col-xs-6">
					<select class="form-control" ng-model="entity.stockroom"
					        ng-options='stockroom.name for stockroom in stockrooms track by stockroom.uuid'
					        ng-change="stockroomDialog('stockroomChange',itemExpiryCurrentPage)">
						<option value="" selected="selected">Any</option>
					</select>
				</div>

				<div class="col-xs-2">
					<input type="button" value="Search" class="confirm"
					       ng-click="stockroomDialog('stockroomChange',itemExpiryCurrentPage)">
				</div>
			</div>
		</div>

		<div class="row" style="padding-top: 10px">
			<div class="col-xs-9">
				<div class="col-xs-2">
					<strong>
						${ui.message('openhmis.inventory.consumption.testingPoint')}:
					</strong>
				</div>

				<div class="col-xs-6">
					<select class="form-control" ng-model="entity.department"
					        ng-options='department.name for department in departments track by department.uuid'
					        ng-change="departmentDialog('departmentChange',itemExpiryCurrentPage)">
						<option value="" selected="selected">Any</option>
					</select>
				</div>

				<div class="col-xs-2">
					<input type="button" value="Search" class="confirm"
					       ng-click="departmentDialog('departmentChange',itemExpiryCurrentPage)">
				</div>
			</div>
		</div>
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
                <span>Items Expiring within 120 days</span>
                <br/>
		<table class="manage-entities-table" id="stockTakeTable">
			<thead>
			<tr>
				<th>${ui.message('openhmis.inventory.item.name')}</th>
				<th>${ui.message('openhmis.inventory.stockroom.expiration')}</th>
				<th>${ui.message('openhmis.inventory.item.quantity')} left</th>
				
			</tr>
			</thead>
			<tbody>
			<tr class="clickable-tr" pagination-id="__stockTake"
			    dir-paginate="entity in fetchedEntities | itemsPerPage: itemExpiryLimit"
			    total-items="totalNumOfResults" current-page="itemExpiryCurrentPage">
				<td>{{entity.item.name}}</td>
				<td>{{entity.expiration | date: "yyyy-MM-dd"}}</td>
				<td>{{entity.quantity}}</td>
				
			</tr>
			</tbody>
		</table>
		${ui.includeFragment("openhmis.commons", "paginationFragment", [
				paginationId      : "__stockTake",
				onPageChange      : "loadStockDetails(itemExpiryCurrentPage)",
				onChange          : "loadStockDetails()",
				model             : "itemExpiryLimit",
				pagingFrom        : "itemExpiryPagingFrom(itemExpiryCurrentPage, itemExpiryLimit)",
				pagingTo          : "itemExpiryPagingTo(itemExpiryCurrentPage, itemExpiryLimit, totalNumOfResults)",
				showRetiredSection: "false"
		])}
		<br/>
		<br/>
		<br/>
	</div>

	<div ng-show="itemExpiryDetails.length != 0" class="detail-section-border-top">
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
			&nbsp;${ui.message('openhmis.inventory.stocktake.change.counter.label')} {{itemExpiryDetails.length}}
		</p>
		<br/>
	</div>

	<div id="showStockDetailsTable" ng-show="showStockDetailsTable == true">
		<table class="manage-entities-table" id="stockTakeChangeDetailsTable">
			<thead>
			<tr>
				<th>${ui.message('openhmis.inventory.item.name')}</th>
				<th>${ui.message('openhmis.inventory.stockroom.expiration')}</th>
				<th>${ui.message('openhmis.inventory.item.quantity')}</th>
				
			</tr>
			</thead>
			<tbody><tr class="clickable-tr" pagination-id="__stockTakeChangeReview"
			           total-items="stockTakeChangeCounter"
			           dir-paginate="stock in stockTakeDetails | itemsPerPage: stockTakeLimitReview">
				<td>{{stock.item.name}}</td>
				<td>{{stock.expiration | date: "yyyy-MM-dd"}}</td>
				<td>{{stock.quantity}}</td>
				
			</tr>
			</tbody>
		</table>
		<br/>
		<br/>
	</div>
 </div>
	

	

