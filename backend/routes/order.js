const express = require('express');
const { store_infos, member_infos, menupans, orderlists } = require('../models');

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
      await orderlists.create({
        menu: order_name,
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
router.get('/noworderlist', async(req, res, next) => {
  const requester_email = req.params.email;
  console.log(requester_email);

  try {
    const requester_id = await member_infos.findOne({
      attributes: ['id'],
      where: {
        email: requester_email,
      }
    });
    console.log(requester_id);

    const requester_orderlist = await orderlists.findAll({
      where: {
        orderer: requester_id.id,
        complete: { [Op.ne] : 1 },
      }
    });
    console.log(requester_orderlist);

    const json = JSON.stringify(requester_orderlist);
    return res.end(json);
  } catch(error) {
    console.error(error);
    next(error);
  }
});

router.get('pastorderlist', async(req, res, next) => {

});

module.exports = router;
