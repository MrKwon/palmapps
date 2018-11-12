const express = require('express');
const Sequelize = require('sequelize');
const { store_infos, member_infos, menupans, orderlists } = require('../models');
const Op = Sequelize.Op;

const router = express.Router();

// 주문을 등록하는 메서드
router.post('/send', async(req, res, next) => {

  const orderArray = req.body;

  console.log(req.body);
  console.log(req.body.length);

  try{
    const userinfo = await member_infos.find({
      attributes: ['id'],
      where: {
        email: orderArray[0].email
      }
    });

    const username_id = userinfo.id;

    for ( let i = 0; i < orderArray.length; i++) {
      const { order_name, order_count, orderee, paytype } = orderArray[i];

      const menu_id = await menupans.findOne({
        attributes: ['id'],
        where: {
          menu_name: order_name,
        }
      });
      console.log(menu_id.id);

      await orderlists.create({
        menu: menu_id.id,
        count: order_count,
        orderer: username_id,
        paytype,
        orderee
      });
    }

    const json = JSON.stringify({
      state: "success"
    });

    return res.end(json);

  } catch (error) {
    console.error(error);
    next(error);
  }
});

// 주문 내역을 가져오는 메서드
router.post('/noworderlist', async(req, res, next) => {
  const req_email = req.body.email;
  console.log(req_email);

  try {
    const req_id_json = await member_infos.findOne({
      attributes: ['id'],
      where: {
        email: req_email,
      }
    });
    console.log(req_id_json.id);

    const req_id = req_id_json.id;

    const req_orderlist = await orderlists.findAll({
      attributes: [''],
      where: {
        orderer: req_id,
        complete: { [Op.eq]: 0 },
      }
    });
    console.log(req_orderlist);

    console.log(req_orderlist.body);
    console.log(req_orderlist.params);

    const json = JSON.stringify(req_orderlist);
    return res.end();

  } catch(error) {
    console.error(error);
    next(error);
  }
});

// router.get('/pastorderlist', async(req, res, next) => {
//
// });

module.exports = router;
