//addin.js

$(document).ready(function(){
	$("#cargorank").select2({
		data:[{id:'A',text:'A'},{id:'B',text:'B'},{id:'C',text:'C'},{id:'D',text:'D'}]
	});
});


app.controller("addinCtrl", ['$scope', 'Util','Database',
function($scope, Util, Database) {
	$scope.save = function () {

	var cargo = new Database.Object('Cargo',{
	 	cargoname: $("#cargoname").val(),
		cargonumber: $("#cargonumber").val(),
		cargosize: $("#cargosize").val(),
		cargoweight: $("#cargoweight").val(),
		cargoaddress: $("#cargoaddress").val(),
		cargorank: $("#cargorank").select2("val"),
		cargoreminder: $("#cargoreminder").val()
	 });
	cargo.save(null,{
			success:function(obj){
				$("#alert").modal('show')
				console.log(obj);
			},error:function(err){
				console.log(err);
			}
	});
	}
}]);