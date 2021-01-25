

(function () {
	'use strict';
	
	var base = angular.module('app.genericEntityController');
	base.controller("ItemExpirationSummaryController", ItemExpirationSummaryController);
	ItemExpirationSummaryController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'EntityRestFactory',
		'ItemExpirationSummaryModel', 'ItemExpirationSummaryRestfulService', 'PaginationService', 'EntityFunctions', 'ItemExpirationSummaryFunctions',
		'CookiesService'];
	
	function ItemExpirationSummaryController($stateParams, $injector, $scope, $filter, EntityRestFactory, ItemExpirationSummaryModel,
	                             ItemExpirationSummaryRestfulService, PaginationService, EntityFunctions, ItemExpirationSummaryFunctions,
	                             CookiesService) {
		var self = this;
		var entity_name_message_key = "openhmis.inventory.admin.itemExpirationSummary";
		var REST_ENTITY_NAME = "itemExpirationSummary";
		
		// @Override
		self.setRequiredInitParameters = self.setRequiredInitParameters || function () {
				self.bindBaseParameters(INVENTORY_MODULE_NAME, REST_ENTITY_NAME, entity_name_message_key, INVENTORY_TASK_DASHBOARD_PAGE_URL);
				self.checkPrivileges(TASK_ACCESS_VIEW_STOCK_OPERATIONS_PAGE);
			}
		
		/**
		 * Initializes and binds any required variable and/or function specific to entity.page
		 * @type {Function}
		 */
		// @Override
		self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
			|| function (uuid) {
				self.loadStockrooms();
				self.loadDepartments();
				$scope.showNoStockroomSelected = true;
				$scope.showNoStockSummaries = false;
				$scope.showStockDetails = false;
				$scope.showStockChangeDetails = false;
				$scope.showStockDetailsTable = false;
				$scope.itemExpiryDetails = [];
				$scope.loading = false;
				
				$scope.stockroomDialog = function (stockroomChange, itemExpiryCurrentPage) {
					console.log("Stockroom Has been selected");
					console.log("stockroomChange: " +  stockroomChange);
					console.log("itemExpiryCurrentPage: " +  itemExpiryCurrentPage);
					if ($scope.itemExpiryDetails.length != 0) {
						$scope.itemExpiryDetails = [];
						console.log("Item Expiry Details is not equal 0: Calling stockroomChangeDialog function");
						self.stockroomChangeDialog(stockroomChange);
					} else {
						console.log("Item Expiry Details is equal 0: Calling loadStockDetails function");					
						$scope.loadStockDetails(itemExpiryCurrentPage);
					}
				}

				$scope.departmentDialog = function (departmentChange, itemExpiryCurrentPage) {
					console.log("Department Has been selected");
					console.log("departmentChange: " +  departmentChange);
					console.log("itemExpiryCurrentPage: " +  itemExpiryCurrentPage);
					if ($scope.itemExpiryDetails.length != 0) {
						$scope.itemExpiryDetails = [];
						console.log("Item Expiry Details is not equal 0: Calling stockroomChangeDialog function");
						self.departmentChangeDialog(departmentChange);
					} else {
						console.log("Item Expiry Details is equal 0: Calling loadStockDetails function");					
						$scope.loadStockDetailsDepartment(itemExpiryCurrentPage);
					}
				}
				
				$scope.loadStockDetails = function (itemExpiryCurrentPage) {
					if ($scope.entity.stockroom != null) {
						var stockroom_uuid = $scope.entity.stockroom.uuid;
						console.log("Inside loadStockDetails (Stockroom UUID): " + stockroom_uuid);
					
						self.loadStockDetails(stockroom_uuid, itemExpiryCurrentPage);
						
						$scope.itemExpiryLimit = CookiesService.get(stockroom_uuid + 'itemExpiryLimit') || 5;
						$scope.itemExpiryCurrentPage = CookiesService.get(stockroom_uuid + 'itemExpiryCurrentPage') || 1;
						$scope.itemExpiryPagingFrom = PaginationService.pagingFrom;
						$scope.itemExpiryPagingTo = PaginationService.pagingTo;
					} else {
						$scope.showNoStockroomSelected = true;
						$scope.showNoStockSummaries = false;
						$scope.showStockChangeDetails = false;
						$scope.itemExpiryDetails = [];
						$scope.showStockDetails = false;
						$scope.showStockDetailsTable = false;
					}
				}

				$scope.loadStockDetailsDepartment = function (itemExpiryCurrentPage) {
					if ($scope.entity.department != null) {
						var department_uuid = $scope.entity.department.uuid;
						console.log("Inside loadStockDetailsDepartment (Department UUID): " + department_uuid);
					
						self.loadStockDetailsDepartment(department_uuid, itemExpiryCurrentPage);
						
						$scope.itemExpiryLimit = CookiesService.get(department_uuid + 'itemExpiryLimit') || 5;
						$scope.itemExpiryCurrentPage = CookiesService.get(department_uuid + 'itemExpiryCurrentPage') || 1;
						$scope.itemExpiryPagingFrom = PaginationService.pagingFrom;
						$scope.itemExpiryPagingTo = PaginationService.pagingTo;
					} else {
						$scope.showNoStockroomSelected = true;
						$scope.showNoStockSummaries = false;
						$scope.showStockChangeDetails = false;
						$scope.itemExpiryDetails = [];
						$scope.showStockDetails = false;
						$scope.showStockDetailsTable = false;
					}
				}
				
				$scope.showTableDetails = function () {
					$scope.showStockDetailsTable = true;
				}
				
				$scope.hideTableDetails = function () {
					$scope.showStockDetailsTable = false;
				}
				
				$scope.getActualQuantity = function (entity) {
					if (entity.actualQuantity >= 0) {
						entity.id = entity.item.uuid + "_" + entity.expiration;
						self.getNewStock(entity);
					}
				}
				
			}
		
		self.stockroomChangeDialog = self.stockroomChangeDialog || function (id) {
				console.log("inside stockroomChangeDialog: " + id);
				ItemExpirationSummaryFunctions.stockroomChangeDialog(id, $scope);
			}
		
		self.getNewStock = self.getNewStock || function (newStock) {
				console.log(" $scope.itemExpiryDetails: " + $scope.itemExpiryDetails);
				console.log("newStock: " + newStock);
				var index = EntityFunctions.findIndexByKeyValue($scope.itemExpiryDetails,newStock.id);
				if (index < 0 ) {
					$scope.itemExpiryDetails.push(newStock);
				} else {
					$scope.itemExpiryDetails[index] = newStock;
				}
				
				/*
				* This loop is to remove any stock that had the actualQuantity updated and before saving changed again to either a value
				* equal to null or a value equal to the quantity
				* */
				for (var i = 0; i < $scope.itemExpiryDetails.length; i++) {
					if ($scope.itemExpiryDetails[i].actualQuantity == $scope.itemExpiryDetails[i].quantity || $scope.itemExpiryDetails[i].actualQuantity == null) {
						$scope.itemExpiryDetails.splice(i, 1);
					}
				}
				
				if ($scope.itemExpiryDetails.length > 0) {
					$scope.showStockChangeDetails = true;
				} else {
					$scope.showStockDetailsTable = false;
				}
				$scope.itemExpiryDetails = $filter('orderBy')($scope.itemExpiryDetails, ['item.name', 'expiration']);
			}
		
		self.loadStockrooms = self.loadStockrooms || function () {
				console.log("loadStockrooms restful service");			
				ItemExpirationSummaryRestfulService.loadStockrooms(INVENTORY_MODULE_NAME, self.onLoadStockroomsSuccessful);
			}

		self.loadDepartments = self.loadDepartments || function () {
				console.log("loadDepartments restful service");			
				ItemExpirationSummaryRestfulService.loadDepartments(INVENTORY_MODULE_NAME, self.onLoadDepartmentsSuccessful);
			}
		
		self.loadStockDetails = self.loadStockDetails || function (stockroomUuid, itemExpiryCurrentPage) {
				console.log("Inside loadStockDetails 2: ");				
				console.log("stockroomUuid: " + stockroomUuid);		
				console.log("itemExpiryCurrentPage: " + itemExpiryCurrentPage);		
				itemExpiryCurrentPage = itemExpiryCurrentPage || $scope.itemExpiryCurrentPage;
				CookiesService.set(stockroomUuid + 'itemExpiryCurrentPage', itemExpiryCurrentPage);
				CookiesService.set(stockroomUuid + 'itemExpiryLimit', $scope.itemExpiryLimit);
				
				ItemExpirationSummaryRestfulService.loadStockDetails(stockroomUuid, CookiesService.get(stockroomUuid + 'itemExpiryCurrentPage'),
					CookiesService.get(stockroomUuid + 'itemExpiryLimit'),
					self.onLoadStockDetailsSuccessful);
			}
		
		self.loadStockDetailsDepartment = self.loadStockDetailsDepartment || function (departmentUuid, itemExpiryCurrentPage) {
				console.log("Inside loadStockDetailsDepartment 2: ");				
				console.log("departmentUuid: " + departmentUuid);		
				console.log("itemExpiryCurrentPage: " + itemExpiryCurrentPage);		
				itemExpiryCurrentPage = itemExpiryCurrentPage || $scope.itemExpiryCurrentPage;
				CookiesService.set(departmentUuid + 'itemExpiryCurrentPage', itemExpiryCurrentPage);
				CookiesService.set(departmentUuid + 'itemExpiryLimit', $scope.itemExpiryLimit);
				
				ItemExpirationSummaryRestfulService.loadStockDetailsDepartment(departmentUuid, CookiesService.get(departmentUuid + 'itemExpiryCurrentPage'),
					CookiesService.get(departmentUuid + 'itemExpiryLimit'),
					self.onLoadStockDetailsSuccessful);
			}

		//callback
		self.onLoadStockroomsSuccessful = self.onLoadStockroomsSuccessful || function (data) {
				console.log("onLoadStockroomsSuccessful: " + data.results);		
				console.log("onLoadStockroomsSuccessful: " + data.results[0]);						
				$scope.stockrooms = data.results;
			}
		
		self.onLoadDepartmentsSuccessful = self.onLoadDepartmentsSuccessful || function (data) {
				console.log("onLoadDepartmentsSuccessful: " + data.results);		
				console.log("onLoadDepartmentsSuccessful: " + data.results[0]);						
				$scope.departments = data.results;
			}

		self.onLoadStockDetailsSuccessful = self.onLoadStockDetailsSuccessful || function (data) {
				$scope.fetchedEntities = data.results;
				console.log("onLoadStockDetailsSuccessful: " + data.results);		
				console.log("onLoadStockDetailsSuccessful: " + data.results[0]);		
				for (var i = 0; i < $scope.fetchedEntities.length; i++) {
					$scope.fetchedEntities[i].id = $scope.fetchedEntities[i].item.uuid + "_" + $scope.fetchedEntities[i].expiration;
					var index = EntityFunctions.findIndexByKeyValue($scope.itemExpiryDetails,$scope.fetchedEntities[i].id);
					if (index > -1) {
						$scope.fetchedEntities[i].actualQuantity = $scope.itemExpiryDetails[index].actualQuantity;
					}
				}
				
				$scope.totalNumOfResults = data.length;
				
				if (data.results.length != 0) {
					$scope.showStockDetails = true;
					$scope.showNoStockroomSelected = false;
					$scope.showNoStockSummaries = false;
				} else {
					$scope.showNoStockroomSelected = false;
					$scope.showNoStockSummaries = true;
					$scope.showStockDetails = false;
				}
			}
		
		self.onChangeEntityError = self.onChangeEntityError || function (error) {
				emr.errorAlert(error);
				$scope.loading = false;
			}
		
		/**
		 * All post-submit validations are done here.
		 * @return boolean
		 */
		// @Override
		
		
		/* ENTRY POINT: Instantiate the base controller which loads the page */
		$injector.invoke(base.GenericEntityController, self, {
			$scope: $scope,
			$filter: $filter,
			$stateParams: $stateParams,
			EntityRestFactory: EntityRestFactory,
			PaginationService: PaginationService,
			GenericMetadataModel: ItemExpirationSummaryModel,
			EntityFunctions: EntityFunctions
		});
	}
})();
