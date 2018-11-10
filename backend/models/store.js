module.exports = (sequelize, DataTypes) => {
  return sequelize.define('store_info' , {
    store_name: {
      type: DataTypes.STRING(32),
      allowNull: false,
    },
    store_type: {
      type: DataTypes.ENUM,
      values: ['식당', '카페'],
      allowNull: false,
    },
    desc: {
      type: DataTypes.STRING(200),
      allowNull: false,
    },
  }, {
    // timestamps: true,
    // paranoid: true,
    // tableName: true,
  });
};
