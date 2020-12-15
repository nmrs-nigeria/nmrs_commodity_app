
(function() {
	define([], loadpage);

	function loadpage() {
		'use strict';
		var app = angular.module('entitiesApp', ['ui.bootstrap', 'ngDialog',
				'ui.router', 'angularUtils.directives.dirPagination',
				'app.css', 'app.filters', 'app.pagination', 'app.cookies',
				'app.genericMetadataModel', 'app.restfulServices',
				'app.consumptionFunctionsFactory', 'app.genericEntityController',
				'app.genericManageController']);
		app.config(function($stateProvider, $urlRouterProvider, $provide) {
			/*
			 * Configure routes and urls. The default route is '/' which loads
			 * manageItems.page. 'edit' route calls item.page -- it
			 * appends a 'uuid' to the url to edit an existing item. 'new'
			 * route is called to create a new item.
			 */
			$urlRouterProvider.otherwise('/');
			$stateProvider.state('/', {
				url : '/',
				templateUrl : 'manageEntities.page',
				controller : 'ManageConsumptionController'
			}).state('edit', {
				url : '/:uuid',
				views : {
					'' : {
						templateUrl : 'entity.page',
						controller : 'ConsumptionController'
					}
				}
			}).state('new', {
				url : '/',
				views : {
					'' : {
						templateUrl : 'entity.page',
						controller : 'ConsumptionController'
					}
				}
			});
			
			$provide.factory('$exceptionHandler', function($injector) {
				return ohmis.handleException;
			});
		});
		return app;
	}
})();
