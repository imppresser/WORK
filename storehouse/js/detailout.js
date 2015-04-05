//detailout.js

$(document).ready(function(){
	/*$("#cargorank").select2({
		data:[{id:'A',text:'A'},{id:'B',text:'B'},{id:'C',text:'C'},{id:'D',text:'D'}]
	});*/
	

});

app.controller("detailoutCtrl", ['$scope', 'Util','Database',
function($scope, Util, Database) {
	var urlArray = Util.splitUrl();
	var id = urlArray[urlArray.length-1];

	var query = new Database.Query('CargoOut');
		query.get(id,{
			success:function(obj){
				$scope.cargoout = obj;
				//$("#cargorank").select2("val",obj.attributes.cargorank)
			},error:function(error){
				console.log(error);
			}
		})
}]);