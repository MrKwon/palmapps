const express = require('express');
const { store_infos } = require('../models');

const router = express.Router();

router.get('/', async(req, res, next) => {
  try{
    const allPartners = await store_infos.findAll({
      attributes: ['store_name', 'store_type', 'desc'],
    });

    const json = JSON.stringify(allPartners);
    // console.log(json);

    return res.end(json);

  } catch (error) {
    console.log(error);
    next(error);
  }
});

// PalmPos에서 메뉴판 등록하는 라우터
// router.post('/enroll', async(req, res, next) => {
//   const { store_name, store_type, desc } = req.body;
//   try {
//     await.create({
//       store_name,
//       store_type,
//       desc,
//     });
//
//     return res.end();
//
//   } catch (error) {
//     console.log(error);
//     next(error);
//   }
// });

module.exports = router;
