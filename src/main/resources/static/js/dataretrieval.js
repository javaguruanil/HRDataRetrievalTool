//'use strict';

(function() {

	var app = angular.module("hackathon", [])
	.controller("DataRetrievalController", DataRetrievalController);
	
	function DataRetrievalController($scope, $http) {

		var vm = this;
		vm.selectedList = {
			"emp_id" : true
		};
		vm.jsonData=[];
		vm.getTablesData = getTablesData;
		vm.submitData = submitData;
		vm.generateReqData = generateReqData;
		vm.openPdf = openPdf;
		vm.sendMail = sendMail;
		vm.loadJsonData = loadJsonData;
//		vm.checkShow=checkShow;
		vm.jsonArray=[];
		
		getTablesData();
		
		function sendMail(){
			
			if(vm.email != undefined && vm.email != null){
				
				$("#myModal").modal('hide');
				
				if($("#loadingDiv").is(":hidden")){
					$("#loadingDiv").show();
				}else{
					 $('#loadingDiv').hide();
				}
				
				
				document.getElementById("loadingDiv").display="";
				
				var obj = {};
				obj.email = vm.email;
				obj.dataList = emailJson;
				obj.subject = titleDescription;
				$http.post('sendMail', obj).then(successCallback, vm.errorCallback);
				
				function successCallback(response){
					
					if($("#loadingDiv").is(":hidden")){
						$("#loadingDiv").show();
					}else{
						 $('#loadingDiv').hide();
					}
					
					emailJson = []
					//$("#myModal").modal('hide');
					var data = response.data;
					alert(data.result);
				}
				
				if($("#loadingDiv").is(":hidden")){
					$("#loadingDiv").show();
				}else{
					 $('#loadingDiv').hide();
				}
				 
			}else{
				alert('Please enter email.');
			}
			
			if($("#loadingDiv").is(":hidden")){
				$("#loadingDiv").show();
			}else{
				 $('#loadingDiv').hide();
			}
			
			
		}
		
		function openPdf(index){
			html2canvas(document.getElementById('exportthis'+index), {
	            onrendered: function (canvas) {
	                var data = canvas.toDataURL();
	                var docDefinition = {
	                    content: [{
	                        image: data,
	                        width: 500,
	                    }]
	                };
	                pdfMake.createPdf(docDefinition).download("tableData.pdf");
	            }
	        });
		}
		
		function errorCallback(errorResponse){
			console.log(errorResponse);
		}
		
		function getTablesData(){
			$http.get('getTableColumnInfo').then(successCallback, vm.errorCallback);
			function successCallback(response){
//				console.log(response.data);
				vm.dataMap = response.data;
			}
		}
		
		var reqData = {
			"employee" : ["emp_id"]
		};
		function generateReqData(key, index, val){
//			console.log(vm.selectedList[val]);
			if(vm.selectedList[val]){
				reqData[key] = reqData[key] || [];
				reqData[key].push(val);
			}else{
				reqData[key].splice(index, 1);
			}
		}
		
		vm.titleArray = [];
		function submitData(){
			
			if(vm.titleDesc.trim().length == 0){
				alert("Please enter description.");
				return false;
			}
			$http.post('submitColumnInfo', reqData).then(successCallback, vm.errorCallback);
			function successCallback(response){
//				console.log(response.data);
				$("#descModal").modal('hide');
				$("#wrapper").toggleClass("active");
				angular.forEach(reqData, function(val, key){
					reqData[key] = [];
				});
				reqData = {
					"employee" : ["emp_id"]
				};
//				var reqData = {};
				vm.selectedList = {
						"emp_id" : true
				};
//				vm.checkShow();
				vm.titleArray.push(vm.titleDesc);
				vm.jsonArray.push(response.data);
				vm.titleDesc = '';
//				vm.jsonData = response.data;
//				vm.cols = Object.keys(vm.jsonData[0]);
			}
		}
		var emailJson = [];
		var titleDescription = '';
		function loadJsonData(reqData, title){
			vm.email = '';
			titleDescription = title;
			emailJson = reqData;
		}
		
//		function checkShow(){
//			angular.forEach(vm.jsonArray,function(arr,index){
//				vm.showButton[index]=(index+1)===vm.jsonArray.length;
//			});
//		}
	}
})();