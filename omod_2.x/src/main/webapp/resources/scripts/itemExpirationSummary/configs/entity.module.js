
(function () {
	define([], loadpage);
	
	function loadpage() {
		'use strict';
		var app = angular.module('entitiesApp', ['ui.bootstrap', 'ngDialog', 'ui.router', 'angularUtils.directives.dirPagination', 'app.css',
			'app.filters', 'app.itemExpirationSummaryFunctionsFactory', 'app.pagination', 'app.cookies', 'app.genericMetadataModel', 'app.restfulServices',
			'app.genericEntityController', 'app.genericManageController']);
		app.config(function ($stateProvider, $urlRouterProvider, $provide) {
			/*
			 * Configure routes and urls. The default route is '/' which loads
			 * manageStockrooms.page. 'edit' route calls stockrooms.page -- it
			 * appends a 'uuid' to the url to edit an existing stockroom. 'new'
			 * route is called to create a new stockroom.
			 */
			$urlRouterProvider.otherwise('/');
			$stateProvider.state('/', {
				url: '/',
				templateUrl: 'entity.page',
				controller: 'ItemExpirationSummaryController'
			});
			
			$provide.factory('$exceptionHandler', function($injector) {
				return ohmis.handleException;
			});
		});
		return app;
	}
})();
