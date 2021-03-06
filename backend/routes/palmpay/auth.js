const express = require('express');
const bcrypt = require('bcryptjs');
const { member_infos } = require('../../models');

const router = express.Router();

/**
 * 사용자가 이메일을 확인하기 위해 요청하는 Router
 * 해당 이메일이 가입되어 있지 않으면 impossible을 res에 json 형식으로 보내고
 * 해당 이메일이 가입되어 있으면 possible을 res에 json 형식으로 보낸다
 */
router.get('/isPossibleId/:email', async(req, res, next) => {
  // 클라이언트에서 요청한 확인하고자 하는 email 주소
  const email = req.params.email;
  console.log("check : ", req.params);

  // async/await 방식 처리
  try {
    // 먼저 member_infos 테이블에서 요청에서 받은 email 주소가 있는지 확인
    // 존재한다면 값이 저장될 것
    const exEmail = await member_infos.find({
      where: { email }
    });

    // exEmail 이 존재하면 state 속성에 impossible을 넣어 전송
    if (exEmail) {
      const json = JSON.stringify({
        state: "impossible"
      });
      return res.end(json);

    // exEmail 이 존재하지 않으면 possible을 넣어 전송
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
