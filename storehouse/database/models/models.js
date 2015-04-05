var mongoose = require('mongoose');
//获取 schemas
var CargoSchema = require('../schemas/cargo');
var CargoOutSchema = require('../schemas/cargoout');
var UserSchema = require('../schemas/user');



var Models = {};
//获取 Models
Models.Cargo = mongoose.model('Cargo',CargoSchema);
Models.CargoOut = mongoose.model('CargoOut',CargoOutSchema);
Models.User = mongoose.model('User',UserSchema);


//根据className获取model
Models.getClass = function(className){
	if(className=='Cargo')
		return Models.Cargo;
	if(className=='CargoOut')
		return Models.CargoOut;
	if(className=="User")
		return Models.User;
}


module.exports = Models;