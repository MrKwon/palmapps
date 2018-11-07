const path = require('path');
const Sequelize = require('sequelize');

const env = process.env.NODE_ENV || 'development';
const config = require(path.join(__dirname, '..', 'config', 'config.json'))[env];
const db = {};

const sequelize = new Sequelize(config.database, config.username, config.password, config);

db.sequelize = sequelize;
db.Sequelize = Sequelize;

db.member_info = require('./member')(sequelize, Sequelize);
db.store_info = require('./store')(sequelize, Sequelize);
db.orderlist = require('./orderlist')(sequelize, Sequelize);

// // orderlist 에는 orderer column에 member의 id 가 foreignKey로 등록됨
// db.member_info.hasMany(db.orderlist, { foreignKey: 'orderer', sourceKey: 'id' });
// db.orderlist.belongsTo(db.member_info, { foreignKey: 'orderer', targetKey: 'id' });
//
// // orderlist 에는 orderee column에 store의 id 가 foreignKey로 등록됨
// db.store_info.hasMany(db.orderlist, { foreignKey: 'orderee', sourceKey: 'id' });
// db.orderlist.belongsTo(db.store_info, { foreignKey: 'orderee', targetKey: 'id' });

module.exports = db;
