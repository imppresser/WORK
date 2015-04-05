app.controller("headerCtrl", ['$scope', 'Util','Database',
function($scope, Util, Database) {
	//Database.User.logout();
	//Database.User.logout();
	$scope.user = Database.User.currentUser();
	//$scope.user.delete();
	/*$scope.user.resetPassword("12345",{
		success:function(_user){
			console.log(_user);
			alert("reset ok")
		}
	});*/

	$scope.loginDialog = function(){
		$("#login").modal('show')
	}
	$scope.signupDialog = function(){
		$("#signup").modal('show')
	}
	$scope.modifypasswordDialog = function(){
		$("#modifypassword").modal('show')
	}

	$scope.login = function(){
		var username = $("#username0").val();
		var password = $("#password0").val();
		Database.User.login(username,password,{
			success:function(user){
				$scope.user = user;
			},error:function(err){
				alert(err.error);
			}
		})
	};
	$scope.signup = function(){
		var username = $("#username1").val();
		var password = $("#password1").val();
		var role =$("input[name='role']:checked").val();	
		Database.User.signup(role,username,password,{
			success:function(user){
				//注册成功
			},error:function(err){
				//注册失败
				alert(err.error);
			}
		});
	}
	$scope.logout = function(){
		Database.User.logout();
		$scope.user = null;
	}
	$scope.modifypassword = function(){
		var password = $("#password2").val();
		console.log(password);
		$scope.user.modifyPassword(password,{
		success:function(_user){
			alert("密码修改成功")
		}
		});
	}

}]);