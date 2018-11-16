const express = require('express');
const { store_infos, menupans } = require('../../models');

const router = express.Router();

router.get('/:store_id', async(req, res, next) => {
  const store_id = req.params.store_id;
  console.log(req.params);
  console.log(store_id);

  try {
    const store_name = await store_infos.find({
      attributes: ['store_name'],
      where: {
        id: store_id,
      }
    });
    const json_name = JSON.stringify(store_name);
    console.log(json_name);

    return res.end(json_name);

  } catch(error) {
    console.error(error);
    next(error);
  }

});

router.get('/:store_id/menupans', async(req, res, next) => {
  const store_id = req.params.store_id;
  console.log(req.params);
  console.log(store_id);

  try {
    const store_menupans = await menupans.findAll({
      where: {
        store_name_id: store_id,
      }
    });

    const json_menupans = JSON.stringify(store_menupans);
    console.log(json_menupans);

    return res.end(json_menupans);

  } catch (error) {
    console.error(error);
    next(error);
  }
})

module.exports = router;
