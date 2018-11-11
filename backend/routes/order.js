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
        username: orderArray[0].username
      }
    });

    const username_id = userinfo.id;

    for ( let i = 0; i < orderArray.length; i++) {
      const { order_name, order_count, orderee } = orderArray[i];
      await orderlists.create({
        menu: order_name,
        count: order_count,
        orderer: username_id,
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

module.exports = router;
