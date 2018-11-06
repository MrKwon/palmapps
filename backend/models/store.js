module.exports = (sequelize, DataTypes) => {
  return sequelize.define('store_info' , {
    name: {
      type: DataTypes.STRING(32),
      allowNull: false,
    },
    desc: {
      type: DataTypes.STRING(200),
      allowNull: false,
    }
  },
  {
    timestamp: true,
    paranoid: true,
    tableName: true,
  });
};
