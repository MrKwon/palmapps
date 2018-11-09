const express = require('express');
const bcrypt = require('bcryptjs');
const { member_infos } = require('../models');

const router = express.Router();

router.get('/isPossibleId/:email', async(req, res, next) => {
  const email = req.params.email;

  try {
    const exEmail = await member_infos.find({
      where: { email }
    });

    if (exEmail) {
      console.log("impossible");
      return res.error();
    } else {
      console.log("possible");

      const json = JSON.stringify({
        state: "possible"
      });

      return res.end(json);
    }

  } catch(error) {
    console.error(error);
    return next(error);
  }
});

router.post('/signup', async(req, res, next) => {
  const { email, password, name, nickname, phone, sex } = req.params;

  try {
    const hash = await bcrypt.hash(password, 12);
    await member_infos.create({
      email,
      password: hash,
      name,
      nickname,
      phone,
      sex
    });

    const json = JSON.stringify({
      state: "success"
    });

    return res.end(json);

  } catch (error) {
    console.error(error);
    return next(error);
  }
});

router.get('/signin/:email', async(req, res, next) => {
  const { email, password } = req.params;

  try {
    const hash = await bcrypt.hash(password, 75);
    const db_password = await member_infos.find({
      attributes: 'password',
      where: {
        email
      }
    });

    if (hash == db_password) {
      const db_info = await member_infos.find({
        where: {
          email
        }
      });
      return res.json.db_info;
    }
  } catch (error) {
    console.error(error);
    next(error);
  }
});

module.exports = router;
