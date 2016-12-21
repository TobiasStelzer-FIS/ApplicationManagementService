sap.ui.define([
	"sap/ui/core/mvc/Controller"
], function(Controller) {
	"use strict";

	return Controller.extend("de.fisgmbh.tgh.applman.controller.Filebrowser", {

		onInit: function() {
			/*
			jQuery.ajax("url", {
				success: function(data) {
					
				},
				error: function() {
					console.log("There was an error.");
				}
			});
			*/
			var oModel = new sap.ui.model.json.JSONModel();
			this.getView().setModel(oModel);
			
			this._loadModelAtRoot();
		},
		
		_loadModelAtRoot: function() {
			var oModel = this.getView().getModel();
//			oModel.loadData("https://applmanserverp1942281469trial.hanatrial.ondemand.com/applman/browse/");
			oModel.loadData("/applman/browse/");
		},
		
		onBackToRoot: function(oEvent) {
			this._loadModelAtRoot();
		},
		
		onItemPress: function(oEvent) {
			var oListItem = oEvent.getParameter("listItem");
			var sBindingPath = oListItem.getBindingContextPath();
			console.log(sBindingPath);
			
			var oModel = this.getView().getModel();
			var sId = oModel.getProperty(sBindingPath + "/Id");
			
			this._handleNavigationRequest(sId);
		},
		
		_handleNavigationRequest: function(sId) {
			var oModel = this.getView().getModel();
			oModel.loadData("https://applmanserverp1942281469trial.hanatrial.ondemand.com/applman/browse/", "id="+sId);
			console.log("_handleNavigationRequest (sId: " + sId + ")");
		}
		
	});

});