//detail.js

$(document).ready(function(){


});

app.controller("detailCtrl", ['$scope', 'Util','Database',
function($scope, Util, Database) {
	var urlArray = Util.splitUrl();
	var id = urlArray[urlArray.length-1];

	var query = new Database.Query('Cargo');
		query.get(id,{
			success:function(obj){
				$scope.cargo = obj;
			},error:function(error){
				console.log(error);
			}
		})
}]);