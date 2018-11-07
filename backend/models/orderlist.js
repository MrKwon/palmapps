module.exports = (sequelize, DataTypes) => {
  return sequelize.define('orderlist', {
    menu: {
      type: DataTypes.STRING,
      allowNull: false,

    },
    count: {
      type: DataTypes.INTEGER.UNSIGNED,
      allowNull: false,

    },
    complete: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: 0,
    },
  }, {
    timestamp: true,
    paranoid: true,
  });
};
