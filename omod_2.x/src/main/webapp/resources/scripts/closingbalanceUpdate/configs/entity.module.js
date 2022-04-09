
(function() {
	define([], loadpage);

	function loadpage() {
		'use strict';
		var app = angular.module('entitiesApp', ['ui.bootstrap', 'ngDialog',
				'ui.router', 'angularUtils.directives.dirPagination',
				'app.css', 'app.filters', 'app.pagination', 'app.cookies',
				'app.genericMetadataModel', 'app.restfulServices',
				'app.arvpharmacyDispenseFunctionsFactory', 'app.genericEntityController',
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
				templateUrl : 'entity.page',
				controller : 'ClosingbalanceUpdateController'
			}).state('new', {
				url : '/',
				views : {
					'' : {
						templateUrl : 'entity.page',
						controller : 'ClosingbalanceUpdateController'
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
