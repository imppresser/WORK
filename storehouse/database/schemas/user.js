var mongoose = require('mongoose');
var BaseSchema = require('./baseschema');
var _ = require('underscore');

var UserSchema = new mongoose.Schema(_.extend({	
		username:String,
		password:String,
		role:String
},BaseSchema.data));

//每次存储数据前都会调用这个方法
UserSchema.pre('save', function(next){
	BaseSchema.preSave(this);
	next();
});

//静态函数
UserSchema.statics = BaseSchema.statics;

module.exports = UserSchema;