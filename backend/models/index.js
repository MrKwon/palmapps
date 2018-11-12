const path = require('path');
const Sequelize = require('sequelize');

const env = process.env.NODE_ENV || 'development';
const config = require(path.join(__dirname, '..', 'config', 'config.json'))[env];
const db = {};

const sequelize = new Sequelize(config.database, config.username, config.password, config);

db.sequelize = sequelize;
db.Sequelize = Sequelize;

db.member_infos = require('./member')(sequelize, Sequelize);
db.store_infos = require('./store')(sequelize, Sequelize);
db.orderlists = require('./orderlist')(sequelize, Sequelize);
db.menupans = require('./menu')(sequelize, Sequelize);

// 아래의 모든 릴레이션들에 _id를 붙여야 할 것 같음
// 가독성이 너무 떨어짐

// orderlist 에는 orderer column에 member의 id 가 foreignKey로 등록됨
db.member_infos.hasMany(db.orderlists, { foreignKey: 'orderer', sourceKey: 'id' });
db.orderlists.belongsTo(db.member_infos, { foreignKey: 'orderer', targetKey: 'id' });

// orderlist 에는 orderee column에 store의 id 가 foreignKey로 등록됨
db.store_infos.hasMany(db.orderlists, { foreignKey: 'orderee', sourceKey: 'id' });
db.orderlists.belongsTo(db.store_infos, { foreignKey: 'orderee', targetKey: 'id' });

// store_info 하나가 menupan에 여러개의 store_name 들을 가지고 있음
db.store_infos.hasMany(db.menupans, { foreignKey: 'store_name', sourceKey: 'id' });
db.menupans.belongsTo(db.store_infos, { foreignKey: 'store_name', targetKey: 'id' });

// menupans id 하나가 orderlist에 여러 개의 order_name 들을 가지고 있음
db.menupans.hasMany(db.orderlists, { foreignKey: 'menu_id', sourceKey: 'id' });
db.orderlists.belongsTo(db.menupans, { foreignKey: 'menu_id', sourceKey: 'id' });

module.exports = db;
