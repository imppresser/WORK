//modify.js

$(document).ready(function(){
	$("#cargorank").select2({
		data:[{id:'A',text:'A'},{id:'B',text:'B'},{id:'C',text:'C'},{id:'D',text:'D'}]
	});
	
	 $('#startDatepicker').datepicker();
	 $('#endDatepicker').datepicker();

});

app.controller("modifyCtrl", ['$scope', 'Util','Database',
function($scope, Util, Database) {
	var urlArray = Util.splitUrl();
	var id = urlArray[urlArray.length-1];

	var query = new Database.Query('Cargo');
		query.get(id,{
			success:function(obj){
				$scope.cargo = obj;
				$("#cargorank").select2("val",obj.attributes.cargorank)
			},error:function(error){
				console.log(error);
			}
		})

	$scope.save = function () {
		var rank = $("#cargorank").select2("val");
	
		$scope.cargo.save({cargorank:rank},{
			success:function(obj){
				//alert('修改成功');
				$("#alert").modal('show')
			},error:function(err){
				console.log(err);
			}
		});
	}
	var user = Database.User.currentUser();

}]);