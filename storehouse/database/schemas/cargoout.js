var mongoose = require('mongoose');
var BaseSchema = require('./baseschema');
var _ = require('underscore');

var CargoOutSchema = new mongoose.Schema(_.extend({	
		cargoname:String,
		cargonumber:String,
		cargosize:String,
		cargoweight:String,
		cargoaddress:String,
		cargorank:String,
		cargoreminder:String,
		cargooutdate:String
},BaseSchema.data));

//每次存储数据前都会调用这个方法
CargoOutSchema.pre('save', function(next){
	BaseSchema.preSave(this);
	next();
});

//静态函数
CargoOutSchema.statics = BaseSchema.statics;

module.exports = CargoOutSchema;