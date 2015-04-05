//out.js

$(document).ready(function(){
	$("#searchT").select2({
		data:[{id:'cargoname',text:'物品名称'},{id:'cargonumber',text:'数量'},{id:'cargosize',text:'大小'},{id:'cargoweight',text:'重量'}
		,{id:'cargoaddress',text:'来源地'},{id:'cargorank',text:'等级'},{id:'cargooutdate',text:'出库日期'}]
	});
});

app.controller("outCtrl", ['$scope', 'Util','Database',
function($scope, Util, Database) {
	var page = 1;
	var PER_NUM = 5;
	var searchType;
	var searchValue;
	var flag = 0;

	function setPageActive(_page){
		$scope.activePage = [];
		$scope.activePage[_page-1] = true;
	}

	//function countTotalPages(){
	//	var query = new Database.Query('Cargo');
	//	query.count({
	//		success:function(count){
	//			console.log(count);
	//			var totalPage = Math.ceil(count.number/PER_NUM);
	//			$scope.pages = [];
	//			for(var i = 0; i < totalPage; i++){
	//				$scope.pages[i]=i+1;
	//			}
	//		},error:function(error){
	//			console.log(error);
	//		}
	//	});
	//}


	$scope.queryByPage = function(_page){
		page = _page;
		var query = new Database.Query('CargoOut');
		query.skip((_page-1)*PER_NUM);
		query.limit(PER_NUM);
		if(flag == 1)
			query.equalTo(searchType,searchValue);
		query.find({
			success:function(results){
				console.log(results);
				$scope.cargoout = results;
				setPageActive(_page);
			},error:function(error){
				console.log("error");
				console.log(error);
			}
		});
		query.count({
			success:function(count){
				console.log(count);
				var totalPage = Math.ceil(count.number/PER_NUM);
				$scope.pages = [];
				for(var i = 0; i < totalPage; i++){
					$scope.pages[i]=i+1;
				}
			},error:function(error){
				console.log(error);
			}
		});
	};

	$scope.deleteItem = function (index) {
		if(Database.User.currentUser().role==='admin'){
			$scope.cargoout[index].delete({
				success:function (obj) {
					$scope.cargoout.splice(index,1);
					if($scope.cargoout.length==0&&page!=1){
						$scope.queryByPage(page-1);
					} else {
						$scope.queryByPage(page);
					}
					//countTotalPages();
				}
			});
		}
		else 
			alert("没有权限");
	}

	$scope.search = function () {
		flag = 1;
		searchType = $("#searchT").select2("val");
		searchValue = $("#searchV").val();
		console.log(searchType);
		console.log(searchValue);
		if(searchType == 'cargorank'){
			if(searchValue == 'A' || searchValue == 'B' || searchValue == 'C'|| searchValue == 'D'){
				$scope.queryByPage(page);
				setPageActive(page);
			}
			else{
				$("#alertRank").modal('show')
			}
		}
		else{
			$scope.queryByPage(page);
			setPageActive(page);
		}
	}

	$scope.searchAll = function () {
		flag = 0;
		$scope.queryByPage(page);
		//countTotalPages();
		setPageActive(page);
	}

	//var cargo = new Database.Object('Cargo',{
	// 	cargoname:"sdf",
	//	cargonumber:"sdf",
	//	cargosize:"sdf",
	//	cargoweight:"sdfs",
	//	cargoaddress:"xcv",
	//	cargorank:"sdf",
	//	cargoreminder:"xv"
	// });

	//cargo.save(null,{
	//	success:function(obj){
	//		console.log(obj);
	//	},error:function(err){
	//		console.log(err);
	//	}
	//});
	//var query = new Database.Query("Cargo");
	//query.equalTo('cargoname',"sdf");
	//query.equalTo('cargosize',"sdf");
	$scope.queryByPage(page);
	//countTotalPages();
	setPageActive(page);

}]);