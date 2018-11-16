const express = require('express');
const { store_infos } = require('../../models');

const router = express.Router();

router.post('/', async(req, res, next) => {
  const beacon_info = req.body.store_id;
  console.log(beacon_info);

  try {
    const store_name = await store_infos.findOne({
      attributes: ['id'],
      where: {
        store_beacon_info: beacon_info
      }
    });

    const json = JSON.stringify(store_name);

    return res.end(json);

  } catch (error) {
    console.error(error);
    next(error);
  }
});

module.exports = router;
