module.exports = (sequelize, DataTypes) => {
  return sequelize.define('orderlist', {
    menu: {
      type: DataTypes.STRING(10),
      allowNull: false,
    },
    count: {
      type: DataTypes.INTEGER.UNSIGNED,
      allowNull: false,
    },
    paytype: {
      type: DataTypes.ENUM,
      values: ['palmcredit', 'card', 'coupon', 'account'],
      allowNull: false,
    },
    complete: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: 0,
    },
  }, {
    // timestamps: true,
    // paranoid: true,
  });
};
