module.exports = (sequelize, DataTypes) => {
  return sequelize.define('store_info' , {
    store_admin: {
      type: DataTypes.STRING(50),
      allowNull: false,
      unique: true,
    },
    store_admin_pw: {
      type: DataTypes.STRING(100),
      allowNull: false,
    },
    store_name: {
      type: DataTypes.STRING(32),
      allowNull: false,
    },
    store_type: {
      type: DataTypes.ENUM,
      values: ['식당', '카페'],
      allowNull: false,
    },
    store_desc: {
      type: DataTypes.STRING(200),
      allowNull: false,
    },
    store_beacon_info: {
      type: DataTypes.STRING(100),
      allowNull: true,
    }
  }, {
    // timestamps: true,
    // paranoid: true,
    // tableName: true,
  });
};
