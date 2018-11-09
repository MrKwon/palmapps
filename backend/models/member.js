module.exports = (sequelize, DataTypes) => {
  return sequelize.define('member_info', {
    email: {
      type: DataTypes.STRING(50),
      allowNull: false,
      unique: true,
    },
    password: {
      type: DataTypes.STRING(100),
      allowNull: false,
    },
    username: {
      type:DataTypes.STRING(30),
      allowNull: false,
    },
    nickname: {
      type: DataTypes.STRING(30),
      allowNull: false,
    },
    phone: {
      type: DataTypes.STRING(20),
      allowNull: false,
    },
    sex: {
      type: DataTypes.ENUM,
      values: ['남자', '여자'],
      allowNull: false,
    },
    runcount: {
      type: DataTypes.INTEGER.UNSIGNED,
      allowNull: false,
      defaultValue: 0,
    }
  }, {
    // timestamps: true,
    // paranoid: true,
    // tableName: true,
  });
};
