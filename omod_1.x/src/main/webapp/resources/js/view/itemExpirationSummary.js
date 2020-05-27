/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
define(
    [
        openhmis.url.backboneBase + 'js/view/generic',
        openhmis.url.inventoryBase + 'js/model/stockroom',
        openhmis.url.inventoryBase + 'js/view/stockroom',
        openhmis.url.inventoryBase + 'js/model/itemExpirationSummary',
        openhmis.url.inventoryBase + 'js/view/operation',
        'js!' + openhmis.url.inventoryBase + 'js/util.js',
        'link!' + openhmis.url.inventoryBase + 'css/style.css'
    ],

    function(openhmis) {
        openhmis.InventoryStockTakeAddEditView = Backbone.View.extend({
            edit: function() {/*make sure that list entries are not editable by clicking*/},
        });

        openhmis.InventoryStockTakeSearchableListView = openhmis.GenericSearchableListView.extend({
            tmplFile: openhmis.url.inventoryBase + 'template/inventoryStockTake.html',
            tmplSelector: '#inventory-stock-take-list',

            events: {
                'click .submit' : 'save',
                'click #show-details' : 'toggleAdjustmentChangesDetail',
            },

            initialize: function(options) {
                openhmis.GenericSearchableListView.prototype.initialize.call(this, options);
                openhmis.StockTakeChangeCounter = 0;
                this.itemStockDetails = {};
                this.searchView.on('resetItemStockAdjustments', this.resetItemStockAdjustments);
                this.stockTakeDetailsView = new openhmis.StockTakeAdjustmentsList({
                    model: new openhmis.GenericCollection([], {
                        model: openhmis.ItemStockSummary
                    }),
                    showRetiredOption: false,
                    showRetired: false,
                    listFields: ['item','expiration', 'quantity'],
                    itemView: openhmis.InventoryStockTakeListDetailItemView
                });
            },

            resetItemStockAdjustments: function() {
                this.itemStockDetails = {};
                openhmis.StockTakeChangeCounter = 0;
            },

            onSearch: function(options, sender) {
                openhmis.GenericSearchableListView.prototype.onSearch.call(this, options, sender);
                this.stockroom = sender.searchFilter ? sender.searchFilter.stockroom_uuid : null;
            },

            addOne: function(model, schema, lineNumber) {
                openhmis.GenericSearchableListView.prototype.addOne.call(this, model, schema, lineNumber);
                var self = this
                model.view.on('quantityChange', function() {
                    var hash = this.model.get('item').get('uuid') + '_' + this.model.get('expiration');
                    if(this.model.get('actualQuantity') != null
                            && !isNaN(this.model.get('actualQuantity'))
                            && this.model.get('actualQuantity') != this.model.get('quantity')) {
                        self.itemStockDetails[hash] = this.model;
                    } else {
                        delete self.itemStockDetails[hash];
                    }
                    self.updateGlobalStockTakeChangeCounter()
                    self.renderAdjustmentChangesShort();
                });
                var hash = model.get('item').get('uuid') + '_' + model.get('expiration');
                $('.actual-quantity').forceNumericOnly();
                if (hash in self.itemStockDetails) {
                	var actual_quantity_id = '#actual-quantity-' + hash;
                    $(actual_quantity_id).val(this.itemStockDetails[hash].get('actualQuantity'));
                }
            },

            showProcessingDialog: function() {
                $('.cancel').prop('disabled', true);
                $('.submit').prop('disabled', true);

                $('#processingDialog').dialog({
                    dialogClass: "no-close",
                    title: "Processing Operation",
                    draggable: false,
                    resizable: false,
                    modal: true,
                    width: 350
                });
            },

            hideProcessingDialog: function() {
                $('.cancel').prop('disabled', false);
                $('.submit').prop('disabled', false);

                $('#processingDialog').dialog("close");
            },

          

            render: function() {
                openhmis.GenericSearchableListView.prototype.render.call(this);
                this.renderAdjustmentChangesShort();
            },

            updateGlobalStockTakeChangeCounter: function() {
                openhmis.StockTakeChangeCounter = Object.keys(this.itemStockDetails).length;
            },

        

            convertToArray: function(associativeArray) {
                var array = [];
                for (var key in associativeArray) {
                    array.push(associativeArray[key]);
                }
                return array;
            }
        });

        openhmis.InventoryStockTakeListItemView = openhmis.GenericListItemView.extend({
            tmplFile: openhmis.url.inventoryBase + 'template/inventoryStockTake.html',
            tmplSelector: '#inventory-stock-take-list-item',

            events: {
                'change .actual-quantity' : 'changeItemStockDetail',
            },

            changeItemStockDetail: function(event) {
                var inputValue = $(event.currentTarget).val();
                if (inputValue < 0) {
                    inputValue = 0;
                    $(event.currentTarget).val(inputValue);
                };
                this.model.set('actualQuantity', parseInt(inputValue));
                this.trigger('quantityChange', this);
            },
        });

        openhmis.StockTakeAdjustmentsList = openhmis.GenericListView.extend({
            tmplFile: openhmis.url.inventoryBase + 'template/inventoryStockTake.html',
            tmplSelector: '#stockTakeAdjustments-list',

            render: function(extraContext) {
                openhmis.GenericListView.prototype.render.call(this, extraContext);
            }
        });

        openhmis.InventoryStockTakeListDetailItemView = openhmis.GenericListItemView.extend({
            tmplFile: openhmis.url.inventoryBase + 'template/inventoryStockTake.html',
            tmplSelector: '#inventory-stock-take-list-detail-item',
        });

        return openhmis;
    }
);
