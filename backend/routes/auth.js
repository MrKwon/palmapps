const express = require('express');
const bcrypt = require('bcryptjs');
const { member_infos } = require('../models');

const router = express.Router();

/**
 * 사용자가 이메일을 확인하기 위해 요청하는 Router
 * 해당 이메일이 가입되어 있지 않으면 impossible을 res에 json 형식으로 보내고
 * 해당 이메일이 가입되어 있으면 possible을 res에 json 형식으로 보낸다
 */
router.get('/isPossibleId/:email', async(req, res, next) => {
  const email = req.params.email;
  console.log("check : ", req.params);

  try {
    const exEmail = await member_infos.find({
      where: { email }
    });

    if (exEmail) {
      const json = JSON.stringify({
        state: "impossible"
      });
      return res.end(json);

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
  const { email, password, username, nickname, phone, sex } = req.body;

  console.log(email, password, username, nickname, phone, sex);

  try {
    const exUser = await member_infos.find({ where: { email } });
    if (exUser) {
      const json = JSON.stringify({
        state: "impossible"
      });

      return res.end(json);
    }
    const hash = await bcrypt.hash(password, 12);
    console.log(hash);
    await member_infos.create({
      email,
      password: hash,
      username,
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

router.post('/signin', async(req, res, next) => {
  const { email, password } = req.body;
  // const email = req.body.email;
  // const password = req.body.password;

  // console.log(req.body);
  console.log(email, password);

  try {
    const exUser = await member_infos.find({ where: { email } });
    if(exUser) {
      // const hash = await bcrypt.hash(password, 12);
      const email = exUser.email;
      const username = exUser.username;
      const nickname = exUser.nickname;
      const result = await bcrypt.compare(password, exUser.password);
      if (result) {
        const json = JSON.stringify({
          state: "success",
          email,
          username,
          nickname,
        });
        return res.end(json);

      } else {
        const json = JSON.stringify({
          state: "pwerror"
        });
        return res.end(json);
      }

    } else {
      const json = JSON.stringify({
        state: "notexist"
      });
      return res.end(json);
    }

  } catch (error) {
    console.error(error);
    next(error);
  }
});

module.exports = router;
