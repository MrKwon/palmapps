module.exports = (sequelize, DataTypes) => {
  return sequelize.define('menupan', {
    menu_name: {
      type: DataTypes.STRING(10),
      allowNull: false,
    },
    menu_price: {
      type: DataTypes.STRING(10),
      allowNull: false,
    },
    menu_img: {
      type: DataTypes.STRING,
      allowNull: true,
      dafaultValue: "Null",
    },
    menu_desc: {
      type: DataTypes.STRING(500),
      allowNull: true,
      defaultValue: "없음"
    },
  }, {
    timestamps: false,
  });
};
