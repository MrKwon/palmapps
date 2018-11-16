const express = require('express');
const Sequelize = require('sequelize');
const { store_infos, member_infos, menupans, orderlists } = require('../../models');
const Op = Sequelize.Op;

const router = express.Router()

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
