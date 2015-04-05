var express = require('express');
var _ = require('underscore');
var port = process.env.PORT || 80;
//处理post中的data数据
var bodyParser = require('body-parser');
//使用mongoose作为数据库
var mongoose = require('mongoose');
var app = express();
//连接本地数据库storeshouse
 mongoose.connect('mongodb://localhost/storeshouse');

var Models = require('./database/models/models');

//使前端post来的数据能从req.body中取得
app.use(bodyParser.urlencoded({extended: false}));
app.use(express.static(__dirname));
//设置视图根目录
app.set('views', './views/pages');
//设置默认模版引擎
app.set('view engine', 'jade');
// app.set('view options', { pretty: true });

//监听端口
app.listen(port);

console.log('storeshouse started on port '+port);

// 编写路由
// get(路由匹配规则,回调方法)
// 从后端匹配到路由匹配规则,调用回调方法


// index page
app.get('/',function(req, res) {
	res.render('index', {
	title: 'storeshouse',
	});
	res.end();
});

app.get('/addin',function(req, res) {
	res.render('addin', {
		title: 'storeshouse'
	});
});

app.get('/out',function(req, res) {
	res.render('out', {
		title: 'storeshouse'
	});
});

app.get('/modify/:id',function(req, res) {
	var id = req.params.id;

	res.render('modify', {
		title: 'storeshouse',
		bid: id
	});
	res.end();
});

app.get('/detail/:id',function(req, res) {
	var id = req.params.id;

	res.render('detail', {
		title: 'storeshouse',
		bid: id
	});
	res.end();
});

app.get('/detailout/:id',function(req, res) {
	var id = req.params.id;

	res.render('detailout', {
		title: 'storeshouse',
		bid: id
	});
	res.end();
});










//数据库操作
app.get('/findData',function(req,res){
	var query = req.query.data;
	query = JSON.parse(query);
	var className = query.className;
	var Class = Models.getClass(className);
	if(Class===undefined||className=="User"){
		res.status(404).send({error:'can not find class '+className});
		res.end();
		return ;
	}

	Class.fetch(query,function(err,results){
		if(err){
			res.status(404).send({error:err,object:results});
			res.end();
			return ;
		}

		_.each(results,function(obj,index){
			var _obj = {};
			_obj.attributes = obj;
			_obj.className = query.className;
			results[index] = _obj;
		});
		res.send(results);
		res.end();
	})
	
});

app.get('/getData',function(req,res){
	var query = req.query.data;
	query = JSON.parse(query);
	var className = query.className;
	var Class = Models.getClass(className);
	if(Class===undefined||className=="User"){
		res.status(404).send({error:'can not find Class '+className});
		res.end();
		return ;
	}
	if(query._id===undefined){
		res.status(404).send({error:'can not find Object without id.'});
		res.end();
		return ;
	}
	Class.findById(query._id, function(err,result){
		if(err){
			res.status(404).send({error:err,object:result});
			res.end();
			return ;
		}
		
		res.send({className:className,attributes:result});
		res.end();
	});
});

app.get('/count',function(req,res){
	var query = req.query.data;
	query = JSON.parse(query);
	var className = query.className;
	var Class = Models.getClass(className);
	if(Class===undefined){
		res.status(404).send({error:'can not find Class '+className});
		res.end();
		return ;
	}

	Class.count(query.equal, function(err,result){
		if(err){
			res.status(404).send({error:err,object:result});
			res.end();
			return ;
		}
		res.send({className:className,number:result});
		res.end();
	});
});



app.post('/save',function(req,res){
	var obj = req.body.data;
	obj = JSON.parse(obj);
	var className = obj.className;
	var Class = Models.getClass(className);
	if(Class===undefined){
		res.status(404).send({error:'can not find Class '+className});
		res.end();
		return ;
	}
	var _obj;
	if(obj.attributes._id !== undefined) {
		Class.findById(obj.attributes._id, function(err,result){
			if(err){
				res.status(404).send({error:err,object:obj});
				res.end();
				return ;
			}
			_obj = _.extend(result, obj.attributes);
			_obj.save(function(err,obj){
				if(err){
					res.status(404).send({error:err,object:obj});
					res.end();
					return ;
				}
				res.send({className:className,attributes:obj});
				res.end();
				});
		});
	} else {
		_obj = new Class(obj.attributes);
		_obj.save(function(err,obj){
			if(err){
				res.status(404).send({error:err,object:obj});
				res.end();
				return ;
			}
			res.send({className:className,attributes:obj});
			res.end();
		});
	}
});


app.post('/removeById',function(req,res){
	var obj = req.body.data;
	obj = JSON.parse(obj);
	var className = obj.className;
	var Class = Models.getClass(className);
	if(Class===undefined){
		res.status(404).send({error:'can not find Class '+className});
		res.end();
		return ;
	}
	if(obj.attributes._id !== undefined) {
		Class.remove({_id:obj.attributes._id},function(err,deleteOk){
			if(err){
				res.status(404).send({error:err,object:deleteOk});
				res.end();
				return ;
			}
			res.send({className:className,attributes:deleteOk});
			res.end();
		});
	} else {
		res.status(404).send({error:'can not find Object without id.'});
		res.end();
		return ;
	}
});

//注册
app.post('/signup',function(req,res){
	var user = req.body.data;
	// user{username, password, role}
	user = JSON.parse(user);
	var className = "User";
	var Class = Models.getClass(className);
	if(Class===undefined){
		res.status(404).send({error:'can not find Class '+className});
		res.end();
		return ;
	}
	//查找是否有同名用户存在
	var query = {};
	query.equal = {};
	query.equal.username = user.username;
	Class.fetch(query,function(err,results){
		if(err){
			res.status(404).send({error:err,object:results});
			res.end();
			return ;
		}
		//存在同名用户
		if(results.length > 0){
			res.status(404).send({error:"username existed"});
			res.end();
			return ;
		} else {
			//不存在 注册
			var _obj = new Class(user);
			_obj.save(function(err,user){
				if(err){
					res.status(404).send({error:err});
					res.end();
					return ;
				}
				//注册成功 返还success
				res.send("success");
				res.end();
			});
		}
	})
});

app.get('/login',function(req,res){
	var user = req.query.data;
	// user username password role
	user = JSON.parse(user);
	var className = "User";
	var Class = Models.getClass(className);
	if(Class===undefined){
		res.status(404).send({error:'can not find class '+className});
		res.end();
		return ;
	}
	var query = {equal:user};
	Class.fetch(query,function(err,results){
		if(err){
			res.status(404).send({error:err});
			res.end();
			return ;
		}
		if(results.length > 0){
			var result = results[0];
			delete result.password;
			res.send(result);
			res.end();
		} else {
			res.status(404).send({error:"username or password error"});
			res.end();
			return ;
		}
	})
	
});