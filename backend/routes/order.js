const express = require('express');
const Sequelize = require('sequelize');
const { store_infos, member_infos, menupans, orderlists } = require('../models');
const Op = Sequelize.Op;

const router = express.Router();

// 주문을 등록하는 메서드
router.post('/send', async(req, res, next) => {

  const order_array = req.body;

  // console.log(req.body);
  // console.log(req.body.length);

  try{
    const user_info = await member_infos.find({
      attributes: ['id'],
      where: {
        email: order_array[0].email
      }
    });

    const username_id = user_info.id;

    for ( let i = 0; i < order_array.length; i++) {
      const { order_name, order_count, orderee_id, paytype } = order_array[i];

      const menu_id = await menupans.findOne({
        attributes: ['id'],
        where: {
          menu_name: order_name,
        }
      });
      // console.log(menu_id.id);

      await orderlists.create({
        menu_id: menu_id.id,
        count: order_count,
        orderer_id: username_id,
        paytype,
        orderee_id,
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
  // 요청자의 이메일 주소를 받아와서
  const req_email = req.body.email;
  // console.log(req_email);

  try {
    // 요청자의 이메일 주소에 해당하는 id 를 찾고
    const req_id_json = await member_infos.findOne({
      attributes: ['id'],
      where: {
        email: req_email,
      }
    });
    // console.log(req_id_json.id);

    const reqester_id = req_id_json.id; // orderer

    // 해당하는 id 가 orderer에 값으로 들어있는 모든 row 중에서
    // complete 속성이 0인 모든 row 들을 orderee orderer, menu, count, createdAt 을
    // JsonArray 형식으로 불러옴 - 여러개이기 때문에
    const user_orderlist = await orderlists.findAll({
      attributes: ['orderee_id', 'createdAt', 'menu_id', 'count' ],
      where: {
        orderer_id: reqester_id,
        complete: { [Op.eq]: 0 },
      }
    });

    const tmp_json_array = JSON.parse(JSON.stringify(user_orderlist));

    // 위에서 만든 JsonArray를 프론트에 보낼 배열의 형식에 맞게 값들을 찾아 재구성
    // ordered_store_name: store_infos.find({ attributes: ['store_name'], where: { id: orderee } });
    // ordered_time : createdAt
    // ordered_menu_name : menupans.find({ attributes: ['menu_name'], where: { id: menu_id } });
    // ordered_count : count
    // ordered_price : menupans.find({ attributes: ['menu_price'], where: { id: menu_id } });
    //                  * count
    let result_json_array = [ ];
    for(let i = 0; i < tmp_json_array.length; i++ ) {
      // console.log(">>>>>>>>>>>>>", i);
      const { orderee_id, createdAt, menu_id, count } = tmp_json_array[i];
      const tmp_store_name = await store_infos.findOne({
        attributes: ['store_name'],
        where: {
          id: orderee_id
        }
      });
      const ordered_store_name = JSON.parse(JSON.stringify(tmp_store_name)).store_name;
      // console.log(ordered_store_name);

      const tmp_menu_info = await menupans.findOne({
        attributes: ['menu_name', 'menu_price'],
        where: {
          id: menu_id
        }
      });
      const ordered_menu_name = JSON.parse(JSON.stringify(tmp_menu_info)).menu_name;
      const ordered_menu_price = JSON.parse(JSON.stringify(tmp_menu_info)).menu_price;

      const to_result_json = {
        ordered_store_name,
        ordered_time: createdAt,
        ordered_menu_name,
        ordered_count: count,
        ordered_price: ordered_menu_price * count,
      }

      // console.log(to_result_json);

      result_json_array.push(to_result_json);
    }

    const json = JSON.stringify(result_json_array);
    // console.log(reqester_orderlist.body);
    // console.log(reqester_orderlist.params);

    // const json = JSON.stringify(req_orderlist);
    return res.end(json);

  } catch(error) {
    console.error(error);
    next(error);
  }
});

router.post('/pastorderlist', async(req, res, next) => {
  // 요청자의 이메일 주소를 받아와서
  const req_email = req.body.email;
  // console.log(req_email);

  try {
    // 요청자의 이메일 주소에 해당하는 id 를 찾고
    const req_id_json = await member_infos.findOne({
      attributes: ['id'],
      where: {
        email: req_email,
      }
    });
    // console.log(req_id_json.id);

    const reqester_id = req_id_json.id; // orderer

    // 해당하는 id 가 orderer에 값으로 들어있는 모든 row 중에서
    // complete 속성이 0인 모든 row 들을 orderee orderer, menu, count, createdAt 을
    // JsonArray 형식으로 불러옴 - 여러개이기 때문에
    const user_orderlist = await orderlists.findAll({
      attributes: ['orderee_id', 'createdAt', 'menu_id', 'count' ],
      where: {
        orderer_id: reqester_id,
        complete: { [Op.eq]: 1 },
      }
    });

    const tmp_json_array = JSON.parse(JSON.stringify(user_orderlist));

    // 위에서 만든 JsonArray를 프론트에 보낼 배열의 형식에 맞게 값들을 찾아 재구성
    // ordered_store_name: store_infos.find({ attributes: ['store_name'], where: { id: orderee } });
    // ordered_time : createdAt
    // ordered_menu_name : menupans.find({ attributes: ['menu_name'], where: { id: menu_id } });
    // ordered_count : count
    // ordered_price : menupans.find({ attributes: ['menu_price'], where: { id: menu_id } });
    //                  * count
    let result_json_array = [ ];
    for(let i = 0; i < tmp_json_array.length; i++ ) {
      // console.log(">>>>>>>>>>>>>", i);
      const { orderee_id, createdAt, menu_id, count } = tmp_json_array[i];
      const tmp_store_name = await store_infos.findOne({
        attributes: ['store_name'],
        where: {
          id: orderee_id
        }
      })
      const ordered_store_name = JSON.parse(JSON.stringify(tmp_store_name)).store_name;
      // console.log(ordered_store_name);

      const tmp_menu_info = await menupans.findOne({
        attributes: ['menu_name', 'menu_price'],
        where: {
          id: menu_id
        }
      });
      const ordered_menu_name = JSON.parse(JSON.stringify(tmp_menu_info)).menu_name;
      const ordered_menu_price = JSON.parse(JSON.stringify(tmp_menu_info)).menu_price;

      const to_result_json = {
        ordered_store_name,
        ordered_time: createdAt,
        ordered_menu_name,
        ordered_count: count,
        ordered_price: ordered_menu_price * count,
      }

      // console.log(to_result_json);

      result_json_array.push(to_result_json);
    }

    const json = JSON.stringify(result_json_array);
    return res.end(json);

  } catch(error) {
    console.error(error);
    next(error);
  }
});

router.patch('/complete/:id', async(req, res, next) => {
  const order_id = req.params.id;

  try {
    const result = 0;
  } catch (error) {
    console.error(error);
    next(error);
  }
});

module.exports = router;
